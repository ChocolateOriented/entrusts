package com.entrusts.mapper;

import com.entrusts.module.entity.Trade;
import java.util.List;

public interface TradeMapper {

    int insert(Trade record);

    List<Trade> selectAll();
}