package com.entrusts.module.dto;

import java.util.List;

/**
 * Created by cyuan on 2018/3/12.
 */
public class TargetMapCurrency {
    private String baseAlias;
    private List<TargetCurrency> targetCurrencies;

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
