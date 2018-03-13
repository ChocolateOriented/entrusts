package com.entrusts.mapper;

import com.entrusts.module.entity.OrderEvent;
import java.util.List;

public interface OrderEventMapper {
	
    int delete(Long id);

    int insertOrderEvent(OrderEvent record);

    OrderEvent get(Long id);

    List<OrderEvent> selectAll();

    int update(OrderEvent record);
    
}