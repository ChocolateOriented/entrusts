package com.entrusts.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entrusts.exception.ServiceException;
import com.entrusts.mapper.DealMapper;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.dto.DealNotify;
import com.entrusts.module.dto.DealNotify.OrderDealDetail;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.OrderStatus;

@Service
public class DealService extends BaseService {

    public static final Integer TYPE_OF_PEOPLE = 0; //代表用户
    public static final Integer TYPE_OF_ROBOT = 1; //代表机器人

	@Autowired
	private DealMapper dealMapper;
	
	@Autowired
	private OrderManageService orderManageService;

	@Autowired
	private OrderEventService orderEventService;

	@Autowired
	private CurrencyListService currencyListService;

	@Autowired
	private TradePairService tradePairService;

	@Autowired
	private MarketService marketService;

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
		if (order == null || order.getDealQuantity() == null || order.getQuantity() == null) {
			return order;
		}
		
		if (order.getQuantity().compareTo(order.getDealQuantity()) > 0) {
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
		if (orderDealDetail.getIsStrategy() != null && orderDealDetail.getIsStrategy() == TYPE_OF_ROBOT) {
			return;
		}
		
		BigDecimal dealAmount = orderDealDetail.getDealPrice().multiply(orderDealDetail.getDealQuantity()).setScale(16, BigDecimal.ROUND_HALF_UP);
		orderDealDetail.setDealAmount(dealAmount);
		Order currentOrder = updateNewDeal(orderDealDetail);
		if (currentOrder == null) {
			return;
		}
		
		orderManageService.updateUserCurrentOrderListFromRedisByDeal(currentOrder, 3600*12);
		marketService.updateDelegateTotalQuantityWhenDeal(orderDealDetail, currentOrder);
		if (currentOrder.getStatus() == OrderStatus.COMPLETE) {
			orderManageService.updateUserHistoryCache(currentOrder);
		}
	}
	
	/**
	 * 接收到成交通知
	 * @param dealNotify
	 * @throws ServiceException 
	 */
	@Transactional
	public void dealNotify(DealNotify dealNotify) throws ServiceException {
		Deal deal = createDealFromNotify(dealNotify);
		if (!save(deal)) {
			logger.info("此成交信息已处理,交易流水号:" + deal.getTradeCode());
			throw new ServiceException("重复成交信息");
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
		
		if (dealNotify.getDealQuantity() == null) {
			throw new IllegalArgumentException("托单成交量为空");
		}
		
		if ((dealNotify.getDealPrice() == null)) {
			throw new IllegalArgumentException("托单成交价格为空");
		}
		
		TradePair tradePair = tradePairService.findTradePairByCoinId(dealNotify.getBaseCurrencyId(), dealNotify.getTargetCurrencyId());
		if (tradePair == null) {
			throw new IllegalArgumentException("无效的交易对");
		}
		
		Deal deal = new Deal();
		askOrder.setDealQuantity(dealNotify.getDealQuantity());
		bidOrder.setDealQuantity(dealNotify.getDealQuantity());
		askOrder.setDealPrice(dealNotify.getDealPrice());
		bidOrder.setDealPrice(dealNotify.getDealPrice());
		deal.setTradeCode(dealNotify.getCode());
		deal.setAskOrderCode(askOrder.getOrderCode());
		deal.setBidOrderCode(bidOrder.getOrderCode());
		deal.setDealPrice(dealNotify.getDealPrice());
		deal.setDealQuantity(dealNotify.getDealQuantity());
		deal.setBaseCurrencyId(dealNotify.getBaseCurrencyId());
		deal.setTargetCurrencyId(dealNotify.getTargetCurrencyId());
		deal.setCreatedTime(dealNotify.getCreatedTime());
		deal.setAskTradeFee(askOrder.getTradeFee());
		deal.setBidTradeFee(bidOrder.getTradeFee());
		deal.setTradePairId(tradePair.getId());
		
		return deal;
	}
}
