package com.entrusts.module.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 * Created by jxli on 2018/3/5.
 */
public class Delegate {

	@NotNull(message = "下单时间不能为空")
	private Date clientTime;
	@NotNull(message = "交易对不能为空")
	private Long tradePairId;
	@NotNull(message = "价格不能为空")
	private Double convertRate;
	@NotNull(message = "买卖方向不能为空")
	private Long direction;
	@NotNull(message = "交易数量不能为空")
	private Double quantity;
	@NotNull(message = "交易金额不能为空")
	private Double amount;
	@NotNull(message = "交易模式不能为空")
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
