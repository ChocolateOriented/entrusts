package com.entrusts.module.enums;

/**
 * Created by sxu
 */
public enum TradeType {
	BUY(1), SELL(2);

	TradeType(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}
}
