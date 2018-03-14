package com.entrusts.module.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jxli on 2018/3/5.
 */
public class Order implements Serializable {

	private String orderCode;
	private String userCode;
	private Date clientTime;//下单时客户端时间
	private Date orderTime;//下单时服务器端时间
	private Long tradePairId;//交易对id
	private BigDecimal convertRate;//基准货币兑换目标货币的比率
	private TradeType tradeType;//买卖方向
	private BigDecimal quantity;//数量
	private BigDecimal amount;//金额(基于基准货币)
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

	public java.util.Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(java.util.Date clientTime) {
		this.clientTime = clientTime;
	}

	public java.util.Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(java.util.Date orderTime) {
		this.orderTime = orderTime;
	}

	public Long getTradePairId() {
		return tradePairId;
	}

	public void setTradePairId(Long tradePairId) {
		this.tradePairId = tradePairId;
	}

	public BigDecimal getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(BigDecimal convertRate) {
		this.convertRate = convertRate;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public OrderMode getMode() {
		return mode;
	}

	public void setMode(OrderMode mode) {
		this.mode = mode;
	}

	public java.util.Date getLastedDealTime() {
		return lastedDealTime;
	}

	public void setLastedDealTime(java.util.Date lastedDealTime) {
		this.lastedDealTime = lastedDealTime;
	}

	public java.util.Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(java.util.Date createdTime) {
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	//买卖方向
	public enum TradeType {
		buy(1), sell(2);

		TradeType(int value) {
			this.value = value;
		}

		private int value;

		public int getValue() {
			return value;
		}
	}

	//交易模式
	public enum OrderMode {
		LIMIT_PRICE_DEAL(1),//限价交易
		MARKET_PRICE_DEAL(2);//市价交易

		OrderMode(int value) {
			this.value = value;
		}

		private int value;

		public int getValue() {
			return value;
		}
	}

	//托单状态
	public enum OrderStatus {
		DELEGATING(10),//托单中
		DELEGATE_FAILED(11),//托单失败
		TRADING(20),//交易中
		COMPLETE(30),//完成交易
		WITHDRAW(40);//撤销

		OrderStatus(int value) {
			this.value = value;
		}

		private int value;

		public int getValue() {
			return value;
		}
	}
}

