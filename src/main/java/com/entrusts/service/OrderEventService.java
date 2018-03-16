package com.entrusts.service;

import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderStatus;
import com.lmax.disruptor.EventHandler;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sxu
 */
@Service
public class OrderEventService extends BaseService implements EventHandler<DelegateEvent> {
	@Autowired
	private OrderEventMapper orderEventMapper;

	/**
	 * 仅由onEvent方法使用, 供Disruptor单线程调用
	 */
	final int maxListSize = 1000;
	private final ArrayList<OrderEvent> orderEvents = new ArrayList<>(maxListSize);

	/**
	 * @Description 批量处理委托日志, 日志处理使用单线程, BatchEventProcessor每批取到的数据一起插入, 最多每次插入1000条
	 * @param delegateEvent
	 * @param sequence
	 * @param isLast
	 * @return void
	 */
	@Override
	public void onEvent(DelegateEvent delegateEvent, long sequence, boolean isLast) throws Exception {
		//未到该批次最后一个 且 List未满
		if (!isLast && orderEvents.size() < maxListSize) {
			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setOrderCode(delegateEvent.getOrderCode());
			orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
			orderEvent.setId(super.generateId());

			if (DelegateEventstatus.PUBLISH_ORDER_SUCCESS.equals(delegateEvent.getDelegateEventstatus())) {
				orderEvent.setStatus(OrderStatus.TRADING);
			} else {
				orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
			}
			orderEvents.add(orderEvent);
			return;
		}

		orderEventMapper.batchInsert(orderEvents);
		orderEvents.clear();
		logger.debug("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "记录托单流程log插入成功");
	}


	public void save(OrderEvent orderEvent){
		orderEvent.setId(super.generateId());
		orderEventMapper.insertOrderEvent(orderEvent);
	}
}
