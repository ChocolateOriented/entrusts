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
	 * @return void
	 * @Description 批量处理委托日志, 日志处理使用单线程, BatchEventProcessor每批取到的数据一起插入, 最多每次插入1000条
	 */
	@Override
	public void onEvent(DelegateEvent delegateEvent, long sequence, boolean isLast) throws Exception {
		//未到该批次最后一个 且 List未满
		logger.debug("收到日志" + delegateEvent.getOrderCode());
		OrderEvent orderEvent = new OrderEvent();
		orderEvent.setOrderCode(delegateEvent.getOrderCode());
		orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
		orderEvent.setRemark(delegateEvent.getRemark());
		orderEvent.setId(super.generateId());

		if (DelegateEventstatus.PUBLISH_ORDER_SUCCESS.equals(delegateEvent.getDelegateEventstatus())) {
			orderEvent.setStatus(OrderStatus.TRADING);
		} else {
			orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
		}
		orderEvents.add(orderEvent);

		if (!isLast && orderEvents.size() < maxListSize) {//等待同一插入
			return;
		}

		try {
			orderEventMapper.batchInsert(orderEvents);
		} catch (Exception e) {
			StringBuilder lostMsg = new StringBuilder(orderEvents.size() * 30);
			for (OrderEvent event : orderEvents) {
				lostMsg.append("," + event.getOrderCode());
			}
			logger.info("批量记录托单流程log插入失败"+lostMsg, e);
		}
		logger.debug("批量记录托单流程log插入成功" + orderEvents.size());
		orderEvents.clear();
	}


	public void save(OrderEvent orderEvent) {
		orderEvent.setId(super.generateId());
		orderEventMapper.insertOrderEvent(orderEvent);
	}
}
