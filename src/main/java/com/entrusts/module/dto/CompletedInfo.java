package com.entrusts.module.dto;

/**
 * Created by cyuan on 2018/2/4.
 */
public class CompletedInfo {
    private Boolean register = false;//是否注册
    private Integer certification ;//是否实名认证
    private Boolean deal =false;//是否有订单
    private Boolean getgift=false;//能否领取
    private String information;//提示信息

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public Integer getCertification() {
        return certification;
    }

    public void setCertification(Integer certification) {
        this.certification = certification;
    }

    public Boolean getDeal() {
        return deal;
    }

    public void setDeal(Boolean deal) {
        this.deal = deal;
    }

    public Boolean getGetgift() {
        return getgift;
    }

    public void setGetgift(Boolean getgift) {
        this.getgift = getgift;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
