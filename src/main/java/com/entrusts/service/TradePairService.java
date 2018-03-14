package com.entrusts.service;

import com.entrusts.mapper.TradePairMapper;
import com.entrusts.module.entity.TradePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jxli on 2018/3/9.
 */
@Service
@CacheConfig(cacheNames="TradePair")
public class TradePairService extends BaseService {
	@Autowired
	TradePairMapper tradePairMapper;

	/**
	 * @Description 查询交易对, 使用本地缓存
	 * @param baseCurrencyAlias 基础货币
	 * @param targetCurrencyAlias 目标货币
	 * @return com.entrusts.module.entity.TradePair
	 */
	@Cacheable(key = "#baseCurrencyAlias+'_'+#targetCurrencyAlias")
	public TradePair findTradePairByCoinName(String baseCurrencyAlias, String targetCurrencyAlias){
		return tradePairMapper.findTradePairByCoinName(baseCurrencyAlias,targetCurrencyAlias);
	}
}
