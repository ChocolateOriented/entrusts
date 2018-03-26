package com.entrusts.service;

import com.entrusts.exception.ApiException;
import com.entrusts.manager.MillstoneClient;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.FreezeDto;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderStatus;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelegateEventHandler extends BaseService {

	@Autowired
	private OrderService orderService;
	@Autowired
	private MillstoneClient millstoneClient;

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
			delegateEvent.setRemark(e.getMessage());
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "新增托单数据失败:", e);
			return;
		}

		logger.debug("用户：{}订单:{} 新增托单数据成功", userCode, orderCode);
		//锁币
		try {
			this.lockCoin(delegateEvent);
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
			delegateEvent.setRemark(e.getMessage());
			orderService.updateOrderStatus(OrderStatus.DELEGATE_FAILED,orderCode,userCode);
			logger.info("用户：" + userCode + " 订单: " + orderCode + " 锁币失败:" + e.getMessage(), e);
			return;
		}
		logger.debug("用户：" + userCode + "订单:" + orderCode + "锁币成功");

		//通知撮合系统
		try {
			orderService.push2Match(delegateEvent);
		} catch (Exception e) {
			delegateEvent.setRemark(e.getMessage());
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.PUSH_MATCH_ERROR);
			orderService.updateOrderStatus(OrderStatus.DELEGATE_FAILED,orderCode,userCode);
			logger.info("用户：" + userCode + "订单:" + orderCode + "通知撮合系统失败", e);
			return;
		}

		delegateEvent.setDelegateEventstatus(DelegateEventstatus.PUBLISH_ORDER_SUCCESS);
		logger.debug("用户：" + userCode + "订单:" + orderCode + "托单成功");
	}

	/**
	 * 锁币
	 * 买入时锁基础货币, 数量=比率*目标货币数量
	 * 卖出时锁目标货币
	 */
	private void lockCoin(DelegateEvent delegateEvent) throws ApiException {
		Integer lockCurrencyId ;
		BigDecimal lockQuantity ;
		switch (delegateEvent.getTradeType()){
			case buy:
				lockCurrencyId = delegateEvent.getBaseCurrencyId();
				lockQuantity = delegateEvent.getQuantity().multiply(delegateEvent.getConvertRate());
				break;
			default:
				lockCurrencyId = delegateEvent.getTargetCurrencyId();
				lockQuantity = delegateEvent.getQuantity();
		}

		FreezeDto freezeDto = new FreezeDto(delegateEvent.getOrderCode(),delegateEvent.getUserCode(),lockCurrencyId,lockQuantity);
		Results result = millstoneClient.freezeForOrder(freezeDto);
		if (result.getCode() != 0) {
			throw new ApiException(result.getMessage());
		}
	}
}
