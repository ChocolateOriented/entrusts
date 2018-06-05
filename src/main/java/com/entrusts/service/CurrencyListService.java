package com.entrusts.service;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entrusts.manager.CandlestickClient;
import com.entrusts.manager.DealmakingClient;
import com.entrusts.mapper.DealMapper;
import com.entrusts.mapper.TradePairMapper;
import com.entrusts.module.dto.*;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Deal;
import com.entrusts.module.entity.DigitalCurrency;
import com.entrusts.module.enums.RedisKeyNameEnum;
import com.entrusts.module.enums.UTCTimeEnum;
import com.entrusts.util.HttpClientUtil;
import com.entrusts.util.RedisUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by cyuan on 2018/3/8.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "Currency")
public class CurrencyListService extends BaseService {

    @Autowired
    private TradePairMapper tradePairMapper;
    @Autowired
    private DealMapper dealMapper;
    @Autowired
    private TradePairService tradePairService;
    @Autowired
    private DealmakingClient dealmakingClient;

    @Autowired
    private CandlestickClient candlestickClient;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyListService.class);

    /**
     * 获取基准货币
     * @return
     */
    @Transactional(readOnly = false)
    public List<BaseCurrency> getBaseCurrency() {

        List<BaseCurrency> baseCurrencies = this.getCurrencyList(RedisKeyNameEnum.baseCurrency.getValue(),null,BaseCurrency.class);

        if(baseCurrencies==null||baseCurrencies.size() == 0){
            //查数据库
            baseCurrencies =tradePairMapper.getBaseCurrency();
            String baseCurrency = setCurrencyList(RedisKeyNameEnum.baseCurrency.getValue(), null, baseCurrencies);
            if(baseCurrency == null){
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
    public Page<TargetCurrency> getTargetCurrency(String baseCurrency, Integer value, Integer pageNum, Integer pageSize) {
        String time1 = RedisKeyNameEnum.keyTarget.getValue()+value;
        String currency1= RedisKeyNameEnum.fieldTarget.getValue()+baseCurrency;
        Page<TargetCurrency> page = new Page<>();
        List<TargetCurrency> currencyList = null;
        try{
            //获取目标货币
            currencyList = this.getCurrencyList(time1, currency1, TargetCurrency.class);
            if(currencyList == null){
                return page;
            }
            //分页
            if(pageNum == null || pageNum < 0){
                pageNum = 1;
            }
            if(pageSize == null || pageSize < 0){
                pageSize = currencyList.size();
            }

            Integer start = Math.min(currencyList.size(),(pageNum-1)*pageSize);
            Integer end = Math.min(currencyList.size(),pageNum*pageSize);
            currencyList = currencyList.subList(start,end);
            page.setEntities(currencyList);
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            page.setTotal((long)currencyList.size());
            //获取最新价格
            String key = RedisKeyNameEnum.keyNow.getValue()+baseCurrency;
            Map<String, String> map = RedisUtil.getMap(key);
            if(map == null){
                //从数据库获取最新数据
                logger.info("缓存中没有最新价格");
                return page;
            }
            for(TargetCurrency targetCurrency : page.getEntities()){
                String currentPrice = map.get(RedisKeyNameEnum.fieldNow.getValue()+targetCurrency.getAlias());

                if(currentPrice==null){
                    targetCurrency.setCurrentPrice(new BigDecimal(0));
                    logger.info("交易对id:"+targetCurrency.getTradePareId()+",没有获取到最新价格");
                }else {
                    targetCurrency.setCurrentPrice(new BigDecimal(currentPrice));
                }
                //设置开盘价
                String openPriceKey = RedisKeyNameEnum.keyOpenPrice.getValue() + baseCurrency;
                String openPriceField = RedisKeyNameEnum.fieldOpenPrice.getValue() + targetCurrency.getAlias();
                String openPriceStr = this.getOpenPrice(openPriceKey,openPriceField);
                if(openPriceStr != null){
                    targetCurrency.setTodayStartPrice(new BigDecimal(openPriceStr));
                }
            }


        }catch (Exception e){
            logger.info("获取目标货币失败",e);
            return null;
        }


        return page;
    }

    /**
     * 获取开盘价格
     * @return
     */
    private String getOpenPrice(String openPriceKey,String openPriceField) {
        Jedis jedis = null;
        String openPrice = null;
        try {
            jedis = RedisUtil.getResource();
            openPrice = jedis.hget(openPriceKey, openPriceField);
        } catch (Exception e) {
            logger.info("获取开盘价异常",e);
            return null;
        } finally {
            RedisUtil.returnResource(jedis);
        }
        return openPrice;
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
    public void setCurrencyToRedis(){
        long l = System.currentTimeMillis();
        //获取redis中的key
        String redisKey = getRedisKey();
        //把数据更新到redis中
        addTargetCurrencyToRedis(redisKey,l);
    }

    /**
     * 定时清理开盘价格
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearOpenPrice(){
        List<BaseCurrency> baseCurrencys = this.getBaseCurrency();
        for (BaseCurrency baseCurrency : baseCurrencys){
            String key = RedisKeyNameEnum.keyOpenPrice.getValue() + baseCurrency.getAlias();
            delOpenPrice(key);
        }

    }

    public void delOpenPrice(String key){
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getResource();
            jedis.del(key);
        }catch (Exception e){
            logger.info("删除开盘价失败",e);
        }finally {
            RedisUtil.returnResource(jedis);
        }
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

    /**把数据更新到redis中
     *
     * @param redisKey
     * @param now
     */
    public void addTargetCurrencyToRedis(String redisKey,Long now){

        logger.info("开始更新目标货币到缓存:"+redisKey);
        //从db获取订单数据
        List<TargetMapCurrency> targetMapCurrencies = targetMapCurrencies(now,redisKey);


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
     * 从db读取数据
     * @param now
     * @return
     */
    public List<TargetMapCurrency> targetMapCurrencies (Long now,String redisKey) {
        Long startTime = now - 86400000;
        //获取最近时间段的成交订单
        List<Deal> dealList = dealMapper.getRecentDeal(startTime, now);
        //获取目标货币交易对信息
        List<TargetMapCurrency> tradePairList = tradePairService.getTargetCurrency();
        //从redis获取旧的数据
        List<TargetMapCurrency> targetMapCurrenciesredis = getTargetMapCurrenciesFromRedis(redisKey);
        //给tradePairList赋值
        for (TargetMapCurrency targetMapCurrency : tradePairList) {
            for (TargetCurrency targetCurrency : targetMapCurrency.getTargetCurrencies()) {
                //给tradePairList赋值(从数据库中查出的数据)
                if(dealList != null && dealList.size()!=0){
                    for (Deal d : dealList) {
                        if (targetCurrency.getTradePareId() == d.getTradePairId()) {
                            targetCurrency.setTodayStartPrice(d.getDealPrice());
                        }
                    }
                }
                //说明数据库没有查出此数据,使用redis中的数据给tradePairList赋值
                if(targetMapCurrenciesredis != null && targetCurrency.getTodayStartPrice().equals(new BigDecimal(0))){
                    dataFromRedis(targetMapCurrenciesredis,targetCurrency);
                }
            }

        }
        return tradePairList;
    }

    /**
     * 从redis获取旧的数据,装换成List
     * @param redisKey
     * @return
     */
    public List<TargetMapCurrency> getTargetMapCurrenciesFromRedis(String redisKey){
        Map<String, String> map = RedisUtil.getMap(redisKey);
        if (map == null){
            return null;
        }
        Set<String> strings = map.keySet();
        List<TargetMapCurrency> targetMapCurrenciesredis = new ArrayList<>();
        for (String s : strings) {
            TargetMapCurrency targetMapCurrency = new TargetMapCurrency();
            targetMapCurrency.setBaseAlias(s.substring(22));
            List<TargetCurrency> targetCurrencies = JSON.parseArray(map.get(s), TargetCurrency.class);
            targetMapCurrency.setTargetCurrencies(targetCurrencies);
            targetMapCurrenciesredis.add(targetMapCurrency);
        }
        return targetMapCurrenciesredis;
    }
    /**
     * 说明数据库没有查出此数据,使用redis中的数据给tradePairList赋值
     */
    public void dataFromRedis(List<TargetMapCurrency> targetMapCurrenciesredis,TargetCurrency targetCurrency){
        for (TargetMapCurrency t : targetMapCurrenciesredis){
            for(TargetCurrency t1 : t.getTargetCurrencies()){
                if(targetCurrency.getTradePareId() == t1.getTradePareId()){
                    targetCurrency.setTodayStartPrice(t1.getTodayStartPrice()==null ? new BigDecimal(0) :t1.getTodayStartPrice());
                    return;
                }
            }
        }
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
//        if(deal.getTradePairId()==null){
//            logger.info("订单中没有交易对");
//            return;
//        }
        try {
			DigitalCurrency baseCurrency = tradePairService.findCurrencyById(deal.getBaseCurrencyId());
			DigitalCurrency targetCurrency = tradePairService.findCurrencyById(deal.getTargetCurrencyId());
			String key = RedisKeyNameEnum.keyNow.getValue() + baseCurrency.getAlias();
			String feild= RedisKeyNameEnum.fieldNow.getValue() + targetCurrency.getAlias();
			this.setCurrencyList(key, feild, deal.getDealPrice());
			logger.info("开始更新开盘价格");
			String openPriceKey = RedisKeyNameEnum.keyOpenPrice.getValue() + baseCurrency.getAlias();
			String openPriceField = RedisKeyNameEnum.fieldOpenPrice.getValue() + targetCurrency.getAlias();
            this.setOpenPrice(openPriceKey,openPriceField,deal.getDealPrice());
            logger.info("开盘价格更新结束");
		} catch (Exception e) {
			logger.info("最新价格插入缓存失败", e);
		}
    }

    public void setOpenPrice(String key, String field, BigDecimal dealPrice) {
        Jedis jedis = null;

        try{
            jedis = RedisUtil.getResource();
            Long flag = jedis.hsetnx(key, field, dealPrice.toString());
            if (flag == 1){
                logger.info("更新开盘价格成功,key:{},field:{},price:{}",key,field,dealPrice);
            }
        }catch (Exception e){
            logger.info("放入当前开盘价格异常",e);
        }finally {
            RedisUtil.returnResource(jedis);
        }
    }

    /**
     * 获取当天24时的时间戳
     * @return
     */
    public Long getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    public List<TargetMapCurrency> getAllTargetCurrency(Integer time) {
        List<BaseCurrency> baseCurrency = getBaseCurrency();
        if(baseCurrency == null || baseCurrency.size() == 0){
            return new ArrayList<>();
        }
        List<TargetMapCurrency> list = new ArrayList<>();
        for (BaseCurrency baseCurrency1 : baseCurrency){
            TargetMapCurrency targetMapCurrency = new TargetMapCurrency();
            targetMapCurrency.setBaseAlias(baseCurrency1.getAlias());
            targetMapCurrency.setBaseCurrencyId(baseCurrency1.getBaseCurrencyId());
            Page<TargetCurrency> page = getTargetCurrency(baseCurrency1.getAlias(), time,null,null);
            if(page == null ){
                return null;
            }
            if( page.getEntities() == null || page.getEntities().size() == 0){
                continue;
            }
            targetMapCurrency.setTargetCurrencies(page.getEntities());
            list.add(targetMapCurrency);
        }
        return list;
    }

    /**
     * 调用接口获取当日最高最低价格
     * @param time
     * @param
     * @return
     */
    public CurrencyMap getHighestAndLowestCurrency(Integer time,Integer baseCurrencyId ) {
        CurrencyMap currencyMap = new CurrencyMap();
        currencyMap.setBaseCurrencyId(baseCurrencyId);
        currencyMap.setTicks(new ArrayList<Tick>());
        String result = null;
        try{

            //调用接口获取所有数据
            result = candlestickClient.getHighestAndLowestCurrency(time);
        }catch (Exception e){
            logger.info("调用撮单系统获取当日最低最高价格接口异常",e);
            return null;
        }
        logger.info("调用接口:/api/candlesticks/v1/market/detail获取的数据:"+result);
        Results results = JSON.parseObject(result, Results.class);
        if(results == null || results.getData()==null){
            return currencyMap;
        }
        if(results.getCode()!=0){
            return null;
        }
        List<CurrencyMap> currencyMaps = results.getData().getJSONArray("entities").toJavaList(CurrencyMap.class);

        if(currencyMaps == null){
            return currencyMap;
        }
        for(CurrencyMap c : currencyMaps){
            if(c.getBaseCurrencyId() == baseCurrencyId){
                currencyMap = c;
            }
        }

        return currencyMap;
    }

    /**
     * @Description 查询支持的货币
     * @param
     * @return java.util.List<java.lang.String>
     */
    @Cacheable
    public List<String> findSupportCurrency() {
        return tradePairMapper.findSupportCurrency();
    }
}