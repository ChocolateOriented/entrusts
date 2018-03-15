package com.entrusts.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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

	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout, null, database);
		return pool;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setHostName(host);
		if(!password2.isEmpty()){
			jedisConnectionFactory.setPassword(password);
		}
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setDatabase(database);
		return jedisConnectionFactory;
	}
	@Bean
	public RedisConnectionFactory redisConnectionFactory2(){
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setHostName(host2);
		if(!password2.isEmpty()){
			jedisConnectionFactory.setPassword(password2);
		}
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setDatabase(database2);
		return jedisConnectionFactory;
	}


	@Bean(name = "redisTemplate1")
	public RedisTemplate<String, Object> redisTemplateObject() throws Exception {
		RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
		redisTemplateObject.setConnectionFactory(redisConnectionFactory());
		setSerializer(redisTemplateObject);
		redisTemplateObject.afterPropertiesSet();
		return redisTemplateObject;
	}

	/*@Bean(name = "redisTemplate2")
	public RedisTemplate<String, Object> redisTemplateObject2() throws Exception {
		RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
		redisTemplateObject.setConnectionFactory(redisConnectionFactory2());
		setSerializer(redisTemplateObject);
		redisTemplateObject.afterPropertiesSet();
		return redisTemplateObject;
	}*/

	@Bean(name = "redisTemplate2")
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory2());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashKeySerializer(template.getKeySerializer());
		template.setHashValueSerializer(template.getValueSerializer());
		return template;
	}



	private void setSerializer(RedisTemplate<String, Object> template) {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setKeySerializer(template.getStringSerializer());
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		//在使用String的数据结构的时候使用这个来更改序列化方式
        /*RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );
        template.setValueSerializer(stringSerializer );
        template.setHashKeySerializer(stringSerializer );
        template.setHashValueSerializer(stringSerializer );*/

	}
}
