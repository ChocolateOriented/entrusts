package com.entrusts.module.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CliRanking implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userCode;
	private String userName;
	private Integer invitationCount;
	private Integer rebateCount;
	private Date date;
	private String currency;
	private BigDecimal amount;
	private Integer globalRecord;
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getInvitationCount() {
		return invitationCount;
	}
	public void setInvitationCount(Integer invitationCount) {
		this.invitationCount = invitationCount;
	}
	public Integer getRebateCount() {
		return rebateCount;
	}
	public void setRebateCount(Integer rebateCount) {
		this.rebateCount = rebateCount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getGlobalRecord() {
		return globalRecord;
	}
	public void setGlobalRecord(Integer globalRecord) {
		this.globalRecord = globalRecord;
	}


}
