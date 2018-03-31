package com.entrusts.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.entrusts.interceptor.Signature;

@Configuration
@ConfigurationProperties(prefix = "sign")
public class SignatureConfig {

	private String privateKey;

	private Integer signEffectiveTime;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Integer getSignEffectiveTime() {
		return signEffectiveTime;
	}

	public void setSignEffectiveTime(Integer signEffectiveTime) {
		this.signEffectiveTime = signEffectiveTime;
	}

	@Bean
	public Signature SignatureFactory() {
		Signature signature = new Signature();
		signature.setPrivateKey(privateKey);
		signature.setSignEffectiveTime(signEffectiveTime);
		return signature;
	}
}
