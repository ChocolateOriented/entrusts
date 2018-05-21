package com.entrusts.web.third;

import com.entrusts.module.dto.CommonResponse;
import com.entrusts.module.dto.Page;
import com.entrusts.module.dto.result.ResultConstant;
import com.entrusts.module.dto.result.Results;
import com.entrusts.module.entity.Order;
import com.entrusts.module.vo.HistoryOrderView;
import com.entrusts.module.vo.OrderDetailView;
import com.entrusts.module.vo.OrderQuery;
import com.entrusts.service.OrderManageService;
import com.entrusts.util.StringUtils;
import com.entrusts.web.BaseController;
import com.entrusts.web.OrderCancelController;
import com.entrusts.web.OrderManageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jxli
 * @version 2018/5/16
 * @Description 内部接口
 */
@RestController
@RequestMapping("entrusts/third/order")
public class ThirdController extends BaseController {

	@Autowired
	private OrderCancelController cancelController;
	@Autowired
	private OrderManageController manageController;
	@Autowired
	private OrderManageService orderManageService;

	@PostMapping(value = "/cancel")
	public Object cancel(@RequestBody Order orderRequest) {
		return cancelController.cancel(orderRequest, orderRequest.getUserCode());
	}

	@PostMapping("/cancelAll")
	public Object cancelAll(@RequestBody Order order) {
		return cancelController.cancelAll(order.getUserCode());
	}

	@RequestMapping(value = "listHistory", method = RequestMethod.GET)
	public CommonResponse<Page<HistoryOrderView>> listHistoryOrder(OrderQuery orderQuery, Integer pageNum, Integer pageSize, String userCode) {
		return manageController.listHistoryOrder(orderQuery, pageNum, pageSize, userCode);
	}

	@GetMapping(value = "listCurrent")
	public Object getListCurrent(OrderQuery orderQuery, Integer pageNum, Integer pageSize, String userCode) {
		return manageController.getListCurrent(orderQuery, pageNum, pageSize, userCode);
	}

	@GetMapping(value = "detail")
	public Results detail(@RequestParam String orderCode, @RequestParam String userCode) {
		if (StringUtils.isBlank(orderCode) || StringUtils.isBlank(userCode)) {
			return new Results(ResultConstant.EMPTY_PARAM);
		}
		OrderDetailView orderDetailView = orderManageService.findOrderDetail(orderCode, userCode);
		return Results.ok().putData(orderDetailView);
	}
}
