package com.entrusts.web.third;

import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.service.OrderService;
import com.entrusts.service.TradePairService;
import com.entrusts.util.RedisUtil;
import com.entrusts.util.StringUtils;
import com.entrusts.web.BaseController;
import com.entrusts.web.OrderDelegateController;
import com.lmax.disruptor.dsl.Disruptor;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

/**
 * Created by jxli on 2018/3/5.
 */
@RestController
@RequestMapping(value = "entrusts/third/order")
public class ThirdOrderDelegateController extends BaseController {

	@Autowired
	TradePairService tradePairService;
	@Autowired
	Disruptor<DelegateEvent> delegateDisruptor;
	@Autowired
	OrderService orderService;

	/**
	 * @param delegate 委托信息
	 * @param bindingResul delegate的有效性校验结果
	 * @return com.entrusts.module.dto.result.Results
	 * @Description 限价交易委托(供第三方平台调用)
	 */
	@PostMapping(value = "delegateLimit")
	@ResponseBody
	public Results delegateLimit(@RequestBody @Validated Delegate delegate, BindingResult bindingResul) {
		String userCode = delegate.getUserCode();
		if (StringUtils.isBlank(userCode)) {
			return new Results(ResultConstant.EMPTY_PARAM, "userCode不能为空");
		}

		String requestToken = delegate.getRequestToken();
		String handledTokenKey = OrderDelegateController.CACHE_DELEGATE_HANDLED_TOKEN_PREFIX + userCode + requestToken;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			String result = jedis.set(handledTokenKey, "", "NX", "PX", 24 * 60 * 60 * 1000);
			if (!"OK".equals(result)) {//已由其他线程处理
				return new Results(ResultConstant.REPEAT_REQUEST);
			}
		} catch (Exception e) {
			logger.info(userCode + "托单失败, 获取Redis连接失败", e);
			return new Results(ResultConstant.INNER_ERROR);
		} finally {
			RedisUtil.returnResource(jedis);
		}

		//数据校验
		if (bindingResul.hasErrors()) {
			return new Results(ResultConstant.EMPTY_PARAM, getFieldErrorsMessages(bindingResul));
		}
		TradePair tradePair = tradePairService.findTradePairByCoinName(delegate.getBaseCurrency(), delegate.getTargetCurrency());
		if (tradePair == null) {
			return new Results(ResultConstant.EMPTY_PARAM, "未知交易对");
		}
		BigDecimal minTradeQuantity = tradePair.getMinTradeQuantity();
		if (null == minTradeQuantity) {
			minTradeQuantity = new BigDecimal("0.00000001");
		}
		if (delegate.getQuantity2Decimal().compareTo(minTradeQuantity) < 0) {
			DecimalFormat format = new DecimalFormat("#.########");
			return new Results(ResultConstant.EMPTY_PARAM, "交易数量错误, 最小值" + format.format(minTradeQuantity));
		}
		BigDecimal minPrice = new BigDecimal("0.00000001");
		if (delegate.getPrice2Decimal().compareTo(minPrice) <= 0) {
			DecimalFormat format = new DecimalFormat("#.########");
			return new Results(ResultConstant.EMPTY_PARAM, "价格错误, 最小值" + format.format(minPrice));
		}

		//发布托单
		delegate.setOrderMode(OrderMode.limit);
		String orderCode = orderService.generateOrderCode(delegate.getOrderMode(), delegate.getTradeType(), tradePair.getId());
		delegateDisruptor.publishEvent(OrderDelegateController.delegateTranslator, delegate, tradePair, orderCode);
		return Results.ok().putData("orderCode", orderCode);
	}
}


