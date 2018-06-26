package com.entrusts.web.internal;

import com.entrusts.module.dto.TradePairQuantity;
import com.entrusts.module.dto.CommonResponse;
import com.entrusts.service.MarketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("entrusts/internal/market")
public class MarketController {

	@Autowired
	private MarketService marketService;

    /**
     * 获取最新价格
     */
    @GetMapping("/getDelegateTotalQuantity")
    public CommonResponse<TradePairQuantity> getDelegateTotalQuantity(@RequestParam("tradePairId") Integer tradePairId){
    	TradePairQuantity tradePairQuantity = marketService.queryDelegateTotalQuantity(tradePairId);
    	CommonResponse<TradePairQuantity> response = new CommonResponse<>(tradePairQuantity);
        return response;
    }
}
