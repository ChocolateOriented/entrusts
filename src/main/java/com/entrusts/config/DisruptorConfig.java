package com.entrusts.config;

import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.service.DelegateEventHandler;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DisruptorConfig {

	@Autowired
	private DelegateEventHandler eventHandler;

	@Value("${disruptor.delegateEventBufferSize}")
	private int delegateEventBufferSize;

	/**
	 * 托单队列
	 * @return
	 */
	@Bean(name = "delegateDisruptor", autowire = Autowire.BY_NAME, initMethod = "start", destroyMethod = "shutdown")
	public Disruptor<DelegateEvent> delegateEventDisruptor(){
		//配置线程池
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		Disruptor<DelegateEvent> disruptor =
				new Disruptor<>(DelegateEvent::new, delegateEventBufferSize, threadFactory, ProducerType.MULTI, new SleepingWaitStrategy());

		EventHandlerGroup<DelegateEvent> orderEventHandlerGroup = disruptor.handleEventsWith(eventHandler::saveOrder);
		orderEventHandlerGroup.then(eventHandler::saveNewOrdereEvent);
		orderEventHandlerGroup.then(eventHandler::publishOrder).then(eventHandler::savePublishOrdereEvent);
		return disruptor;
	}
}
