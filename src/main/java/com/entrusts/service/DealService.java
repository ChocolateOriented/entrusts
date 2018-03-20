package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.mapper.DealMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.OrderStatus;

@Service
public class DealService extends BaseService {

	@Autowired
	private DealMapper dealMapper;
	
	@Autowired
	private OrderManageService orderManageService;

	@Autowired
	private OrderEventService orderEventService;

	public boolean save(Deal trade) {
		return dealMapper.insert(trade) != 0;
	}

	/**
	 * 更新托单成交信息和状态(如果完成)
	 * @param deal
	 * @return
	 */
	@Transactional
	public Order updateNewDeal(Deal deal) {
		orderManageService.updateOrderNewDeal(deal);
		Order order = orderManageService.get(deal.getOrderCode());
		if (order.getDealQuantity() == null || order.getQuantity() == null || !order.getDealQuantity().equals(order.getQuantity())) {
			return order;
		}
		
		if (orderManageService.completeOrder(order)) {
			order.setStatus(OrderStatus.COMPLETE);
		}
		
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setOrderCode(order.getOrderCode());
		orderEvent.setStatus(order.getStatus());
		orderEvent.setDealAmout(order.getDealAmount());
		orderEvent.setDealQuantity(order.getDealQuantity());
		orderEventService.save(orderEvent);
		
		return order;
	}
}
