package com.entrusts.module.dto;

import java.math.BigDecimal;

/**
 * Created by cyuan on 2018/3/8.
 */
public class TargetCurrency {
    private Integer tradePareId;
    private Integer targetCurrencyId;
    private String name;//货币名称
    private String alias; //货币别名
    private Integer baseCurrencyId;//基准货币id
    private String baseAlias;//基准货币别名
    private BigDecimal todayStartPrice;//当日起始价格
    private BigDecimal currentPrice;//当前最新价格

    private Integer pricePrecision = 8; //价格精度位数
    private Integer amountPrecision = 8; //数量精度位数



    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public String getBaseAlias() {
        return baseAlias;
    }

    public void setBaseAlias(String baseAlias) {
        this.baseAlias = baseAlias;
    }

    public Integer getTradePareId() {
        return tradePareId;
    }

    public void setTradePareId(Integer tradePareId) {
        this.tradePareId = tradePareId;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public BigDecimal getTodayStartPrice() {
        return todayStartPrice;
    }

    public void setTodayStartPrice(BigDecimal todayStartPrice) {
        this.todayStartPrice = todayStartPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getAmountPrecision() {
        return amountPrecision;
    }

    public void setAmountPrecision(Integer amountPrecision) {
        this.amountPrecision = amountPrecision;
    }
}
