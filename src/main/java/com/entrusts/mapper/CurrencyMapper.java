package com.entrusts.mapper;

import com.entrusts.module.dto.BaseCurrency;
import com.entrusts.module.dto.CurrencyInfo;

import java.util.List;

/**
 * Created by cyuan on 2018/6/26.
 */
public interface CurrencyMapper {
    /**
     * 获取所有货币信息
     */
    List<CurrencyInfo> getAllCurrency();

    BaseCurrency getCurrencyByAlias(String alias);
}
