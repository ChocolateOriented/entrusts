package com.entrusts.service;

import com.entrusts.mapper.DigitalCurrencyMapper;
import com.entrusts.mapper.TradePairMapper;
import com.entrusts.module.dto.TargetCurrency;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.module.entity.DigitalCurrency;
import com.entrusts.module.entity.TradePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

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
		List<TargetMapCurrency> targetMapCurrencies = new ArrayList<>();
		Map<String,List<TargetCurrency>> listMap = new HashMap<>();
		//取出所有交易对
		List<TradePair> tradePairList = tradePairMapper.getAllTradePair();
		//把交易对中的所有货币的key放入set中
		Set<Integer> set = new HashSet<>();
		for(TradePair t : tradePairList){
			set.add(t.getBaseCurrencyId());
			set.add(t.getTargetCurrencyId());
		}

		Map<Integer,DigitalCurrency> currencyMap = new HashMap<>();
		//根据key的set获取对应的货币
		List<DigitalCurrency> digitalCurrencyList = digitalCurrencyMapper.selectByPrimaryKeys(set);
		//把对应的货币放入map中,方便获取
		for(DigitalCurrency d : digitalCurrencyList){
			currencyMap.put(d.getId(),d);
		}
		//交易对信息装换成map,key是基准货币,value是List<TargetCurrency>
		for(TradePair t : tradePairList){
			DigitalCurrency baseDC = currencyMap.get(t.getBaseCurrencyId());
			DigitalCurrency targetDC = currencyMap.get(t.getTargetCurrencyId());
			if(baseDC == null || targetDC == null){
				continue;
			}
			TargetCurrency targetCurrency = new TargetCurrency();
			targetCurrency.setTargetCurrencyId(targetDC.getId());
			targetCurrency.setName(targetDC.getName());
			targetCurrency.setAlias(targetDC.getAlias());
			targetCurrency.setTradePareId(t.getId());
			targetCurrency.setCurrentPrice(new BigDecimal(0));
			targetCurrency.setTodayStartPrice(new BigDecimal(0));
			//判断是否包含key就放入key对应的list中
			if(listMap.containsKey(baseDC.getAlias())){
				listMap.get(baseDC.getAlias()).add(targetCurrency);
			}else {
				List<TargetCurrency> targetCurrencyList = new ArrayList<>();
				targetCurrencyList.add(targetCurrency);
				listMap.put(baseDC.getAlias(),targetCurrencyList);
			}
		}
		//将map装换成List<TargetMapCurrency>
		for(Map.Entry<String,List<TargetCurrency>> entryset: listMap.entrySet()){
			TargetMapCurrency targetMapCurrency = new TargetMapCurrency();
			targetMapCurrency.setBaseAlias(entryset.getKey());
			targetMapCurrency.setTargetCurrencies(entryset.getValue());
			targetMapCurrencies.add(targetMapCurrency);
		}
		return targetMapCurrencies;
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
