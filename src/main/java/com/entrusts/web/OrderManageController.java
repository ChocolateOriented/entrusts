package com.entrusts.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.Page;
import com.entrusts.module.dto.TimePage;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderQuery;
import com.entrusts.service.OrderManageService;

@RestController
@RequestMapping(value = "order")
public class OrderManageController {

	@Autowired
	private OrderManageService orderManageService;

	@RequestMapping(value = "listHistory", method = RequestMethod.GET)
	public CommonResponse<Page<HistoryOrderView>> listHistoryOrder(OrderQuery orderQuery, Integer pageNum, Integer pageSize, HttpServletRequest request) {
		String userCode = request.getParameter("Account-Code");
		orderQuery.setUserCode(userCode);
		CommonResponse<Page<HistoryOrderView>> response = new CommonResponse<>();
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Page<HistoryOrderView> page = orderManageService.findHistoryOrderByPage(orderQuery, pageNum, pageSize);
		response.setData(page);
		return response;
	}
	
	@RequestMapping(value = "listHistoryByCreatedTime", method = RequestMethod.GET)
	public CommonResponse<TimePage<HistoryOrderView>> listHistoryByCreatedTime(OrderQuery orderQuery, Integer limit, HttpServletRequest request) {
		String userCode = request.getParameter("Account-Code");
		orderQuery.setUserCode(userCode);
		CommonResponse<TimePage<HistoryOrderView>> response = new CommonResponse<>();
		if (limit == null) {
			limit = 10;
		}
		TimePage<HistoryOrderView> page = orderManageService.findHistoryOrderByTime(orderQuery, limit);
		response.setData(page);
		return response;
	}

}
