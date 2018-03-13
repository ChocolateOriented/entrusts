package com.entrusts.module.vo;

public class OrderQuery {

	private String userCode;

	private String orderCode;

	private String baseCurrency;

	private String targetCurrency;

	private String tradeType;

	private Long startTime;

	private Long endTime;

	private Long toCreatedTime;

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

	public Integer getDirection() {
		if (tradeType == null) {
			return null;
		}
		
		if (tradeType.equals("buy")) {
			return 1;
		} else if (tradeType.equals("sell")) {
			return 2;
		} else {
			return null;
		}
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getToCreatedTime() {
		return toCreatedTime;
	}

	public void setToCreatedTime(Long toCreatedTime) {
		this.toCreatedTime = toCreatedTime;
	}

	public boolean hasCondition() {
		return baseCurrency != null || targetCurrency != null || tradeType != null
				|| startTime != null || endTime != null || toCreatedTime != null;
	}
	
	public boolean matchConditions(HistoryOrderView orderView) {
		if (orderView == null) {
			return false;
		}
		
		if (baseCurrency != null && !baseCurrency.equals(orderView.getBaseCurrency())) {
			return false;
		}
		
		if (targetCurrency != null && !targetCurrency.equals(orderView.getTargetCurrency())) {
			return false;
		}
		
		if (tradeType != null && !tradeType.equals(orderView.getTradeType())) {
			return false;
		}
		
		if (startTime != null && (orderView.getDate() == null || startTime > orderView.getDate())) {
			return false;
		}
		
		if (endTime != null && (orderView.getDate() == null || endTime < orderView.getDate())) {
			return false;
		}
		
		if (toCreatedTime != null && (orderView.getDate() == null || toCreatedTime <= orderView.getDate())) {
			return false;
		}
		
		return true;
	}

	public boolean matchConditionsByCurrent(CurrentEntrusts currentEntrusts) {
		if (currentEntrusts == null) {
			return false;
		}

		if (baseCurrency != null && !baseCurrency.equals(currentEntrusts.getBaseCurrency())) {
			return false;
		}

		if (targetCurrency != null && !targetCurrency.equals(currentEntrusts.getTargetCurrency())) {
			return false;
		}

		if (tradeType != null && !tradeType.equals(currentEntrusts.getTradeType())) {
			return false;
		}

		if (startTime != null && (currentEntrusts.getDate() == null || startTime > currentEntrusts.getDate())) {
			return false;
		}

		if (endTime != null && (currentEntrusts.getDate() == null || endTime < currentEntrusts.getDate())) {
			return false;
		}

		return true;
	}
}
