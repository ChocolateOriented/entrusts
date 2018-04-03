package com.entrusts.module.dto.result;

import java.text.DecimalFormat;

public enum ResultConstant {
    SUCCESS(0L, "ok"),
    INNER_ERROR(3L, "系统内部错误"),
    EMPTY_PARAM(4L, "缺少参数或参数错误"),
    EMPTY_ENTITY(5L, "请求的数据不存在"),
    SYSTEM_BUSY(6L, "系统繁忙, 请重试!"),
    REPEAT_REQUEST(7L,"重复请求, 已处理"),

    //10: 交易委托
    INVALID_TOKEN(1001,"无效Request-Token"),
    EXCEED_MAX_DELEGATE(1002,"同时发布托单数量达到上限");

    public final long code;
    public final String message;
    public static final String BASE_CODE = "1115" ;

    /**
     * @Description 报错号共8位  {####}(平台号)+{##}(模块)+{##}(异常)
     * @param
     * @return long
     */
    public long getFullCode() {
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        return this.code != SUCCESS.code?Long.valueOf(BASE_CODE + decimalFormat.format(this.code)) :this.code;
    }

    private ResultConstant(long code, String message) {
        this.code = code;
        this.message = message;
    }
}
