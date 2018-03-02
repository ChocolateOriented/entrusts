package com.entrusts.module.dto.result;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by sxu on 2018/2/28.
 */
public class ResultDate {

    private final JSONObject jsonObject;

    private ResultDate(ResultDate.Builder builder) {
        this.jsonObject = builder.jsonObject;
    }

    public JSONObject value() {
        return this.jsonObject;
    }

    public static class Builder {
        private final JSONObject jsonObject = new JSONObject();

        public Builder() {
        }

        public ResultDate.Builder append(String displayName, Object obj) {
            if(obj instanceof ResultDate) {
                this.jsonObject.put(displayName, ((ResultDate)obj).value());
            } else {
                this.jsonObject.put(displayName, obj);
            }

            return this;
        }

        public ResultDate build() {
            return new ResultDate(this);
        }
    }
}
