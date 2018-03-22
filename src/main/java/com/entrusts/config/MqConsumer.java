package com.entrusts.config;

import com.entrusts.listener.DefaultListener;
import com.entrusts.util.Mo9MqConsumer;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqSubscription;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mq")
public class MqConsumer {

	private String millstoneConsumerId;

	private String accessKey;

	private String secretKey;

	private String onsAddr;

	private String messageModelString;

	private int consumeThreadNums;

	private String millstoneTopic;

	@Autowired
	private DefaultListener millstoneListener;

	public String getMillstoneConsumerId() {
		return millstoneConsumerId;
	}

	public void setMillstoneConsumerId(String millstoneConsumerId) {
		this.millstoneConsumerId = millstoneConsumerId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getOnsAddr() {
		return onsAddr;
	}

	public void setOnsAddr(String onsAddr) {
		this.onsAddr = onsAddr;
	}

	public String getMessageModelString() {
		return messageModelString;
	}

	public void setMessageModelString(String messageModelString) {
		this.messageModelString = messageModelString;
	}

	public int getConsumeThreadNums() {
		return consumeThreadNums;
	}

	public void setConsumeThreadNums(int consumeThreadNums) {
		this.consumeThreadNums = consumeThreadNums;
	}

	public String getMillstoneTopic() {
		return millstoneTopic;
	}

	public void setMillstoneTopic(String millstoneTopic) {
		this.millstoneTopic = millstoneTopic;
	}

	@Bean(initMethod = "start", destroyMethod = "shutdown")
	public Mo9MqConsumer millstoneConsumerFactory() {
		Mo9MqConsumer consumer = new Mo9MqConsumer();
		consumer.setConsumerId(millstoneConsumerId);
		consumer.setAccessKey(accessKey);
		consumer.setSecretKey(secretKey);
		consumer.setOnsAddr(onsAddr);
		consumer.setMessageModelString(messageModelString);
		consumer.setConsumeThreadNums(consumeThreadNums);
		consumer.setSubscriptionMap(instantiateListener());
		
		return consumer;
	}
	
	private Map<MqSubscription, IMqMsgListener> instantiateListener() {
		Map<MqSubscription, IMqMsgListener> subscriptionMap = new HashMap<>();

		MqSubscription subscription = new MqSubscription();
		subscription.setTopic(millstoneTopic);
		subscription.setExpression("*");
		IMqMsgListener listener = millstoneListener;
		subscriptionMap.put(subscription, listener);
		
		return subscriptionMap;
	}
}
