package com.entrusts.module.enums;

/**
 * Created by sxu  
 */
public enum DelegateEventstatus {
	
	INSERT_ORDERDB_SUCCESS(10),  // 新增托单数据成功
	INSERT_ORDERDB_ERROR(11),  // 新增托单数据失败
	
	RREQUESTACCOUNT_SUCCESS(20),//锁币成功
	RREQUESTACCOUNT_ERROR(21),//锁币失败
	
	INSERT_MQPUSH_SUCCESS(30),//mq入库成功
	INSERT_MQPUSH_ERROR(31),//mq入库失败
	
	UPDATE_ORDERDB_SUCCESS(40),//更新托单数据成功
	UPDATE_ORDERDB_ERROR(41),//更新托单数据失败
	
	ORDER_SUCCESS(50),//托单完成
	ORDER_ERROR(51);//托单未完成
	
//	MQPUSH_SUCCESS(100),//推送mq成功
//	MQPUSH_ERROR(101);//推送mq失败
	
//	INSERT_ORDEREEVENT_SUCCESS(20),  // 新增托单数据LOG成功
//	INSERT_ORDEREEVENT_ERROR(21),  // 新增托单数据LOG失败
//	LAST_LOGHANDLER_SUCCESS(60),     // 日志记录成功
//	LAST_LOGHANDLER_ERROR(61);     // 日志记录失败
	
	DelegateEventstatus(int value) {
		this.value = value;
	}
	private int value;
	public int getValue() {
		return value;
	}
	
}
