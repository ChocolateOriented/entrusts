package com.entrusts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cyuan on 2018/3/20.
 */
@Configuration
public class ExecutorConfig {
    @Bean(destroyMethod = "shutdown",value = "orderCancelExecutorService")
    public ExecutorService orderCancelExecutor(){
        return new ThreadPoolExecutor(80,100,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(80));
    }
}
