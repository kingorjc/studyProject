package com.stu.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.stu.domain.BaseEntity;
import com.stu.util.HQHelper;
import com.stu.util.ObjectHelper;

abstract class BaseDAO<T extends BaseEntity> {
	Logger logger = LoggerFactory.getLogger(BaseDAO.class);

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 创建Criteria对象
	 * 
	 * @param c
	 *            查询主对象Class
	 * @return
	 */
	protected Criteria createCriteria(Class<T> c) {
		return getSession().createCriteria(c);
	}

	/**
	 * 在同一线程获取session
	 * 
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}


	/**
	 * 在不同线程获取session
	 * 
	 * @return
	 */
	public Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 * 添加实体
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected T save(T obj) {
		obj.setCreateTime(new Date());
		obj.setUpdateTime(new Date());
		obj.setIsDeleted(false);
		getSession().save(obj);
		return obj;

	}

	/**
	 * 添加或修改实体
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected T saveOrUpdate(T obj) {
		if (ObjectHelper.isEmpty(obj.getId())) {
			obj.setCreateTime(new Date());
		} else {
			obj.setUpdateTime(new Date());
		}
		obj.setIsDeleted(false);
		getSession().saveOrUpdate(obj);
		getSession().flush();

		return obj;
	}

	/**
	 * 修改实体
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected T update(T obj) {
		obj.setUpdateTime(new Date());
		obj.setIsDeleted(false);
		getSession().update(obj);
		return obj;
	}

	/**
	 * merge来合并两个session中的同一对象
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected Object merge(Object obj) {
		return getSession().merge(obj);
	}

	/**
	 * 通过ID获取实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	protected T get(Class<T> entityClass, Serializable id) {
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 通过实体ID加载实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	protected Object load(Class<T> entityClass, Serializable id) {
		return getSession().load(entityClass, id);
	}

	/**
	 * 删除单实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 * @throws Exception
	 */
	protected boolean delete(Class<T> entityClass, Serializable id) {
		getSession().delete(get(entityClass, id));
		return true;
	}

	/**
	 * 逻辑删除单实体
	 */
	protected boolean logicDelete(Class<T> entityClass, Serializable id) {
		logicDelete(get(entityClass, id));
		return true;
	}

	/**
	 * 删除单实体
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	protected void delete(Object obj) {
		getSession().delete(obj);
	}

	/**
	 * 逻辑删除单实体
	 * 
	 * @param obj
	 */
	protected void logicDelete(T obj) {
		obj.setDeleteTime(new Date());
		obj.setIsDeleted(true);
		getSession().update(obj);
	}

	/**
	 * 删除多实体
	 * 
	 * @param c
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	protected boolean delete(Class<T> entityClass, Serializable[] ids) {
		for (Serializable id : ids) {
			delete(entityClass, id);
		}
		return true;

	}

	/**
	 * 逻辑删除多实体
	 * 
	 * @param entityClass
	 * @param ids
	 * @return
	 */
	protected boolean logicDelete(Class<T> entityClass, Serializable[] ids) {
		for (Serializable id : ids) {
			logicDelete(entityClass, id);
		}
		return true;

	}

	/**
	 * 获取总记录数
	 * 
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Long countAll(Class entityClass) {
		return countAll(entityClass, null);
	}

	/**
	 * 获取所有实体
	 * 
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	protected List<T> findAll(Class<T> entityClass) {
		return findAll(entityClass, null, -1, -1);
	}

	/**
	 * 获取分页实体
	 * 
	 * @param entityClass
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected List<T> findAll(Class entityClass, int currentPage, int pageSize) {
		return findAll(entityClass, null, currentPage, pageSize);
	}

	/**
	 * 获取总记录数
	 * 
	 * @param entityClass
	 * @param conditionQuery
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Long countAll(Class entityClass, HQHelper hibernateUtil) {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.add(Restrictions.or(Restrictions.eq("isDeleted", false), Restrictions.isNull("isDeleted")));
		if (!ObjectHelper.isEmpty(hibernateUtil)) {
			hibernateUtil.buildSelect(criteria);
		}
		criteria.setProjection(Projections.rowCount());

		Long total = (Long) criteria.uniqueResult();
		if (!ObjectHelper.isEmpty(total))
			return total;
		return 0l;
	}

	/**
	 * 获取所有实体
	 * 
	 * @param entityClass
	 * @param conditionQuery
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected List<T> findAll(Class entityClass, HQHelper conditionQuery) {
		return findAll(entityClass, conditionQuery, -1, -1);
	}

	/**
	 * 获取分页实体
	 * 
	 * @param entityClass
	 * @param conditionQuery
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<T> findAll(Class entityClass, HQHelper conditionQuery, int currentPage, int pageSize) {
		Criteria criteria = getSession().createCriteria(entityClass);

		criteria.add(Restrictions.or(Restrictions.eq("isDeleted", false), Restrictions.isNull("isDeleted")));

		if (!ObjectHelper.isEmpty(conditionQuery)) {
			conditionQuery.buildSelect(criteria);
			conditionQuery.buildOrder(criteria);
		}

		if (currentPage > 0 && pageSize > 0) {
			criteria.setFirstResult((currentPage - 1) * pageSize);
			criteria.setMaxResults(pageSize);
		}

		return criteria.list();

	}

}
