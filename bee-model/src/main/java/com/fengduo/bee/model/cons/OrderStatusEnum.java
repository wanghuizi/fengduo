/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类OrderStatusEnum.java的实现描述：订单状态
 * 
 * @author jie.xu
 * @date 2015年6月17日 下午4:57:26
 */
public enum OrderStatusEnum {

    NON_PAY("未支付", 0),
    //
    SUCCESS_PAY("支付成功", 1),
    //
    FAIL_PAY("支付失败", 2),
    //
    CANCEL_PAY("取消支付", 3),
    //
    SUCCESS_REFUND("退款成功", 4),
    //
    FAIL_REFUND("退款失败", 5),
    //
    OVERDUE_PAY("支付过期", 6),
    //
    WAIT_REFUND("退款待确认", 7);

    private String name;
    private int    value;

    private OrderStatusEnum(String name, int value) {
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

    public static OrderStatusEnum getEnum(Integer value) {
        for (OrderStatusEnum ose : values()) {
            if (ose.getValue() == value) {
                return ose;
            }
        }
        return null;
    }

    public static boolean isNonPay(Integer v) {
        return getEnum(v) == NON_PAY;
    }
}
