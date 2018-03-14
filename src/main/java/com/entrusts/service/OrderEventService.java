package com.entrusts.service;

import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.module.entity.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sxu
 */
@Service
public class OrderEventService extends BaseService {
	@Autowired
	private OrderEventMapper orderEventMapper;

	public void save(OrderEvent orderEvent){
		orderEvent.setId(super.generateId());
		orderEventMapper.insertOrderEvent(orderEvent);
	}
}
