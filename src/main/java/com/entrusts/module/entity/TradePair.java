package com.entrusts.module.entity;

import java.math.BigDecimal;

public class TradePair {

	private Integer id;

	private Integer baseCurrencyId;//基准货币

	private String baseCurrencyName;

	private Integer targetCurrencyId;//目标货币

	private String targetCurrencyName;

	private BigDecimal minTradeQuantity;

	private Byte isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getMinTradeQuantity() {
		return minTradeQuantity;
	}

	public void setMinTradeQuantity(BigDecimal minTradeQuantity) {
		this.minTradeQuantity = minTradeQuantity;
	}

	public Byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}

	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}

	public String getTargetCurrencyName() {
		return targetCurrencyName;
	}

	public void setTargetCurrencyName(String targetCurrencyName) {
		this.targetCurrencyName = targetCurrencyName;
	}
}