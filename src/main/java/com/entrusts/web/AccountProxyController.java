package com.entrusts.web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jxli on 2018/3/21.
 * 账户操作代理
 */
@FeignClient("${feignName.millstoneName}")
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

	/**
	 * 获取钱包地址
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@RequestMapping(value = "get_wallet_address", method = RequestMethod.GET)
	@ResponseBody
	String getWalletAddress(@RequestParam("userCode") String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId);

	/**
	 * 获取账户信息
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@RequestMapping(value = "get_account_asset_by_user_code_and_encrypt_currency_id", method = RequestMethod.GET)
	@ResponseBody
	String getAccountAssetByUserCodeAndEncryptCurrencyId(@RequestParam("userCode") String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId);

	/**
	 * 获取用户资产列表
	 * @param userCode
	 * @return
	 */
	@RequestMapping(value = "find_account_asset", method = RequestMethod.GET)
	@ResponseBody
	String findAccountAsset(@RequestParam("userCode") String userCode);

	/**
	 * 提币
	 * @param withdrawnRequest
	 * @return
	 */
	@RequestMapping(value = "withdrawn", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String withdrawn(@RequestBody String withdrawnRequest);

	/**
	 * 划转资产(增加)
	 * @param transferRequest
	 * @return
	 */
	@RequestMapping(value = "transfer_asset_into", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String transferAssetInto(@RequestBody String transferRequest);

	/**
	 * 划转资产(减)
	 * @param transferRequest
	 * @return
	 */
	@RequestMapping(value = "transfer_asset_out", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String transferAssetOut(@RequestBody String transferRequest);

	/**
	 * 划转失败补偿
	 * @param compensationRequest
	 * @return
	 */
	@RequestMapping(value = "transfer_failure_compensation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String transferFailureCompensation(@RequestBody String compensationRequest);
}
