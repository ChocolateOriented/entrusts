
package com.entrusts.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.entrusts.module.dto.TimePage;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.vo.CurrentEntrusts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DealNotify.OrderDealDetail;
import com.entrusts.module.dto.Page;
import com.entrusts.module.entity.Order;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.util.RedisUtil;
import com.entrusts.module.vo.OrderQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

@Service
public class OrderManageService extends BaseService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private TradePairService tradePairService;

	private static final String historyOrderPrefix = "com.entrusts.service.OrderManageService";

	private static final String currentOrderPrefix = "com.entrusts.service.CurrentOrderManageService";

	private static final String currentOrderUserKey = currentOrderPrefix + ".currentuserDate.";

	public static final String totalCurrentOrderUserKey = currentOrderPrefix + ".currentuserTotalData.";

	private static final String historyOrderUserKey = historyOrderPrefix + ".userData.";

	private static final String totalHistoryOrderUserKey = historyOrderPrefix + ".userTotalData.";

	private static final String historyCacheUserHitCountKey = historyOrderPrefix + ".userCacheHitCount";

	private static final int perUserOrderCacheLimit = 50;

	private static final int userCacheLimit = 20000;

	public Order get(String orderCode) {
		return orderMapper.get(orderCode);
	}

	/**
	 * 查询用户历史托单分页
	 * @param orderQuery
	 * @param pageNum
	 * @param PageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<HistoryOrderView> findHistoryOrderByPage(OrderQuery orderQuery, int pageNum, int PageSize) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String userKey = historyOrderUserKey + userCode;
		int requireNum = pageNum * PageSize;
		String totalValue = RedisUtil.get(userTotalKey);
		Map<String, String> cache = RedisUtil.getMap(userKey);
		logger.info(userCode + "用户历史托单总数量缓存：" + totalValue);
		
		//无缓存
		if (totalValue == null || cache == null) {
			if (requireNum > perUserOrderCacheLimit) { //若需要的数据量大于缓存上限，则直接从数据库获取
				return findHistoryOrderByPageFromDB(orderQuery, pageNum, PageSize);
			} else if (!orderQuery.hasCondition()) { //若需要的数据量不大于缓存上限，且不含查询条件，则获取上限数据并缓存
				return findAndCacheLimitHistoryOrder(userCode, pageNum, PageSize);
			} else { //有查询条件无缓存时，直接从数据库获取
				return findHistoryOrderByPageFromDB(orderQuery, pageNum, PageSize);
			}
		}
		
		int total = Integer.parseInt(totalValue);
		//缓存的为全量数据，或者不含查询条件且需要的数据量不大于缓存上限时，从缓存获取，否则从数据库获取
		if (total <= perUserOrderCacheLimit || (requireNum <= perUserOrderCacheLimit && !orderQuery.hasCondition())) {
			List<HistoryOrderView> orders = findHistoryOrderFromRedis(orderQuery, cache);
			Page<HistoryOrderView> page = new Page<>();
			int start = Math.min(orders.size(), (pageNum - 1) * PageSize);
			int end = Math.min(orders.size(), requireNum);
			page.setEntities(orders.subList(start, end));
			page.setTotal((long) orders.size());
			page.setPageNum(pageNum);
			page.setPageSize(PageSize);
			return page;
		} else {
			return findHistoryOrderByPageFromDB(orderQuery, pageNum, PageSize);
		}
		
	}

	/**
	 * 查询用户历史托单时间分页
	 * @param orderQuery
	 * @param limit
	 * @return
	 */
	@Transactional(readOnly = true)
	public TimePage<HistoryOrderView> findHistoryOrderByTime(OrderQuery orderQuery, int limit) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String userKey = historyOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		Map<String, String> cache = RedisUtil.getMap(userKey);
		logger.info(userCode + "用户历史托单总数量缓存：" + totalValue);
		
		//无缓存
		if (totalValue == null || cache == null) {
			if (limit > perUserOrderCacheLimit) { //若需要的数据量大于缓存上限，则直接从数据库获取
				return findHistoryOrderByTimeFromDB(orderQuery, limit);
			} else if (!orderQuery.hasCondition()) { //若需要的数据量不大于缓存上限，且不含查询条件，则获取上限数据并缓存
				return findAndCacheLimitHistoryOrder(userCode, limit);
			} else { //有查询条件无缓存时，直接从数据库获取
				return findHistoryOrderByTimeFromDB(orderQuery, limit);
			}
		}
		
		int total = Integer.parseInt(totalValue);
		//缓存的为全量数据，或者不含查询条件且需要的数据量不大于缓存上限时，从缓存获取，否则从数据库获取
		if (total <= perUserOrderCacheLimit || (limit <= perUserOrderCacheLimit && !orderQuery.hasCondition())) {
			List<HistoryOrderView> orders = findHistoryOrderFromRedis(orderQuery, cache);
			TimePage<HistoryOrderView> page = new TimePage<>();
			page.setEntities(orders.subList(0, Math.min(limit, orders.size())));
			page.setTotal((long) orders.size());
			page.setLimit(limit);
			return page;
		} else {
			return findHistoryOrderByTimeFromDB(orderQuery, limit);
		}
		
	}

	/**
	 * 从数据库查询用户历史托单分页
	 * @param orderQuery
	 * @param pageNum
	 * @param PageSize
	 * @return
	 */
	private Page<HistoryOrderView> findHistoryOrderByPageFromDB(OrderQuery orderQuery, int pageNum, int PageSize) {
		Page<HistoryOrderView> page = new Page<>();
		PageHelper.startPage(pageNum, PageSize);
		List<HistoryOrderView> orders = orderMapper.findHistoryOrderByPage(orderQuery);
		PageInfo<HistoryOrderView> pageInfo = new PageInfo<>(orders);
		page.setEntities(orders);
		page.setTotal(pageInfo.getTotal());
		page.setPageNum(pageInfo.getPageNum());
		page.setPageSize(pageInfo.getPageSize());
		return page;
	}

	/**
	 * 从数据库查询用户历史托单时间分页
	 * @param orderQuery
	 * @param limit
	 * @return
	 */
	private TimePage<HistoryOrderView> findHistoryOrderByTimeFromDB(OrderQuery orderQuery, int limit) {
		TimePage<HistoryOrderView> page = new TimePage<>();
		long total = orderMapper.countHistoryOrderByTime(orderQuery);
		List<HistoryOrderView> orders = null;
		if (total > 0) {
			orders = orderMapper.findHistoryOrderByTime(orderQuery, limit);
		} else {
			orders = new ArrayList<>();
		}
		page.setEntities(orders);
		page.setTotal(total);
		page.setLimit(limit);
		return page;
	}

	/**
	 * 从缓存查询用户历史托单分页
	 * @param orderQuery
	 * @return
	 */
	private List<HistoryOrderView> findHistoryOrderFromRedis(OrderQuery orderQuery, Map<String, String> cache) {
		String userCode = orderQuery.getUserCode();
		logger.info(userCode + "用户历史托单数据查询缓存");
		String hitCountKey = historyCacheUserHitCountKey;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			jedis.zincrby(hitCountKey, 1, userCode);
		} catch (Exception e) {
			logger.info(userCode + "用户历史托单缓存访问统计更新失败", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		List<HistoryOrderView> historyOrders = new ArrayList<>();
		
		if (cache == null || cache.isEmpty()) {
			return historyOrders;
		}
		
		for (String jsonStr : cache.values()) {
			HistoryOrderView order = JSON.parseObject(jsonStr, HistoryOrderView.class);
			historyOrders.add(order);
		}
		
		Stream<HistoryOrderView> stream = historyOrders.stream();
		if (orderQuery.hasCondition()) {
			stream = stream.filter(order -> orderQuery.matchConditions(order));
		}
		
		historyOrders = stream.sorted((HistoryOrderView o1, HistoryOrderView o2) -> o1.getDate() == null ? 1 :
				o2.getDate() == null ? -1 : o2.getDate().compareTo(o2.getDate()))
					.collect(Collectors.toList());
		
		return historyOrders;
	}

	/**
	 * 从数据库查询并缓存上限量的用户历史托单分页
	 * @param userCode
	 * @param pageNum
	 * @param PageSize
	 * @return
	 */
	private Page<HistoryOrderView> findAndCacheLimitHistoryOrder(String userCode, int pageNum, int PageSize) {
		List<HistoryOrderView> limitOrders = orderMapper.findLimitHistoryOrder(userCode, perUserOrderCacheLimit);
		int size = limitOrders.size();
		int total = size;
		if (size >= perUserOrderCacheLimit) {
			total = orderMapper.totalHistoryOrder(userCode);
		}
		
		cacheLimitHistoryOrder(userCode, total, limitOrders);
		
		Page<HistoryOrderView> page = new Page<>();
		int start = Math.min(limitOrders.size(), (pageNum - 1) * PageSize);
		int end = Math.min(limitOrders.size(), pageNum * PageSize);
		
		page.setEntities(limitOrders.subList(start, end));
		page.setTotal((long) total);
		page.setPageSize(PageSize);
		page.setPageNum(pageNum);
		return page;
	}

	/**
	 * 从数据库查询并缓存上限量的用户历史托单时间分页
	 * @param userCode
	 * @param limit
	 * @return
	 */
	private TimePage<HistoryOrderView> findAndCacheLimitHistoryOrder(String userCode, int limit) {
		List<HistoryOrderView> limitOrders = orderMapper.findLimitHistoryOrder(userCode, perUserOrderCacheLimit);
		int size = limitOrders.size();
		int total = size;
		if (size >= perUserOrderCacheLimit) {
			total = orderMapper.totalHistoryOrder(userCode);
		}
		
		cacheLimitHistoryOrder(userCode, total, limitOrders);
		
		TimePage<HistoryOrderView> page = new TimePage<>();
		page.setEntities(limitOrders.subList(0, Math.min(limit, limitOrders.size())));
		page.setTotal((long) total);
		return page;
	}

	/**
	 * 缓存历史托单数据
	 * @param userCode
	 * @param total
	 * @param limitOrders
	 */
	private void cacheLimitHistoryOrder(String userCode, int total, List<HistoryOrderView> limitOrders) {
		String userKey = historyOrderUserKey + userCode;
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String hitCountKey = historyCacheUserHitCountKey + userCode;

		Map<String, String> cacheMap = new HashMap<>();;
		for (HistoryOrderView orderView : limitOrders) {
			cacheMap.put(orderView.getOrderCode(), orderView.toString());
		}
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			jedis.watch(userKey);
			Transaction trans = jedis.multi();
			trans.hmset(userKey, cacheMap);
			trans.set(userTotalKey, String.valueOf(total));
			trans.zincrby(hitCountKey, 1, userCode);
			trans.exec();
		} catch (Exception e) {
			logger.info(userCode + "用户历史托缓存失败", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 更新历史托单缓存
	 * @param order
	 */
	public void updateUserHistoryCache(Order order) {
		if (order == null) {
			return;
		}
		
		String userCode = order.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String userKey = historyOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		
		if (totalValue == null) {
			return;
		}
		
		HistoryOrderView orderView = createHistoryOrderView(order);
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			if (jedis.hsetnx(userKey, orderView.getOrderCode(), orderView.toString()) == 1) {
				jedis.incr(userTotalKey);
			}
		} catch (Exception e) {
			logger.info(userCode + "用户历史托缓存更新失败", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		
	}

	public void updateUserHistoryCaches(List<Order> orders) {
		if (orders == null || orders.isEmpty()) {
			return;
		}
		
		for (Order order : orders) {
			updateUserHistoryCache(order);
		}
	}

	/**
	 * 根据托单信息生成历史托单视图
	 * @param order
	 * @return
	 */
	public HistoryOrderView createHistoryOrderView(Order order) {
		if (order.getOrderCode() == null) {
			throw new IllegalArgumentException("托单编号为空");
		}
		
		if (order.getStatus() == null) {
			throw new IllegalArgumentException("交易类型为空");
		}
		
		if (order.getQuantity() == null) {
			throw new IllegalArgumentException("委托数量为空");
		}
		
		if (order.getDealQuantity() == null) {
			throw new IllegalArgumentException("成交数量为空");
		}
		
		if (order.getConvertRate() == null) {
			throw new IllegalArgumentException("委托单价为空");
		}
		
		if (order.getTradePairId() == null) {
			throw new IllegalArgumentException("交易对为空");
		}
		
		HistoryOrderView orderView = new HistoryOrderView();
		orderView.setOrderCode(order.getOrderCode());
		orderView.setDate(order.getCreatedTime());
		orderView.setTradeType(order.getTradeType().toString());
		orderView.setStatus(String.valueOf(order.getStatus().getValue()));
		orderView.setOrderPrice(order.getConvertRate());
		orderView.setDealTargetQuantity(order.getDealQuantity());
		orderView.setOrderTargetQuantity(order.getQuantity());
		orderView.setDealBaseAmount(order.getConvertRate().multiply(order.getDealQuantity(), new MathContext(8)));
		orderView.setServiceFee(order.getConvertRate().multiply((
				order.getQuantity().subtract(order.getDealQuantity())), new MathContext(8)));
		TradePair tradePair = tradePairService.findTradePairById(order.getTradePairId());
		orderView.setBaseCurrency(tradePair.getBaseCurrencyName());
		orderView.setBaseCurrency(tradePair.getTargetCurrencyName());
		return orderView;
	}

	/**
	 * 更新托单成交信息
	 * @param orderDealDetail
	 */
	public void updateOrderNewDeal(OrderDealDetail orderDealDetail) {
		orderMapper.updateOrderNewDeal(orderDealDetail);
	}

	/**
	 * 更新托单为完成交易
	 * @param order
	 * @return true 此次操作更新成功 false 此次未更新数据,表明由其他线程成功更新数据
	 */
	public boolean completeOrder(Order order) {
		return orderMapper.completeOrder(order) != 0;
	}


	/**
	 * 分页查询当前用户托单信息
	 * @param orderQuery
	 * @param pageNum
	 * @param PageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<CurrentEntrusts> findCurrentOrder(OrderQuery orderQuery, int pageNum, int PageSize) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalCurrentOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		if (totalValue == null) {
			findAndCacheLimitCurrentOrder(userCode, pageNum, PageSize);
		}
		List<CurrentEntrusts> orders = findCurrentOrderFromRedis(orderQuery);
		Page<CurrentEntrusts> page = new Page<>();
		if (orders != null){
			page.setEntities(orders.subList((pageNum - 1) * PageSize, pageNum * PageSize > orders.size() ? orders.size() : pageNum * PageSize));
			page.setTotal((long) orders.size());
		}else {
			page.setEntities(null);
			page.setTotal(0L);
		}
		page.setPageNum(pageNum);
		page.setPageSize(PageSize);
		return page;

	}

	/**
	 * 移动端查询当前用户托单信息
	 * @param orderQuery
	 * @param limit
	 * @return
	 */
	@Transactional(readOnly = true)
	public TimePage<CurrentEntrusts> findCurrentOrderByTime(OrderQuery orderQuery, int limit) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalCurrentOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		//无缓存时
		if (totalValue == null) {
			 findAndCacheLimitCurrentOrder(userCode, limit);
		}
		List<CurrentEntrusts> orders = findCurrentOrderFromRedis(orderQuery);
		TimePage<CurrentEntrusts> page = new TimePage<>();
		if (orders != null){
			page.setEntities(orders.subList(0, limit > orders.size() ? orders.size() : limit));
			page.setTotal((long) orders.size());
		}else {
			page.setEntities(null);
			page.setTotal(0L);
		}
		page.setLimit(limit);
		return page;


	}

	/**
	 * 分页获取用户制定数量当前托单的数据
	 * @param userCode
	 * @param pageNum
	 * @param PageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	private Page<CurrentEntrusts> findAndCacheLimitCurrentOrder(String userCode, int pageNum, int PageSize) {
		List<CurrentEntrusts> limitOrders = orderMapper.findCurrentOrder(userCode);
		int total = limitOrders.size();
		cacheLimitCurrentOrder(userCode, total, limitOrders);
		Page<CurrentEntrusts> page = new Page<>();
		page.setEntities(limitOrders.subList((pageNum - 1) * PageSize, pageNum * PageSize > limitOrders.size() ? limitOrders.size() :pageNum * PageSize));
		page.setTotal((long) total);
		page.setPageNum(pageNum);
		return page;
	}


	/**
	 * 移动端获取用户制定数量当前托单的数据
	 * @param userCode
	 * @return
	 */
	@Transactional(readOnly = true)
	private TimePage<CurrentEntrusts> findAndCacheLimitCurrentOrder(String userCode, int limit) {
		List<CurrentEntrusts> limitOrders = orderMapper.findCurrentOrder(userCode);
		int total = limitOrders.size();
		//更新用户当前托单的缓存数据
		cacheLimitCurrentOrder(userCode, total, limitOrders);

		TimePage<CurrentEntrusts> page = new TimePage<>();
		page.setEntities(limitOrders.subList(0, limit > total ? total : limit));
		page.setTotal((long) total);
		return page;
	}

	/**
	 * 从数据库中查询当前托单数据，放入缓存中
	 * @param userCode
	 * @param total
	 * @param limitOrders
	 */
	@Transactional(readOnly = true)
	private void cacheLimitCurrentOrder(String userCode, int total, List<CurrentEntrusts> limitOrders) {
		if (limitOrders.size() > 0){
			String userKey = currentOrderUserKey + userCode;
			String userTotalKey = totalCurrentOrderUserKey + userCode;
			Map<String, String> cacheMap = new HashMap<>();
			for (CurrentEntrusts currentEntrusts : limitOrders) {
				cacheMap.put(currentEntrusts.getOrderCode(), JSON.toJSONString(currentEntrusts));
			}
			Jedis jedis = null;
			try {
				jedis = RedisUtil.getResource();
				jedis.watch(userKey);
				Transaction trans = jedis.multi();
				trans.hmset(userKey, cacheMap);
				trans.set(userTotalKey, String.valueOf(total));
				trans.exec();
			} catch (Exception e) {

			}finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
	}

	//根据用户从缓存中读取当前托单

	/**
	 * 查询当前用户的托单数据从redis缓存中
	 * @param orderQuery
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<CurrentEntrusts> findCurrentOrderFromRedis(OrderQuery orderQuery) {
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + orderQuery.getUserCode());
		if (currentOrders == null || currentOrders.isEmpty()) {
			return null;
		}
		List<CurrentEntrusts> list = new ArrayList<>(currentOrders.size());
		for (String entrsts: currentOrders.keySet()){
			list.add(JSON.parseObject(currentOrders.get(entrsts), CurrentEntrusts.class));
		}
		list = list.stream().filter(currentEntrusts -> orderQuery.matchConditionsByCurrent(currentEntrusts))
				.sorted((CurrentEntrusts o1, CurrentEntrusts o2) -> o1.getDate() == null ? 1 :
						o2.getDate() == null ? -1 : o2.getDate().compareTo(o2.getDate()))
				.collect(Collectors.toList());

		return list;
	}


	/**
	 * 根据用户当前托单，添加新当前托单到缓存
	 * @param order
	 * @param cacheSeconds
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean addUserCurrentOrderListFromRedis(Order order, int cacheSeconds){
		if (StringUtils.isEmpty(order.getOrderCode())){
			return false;
		}
		CurrentEntrusts currentEntrusts = copyPropertiesOrder(order, new CurrentEntrusts());
		String userKey = currentOrderUserKey + order.getUserCode();
		String userTotalKey = totalCurrentOrderUserKey + order.getUserCode();
		Map<String, String> currentOrders = RedisUtil.getMap(userKey);
		if (currentOrders == null){
			currentOrders = new HashMap<String, String>();
		}
		currentOrders.put(currentEntrusts.getOrderCode(), JSON.toJSONString(currentEntrusts));
		int total = Integer.valueOf(RedisUtil.get(userTotalKey) == null? "0" : RedisUtil.get(userTotalKey));
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			jedis.watch(userKey);
			Transaction trans = jedis.multi();
			trans.hmset(userKey, currentOrders);
			trans.set(userTotalKey, String.valueOf(total+1));
			if (cacheSeconds != 0) {
				trans.expire(userTotalKey, cacheSeconds);
				trans.expire(userKey, cacheSeconds);
			}
			trans.exec();
		} catch (Exception e) {
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return true;
	}


	/**
	 * 托单系统状态变更：根据用户托单，更新制定托单缓存
	 * @param trading
	 * @param orderCode
	 * @param userCode
	 * @param cacheSeconds
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean updateUserCurrentOrderListFromRedis(OrderStatus trading, String orderCode, String userCode, int cacheSeconds){
		if (StringUtils.isEmpty(orderCode)){
			return false;
		}
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + userCode);
		if (currentOrders == null){
			return false;
		}
		String jsonString = currentOrders.get(orderCode);
		if (StringUtils.isEmpty(jsonString)){
			return false;
		}
		CurrentEntrusts currentEntrusts = JSON.parseObject(jsonString, CurrentEntrusts.class);
		currentEntrusts.setStatus(trading.getValue()+"");
		currentOrders.put(orderCode, JSON.toJSONString(currentEntrusts));
		String result = RedisUtil.setMap(currentOrderUserKey + userCode, currentOrders, cacheSeconds);
		return result == null ? false : true;
	}



	/**
	 * 成交系统返回，更新缓存信息
	 * @param order
	 * @param cacheSeconds
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean updateUserCurrentOrderListFromRedisByDeal(Order order, int cacheSeconds){
		if (StringUtils.isEmpty(order.getUserCode())){
			return false;
		}
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + order.getUserCode());
		if (currentOrders == null){
			return false;
		}
		String jsonString = currentOrders.get(order.getOrderCode());
		if (StringUtils.isEmpty(jsonString)){
			return false;
		}
		if (order.getDealQuantity().equals(order.getQuantity())){
			String userTotalKey = totalCurrentOrderUserKey + order.getUserCode();
			RedisUtil.mapRemove(currentOrderUserKey + order.getUserCode(), order.getOrderCode());
			RedisUtil.set(userTotalKey, (Integer.valueOf(RedisUtil.get(userTotalKey)) -1)+"", cacheSeconds);
			return true;
		}

		currentOrders.put(order.getOrderCode(), JSON.toJSONString(copyPropertiesOrder(order, new CurrentEntrusts())));
		String result = RedisUtil.setMap(currentOrderUserKey + order.getUserCode(), currentOrders, cacheSeconds);
		return result == null ? false : true;
	}


	/**
	 * 成交系统返回撤销成功，删除缓存
	 * @param userCode
	 * @param orderCode
	 * @param cacheSeconds
	 */
	@Transactional(readOnly = true)
	public void deleteUserCurrentOrderListFromRedisByDeal(String userCode, String orderCode, int cacheSeconds){
		if (StringUtils.isEmpty(userCode)){
			return;
		}
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + userCode);
		if (currentOrders == null){
			return;
		}
		if (orderCode != null){
			String userTotalKey = totalCurrentOrderUserKey + userCode;;
			RedisUtil.mapRemove(currentOrderUserKey + userCode, orderCode);
			RedisUtil.set(userTotalKey, (Integer.valueOf(RedisUtil.get(userTotalKey)) -1)+"", cacheSeconds);
			return;
		}else {
			RedisUtil.del(currentOrderUserKey + userCode);
			RedisUtil.del(totalCurrentOrderUserKey + userCode);
		}
	}

	/**
	 * Order特定数据辅到CurrentEntrusts
	 * @param order
	 * @param currentEntrusts
	 * @return
	 */
	private CurrentEntrusts copyPropertiesOrder(Order order, CurrentEntrusts currentEntrusts){
		currentEntrusts.setOrderCode(order.getOrderCode());
		currentEntrusts.setDate(order.getOrderTime());
		currentEntrusts.setTradeType(order.getTradeType().name());
		currentEntrusts.setStatus(order.getStatus().getValue()+"");
		currentEntrusts.setDealTargetQuantity(order.getDealQuantity() == null ? new BigDecimal(0):order.getDealQuantity());
		currentEntrusts.setOrderTargetQuantity(order.getQuantity());
		TradePair tradePair = tradePairService.findTradePairById(order.getTradePairId());
		currentEntrusts.setTargetCurrency(tradePair.getTargetCurrencyName());
		currentEntrusts.setBaseCurrency(tradePair.getBaseCurrencyName());
		return currentEntrusts;
	}

	/**
	 * 清除历史托单缓存
	 * LFU缓存淘汰策略
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void clearUserHistoryOrderCache() {
		logger.info("清除历史托单缓存开始");
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			Set<String> users = jedis.zrange(historyCacheUserHitCountKey, userCacheLimit + 1, -1);
			for (String userCode : users) {
				deleteUserHistoryOrderCache(userCode, jedis);
			}
			
			long total = jedis.zcard(historyCacheUserHitCountKey);
			
			int pageSize = 500;
			
			int page = (int) (total / pageSize);
			int start = 0;
			int end = pageSize - 1;
			for (int i = 0; i < page; i++) {
				reduceUserHitCountAndClearExcessCache(start, end, jedis);
				
				start = end + 1;
				end += pageSize;
			}
			
			//最后一页
			reduceUserHitCountAndClearExcessCache(start, -1, jedis);
		} catch (Exception e) {
			logger.error("除历史托单缓存失败", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		logger.info("清除历史托单缓存结束");
	}

	/**
	 * 删除历史订单缓存
	 * @param userCode
	 * @param jedis
	 */
	private void deleteUserHistoryOrderCache(String userCode, Jedis jedis) {
		String userKey = historyOrderUserKey + userCode;
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String hitCountKey = historyCacheUserHitCountKey + userCode;
		List<String> keys = new ArrayList<>();
		keys.add(userKey);
		keys.add(userTotalKey);
		keys.add(hitCountKey);

		List<String> args = new ArrayList<>();
		args.add(userCode);

		String script = "redis.call('del', KEYS[1]);"
				+ "redis.call('del', KEYS[2]);"
				+ "redis.call('zrem', KEYS[3], ARGV[1]);";
		jedis.eval(script, keys, args);
	}

	/**
	 * 缩减过去的访问计数,并清除超额缓存
	 * @param start
	 * @param end
	 * @param jedis
	 */
	private void reduceUserHitCountAndClearExcessCache(int start, int end, Jedis jedis) {
		Set<Tuple> tuples = jedis.zrangeWithScores(historyCacheUserHitCountKey, start, end);
		int limit = perUserOrderCacheLimit * 2;
		
		for (Tuple tuple : tuples) {
			String userCode = tuple.getElement();
			String userKey = historyOrderUserKey + userCode;
			Long size = jedis.hlen(userKey);
			
			//缓存的数据量大于限额2倍时清除
			if (size > limit) {
				deleteUserHistoryOrderCache(userKey, jedis);
				continue;
			}
			
			long score = (long) tuple.getScore();
			//计数为0时删除
			if (score == 0) {
				deleteUserHistoryOrderCache(userKey, jedis);
				continue;
			}
			
			score = score / 2;
			jedis.zadd(historyCacheUserHitCountKey, score, userCode);
		}
	}
}
