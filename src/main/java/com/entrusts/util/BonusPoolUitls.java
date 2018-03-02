package com.entrusts.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sxu on 2018/2/1.
 */
public class BonusPoolUitls {
    private static final Logger logger = LoggerFactory.getLogger(BonusPoolUitls.class);

    private static AtomicInteger numberOfBonus;

    private static AtomicInteger remainingOfPeople;

    private static AtomicInteger bonus;

    private static volatile boolean isInit = false;

    public static boolean getIsInit() {
        return isInit;
    }

    public static void setIsInit(boolean isInit) {
        if(isInit == true){
            if (numberOfBonus == null || remainingOfPeople == null || bonus == null){
                return;
            }
        }
        BonusPoolUitls.isInit = isInit;
    }


    public static void initBonusPool(int numberOfBonus1, int remainingoOfPeople1, int bonus1) {
        numberOfBonus = new AtomicInteger(numberOfBonus1);
        remainingOfPeople = new AtomicInteger(remainingoOfPeople1);
        bonus = new AtomicInteger(bonus1);
        isInit = true;
        updateCache();
    }

    private static void BonusPoolinit(){
        if (!isInit){
            try {
                int tmpNumberOfBonus = Integer.valueOf(RedisUtil.get("entrusts_BonusPool_numberOfBonus"));
                int tmpRemainingOfPeople = Integer.valueOf(RedisUtil.get("entrusts_BonusPool_remainingOfPeople"));
                int tmpBonus = Integer.valueOf(RedisUtil.get("entrusts_BonusPool_bonus"));
                initBonusPool(tmpNumberOfBonus,tmpRemainingOfPeople,tmpBonus);
            }catch (Exception e){
                logger.info("redis异常:"+e);
            }

        }
    }

    public static void clear(){
        numberOfBonus = null;
        remainingOfPeople = null;
        bonus = null;
        isInit = false;
    }

    /**
     * 判断是否更新缓存里面的初始金额，如果更新读取缓存里面的值
     */
    private static void checkCache(){
        if (RedisUtil.islive == true){
            try {
                if (numberOfBonus.get() > Integer.valueOf(RedisUtil.get("entrusts_BonusPool_numberOfBonus"))
                        || remainingOfPeople.get() > Integer.valueOf(RedisUtil.get("entrusts_BonusPool_remainingOfPeople"))
                        || !Integer.valueOf(RedisUtil.get("entrusts_BonusPool_bonus")).equals(bonus.get())) {
                    isInit =false;
                    BonusPoolinit();
                }
            }catch (Exception e){
                logger.info("redis异常:"+e);
            }
        }

    }

    /**
     * 写入reids缓存
     */
    private static void updateCache(){
        try {
            if (RedisUtil.islive == true){
                RedisUtil.set("entrusts_BonusPool_numberOfBonus", numberOfBonus.get()+"", 0);
                RedisUtil.set("entrusts_BonusPool_remainingOfPeople", remainingOfPeople.get()+"", 0);
                RedisUtil.set("entrusts_BonusPool_bonus", bonus.get()+"", 0);
            }
        }catch (Exception e){
            logger.info("redis异常:"+e);
        }

    }


    /**
     * 奖金池里减去自定义的金额
     * @param delta
     * @return
     */
    public  static boolean reduce(int delta){
        BonusPoolinit();
        checkCache();
        if (getRemainingOfPeople() >0 && getNumberOfBonus() > 0){
            remainingOfPeople.decrementAndGet();
            numberOfBonus.addAndGet(-delta);
            updateCache();
            return true;
        }
        return false;
    }

    /**
     * 奖金池里减去固定金额
     * @return
     */
    public  static boolean reduce(){
        BonusPoolinit();
        checkCache();
        if (getRemainingOfPeople() >0 && getNumberOfBonus() > 0){
            remainingOfPeople.decrementAndGet();
            numberOfBonus.addAndGet(-bonus.get());
            updateCache();
            return true;
        }
        return false;
    }

    /**
     * 回滚
     * @return
     */
    public static boolean reduceRollback(){
        BonusPoolinit();
        checkCache();
        remainingOfPeople.incrementAndGet();
        numberOfBonus.addAndGet(bonus.get());
        updateCache();
        return true;
    }

    /**
     * 奖金池是否可有余额
     */
    public static boolean isDeductible(){
        if (getRemainingOfPeople() >0 && getNumberOfBonus() > 0){
            return true;
        }
        return false;
    }
    /**
     * 获取还可以获取奖金的剩余人数
     * @return
     */
    public static int getRemainingOfPeople(){
        BonusPoolinit();
        if (RedisUtil.islive){
            return Integer.valueOf(RedisUtil.get("entrusts_BonusPool_remainingOfPeople") == null ? remainingOfPeople.get()+"" : RedisUtil.get("entrusts_BonusPool_remainingOfPeople"));
        }
        return remainingOfPeople.get();
    }

    /**
     * 获取剩余奖金池资金金额
     * @return
     */
    public static int getNumberOfBonus() {
        BonusPoolinit();
        if (RedisUtil.islive){
            return Integer.valueOf(RedisUtil.get("entrusts_BonusPool_numberOfBonus") == null ? numberOfBonus.get()+"" : RedisUtil.get("entrusts_BonusPool_numberOfBonus"));
        }
        return numberOfBonus.get();
    }

}
