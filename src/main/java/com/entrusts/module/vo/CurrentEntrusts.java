package com.entrusts.module.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jxguo on 2018/3/7.
 */
public class CurrentEntrusts {

    private String orderCode; //托单编号
    private Date date; //委托时间
    private String baseCurrency; //基准货币
    private String targetCurrency; //目标货币
    private String tradeType; //交易类型(sell, buy)
    private BigDecimal orderPrice; //委托单价
    private BigDecimal orderTargetQuantity; //委托数量
    private BigDecimal dealTargetQuantity; //成交数量
    private String status; //状态(0:委托中(不可撤销), 1:交易中, 2:完成交易, 3:撤销)
    private BigDecimal serviceFee; //费率

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getDate() {
        return date == null ? null : date.getTime();
    }

    public void setDate(Object date) {
        if (date instanceof Date){
            this.date = (Date) date;
        }else if (date instanceof Long){
            this.date = new Date((Long) date);
        }
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderTargetQuantity() {
        return orderTargetQuantity;
    }

    public void setOrderTargetQuantity(BigDecimal orderTargetQuantity) {
        this.orderTargetQuantity = orderTargetQuantity;
    }

    public BigDecimal getDealTargetQuantity() {
        return dealTargetQuantity == null ? new BigDecimal(0) : dealTargetQuantity;
    }

    public void setDealTargetQuantity(BigDecimal dealTargetQuantity) {
        this.dealTargetQuantity = dealTargetQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getServiceFee() {
        return serviceFee == null ? new BigDecimal(0) : serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }
}
