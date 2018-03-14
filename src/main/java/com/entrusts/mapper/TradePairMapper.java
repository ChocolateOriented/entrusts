package com.entrusts.mapper;

import com.entrusts.module.entity.TradePair;
import org.apache.ibatis.annotations.Param;

public interface TradePairMapper{

	/**
	 * @Description 查询交易对
	 * @param baseCurrencyAlias
	 * @param targetCurrencyAlias
	 * @return java.math.BigDecimal
	 */
	TradePair findTradePairByCoinName(@Param("baseCurrencyAlias") String baseCurrencyAlias,@Param("targetCurrencyAlias") String targetCurrencyAlias);
}