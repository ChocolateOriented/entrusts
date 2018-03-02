package com.entrusts.module.dto;

public class Page<T>{

	private static final long serialVersionUID = 1L;
	private T list;
	private Integer pageNum ;
	private Integer pageSize ;
	private Long total ;

	public T getList() {
		return list;
	}

	public void setList(T list) {
		this.list = list;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	
}
