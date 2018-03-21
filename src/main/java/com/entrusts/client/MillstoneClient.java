package com.entrusts.client;

import com.entrusts.module.dto.FreezeDto;
import com.entrusts.module.dto.result.Results;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jxli on 2018/3/21.
 */
@FeignClient("millstone")
public interface MillstoneClient {

	/**
	 * @Description 锁币 
	 * @param freezeDto
	 * @return com.entrusts.module.dto.result.Results
	 */
	@RequestMapping(method = RequestMethod.POST, value = "api/millstone/v1/account/freeze_for_order", produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	Results freezeForOrder(FreezeDto freezeDto);
}
