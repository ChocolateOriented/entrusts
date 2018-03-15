package com.entrusts.service;

import com.alibaba.fastjson.JSON;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.UnfreezeEntity;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cyuan on 2018/3/13.
 */
@Service
@Transactional
public class OrderCancelService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCancelService.class);
    @Value("${url.urldealmaking}")
    private String urldealmaking;

    @Autowired
    private OrderMapper orderMapper;
    public Boolean cancelOrder(String orderCode) {

        UnfreezeEntity unfreezeEntity = orderMapper.queryUnfreezeInfo(orderCode);
        if(unfreezeEntity == null){
            logger.info("订单号:"+orderCode+",没有此订单");
            return false;
        }

        Boolean aBoolean = toCancelOrder(unfreezeEntity);
        return aBoolean;
    }

    public Boolean cancelAll(String userCode) {
        Boolean flag = true;
        List<UnfreezeEntity> unfreezeEntities = orderMapper.queryAllUnfreezeInfo(userCode);
        if(unfreezeEntities == null || unfreezeEntities.size() == 0){
            return false;
        }
        for(UnfreezeEntity unfreezeEntity : unfreezeEntities){
            Boolean aBoolean = toCancelOrder(unfreezeEntity);
            if(!aBoolean){
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 单个撤销订单
     * @param unfreezeEntity
     * @return
     */
    public Boolean toCancelOrder(UnfreezeEntity unfreezeEntity){
        //调用搓单系统取消订单
        String s = delCancelOrder(unfreezeEntity);
        if(s == null || (Integer)JSON.parseObject(s).get("code") != 0){
            logger.info("订单号:"+unfreezeEntity.getOrder().getOrderCode()+",调用通知撮单系统撤销接口失败");
            return false;
        }

        //修改数据库状态
        Integer integer = updateOrderAfterCancel(unfreezeEntity);

        //解锁
        String s1 = unfreezeForOrder(unfreezeEntity);
        if(s1 == null || (Integer)JSON.parseObject(s1).get("code") != 0){
            logger.info("订单号:"+unfreezeEntity.getOrder().getOrderCode()+",撮单系统取消成功,但是货币解锁失败");
            Order order = unfreezeEntity.getOrder();
            orderMapper.updateOrderStatus(OrderStatus.WITHDRAW_UNTHAWING,order.getOrderCode());
            return false;
        }
        return true;
    }

    /**
     * 撤销后更新数据库
     * @param unfreezeEntity
     * @return
     */
    public Integer updateOrderAfterCancel(UnfreezeEntity unfreezeEntity){
        Order order = unfreezeEntity.getOrder();
        int i = orderMapper.updateOrderStatus(OrderStatus.WITHDRAW,order.getOrderCode());
        return i;
    }
    /**
     * 调用搓单系统取消订单
     * @param
     * @return
     * {
    "orderCode": 123,                      //订单code
    "price": 222,                     //订单单价
    "quantity": 123.58,                 //订单数量
    "orderType": "limit",             //订单类型
    "targetCurrencyId":111,                     //数字货币
    "userCode":1563,                  //用户code
    "marketId": 111,                 //市场
    "baseCurrencyId": 2333,               //流通货币
    "tradeType": "sell",              //交易类型
    "createdTime": "2019/02/28",        //创建时间

    }
     */
    public String delCancelOrder(UnfreezeEntity unfreezeEntity){
//        Map<String,Object> params = new HashMap<>();
//        params.put("orderCode",unfreezeEntity.getOrder().getOrderCode());
//        params.put("price",unfreezeEntity.getOrder().getConvertRate());
//        params.put("quantity",unfreezeEntity.getOrder().getQuantity());
//        params.put("orderType",unfreezeEntity.getOrder().getMode().name());
//        params.put("targetCurrencyId",unfreezeEntity.getTargetCurrencyId());
//        params.put("userCode",unfreezeEntity.getOrder().getUserCode());
//        params.put("marketId",unfreezeEntity.getOrder().getTradePairId());
//        params.put("baseCurrencyId",unfreezeEntity.getBaseCurrencyId());
//        params.put("tradeType",unfreezeEntity.getOrder().getTradeType().name());
//        params.put("createdTime",unfreezeEntity.getOrder().getCreatedTime());
//        Map<String,Object> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        String s = null;
//        try {
//            s = HttpClientUtil.httpPostRequest(urldealmaking+"/dealmaking/delcancel_order",headers,params);
//        } catch (UnsupportedEncodingException e) {
//            logger.info("调用通知撮单系统撤销接口失败",e);
//        }
        String s = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"ok\"\n" +
                "}";
        return s;
    }
    /**
     * 解锁/api/millstone/v1/account/unfreeze_for_order
     * {
     "orderCode": "123112",
     "userCode": "1231231",
     "encryptCurrencyId": 1,
     "quantity": 17.1
     }
     */
    public String unfreezeForOrder(UnfreezeEntity unfreezeEntity){
//        Map<String,Object>  map = new HashMap<>();
//        map.put("orderCode",unfreezeEntity.getOrder().getOrderCode());
//        map.put("userCode",unfreezeEntity.getOrder().getUserCode());
//        map.put("encryptCurrencyId",unfreezeEntity.getEncryptCurrencyId());
//        map.put("quantity",unfreezeEntity.getResiduequantity());
//        Map<String,Object> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        String s = null;
//        try {
//            s = HttpClientUtil.httpPostRequest("/api/millstone/v1/account/unfreeze_for_order",headers,map);
//        } catch (UnsupportedEncodingException e) {
//            logger.info("调用解锁接口失败",e);
//        }
        String s = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"ok\"\n" +
                "}";
        return s;
    }



}
