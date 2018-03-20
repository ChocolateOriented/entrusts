package com.entrusts.service;

import com.alibaba.fastjson.JSON;
import com.entrusts.exception.ApiException;
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

@Service
public class DelegateEventHandler extends BaseService {

	@Autowired
	private OrderService orderService;

	@Value("${url.lockCoin}")
	private String url;

	/**
	 * 发布托单
	 * 账户锁币&MQ入库&变更托单状态
	 */
	public void publishOrder(DelegateEvent delegateEvent) {
		String userCode = delegateEvent.getUserCode();
		String orderCode = delegateEvent.getOrderCode();

		//入库
		try {
			orderService.saveNewOrderByEvent(delegateEvent);
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_ERROR);
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "新增托单数据失败:", e);
			return;
		}

		logger.debug("用户：{}订单:{} 新增托单数据成功", userCode, orderCode);
		//锁币
		try {
//TODO 测试暂时注释,			this.lockCoin(delegateEvent);
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
			orderService.updateOrderStatus(OrderStatus.DELEGATE_FAILED,orderCode,userCode);
			logger.info("用户：" + userCode + " 订单: " + orderCode + " 锁币失败:" + e.getMessage(), e);
			return;
		}
		logger.debug("用户：" + userCode + "订单:" + orderCode + "锁币成功");

		//通知撮合系统
		try {
			orderService.push2Match(delegateEvent);
		} catch (Exception e) {
			orderService.updateOrderStatus(OrderStatus.DELEGATE_FAILED,orderCode,userCode);
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.PUSH_MATCH_ERROR);
			logger.info("用户：" + userCode + "订单:" + orderCode + "通知撮合系统失败", e);
			return;
		}

		delegateEvent.setDelegateEventstatus(DelegateEventstatus.PUBLISH_ORDER_SUCCESS);
		logger.debug("用户：" + userCode + "订单:" + orderCode + "托单成功");
	}

	/**
	 * 锁币
	 */
	private void lockCoin(DelegateEvent delegateEvent) throws ApiException {
		/**   账户锁币        */
		Map<String, Object> map = new HashMap<>();
		map.put("orderCode", delegateEvent.getOrderCode());
		map.put("userCode", delegateEvent.getUserCode());
		map.put("encryptCurrencyId", delegateEvent.getTargetCurrencyId());
		map.put("quantity", delegateEvent.getQuantity());

		Results result = RestTemplateUtils.post(this.url + "/account/freeze_for_order", Results.class, null, null, JSON.toJSONString(map));

		if (result.getCode() != 0) {
			throw new ApiException(result.getMessage());
		}
	}
}
