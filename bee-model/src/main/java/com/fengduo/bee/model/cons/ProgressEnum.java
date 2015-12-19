/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 众筹进展枚举(预约中,发行中,缴款中,已成功,众筹失败)
 * 
 * @author zxc Jun 12, 2015 3:42:45 PM
 */
public enum ProgressEnum {

    // 尚未开始
    UNSTART(0, "unstart", "尚未开始", 0, 0),
    // 预热中
    RESERVATION(1, "reservation", "预热中", 7, 7),
    // 基石投资者进入期
    CORNERSTONE(2, "cornerstone", "基石投资进入期", 14, 21),
    // 预售期
    PRESELL(3, "presell", "预售期", 60, 81),
    // 付款期
    PAYING(4, "paying", "付款期", 3, 84),
    // 交割期
    TICKET(5, "ticket", "交割期", 14, 98),
    // 已成功
    SUCCESS(6, "success", "众筹成功", 0, 0),
    // 众筹失败
    FAILURE(7, "failure", "众筹失败", 0, 0);

    public int    value;

    public String name;

    public String desc;

    private int   days;        // 间隔天数

    private int   intervalDays; // 间隔天数,参照审核通过日期

    private ProgressEnum(int value, String name, String desc, int days, int intervalDays) {
        this.value = value;
        this.name = name;
        this.desc = desc;
        this.days = days;
        this.intervalDays = intervalDays;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static ProgressEnum getAction(int value) {
        for (ProgressEnum state : values()) {
            if (value == state.getValue()) return state;
        }
        return null;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 是否是合法的认购期
     */
    public static boolean isLegalSubProgress(int value, Integer subType) {
        ProgressEnum pe = getAction(value);
        if (SubUserTypeEnum.isCornerstoneSub(subType)) {
            if (pe == CORNERSTONE) {
                return true;
            }
        } else if (SubUserTypeEnum.isFollowSub(subType)) {
            if (pe == PRESELL) {
                return true;
            }
        }
        return false;
    }

    public int getIntervalDays() {
        return intervalDays;
    }
}
