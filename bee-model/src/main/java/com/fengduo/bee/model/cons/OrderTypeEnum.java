/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 类OrderTypeEnum.java的实现描述：订单类型
 * 
 * @author jie.xu
 * @date 2015年6月17日 下午4:56:48
 */
public enum OrderTypeEnum {

	//
	CASH_DEPOSIT("保证金", 1),

	//
	CASH_ALL("全额", 2),
	//
	REFUND_DEPOSIT("退款保证金", 3),

	//
	REFUND_ALL("退款全额", 4);

	private String name;
	private int value;

	private OrderTypeEnum(String name, int value) {
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

	public static OrderTypeEnum getEnum(int value) {
		for (OrderTypeEnum ote : values()) {
			if (ote.getValue() == value) {
				return ote;
			}
		}
		return null;
	}

	/**
	 * 订单是否是保证金类型
	 */
	public static boolean isCashDeposit(Integer value) {
		return getEnum(value) == CASH_DEPOSIT;
	}

	/**
	 * 订单是否是全额类型
	 */
	public static boolean isCashAll(Integer value) {
		return getEnum(value) == CASH_ALL;
	}
}
