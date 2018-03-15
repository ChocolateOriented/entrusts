package com.entrusts.module.enums;

/**
 * Created by cyuan on 2018/3/13.
 */
public enum UTCTimeEnum {

    IDLE(720,120),
    AESST(660,130),
    ACSST(630,135),
    AEST(600,140),
    CAST(570,145),
    AWSST(540,150),
    MT(510,155),
    CCT(480,160),
    JT(450,165),
    ALMST(420,170),
    MMT(390,175),
    ALMT(360,180),
    IOT(300,190),
    AFT(270,195),
    EAST(240,200),
    IRT(210,205),
    EAT(180,210),
    BDST(120,220),
    BST(60,230),
    GMT(0,0),
    WAT(-60,10),
    FNT(-120,20),
    NDT(-150,25),
    ADT(-180,30),
    NFT(-210,35),
    AST(-240,40),
    ACT(-300,50),
    CST(-360,60),
    MST(-420,70),
    AKDT(-480,80),
    AKST(-540,90),
    MART(-570,95),
    AHST(-600,100),
    NT(-660,110);
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
