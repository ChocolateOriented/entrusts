package com.entrusts.module.enums;

/**
 * Created by cyuan on 2018/3/12.
 */
public enum RedisKeyNameEnum {
    baseCurrency("base_currency","基准货币redis中的key前缀"),
    keyTarget("key_target_currency_","目标货币的key前缀"),
    fieldTarget("field_target_currency_","目标货币field前缀"),
    keyNow("key_currency_now_","最新价格key前缀"),
    fieldNow("field_targe_now_","最新价格field前缀");


    private String value;
    private String desc;

    RedisKeyNameEnum(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
