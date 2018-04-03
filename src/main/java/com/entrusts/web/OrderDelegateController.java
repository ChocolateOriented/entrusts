package com.entrusts.web;

import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.TradeType;
import com.entrusts.service.OrderManageService;
import com.entrusts.service.OrderService;
import com.entrusts.service.TradePairService;
import com.entrusts.util.RedisUtil;
import com.entrusts.util.StringUtils;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.dsl.Disruptor;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

/**
 * Created by jxli on 2018/3/5.
 */
@RestController
@RequestMapping(value = "entrusts/order")
public class OrderDelegateController extends BaseController {

	@Autowired
	Disruptor<DelegateEvent> delegateDisruptor;
	@Autowired
	TradePairService tradePairService;
	@Autowired
	OrderService orderService;

	private DelegateTranslator delegateTranslator = new DelegateTranslator();
	private static final String CACHE_DELEGATE_REQUEST_TOKEN_PREFIX = "delegate_request_token_";
	private static final String CACHE_DELEGATE_HANDLED_TOKEN_PREFIX = "delegate_handled_token_";

	/**
	 * @Description  限价交易委托
	 * @param userCode
	 * @param clientTime
	 * @param delegate 委托信息
	 * @param bindingResul delegate的有效性校验结果
	 * @return com.entrusts.module.dto.result.Results
	 */
	@PostMapping(value = "delegateLimit")
	@ResponseBody
	public Results delegateLimit(@RequestHeader(ACCOUNT_CODE) String userCode, @RequestHeader(TIMESTAMP) Long clientTime,
			@RequestBody @Validated Delegate delegate, BindingResult bindingResul) {
		String validTokenKey = CACHE_DELEGATE_REQUEST_TOKEN_PREFIX + userCode;
		String requestToken = delegate.getRequestToken();
		String handledTokenKey = CACHE_DELEGATE_HANDLED_TOKEN_PREFIX + userCode + requestToken;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();

			String validToken = jedis.get(validTokenKey);
			//无效token
			if (!requestToken.equals(validToken)) {
				if (!jedis.exists(handledTokenKey)) {//token不是由系统生成or已处理并过期
					return new Results(ResultConstant.EMPTY_PARAM.code, "无效Request-Token");
				}
				//已处理
				return new Results(ResultConstant.REPEAT_REQUEST);
			}
			//有效token
			String result = jedis.set(handledTokenKey, "", "NX", "PX", 60 * 60 * 1000);
			if (!"OK".equals(result)) {//已由其他线程处理
				return new Results(ResultConstant.REPEAT_REQUEST);
			}
			jedis.del(validTokenKey);

			String userTotalKey = OrderManageService.totalCurrentOrderUserKey + userCode;
			String totalValue = jedis.get(userTotalKey);
			if (StringUtils.isNotBlank(totalValue) && Integer.parseInt(totalValue) > 20) {
				return new Results(ResultConstant.EMPTY_PARAM.code, "最多同时发布20条托单");
			}
		} catch (Exception e) {
			logger.info(userCode+"托单失败, 获取Redis连接失败",e);
			return new Results(ResultConstant.INNER_ERROR);
		} finally {
			RedisUtil.returnResource(jedis);
		}

		//数据校验
		if (bindingResul.hasErrors()) {
			return new Results(ResultConstant.EMPTY_PARAM.code, getFieldErrorsMessages(bindingResul));
		}
		TradePair tradePair = tradePairService.findTradePairByCoinName(delegate.getBaseCurrency(), delegate.getTargetCurrency());
		if (tradePair == null) {
			return new Results(ResultConstant.EMPTY_PARAM.code, "未知交易对");
		}
		BigDecimal minTradeQuantity = tradePair.getMinTradeQuantity();
		if (minTradeQuantity != null && delegate.getQuantity().compareTo(minTradeQuantity) < 0) {
			DecimalFormat format = new DecimalFormat("#.########");
			return new Results(ResultConstant.EMPTY_PARAM.code, "交易数量错误, 最小值" + format.format(minTradeQuantity));
		}

		//发布托单
		delegate.setUserCode(userCode);
		delegate.setClientTime(new Date(clientTime));
		delegate.setOrderMode(OrderMode.limit);
		String orderCode = generateOrderCode(delegate.getOrderMode(), delegate.getTradeType(), tradePair.getId());
		delegateDisruptor.publishEvent(delegateTranslator, delegate, tradePair, orderCode);
		return Results.ok().putData("orderCode", orderCode);
	}

	/**
	 * @return com.entrusts.module.dto.BaseResponse
	 * @Description 防止请求重复提交 下单前先请求获取token, 每个用户在*生成token至使用或失效期间*只能发起一笔交易, 同一token只会处理一次 若交易1请求了token收到结果A, 但服务器未收到交易消息, 交易2请求token时还会返回A,
	 * 交易1与交易2只有一个能成功
	 */
	@GetMapping(value = "requestToken")
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


	/**
	 * @return java.lang.String
	 * @Description 生成ordercode, snowFlakeId + 预留位(0) + 交易对Id(4字符) + 交易类型与成交模式(1字符)
	 * 使用10进制存储订单信息
	 * @param orderMode
	 * @param tradeType
	 * @param tradePairId
	 */
	private String generateOrderCode(OrderMode orderMode, TradeType tradeType, Integer tradePairId) {

		final int MODE_BIT = 1; //成交模式占用2进制位数

		DecimalFormat format = new DecimalFormat("0000");
		int mode = orderMode.equals(OrderMode.limit) ? 0 : 1;
		int type = tradeType.equals(TradeType.buy) ? 0 : 1;
		int modeAndtype = type << MODE_BIT | mode;

		return orderService.generateId() + "0" + format.format(tradePairId) + modeAndtype;
	}

	class DelegateTranslator implements EventTranslatorThreeArg<DelegateEvent, Delegate, TradePair, String> {
		/**
		 * @Description 将委托信息存入Disruptor队列预先创建的Event中
		 * @param event 队列中的event对象
		 * @param sequence 队列中的位置
		 * @param delegate 委托信息
		 * @param tradePair 对应交易对信息
		 * @param orderCode 生成的orderCode
		 * @return void
		 */
		@Override
		public void translateTo(DelegateEvent event, long sequence, Delegate delegate, TradePair tradePair, String orderCode) {
			event.setOrderCode(orderCode);
			event.setOrderTime(new Date());

			event.setMode(delegate.getOrderMode());
			event.setUserCode(delegate.getUserCode());
			event.setClientTime(delegate.getClientTime());
			event.setTradeType(delegate.getTradeType());
			event.setConvertRate(delegate.getPrice());
			event.setQuantity(delegate.getQuantity());

			event.setBaseCurrencyId(tradePair.getBaseCurrencyId());
			event.setTargetCurrencyId(tradePair.getTargetCurrencyId());
			event.setTradePairId(tradePair.getId());
		}
	}

}


