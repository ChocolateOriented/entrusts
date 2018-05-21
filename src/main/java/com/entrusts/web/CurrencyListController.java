package com.entrusts.web;

import com.entrusts.module.dto.*;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.vo.HistoryOrderView;
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

    /**
     * @Description 查询支持的货币 
     * @param 
     * @return com.entrusts.module.dto.result.Results
     */
    @GetMapping("/list")
    public Results list(){
        List<String> currencyList = currencyListService.findSupportCurrency();
        return Results.ok().putData("entities", currencyList);
    }

    @GetMapping("/listBase")
    public Results getBaseCurrency(){
        List<BaseCurrency> baseCurrencyList = currencyListService.getBaseCurrency();
        if(baseCurrencyList == null){
            return new Results(ResultConstant.EMPTY_ENTITY);
        }
        return Results.ok().putData("entities", baseCurrencyList);
    }
    @GetMapping("/listTarget")
    public CommonResponse<Page<TargetCurrency>> getTargetCurrency(@RequestParam("baseCurrency") String currency,Integer pageNum, Integer pageSize,@RequestParam("timeZoneOffset") Integer value){
        Page<TargetCurrency> page = currencyListService.getTargetCurrency(currency,value,pageNum,pageSize);
        CommonResponse<Page<TargetCurrency>> response = new CommonResponse<>();
        if(page == null){
            response.setCode((int)ResultConstant.SYSTEM_BUSY.getFullCode());
            response.setMessage(ResultConstant.SYSTEM_BUSY.message);
            return response;
        }
        response.setData(page);
        return response;
    }
    @GetMapping("/allListTarget")
    public Results getAllTargetCurrency(@RequestParam(value = "timeZoneOffset",defaultValue = "0") Integer time){
        List<TargetMapCurrency> targetMapCurrencys = currencyListService.getAllTargetCurrency(time);
        if(targetMapCurrencys == null){
            return new Results(ResultConstant.EMPTY_ENTITY);
        }
        return Results.ok().putData("entities", targetMapCurrencys);
    }
    @GetMapping("/all_list_target")
    public Results getAllTargetCurrency2(@RequestParam(value = "timeZoneOffset",defaultValue = "0") Integer time){
        List<TargetMapCurrency> targetMapCurrencys = currencyListService.getAllTargetCurrency(time);
        if(targetMapCurrencys == null){
            return new Results(ResultConstant.EMPTY_ENTITY);
        }
        return Results.ok().putData("entities", targetMapCurrencys);
    }
    /**
     * (获取的数据是对应时区所有的交易对单日的价格,此方法取出所有数据中的对应基准货币的数据返回给前端)
     * @param time
     * @param baseCurrencyId
     * @return
     */
    @GetMapping("/getHighestAndLowestCurrency")
    public Results getHighestAndLowestCurrency(@RequestParam("timeZoneOffset") Integer time,@RequestParam("baseCurrencyId") Integer baseCurrencyId ){

        CurrencyMap currencyMap = currencyListService.getHighestAndLowestCurrency(time,baseCurrencyId);

        if(currencyMap == null){
            return new Results(ResultConstant.SYSTEM_BUSY);
        }

        return Results.ok().putData("entities",currencyMap);
    }
}
