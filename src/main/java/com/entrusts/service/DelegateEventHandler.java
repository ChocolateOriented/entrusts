package com.entrusts.service;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.util.RestTemplateUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DelegateEventHandler extends BaseService {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderEventMapper orderEventMapper;

	@Value("${url.lockCoin}")
	private String url;

	/**
	 * 保存托单数据
	 */
	public void saveOrder(DelegateEvent delegateEvent, long sequence, boolean endOfBatch) {
		try {
			orderService.saveNewOrderByEvent(delegateEvent);
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_ERROR);
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "新增托单数据失败:", e);
			return;
		}

		delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_SUCCESS);
		logger.debug("用户：{}订单:{} 新增托单数据成功", delegateEvent.getUserCode(), delegateEvent.getOrderCode());
	}

	/**
	 * 保存新托单日志
	 */
	@Transactional
	public void saveNewOrdereEvent(DelegateEvent delegateEvent, long sequence, boolean endOfBatch) {
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setOrderCode(delegateEvent.getOrderCode());
		orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
		if (DelegateEventstatus.INSERT_ORDERDB_SUCCESS.equals(delegateEvent.getDelegateEventstatus())) {
			orderEvent.setStatus(OrderStatus.DELEGATING);
		} else {
			orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
		}

		orderEventMapper.insertOrderEvent(orderEvent);
	}

	/**
	 * 发布托单
	 * 账户锁币&MQ入库&变更托单状态
	 */
	public void publishOrder(DelegateEvent delegateEvent, long sequence, boolean endOfBatch) {
		if (DelegateEventstatus.INSERT_ORDERDB_ERROR.equals(delegateEvent.getDelegateEventstatus())) {
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "新增托单数据未成功,账户锁币&MQ入库&变更托单状态未执行");
			return;
		}
		/**   账户锁币        */
		Map<String, Object> map = new HashMap<>();
		map.put("orderCode", delegateEvent.getOrderCode());
		map.put("userCode", delegateEvent.getUserCode());
		map.put("encryptCurrencyId", delegateEvent.getTargetCurrencyId());
		map.put("quantity", delegateEvent.getQuantity());

		String userCode = delegateEvent.getUserCode();
		String orderCode = delegateEvent.getOrderCode();

		Results result = null;
		try {
			result = RestTemplateUtils.post(this.url + "/account/freeze_for_order", Results.class, null, null, JSON.toJSONString(map));
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
			logger.error("用户："+userCode+" 订单: "+orderCode+" 锁币失败:",e);
			return;
		}

		if (result.getCode() != 0) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "锁币失败:" + result.getMessage());
			return;
		}

		delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_SUCCESS);
		logger.debug("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "锁币成功");

		orderService.push2match(delegateEvent);
		delegateEvent.setDelegateEventstatus(DelegateEventstatus.PUBLISH_ORDER_SUCCESS);
		logger.debug("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "锁币成功");
	}

	/**
	 * 记录托单流程log
	 */
	@Transactional(readOnly = false)
	public void savePublishOrdereEvent(DelegateEvent delegateEvent, long sequence, boolean endOfBatch) {
		if (DelegateEventstatus.INSERT_ORDERDB_ERROR.equals(delegateEvent.getDelegateEventstatus())) {
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "插入托单数据未成功,账户锁币&MQ入库&变更托单状态未执行,记录托单流程log未执行");
			return;
		}

		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setOrderCode(delegateEvent.getOrderCode());
		orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());

		if (DelegateEventstatus.PUBLISH_ORDER_SUCCESS.equals(delegateEvent.getDelegateEventstatus())) {
			orderEvent.setStatus(OrderStatus.TRADING);
		} else {
			orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
		}
		orderEventMapper.insertOrderEvent(orderEvent);
		logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "记录托单流程log插入成功");
	}


}
