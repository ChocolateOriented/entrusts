package com.entrusts.web;

import com.entrusts.module.dto.BaseResponse;
import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	@Autowired
	private RingBuffer<DelegateEvent> delegateEventBuffer;

	private static final EventTranslatorOneArg<DelegateEvent, Delegate> DELEGATE_TRANSLATOR =
			(event, sequence, delegate) -> {
//					event.set(bb.getLong(0));
			};


	@RequestMapping(value = "delegate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public BaseResponse delegate(@RequestHeader("Account-Code") String userCode, @RequestBody Delegate delegate) {
		delegateEventBuffer.publishEvent(DELEGATE_TRANSLATOR, delegate);
		return null;
	}
}
