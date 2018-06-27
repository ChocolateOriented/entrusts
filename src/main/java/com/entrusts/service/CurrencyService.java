package com.entrusts.service;

import com.entrusts.mapper.CurrencyMapper;
import com.entrusts.module.dto.BaseCurrency;
import com.entrusts.module.dto.CurrencyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cyuan on 2018/6/26.
 */
@Service
@CacheConfig(cacheNames = "CurrencyInfo")
public class CurrencyService {
    @Autowired
    public CurrencyMapper currencyMapper;
    @Cacheable
    public List<CurrencyInfo> getAllCurrencyInfo() {
        List<CurrencyInfo> allCurrency = currencyMapper.getAllCurrency();
        for (CurrencyInfo currencyInfo :     allCurrency) {
            currencyInfo.setChargeRate(new BigDecimal(0.2).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        return allCurrency;
    }

    @Cacheable(key = "#alias + 'alias'")
    public BaseCurrency getCurrencyByAlias(String alias) {
        return currencyMapper.getCurrencyByAlias(alias);
    }
}
