package com.entrusts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.entrusts.util.SnowFlake;

@Configuration
public class IdGeneratorConfig {

	@Value("${datacenterId}")
	private long datacenterId;

	@Value("${machineId}")
	private long machineId;

	@Bean(name = "snowFlake")
	@Scope("prototype")
	public SnowFlake snowFlakeFactory() {
		return new SnowFlake(datacenterId, machineId);
	}
}
