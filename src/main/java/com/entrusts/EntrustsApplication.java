package com.entrusts;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.entrusts.mapper")
public class EntrustsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntrustsApplication.class, args);
	}

}
