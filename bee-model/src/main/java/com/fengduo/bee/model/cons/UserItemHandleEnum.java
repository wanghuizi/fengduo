/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类UserItemEnum.java的实现描述：用户和项目关联操作类型
 * 
 * @author jie.xu
 * @date 2015年6月21日 下午8:05:29
 */
public enum UserItemHandleEnum {

    //
    MY_LAUNCH("我发起的", 1),
    //
    MY_INVESTMENT("我投资的", 2),
    //
    MY_FOLLOW("我关注的", 3);

    private String name;
    private int    value;

    private UserItemHandleEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
