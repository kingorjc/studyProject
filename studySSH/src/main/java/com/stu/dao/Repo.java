package com.stu.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface Repo<E> {
	/**
	 * 根据实体Id获取实体对象
	 * 
	 * @param eid
	 *            实体ID
	 * @return 实体对象
	 */
	public E getEntity(Serializable eid);

	/**
	 * 根据实体ID加载一个持久态的实体
	 * 
	 * @param eid
	 *            实体ID
	 * @return 实体对象
	 */
	public E loadEntity(Serializable eid);

	/**
	 * 获取所有实体对象
	 * 
	 * @return 实体对象列表
	 */
	public List<E> getAllEntity() ;

	/**
	 * 获取所有实体对象,并根据sortConditions指定的排序条件进行排序，key - 用于排序的属性名称，value -
	 * 排序条件，asc：升序，desc：降序
	 * 
	 * @return 实体对象列表
	 */
	public List<E> getAllEntity(Map<String, String> sortConditions);

	/**
	 * 保存实体对象
	 * 
	 * @param e
	 *            实体对象
	 */
	public Serializable saveEntity(E e);

	/**
	 * 更新实体对象
	 * 
	 * @param e
	 *            实体对象
	 * @return @see org.hibernate.Session.update(Object object)
	 */
	public E updateEntity(E e);

	/**
	 * 新增或修改实体对象
	 * 
	 * @param e
	 * @return @see org.hibernate.Session.saveOrUpdate(Object object)
	 */
	public E saveOrUpdateEntity(E e);

	/**
	 * 删除实体对象
	 * 
	 * @param e
	 *            实体对象
	 * @return
	 */
	public void deleteEntity(E e);

	/**
	 * 逻辑删除实体对象
	 * 
	 * @param e
	 *            实体对象
	 * @return
	 */
	public void logicDeleteEntity(E e);

	/**
	 * 根据实体ID删除实体对象
	 * 
	 * @param eid
	 *            实体ID
	 */
	public void deleteEntity(Serializable eid);

	/**
	 * 根据实体ID逻辑删除实体对象
	 * 
	 * @param eid
	 *            实体ID
	 */
	public void loginDeleteEntity(Serializable eid);

	/**
	 * 根据实体ID批量删除实体对象
	 * 
	 * @param eids
	 *            实体ID集合
	 */
	public void deleteEntity(Serializable[] eids);

	/**
	 * 根据实体ID批量逻辑删除实体对象
	 * 
	 * @param eids
	 *            实体ID集合
	 */
	public void loginDeleteEntity(Serializable[] eids);

	/**
	 * 批量删除实体对象
	 * 
	 * @param es
	 *            实体集合
	 */
	public void deleteEntity(List<E> es);

	/**
	 * 批量逻辑删除实体对象
	 * 
	 * @param es
	 *            实体集合
	 */
	public void logicDeleteEntity(List<E> es);

	/**
	 * 获取所有实体对象数量
	 * 
	 * @return 实体对象数量
	 */
	public Long countAllEntity();

	}