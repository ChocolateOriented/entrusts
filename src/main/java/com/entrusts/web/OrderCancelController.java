package com.entrusts.web;

import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.service.OrderCancelService;
import com.entrusts.service.OrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        Boolean flag = orderCancelService.cancelOrder(orderCode);
        if(flag){
            String userCode = request.getHeader("Account-Code");
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, orderCode, 3600*12);
            return Results.ok();
        }else {
            return new Results(ResultConstant.INNER_ERROR.code,"撤销失败");
        }
    }
    @PostMapping("/cancelAll")
    public Object cancelAll(HttpServletRequest request){
        String userCode = request.getHeader("Account-Code");
        Boolean flag = orderCancelService.cancelAll(userCode);
        if(flag){
            orderManageService.deleteUserCurrentOrderListFromRedisByDeal(userCode, null, 3600*12);
            return Results.ok();
        }else {
            return new Results(ResultConstant.INNER_ERROR.code,"撤销失败");
        }
    }
}
