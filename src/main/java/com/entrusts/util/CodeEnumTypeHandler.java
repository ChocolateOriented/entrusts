package com.entrusts.util;

import com.entrusts.module.enums.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Iterator;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by jxli on 2018/3/14.
 */
@MappedTypes(value = {DelegateEventstatus.class, OrderMode.class, OrderStatus.class, TradeType.class})
public class CodeEnumTypeHandler<E extends Enum<E> & BaseCodeEnum> extends BaseTypeHandler<E> {
	private Class<E> type;

	public CodeEnumTypeHandler(Class<E> type) {
		if(type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		} else {
			this.type = type;
		}
	}

	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if(jdbcType == null) {
			ps.setInt(i, parameter.getValue());
		} else {
			ps.setObject(i, parameter.getValue(), jdbcType.TYPE_CODE);
		}
	}

	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		return this.getCodeEnumByValue(value);
	}

	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		return this.getCodeEnumByValue(value);
	}

	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
		return this.getCodeEnumByValue(value);
	}

	//通过value获取对应的Enum
	private E getCodeEnumByValue(int value){
		Iterator<E> iterator = EnumSet.allOf(this.type).iterator();
		while (iterator.hasNext()){
			E e = iterator.next();
			if (e.getValue() == value){
				return e;
			}
		}
		return null;
	}

}
