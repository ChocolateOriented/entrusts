package com.entrusts.module.dto;

import java.math.BigDecimal;

public class DealNotify {

	private String code;
	
	private Integer baseCurrencyId;

	private Integer targetCurrencyId;

	private BigDecimal dealPrice;

	private BigDecimal dealQuantity;

	private Long createdTime;

	private OrderDealDetail bidOrder;

	private OrderDealDetail askOrder;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(Integer baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public Integer getTargetCurrencyId() {
		return targetCurrencyId;
	}

	public void setTargetCurrencyId(Integer targetCurrencyId) {
		this.targetCurrencyId = targetCurrencyId;
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

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public OrderDealDetail getBidOrder() {
		return bidOrder;
	}

	public void setBidOrder(OrderDealDetail bidOrder) {
		this.bidOrder = bidOrder;
	}

	public OrderDealDetail getAskOrder() {
		return askOrder;
	}

	public void setAskOrder(OrderDealDetail askOrder) {
		this.askOrder = askOrder;
	}

	public class OrderDealDetail {

		private String userCode;
		
		private String orderCode;
		
		private String orderType;
		
		private String tradeType;
		
		private BigDecimal tradeFee;
		
		private Long createdTime;
		
		private BigDecimal dealPrice;

		private BigDecimal dealQuantity;

		private BigDecimal dealAmount;

		private Integer dealEncryptCurrencyId;
		
		private Integer tradeEncryptCurrencyId;
		
		private BigDecimal tradeEncryptCurrencyQuantity;
		
		private Integer isInitiator;

		public String getUserCode() {
			return userCode;
		}

		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}

		public String getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}

		public String getOrderType() {
			return orderType;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}

		public String getTradeType() {
			return tradeType;
		}

		public void setTradeType(String tradeType) {
			this.tradeType = tradeType;
		}

		public BigDecimal getTradeFee() {
			return tradeFee;
		}

		public void setTradeFee(BigDecimal tradeFee) {
			this.tradeFee = tradeFee;
		}

		public Long getCreatedTime() {
			return createdTime;
		}

		public void setCreatedTime(Long createdTime) {
			this.createdTime = createdTime;
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

		public Integer getDealEncryptCurrencyId() {
			return dealEncryptCurrencyId;
		}

		public void setDealEncryptCurrencyId(Integer dealEncryptCurrencyId) {
			this.dealEncryptCurrencyId = dealEncryptCurrencyId;
		}

		public Integer getTradeEncryptCurrencyId() {
			return tradeEncryptCurrencyId;
		}

		public void setTradeEncryptCurrencyId(Integer tradeEncryptCurrencyId) {
			this.tradeEncryptCurrencyId = tradeEncryptCurrencyId;
		}

		public BigDecimal getTradeEncryptCurrencyQuantity() {
			return tradeEncryptCurrencyQuantity;
		}

		public void setTradeEncryptCurrencyQuantity(BigDecimal tradeEncryptCurrencyQuantity) {
			this.tradeEncryptCurrencyQuantity = tradeEncryptCurrencyQuantity;
		}

		public Integer getIsInitiator() {
			return isInitiator;
		}

		public void setIsInitiator(Integer isInitiator) {
			this.isInitiator = isInitiator;
		}
		
	}

}
