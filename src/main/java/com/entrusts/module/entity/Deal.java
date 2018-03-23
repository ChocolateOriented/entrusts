package com.entrusts.module.entity;

import java.math.BigDecimal;

public class Deal {

    private String tradeCode;

    private String bidOrderCode;

    private String askOrderCode;

    private BigDecimal tradeFee;

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

    public BigDecimal getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(BigDecimal tradeFee) {
        this.tradeFee = tradeFee;
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