package com.stu.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

public class QueryCondition {
	private String name;
	private Object value;
	private Rule rule = Rule.ALLLIKE;
	/**
	 * 连接词
	 */
	private String joinWord = "AND";

	/**
	 * 过滤字段类型
	 */
	private Class<?> fieldType = null;
	/**
	 * 多列匹配，如名称和简称两个字段进行匹配查询
	 */
	private boolean multiFilter;
	/**
	 * 多列匹配所使用的列name like 'cyc' or nickname like 'cyc'
	 */
	private String[] names;

	public String getJoinWord() {
		return joinWord;
	}

	public void setJoinWord(String joinWord) {
		this.joinWord = joinWord;
	}

	public QueryCondition() {
	}

	public QueryCondition(String filterName, Object filterValue) {
		if (filterName.indexOf("_") != -1) {
			String condition = StringUtils.substringBefore(filterName, "_");
			String matchTypeCode = condition.substring(0,
					condition.length() - 1);
			String fieldTypeCode = condition.substring(condition.length() - 1,
					condition.length());

			this.rule = ((Rule) Enum.valueOf(Rule.class, matchTypeCode));
			this.fieldType = ((FieldType) Enum.valueOf(FieldType.class,fieldTypeCode)).getValue();

			String fieldNameList = StringUtils.substringAfter(filterName, "_");

			this.names = fieldNameList.split("_OR_");
			if (this.names.length == 1) {
				this.name = this.names[0];
			}
			this.value = ConvertUtils.convert(filterValue.toString(),
					this.fieldType);
			return;
		}
		this.name = filterName;
		this.value = filterValue;
	}

	public QueryCondition(String name, Object value, Rule rule) {
		this.name = name;
		this.value = value;
		this.rule = rule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public boolean isMultiFilter() {
		multiFilter = this.names != null && this.names.length > 1;
		return multiFilter;
	}

	public void setMultiFilter(boolean multiFilter) {
		this.multiFilter = multiFilter;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}
}
