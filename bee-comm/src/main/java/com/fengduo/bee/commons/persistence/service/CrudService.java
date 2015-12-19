/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.persistence.service;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;

/**
 * 对于service类比较单一的情况,可以实现当前接口,如:只对应一张表结构
 * 
 * <pre>
 * 对于自定义的情况可以不继承当前类;
 * 这里的泛型<T>为实体对象;
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:22:54 PM
 */
@SuppressWarnings("unchecked")
public interface CrudService<T> {

    T find(Parameter q);

    List<T> list(Parameter q);

    PaginationList<T> listPagination(Parameter q, IPageUrl... i);

    T getById(Long id);

    Long add(T... t);

    boolean update(T t);

    boolean delete(Long id);

    boolean realDelete(Long id);
}
