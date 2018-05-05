package com.entrusts.web;

import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.result.OrderMessage;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.service.OrderCancelService;
import com.entrusts.service.OrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cyuan on 2018/3/13.
 */
@RestController
@RequestMapping("entrusts/order")
public class OrderCancelController extends BaseController  {

    @Autowired
    private OrderCancelService orderCancelService;
    @Autowired
    private OrderManageService orderManageService;
    @Value("${adminKey}")
    private String adminKey;//只有请求中携带参数和此参数相等的时候才能进入cancelErrorOrder接口
    @Value("${adminFlag}")
    private String adminFlag;//只有配置文件里面此字段是true的时候才能进入cancelErrorOrder接口
    @PostMapping(value = "/cancel")
    public Object cancel(@RequestBody Order orderRequest, HttpServletRequest request){
        String userCode = request.getHeader("Account-Code");
        CommonResponse<Order> orderCommonResponse = orderCancelService.cancelOrder(orderRequest.getOrderCode(),userCode);
        if(orderCommonResponse==null ){
            //说明此订单不存在
            return new Results(ResultConstant.EMPTY_ENTITY.getFullCode(),"订单交易已完成");

        }

        Order order = orderCommonResponse.getData();
        if (order.getStatus() == OrderStatus.TRADING ){
            //撤销失败
            if(orderCommonResponse.getCode() == 4001){
                //说明订单已经撮合完成,订单交易完成
                orderCommonResponse.setMessage("订单交易已经完成");
            }else {
                //说明撮合失败,显示稍后重试
                orderCommonResponse.setMessage("请稍后重试");
            }
            return new Results(ResultConstant.INNER_ERROR.getFullCode(),orderCommonResponse.getMessage());
        }else {
            //撤销成功

            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, orderRequest.getOrderCode(), 3600*12);
            orderManageService.updateUserHistoryCache(order);
            return Results.ok();
        }
    }
    @PostMapping("/cancelAll")
    public Object cancelAll(HttpServletRequest request){
        String userCode = request.getHeader("Account-Code");
        List<CommonResponse<Order>>orderList = orderCancelService.cancelAll(userCode);
        if (orderList == null){
        	return new Results(ResultConstant.EMPTY_ENTITY.getFullCode(),"无交易中托单");
        }
        
        List<Order> successOrder = new ArrayList<>();
        List<Order> faildOrder = new ArrayList<>();
        for(CommonResponse<Order> order :orderList){
            if(order.getData().getStatus()==OrderStatus.TRADING){
                faildOrder.add(order.getData());
            }else {
                successOrder.add(order.getData());
            }
        }

        orderManageService.updateUserHistoryCaches(successOrder);

        if(faildOrder.size()==0 && successOrder.size()>0){
            //全部撤销成功
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, null, 3600*12);
            return Results.ok();
        }else {
            List<OrderMessage> orderMsg = new ArrayList<>();
            for (CommonResponse<Order> CommonResponse : orderList) {
                OrderMessage msg = new OrderMessage();
                msg.setMessage(CommonResponse.getMessage());
                msg.setOrderCode(CommonResponse.getData() == null ? null : CommonResponse.getData().getOrderCode());
                orderMsg.add(msg);
            }
            for (Order order : successOrder){
                orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, order.getOrderCode(), 3600*12);
            }
            return new Results(ResultConstant.INNER_ERROR.getFullCode(),"撤销成功"+successOrder.size()+"条").putData("entities", orderMsg);
        }
    }

    /**
     * 此接口为了临时把测试时在撮单系统丢失的数据但是未解冻的订单进行解冻处理,并且把数据库的状态改为已取消
     * @param request
     * @return
     */
    @PostMapping("/cancelErrorOrder")
    public Object cancelErrorOrder(@RequestHeader(value = "Admin-Key") String key, HttpServletRequest request){
        if(!"true".equals(adminFlag) || !key.equals(adminKey)){
            return new Results(ResultConstant.EMPTY_PARAM.getFullCode(),"非法请求");
        }
        String userCode = request.getHeader("Account-Code");
        String orderCode = request.getHeader("Order-Code");
        List<CommonResponse<Order>> orderList =orderCancelService.cancelErrorOrder(userCode,orderCode);

        if (orderList == null || orderList.size()==0){
            return new Results(ResultConstant.EMPTY_ENTITY.getFullCode(),"无交易中托单,或解冻失败");
        }
        List<Order> successOrder = new ArrayList<>();
        for(CommonResponse<Order> order :orderList){
            successOrder.add(order.getData());
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, order.getData().getOrderCode(), 3600*12);

        }
        orderManageService.updateUserHistoryCaches(successOrder);
        return Results.ok();
    }
}
