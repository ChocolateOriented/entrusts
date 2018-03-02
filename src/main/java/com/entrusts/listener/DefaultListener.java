package com.entrusts.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;

/**
 * Created by sxu on 2018/2/4.
 */
@Component("defaultListener")
public class DefaultListener implements IMqMsgListener {


    protected Logger logger = LoggerFactory.getLogger(getClass());
	    
    @Override
    public MqAction consume(MqMessage msg, Object consumeContext) {

        return MqAction.ReconsumeLater;
    }

}
