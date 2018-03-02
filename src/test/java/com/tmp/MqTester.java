package com.tmp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.entrusts.EntrustsApplication;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;
import com.mo9.mqclient.impl.aliyun.AliyunMqProducer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntrustsApplication.class)
@EnableAutoConfiguration
public class MqTester {

	@Autowired
	private AliyunMqProducer producer;
	
	@Test
	public void testMq() {
		MqMessage message = new MqMessage("TOPIC_MO9_CLONE_MIS_CUSTOMER", "customerServiceFeedback_problem", "test123", "{id:\"123\", type:\"test\"}");
		MqSendResult result = producer.send(message);
		System.out.println(result.getMessageId());
	}
}
