package com.entrusts.module.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jxli on 2018/3/21.
 * 冻结货币
 */
public class FreezeDto implements Serializable {
	private String orderCode;
	private String userCode;
	private Integer encryptCurrencyId;
	private BigDecimal quantity;

	public FreezeDto(String orderCode, String userCode, Integer encryptCurrencyId, BigDecimal quantity) {
		this.orderCode = orderCode;
		this.userCode = userCode;
		this.encryptCurrencyId = encryptCurrencyId;
		this.quantity = quantity;
	}

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

	public Integer getEncryptCurrencyId() {
		return encryptCurrencyId;
	}

	public void setEncryptCurrencyId(Integer encryptCurrencyId) {
		this.encryptCurrencyId = encryptCurrencyId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}
