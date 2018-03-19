package com.entrusts.mapper;

import com.entrusts.module.entity.Deal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DealMapper {

    int insert(Deal record);

    List<Deal> selectAll();

    List<Deal> getRecentDeal(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}