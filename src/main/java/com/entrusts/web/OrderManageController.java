package com.entrusts.web;

import javax.servlet.http.HttpServletRequest;

import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.ResultDate;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Order;
import com.entrusts.module.vo.CurrentEntrusts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.Page;
import com.entrusts.module.dto.TimePage;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderQuery;
import com.entrusts.service.OrderManageService;

import java.util.List;

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

	@GetMapping(value = "listCurrent")
	public Object getListCurrent(OrderQuery orderQuery, Integer pageNum, Integer pageSize, HttpServletRequest request){
		String userCode = request.getParameter("Account-Code");
		orderQuery.setUserCode(userCode);
		orderManageService.updateUserCurrentCache(new Order());
		List<CurrentEntrusts> currentEntrusts = orderManageService.findCurrentOrderFromRedis(orderQuery);
		Page<CurrentEntrusts> page = new Page<>();
		if (currentEntrusts.size() == 0){
			page.setEntities(null);
		}else {
			page.setEntities(currentEntrusts.subList((pageNum - 1) * pageSize, pageNum * pageSize));
		}
		page.setTotal((long) currentEntrusts.size());
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		CommonResponse<Page<CurrentEntrusts>> response = new CommonResponse<>();
		response.setData(page);
		return response;
	}

	@GetMapping(value = "listCurrentByCreatedTime")
	public Object getListCurrentByCreatedTime(OrderQuery orderQuery, Integer limit, HttpServletRequest request){
		String userCode = request.getParameter("Account-Code");
		orderQuery.setUserCode(userCode);
		CommonResponse<TimePage<CurrentEntrusts>> response = new CommonResponse<>();
		if (limit == null) {
			limit = 10;
		}
		TimePage<CurrentEntrusts> page = orderManageService.findCurrentOrderByTime(orderQuery, limit);
		response.setData(page);
		return response;
	}

}
