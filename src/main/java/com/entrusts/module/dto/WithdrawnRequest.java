package com.entrusts.module.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class WithdrawnRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer coinId;

	private String address;

	private BigDecimal amount;

	private String code;

	private String password;

	public Integer getCoinId() {
		return coinId;
	}

	public void setCoinId(Integer coinId) {
		this.coinId = coinId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
