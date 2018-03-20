package com.entrusts.config;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(FeignAutoConfiguration.class)
@ConfigurationProperties(prefix = "httpclient.pool")
public class FeignHttpClientConfig {

	private int maxTotal;

	private int defaultMaxPerRoute;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	@Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
    	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    	cm.setMaxTotal(maxTotal);
    	cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
    	return cm;
    }
    
    @Bean
    public HttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
    	CloseableHttpClient httpClient = HttpClients.custom()
    			.setConnectionManager(poolingHttpClientConnectionManager)
    			.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
    				@Override
    				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
    					long alive = super.getKeepAliveDuration(response, context);
    					if (alive == -1) {
    						// 没有设置keep alive值
    						alive = 500;
    					}
    					return alive;
    				}
    			}).build();
    	return httpClient;
	}
}
