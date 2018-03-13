package com.entrusts.service;



import com.entrusts.mapper.EntMqMessageMapper;
import com.entrusts.module.entity.EntMqMessage;
import com.mo9.mqclient.IMqProducer;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jxli on 2017/11/10.
 * 消息队列服务
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class EntMqMessageService extends BaseService {
	@Autowired
	IMqProducer mqProducer;
	@Autowired
	EntMqMessageMapper dao;

	/**
	 * @Description 发送消息到消息队列, 并持久化到数据库, 若是失败, 定时重发
	 * @param topic 消息主题
	 * @param tag 消息标签(主题下的分类)
	 * @param key 业务主键
	 * @param body 消息体
	 * @return void
	 */
	@Transactional
	public void send (String topic, String tag, String key, String body) {
		EntMqMessage entMqMessage = new EntMqMessage(topic, tag, key, body);
		dao.insert(entMqMessage);
		this.sendMqMessage(entMqMessage);
	}

	/**
	 * @Description 定时重试推送消息
	 * @param
	 * @return void
	 */
	@Scheduled(cron = "0 0/10 * * * ?")
	@Transactional
	public void messageResendTask () {
		List<EntMqMessage> messages= dao.findResendMessages();
		logger.info("正在重发消息, 共"+messages.size()+"条");
		for (EntMqMessage entMqMessage : messages) {
			this.sendMqMessage(entMqMessage);
		}
	}

	/**
	 * @Description 推送消息
	 * @param entMqMessage
	 * @return void
	 */
	@Transactional
	private void sendMqMessage(EntMqMessage entMqMessage) {
		MqMessage message = new MqMessage(entMqMessage.getTopic(), entMqMessage.getTag(), entMqMessage.getKey(), entMqMessage.getBody());
		try {
			MqSendResult result = mqProducer.send(message);
			entMqMessage.setMsgId(result.getMessageId());
			dao.update(entMqMessage);
		} catch (Exception e) {//消息发送失败
			logger.info("消息发送失败,等待重发" + entMqMessage.toString(), e);
		}
	}

}
