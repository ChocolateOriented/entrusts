package com.entrusts.module.entity;

import java.util.Date;

/**
 * Created by jxli on 2018/3/5.
 */
public class Order {

	private Long id;
	private Long orderCode;
	private String userCode;
	private Date clientTime;//下单时客户端时间
	private Date orderTime;//下单时服务器端时间
	private Long tradePairId;//交易对id
	private Double convertRate;//基准货币兑换目标货币的比率
	private Long direction;//买卖方向(1:买, 2:卖)
	private Double quantity;//数量
	private Double amount;//金额(基于基准货币)
	private Long mode;//交易模式(1:限价交易, 2:市价交易)
	private Long status;//状态(10: 托单中, 11:托单失败, 20:交易中, 30:完成交易, 40:撤销)
	private Double serviceFeeRate;//交易费率
	private Double dealAmout;//已成交金额
	private Double dealQuantity;//已成交数量
	private Date lastedDealTime;
	private Date createdTime;
	private Date updatedTime;
	private Long isDeleted;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	public Long getDirection() {
		return direction;
	}

	public void setDirection(Long direction) {
		this.direction = direction;
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


	public Long getMode() {
		return mode;
	}

	public void setMode(Long mode) {
		this.mode = mode;
	}


	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
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

}

