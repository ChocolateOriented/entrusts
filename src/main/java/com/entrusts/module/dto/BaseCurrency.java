package com.entrusts.module.dto;

/**
 * Created by cyuan on 2018/3/8.
 */
public class BaseCurrency {

    private Integer baseCurrencyId;
    private String name;//货币名称
    private String alias; //货币别名
    private String unit;//货币单位

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
