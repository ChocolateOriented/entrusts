package com.entrusts.web;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Created by jxli on 2018/3/8.
 */
public abstract class BaseController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected static final String ACCOUNT_CODE = "Account-Code";//请求头, 当前操作用户的编号
	protected static final String TIMESTAMP = "Timestamp"; //请求头, 时间戳

	/**
	 * @Description 获取字段错误信息
	 * @param bindingResul
	 * @return java.lang.String
	 */
	protected static String getFieldErrorsMessages(BindingResult bindingResul){
		if (!bindingResul.hasErrors()){
			return "";
		}

		StringBuilder errorMsg = new StringBuilder();
		List<FieldError> fieldErrors = bindingResul.getFieldErrors();
		if (fieldErrors == null){
			return "";
		}

		for (int i = 0; i < fieldErrors.size(); i++) {
			if (i > 0){
				errorMsg.append(", ");
			}
			errorMsg.append(fieldErrors.get(i).getDefaultMessage());
		}
		return errorMsg.toString();
	}
}
