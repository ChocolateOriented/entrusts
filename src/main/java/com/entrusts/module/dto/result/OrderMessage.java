package com.entrusts.module.dto.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyuan on 2018/4/3.
 */
public class OrderMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderCode;
    private String message;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

