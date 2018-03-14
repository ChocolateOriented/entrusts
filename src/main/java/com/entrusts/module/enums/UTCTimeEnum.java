package com.entrusts.module.enums;

/**
 * Created by cyuan on 2018/3/13.
 */
public enum UTCTimeEnum {
    UTC(8),
    EUTC1(7),
    EUTC2(6),
    EUTC3(5),
    EUTC4(4),
    EUTC5(3),
    EUTC6(2),
    EUTC7(1),
    EUTC8(0),
    EUTC9(23),
    EUTC10(22),
    EUTC11(21),
    EWUTC12(20),
    WUTC1(9),
    WUTC2(10),
    WUTC3(11),
    WUTC4(12),
    WUTC5(13),
    WUTC6(14),
    WUTC7(15),
    WUTC8(16),
    WUTC9(17),
    WUTC10(18),
    WUTC11(19);
    private Integer time;
    UTCTimeEnum(Integer time){
        this.time = time;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
