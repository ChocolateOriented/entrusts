package com.entrusts.util;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;

/**
 * Created by jxli on 2017/10/18.
 */
public class JsonUtil {
	/**
	 * 用于fastjson解析使用蛇形命名规则的Json(使用_分隔), 到Java对象(不区分大小写)
	 * 如{overdue_amount:10}
	 */
	public static final ParserConfig PARSER_SNAKE_NAMING = new ParserConfig();

	static {
		PARSER_SNAKE_NAMING.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase ;
	}

}
