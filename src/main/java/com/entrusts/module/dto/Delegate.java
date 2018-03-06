package com.entrusts.module.dto;

import java.util.Date;

/**
 * Created by jxli on 2018/3/5.
 */
public class Delegate {

	private Date clientTime;
	private Long tradePairId;
	private Double convertRate;
	private Long direction;
	private Double quantity;
	private Double amount;

	private Long mode;

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
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

	public Long getDirection() {
		return direction;
	}

	public void setDirection(Long direction) {
		this.direction = direction;
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

	public Long getMode() {
		return mode;
	}

	public void setMode(Long mode) {
		this.mode = mode;
	}
}
