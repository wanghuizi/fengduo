/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类WithdrawTypeEnum.java的实现描述：提现方式
 * 
 * @author jie.xu
 * @date 2015年6月17日 下午5:23:40
 */
public enum WithdrawTypeEnum {

    ONLINE("线上支付", 1), OFFLINE("线下支付", 2);

    private String name;
    private int    value;

    private WithdrawTypeEnum(String name, int value) {
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

    public static WithdrawTypeEnum getEnum(int value) {
        for (WithdrawTypeEnum ote : values()) {
            if (ote.getValue() == value) {
                return ote;
            }
        }
        return null;
    }
}
