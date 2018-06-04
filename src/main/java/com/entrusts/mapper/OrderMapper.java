package com.entrusts.mapper;

import com.entrusts.module.dto.DealNotify.OrderDealDetail;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.vo.CurrentEntrusts;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderDetailView;
import com.entrusts.module.vo.OrderQuery;
import java.util.Date;
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
	int updateOrderStatus(@Param("status") OrderStatus status, @Param("orderCode") String orderCode,@Param("updatedTime") Date updatedTime);

	Order get(String orderCode);

	List<HistoryOrderView> findHistoryOrderByPage(OrderQuery orderQuery);

	long countHistoryOrderByTime(OrderQuery orderQuery);

	List<HistoryOrderView> findHistoryOrderByTime(@Param("orderQuery") OrderQuery orderQuery, @Param("limit") int limit);

	List<HistoryOrderView> findLimitHistoryOrder(@Param("userCode") String userCode, @Param("limit") int limit);

	long totalHistoryOrder(String userCode);

	/**
	 * 查询全量当前数据
	 * @param userCode
	 * @return
	 */
	List<CurrentEntrusts> findCurrentOrder(@Param("userCode") String userCode);

    void updateOrderNewDeal(OrderDealDetail orderDealDetail);

	int completeOrder(Order order);

	/**
	 * 获取撤销订单状态20
	 * @param orderCode
	 * @return
	 */
	Order queryUnfreezeInfo(@Param("orderCode") String orderCode,@Param("userCode") String userCode);

	/**
	 * 根据用户code获取所有撤销订单状态20
	 * @param userCode
	 * @return
	 */
	List<Order> queryAllUnfreezeInfo(String userCode);

	/**
	 * 获取交易状态为20或50的单个订单
	 * @param orderCode
	 * @param userCode
	 * @return
	 */
    Order queryErrorOrderUnfreezeInfo(@Param("orderCode") String orderCode,@Param("userCode") String userCode);

	/**
	 * 获取交易状态20和50的所有订单
	 * @param userCode
	 * @return
	 */
	List<Order> queryErrorOrderAllUnfreezeInfo(String userCode);

	/**
	 * @Description 托单详情
	 * @param orderCode
	 * @param userCode
	 * @return com.entrusts.module.vo.OrderDetailView
	 */
	OrderDetailView findOrderDetailView(@Param("orderCode") String orderCode,@Param("userCode") String userCode);
}