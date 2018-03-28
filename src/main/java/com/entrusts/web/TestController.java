package com.entrusts.web;

import com.entrusts.manager.MillstoneClient;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.service.TradePairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cyuan on 2018/3/23.
 */
@RestController
@RequestMapping("auth")
public class TestController {
    @Autowired
    private MillstoneClient millstoneClient;
    @Autowired
    private TradePairService tradePairService;
    @Value("${nest.exclude.urls}")
    private String urls;
    @RequestMapping("hello")
    public String test1(){
        return urls;
    }
}
