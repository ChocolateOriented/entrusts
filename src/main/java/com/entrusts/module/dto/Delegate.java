package com.entrusts.module.dto;

import com.entrusts.module.enums.OrderMode;
import com.entrusts.module.enums.TradeType;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by jxli on 2018/3/5.
 */
public class Delegate {
	private String userCode;
	private OrderMode orderMode;
	private Date clientTime;

	@NotBlank(message = "请求令牌能为空")
	private String requestToken;
	@NotNull(message = "买卖方向不能为空")
	private TradeType tradeType;
	@NotBlank(message = "基准货币不能为空")
	private String baseCurrency;
	@NotBlank(message = "目标货币不能为空")
	private String targetCurrency;
	@NotNull(message = "价格不能为空")
	@DecimalMin(value = "0.00000001",message = "价格必须大于0")
	private BigDecimal price;
	@NotNull(message = "交易数量不能为空")
	@DecimalMin(value = "0.00000001",message = "交易数量必须大于0")
	private BigDecimal quantity;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public OrderMode getOrderMode() {
		return orderMode;
	}

	public void setOrderMode(OrderMode orderMode) {
		this.orderMode = orderMode;
	}

	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}
