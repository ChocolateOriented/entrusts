package com.entrusts.module.dto;

import com.entrusts.module.entity.Order.OrderMode;
import com.entrusts.module.entity.Order.TradeType;
import java.util.Date;

import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;


/**
 * Created by jxli on 2018/3/5.
 */
public class DelegateEvent {

	private String orderCode;
	private String userCode;
	private Date clientTime;
	private Date orderTime;
	private Integer baseCurrencyId;
	private Integer targetCurrencyId;
	private Integer tradePairId;
	private BigDecimal convertRate;
	private TradeType tradeType;//买卖方向
	private BigDecimal quantity;
	private BigDecimal amount;
	private OrderMode mode;//交易模式
	private OrderStatus status;// 状态
	private DelegateEventstatus delegateEventstatus; // 队列执行状态

	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Long getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Long tradePairId) {
		this.tradePairId = tradePairId;
	}

	public Double getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(Double convertRate) {
		this.convertRate = convertRate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public OrderMode getMode() {
		return mode;
	}

	public void setMode(OrderMode mode) {
		this.mode = mode;
	}
}
