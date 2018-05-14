package com.entrusts.module.dto;

import java.io.Serializable;

/**
 * Created by cyuan on 2018/5/11.
 */
public class Tick implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer targetCurrencyId;
    private Integer high;
    private Integer low;

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getLow() {
        return low;
    }

    public void setLow(Integer low) {
        this.low = low;
    }
}
