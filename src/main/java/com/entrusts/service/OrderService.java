package com.entrusts.service;

import com.alibaba.fastjson.JSONObject;
import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
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
	private OrderEventMapper orderEventMapper;
	@Autowired
	private OrderManageService orderManageService;

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
		this.save(order);
	}

	/**
	 * 插入托单数据
	 */
	@Transactional
	public void save(Order order) {
		orderMapper.insertOrder(order);
		orderManageService.addUserCurrentOrderListFromRedis(order, 3600*12);
	}

	/**
	 * 更新托单状态(交易中)
	 */
	@Transactional
	public void updateOrderStatus(OrderStatus status, String orderCode, String userCode) {
		orderMapper.updateOrderStatus(status, orderCode);
		orderManageService.updateUserCurrentOrderListFromRedis(status, orderCode, userCode, 3600*12);
	}

	/**
	 * 推送订单至撮合
	 */
	@Transactional
	public void push2Match(DelegateEvent delegateEvent) {
		JSONObject body = new JSONObject();
		body.put("orderCode", delegateEvent.getOrderCode());
		body.put("price", delegateEvent.getConvertRate());
		body.put("quantity", delegateEvent.getQuantity());
		body.put("orderType", delegateEvent.getMode());
		body.put("userCode", delegateEvent.getUserCode());
		body.put("marketId", delegateEvent.getTradePairId());
		body.put("baseCurrencyId", delegateEvent.getBaseCurrencyId());
		body.put("tradeType", delegateEvent.getTradeType());
		body.put("createdTime", delegateEvent.getOrderTime().getTime());

		mqMessageService.send(delegatePushTopic, delegatePushTag, delegateEvent.getOrderCode(), body.toJSONString());
		this.updateOrderStatus(OrderStatus.TRADING, delegateEvent.getOrderCode(), delegateEvent.getUserCode());
	}
}
