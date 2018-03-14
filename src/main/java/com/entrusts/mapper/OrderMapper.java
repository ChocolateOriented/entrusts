package com.entrusts.mapper;

import com.entrusts.module.entity.Order;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderQuery;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

	/**
	 * 插入被邀请码数据
	 */
	int insertOrder(Order order);

	/**
	 * 更新托单状态（交易中）
	 */
	int updateOrderStatus(@Param("status") OrderStatus status, @Param("orderCode") String orderCode);

	Order get(Long orderCode);

	List<HistoryOrderView> findHistoryOrderByPage(OrderQuery orderQuery);

	long countHistoryOrderByTime(OrderQuery orderQuery);

	List<HistoryOrderView> findHistoryOrderByTime(OrderQuery orderQuery, @Param("limit") int limit);

	List<HistoryOrderView> findLimitHistoryOrder(@Param("userCode") String userCode, @Param("limit") int limit);

	int totalHistoryOrder(String userCode);

	HistoryOrderView getHistoryOrder(Order order);
	
	void updateOrderNewDeal(Deal trade);
	
	int completeOrder(Order order);
}