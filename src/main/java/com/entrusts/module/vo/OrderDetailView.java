package com.entrusts.module.vo;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;
import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailView {

	private String orderCode; //托单编号
	private Date createdTime; //委托时间
	private String baseCurrency; //基准货币
	private String targetCurrency; //目标货币
	private TradeType tradeType; //交易类型(sell, buy)
	private BigDecimal orderPrice; //委托单价
	private BigDecimal orderTargetQuantity; //委托数量
	private BigDecimal dealTargetQuantity; //成交数量
	private BigDecimal dealBaseAmount; //成交金额
	private OrderStatus status; //状态(0:委托中(不可撤销), 1:交易中, 2:完成交易, 3:撤销)
	private BigDecimal serviceFee; //费率
	private Date lastedDealTime;//最后成交时间
	private OrderMode mode;//交易模式

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
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
		return dealTargetQuantity;
	}

	public void setDealTargetQuantity(BigDecimal dealTargetQuantity) {
		this.dealTargetQuantity = dealTargetQuantity;
	}

	public BigDecimal getDealBaseAmount() {
		return dealBaseAmount;
	}

	public void setDealBaseAmount(BigDecimal dealBaseAmount) {
		this.dealBaseAmount = dealBaseAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Date getLastedDealTime() {
		return lastedDealTime;
	}

	public void setLastedDealTime(Date lastedDealTime) {
		this.lastedDealTime = lastedDealTime;
	}

	public OrderMode getMode() {
		return mode;
	}

	public void setMode(OrderMode mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
