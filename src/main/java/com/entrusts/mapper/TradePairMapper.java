package com.entrusts.mapper;

import com.entrusts.module.dto.AliasMap;
import com.entrusts.module.dto.BaseCurrency;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.entity.TradePair;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradePairMapper{

	/**
	 * @Description 查询交易对
	 * @param baseCurrencyAlias
	 * @param targetCurrencyAlias
	 * @return java.math.BigDecimal
	 */
	TradePair findTradePairByCoinName(@Param("baseCurrencyAlias") String baseCurrencyAlias,@Param("targetCurrencyAlias") String targetCurrencyAlias);

	/**
	 * 通过交易对Id查询交易对
	 * @param tradePairId
	 * @return
	 */
	TradePair findTradePairById(int tradePairId);

    List<BaseCurrency> getBaseCurrency();

    List<TargetMapCurrency> updateTargetCurrency();

    AliasMap getAllAlias(Integer tradePairId);

	List<TargetMapCurrency> getTargetCurrency(@Param("tradePareIds") List<Integer> tradePareIds);
}