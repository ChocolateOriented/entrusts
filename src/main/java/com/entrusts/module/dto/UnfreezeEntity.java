package com.entrusts.module.dto;

import com.entrusts.module.entity.Order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cyuan on 2018/3/13.
 */
public class UnfreezeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer encryptCurrencyId;
    private BigDecimal residuequantity;
    private Integer targetCurrencyId;
    private Integer baseCurrencyId;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getEncryptCurrencyId() {
        return encryptCurrencyId;
    }

    public void setEncryptCurrencyId(Integer encryptCurrencyId) {
        this.encryptCurrencyId = encryptCurrencyId;
    }

    public BigDecimal getResiduequantity() {
        return residuequantity;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public void setResiduequantity(BigDecimal residuequantity) {
        this.residuequantity = residuequantity;
    }
}
