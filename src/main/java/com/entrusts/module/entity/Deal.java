package com.entrusts.module.entity;

import java.math.BigDecimal;

public class Deal {

    private String tradeCode;

    private String bidOrderCode;

    private String askOrderCode;

    private BigDecimal bidTradeFee;

    private BigDecimal askTradeFee;

    private BigDecimal dealPrice;

    private BigDecimal dealQuantity;

    private Integer baseCurrencyId;

    private Integer targetCurrencyId;

    private Long createdTime;

    private Integer tradePairId;

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getBidOrderCode() {
        return bidOrderCode;
    }

    public void setBidOrderCode(String bidOrderCode) {
        this.bidOrderCode = bidOrderCode;
    }

    public String getAskOrderCode() {
        return askOrderCode;
    }

    public void setAskOrderCode(String askOrderCode) {
        this.askOrderCode = askOrderCode;
    }

    public BigDecimal getBidTradeFee() {
		return bidTradeFee;
	}

	public void setBidTradeFee(BigDecimal bidTradeFee) {
		this.bidTradeFee = bidTradeFee;
	}

	public BigDecimal getAskTradeFee() {
		return askTradeFee;
	}

	public void setAskTradeFee(BigDecimal askTradeFee) {
		this.askTradeFee = askTradeFee;
	}

	public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getDealQuantity() {
        return dealQuantity;
    }

    public void setDealQuantity(BigDecimal dealQuantity) {
        this.dealQuantity = dealQuantity;
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

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getTradePairId() {
        return tradePairId;
    }

    public void setTradePairId(Integer tradePairId) {
        this.tradePairId = tradePairId;
    }
}