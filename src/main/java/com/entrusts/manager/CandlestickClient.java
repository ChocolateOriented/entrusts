package com.entrusts.manager;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Created by cyuan on 2018/5/11.
 */
@FeignClient(name = "${feignClient.candlestick.name}",path = "${feignClient.candlestick.containerName}")
public interface CandlestickClient {
    String CANDLESTICKS = "/api/candlesticks/v1/market/";
    @GetMapping(value = CANDLESTICKS + "detail")
    String getHighestAndLowestCurrency(Integer offset);

}
