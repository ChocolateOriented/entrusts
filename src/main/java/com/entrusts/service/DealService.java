package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.mapper.DealMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.OrderStatus;

@Service
public class DealService extends BaseService {

	@Autowired
	private DealMapper dealMapper;
	
	@Autowired
	private OrderManageService orderManageService;

	public boolean save(Deal trade) {
		return dealMapper.insert(trade) != 0;
	}
	
	@Transactional
	public Order updateOrderNewDeal(Deal deal) {
		orderManageService.updateOrderNewDeal(deal);
		Order order = orderManageService.get(deal.getOrderCode());
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
