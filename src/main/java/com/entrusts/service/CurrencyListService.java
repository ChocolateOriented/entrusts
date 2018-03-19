package com.entrusts.service;

import com.alibaba.fastjson.JSON;

import com.entrusts.mapper.TradePairMapper;
import com.entrusts.module.dto.AliasMap;
import com.entrusts.module.dto.BaseCurrency;
import com.entrusts.module.dto.TargetCurrency;
import com.entrusts.module.dto.TargetMapCurrency;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.enums.RedisKeyNameEnum;
import com.entrusts.module.enums.UTCTimeEnum;
import com.entrusts.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by cyuan on 2018/3/8.
 */
@Service
@Transactional
public class CurrencyListService extends BaseService {

    @Autowired
    private TradePairMapper tradePairMapper;

    private static final Logger logger = LoggerFactory.getLogger(CurrencyListService.class);

    /**
     * 获取基准货币
     * @return
     */
    @Transactional(readOnly = false)
    public List<BaseCurrency> getBaseCurrency() {

        List<BaseCurrency> baseCurrencies = this.getCurrencyList(RedisKeyNameEnum.baseCurrency.getValue(),null,BaseCurrency.class);

        if(baseCurrencies == null){
            //查数据库
            baseCurrencies =tradePairMapper.getBaseCurrency();
            if(baseCurrencies == null){
                logger.info("读取数据库中数据失败");
                return null;
            }
            String baseCurrency = setCurrencyList(RedisKeyNameEnum.baseCurrency.getValue(), null, baseCurrencies);
            if(baseCurrency == null || "0".equals(baseCurrency)){
                logger.info("基准货币放入缓存失败");
            }
        }

        return baseCurrencies;
    }

    /**
     * 获取目标货币列表
     * @return
     */
    @Transactional(readOnly = false)
    public List<TargetCurrency> getTargetCurrency(String baseCurrency,Integer value) {
        String time1 = RedisKeyNameEnum.keyTarget.getValue()+value;
        String currency1= RedisKeyNameEnum.fieldTarget.getValue()+baseCurrency;
        //获取目标货币
        List<TargetCurrency> currencyList = this.getCurrencyList(time1, currency1, TargetCurrency.class);
        if(currencyList == null){
            return null;
        }
        //获取最新价格
        String key = RedisKeyNameEnum.keyNow.getValue()+baseCurrency;
        Map<String, String> map = RedisUtil.getMap(key);
        if(map == null){
            //从数据库获取最新数据
            logger.info("缓存中获取最新价格失败");
            return currencyList;
        }
        for(TargetCurrency targetCurrency : currencyList){
            String s = map.get(RedisKeyNameEnum.fieldNow.getValue()+targetCurrency.getAlias());
            if(s==null){
                targetCurrency.setCurrentPrice(0);
                logger.info("没有获取到最新价格");
            }else {
                targetCurrency.setCurrentPrice(Double.valueOf(s));
            }

        }

        return currencyList;
    }

    /**
     * 获取redis中的list
     * @param key
     * @param field
     * @param t
     * @param <T>
     * @return
     */
    @Transactional(readOnly = false)
    public <T> List<T> getCurrencyList(String key,String field,Class<T> t){
        if(field == null){
            String s = RedisUtil.get(key);
            if(s==null){
                logger.info("获取redis数据失败");
                return null;
            }
            List<T> ts = JSON.parseArray(s, t);
            return ts;
        }
        String value = RedisUtil.getMap(key, field);
        if(value == null){
            return null;
        }
        List<T> ts =null;
        try {
            ts= JSON.parseArray(value, t);
        }catch (Exception e){
            logger.info("json解析成list错误",e);
        }

        return ts;

    }

    /**
     * list放入缓存
     * @param key
     * @param field
     * @param t
     * @param <T>
     * @return
     */
    @Transactional(readOnly = false)
    public <T> String setCurrencyList (String key,String field,T t){
        if(t == null){
            logger.info("传入参数为空");
            return null;
        }
        String s = JSON.toJSONString(t);
        if(field == null){
            String set = RedisUtil.set(key, s, 0);
            return set;
        }else {
            String s1 = RedisUtil.mapPut(key, field, s);
            return s1;
        }
    }

    /**
     * 每半时跟新数据库的基准数据到redis
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateTargetCurrency(){
        String redisKey = getRedisKey();
        addTargetCurrencyToRedis(redisKey);
    }

    /**
     * 根据UTC时间获取redis中的key
     * @return
     */
    public String getRedisKey(){
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int key ;
        if(hour<12){
            key = -(hour * 60 + minute);
        }else {
            key =1440- (hour * 60 + minute);
        }

        String redisKey =RedisKeyNameEnum.keyTarget.getValue()+ key;
        return redisKey;
    }
    public void addTargetCurrencyToRedis(String redisKey){

        logger.info("开始更新目标货币到缓存:"+redisKey);
        List<TargetMapCurrency> targetMapCurrencies = tradePairMapper.updateTargetCurrency();
        if(targetMapCurrencies == null || targetMapCurrencies.size() ==0){
            logger.info("读取数据库失败");
            return;
        }
        for (TargetMapCurrency t : targetMapCurrencies){
            String redisFeild=RedisKeyNameEnum.fieldTarget.getValue()+t.getBaseAlias();
            List<TargetCurrency> targetCurrencies = t.getTargetCurrencies();
            setCurrencyList(redisKey,redisFeild,targetCurrencies);
        }
        logger.info("更新目标货币到缓存结束"+redisKey);
    }
    /**
     * 根据时间获取UTCTimerEnum中对应的名字作为redis中的key
     * @param integer
     * @return
     */
    public String getUTC(Integer integer){
        String UTC = null;
        UTCTimeEnum[] values = UTCTimeEnum.values();
        for(UTCTimeEnum u : values){
            if(u.getValue()==integer){
                UTC=u.name();
            }
        }
        return UTC;
    }
    /**
     * 订单过来,更新数据到Redis
     * @param deal
     */
    @Transactional(readOnly = false)
    public void updateCurrentPrice(Deal deal){
        if(deal.getTradePairId()==null){
            logger.info("订单中没有交易对");
            return;
        }
        AliasMap aliasMap = tradePairMapper.getAllAlias(deal.getTradePairId());
        String key = RedisKeyNameEnum.keyNow.getValue() + aliasMap.getBaseAlias();
        String feild= RedisKeyNameEnum.fieldNow.getValue() + aliasMap.getTargetAlias();
        String s = setCurrencyList(key, feild, deal.getDealPrice());
        if(s == null || "0".equals(s)){
            logger.info("最新价格插入缓存有误");
        }
    }

    public List<TargetMapCurrency> getAllTargetCurrency(Integer time) {
        List<BaseCurrency> baseCurrency = getBaseCurrency();
        if(baseCurrency == null || baseCurrency.size() == 1){
            return null;
        }
        List<TargetMapCurrency> list = new ArrayList<>();
        for (BaseCurrency baseCurrency1 : baseCurrency){
            TargetMapCurrency targetMapCurrency = new TargetMapCurrency();
            targetMapCurrency.setBaseAlias(baseCurrency1.getAlias());
            List<TargetCurrency> targetCurrency = getTargetCurrency(baseCurrency1.getAlias(), time);
            if(targetCurrency == null){
                continue;
            }
            targetMapCurrency.setTargetCurrencies(targetCurrency);
            list.add(targetMapCurrency);
        }
        return list;
    }
}