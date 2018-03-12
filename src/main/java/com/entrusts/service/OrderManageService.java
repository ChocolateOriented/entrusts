package com.entrusts.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.entrusts.module.vo.CurrentEntrusts;
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

	private static final String historyOrderPrefix = "com.entrusts.service.OrderManageService";

	private static final String currentOrderPrefix = "com.entrusts.service.CurrentOrderManageService";

	private static final String historyOrderUserKey = historyOrderPrefix + ".userData.";

	private static final String currentOrderUserKey = currentOrderPrefix + ".userDate.";
	
	private static final String totalHistoryOrderUserKey = historyOrderPrefix + ".userTotalData.";

	private static final String totalCurrentOrderUserKey = currentOrderPrefix + ".userTotalData.";
	
	private static final int perUserOrderCacheLimit = 50;

	public Page<HistoryOrderView> findHistoryOrder(OrderQuery orderQuery, int pageNum, int PageSize) {
		String userCode = orderQuery.getUserCode();
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		String totalValue = RedisUtil.get(userTotalKey);
		int requireNum = pageNum * PageSize;
		if (totalValue == null) {
			if (requireNum > perUserOrderCacheLimit) { //若需要的数据量大于缓存上限，则直接从数据库获取
				return findHistoryOrderFromDB(orderQuery, pageNum, PageSize);
			} else if (!orderQuery.hasCondition()) { //若需要的数据量不大于缓存上限，且不含查询条件，则获取上限数据并缓存
				return findAndCacheLimitHistoryOrder(userCode, pageNum, PageSize);
			} else { //有查询条件无缓存时，直接从数据库获取
				return findHistoryOrderFromDB(orderQuery, pageNum, PageSize);
			}
		}
		
		int total = Integer.parseInt(totalValue);
		//缓存的为全量数据，或者不含查询条件且需要的数据量不大于缓存上限时，从缓存获取，否则从数据库获取
		if (total < perUserOrderCacheLimit || (requireNum <= perUserOrderCacheLimit && !orderQuery.hasCondition())) {
			List<HistoryOrderView> orders = findHistoryOrderFromRedis(orderQuery);
			Page<HistoryOrderView> page = new Page<>();
			page.setEntities(orders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
			page.setTotal((long) orders.size());
			page.setPageNum(pageNum);
			page.setPageSize(PageSize);
			return page;
		} else {
			return findHistoryOrderFromDB(orderQuery, pageNum, PageSize);
		}
		
	}
	
	private Page<HistoryOrderView> findHistoryOrderFromDB(OrderQuery orderQuery, int pageNum, int PageSize) {
		Page<HistoryOrderView> page = new Page<>();
		PageHelper.startPage(pageNum, PageSize);
		List<HistoryOrderView> orders = orderMapper.findHistoryOrder(orderQuery);
		PageInfo<HistoryOrderView> pageInfo = new PageInfo<>(orders);
		page.setEntities(orders);
		page.setTotal(pageInfo.getTotal());
		page.setPageNum(pageInfo.getPageNum());
		page.setPageSize(pageInfo.getPageSize());
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
		
		historyOrders = historyOrders.stream().filter(order -> orderQuery.matchConditions(order))
			.sorted((HistoryOrderView o1, HistoryOrderView o2) -> o1.getDate() == null ? 1 :
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
			jedis.set(userTotalKey, String.valueOf(size));
			trans.exec();
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		Page<HistoryOrderView> page = new Page<>();
		page.setEntities(limitOrders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
		page.setTotal((long) total);
		page.setPageNum(pageNum);
		return page;
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
		int requireNum = pageNum * PageSize;
		if (totalValue == null) {
			if (requireNum > perUserOrderCacheLimit) { //若需要的数据量大于缓存上限，则直接从数据库获取
				return findCurrentOrderFromDB(orderQuery, pageNum, PageSize);
			} else if (!orderQuery.hasCondition()) { //若需要的数据量不大于缓存上限，且不含查询条件，则获取上限数据并缓存
				//return findAndCacheLimitHistoryOrder(userCode, pageNum, PageSize);
			} else { //有查询条件无缓存时，直接从数据库获取
				return findCurrentOrderFromDB(orderQuery, pageNum, PageSize);
			}
		}

		int total = Integer.parseInt(totalValue);
		//缓存的为全量数据，或者不含查询条件且需要的数据量不大于缓存上限时，从缓存获取，否则从数据库获取
		if (total < perUserOrderCacheLimit || (requireNum <= perUserOrderCacheLimit && !orderQuery.hasCondition())) {
			List<CurrentEntrusts> orders = findCurrentOrderFromRedis(orderQuery);
			Page<CurrentEntrusts> page = new Page<>();
			page.setEntities(orders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
			page.setTotal((long) orders.size());
			page.setPageNum(pageNum);
			page.setPageSize(PageSize);
			return page;
		} else {
			return findCurrentOrderFromDB(orderQuery, pageNum, PageSize);
		}

	}


	public void updateUserCurrentCache(Order order) {
		Map<String,String> cacheMap = new HashMap<String, String>();
		for (int i=0; i<20; i++){
			CurrentEntrusts entrusts = new CurrentEntrusts();
			entrusts.setBaseCurrency("BTC");
			entrusts.setOrderCode(String.valueOf(generateId()));
			entrusts.setDate(System.currentTimeMillis());
			entrusts.setDealTargetQuantity(new BigDecimal("0.00000201"));
			entrusts.setOrderTargetQuantity(new BigDecimal("0.00002001"));
			entrusts.setOrderPrice(new BigDecimal("0.00000001"));
			entrusts.setTargetCurrency("BTC");
			entrusts.setTradeType("1");
			entrusts.setStatus("sell");
			cacheMap.put(entrusts.getOrderCode()+"",JSON.toJSONString(entrusts));
		}
		RedisUtil.setMap("testentrsts", cacheMap, 0);
	}


	private Page<CurrentEntrusts> findCurrentOrderFromDB(OrderQuery orderQuery, int pageNum, int PageSize) {
		Page<CurrentEntrusts> page = new Page<>();
		PageHelper.startPage(pageNum, PageSize);
		List<CurrentEntrusts> orders = orderMapper.findCurrentOrder(orderQuery);
		PageInfo<CurrentEntrusts> pageInfo = new PageInfo<>(orders);
		page.setEntities(orders);
		page.setTotal(pageInfo.getTotal());
		page.setPageNum(pageInfo.getPageNum());
		page.setPageSize(pageInfo.getPageSize());
		return page;
	}

	private Page<CurrentEntrusts> findAndCacheLimitCurrentOrder(String userCode, int pageNum, int PageSize) {
		List<CurrentEntrusts> limitOrders = orderMapper.findLimitCurrentOrder(userCode, perUserOrderCacheLimit);
		int size = limitOrders.size();
		int total = size;
		if (size >= perUserOrderCacheLimit) {
			total = orderMapper.totalCurrentOrder(userCode);
		}

		String userKey = historyOrderUserKey + userCode;
		String userTotalKey = totalHistoryOrderUserKey + userCode;
		Map<String, String> cacheMap = new HashMap<>();;
		for (CurrentEntrusts orderView : limitOrders) {
			cacheMap.put(orderView.getOrderCode(), orderView.toString());
		}
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			jedis.watch(userKey);
			Transaction trans = jedis.multi();
			jedis.hmset(userKey, cacheMap);
			jedis.set(userTotalKey, String.valueOf(size));
			trans.exec();
		} catch (Exception e) {
			if (jedis != null) {
				jedis.close();
			}
		}

		Page<CurrentEntrusts> page = new Page<>();
		page.setEntities(limitOrders.subList((pageNum - 1) * PageSize, pageNum * PageSize));
		page.setTotal((long) total);
		page.setPageNum(pageNum);
		return page;
	}

	public List<CurrentEntrusts> findCurrentOrderFromRedis(OrderQuery orderQuery) {
		Map<String, String> currentOrders = RedisUtil.getMap("testentrsts");
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


	@Scheduled(cron = "0 0 0 * * ?")
	public void clearUserHistoryOrderCache() {
		
	}
}
