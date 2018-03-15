package com.entrusts.module.dto;

/**
 * Created by cyuan on 2018/3/8.
 */
public class TargetCurrency {

    private Integer targetCurrencyId;
    private String name;//货币名称
    private String alias; //货币别名
    private double todayStartPrice;//当日起始价格
    private double currentPrice;//当前最新价格

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

    public double getTodayStartPrice() {
        return todayStartPrice;
    }

    public void setTodayStartPrice(double todayStartPrice) {
        this.todayStartPrice = todayStartPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
