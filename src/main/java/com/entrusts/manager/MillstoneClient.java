package com.entrusts.manager;

import com.entrusts.module.dto.FreezeDto;
import com.entrusts.module.dto.result.Results;
import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cyuan on 2018/3/20.
 */
@FeignClient(name = "${feignClient.millstone.name}", path = "${feignClient.millstone.containerName}")
public interface MillstoneClient {

	String MILLSTONE_ACCOUNT_PATH = "/api/millstone/v1/account/";
	String MILLSTONE_TRADE_PATH = "/api/millstone/v1/trade/";
	String ACCOUNT_TRANSFER_PATH = "/account/transfer/";

	/**
	 * @return java.lang.String
	 * @Description 解锁货币
	 */
	@PostMapping(MILLSTONE_ACCOUNT_PATH + "unfreeze_for_order")
	String unfreezeForOrder(FreezeDto freezeDto);

	/**
	 * @return com.entrusts.module.dto.result.Results
	 * @Description 锁币
	 */
	@PostMapping(MILLSTONE_ACCOUNT_PATH + "freeze_for_order")
	Results freezeForOrder(FreezeDto freezeDto);

	/**
	 * @param userCode 用户code
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param direction 货币的流向(1:转入，2:转出)，缺省为全部
	 * @param pageNo 分页号(缺省为1)
	 * @param pageSize 分页记录数(缺省为10)
	 * @return java.lang.String
	 * @Description 获取用户数字账户的划转记录
	 */
	@GetMapping(MILLSTONE_ACCOUNT_PATH + "search_transfer_record")
	String searchTransferRecord(@RequestParam("userCode") String userCode,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize);

	/**
	 * @param userCode 用户code
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param direction 货币的流向(1:转入，2:转出)，缺省为全部
	 * @param toCreatedTime 如果传了本字段，服务将以实体创建时间降序排序，查询创建时间小于（但不包括）本字段的实体。如果没有传本字段，相当查询创建时间最大的一段数据实体
	 * @param limit 获取数据实体上限数。如果给的值超过服务器上限，返回数据数量只能达到服务器上限)
	 * @return java.lang.String
	 * @Description 获取用户数字账户的划转记录(按时间分页)
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_transfer_record_by_created_time")
	String searchTransferRecordByCreatedTime(@RequestParam("userCode") String userCode,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit);

	/**
	 * @param tradeType 交易类型(1:充值，2:提币)
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @return java.lang.String
	 * @Description 获取用户数字账户的钱包记录
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_record")
	String searchRecord(@RequestParam("userCode") String userCode,
			@RequestParam(value = "tradeType") String tradeType,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize);

	/**
	 * @param tradeType 同上
	 * @param encryptCurrencyId 同上
	 * @param toCreatedTime 同上
	 * @param limit 同上
	 * @return java.lang.String
	 * @Description 获取用户数字账户的钱包记录(按时间分页)
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_record_by_created_time")
	String searchRecordByCreatedTime(@RequestParam("userCode") String userCode,
			@RequestParam(value = "tradeType") String tradeType,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit);

	/**
	 * @param baseCurrencyId 基准数字货币id
	 * @param targetCurrencyId 目标数字货币id
	 * @return java.lang.String
	 * @Description 获取用户的交易记录
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_trade_record")
	String searchTradeRecord(@RequestParam("userCode") String userCode,
			@RequestParam(value = "baseCurrencyId", required = false) String baseCurrencyId,
			@RequestParam(value = "targetCurrencyId", required = false) String targetCurrencyId,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize);

	/**
	 * @param baseCurrencyId 同上
	 * @param targetCurrencyId 同上
	 * @param toCreatedTime 同上
	 * @param limit 同上
	 * @return java.lang.String
	 * @Description 获取用户的交易记录(按时间分页)
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_trade_record_by_created_time")
	String searchTradeRecordByCreatedTime(@RequestParam("userCode") String userCode,
			@RequestParam(value = "baseCurrencyId", required = false) String baseCurrencyId,
			@RequestParam(value = "targetCurrencyId", required = false) String targetCurrencyId,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit);

	/**
	 * @return java.lang.String
	 * @Description 获取划转详情
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "get_transfer_detail")
	String transferDetail(@RequestParam("userCode") String userCode,
			@RequestParam(value = "transferId") String transferId);

	/**
	 * @return java.lang.String
	 * @Description 获取交易详情
	 */
	@GetMapping( MILLSTONE_TRADE_PATH + "record/get_detail")
	String tradeDetail(@RequestParam("userCode") String userCode,
			@RequestParam(value = "tradeRecordId") String tradeRecordId);

	/**
	 * @return java.lang.String
	 * @Description 获取转账详情
	 */
	@GetMapping( ACCOUNT_TRANSFER_PATH + "get_transfer_account_detail")
	String transferAccountDetail(@RequestParam("userCode") String userCode,
			@RequestParam(value = "transferAccountId") String transferAccountId);

	/**
	 * 提币
	 * @param withdrawnRequest
	 * @return
	 */
	@PostMapping(value = MILLSTONE_ACCOUNT_PATH + "withdrawn", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	String withdrawn(@RequestBody String withdrawnRequest);

	/**
	 * 获取用户资产列表
	 * @param userCode
	 * @return
	 */
	@GetMapping( MILLSTONE_ACCOUNT_PATH + "search_account_asset")
	String searchAccountAsset(@RequestParam("userCode") String userCode, @RequestParam("pageNo") String pageNo, @RequestParam("pageSize") String pageSize);

	/**
	 * 获取账户信息
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@GetMapping(MILLSTONE_ACCOUNT_PATH + "get_account_asset_by_user_code_and_encrypt_currency_id")
	String getAccountAssetByUserCodeAndEncryptCurrencyId(@RequestParam("userCode") String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId);

	/**
	 * 获取钱包地址
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@GetMapping(MILLSTONE_ACCOUNT_PATH + "get_wallet_address")
	String getWalletAddress(@RequestParam("userCode") String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId);

}
