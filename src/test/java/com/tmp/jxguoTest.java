package com.tmp;

import com.entrusts.EntrustsApplication;
import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.Page;
import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.TradeType;
import com.entrusts.module.vo.CurrentEntrusts;
import com.entrusts.module.vo.OrderQuery;
import com.entrusts.service.OrderManageService;
import com.entrusts.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jxguo on 2018/3/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntrustsApplication.class)
@EnableAutoConfiguration
public class jxguoTest {
    @Autowired
    private OrderManageService orderManageService;

    @Autowired
    private OrderService orderServicel;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void test(){
        DelegateEvent delegateEvent = new DelegateEvent();
        delegateEvent.setAmount(new BigDecimal(0.000012121));
        delegateEvent.setBaseCurrencyId(1);
        delegateEvent.setClientTime(new Date());
        delegateEvent.setOrderTime(new Date());
        delegateEvent.setOrderCode(orderServicel.generateId()+"");
        delegateEvent.setTradePairId(1);
        delegateEvent.setConvertRate(new BigDecimal(0.000012121));
        //delegateEvent.setDelegateEventstatus(DelegateEventstatus.INSERT_ORDERDB_SUCCESS);
        delegateEvent.setTradeType(TradeType.sell);
        delegateEvent.setQuantity(new BigDecimal(0.000012121));
        delegateEvent.setUserCode("21231231231231231");

        orderServicel.saveNewOrderByEvent(delegateEvent);
    }
    @Test
    public void test1(){
        OrderQuery query = new OrderQuery();
        query.setUserCode("21231231231231231");
        Page<CurrentEntrusts> currentOrder = orderManageService.findCurrentOrder(query, 1, 10);
        System.out.println("");
    }
}
