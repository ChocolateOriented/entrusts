package com.entrusts.mapper;

import com.entrusts.module.entity.DigitalCurrency;

import java.util.List;

public interface DigitalCurrencyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCurrency record);

    DigitalCurrency selectByPrimaryKey(Integer id);

    List<DigitalCurrency> selectAll();

    int updateByPrimaryKey(DigitalCurrency record);
}