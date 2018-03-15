package com.entrusts.module.enums;

/**
 * Created by cyuan on 2018/3/13.
 */
public enum UTCTimeEnum {

    IDLE(720,200),
    AESST(660,210),
    ACSST(630,215),
    AEST(600,220),
    CAST(570,225),
    AWSST(540,230),
    MT(510,235),
    CCT(480,0),
    JT(450,5),
    ALMST(420,10),
    MMT(390,20),
    ALMT(360,25),
    IOT(300,30),
    AFT(270,35),
    EAST(240,40),
    IRT(210,40),
    EAT(180,50),
    BDST(120,60),
    BST(60,70),
    GMT(0,80),
    WAT(-60,90),
    FNT(-120,100),
    NDT(-150,105),
    ADT(-180,110),
    NFT(-210,115),
    AST(-240,120),
    ACT(-300,130),
    CST(-360,140),
    MST(-420,150),
    AKDT(-480,160),
    AKST(-540,170),
    MART(-570,175),
    AHST(-600,180),
    NT(-660,190);
    private Integer value;
    private Integer time;

    UTCTimeEnum(Integer value, Integer time) {
        this.value = value;
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static String getName(Integer value){
        UTCTimeEnum[] values = UTCTimeEnum.values();
        for (UTCTimeEnum utcTimeEnum : values){
            Integer value1 = utcTimeEnum.getValue();
            if(utcTimeEnum.getValue().equals(value)){
                return utcTimeEnum.name();
            }
        }
        return null;
    }
    public static String getNameByTime(Integer time){
        UTCTimeEnum[] values = UTCTimeEnum.values();
        for (UTCTimeEnum utcTimeEnum : values){
            if(utcTimeEnum.getTime().equals(time) ){
                return utcTimeEnum.name();
            }
        }
        return null;
    }
}
