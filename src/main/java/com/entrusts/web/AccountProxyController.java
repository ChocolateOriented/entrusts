package com.entrusts.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.entrusts.manager.MillstoneClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxli on 2018/3/21.
 * 账户操作代理
 */
@RestController
@RequestMapping(value = "api/millstone/v1/account")
public class AccountProxyController extends BaseController {

	@Autowired
	private MillstoneClient millstoneClient;

	/**
	 * @Description 获取用户数字账户的划转记录
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param direction 货币的流向(1:转入，2:转出)，缺省为全部
	 * @param userCode 用户code
	 * @return java.lang.String
	 */
	@GetMapping(value = "search_transfer_record")
	@ResponseBody
	public String searchTransferRecord(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize){

		return millstoneClient.searchTransferRecord(userCode, encryptCurrencyId, direction, pageNo, pageSize);
	}

	/**
	 * @param userCode 用户code
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @param direction 货币的流向(1:转入，2:转出)，缺省为全部
	 * @param toCreatedTime 如果传了本字段，服务将以实体创建时间降序排序，查询创建时间小于（但不包括）本字段的实体。如果没有传本字段，相当查询创建时间最大的一段数据实体
	 * @param limit 获取数据实体上限数。如果给的值超过服务器上限，返回数据数量只能达到服务器上限)
	 * @return java.lang.String
	 * @Description 获取用户数字账户的划转记录(按时间分页)
	 */
	@GetMapping(value = "search_transfer_record_by_created_time")
	@ResponseBody
	public String searchTransferRecordByCreatedTime(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "direction", required = false) String direction,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit){

		return millstoneClient.searchTransferRecordByCreatedTime(userCode,encryptCurrencyId,direction,toCreatedTime,limit);
	}

	/**
	 * @param tradeType 交易类型(1:充值，2:提币)
	 * @param encryptCurrencyId 货币ID，缺省为获取全部货币
	 * @return java.lang.String
	 * @Description 获取用户数字账户的钱包记录
	 */
	@GetMapping(value = "search_record")
	@ResponseBody
	public String searchRecord(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "tradeType") String tradeType,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize){

		return millstoneClient.searchRecord(userCode,tradeType,encryptCurrencyId,pageNo,pageSize);
	}

	/**
	 * @param tradeType 同上
	 * @param encryptCurrencyId 同上
	 * @param toCreatedTime 同上
	 * @param limit 同上
	 * @return java.lang.String
	 * @Description 获取用户数字账户的钱包记录(按时间分页)
	 */
	@GetMapping(value = "search_record_by_created_time")
	@ResponseBody
	public String searchRecordByCreatedTime(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "tradeType") String tradeType,
			@RequestParam(value = "encryptCurrencyId", required = false) String encryptCurrencyId,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit){

		return millstoneClient.searchRecordByCreatedTime(userCode,tradeType,encryptCurrencyId,toCreatedTime,limit);
	}


	/**
	 * @param baseCurrencyId 基准数字货币id
	 * @param targetCurrencyId 目标数字货币id
	 * @return java.lang.String
	 * @Description 获取用户的交易记录
	 */
	@GetMapping(value = "search_trade_record")
	@ResponseBody
	public String searchTradeRecord(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "baseCurrencyId", required = false) String baseCurrencyId,
			@RequestParam(value = "targetCurrencyId", required = false) String targetCurrencyId,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize){

		return millstoneClient.searchTradeRecord(userCode,baseCurrencyId,targetCurrencyId,pageNo,pageSize);
	}

	/**
	 * @param baseCurrencyId 同上
	 * @param targetCurrencyId 同上
	 * @param toCreatedTime 同上
	 * @param limit 同上
	 * @return java.lang.String
	 * @Description 获取用户的交易记录(按时间分页)
	 */
	@GetMapping(value = "search_trade_record_by_created_time")
	@ResponseBody
	public String searchTradeRecordByCreatedTime(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "baseCurrencyId", required = false) String baseCurrencyId,
			@RequestParam(value = "targetCurrencyId", required = false) String targetCurrencyId,
			@RequestParam(value = "toCreatedTime", required = false) String toCreatedTime,
			@RequestParam(value = "limit", required = false) String limit){

		return millstoneClient.searchTradeRecordByCreatedTime(userCode,baseCurrencyId,targetCurrencyId,toCreatedTime,limit);
	}

	/**
	 * @return java.lang.String
	 * @Description 获取划转详情
	 */
	@GetMapping(value = "get_transfer_detail")
	@ResponseBody
	public String transferDetail(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "transferId") String transferId){
		return millstoneClient.transferDetail(userCode, transferId);
	}

	/**
	 * @return java.lang.String
	 * @Description 获取交易详情
	 */
	@GetMapping(value = "get_trade_detail")
	@ResponseBody
	public String tradeDetail(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "tradeRecordId") String tradeRecordId){
		return millstoneClient.tradeDetail(userCode, tradeRecordId);
	}

	/**
	 * @return java.lang.String
	 * @Description 获取转账详情
	 */
	@GetMapping(value = "get_transfer_account_detail")
	@ResponseBody
	public String transferAccountDetail(@RequestHeader(ACCOUNT_CODE) String userCode,
			@RequestParam(value = "transferAccountId") String transferAccountId){
		return millstoneClient.transferAccountDetail(userCode, transferAccountId);
	}
	
	/**
	 * 提币
	 * @param withdrawnRequest
	 * @return
	 */
	@PostMapping(value = "withdrawn")
	public String withdrawn(@RequestHeader(ACCOUNT_CODE) String userCode, @RequestBody String withdrawnRequest) {
		JSONObject jsonObj = JSON.parseObject(withdrawnRequest);
		jsonObj.put("userCode", userCode);
		return millstoneClient.withdrawn(jsonObj.toJSONString());
	}

	/**
	 * 获取用户资产列表
	 * @param userCode
	 * @return
	 */
	@GetMapping(value = "search_account_asset")
	public String searchAccountAsset(@RequestHeader(ACCOUNT_CODE) String userCode, @RequestParam(value = "pageNo", required = false) String pageNo,
			@RequestParam(value = "pageSize", required = false) String pageSize) {
		return millstoneClient.searchAccountAsset(userCode, pageNo, pageSize);
	}

	/**
	 * 获取账户信息
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@GetMapping(value = "get_account_asset_by_user_code_and_encrypt_currency_id")
	public String getAccountAssetByUserCodeAndEncryptCurrencyId(@RequestHeader(ACCOUNT_CODE) String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId) {
		return millstoneClient.getAccountAssetByUserCodeAndEncryptCurrencyId(userCode, encryptCurrencyId);
	}
	
	/**
	 * 获取钱包地址
	 * @param userCode
	 * @param encryptCurrencyId
	 * @return
	 */
	@GetMapping(value = "get_wallet_address")
	public String getWalletAddress(@RequestHeader(ACCOUNT_CODE) String userCode, @RequestParam("encryptCurrencyId") Integer encryptCurrencyId) {
		return millstoneClient.getWalletAddress(userCode, encryptCurrencyId);
	}
}
