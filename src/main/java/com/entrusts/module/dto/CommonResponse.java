package com.entrusts.module.dto;

public class CommonResponse<T> extends BaseResponse {

	private static final long serialVersionUID = 1L;

	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public CommonResponse() {
		super();
	}

	public CommonResponse(T data) {
		super();
		this.data = data;
	}

}
