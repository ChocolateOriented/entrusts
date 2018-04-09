package com.entrusts.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class JedisConfig {

	private String host;

	private String host2;

	private int port;

	private String password;

	private String password2;

	private int timeout;

	private int database;

	private int database2;

	private int maxTotal;

	private int maxWait;

	private int maxIdle;

	private int minIdle;

	public String getHost2() {
		return host2;
	}

	public void setHost2(String host2) {
		this.host2 = host2;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getDatabase2() {
		return database2;
	}

	public void setDatabase2(int database2) {
		this.database2 = database2;
	}

	@Bean(name = "commonRedisPool")
	public JedisPool commonRedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
		return pool;
	}

	@Bean
	public RedisConnectionFactory nestRedisConnectionFactory(){
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		jedisConnectionFactory.setHostName(host2);
		if(!password2.isEmpty()){
			jedisConnectionFactory.setPassword(password2);
		}
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setDatabase(database2);
		return jedisConnectionFactory;
	}

	/**
	 *  用户中心redis模板操作类
	 */
	@Bean(name = "nestRedisTemplate")
	public RedisTemplate<String, Object> nestRedisTemplate(@Autowired RedisConnectionFactory nestRedisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(nestRedisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashKeySerializer(template.getKeySerializer());
		template.setHashValueSerializer(template.getValueSerializer());
		return template;
	}

}
