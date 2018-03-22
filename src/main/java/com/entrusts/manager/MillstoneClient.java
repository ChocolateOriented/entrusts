package com.entrusts.manager;

import com.entrusts.module.dto.FreezeDto;
import com.entrusts.module.dto.result.Results;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by cyuan on 2018/3/20.
 */
@FeignClient(name = "millstone", path = "/api/millstone/v1/account")
public interface MillstoneClient {
    @RequestMapping(value = "/unfreeze_for_order",produces = "application/json",method = RequestMethod.POST)
    String unfreezeForOrder(@RequestBody Map<String,Object> map);

    /**
     * @Description 锁币
     * @param freezeDto
     * @return com.entrusts.module.dto.result.Results
     */
    @RequestMapping(method = RequestMethod.POST, value = "freeze_for_order", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    Results freezeForOrder(FreezeDto freezeDto);
}
