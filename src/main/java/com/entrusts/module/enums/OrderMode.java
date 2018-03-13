package com.entrusts.module.enums;

/**
 * Created by sxu  
 */
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
