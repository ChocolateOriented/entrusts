package com.tmp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.entrusts.EntrustsApplication;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntrustsApplication.class)
@EnableAutoConfiguration
public class JedisTest {
	
	@Autowired
	private JedisPool jedisPool;
	
	@Test
	public void redisTest() {
		Jedis j = jedisPool.getResource();
		j.set("test", "123");
		System.out.println(j.get("test"));
		j.close();
	}
}
