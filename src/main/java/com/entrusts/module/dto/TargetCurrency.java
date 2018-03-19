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
    private BigDecimal todayStartPrice;//当日起始价格
    private BigDecimal currentPrice;//当前最新价格

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
}
