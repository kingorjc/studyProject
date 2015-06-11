package com.stu.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


/**
 * Hibernate条件查询排序组装工具
 * 
 * @author 印鲜刚
 * @version 1.0.0
 * @since 1.0.0
 */
public class HQHelper {

	private List<Order> orders = new ArrayList<Order>();
	private List<Criterion> criterions = new ArrayList<Criterion>();
	/**
	 * 表达式操作符
	 */
	private static final String[] expressions = new String[] { ">=", "<=",
			"<>", "=", ">", "<", "like", "is null", "is not null", "is empty",
			"is not empty", "in" };

	/**
	 * 获取表达式操作符两边的字符串
	 * 
	 * @param expression
	 * @return
	 */
	private String[] getCuttingCharsets(String expression) {
		String[] temp = new String[3];
		if (ObjectHelper.isEmpty(expression))
			return null;
		for (String exp : expressions) {
			int index = expression.indexOf(exp);
			if (index > -1) {
				temp[0] = expression.substring(0, index).trim();
				temp[1] = exp.trim();
				temp[2] = expression.substring(index + exp.length(),
						expression.length()).trim();
				return temp;
			}
		}
		return null;
	}

	/**
	 * 添加排序条件
	 * 
	 * @param order
	 */
	public HQHelper addOrder(Order order) {
		orders.add(order);

		return this;
	}

	/**
	 * 组装排序条件
	 * 
	 * @param criteria
	 */
	public void buildOrder(Criteria criteria) {
		for (Order order : orders) {
			criteria.addOrder(order);
		}
		orders.clear();
	}

	/**
	 * 添加查询条件
	 * 
	 * @param criterion
	 */
	public HQHelper addSelect(Criterion criterion) {
		criterions.add(criterion);

		return this;
	}

