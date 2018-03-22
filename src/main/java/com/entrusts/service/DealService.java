package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.mapper.DealMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.dto.DealNotify;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.OrderStatus;
import com.mo9.mqclient.MqAction;

@Service
public class DealService extends BaseService {

	@Autowired
	private DealMapper dealMapper;
	
	@Autowired
	private OrderManageService orderManageService;

	@Autowired
	private OrderEventService orderEventService;

	@Autowired
	private CurrencyListService currencyListService;

	@Transactional
	public boolean save(Deal deal) {
		return dealMapper.insert(deal) != 0;
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

	@Transactional
	public void handleDeal(Deal deal) {
		Order order = orderManageService.get(deal.getOrderCode());
		if (order == null) {
			logger.info("不存在此托单");
			return;
		}
		
		deal.setTradePairId(order.getTradePairId());
		if (!save(deal)) {
			logger.info("此成交信息已处理，交易流水号:" + deal.getTradeCode());
			return;
		}
		currencyListService.updateCurrentPrice(deal);
		Order currentOrder = updateNewDeal(deal);
		orderManageService.updateUserCurrentOrderListFromRedisByDeal(currentOrder, 3600*12);
		if (currentOrder.getStatus() == OrderStatus.COMPLETE) {
			orderManageService.updateUserHistoryCache(currentOrder);
		}
	}

	@Transactional
	public void dealNotify(DealNotify dealNotify) {
		String code = null;
		if (dealNotify == null || (code = dealNotify.getCode()) == null 
				|| dealNotify.getAskOrder() == null || dealNotify.getBidOrder() == null) {
        	throw new IllegalArgumentException("成交信息不完整");
        }
		dealNotify.getAskOrder().setCode(code);
		dealNotify.getBidOrder().setCode(code);
		
        handleDeal(dealNotify.getAskOrder());
        handleDeal(dealNotify.getBidOrder());
	}
}
