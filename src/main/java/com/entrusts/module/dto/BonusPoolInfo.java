package com.entrusts.module.dto;

import java.util.Date;

/**
 * Created by sxu on 2018/2/4.
 */
public class BonusPoolInfo {

    private int remainingBonus;

    private int rate;

    private Date date;

    private String currency;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRemainingBonus() {
        return remainingBonus;
    }

    public void setRemainingBonus(int remainingBonus) {
        this.remainingBonus = remainingBonus;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
