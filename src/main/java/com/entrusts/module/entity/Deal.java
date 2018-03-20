package com.entrusts.module.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class Deal {

	@NotNull(message = "交易流水号为空")
    private String tradeCode;

	@NotNull(message = "托单编号为空")
    private String orderCode;

	@NotNull(message = "无效的成交价格")
	@DecimalMin(value = "0", message = "无效的成交价格")
    private BigDecimal dealPrice;

	@NotNull(message = "无效的成交量")
	@DecimalMin(value = "0", message = "无效的成交量")
    private BigDecimal dealQuantity;

	@NotNull(message = "无效的成交金额")
	@DecimalMin(value = "0", message = "无效的成交金额")
    private BigDecimal dealAmount;

    private BigDecimal serviceFee;

    private Long dealTime;

    private Integer tradePairId;

    public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
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

    public Long getDealTime() {
        return dealTime;
    }

    public void setDealTime(Long dealTime) {
        this.dealTime = dealTime;
    }

    public Integer getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Integer tradePairId) {
		this.tradePairId = tradePairId;
	}

}