package com.entrusts.module.dto;

import java.io.Serializable;
import java.util.List;

public class TimePage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> entities;

	private Integer limit;

	private Long total;

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
