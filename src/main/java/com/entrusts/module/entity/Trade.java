package com.entrusts.module.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
    private Long tradeCode;

    private Long orderCode;

    private BigDecimal dealPrice;

    private BigDecimal dealQuantity;

    private BigDecimal dealAmount;

    private BigDecimal serviceFee;

    private Date dealTime;

    public Long getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(Long tradeCode) {
        this.tradeCode = tradeCode;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public BigDecimal getDealQuantity() {
        return dealQuantity;
    }

    public void setDealQuantity(BigDecimal dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
}