	/**
	 * 组装查询条件
	 * 
	 * @param criteria
	 */
	public void buildSelect(Criteria criteria) {
		Map<String, Criteria> groupCriteria = new HashMap<String, Criteria>();
		for (Criterion criterion : criterions) {
			String[] transCharater = this.getCuttingCharsets(criterion
					.toString());
			if (ObjectHelper.isEmpty(transCharater))
				return;
			String expressionType = transCharater[0];
			String expressionOperator = transCharater[1];
			String expressionValue = transCharater[2];
			int indexOf = expressionType.indexOf(".");
			if (indexOf > -1) {
				expressionType = expressionType.replace(".", "@");
				int len = expressionType.split("@").length;
				Criteria tempCriteria = null;
				for (int i = 0; i < len - 1; i++) {
					tempCriteria = groupCriteria
							.get(expressionType.split("@")[i]);
					if (ObjectHelper.isEmpty(tempCriteria)) {
						if (i == 0)
							tempCriteria = criteria
									.createCriteria(expressionType.split("@")[i]);
						else
							tempCriteria = groupCriteria.get(
									expressionType.split("@")[i - 1])
									.createCriteria(
											expressionType.split("@")[i]);
					}
					groupCriteria.put(expressionType.split("@")[i],
							tempCriteria);
				}
				String domainProperty = expressionType.split("@")[len - 1];
				tempCriteria = groupCriteria
						.get(expressionType.split("@")[len - 2]);
				if (expressionOperator.equalsIgnoreCase(">=")) {
					tempCriteria.add(Restrictions.ge(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("<=")) {
					tempCriteria.add(Restrictions.le(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("<>")) {
					tempCriteria.add(Restrictions.ne(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("=")) {
					tempCriteria.add(Restrictions.eq(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase(">")) {
					tempCriteria.add(Restrictions.gt(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("<")) {
					tempCriteria.add(Restrictions.lt(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("like")) {
					tempCriteria.add(Restrictions.like(domainProperty,
							expressionValue));
				} else if (expressionOperator.equalsIgnoreCase("is null")) {
					tempCriteria.add(Restrictions.isNull(domainProperty));
				} else if (expressionOperator.equalsIgnoreCase("is not null")) {
					tempCriteria.add(Restrictions.isNotNull(domainProperty));
				} else if (expressionOperator.equalsIgnoreCase("is empty")) {
					tempCriteria.add(Restrictions.isEmpty(domainProperty));
				} else if (expressionOperator.equalsIgnoreCase("is not empty")) {
					tempCriteria.add(Restrictions.isNotEmpty(domainProperty));
				} else if (expressionOperator.equalsIgnoreCase("in")) {
					String inStr = expressionValue.substring(1,
							expressionValue.length() - 1);
					tempCriteria.add(Restrictions.in(domainProperty,
							inStr.split(", ")));
				}
			} else {
				criteria.add(criterion);
			}
		}
		criterions.clear();
	}

	public static String where(String[] wheres) {
		if (wheres != null && wheres.length > 0) {
			StringBuffer where = new StringBuffer();
			where.append(" ");
			where.append("where");
			where.append(" ");
			for (int i = 0; i < wheres.length; i++) {
				if (wheres[i] == null) {
					continue;
				}
				where.append(wheres[i]);

				if (i + 1 < wheres.length) {
					where.append(" ");
					where.append("and");
					where.append(" ");
				}
			}
			return where.toString();
		}
		return "";
	}

	/**
	 * 返回例如userName,password
	 * 
	 * @param columns
	 * @return
	 */
	public String intoColumns(String columns[]) {
		if (columns != null && columns.length > 0) {
			return "(" + split(columns) + ")";
		} else {

			return "*";
		}
	}

	/**
	 * 返回字符串 values(?,?,?....)
	 * 
	 * @param length
	 * @return
	 */
	public String values(String valuesColumn) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" values").append("(").append(valuesColumn)
				.append(")");
		return sqlBuffer.toString();
	}

	/**
	 * 返回?,?,?样子的字符串
	 * 
	 * @param length
	 * @param bracket
	 * @return
	 */
	public String splitParams(int length) {
		StringBuffer sqlBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sqlBuffer.append("?");
			if (i + 1 < length) {
				sqlBuffer.append(",");
			}
		}
		return sqlBuffer.toString();
	}

	public String split(String[] columns) {
		if (columns == null || columns.length == 0) {
			return "";
		}
		StringBuffer sqlBuffer = new StringBuffer();
		for (int i = 0; i < columns.length; i++) {
			sqlBuffer.append(columns[i]);
			if (i + 1 < columns.length) {
				sqlBuffer.append(",");
			}
		}
		return sqlBuffer.toString();
	}

	public String set(String[] set) {
		if (set == null || set.length == 0) {
			return "";
		}
		StringBuffer sqlBuffer = new StringBuffer(" set ");
		sqlBuffer.append(split(set));
		return sqlBuffer.toString();
	}

	public String orderBy(String by, boolean desc) {
		if (by == null) {
			return "";
		}
		return " order by " + by + " " + (desc ? "desc" : "");
	}

	public static void conditions(StringBuffer strbf, Object[] queryParmas,
			List<QueryCondition> conditions) {
		List<QueryCondition> conditionTmp = new ArrayList<QueryCondition>();
		for (QueryCondition c : conditions) {
			if (c.getValue() instanceof Collection) {
				conditionTmp.add(c);
			}
		}
		conditions.removeAll(conditionTmp);
		conditions.addAll(conditionTmp);
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).getRule() == Rule.ALLLIKE) {
				strbf.append(" AND " + conditions.get(i).getName() + " like ?");
				queryParmas[i] = "%" + conditions.get(i).getValue() + "%";
				continue;
			} else if (conditions.get(i).getRule() == Rule.LEFTLIKE) {
				strbf.append(" AND " + conditions.get(i).getName() + " like ?");
				queryParmas[i] = "%" + conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.RIGHTLIKE) {
				strbf.append(" AND " + conditions.get(i).getName() + " like ?");
				queryParmas[i] = conditions.get(i).getValue() + "%";
				continue;
			} else if (conditions.get(i).getRule() == Rule.THAN) {
				strbf.append(" AND " + conditions.get(i).getName() + ">?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.LESSTHAN) {
				strbf.append(" AND " + conditions.get(i).getName() + "<?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.EQUAL) {
				strbf.append(" AND " + conditions.get(i).getName() + "=?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.NOTEQUAL) {
				strbf.append(" AND " + conditions.get(i).getName() + "<>?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.IN) {
				strbf.append(" AND " + conditions.get(i).getName()
						+ " in (:list" + i + ")");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.NOTIN) {
				strbf.append(" AND " + conditions.get(i).getName()
						+ " NOT IN (:list" + i + ")");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.NOTIN_STRING) {
				strbf.append(" AND " + conditions.get(i).getName()
						+ " NOT IN (?)");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.IN_STRING) {
				strbf.append(" AND " + conditions.get(i).getName() + " IN (?)");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else {
				strbf.append(" AND " + conditions.get(i).getName()
						+ " like('%?%')");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			}

		}
	}

	public static void conditions(StringBuffer strbf, Object[] queryParmas,
			String tableName, List<QueryCondition> conditions) {
		List<QueryCondition> conditionTmp = new ArrayList<QueryCondition>();
		for (QueryCondition c : conditions) {
			if (c.getValue() instanceof Collection) {
				conditionTmp.add(c);
			}
		}
		conditions.removeAll(conditionTmp);
		conditions.addAll(conditionTmp);
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).getRule() == Rule.ALLLIKE) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + " like ?");
				queryParmas[i] = "%" + conditions.get(i).getValue() + "%";
				continue;
			} else if (conditions.get(i).getRule() == Rule.LEFTLIKE) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + " like ?");
				queryParmas[i] = "%" + conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.RIGHTLIKE) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + " like ?");
				queryParmas[i] = conditions.get(i).getValue() + "%";
				continue;
			} else if (conditions.get(i).getRule() == Rule.THAN) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + ">?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.LESSTHAN) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + "<?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.EQUAL) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + "=?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.NOTEQUAL) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + "<>?");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.IN) {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + " in (:list" + i + ")");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else if (conditions.get(i).getRule() == Rule.NOTIN) {
				strbf.append(" AND " + conditions.get(i).getName()
						+ " NOT IN (:list" + i + ")");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			} else {
				strbf.append(" AND " + tableName + "."
						+ conditions.get(i).getName() + " like('%?%')");
				queryParmas[i] = conditions.get(i).getValue();
				continue;
			}

		}
	}

	public static StringBuffer preWhere() {
		return new StringBuffer(" WHERE 1=1");
	}
}

