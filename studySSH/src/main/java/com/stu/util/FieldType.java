package com.stu.util;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 过滤字段类型缩写枚举(Collection类型尚缺)
 * 
 */
public enum FieldType {
	S(String.class), T(Short.class), I(Integer.class), L(Long.class), F(
			Float.class), N(Double.class), D(Date.class), B(Boolean.class), BD(
			BigDecimal.class);

	private Class<?> clazz;

	private FieldType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getValue() {
		return this.clazz;
	}
}