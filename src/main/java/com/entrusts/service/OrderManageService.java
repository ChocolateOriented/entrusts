package com.entrusts.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.Page;
import com.entrusts.module.dto.TimePage;
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

	private static final String historyOrderUserKey = historyOrderPrefix + ".userData.";
	
	private static final String totalHistoryOrderUserKey = historyOrderPrefix + ".userTotalData.";
	
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
	
	@Scheduled(cron = "0 0 0 * * ?")
	public void clearUserHistoryOrderCache() {
		
	}
}
