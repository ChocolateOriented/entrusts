package com.entrusts.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.aliyun.openservices.ons.api.*;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqSubscription;
import com.mo9.mqclient.impl.aliyun.AliyunMessageModel;
import com.mo9.mqclient.impl.aliyun.AliyunMqConsumer;
import com.mo9.mqclient.impl.aliyun.AliyunMqMsgListener;

public class Mo9MqConsumer extends AliyunMqConsumer {

	/**
	 * 消费者ID
	 */
	private String consumerId;

	/**
	 * 阿里云身份验证accessKey，在阿里云服务器管理控制台创建
	 */
	private String accessKey;

	/**
	 * 阿里云身份验证secretKey，在阿里云服务器管理控制台创建
	 */
	private String secretKey;

	/**
	 * 阿里云服务地址
	 */
	private String onsAddr;

	/**
	 * 订阅方式
	 */
	private AliyunMessageModel messageModel;

	/**
	 * 消费者最大线程数,默认50
	 */
	private int consumeThreadNums = 50;

	private String messageModelString ;

	/**
	 * 订阅监听器map
	 */
	private Map<MqSubscription, IMqMsgListener> subscriptionIMqMsgListenerMap = new HashMap<MqSubscription, IMqMsgListener>();

	/**
	 * 消费者
	 */
	private Consumer consumer;

	public void start() {
		Properties producerProperties = new Properties();
		producerProperties.setProperty(PropertyKeyConst.ConsumerId, this.consumerId);
		producerProperties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
		producerProperties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
		producerProperties.setProperty(PropertyKeyConst.ONSAddr, this.onsAddr);
		producerProperties.setProperty(PropertyKeyConst.ConsumeThreadNums, Integer.toString(this.consumeThreadNums));

		if("CLUSTERING".equals(this.messageModelString)){
			producerProperties.setProperty(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
		}else{
			producerProperties.setProperty(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
		}

		this.consumer = ONSFactory.createConsumer(producerProperties);

		Set<Entry<MqSubscription, IMqMsgListener>> entrySet = this.subscriptionIMqMsgListenerMap
				.entrySet();
		for (Entry<MqSubscription, IMqMsgListener> entry : entrySet) {
			this.subscribe(entry.getKey(), entry.getValue());
		}

		while (true) {
			if (this.consumer.isClosed()) {
				this.consumer.start();
				return;
			}

		}

	}

	public void shutdown() {

		if (this.consumer != null) {
			this.consumer.shutdown();
		}

	}

	public void setSubscriptionMap(Map<MqSubscription, IMqMsgListener> subscriptionMap) {
		this.subscriptionIMqMsgListenerMap = subscriptionMap;
	}


	public boolean isStarted() {
		if (this.consumer != null) {
			return this.isStarted();
		} else {
			return false;
		}

	}

	public boolean isClosed() {

		if (this.consumer != null) {
			return this.isClosed();
		} else {
			return true;
		}
	}

	public void subscribe(MqSubscription subscription, IMqMsgListener mqMsgListener) {
		if (this.consumer != null) {
			AliyunMqMsgListener aliyunMQMsgListener = new AliyunMqMsgListener(mqMsgListener);
			aliyunMQMsgListener.setConsumerId(this.consumerId);
			this.consumer
					.subscribe(subscription.getTopic(), subscription.getExpression(), aliyunMQMsgListener);
		}
	}

	public void unsubscribe(String topic) {
		if (this.consumer != null) {
			this.consumer.unsubscribe(topic);
		}
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
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

	public AliyunMessageModel getMessageModel() {
		return messageModel;
	}

	public void setMessageModel(AliyunMessageModel messageModel) {
		this.messageModel = messageModel;
	}

	public int getConsumeThreadNums() {
		return consumeThreadNums;
	}

	public void setConsumeThreadNums(int consumeThreadNums) {
		this.consumeThreadNums = consumeThreadNums;
	}

	public String getMessageModelString() {
		return messageModelString;
	}

	public void setMessageModelString(String messageModelString) {
		this.messageModelString = messageModelString;
	}
}

