package com.entrusts.mapper;

import com.entrusts.module.vo.OrderView;
import com.entrusts.module.vo.OrderQuery;

import java.util.List;

public interface OrderMapper {

    List<OrderView> findHistoryOrder(OrderQuery orderQuery);
}