package com.entrusts.config;

import com.entrusts.module.dto.DelegateEvent;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by jxli on 2018/3/5.
 */
public class DisruptorConfig {
	@Value("${delegateEventBufferSize}")
	private int delegateEventBufferSize;

	@Bean("delegateEventBuffer")
	public RingBuffer<DelegateEvent> delegateEventDisruptor(){
		//配置线程池
		Executor executor = Executors.newCachedThreadPool();
		EventHandler storeHandler = (EventHandler<DelegateEvent>) (delegateEvent, sequence, endOfBatch) -> {
			//insertToDB
		};
		EventHandler logHandler = (EventHandler<DelegateEvent>) (delegateEvent, l, b) -> {
			//insertToDB
		};
		EventHandler lockCoinHandler = (EventHandler<DelegateEvent>) (delegateEvent, l, b) -> {
			//requestAccount
		};
		EventHandler mqHandler = (EventHandler<DelegateEvent>) (delegateEvent, l, b) -> {
			//publish Mq 2 match
			//update 2 trading
		};
		EventHandler successLogHandler = (EventHandler<DelegateEvent>) (delegateEvent, l, b) -> {
			//insertToDB
		};
		Disruptor disruptor= new Disruptor(DelegateEvent::new,delegateEventBufferSize,executor, ProducerType.MULTI, new SleepingWaitStrategy());

		disruptor.handleEventsWith(storeHandler).then(lockCoinHandler).then(mqHandler,successLogHandler);
		disruptor.handleEventsWith(logHandler);

		disruptor.start();
		return disruptor.getRingBuffer();
	}
}
