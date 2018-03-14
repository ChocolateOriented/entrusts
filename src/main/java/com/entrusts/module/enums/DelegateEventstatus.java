package com.entrusts.module.enums;

/**
 * Created by sxu  
 */
public enum DelegateEventstatus {
	
	INSERT_ORDERDB_SUCCESS(10),  // 新增托单数据成功
	INSERT_ORDERDB_ERROR(11),  // 新增托单数据失败
	
	RREQUESTACCOUNT_SUCCESS(20),//锁币成功
	RREQUESTACCOUNT_ERROR(21),//锁币失败

	PUBLISH_ORDER_SUCCESS(30);//发布托单成功

	DelegateEventstatus(int value) {
		this.value = value;
	}
	private int value;
	public int getValue() {
		return value;
	}
	
}
