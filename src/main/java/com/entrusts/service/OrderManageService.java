package com.entrusts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.Page;
import com.entrusts.module.vo.OrderView;
import com.entrusts.util.RedisUtil;
import com.entrusts.module.vo.OrderQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class OrderManageService extends BaseService {

	@Autowired
	private OrderMapper orderMapper;

	private static final String historyOrderPrefix = "com.entrusts.service.OrderManageService";

	private static final String historyOrderUserKey = historyOrderPrefix + ".userData.";

	public Page<OrderView> findHistoryOrder(OrderQuery orderQuery, int pageNum, int PageSize) {
		Page<OrderView> page = new Page<>(); 
		PageHelper.startPage(pageNum, PageSize);
		List<OrderView> orders = orderMapper.findHistoryOrder(orderQuery);
		PageInfo<OrderView> pageInfo = new PageInfo<>(orders);
		page.setEntities(orders);
		page.setTotal(pageInfo.getTotal());
		page.setPageNum(pageInfo.getPageNum());
		page.setPageSize(pageInfo.getPageSize());
		return page;
	}
	
	public List<OrderView> findHistoryOrderFromRedis(OrderQuery orderQuery) {
		String userCode = orderQuery.getUserCode();
		String userKey = historyOrderUserKey + userCode;
		List<String> cacheList = RedisUtil.getList(userKey);
		List<OrderView> historyOrders = new ArrayList<>();
		
		if (historyOrders == null || historyOrders.isEmpty()) {
			return null;
		}
		
		for (String jsonStr : cacheList) {
			OrderView order = JSON.parseObject(jsonStr, OrderView.class);
			historyOrders.add(order);
		}
		
		historyOrders = historyOrders.stream().filter(order -> orderQuery.matchConditions(order))
			.sorted((OrderView o1, OrderView o2) -> o1.getDate() == null ? 1 :
				o2.getDate() == null ? -1 : o2.getDate().compareTo(o2.getDate()))
					.collect(Collectors.toList());
		
		return historyOrders;
	}
}
