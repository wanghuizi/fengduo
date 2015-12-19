/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.persistence.mapper;

import java.util.List;

import com.fengduo.bee.commons.persistence.Parameter;

/**
 * Mybatis数据库层CRUD操作的基类接口,对于自定义的情况可以不继承当前类
 * 
 * @author zxc Jun 2, 2015 4:53:54 PM
 */
public interface CrudMapper<T> extends BaseMapper<T> {

    /**
     * 获取单条数据,根据主键id查询
     * 
     * <pre>
     * 可以用listPagination(Parameter query)实现
     * </pre>
     * 
     * @param id
     * @return
     */
    public T getById(Object id);

    /**
     * 获取单条数据,根据条件对象查询
     * 
     * <pre>
     * 可以用listPagination(Parameter query)实现
     * </pre>
     * 
     * @param query
     * @return
     */
    public T find(Parameter query);

    /**
     * 查询数据列表,根据条件对象查询,实现分页
     * 
     * @param query
     * @return
     */
    public List<T> listPagination(Parameter query);

    /**
     * 计算条件查询的总数量
     * 
     * @param query
     * @return
     */
    public int count(Parameter query);

    /**
     * 查询所有数据列表,根据条件对象查询,不分页
     * 
     * <pre>
     * 可以用listPagination(Map<String, ?> query)实现
     * </pre>
     * 
     * @param entity
     * @return
     */
    public List<T> list(Parameter query);

    /**
     * 插入数据
     * 
     * @param entity
     * @return
     */
    public int insert(T entity);

    /**
     * 根据Id更新对象
     * 
     * <pre>
     * 可以用update实现
     * </pre>
     * 
     * @param t
     * @return
     */
    public int updateById(T entity);

    /**
     * 更新数据
     * 
     * @param entity
     * @return
     */
    public int update(T entity);

    /**
     * 删除数据(一般为逻辑删除，更新del_flag字段为1),根据主键id查询
     * 
     * <pre>
     * 可以用updateById实现
     * </pre>
     * 
     * @param id
     * @see public int delete(T entity)
     * @return
     */
    public int deleteById(Object id);

    /**
     * 删除数据(一般为逻辑删除，更新del_flag字段为1),根据条件对象查询
     * 
     * @param entity
     * @return
     */
    public int delete(T entity);
}
