package com.entrusts.mapper;

import com.entrusts.module.entity.Deal;
import java.util.List;

public interface DealMapper {

    int insert(Deal record);

    List<Deal> selectAll();
}