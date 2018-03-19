package com.entrusts.config;

import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.service.DelegateEventHandler;

import com.entrusts.service.OrderEventService;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
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
	@Autowired
	private OrderEventService orderEventService;

	@Value("${disruptor.delegateEvent.bufferSize}")
	private int delegateEventBufferSize;//委托事件任务队列大小
	@Value("${disruptor.delegateEvent.publishOrderThreadNum}")
	private int publishOrderThreadNum;//发布托单线程数

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

		WorkHandler<DelegateEvent>[] publishOrderWorkers = new WorkHandler[publishOrderThreadNum];
		for (int i = 0; i < publishOrderThreadNum; i++) {
			publishOrderWorkers[i] = eventHandler::publishOrder;
		}

		disruptor.handleEventsWithWorkerPool(publishOrderWorkers).then(orderEventService);
		return disruptor;
	}
}
