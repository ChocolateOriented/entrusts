package com.entrusts.mapper;

import com.entrusts.module.entity.Order;
import com.entrusts.module.vo.CurrentEntrusts;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderQuery;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

	List<HistoryOrderView> findHistoryOrderByPage(OrderQuery orderQuery);

	long countHistoryOrderByTime(OrderQuery orderQuery);

	List<HistoryOrderView> findHistoryOrderByTime(OrderQuery orderQuery, @Param("limit") int limit);

	List<HistoryOrderView> findLimitHistoryOrder(@Param("userCode") String userCode, @Param("limit") int limit);

	int totalHistoryOrder(String userCode);

	HistoryOrderView getHistoryOrder(Order order);

	List<CurrentEntrusts> findCurrentOrderByPage(OrderQuery orderQuery);

	long countCurrentOrderByTime(OrderQuery orderQuery);

	List<CurrentEntrusts> findCurrentOrderByTime(OrderQuery orderQuery, @Param("limit") int limit);

	List<CurrentEntrusts> findLimitCurrentOrder(@Param("userCode") String userCode, @Param("limit") int limit);

	int totalCurrentOrder(String userCode);

	CurrentEntrusts getCurrentOrder(Order order);
}