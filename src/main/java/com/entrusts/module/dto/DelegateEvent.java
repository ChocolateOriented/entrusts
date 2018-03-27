package com.entrusts.module.dto;

import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.TradeType;
import java.math.BigDecimal;
import java.util.Date;


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

	private DelegateEventstatus delegateEventstatus; // 队列执行状态
	private String remark;//备注

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
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

	public Integer getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(Integer baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public Integer getTargetCurrencyId() {
		return targetCurrencyId;
	}

	public void setTargetCurrencyId(Integer targetCurrencyId) {
		this.targetCurrencyId = targetCurrencyId;
	}

	public Integer getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Integer tradePairId) {
		this.tradePairId = tradePairId;
	}

	public BigDecimal getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(BigDecimal convertRate) {
		this.convertRate = convertRate;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OrderMode getMode() {
		return mode;
	}

	public void setMode(OrderMode mode) {
		this.mode = mode;
	}

	public DelegateEventstatus getDelegateEventstatus() {
		return delegateEventstatus;
	}

	public void setDelegateEventstatus(DelegateEventstatus delegateEventstatus) {
		this.delegateEventstatus = delegateEventstatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
