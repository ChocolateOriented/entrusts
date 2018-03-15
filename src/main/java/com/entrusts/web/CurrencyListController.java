package com.entrusts.web;

import com.entrusts.module.dto.BaseCurrency;
import com.entrusts.module.dto.TargetCurrency;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.service.CurrencyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cyuan on 2018/3/8.
 */
@RestController
@RequestMapping("entrusts/currency")
public class CurrencyListController extends BaseController {
    @Autowired
    private CurrencyListService currencyListService;
    @GetMapping("/listBase")
    public Results getBaseCurrency(){
        List<BaseCurrency> baseCurrencyList = currencyListService.getBaseCurrency();
        if(baseCurrencyList == null){
            return new Results(ResultConstant.INNER_ERROR.code,"获取失败");
        }
        return Results.ok().putData("entities", baseCurrencyList);
    }
    @GetMapping("/listTarget")
    public Results getTargetCurrency(@RequestParam("baseCurrency") String currency,@RequestParam("time") String time){
        List<TargetCurrency> targetCurrencys = currencyListService.getTargetCurrency(currency,time);
        if(targetCurrencys == null){
            return new Results(ResultConstant.INNER_ERROR.code,"获取失败");
        }
        return Results.ok().putData("entities", targetCurrencys);
    }
    @GetMapping("/allListTarget")
    public Results getAllTargetCurrency(@RequestParam("time") String time){
        List<TargetMapCurrency> targetMapCurrencys = currencyListService.getAllTargetCurrency(time);
        if(targetMapCurrencys == null){
            return new Results(ResultConstant.INNER_ERROR.code,"获取失败");
        }
        return Results.ok().putData("entities", targetMapCurrencys);
    }
}
