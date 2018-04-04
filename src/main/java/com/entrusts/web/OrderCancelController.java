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

    @PostMapping(value = "/cancel")
    public Object cancel(@RequestBody Order orderRequest, HttpServletRequest request){
        CommonResponse<Order> orderCommonResponse = orderCancelService.cancelOrder(orderRequest.getOrderCode());
        if(orderCommonResponse==null){
            //说明此订单不存在
            return new Results(ResultConstant.EMPTY_ENTITY);

        }
        Order order = orderCommonResponse.getData();
        if (order.getStatus() == OrderStatus.TRADING ){
            //撤销失败
            return new Results(ResultConstant.INNER_ERROR.getFullCode(),orderCommonResponse.getMessage());
        }else {
            //撤销成功
            String userCode = request.getHeader("Account-Code");
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
}
