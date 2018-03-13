package com.entrusts.mapper;


import org.apache.ibatis.annotations.Param;

import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;

public interface OrderMapper {

	/**
	 * 插入被邀请码数据
	 */
	int insertOrder(Order order);

	/**
	 * 更新托单状态（交易中）
	 */
	int updateOrderStatus(@Param("status") OrderStatus status, @Param("orderCode") String orderCode);

}