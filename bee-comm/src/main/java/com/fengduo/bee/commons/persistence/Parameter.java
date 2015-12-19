/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.persistence;

import com.fengduo.bee.commons.cons.DBSortTypeEnum;
import com.fengduo.bee.commons.pagination.Pagination;

/**
 * 查询参数类
 * 
 * @author zxc Jun 2, 2015 4:56:30 PM
 */
public class Parameter extends Pagination {

    private static final long serialVersionUID = 413477673531097538L;

    // 数据库排序字段
    private String            sort             = DBSortTypeEnum.GMT_CREATE.getName();

    public static Parameter newParameter() {
        return new Parameter();
    }

    /**
     * 构造类，例：new Parameter(id, parentIds)
     * 
     * @param values 参数值
     */
    public Parameter(Object... values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                put("p" + (i + 1), values[i]);
            }
        }
    }

    /**
     * 构造类，例：new Parameter(new Object[][]{{"id", id}, {"parentIds", parentIds}})
     * 
     * @param parameters 参数二维数组
     */
    public Parameter(Object[][] parameters) {
        if (parameters != null) {
            for (Object[] os : parameters) {
                if (os.length == 2) {
                    put((String) os[0], os[1]);
                }
            }
        }
    }

    /**
     * Put方法,返回被修改的类
     * 
     * @param key
     * @param value
     * @return
     */
    public Parameter pu(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
