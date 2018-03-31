package com.entrusts.config;

import com.mo9.mqclient.impl.aliyun.AliyunMqOrderProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mq")
public class MqOrderProducer {

	private String productId;

	private String accessKey;

	private String secretKey;

	private String onsAddr;

	private long sendMsgTimeoutMillis;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public long getSendMsgTimeoutMillis() {
		return sendMsgTimeoutMillis;
	}

	public void setSendMsgTimeoutMillis(long sendMsgTimeoutMillis) {
		this.sendMsgTimeoutMillis = sendMsgTimeoutMillis;
	}

	@Bean(initMethod = "init", destroyMethod = "destory")
	public AliyunMqOrderProducer producerFactory() {
		AliyunMqOrderProducer producer = new AliyunMqOrderProducer();
		producer.setProductId(productId);
		producer.setAccessKey(accessKey);
		producer.setSecretKey(secretKey);
		producer.setOnsAddr(onsAddr);
		producer.setSendMsgTimeoutMillis(sendMsgTimeoutMillis);
		return producer;
	}
}
