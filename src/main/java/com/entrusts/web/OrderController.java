package com.entrusts.web;

import com.entrusts.module.dto.BaseResponse;
import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.Date;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
public class OrderController {
/*	@Autowired
	Disruptor<DelegateEvent> delegateDisruptor;

	private static final EventTranslatorTwoArg<DelegateEvent, Delegate, String> DELEGATE_TRANSLATOR =
			(event, sequence, delegate, userCode) -> {
				//TODO 生成ordercode
				*//**
				 数字货币类型（数字编号）
				 买卖类型（B买，C卖）
				 成交模式（主要是限价交易和市价交易）
				 预留字段（默认为0）
				 *//*
//				event.setOrderCode();
				event.setOrderTime(new Date());
				event.setUserCode(userCode);
				BeanUtils.copyProperties(delegate, event);
			};


	@RequestMapping(value = "delegateLimit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delegateLimit(@RequestHeader("Account-Code") String userCode, @RequestBody@Validated Delegate delegate) {
		//TODO 检查request是否处理过, 建议前端生成requestId
		//TODO 数据校验
		delegateDisruptor.publishEvent(DELEGATE_TRANSLATOR, delegate,userCode);
		return null;
	}*/
}
