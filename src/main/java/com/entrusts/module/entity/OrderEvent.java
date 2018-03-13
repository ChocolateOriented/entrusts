package com.entrusts.module.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.entrusts.module.enums.DelegateEventstatus;
import com.entrusts.module.enums.OrderStatus;


public class OrderEvent {
    private Long id;

    private Long orderCode;

    private OrderStatus status;

    private BigDecimal dealAmout;

    private BigDecimal dealQuantity;

    private Date createdTime;

    private Date lastedDealTime;

    private String remark;
    
    private DelegateEventstatus delegateEventstatus; // 队列执行状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getDealAmout() {
        return dealAmout;
    }

    public void setDealAmout(BigDecimal dealAmout) {
        this.dealAmout = dealAmout;
    }

    public BigDecimal getDealQuantity() {
        return dealQuantity;
    }

    public void setDealQuantity(BigDecimal dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastedDealTime() {
        return lastedDealTime;
    }

    public void setLastedDealTime(Date lastedDealTime) {
        this.lastedDealTime = lastedDealTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public DelegateEventstatus getDelegateEventstatus() {
		return delegateEventstatus;
	}

	public void setDelegateEventstatus(DelegateEventstatus delegateEventstatus) {
		this.delegateEventstatus = delegateEventstatus;
	}

    
    
}