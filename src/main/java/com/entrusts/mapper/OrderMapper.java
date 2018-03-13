package com.entrusts.mapper;


import org.apache.ibatis.annotations.Param;

import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;

public interface OrderMapper {
	
    /**
     * 插入被邀请码数据
     * @param record
     * @return
     */
	public int insertOrder(Order order);
    
    /**
     * 更新托单状态（交易中）
     * @param record
     * @return
     */
    public int updateOrderStatus(@Param("status")OrderStatus status, @Param("orderCode")Long orderCode);
//
//    /**
//     * 根据用户code和订单号获取返利记录
//     * @param record
//     * @return
//     */
//    public RebateInfo getByUserOrder(RebateInfo record);
    

}