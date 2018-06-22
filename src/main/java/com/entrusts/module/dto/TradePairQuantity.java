package com.entrusts.module.dto;

import java.math.BigDecimal;

public class TradePairQuantity {

	private Integer tradePairId;

	private BigDecimal buyQuantity;

	private BigDecimal sellQuantity;

	public Integer getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Integer tradePairId) {
		this.tradePairId = tradePairId;
	}

	public BigDecimal getBuyQuantity() {
		return buyQuantity == null ? new BigDecimal(0) : buyQuantity;
	}

	public void setBuyQuantity(BigDecimal buyQuantity) {
		this.buyQuantity = buyQuantity;
	}

	public BigDecimal getSellQuantity() {
		return sellQuantity == null ? new BigDecimal(0) : sellQuantity;
	}

	public void setSellQuantity(BigDecimal sellQuantity) {
		this.sellQuantity = sellQuantity;
	}

}
