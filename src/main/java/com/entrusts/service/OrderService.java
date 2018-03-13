package com.entrusts.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.entrusts.mapper.EntMqMessageMapper;
import com.entrusts.mapper.OrderEventMapper;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.EntMqMessage;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.util.RestTemplateUtils;
import com.mo9.mqclient.IMqProducer;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;

/**
 * Created by sxu  
 */
@Service
@Transactional
public class OrderService extends BaseService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public static final int DATABASE_OPERATION_SUCCESS = 1; //数据库操作成功
    
    
	
	@Value("${url.xxxx}")
    private String url;
	
	@Value("${mq.delegatePushTopic}")
    private String delegatePushTopic;
	
	@Value("${mq.delegatePushTag}")
    private String delegatePushTag;
	
	@Value("${mq.secretKey}")
    private String secretKey;
	
	@Autowired
    private OrderMapper orderMapper;
	
	@Autowired
    private OrderEventMapper orderEventMapper;
	
	@Autowired
	private IMqProducer mqProducer;
	@Autowired
	private EntMqMessageMapper mqMessageMapper;
	
	
	/**
     * 插入托单数据
     */
    @Transactional(readOnly = false)
    public void insertOrder(DelegateEvent delegateEvent,long sequence,boolean endOfBatch){
    	try {
    		Order order = new Order();
    		BeanUtils.copyProperties(delegateEvent, order);
    		int num = orderMapper.insertOrder(order);
			if (DATABASE_OPERATION_SUCCESS == num) {
				delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_SUCCESS);
				logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"新增托单数据成功");
			}else{
				delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_ERROR);
				logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"新增托单数据未保存");
			}
    	} catch (Exception e) {
    		delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_ERROR);
	       	e.printStackTrace();
	       	logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"新增托单数据失败:",e);
        } 
    }
    
    /**
     *  插入托单log数据
     */
    @Transactional(readOnly = false)
    public void insertOrdereEvent(DelegateEvent delegateEvent,long sequence,boolean endOfBatch){
    	try {
    		OrderEvent orderEvent = new OrderEvent();
    		orderEvent.setOrderCode(delegateEvent.getOrderCode());
    		orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
    		if(DelegateEventstatus.INSERT_ORDERDB_SUCCESS == delegateEvent.getDelegateEventstatus()){
    			orderEvent.setStatus(OrderStatus.DELEGATING);
//    			orderEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_SUCCESS);
    		}else{
    			orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
    		}
    		int num =orderEventMapper.insertOrderEvent(orderEvent);
    		if (DATABASE_OPERATION_SUCCESS == num) {
//				delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDEREEVENT);
				logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"托单数据log插入成功");
			}else{
				logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"托单数据log未保存");
			}
    	} catch (Exception e) {
	       	e.printStackTrace();
	       	logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"托单数据log插入失败:",e);
        }
    }
    
    /**
     *  账户锁币&MQ入库&变更托单状态
     */
    @Transactional(readOnly = false)
    public void requestAccountAndInsertMqOrderAndUpdateOrder(DelegateEvent delegateEvent,long sequence,boolean endOfBatch){
    	
    	if(DelegateEventstatus.INSERT_ORDERDB_SUCCESS == delegateEvent.getDelegateEventstatus()){
//    		 String url = "http://192.168.6.27:3000/mock/111/api/millstone/v1/account/freeze_for_order";
    		//String result = PostRequest.postRequest(this.url+"/activity/unfreeze", para);
    		/**   账户锁币        */
	    	 try {
	    		 Map<String,Object> map  = new HashMap<>();
	    		 map.put("orderCode",delegateEvent.getOrderCode());
	    		 map.put("userCode",delegateEvent.getUserCode());
	    		 map.put("encryptCurrencyId",delegateEvent.getTargetCurrencyId());
	    		 map.put("quantity",delegateEvent.getQuantity());
	    		 
	             Results result = RestTemplateUtils.post(this.url+"/account/freeze_for_order", Results.class,null, null, JSON.toJSONString(map));
	             if (result.getCode() == 0){
	            	 delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_SUCCESS);
	                 logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"锁币成功");
	                 
	                 insertMqPush(delegateEvent);
	                 
	                 updateOrderStatus(delegateEvent);
	                 
	             } else {
	            	 delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
	                 logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"锁币失败:"+result.getMessage());
	             }
	         } catch (Exception e) {
	        	 delegateEvent.setDelegateEventstatus(DelegateEventstatus.RREQUESTACCOUNT_ERROR);
	        	 e.printStackTrace();
	        	 logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"锁币失败:",e);
	         }
	    	 
	    	 delegateEvent.setDelegateEventstatus(DelegateEventstatus.ORDER_SUCCESS);
	    	 
    	}else{
    		delegateEvent.setDelegateEventstatus(DelegateEventstatus.ORDER_ERROR);
    		logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"新增托单数据未成功,账户锁币&MQ入库&变更托单状态未执行");
    	}
    	
    }
    
    /**   
     * MQ入库   
     */
	public void insertMqPush(DelegateEvent delegateEvent) {
		try {
			JSONObject body = new JSONObject();
			body.put("orderCode", value);
			body.put("price", value);
			body.put("quantity", value);
			body.put("orderType", value);
			body.put("userCode", value);
			body.put("marketId", value);
			body.put("baseCurrencyId", value);
			body.put("tradeType", value);
			body.put("createdTime", value);
			body.put("updatedTime", value);
			body.put("status", value);
			body.put("isInitiator", "0");
			// INSERT__MQPUSH_SUCCESS
			String json = JSON.toJSONString(stu);
			EntMqMessage entMqMessage = new EntMqMessage(delegatePushTopic, delegatePushTag, secretKey, body);
			mqMessageMapper.insert(entMqMessage);
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_MQPUSH_SUCCESS);
			logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "MQ入库成功");
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_MQPUSH_ERROR);
			e.printStackTrace();
			logger.error("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "MQ入库  失败:", e);
			return;
		}
	}
    
    
    /**  
     * 更新托单状态(交易中)
     */
	public void updateOrderStatus(DelegateEvent delegateEvent) {
		try {
			
			int num = orderMapper.updateOrderStatus(OrderStatus.TRADING,delegateEvent.getOrderCode());
			if(DATABASE_OPERATION_SUCCESS == num){
				delegateEvent.setDelegateEventstatus(DelegateEventstatus.UPDATE_ORDERDB_SUCCESS);
				logger.info("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "更新托单状态(交易中)成功");
			}
		} catch (Exception e) {
			delegateEvent.setDelegateEventstatus(DelegateEventstatus.UPDATE_ORDERDB_ERROR);
			e.printStackTrace();
			logger.error("用户：" + delegateEvent.getUserCode() + "订单:" + delegateEvent.getOrderCode() + "更新托单状态(交易中)失败:",e);
			return;
		}
	}
    
    /**
     *  记录托单流程log
     */
    @Transactional(readOnly = false)
    public void delegateLogHandler(DelegateEvent delegateEvent,long sequence,boolean endOfBatch){
    	
    	if(DelegateEventstatus.ORDER_ERROR != delegateEvent.getDelegateEventstatus()){
    		try {
        		OrderEvent orderEvent = new OrderEvent();
        		orderEvent.setOrderCode(delegateEvent.getOrderCode());
        		orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
        		
        		if(DelegateEventstatus.ORDER_SUCCESS == delegateEvent.getDelegateEventstatus()){
        			orderEvent.setStatus(OrderStatus.TRADING);
//        			orderEvent.setDelegateEventstatus(DelegateEventstatus.ORDER_SUCCESS);
        		}else{
        			orderEvent.setStatus(OrderStatus.DELEGATE_FAILED);
//        			orderEvent.setDelegateEventstatus(delegateEvent.getDelegateEventstatus());
        		}
        		int num =orderEventMapper.insertOrderEvent(orderEvent);
        		if (1 == num) {
    				logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"记录托单流程log插入成功");
    			}else{
    				logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"记录托单流程log未保存");
    			}
        	} catch (Exception e) {
    	       	e.printStackTrace();
    	       	logger.error("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"记录托单流程log失败:",e);
            }
    	}else{
    		logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"插入托单数据未成功,账户锁币&MQ入库&变更托单状态未执行,记录托单流程log未执行");
    	}
    }
    
    /**
     *  推送mq更新托单状态
     */
    @Transactional(readOnly = false)
    public void mqPushForOrder(DelegateEvent delegateEvent,long sequence,boolean endOfBatch){
    	if(DelegateEventstatus.ORDER_SUCCESS == delegateEvent.getDelegateEventstatus()){
    		//mqpush
    		MqMessage message = new MqMessage(delegatePushTopic, delegatePushTag, secretKey, entMqMessage.getBody());
    		try {
    			MqSendResult result = mqProducer.send(message);
    			entMqMessage.setMsgId(result.getMessageId());
    			mqMessageMapper.update(entMqMessage);
    		} catch (Exception e) {//消息发送失败
    			logger.info("消息发送失败,等待重发" + entMqMessage.toString(), e);
    		}
    	}
    }
    
    
    
//    public static void main(String[] args) {
//    	try {
//            String result = HttpClientUtil.httpPostRequest(url,map);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            logger.info("用户："+ delegateEvent.getUserCode() +"订单:" + delegateEvent.getOrderCode() +"锁币失败:",e);
//        }
//	}

	
}
