package com.stu.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stu.domain.BaseEntity;

public abstract class AbstractDAO<E extends BaseEntity> extends BaseDAO<E>{
	
	Logger logger = LoggerFactory.getLogger(AbstractDAO.class);;


	public E saveEntity(E e){
		return this.save(e);
	}
	
	public E updateEntity(E e){
		return this.update(e);
	}
	
	public E getEntity(Class<E> cla,Serializable id){
		return this.get(cla, id);
	}

}