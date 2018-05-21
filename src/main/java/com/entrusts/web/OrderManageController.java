package com.entrusts.web;

import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.Page;
import com.entrusts.module.dto.TimePage;
import com.entrusts.module.vo.CurrentEntrusts;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderQuery;
import com.entrusts.service.OrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "entrusts/order")
public class OrderManageController extends BaseController {

	@Autowired
	private OrderManageService orderManageService;

	@RequestMapping(value = "listHistory", method = RequestMethod.GET)
	public CommonResponse<Page<HistoryOrderView>> listHistoryOrder(OrderQuery orderQuery, Integer pageNum, Integer pageSize, @RequestHeader(ACCOUNT_CODE) String userCode) {
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
	public CommonResponse<TimePage<HistoryOrderView>> listHistoryByCreatedTime(OrderQuery orderQuery, Integer limit, @RequestHeader(ACCOUNT_CODE) String userCode) {
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
	public Object getListCurrent(OrderQuery orderQuery, Integer pageNum, Integer pageSize, @RequestHeader(ACCOUNT_CODE) String userCode){
		orderQuery.setUserCode(userCode);
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Page<CurrentEntrusts> page = orderManageService.findCurrentOrder(orderQuery, pageNum, pageSize);
		CommonResponse<Page<CurrentEntrusts>> response = new CommonResponse<>();
		response.setData(page);
		return response;
	}

	@GetMapping(value = "listCurrentByCreatedTime")
	public Object getListCurrentByCreatedTime(OrderQuery orderQuery, Integer limit, @RequestHeader(ACCOUNT_CODE) String userCode){
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
