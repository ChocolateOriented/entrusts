package com.entrusts.module.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

public class HistoryOrderView {

	private String orderCode;

	private Date date;

	private String baseCurrency;

	private String targetCurrency;

	private String tradeType;

	private BigDecimal orderPrice;

	private BigDecimal orderTargetQuantity;

	private BigDecimal dealTargetQuantity;

	private BigDecimal dealBaseAmount;

	private BigDecimal serviceFee;

	private String status;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getDate() {
		return date == null ? null : date.getTime();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderTargetQuantity() {
		return orderTargetQuantity;
	}

	public void setOrderTargetQuantity(BigDecimal orderTargetQuantity) {
		this.orderTargetQuantity = orderTargetQuantity;
	}

	public BigDecimal getDealTargetQuantity() {
		return dealTargetQuantity == null ? new BigDecimal(0) : dealTargetQuantity;
	}

	public void setDealTargetQuantity(BigDecimal dealTargetQuantity) {
		this.dealTargetQuantity = dealTargetQuantity;
	}

	public BigDecimal getDealBaseAmount() {
		return dealBaseAmount;
	}

	public void setDealBaseAmount(BigDecimal dealBaseAmount) {
		this.dealBaseAmount = dealBaseAmount;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
