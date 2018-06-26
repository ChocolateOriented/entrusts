package com.entrusts.service;

import com.entrusts.mapper.CurrencyMapper;
import com.entrusts.module.dto.CurrencyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
        return currencyMapper.getAllCurrency();
    }
}
