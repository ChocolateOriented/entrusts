package com.entrusts.module.dto.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxu on 2018/2/28.
 */
public class Results {

    public static final int BASE = 10100000;

    private Long code;

    private String message;

    @JsonInclude(Include.NON_NULL)
    private JSONObject data;

    public Results() {
    }

    public Results(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Results(ResultConstant constant) {
        this.code = constant.code;
        this.message = constant.message;
    }

    /**
     * @Description 成功默认响应
     * @param
     * @return com.entrusts.module.dto.result.Results
     */
    public static Results ok () {
        return new Results(ResultConstant.SUCCESS );
    }

    /**
     * @Description 添加响应结果
     * @param key
     * @param value
     * @return com.entrusts.module.dto.result.Results
     */
    public Results putData(String key,Object value){
        if (data ==null){
            data = new JSONObject();
        }
        data.put(key,value);
        return this;
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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    //示例
    public static void main(String[] args) {
        Results results1 = Results.ok();
        System.out.println(JSON.toJSONString(results1));

        List<String> ll = new ArrayList<>();
        ll.add("1");
        ll.add("2");
        Results results12 = Results.ok().putData("list", ll).putData("pageSize",5);
        System.out.println(JSON.toJSONString(results12));
    }

}
