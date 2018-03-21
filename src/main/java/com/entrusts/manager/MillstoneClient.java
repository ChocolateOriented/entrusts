package com.entrusts.manager;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by cyuan on 2018/3/20.
 */
@FeignClient(name = "${feignName.millstoneName}", path = "/api/millstone/v1/account")
public interface MillstoneClient {
    @RequestMapping(value = "/unfreeze_for_order",produces = "application/json",method = RequestMethod.POST)
    String unfreezeForOrder(@RequestBody Map<String,Object> map);
}
