package com.entrusts.manager;

import com.entrusts.module.dto.DelCancelOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cyuan on 2018/3/20.
 */
@FeignClient(name = "${feignClient.dealmaking.name}",path = "${feignClient.dealmaking.containerName}")
public interface DealmakingClient {
    String DEAL_MAKING_PATH = "/api/bull/v1/dealmaking/";
    @RequestMapping(value = DEAL_MAKING_PATH+"cancel_order",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = "application/json",method = RequestMethod.POST)
    String delCancelOrder(String delCancelOrder);

}
