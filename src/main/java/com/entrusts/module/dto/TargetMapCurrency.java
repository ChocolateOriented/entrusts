package com.entrusts.module.dto;

import java.util.List;

/**
 * Created by cyuan on 2018/3/12.
 */
public class TargetMapCurrency {
    private Integer baseCurrencyId;//基准货币
    private String baseAlias;
    private List<TargetCurrency> targetCurrencies;

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

    public List<TargetCurrency> getTargetCurrencies() {
        return targetCurrencies;
    }

    public void setTargetCurrencies(List<TargetCurrency> targetCurrencies) {
        this.targetCurrencies = targetCurrencies;
    }
}
