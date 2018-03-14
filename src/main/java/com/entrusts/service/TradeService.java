package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.mapper.TradeMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.Trade;
import com.entrusts.module.enums.OrderStatus;

@Service
public class TradeService extends BaseService {

	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private OrderManageService orderManageService;

	public boolean save(Trade trade) {
		return tradeMapper.insert(trade) != 0;
	}
	
	@Transactional
	public Order updateOrderNewDeal(Trade trade) {
		orderManageService.updateOrderNewDeal(trade);
		Order order = orderManageService.get(trade.getOrderCode());
		if (order.getDealQuantity() == null || order.getQuantity() == null || !order.getDealQuantity().equals(order.getQuantity())) {
			return null;
		}
		
		order.setStatus(OrderStatus.COMPLETE);
		if (orderManageService.completeOrder(order)) {
			return order;
		}
		
		return null;
	}
}
