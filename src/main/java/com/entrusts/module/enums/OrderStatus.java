package com.entrusts.module.enums;

/**
 * Created by sxu  
 */
//托单状态
public enum OrderStatus implements BaseCodeEnum {
	DELEGATING(10),//托单中
	DELEGATE_FAILED(11),//托单失败
	TRADING(20),//交易中
	COMPLETE(30),//完成交易
	WITHDRAW(40),//撤销
	WITHDRAW_UNTHAWING(50);//撤销未解冻


	OrderStatus(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}
}