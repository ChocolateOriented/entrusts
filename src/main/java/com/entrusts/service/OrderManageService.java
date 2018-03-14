package com.entrusts.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.entrusts.module.dto.TimePage;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.vo.CurrentEntrusts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.Page;
import com.entrusts.module.entity.Order;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.util.RedisUtil;
import com.entrusts.module.vo.OrderQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@Service
public class OrderManageService extends BaseService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private TradePairService tradePairService;

	private static final String historyOrderPrefix = "com.entrusts.service.OrderManageService";

	private static final String currentOrderPrefix = "com.entrusts.service.CurrentOrderManageService";

	private static final String historyOrderUserKey = historyOrderPrefix + ".userData.";

	private static final String currentOrderUserKey = currentOrderPrefix + ".currentuserDate.";

	private static final String totalHistoryOrderUserKey = historyOrderPrefix + ".userTotalData.";

	private static final String totalCurrentOrderUserKey = currentOrderPrefix + ".currentuserTotalData.";

	private static final int perUserOrderCacheLimit = 50;

	public Page<HistoryOrderView> findHistoryOrderByPage(OrderQuery orderQuery, int pageNum, int PageSize) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		int requireNum = pageNum * PageSize;
		if (totalValue == null) {
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
			List<HistoryOrderView> orders = findHistoryOrderFromRedis(orderQuery);
			Page<HistoryOrderView> page = new Page<>();
			page.setEntities(orders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
			page.setTotal((long) orders.size());
			page.setPageNum(pageNum);
			page.setPageSize(PageSize);
			return page;
		} else {
			return findHistoryOrderByPageFromDB(orderQuery, pageNum, PageSize);
		}
		
	}
	
	public TimePage<HistoryOrderView> findHistoryOrderByTime(OrderQuery orderQuery, int limit) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		//无缓存时
		if (totalValue == null) {
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
			List<HistoryOrderView> orders = findHistoryOrderFromRedis(orderQuery);
			TimePage<HistoryOrderView> page = new TimePage<>();
			page.setEntities(orders.subList(0, limit));
			page.setTotal((long) orders.size());
			page.setLimit(limit);
			return page;
		} else {
			return findHistoryOrderByTimeFromDB(orderQuery, limit);
		}

	}

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

	private List<HistoryOrderView> findHistoryOrderFromRedis(OrderQuery orderQuery) {
		String userCode = orderQuery.getUserCode();
		String userKey = historyOrderUserKey + userCode;
		Map<String, String> cache = RedisUtil.getMap(userKey);
		List<HistoryOrderView> historyOrders = new ArrayList<>();
		
		if (historyOrders == null || historyOrders.isEmpty()) {
			return null;
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
	
	private Page<HistoryOrderView> findAndCacheLimitHistoryOrder(String userCode, int pageNum, int PageSize) {
		List<HistoryOrderView> limitOrders = orderMapper.findLimitHistoryOrder(userCode, perUserOrderCacheLimit);
		int size = limitOrders.size();
		int total = size;
		if (size >= perUserOrderCacheLimit) {
			total = orderMapper.totalHistoryOrder(userCode);
		}
		
		cacheLimitHistoryOrder(userCode, total, limitOrders);

		Page<HistoryOrderView> page = new Page<>();
		page.setEntities(limitOrders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
		page.setTotal((long) total);
		page.setPageNum(pageNum);
		return page;
	}

	private TimePage<HistoryOrderView> findAndCacheLimitHistoryOrder(String userCode, int limit) {
		List<HistoryOrderView> limitOrders = orderMapper.findLimitHistoryOrder(userCode, perUserOrderCacheLimit);
		int size = limitOrders.size();
		int total = size;
		if (size >= perUserOrderCacheLimit) {
			total = orderMapper.totalHistoryOrder(userCode);
		}

		cacheLimitHistoryOrder(userCode, total, limitOrders);

		TimePage<HistoryOrderView> page = new TimePage<>();
		page.setEntities(limitOrders.subList(0, limit));
		page.setTotal((long) total);
		return page;
	}

	private void cacheLimitHistoryOrder(String userCode, int total, List<HistoryOrderView> limitOrders) {
		String userKey = historyOrderUserKey + userCode;
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		Map<String, String> cacheMap = new HashMap<>();;
		for (HistoryOrderView orderView : limitOrders) {
			cacheMap.put(orderView.getOrderCode(), orderView.toString());
		}
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			jedis.watch(userKey);
			Transaction trans = jedis.multi();
			jedis.hmset(userKey, cacheMap);
			jedis.set(userTotalKey, String.valueOf(total));
			trans.exec();
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
		}
	}



	public void updateUserHistoryCache(Order order) {
		String userCode = order.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String userKey = historyOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		
		if (totalValue == null) {
			return;
		}
		
		HistoryOrderView orderView = orderMapper.getHistoryOrder(order);
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			if (jedis.hsetnx(userKey, orderView.getOrderCode(), orderView.toString()) == 1) {
				jedis.incr(userTotalKey);
			}
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

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
			page.setEntities(orders.subList((pageNum - 1) * PageSize, pageNum * PageSize > orders.size() ? orders.size() :pageNum * PageSize));
			page.setTotal((long) orders.size());
		}else {
			page.setEntities(null);
			page.setTotal(0L);
		}
		page.setPageNum(pageNum);
		page.setPageSize(PageSize);
		return page;

	}

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


	//获取用户制定数量当前脱单的缓存数据
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


	private TimePage<CurrentEntrusts> findAndCacheLimitCurrentOrder(String userCode, int limit) {
		List<CurrentEntrusts> limitOrders = orderMapper.findCurrentOrder(userCode);
		int total = limitOrders.size();
		//更新用户当前脱单的缓存数据
		cacheLimitCurrentOrder(userCode, total, limitOrders);

		TimePage<CurrentEntrusts> page = new TimePage<>();
		page.setEntities(limitOrders.subList(0, limit));
		page.setTotal((long) total);
		return page;
	}

	private void cacheLimitCurrentOrder(String userCode, int total, List<CurrentEntrusts> limitOrders) {
		String userKey = currentOrderUserKey + userCode;
		String userTotalKey = totalCurrentOrderUserKey + userCode;
		Map<String, String> cacheMap = new HashMap<>();;
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
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	//根据用户从缓存中读取当前脱单
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

	//根据用户订单，添加当前脱单缓存
	public boolean addUserCurrentOrderListFromRedis(Order order){
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
			trans.exec();
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
			return false;
		}
		return true;
	}

	//脱单系统状态变更：根据用户订单，更新制定脱单缓存
	public boolean updateUserCurrentOrderListFromRedis(OrderStatus trading, String orderCode, String userCode, int cacheSeconds){
		if (StringUtils.isEmpty(orderCode)){
			return false;
		}
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + userCode);
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

	//成交系统返回，更新缓存信息
	public boolean updateUserCurrentOrderListFromRedisByDeal(Order order, int cacheSeconds){
		if (StringUtils.isEmpty(order.getUserCode())){
			return false;
		}
		Map<String, String> currentOrders = RedisUtil.getMap(currentOrderUserKey + order.getUserCode());
		String jsonString = currentOrders.get(order.getOrderCode());
		if (StringUtils.isEmpty(jsonString)){
			return false;
		}
		if (order.getDealQuantity().equals(order.getQuantity())){
			currentOrders.remove(order.getOrderCode());
			String result = RedisUtil.setMap(currentOrderUserKey + order.getUserCode(), currentOrders, cacheSeconds);
			return result == null ? false : true;
		}

		currentOrders.put(order.getOrderCode(), JSON.toJSONString(copyPropertiesOrder(order, new CurrentEntrusts())));
		String result = RedisUtil.setMap(currentOrderUserKey + order.getUserCode(), currentOrders, cacheSeconds);
		return result == null ? false : true;
	}


	private CurrentEntrusts copyPropertiesOrder(Order order, CurrentEntrusts currentEntrusts){
		currentEntrusts.setOrderCode(order.getOrderCode());
		currentEntrusts.setDate(order.getOrderTime().getTime());
		currentEntrusts.setTradeType(order.getTradeType().name());
		currentEntrusts.setStatus(order.getStatus().getValue()+"");
		currentEntrusts.setDealTargetQuantity(order.getDealQuantity() == null ? new BigDecimal(0):order.getDealQuantity());
		currentEntrusts.setOrderTargetQuantity(order.getQuantity());
		TradePair tradePair = tradePairService.findTradePairById(order.getTradePairId());
		currentEntrusts.setTargetCurrency(tradePair.getTargetCurrencyName());
		currentEntrusts.setBaseCurrency(tradePair.getBaseCurrencyName());
		return currentEntrusts;
	}


	@Scheduled(cron = "0 0 0 * * ?")
	public void clearUserHistoryOrderCache() {
		
	}
}
