package com.entrusts.module.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

public class Deal {

    private String code;

    private String orderCode;

    private String orderType;

    private String tradeType;

    private BigDecimal tradeFee;

    private BigDecimal dealPrice;

    private BigDecimal dealQuantity;

    private Integer baseCurrencyid;

    private Integer targetCurrencyid;

    private Long createdTime;

    private Byte isInitiator;

    private Integer tradePairId;

    public String getTradeCode() {
        return (orderCode == null ? "" : orderCode) + (code == null ? "" : code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    @JSONField(name = "tradeEncryptCurrencyQuantity")
    public void setDealQuantity(BigDecimal dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public Integer getBaseCurrencyid() {
        return baseCurrencyid;
    }

    @JSONField(name = "dealEncryptCurrencyId")
    public void setBaseCurrencyid(Integer baseCurrencyid) {
        this.baseCurrencyid = baseCurrencyid;
    }

    public Integer getTargetCurrencyid() {
        return targetCurrencyid;
    }

    @JSONField(name = "tradeEncryptCurrencyId")
    public void setTargetCurrencyid(Integer targetCurrencyid) {
        this.targetCurrencyid = targetCurrencyid;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getIsInitiator() {
        return isInitiator;
    }

    public void setIsInitiator(Byte isInitiator) {
        this.isInitiator = isInitiator;
    }

    public Integer getTradePairId() {
        return tradePairId;
    }

    public void setTradePairId(Integer tradePairId) {
        this.tradePairId = tradePairId;
    }
}