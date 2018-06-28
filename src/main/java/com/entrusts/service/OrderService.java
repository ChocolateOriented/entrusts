package com.entrusts.service;

import com.alibaba.fastjson.JSONObject;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;
import java.text.DecimalFormat;
import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sxu
 */
@Service
public class OrderService extends BaseService {

	@Autowired
	EntMqMessageService mqMessageService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderManageService orderManageService;
	@Autowired
	private MarketService marketService;

	@Value("${mq.delegatePushTopic}")
	private String delegatePushTopic;
	@Value("${mq.delegatePushTag}")
	private String delegatePushTag;

	/**
	 * 插入托单数据
	 */
	@Transactional
	public void saveNewOrderByEvent(DelegateEvent delegateEvent) {
		Order order = new Order();
		BeanUtils.copyProperties(delegateEvent, order);
		order.setStatus(OrderStatus.DELEGATING);
		this.saveNewOrder(order);
	}

	/**
	 * 插入托单数据
	 */
	@Transactional
	public void saveNewOrder(Order order) {
		Date now = new Date();
		order.setCreatedTime(now);
		order.setUpdatedTime(now);
		orderMapper.insertOrder(order);
		orderManageService.addUserCurrentOrderListFromRedis(order, 3600*12);
	}

	/**
	 * 更新托单状态(交易中)
	 */
	@Transactional
	public void updateOrderStatus(OrderStatus status, String orderCode, String userCode) {
		orderMapper.updateOrderStatus(status, orderCode,new Date());
		Order order = orderMapper.get(orderCode);
		if (status.equals(OrderStatus.DELEGATING) || status.equals(OrderStatus.TRADING)){
			orderManageService.updateUserCurrentOrderListFromRedis(status, orderCode, userCode, 3600*12);
		} else {
			orderManageService.updateUserHistoryCache(order);
			orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, orderCode, 3600*12);
		}
		marketService.updateDelegateTotalQuantity(order);
	}

	/**
	 * 推送订单至撮合
	 */
	@Transactional
	public String push2Match(DelegateEvent delegateEvent) {
		JSONObject body = new JSONObject();
		body.put("orderCode", delegateEvent.getOrderCode());
		body.put("price", delegateEvent.getConvertRate());
		body.put("quantity", delegateEvent.getQuantity());
		body.put("orderType", delegateEvent.getMode());
		body.put("userCode", delegateEvent.getUserCode());
		body.put("marketId", delegateEvent.getTradePairId());
		body.put("baseCurrencyId", delegateEvent.getBaseCurrencyId());
		body.put("targetCurrencyId", delegateEvent.getTargetCurrencyId());
		body.put("tradeType", delegateEvent.getTradeType());
		body.put("createdTime", delegateEvent.getOrderTime().getTime());
		body.put("isStrategy", 0);

		//有序队列分区键, 使用交易对+交易类型组成
		String shardingKey = delegateEvent.getTradePairId() + delegateEvent.getTradeType().name();
		this.updateOrderStatus(OrderStatus.TRADING, delegateEvent.getOrderCode(), delegateEvent.getUserCode());

		return mqMessageService.orderSend(delegatePushTopic, delegatePushTag, delegateEvent.getOrderCode(), body.toJSONString(),shardingKey);
	}

	/**
	 * @return java.lang.String
	 * @Description 生成ordercode, snowFlakeId + 预留位(0) + 交易对Id(4字符) + 交易类型与成交模式(1字符)
	 * 使用10进制存储订单信息
	 * @param orderMode
	 * @param tradeType
	 * @param tradePairId
	 */
	public String generateOrderCode(OrderMode orderMode, TradeType tradeType, Integer tradePairId) {
		final int MODE_BIT = 1; //成交模式占用2进制位数

		DecimalFormat format = new DecimalFormat("0000");
		int mode = orderMode.equals(OrderMode.limit) ? 0 : 1;
		int type = tradeType.equals(TradeType.buy) ? 0 : 1;
		int modeAndtype = type << MODE_BIT | mode;

		return this.generateId() + "0" + format.format(tradePairId) + modeAndtype;
	}
}
