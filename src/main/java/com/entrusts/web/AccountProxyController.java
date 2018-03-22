package com.entrusts.web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jxli on 2018/3/21.
 * 账户操作代理
 */
@FeignClient("entrusts")
@RequestMapping("api/millstone/v1/account")
public interface AccountProxyController {

	/**
	 * @Description 获取用户数字账户的划转记录
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param direction 货币的流向(1:转入，2:转出)，缺省为全部
	 * @param userCode 用户code
	 * @return java.lang.String
	 */
	@RequestMapping(method = RequestMethod.GET, value = "search_transfer_record", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String searchTransferRecord(@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam("userCode") String userCode);

	/**
	 * @Description  获取用户的交易记录
	 * @param paymentEncryptCurrencyId 支付的数字货币id
	 * @param tradeEncryptCurrencyId 交易的数字货币id
	 * @param userCode 用户code
	 * @return java.lang.String
	 */
	@RequestMapping(method = RequestMethod.GET, value = "search_trade_record", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String searchTradeRecord(@RequestParam(value = "paymentEncryptCurrencyId", required = false) String paymentEncryptCurrencyId,
			@RequestParam(value = "tradeEncryptCurrencyId", required = false) String tradeEncryptCurrencyId,
			@RequestParam("userCode") String userCode);

	/**
	 * @Description  获取用户数字账户的钱包记录
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param tradeType 交易类型(1:充值，2:提币)
	 * @param userCode 用户code
	 * @return java.lang.String
	 */
	@RequestMapping(method = RequestMethod.GET, value = "search_record", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String searchRecord(@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "tradeType") String tradeType,
			@RequestParam("userCode") String userCode);
}
