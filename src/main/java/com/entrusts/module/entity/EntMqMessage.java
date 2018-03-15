package com.entrusts.module.entity;


import java.util.Date;

/**
 * Created by yuanchao
 * 消息队列实体类
 */
public class EntMqMessage {
	private Long id;
	private String topic;//消息主题
	private String tag;//消息标签(主题下的分类)
	private String key;//业务主键
	private String body;//消息体
	private String msgId;//消息ID
	private Date createdTime;
	private Date updatedTime;
	public EntMqMessage(String topic, String tag, String key, String body) {
		this.topic = topic;
		this.tag = tag;
		this.key = key;
		this.body = body;
	}

	public EntMqMessage() {
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "EntMqMessage{" +
				"id=" + id +
				", topic='" + topic + '\'' +
				", tag='" + tag + '\'' +
				", key='" + key + '\'' +
				", body='" + body + '\'' +
				", msgId='" + msgId + '\'' +
				", createdTime=" + createdTime +
				", updatedTime=" + updatedTime +
				'}';
	}
}
