package com.entrusts;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.entrusts.mapper")
public class EntrustsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntrustsApplication.class, args);
	}

}
