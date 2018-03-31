package com.entrusts.service;


import com.mo9.mqclient.IMqOrderProducer;
import com.mo9.mqclient.MqMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yuanchao
 * 消息队列服务
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class EntMqMessageService extends BaseService {
	@Autowired
	IMqOrderProducer mqOrderProducer;

	/**
	 * @Description 发送有序消息到消息队列
	 * @param topic 消息主题
	 * @param tag 消息标签(主题下的分类)
	 * @param key 业务主键
	 * @param body 消息体
	 * @return void
	 */
	public void orderSend (String topic, String tag, String key, String body, String shardingKey) {
		MqMessage message = new MqMessage(topic, tag, key,body);
		mqOrderProducer.send(message, shardingKey);
		logger.info("{} {} {} {} {} 有序mq发送成功", topic, tag, key, body, shardingKey);
	}
}
