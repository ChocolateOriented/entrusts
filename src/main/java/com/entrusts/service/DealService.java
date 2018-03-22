package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.mapper.DealMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.dto.DealNotify;
import com.entrusts.module.dto.DealNotify.OrderDealDetail;
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

	@Autowired
	private CurrencyListService currencyListService;

	@Transactional
	public boolean save(Deal deal) {
		return dealMapper.insert(deal) != 0;
	}

	/**
	 * 更新托单成交信息和状态(如果完成)
	 * @param orderDealDetail
	 * @return
	 */
	@Transactional
	public Order updateNewDeal(OrderDealDetail orderDealDetail) {
		orderManageService.updateOrderNewDeal(orderDealDetail);
		Order order = orderManageService.get(orderDealDetail.getOrderCode());
		if (order == null || order.getDealQuantity() == null || order.getQuantity() == null || !order.getDealQuantity().equals(order.getQuantity())) {
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

	/**
	 * 处理托单成交信息
	 * @param orderDealDetail
	 */
	@Transactional
	public void handleOrderDeal(OrderDealDetail orderDealDetail) {
		Order currentOrder = updateNewDeal(orderDealDetail);
		if (currentOrder == null) {
			return;
		}
		
		orderManageService.updateUserCurrentOrderListFromRedisByDeal(currentOrder, 3600*12);
		if (currentOrder.getStatus() == OrderStatus.COMPLETE) {
			orderManageService.updateUserHistoryCache(currentOrder);
		}
	}
	
	/**
	 * 接收到成交通知
	 * @param dealNotify
	 */
	@Transactional
	public void dealNotify(DealNotify dealNotify) {
		Deal deal = createDealFromNotify(dealNotify);
		Order askOrder = orderManageService.get(deal.getAskOrderCode());
		if (askOrder == null) {
			logger.info("不存在此托单,托单号" + deal.getAskOrderCode());
			return;
		}
		
		Order bidOrder = orderManageService.get(deal.getBidOrderCode());
		if (bidOrder == null) {
			logger.info("不存在此托单,托单号" + deal.getBidOrderCode());
			return;
		}
		
		deal.setTradePairId(askOrder.getTradePairId());
		
		if (!save(deal)) {
			logger.info("此成交信息已处理,交易流水号:" + deal.getTradeCode());
			return;
		}
		currencyListService.updateCurrentPrice(deal);
		handleOrderDeal(dealNotify.getAskOrder());
		handleOrderDeal(dealNotify.getBidOrder());
	}
	
	/**
	 * 根据成交信息通知生成成交实体
	 * @param dealNotify
	 * @return
	 */
	private Deal createDealFromNotify(DealNotify dealNotify) {
		OrderDealDetail askOrder = null;
		OrderDealDetail bidOrder = null;
		
		if (dealNotify == null || dealNotify.getCode() == null
				|| (askOrder = dealNotify.getAskOrder()) == null || (bidOrder = dealNotify.getBidOrder()) == null) {
        	throw new IllegalArgumentException("成交信息不完整");
        }
		
		if (askOrder.getOrderCode() == null || bidOrder.getOrderCode() == null) {
			throw new IllegalArgumentException("托单编号为空");
		}
		
		if (askOrder.getTradeEncryptCurrencyQuantity() == null || bidOrder.getTradeEncryptCurrencyQuantity() == null) {
			throw new IllegalArgumentException("托单成交量为空");
		}
		
		if (askOrder.getDealPrice() == null || bidOrder.getDealPrice() == null) {
			throw new IllegalArgumentException("托单成交价格为空");
		}
		
		Deal deal = new Deal();
		deal.setTradeCode(dealNotify.getCode());
		deal.setAskOrderCode(askOrder.getOrderCode());
		deal.setBidOrderCode(bidOrder.getOrderCode());
		deal.setDealPrice(askOrder.getDealPrice());
		deal.setDealQuantity(askOrder.getTradeEncryptCurrencyQuantity());
		deal.setBaseCurrencyid(askOrder.getDealEncryptCurrencyId());
		deal.setTargetCurrencyid(askOrder.getTradeEncryptCurrencyId());
		deal.setCreatedTime(askOrder.getCreatedTime());
		deal.setTradeFee(askOrder.getTradeFee());
		
		return deal;
	}
}
