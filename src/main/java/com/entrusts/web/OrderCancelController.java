package com.entrusts.web;

import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.service.OrderCancelService;
import com.entrusts.service.OrderManageService;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.expression.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    public Object cancel(@RequestParam("orderCode") String orderCode, HttpServletRequest request){
        Order order = orderCancelService.cancelOrder(orderCode);
        if(order==null){
            //说明此订单不存在
            return new Results(ResultConstant.EMPTY_ENTITY.code,"此订单不存在");

        }else if (order.getStatus() == OrderStatus.WITHDRAW ){
            //撤销成功
            String userCode = request.getHeader("Account-Code");
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, orderCode, 3600*12);
            return Results.ok();
        }else if (order.getStatus() == OrderStatus.TRADING){
            //撤销失败
            return new Results(ResultConstant.INNER_ERROR.code,"撤销失败");
        }else {
            //撮单系统取消成功,但是货币解锁失败
            return new Results(ResultConstant.SYSTEM_BUSY.code,"撮单系统取消成功,但是货币解锁失败");
        }
    }
    @PostMapping("/cancelAll")
    public Object cancelAll(HttpServletRequest request){
        String userCode = request.getHeader("Account-Code");
        Map<OrderStatus,List<Order>> map = orderCancelService.cancelAll(userCode);
        if (map == null){
            return new Results(ResultConstant.EMPTY_ENTITY.code,"请求数据不存在");
        }
        if(map.containsKey(OrderStatus.WITHDRAW) && map.size()==1){
            //说明全部撤销成功
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, null, 3600*12);
            return Results.ok();
        }else if(map.containsKey(OrderStatus.TRADING) && map.size()==1){
            //说明全部撤销失败INNER_ERROR
            return new Results(ResultConstant.INNER_ERROR.code,"撤销全部失败");
        }else {
            //说明部分成功部分失败SYSTEM_BUSY
            return new Results(ResultConstant.SYSTEM_BUSY.code,"撤销部分成功");
        }
    }
}
