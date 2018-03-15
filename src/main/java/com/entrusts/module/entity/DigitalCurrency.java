package com.entrusts.module.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DigitalCurrency {
    private Integer id;

    private String name;

    private String alias;

    private String unit;

    private String icon;

    private String link;

    private BigDecimal serviceChargeRate;

    private BigDecimal transferAccountChargeRate;

    private BigDecimal blockchainChargeRate;

    private BigDecimal minTradeVolume;

    private Byte isDeleted;

    private Byte isSupported;

    private Short minTradeLimitTime;

    private Short maxTradeLimitTime;

    private Integer createdId;

    private Integer updatedId;

    private Date createdTime;

    private Date updatedTime;

    private Date deletedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BigDecimal getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(BigDecimal serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    public BigDecimal getTransferAccountChargeRate() {
        return transferAccountChargeRate;
    }

    public void setTransferAccountChargeRate(BigDecimal transferAccountChargeRate) {
        this.transferAccountChargeRate = transferAccountChargeRate;
    }

    public BigDecimal getBlockchainChargeRate() {
        return blockchainChargeRate;
    }

    public void setBlockchainChargeRate(BigDecimal blockchainChargeRate) {
        this.blockchainChargeRate = blockchainChargeRate;
    }

    public BigDecimal getMinTradeVolume() {
        return minTradeVolume;
    }

    public void setMinTradeVolume(BigDecimal minTradeVolume) {
        this.minTradeVolume = minTradeVolume;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getIsSupported() {
        return isSupported;
    }

    public void setIsSupported(Byte isSupported) {
        this.isSupported = isSupported;
    }

    public Short getMinTradeLimitTime() {
        return minTradeLimitTime;
    }

    public void setMinTradeLimitTime(Short minTradeLimitTime) {
        this.minTradeLimitTime = minTradeLimitTime;
    }

    public Short getMaxTradeLimitTime() {
        return maxTradeLimitTime;
    }

    public void setMaxTradeLimitTime(Short maxTradeLimitTime) {
        this.maxTradeLimitTime = maxTradeLimitTime;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public Integer getUpdatedId() {
        return updatedId;
    }

    public void setUpdatedId(Integer updatedId) {
        this.updatedId = updatedId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }
}