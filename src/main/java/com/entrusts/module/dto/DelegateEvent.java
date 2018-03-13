package com.entrusts.module.dto;

import java.util.Date;

import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;


/**
 * Created by jxli on 2018/3/5.
 */
public class DelegateEvent {

	private Long orderCode;
	private String userCode;
	private Date clientTime;
	private Date orderTime;
	private Long tradePairId;
	private Double convertRate;
	private TradeType tradeType;// 买卖方向
	private Double quantity;
	private Double amount;
	private OrderMode mode;// 交易模式
	private OrderStatus status;// 状态
	private Integer targetCurrencyId; //数字货币
	private Integer baseCurrencyId;  //流通货币
	
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

//	public String getDelegateEventstatus() {
//		return delegateEventstatus;
//	}
//
//	public void setDelegateEventstatus(String delegateEventstatus) {
//		this.delegateEventstatus = delegateEventstatus;
//	}

	public Integer getTargetCurrencyId() {
		return targetCurrencyId;
	}

	public void setTargetCurrencyId(Integer targetCurrencyId) {
		this.targetCurrencyId = targetCurrencyId;
	}

	public Integer getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(Integer baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public DelegateEventstatus getDelegateEventstatus() {
		return delegateEventstatus;
	}

	public void setDelegateEventstatus(DelegateEventstatus delegateEventstatus) {
		this.delegateEventstatus = delegateEventstatus;
	}

	
	

}
