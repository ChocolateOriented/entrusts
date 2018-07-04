package com.entrusts.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrusts.mapper.OrderMapper;
import com.entrusts.module.dto.DealNotify.OrderDealDetail;
import com.entrusts.module.dto.TradePairQuantity;
import com.entrusts.module.entity.Order;
import com.entrusts.module.enums.OrderStatus;
import com.entrusts.module.enums.TradeType;
import com.entrusts.util.RedisUtil;
import com.entrusts.util.StringUtils;

import redis.clients.jedis.Jedis;

@Service
public class MarketService extends BaseService {

	@Autowired
	private OrderMapper orderMapper;

	private static final int QUANTITY_SCALE = 8;

	private static final BigDecimal SCALE_FACTOR = BigDecimal.valueOf(Math.pow(10, QUANTITY_SCALE));

	private static final String userTotalQuantityKey = "MarketService.userTotalQuantity.";

	/**
	 * 查询用户挂单总量
	 */
	public TradePairQuantity queryDelegateTotalQuantity(Integer tradePairId) {
		String buyKey = userTotalQuantityKey + TradeType.buy;
		String sellKey = userTotalQuantityKey + TradeType.sell;
		String field = String.valueOf(tradePairId);
		Jedis jedis = null;
		TradePairQuantity tradePairQuantity = null;
		try {
			jedis = RedisUtil.getResource();
			String buyQuantity = jedis.hget(buyKey, field);
			String sellQuantity = jedis.hget(sellKey, field);
			
			if (StringUtils.isEmpty(buyQuantity) || StringUtils.isEmpty(sellQuantity)) {
				logger.info("没有获取到交易对{}挂单总量缓存", tradePairId);
				tradePairQuantity = orderMapper.queryDelegateTotalQuantity(tradePairId);
				if (tradePairQuantity == null) {
					tradePairQuantity = new TradePairQuantity();
				}
				jedis.hincrBy(buyKey, field, tradePairQuantity.getBuyQuantity().multiply(SCALE_FACTOR).setScale(0).longValue());
				jedis.hincrBy(sellKey, field, tradePairQuantity.getSellQuantity().multiply(SCALE_FACTOR).setScale(0).longValue());
			} else {
				logger.info("交易对{}挂单总量获取缓存", tradePairId);
				tradePairQuantity = new TradePairQuantity();
				tradePairQuantity.setTradePairId(tradePairId);
				tradePairQuantity.setBuyQuantity(new BigDecimal(buyQuantity).divide(SCALE_FACTOR));
				tradePairQuantity.setSellQuantity(new BigDecimal(sellQuantity).divide(SCALE_FACTOR));
			}
		} catch (Exception e) {
			logger.info("获取交易对挂单数量失败：", e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
		return tradePairQuantity;
	}

	/**
	 * 更新用户挂单总量
	 * @param order
	 */
	public void updateDelegateTotalQuantity(Order order) {
		String key = userTotalQuantityKey + order.getTradeType();
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			if (!jedis.hexists(key, String.valueOf(order.getTradePairId()))) {
				return;
			}
			BigDecimal updateNum = BigDecimal.ZERO;
			if (order.getStatus() == OrderStatus.TRADING) {
				updateNum = order.getQuantity();
			} else if (order.getStatus() == OrderStatus.WITHDRAW || order.getStatus() == OrderStatus.WITHDRAW_UNTHAWING) {
				if (order.getDealQuantity() == null) {
					updateNum = order.getQuantity().negate();
				} else {
					updateNum = order.getQuantity().subtract(order.getDealQuantity()).negate();
				}
			} else {
				return;
			}
			long increment = updateNum.multiply(SCALE_FACTOR).setScale(0).longValue();
			Long newValue = jedis.hincrBy(key, String.valueOf(order.getTradePairId()), increment);
			logger.debug("交易对{}挂单总量,变更{},变更为{}", order.getTradePairId(), increment, newValue);
		} catch (Exception e) {
			logger.info("更新交易对挂单数量失败：", e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	/**
	 * 成交时更新用户挂单总量
	 * @param orderDetail
     * @param order
	 */
	public void updateDelegateTotalQuantityWhenDeal(OrderDealDetail orderDetail, Order order) {
		String key = userTotalQuantityKey + order.getTradeType();
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getResource();
			if (!jedis.hexists(key, String.valueOf(order.getTradePairId()))) {
				return;
			}
			BigDecimal updateNum = orderDetail.getDealQuantity().negate();
			
			long increment = updateNum.multiply(SCALE_FACTOR).setScale(0).longValue();
			Long newValue = jedis.hincrBy(key, String.valueOf(order.getTradePairId()), increment);
			logger.debug("交易对{}挂单总量,变更{},变更为{}", order.getTradePairId(), increment, newValue);
		} catch (Exception e) {
			logger.info("成交更新交易对挂单数量失败：", e);
		} finally {
			RedisUtil.returnResource(jedis);
		}
	}

	/**
	 * 更新用户挂单总量
	 * @param orders
	 */
	public void updateDelegateTotalQuantity(List<Order> orders) {
		for (Order order : orders) {
			updateDelegateTotalQuantity(order);
		}
	}
}
