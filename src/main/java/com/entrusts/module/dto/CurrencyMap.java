package com.entrusts.module.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyuan on 2018/5/11.
 */
public class CurrencyMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer baseCurrencyId;
    private List<Tick> ticks;

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public void setTicks(List<Tick> ticks) {
        this.ticks = ticks;
    }
}
