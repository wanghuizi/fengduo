/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.persistence.service;

import java.util.List;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.lang.ArrayUtils;
import com.fengduo.bee.commons.core.utils.Reflections;
import com.fengduo.bee.commons.pagination.Pagination;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;

/**
 * 抽象的service层基础类,实现了分页方法
 * 
 * @author zxc Jun 9, 2015 1:22:54 PM
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseService {

    /**
     * 封装默认的实际的分页方法
     * 
     * @param q
     * @param dao
     * @param countMethodName
     * @param listMethodName
     * @param i
     * @return
     */
    public PaginationList pagination(Parameter q, final Object dao, IPageUrl... i) {

        return pagination(q, dao, "count", "listPagination", i);
    }

    /**
     * 封装实际的分页方法
     * 
     * @param q
     * @param dao
     * @param countMethodName
     * @param listMethodName
     * @param i
     * @return
     */
    public PaginationList pagination(Parameter q, final Object dao, final String countMethodName,
                                     final String listMethodName, IPageUrl... i) {
        // q.put("sort", "");排序规则
        // 设置分页每页大小
        q.setNowPageIndex(0);
        if (q.get("pageSize") != null) {
            q.setPageSize((int) q.get("pageSize"));
        }
        if (q.get("page") != null) {
            q.setNowPageIndex(Argument.isNotPositive((Integer) q.get("page")) ? 0 : ((int) q.get("page") - 1));
        }

        PaginationList paginationList = new PaginationList(q);

        // List<Parameter> mapList = Lists.newArrayList();
        // mapList.add(q);

        Integer count = (Integer) Reflections.invokeMethodByName(dao, countMethodName, new Parameter[] { q });
        int totalCount = (count == null) ? 0 : count;
        q.init(totalCount);
        q.put("pageSize", q.getPageSize());
        q.put("startRecordIndex", q.getStartRecordIndex());

        if (totalCount > 0) {
            List items = (List) Reflections.invokeMethodByName(dao, listMethodName, new Parameter[] { q });
            if (items != null) {
                paginationList.addAll(items);
            }
        }
        return parsePages(q, paginationList, i);
    }

    /**
     * 封装自定义URL的分页方法
     * 
     * @param p
     * @param paginationList
     * @param iPages
     * @return
     */
    public PaginationList parsePages(Pagination p, PaginationList paginationList, IPageUrl... iPages) {
        ArrayUtils.removeNullElement(iPages);
        if (Argument.isEmptyArray(iPages)) {
            return paginationList;
        }
        if (p instanceof Pagination) {
            paginationList.setQuery(PaginationParser.getPaginationList(((Pagination) p).getNowPageIndex(),
                                                                       ((Pagination) p).getPageSize(),
                                                                       ((Pagination) p).getAllRecordNum(), iPages[0]));
        }
        return paginationList;
    }
}
