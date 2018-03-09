package com.entrusts.module.vo;

import java.math.BigDecimal;

public class OrderView {

	private String orderCode;

	private Long date;

	private String baseCurrency;

	private String targetCurrency;

	private String tradeType;

	private BigDecimal orderPrice;

	private BigDecimal orderTargetQuantity;

	private BigDecimal dealTargetQuantity;

	private String status;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
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
		return dealTargetQuantity;
	}

	public void setDealTargetQuantity(BigDecimal dealTargetQuantity) {
		this.dealTargetQuantity = dealTargetQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
