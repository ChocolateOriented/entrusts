package com.entrusts.service;

import com.entrusts.mapper.DigitalCurrencyMapper;
import com.entrusts.mapper.TradePairMapper;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.module.entity.DigitalCurrency;
import com.entrusts.module.entity.TradePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jxli on 2018/3/9.
 */
@Service
@CacheConfig(cacheNames="TradePair")
public class TradePairService extends BaseService {
	@Autowired
	TradePairMapper tradePairMapper;
	@Autowired
	DigitalCurrencyMapper digitalCurrencyMapper;

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

	/**
	 * 查询交易对根据交易对
	 * @param tradePairId
	 * @return
	 */
	@Cacheable(key = "#tradePairId")
	public TradePair findTradePairById(int tradePairId){
		return tradePairMapper.findTradePairById(tradePairId);
	}

	/**
	 * 根据交易对List获取目标货币信息
	 * @param
	 * @return
	 */
	@Cacheable()
	public List<TargetMapCurrency> getTargetCurrency(){
		return tradePairMapper. getTargetCurrency();
	}
	/**
	 * 根据id查货币信息
	 * @param
	 * @return
	 */
	@Cacheable(key = "#currencyId+'_currencyId'")
	public DigitalCurrency findCurrencyById(int currencyId){
		return digitalCurrencyMapper.selectByPrimaryKey(currencyId);
	}
}
