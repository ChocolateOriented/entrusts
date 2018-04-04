package com.entrusts.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.entrusts.manager.DealmakingClient;
import com.entrusts.manager.MillstoneClient;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.DelCancelOrder;
import com.entrusts.module.dto.FreezeDto;
import com.entrusts.module.dto.UnfreezeEntity;
import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.OrderEvent;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;



/**
 * Created by cyuan on 2018/3/13.
 */
@Service
@Transactional
public class OrderCancelService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCancelService.class);
    @Autowired
    private DealmakingClient dealmakingClient;
    @Autowired
    private MillstoneClient millstoneClient;
    @Autowired
    private TradePairService tradePairService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderEventService orderEventService;
    @Value("${sleep.time}")
    private Long sleepTime;
    public CommonResponse<Order> cancelOrder(String orderCode,String userCode) {

        UnfreezeEntity unfreezeEntity = queryUnfreezeInfo(orderCode,userCode);
        if(unfreezeEntity == null){
            logger.info("订单号:"+orderCode+",没有此订单");
            return null;
        }

        CommonResponse<Order> orderCommonResponse = toCancelOrder(unfreezeEntity);

        return orderCommonResponse;
    }
    public UnfreezeEntity queryUnfreezeInfo(String orderCode,String userCode){
        //获取对应的order
        Order order = orderMapper.queryUnfreezeInfo(orderCode,userCode);
        if(order == null){
            return null;
        }
        TradePair tradePairById = tradePairService.findTradePairById(order.getTradePairId());
        UnfreezeEntity unfreezeEntity = new UnfreezeEntity();
        unfreezeEntity.setBaseCurrencyId(tradePairById.getBaseCurrencyId());
        unfreezeEntity.setTargetCurrencyId(tradePairById.getTargetCurrencyId());
        unfreezeEntity.setOrder(order);
        return unfreezeEntity;
    }
    public List<CommonResponse<Order>> cancelAll(String userCode) {

        Map<OrderStatus,List<CommonResponse<Order>>> map = new HashMap<>();
        List<CommonResponse<Order>> cancelOrder = new ArrayList<>();
        List<UnfreezeEntity> unfreezeEntities = queryAllUnfreezeInfo(userCode);
        logger.info("用户id:"+userCode+"获取到此人的可取消订单数量"+unfreezeEntities.size());
        if(unfreezeEntities == null || unfreezeEntities.size() == 0){
            return null;
        }
        for(UnfreezeEntity unfreezeEntity : unfreezeEntities){
            CommonResponse<Order> orderCommonResponse = toCancelOrder(unfreezeEntity);
            cancelOrder.add(orderCommonResponse);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return cancelOrder;
    }
    public List<UnfreezeEntity> queryAllUnfreezeInfo(String userCode){
        List<Order> orderList = orderMapper.queryAllUnfreezeInfo(userCode);
        List<UnfreezeEntity> unfreezeEntityList = new ArrayList<>();
        for (Order o : orderList){
            TradePair tradePairById = tradePairService.findTradePairById(o.getTradePairId());
            if(tradePairById == null){
                continue;
            }
            UnfreezeEntity unfreezeEntity = new UnfreezeEntity();
            unfreezeEntity.setOrder(o);
            unfreezeEntity.setTargetCurrencyId(tradePairById.getTargetCurrencyId());
            unfreezeEntity.setBaseCurrencyId(tradePairById.getBaseCurrencyId());
            unfreezeEntityList.add(unfreezeEntity);
        }
        return unfreezeEntityList;
    }
    /**
     * 单个撤销订单
     * @param unfreezeEntity
     * @return
     */
    public CommonResponse<Order> toCancelOrder(UnfreezeEntity unfreezeEntity){
        Order order = unfreezeEntity.getOrder();
        //调用搓单系统取消订单
        String s = delCancelOrder(unfreezeEntity);
        CommonResponse<Order> response = JSON.parseObject(s, new TypeReference<CommonResponse<Order>>() {});

        logger.info("订单号:"+unfreezeEntity.getOrder().getOrderCode()+s);
        if(s == null || !response.isSuccess()){

            logger.info("订单号:"+unfreezeEntity.getOrder().getOrderCode()+",调用通知撮单系统撤销接口失败");
            response.setData(order);
            return response;
        }

        //解锁
        String s1 = unfreezeForOrder(unfreezeEntity);
        logger.info("解锁货币订单号:"+unfreezeEntity.getOrder().getOrderCode()+s1);
        if(s1 == null || (Integer)JSON.parseObject(s1).get("code") != 0){
            logger.info("订单号:"+unfreezeEntity.getOrder().getOrderCode()+",撮单系统取消成功,但是货币解锁失败");
            order = updateOrderAfterCancel(unfreezeEntity,OrderStatus.WITHDRAW_UNTHAWING);
        }else {
            //修改数据库状态
            order = updateOrderAfterCancel(unfreezeEntity,OrderStatus.WITHDRAW);
        }
        response.setData(order);
        return response;
    }

    /**
     * 撤销后更新数据库
     * @param unfreezeEntity
     * @return
     */
    public Order updateOrderAfterCancel(UnfreezeEntity unfreezeEntity,OrderStatus orderStatus){

        Order order = unfreezeEntity.getOrder();
        orderMapper.updateOrderStatus(orderStatus, order.getOrderCode(), new Date());
        order.setStatus(orderStatus);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderCode(order.getOrderCode());
        orderEvent.setStatus(order.getStatus());
        orderEvent.setDealAmout(order.getDealAmount());
        orderEvent.setDealQuantity(order.getDealQuantity());
        orderEvent.setLastedDealTime(order.getLastedDealTime());
        orderEventService.save(orderEvent);
        return order;
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
        Order order = unfreezeEntity.getOrder();
        Date createdTime = order.getCreatedTime();
        Instant instant = createdTime.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime createdDate = LocalDateTime.ofInstant(instant, zone);
        DelCancelOrder delCancelOrder = new DelCancelOrder();
        delCancelOrder.setOrderCode(order.getOrderCode());
        delCancelOrder.setPrice(order.getConvertRate());
        delCancelOrder.setQuantity(order.getQuantity());
        delCancelOrder.setOrderType(order.getMode().name());
        delCancelOrder.setTargetCurrencyId(unfreezeEntity.getTargetCurrencyId());
        delCancelOrder.setUserCode(order.getUserCode());
        delCancelOrder.setMarketId(order.getTradePairId());
        delCancelOrder.setBaseCurrencyId(unfreezeEntity.getBaseCurrencyId());
        delCancelOrder.setTradeType(order.getTradeType().name());
        delCancelOrder.setCreatedTime(createdDate);
        String delOrder = JSON.toJSONString(delCancelOrder, SerializerFeature.UseISO8601DateFormat);
        String s = dealmakingClient.delCancelOrder(delOrder);

//        String s = "{\n" +
//                "  \"code\": 0,\n" +
//                "  \"message\": \"ok\"\n" +
//                "}";
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
     *///, path = "/dealmaking"
    public String unfreezeForOrder(UnfreezeEntity unfreezeEntity){
        Order order = unfreezeEntity.getOrder();
        TradeType tradeType = order.getTradeType();
        Integer encryptCurrencyId =null;
        BigDecimal quantity = null;
        if(tradeType.getValue() == 1){
            //说明是买方,基准货币金额相减
            encryptCurrencyId=unfreezeEntity.getBaseCurrencyId();
            //托单单价*(托单总数量-已成交数量)
            quantity = order.getConvertRate().multiply(order.getQuantity().subtract(order.getDealAmount()==null?new BigDecimal(0):order.getDealAmount()));
        }else {
            //说明是卖方,托单总数量-已成交数量
            encryptCurrencyId=unfreezeEntity.getTargetCurrencyId();
            quantity= order.getQuantity().subtract(order.getDealQuantity() == null? new BigDecimal(0) : order.getDealQuantity());
        }
//        Map<String,Object>  map = new HashMap<>();
//        map.put("orderCode",unfreezeEntity.getOrder().getOrderCode());
//        map.put("userCode",unfreezeEntity.getOrder().getUserCode());
//        map.put("encryptCurrencyId",encryptCurrencyId);
//        map.put("quantity",quantity);
        FreezeDto freezeDto = new FreezeDto();
        freezeDto.setOrderCode(order.getOrderCode());
        freezeDto.setUserCode(order.getUserCode());
        freezeDto.setEncryptCurrencyId(encryptCurrencyId);
        freezeDto.setQuantity(quantity);

        String s = millstoneClient.unfreezeForOrder(freezeDto);
//        String s = "{\n" +
//                "  \"code\": 0,\n" +
//                "  \"message\": \"ok\"\n" +
//                "}";
        return s;
    }



}
