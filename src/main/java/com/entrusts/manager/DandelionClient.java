package com.entrusts.manager;

import com.entrusts.module.dto.result.Results;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cyuan on 2018/6/27.
 */
@FeignClient(name = "${feignClient.dandelion.name}",path = "${feignClient.dandelion.containerName}")
public interface DandelionClient {
    String DANDELION_PATH = "/open/api/dandelion/v1/activity/";
    @RequestMapping(value = DANDELION_PATH + "get_current_price", method = RequestMethod.GET)
    Results getExchangeRate(@RequestParam("digitalCurrencyId") Integer digitalCurrencyId, @RequestParam("legalTenderType") String legalTenderType);
}
