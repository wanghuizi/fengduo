/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类SubTypeEnum.java的实现描述：认购类型： 基石投资，跟投
 * 
 * @author jie.xu
 * @date 2015年6月18日 下午10:19:34
 */
public enum SubUserTypeEnum {

    CORNERSTONE_SUB("基石投资人", 1),
    //
    FOLLOW_SUB("跟投人", 2);

    private String name;
    private int    value;

    private SubUserTypeEnum(String name, int value) {
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

    public static SubUserTypeEnum getEnum(Integer value) {
        if (value == null) {
            return null;
        }
        for (SubUserTypeEnum ote : values()) {
            if (ote.getValue() == value) {
                return ote;
            }
        }
        return null;
    }

    public static boolean isCornerstoneSub(Integer v) {
        return getEnum(v) == CORNERSTONE_SUB;
    }

    public static boolean isFollowSub(Integer v) {
        return getEnum(v) == FOLLOW_SUB;
    }
}
