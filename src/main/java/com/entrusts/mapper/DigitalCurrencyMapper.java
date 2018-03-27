package com.entrusts.mapper;

import com.entrusts.module.entity.DigitalCurrency;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DigitalCurrencyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DigitalCurrency record);

    DigitalCurrency selectByPrimaryKey(Integer id);

    List<DigitalCurrency> selectAll();

    int updateByPrimaryKey(DigitalCurrency record);

    /**
     * 根据多个key获取货币信息
     * @param ids
     * @return
     */
    List<DigitalCurrency> selectByPrimaryKeys(@Param("ids") Set<Integer> ids);
}