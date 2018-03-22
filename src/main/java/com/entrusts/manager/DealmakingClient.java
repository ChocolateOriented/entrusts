package com.entrusts.manager;

import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cyuan on 2018/3/20.
 */
@FeignClient(name = "${feignName.dealmakingName}",path = "/dealmaking")
public interface DealmakingClient {
    @RequestMapping(value = "/cancel_order",produces = "application/json",method = RequestMethod.POST)
    String delCancelOrder(@RequestBody Map<String,Object> map);

}
