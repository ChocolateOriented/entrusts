package com.entrusts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.entrusts.util.SnowFlake;

public class BaseService {

	@Autowired
	public SnowFlake snowFlake;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public long generateId() {
		return snowFlake.nextId();
	}
}
