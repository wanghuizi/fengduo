/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.persistence.service.impl;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.commons.persistence.service.CrudService;

/**
 * 对单一service而言,可以继承当前类,并传入泛型<T>,如:只对应一张表结构
 * 
 * <pre>
 * 对于自定义的情况可以不继承当前类;
 * 这里的泛型<T>为实体对象;
 * </pre>
 * 
 * @author zxc Jul 6, 2015 2:43:42 PM
 */
public class CrudServiceImpl<T> extends BaseService implements CrudService<T> {

    @Override
    public T find(Parameter q) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> list(Parameter q) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginationList<T> listPagination(Parameter q, IPageUrl... i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T getById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Long add(T... t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean update(T t) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean realDelete(Long id) {
        // TODO Auto-generated method stub
        return false;
    }
}
