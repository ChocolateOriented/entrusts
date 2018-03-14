package com.entrusts.module.enums;

/**
 * Created by sxu
 */
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
