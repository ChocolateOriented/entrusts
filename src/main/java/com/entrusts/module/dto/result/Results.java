package com.entrusts.module.dto.result;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by sxu on 2018/2/28.
 */
public class Results {

    public static final int BASE = 10100000;
    private Long code;
    private String message;
    private Object data = new JSONObject();
    //private Long timestamp;

    public Results() {
    }

    private Results(Long code, String message, Object data) {

        this.code = code;
        this.message = message;
        if(data != null) {
            this.data = data;
        }

        //this.timestamp = Long.valueOf(Instant.now().toEpochMilli());
    }

    public static Results ok(ResultConstant c) {
        return new Results(Long.valueOf(c.code), c.message, (Object)null);
    }

    public static Results ok(ResultConstant c, ResultDate data) {
        return new Results(Long.valueOf(c.code), c.message, data.value());
    }

    public static Results ok(ResultConstant c, String message) {
        return new Results(Long.valueOf(c.code), message, (Object)null);
    }

    public static Results ok(ResultConstant c, String message, ResultDate data) {
        return new Results(Long.valueOf(c.code), message, data.value());
    }

    public static Results nok(ResultConstant c) {
        return new Results(Long.valueOf(c.code), c.message, (Object)null);
    }

    public static Results nok(ResultConstant c, ResultDate data) {
        return new Results(Long.valueOf(c.code), c.message, data.value());
    }

    public static Results nok(ResultConstant c, String message) {
        return new Results(Long.valueOf(c.code), message, (Object)null);
    }

    public static Results nok(ResultConstant c, String message, ResultDate data) {
        return new Results(Long.valueOf(c.code), message, data.value());
    }

    public Long getCode() {
        return this.code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
