/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.cons;

/**
 * 数据库排序规则
 * 
 * @author zxc Jul 6, 2015 3:31:05 PM
 */
public enum DBSortTypeEnum {

    // gmt_create
    GMT_MODIFIED(1, "GMT_MODIFIED"),
    // gmt_create
    GMT_CREATE(2, "GMT_CREATE"),
    // recommend
    ID(3, "id");

    public int    value;

    public String name;

    private DBSortTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static DBSortTypeEnum getAction(int value) {
        for (DBSortTypeEnum state : values()) {
            if (value == state.getValue()) return state;
        }
        return null;
    }
}
