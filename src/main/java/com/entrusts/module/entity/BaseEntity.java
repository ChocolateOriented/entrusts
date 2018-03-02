package com.entrusts.module.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
