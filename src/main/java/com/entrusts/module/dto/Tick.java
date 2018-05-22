package com.entrusts.module.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cyuan on 2018/5/11.
 */
public class Tick implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer targetCurrencyId;
    private BigDecimal high;
    private BigDecimal low;

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }
}
