package com.entrusts.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.entrusts.mapper.TradeMapper;

public class TradeService extends BaseService {

	@Autowired
	private TradeMapper tradeMapper;
}
