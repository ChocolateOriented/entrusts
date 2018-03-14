package com.entrusts.module.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;

/**
 * Created by jxli on 2018/3/5.
 */
public class Order implements Serializable {

	private String orderCode;
	private String userCode;
	private Date clientTime;//下单时客户端时间
	private Date orderTime;//下单时服务器端时间
	private Integer tradePairId;//交易对id
	private BigDecimal convertRate;//基准货币兑换目标货币的比率
	private TradeType tradeType;//买卖方向
	private BigDecimal quantity;//数量
	private OrderMode mode;//交易模式
	private OrderStatus status;//状态
	private BigDecimal serviceFeeRate;//交易费率
	private BigDecimal dealAmount;//已成交金额
	private BigDecimal dealQuantity;//已成交数量
	private Date lastedDealTime;
	private Date createdTime;
	private Date updatedTime;
	private Long isDeleted;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Integer tradePairId) {
		this.tradePairId = tradePairId;
	}

	public BigDecimal getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(BigDecimal convertRate) {
		this.convertRate = convertRate;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public OrderMode getMode() {
		return mode;
	}

	public void setMode(OrderMode mode) {
		this.mode = mode;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getServiceFeeRate() {
		return serviceFeeRate;
	}

	public void setServiceFeeRate(BigDecimal serviceFeeRate) {
		this.serviceFeeRate = serviceFeeRate;
	}

	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

	public BigDecimal getDealQuantity() {
		return dealQuantity;
	}

	public void setDealQuantity(BigDecimal dealQuantity) {
		this.dealQuantity = dealQuantity;
	}

	public Date getLastedDealTime() {
		return lastedDealTime;
	}

	public void setLastedDealTime(Date lastedDealTime) {
		this.lastedDealTime = lastedDealTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
}

