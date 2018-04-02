package com.entrusts.module.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithdrawnDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userCode;

	private Integer encryptCurrencyId;

	private String address;

	private BigDecimal quantity;

	private String messageCode;

	private String tradePassword;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getTradePassword() {
		return tradePassword;
	}

	public void setTradePassword(String tradePassword) {
		this.tradePassword = tradePassword;
	}

}
