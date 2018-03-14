package com.entrusts.web;

import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Order.OrderMode;
import com.entrusts.module.entity.TradePair;
import com.entrusts.service.DelegateTranslator;
import com.entrusts.service.TradePairService;
import com.entrusts.util.RedisUtil;
import com.entrusts.util.StringUtils;
import com.lmax.disruptor.dsl.Disruptor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxli on 2018/3/5.
 */
@RestController
@RequestMapping(value = "order")
public class OrderController extends BaseController {

	@Autowired
	Disruptor<DelegateEvent> delegateDisruptor;
	@Autowired
	TradePairService tradePairService;
	@Autowired
	private DelegateTranslator delegateTranslator;

	private static final String CACHE_DELEGATE_REQUEST_TOKEN_PREFIX = "delegate_request_token_";
	private static final String CACHE_DELEGATE_HANDLED_TOKEN_PREFIX = "delegate_handled_token_";

	/**
	 * @Description  限价交易委托
	 * @param userCode
	 * @param clientTime
	 * @param requestToken 请求令牌, 见方法requestToken
	 * @param delegate 委托信息
	 * @param bindingResul delegate的有效性校验结果
	 * @return com.entrusts.module.dto.result.Results
	 */
	@RequestMapping(value = "delegateLimit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results delegateLimit(@RequestHeader(ACCOUNT_CODE) String userCode,@RequestHeader(TIMESTAMP) Long clientTime, @RequestHeader
			("Request-Token") String requestToken, @RequestBody @Validated Delegate delegate, BindingResult bindingResul) {

		String validTokenKey = CACHE_DELEGATE_REQUEST_TOKEN_PREFIX + userCode;
		String validToken = RedisUtil.get(validTokenKey);
		String handledTokenKey = CACHE_DELEGATE_HANDLED_TOKEN_PREFIX + userCode + requestToken;

		//无效token
		if (!requestToken.equals(validToken)) {
			String handResult = RedisUtil.get(handledTokenKey);
			if (null == handResult) {//token不是由系统生成or已处理并过期
				return new Results(ResultConstant.EMPTY_PARAM.code, "无效Request-Token");
			}
			//已处理
			return new Results(ResultConstant.REPEAT_REQUEST);
		}

		//有效token
		boolean isSuccess = RedisUtil.setIfNotExist(handledTokenKey, "", 60 * 60 * 1000);
		if (!isSuccess) {//已由其他线程处理
			return new Results(ResultConstant.REPEAT_REQUEST);
		}

		RedisUtil.del(validTokenKey);
		if (bindingResul.hasErrors()){
			return new Results(ResultConstant.EMPTY_PARAM.code, getFieldErrorsMessages(bindingResul));
		}
		TradePair tradePair = tradePairService.findTradePairByCoinName(delegate.getBaseCurrency(),delegate.getTargetCurrency());
		if (tradePair == null){
			return new Results(ResultConstant.EMPTY_PARAM.code, "未知交易对");
		}
		BigDecimal minTradeQuantity = tradePair.getMinTradeQuantity();
		if (minTradeQuantity!=null && delegate.getQuantity().compareTo(minTradeQuantity) < 0){
			return new Results(ResultConstant.EMPTY_PARAM.code, "交易数额错误, 最小值" + minTradeQuantity.toString());
		}
		//TODO 最多同时发布20条托单

		delegate.setUserCode(userCode);
		delegate.setClientTime(new Date(clientTime));
		delegateDisruptor.publishEvent(delegateTranslator, delegate, tradePair,OrderMode.LIMIT_PRICE_DEAL);
		return Results.ok();
	}

	/**
	 * @return com.entrusts.module.dto.BaseResponse
	 * @Description 防止请求重复提交 下单前先请求获取token, 每个用户在*生成token至使用或失效期间*只能发起一笔交易, 同一token只会处理一次 若交易1请求了token收到结果A, 但服务器未收到交易消息, 交易2请求token时还会返回A,
	 * 交易1与交易2只有一个能成功
	 */
	@RequestMapping(value = "requestToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results requestToken(@RequestHeader(ACCOUNT_CODE) String userCode) {
		String key = CACHE_DELEGATE_REQUEST_TOKEN_PREFIX + userCode;
		String token = RedisUtil.get(key);
		if (StringUtils.isNotBlank(token)) {//已生成token
			return Results.ok().putData("token", token);
		}

		//生成新的token
		token = UUID.randomUUID().toString();
		boolean isSuccess = RedisUtil.setIfNotExist(key, token, 2 * 60 * 1000);
		if (isSuccess) {
			return Results.ok().putData("token", token);
		}

		//插入Redis失败说明用户同时发起了多次请求
		return new Results(ResultConstant.SYSTEM_BUSY);
	}
}


