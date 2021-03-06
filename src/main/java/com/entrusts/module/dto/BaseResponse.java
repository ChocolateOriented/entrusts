package com.entrusts.module.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code = 0;

	private String message = "ok";

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return code != null && code == 0;
	}
}
