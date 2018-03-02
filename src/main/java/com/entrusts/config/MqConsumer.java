package com.entrusts.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.entrusts.listener.DefaultListener;
import com.entrusts.util.Mo9MqConsumer;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqSubscription;

@Configuration
@ConfigurationProperties(prefix = "mq")
public class MqConsumer {

	private String consumerId;
	
	private String riskConsumerId;

	private String accessKey;

	private String secretKey;

	private String onsAddr;

	private String messageModelString;

	private int consumeThreadNums;

	private String dandelionTopic;

	private String riskTopic;

	@Autowired
	private DefaultListener defaultListener;

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getRiskConsumerId() {
		return riskConsumerId;
	}

	public void setRiskConsumerId(String riskConsumerId) {
		this.riskConsumerId = riskConsumerId;
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

	public String getDandelionTopic() {
		return dandelionTopic;
	}

	public void setDandelionTopic(String dandelionTopic) {
		this.dandelionTopic = dandelionTopic;
	}

	public String getRiskTopic() {
		return riskTopic;
	}

	public void setRiskTopic(String riskTopic) {
		this.riskTopic = riskTopic;
	}

	
	
}
