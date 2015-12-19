/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类SubHandleStatusEnum.java的实现描述：1=保证金尚未支付,2=保证金已经支付成功,3=关闭，4=全额付款未支付，5=全额付款成功
 * 
 * @author jie.xu
 * @date 2015年6月22日 下午4:56:17
 */
public enum SubHandleStatusEnum {

    //
    DEPOSIT_NOT_PAY("保证金尚未支付", 1),
    //
    DEPOSIT_SUCCESS_PAY("保证金支付成功", 2),
    //
    CLOSE("取消", 3),
    //
    ALL_NOT_PAY("全额付款未支付", 4),
    //
    ALL_SUCCESS_PAY("全额付款成功", 5);

    private String name;
    private int    value;

    private SubHandleStatusEnum(String name, int value) {
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

    public static SubHandleStatusEnum getEnum(Integer v) {
        if (v == null) {
            return null;
        }
        for (SubHandleStatusEnum se : values()) {
            if (se.getValue() == v) {
                return se;
            }
        }
        return null;
    }

    public static boolean isDepositNotPay(Integer v) {
        return getEnum(v) == DEPOSIT_NOT_PAY;
    }

    public static boolean isDepositSuccessPay(Integer v) {
        return getEnum(v) == DEPOSIT_SUCCESS_PAY;
    }

    public static boolean isClose(Integer v) {
        return getEnum(v) == CLOSE;
    }

    public static boolean isAllNotPay(Integer v) {
        return getEnum(v) == ALL_NOT_PAY;
    }

    public static boolean isAllSuccessPay(Integer v) {
        return getEnum(v) == ALL_SUCCESS_PAY;
    }
}
