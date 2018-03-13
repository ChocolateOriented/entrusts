package com.entrusts.mapper;

import com.entrusts.module.entity.EntMqMessage;
import java.util.List;

/**
 * Created by yuanchao  
 */

public interface EntMqMessageMapper  {

	/**
	 * @Description 查询需要重发的消息(MsgId为Null的消息)
	 * @param
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.EntMqMessage>
	 */
	List<EntMqMessage> findResendMessages();

	void insert(EntMqMessage entMqMessage);

	void update(EntMqMessage entMqMessage);
}
