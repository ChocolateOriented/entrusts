package com.entrusts.service;

import com.entrusts.module.dto.Delegate;
import com.entrusts.module.dto.DelegateEvent;
import com.entrusts.module.entity.TradePair;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.TradeType;
import com.lmax.disruptor.EventTranslatorThreeArg;
import java.text.DecimalFormat;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * Created by jxli on 2018/3/13.
 */
@Service
public class DelegateTranslator extends BaseService implements EventTranslatorThreeArg<DelegateEvent, Delegate, TradePair, OrderMode> {

	/**
	 * @Description 将委托信息存入Disruptor队列预先创建的Event中
	 * @param event
	 * @param sequence
	 * @param delegate
	 * @param tradePair
	 * @param orderMode
	 * @return void
	 */
	@Override
	public void translateTo(DelegateEvent event, long sequence, Delegate delegate, TradePair tradePair, OrderMode orderMode) {
		event.setOrderTime(new Date());
		event.setMode(orderMode);

		event.setUserCode(delegate.getUserCode());
		event.setClientTime(delegate.getClientTime());
		event.setTradeType(delegate.getTradeType());
		event.setConvertRate(delegate.getPrice());
		event.setQuantity(delegate.getQuantity());

		event.setBaseCurrencyId(tradePair.getBaseCurrencyId());
		event.setTargetCurrencyId(tradePair.getTargetCurrencyId());
		event.setTradePairId(tradePair.getId());

		generateOrderCode(event);
	}

	/**
	 * @return java.lang.String
	 * @Description 生成ordercode, snowFlakeId + 预留位(0) + 交易对Id(4字符) + 交易类型与成交模式(1字符)
	 * 使用10进制存储订单信息
	 */
	private void generateOrderCode(DelegateEvent event) {

		final int MODE_BIT = 1; //成交模式占用2进制位数

		DecimalFormat format = new DecimalFormat("0000");
		int mode = event.getMode().equals(OrderMode.limit) ? 0 : 1;
		int type = event.getTradeType().equals(TradeType.buy) ? 0 : 1;
		int modeAndtype = type << MODE_BIT | mode;

		event.setOrderCode(super.generateId() + "0" + format.format(event.getTradePairId().intValue()) + modeAndtype);
	}
}
