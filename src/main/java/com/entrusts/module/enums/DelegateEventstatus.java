package com.entrusts.module.enums;

/**
 * Created by sxu  
 */
public enum DelegateEventstatus implements BaseCodeEnum {

	PUBLISH_ORDER_SUCCESS(0),//发布托单成功
	INSERT_ORDERDB_ERROR(10),  // 新增托单数据失败
	RREQUESTACCOUNT_ERROR(20),//锁币失败
	PUSH_MATCH_ERROR(30);//通知撮合失败

	DelegateEventstatus(int value) {
		this.value = value;
	}

	private int value;
	public int getValue() {
		return value;
	}
	
}
