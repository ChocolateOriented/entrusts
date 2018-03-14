package com.entrusts.service;

import com.alibaba.fastjson.JSONObject;
import com.entrusts.mapper.EntMqMessageMapper;
import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.entity.EntMqMessage;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import com.mo9.mqclient.IMqProducer;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;
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
	private OrderMapper orderMapper;
	@Autowired
	private OrderEventMapper orderEventMapper;
	@Autowired
	private EntMqMessageMapper mqMessageMapper;
	@Autowired
	private IMqProducer mqProducer;

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
		order.setStatus(OrderStatus.DELEGATING); orderMapper.insertOrder(order);
		this.save(order);
	}

	/**
	 * 插入托单数据
	 */
	@Transactional
	public void save(Order order) {
		orderMapper.insertOrder(order);
	}

	/**
	 * 更新托单状态(交易中)
	 */
	@Transactional
	public void updateOrderStatus(OrderStatus trading, String orderCode) {
		orderMapper.updateOrderStatus(trading, orderCode);
	}

	/**
	 * 推送订单至撮合
	 */
	@Transactional
	public void push2match(DelegateEvent delegateEvent) {
		EntMqMessage entMqMessage = insertMqPush(delegateEvent);
		this.updateOrderStatus(OrderStatus.TRADING, delegateEvent.getOrderCode());
		try {
			push2match(entMqMessage);
		} catch (Exception e) {
			logger.info(delegateEvent.getOrderCode() + "推送至撮合失败", e);
		}
	}

	/**
	 * MQ入库
	 */
	public EntMqMessage insertMqPush(DelegateEvent delegateEvent) {
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
		EntMqMessage entMqMessage = new EntMqMessage(delegatePushTopic, delegatePushTag, delegateEvent.getOrderCode(), body.toJSONString());
		mqMessageMapper.insert(entMqMessage);
		return entMqMessage;
	}

	/**
	 * 推送mq更新托单状态
	 */
	private void push2match(EntMqMessage entMqMessage) {
		MqMessage message = new MqMessage(entMqMessage.getTopic(), entMqMessage.getTag(), entMqMessage.getKey(), entMqMessage.getBody());
		MqSendResult result = mqProducer.send(message);
		message.setMsgId(result.getMessageId());
		mqMessageMapper.updateByKey(entMqMessage);
	}
}
