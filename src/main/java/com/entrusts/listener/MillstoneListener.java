package com.entrusts.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.entrusts.module.dto.DealNotify;
import com.entrusts.service.DealService;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;

@Component("millstoneListener")
public class MillstoneListener implements IMqMsgListener {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DealService dealService;

    @Value("${mq.millstoneDealTag}")
    private String TAG_DEAL;
    
    @Override
    public MqAction consume(MqMessage msg, Object consumeContext) {
        String tag = msg.getTag();
        if (TAG_DEAL.equals(tag)) {
            logger.info("接收成交通知msg");
            return customeTagDeal(msg);
        }
        
        return MqAction.ReconsumeLater;
    }

    public MqAction customeTagDeal(MqMessage msg) {
        try {
            String json = msg.getBody();
            json = "{\"code\":\"asdasd12312321\",\"bidOrder\":{\"userCode\":\"\",\"orderCode\":\"171620386742603776000022\",\"orderType\":\"\",\"tradeType\":\"\",\"createdTime\":10201010,\"tradeFee\":0.9,\"dealPrice\":0.8,\"dealEncryptCurrencyId\":1,\"tradeEncryptCurrencyQuantity\":17.0,\"tradeEncryptCurrencyId\":2,\"isInitiator\":1},\"askOrder\":{\"userCode\":\"\",\"orderCode\":\"171620386742603776000022\",\"orderType\":\"\",\"tradeType\":\"\",\"tradeFee\":0.9,\"createdTime\":10201010,\"dealPrice\":0.8,\"dealEncryptCurrencyId\":1,\"tradeEncryptCurrencyQuantity\":17.0,\"tradeEncryptCurrencyId\":2,\"isInitiator\":0}}";
            logger.info("成交信息：" + json);
            
            DealNotify dealNotify = JSON.parseObject(json, DealNotify.class);
            dealService.dealNotify(dealNotify);
            
        } catch (Exception e) {
            logger.info("接收成交信息异常", e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;

    }

}
