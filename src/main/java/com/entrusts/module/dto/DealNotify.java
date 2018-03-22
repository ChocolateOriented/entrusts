package com.entrusts.module.dto;

import com.entrusts.module.entity.Deal;

public class DealNotify {

	private String code;
	
	private Deal bidOrder;

	private Deal askOrder;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Deal getBidOrder() {
		return bidOrder;
	}

	public void setBidOrder(Deal bidOrder) {
		this.bidOrder = bidOrder;
	}

	public Deal getAskOrder() {
		return askOrder;
	}

	public void setAskOrder(Deal askOrder) {
		this.askOrder = askOrder;
	}

}
