package com.tmp;

import com.entrusts.util.RedisUtil;
import java.util.UUID;
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

	private static final String key = "ceshi";

	@Test
	public void redisLockTest() {
		Runnable runnable = ()-> {
			String requestId = UUID.randomUUID().toString();
			System.out.println(Thread.currentThread().getId()+","+ "准备获取锁");
				boolean result = RedisUtil.tryLock(key,requestId);
				System.out.println(Thread.currentThread().getId()+","+ result);
				try {
					Thread.sleep(20*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (result){
					RedisUtil.releaseLock(key,requestId);
					System.out.println(Thread.currentThread().getId()+","+ "释放锁");
				}
		};


		Thread thread1 = new Thread(runnable);
		Thread thread2 = new Thread(runnable);
		thread1.start();
		thread2.start();
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
