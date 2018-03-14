package com.entrusts.module.entity;

import java.math.BigDecimal;

public class TradePair {
    private Integer id;

    private Integer baseCurrencyId;

    private Integer targetCurrencyId;

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
}