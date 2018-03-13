package com.entrusts.module.entity;

import java.io.Serializable;
import java.util.Date;

import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;

/**
 * Created by sxu
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5789186753968378066L;
	
	private Long orderCode;
	private String userCode;
	private Date clientTime;//下单时客户端时间
	private Date orderTime;//下单时服务器端时间
	private Long tradePairId;//交易对id
	private Double convertRate;//基准货币兑换目标货币的比率
	private TradeType tradeType;//买卖方向
	private Double quantity;//数量
	private Double amount;//金额(基于基准货币)O
	private OrderMode mode;//交易模式
	private OrderStatus status;//状态
	private Double serviceFeeRate;//交易费率
	private Double dealAmout;//已成交金额
	private Double dealQuantity;//已成交数量
	private Date lastedDealTime;
	private Date createdTime;
	private Date updatedTime;
	private Long isDeleted;
	

	public Order() {
		super();
	}


	public Order(Long orderCode, String userCode, Date clientTime, Date orderTime, Long tradePairId, Double convertRate,
			TradeType tradeType, Double quantity, Double amount, OrderMode mode, OrderStatus status,
			Double serviceFeeRate, Double dealAmout, Double dealQuantity, Date lastedDealTime, Date createdTime,
			Date updatedTime, Long isDeleted) {
		super();
		this.orderCode = orderCode;
		this.userCode = userCode;
		this.clientTime = clientTime;
		this.orderTime = orderTime;
		this.tradePairId = tradePairId;
		this.convertRate = convertRate;
		this.tradeType = tradeType;
		this.quantity = quantity;
		this.amount = amount;
		this.mode = mode;
		this.status = status;
		this.serviceFeeRate = serviceFeeRate;
		this.dealAmout = dealAmout;
		this.dealQuantity = dealQuantity;
		this.lastedDealTime = lastedDealTime;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.isDeleted = isDeleted;
	}
	
	
	

	public Order(Long orderCode, String userCode, Date clientTime, Date orderTime, Long tradePairId, Double convertRate,
			TradeType tradeType, Double quantity, Double amount, OrderMode mode, OrderStatus status, Date createdTime,
			Date updatedTime, Long isDeleted) {
		super();
		this.orderCode = orderCode;
		this.userCode = userCode;
		this.clientTime = clientTime;
		this.orderTime = orderTime;
		this.tradePairId = tradePairId;
		this.convertRate = convertRate;
		this.tradeType = tradeType;
		this.quantity = quantity;
		this.amount = amount;
		this.mode = mode;
		this.status = status;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.isDeleted = isDeleted;
	}




	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
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

	public Double getConvertRate() {
		return convertRate;
	}

	public void setConvertRate(Double convertRate) {
		this.convertRate = convertRate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getServiceFeeRate() {
		return serviceFeeRate;
	}

	public void setServiceFeeRate(Double serviceFeeRate) {
		this.serviceFeeRate = serviceFeeRate;
	}

	public Double getDealAmout() {
		return dealAmout;
	}

	public void setDealAmout(Double dealAmout) {
		this.dealAmout = dealAmout;
	}


	public Double getDealQuantity() {
		return dealQuantity;
	}

	public void setDealQuantity(Double dealQuantity) {
		this.dealQuantity = dealQuantity;
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


	public OrderStatus getStatus() {
		return status;
	}


	public void setStatus(OrderStatus status) {
		this.status = status;
	}





	
	
}

