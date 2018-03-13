package com.entrusts.config;

import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.service.OrderService;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DisruptorConfig {
	
	private OrderService orderService;

	@Value("${delegateEventBufferSize}")
	private int delegateEventBufferSize;

	/**
	 * 托单队列
	 * @return
	 */
	@Bean(name = "delegateDisruptor", autowire = Autowire.BY_NAME, initMethod = "start", destroyMethod = "shutdown")
	public Disruptor<DelegateEvent> delegateEventDisruptor(){
		//配置线程池
//		Executor executor = Executors.newCachedThreadPool();
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		Disruptor<DelegateEvent> disruptor = 
				new Disruptor<DelegateEvent>(DelegateEvent::new,delegateEventBufferSize,threadFactory, ProducerType.MULTI, new SleepingWaitStrategy());
//		EventHandlerGroup<DelegateEvent> orderEventHandlerGroup = 
//				disruptor.handleEventsWith(orderService::insertOrder);
////				.then(orderService::insertOrder);
//		orderEventHandlerGroup.then(orderService::insertOrdereEvent);
//		orderEventHandlerGroup.then(orderService::getRrequestAccountByFreezeForOrder)
//		.then(orderService::mqPushForOrderAndUpdateOrderStatus,orderService::successLogHandler);
////		disruptor.handleEventsWith(orderService::successLogHandler);
		
		EventHandlerGroup<DelegateEvent> orderEventHandlerGroup = 
				disruptor.handleEventsWith(orderService::insertOrder);
		orderEventHandlerGroup.then(orderService::insertOrdereEvent);
		orderEventHandlerGroup.then(orderService::requestAccountAndInsertMqOrderAndUpdateOrder)
		.then(orderService::mqPushForOrder,orderService::delegateLogHandler);
		
		return disruptor;
	}
	
	
	
}
