/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 团队人数枚举
 * 
 * <pre>
 *   0~50人
 *   50~100人
 *   100~150人
 *   150~200人
 *   200人以上
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:29:15 PM
 */
public enum TeamCountEnum {

	//
	FIFTY(0, "fifty", "0-50"),
	//
	HUNDRED(100, "hundred", "50-100"),
	//
	HUNDRED_FIFTY(200, "two_hundred", "100-150"),
	//
	TWO_HUNDRED(300, "two_hundred", "150-200"),
	//
	OTHER(400, "other", " &gt;200");

	public int value;

	public String name;

	public String desc;

	private TeamCountEnum(int value, String name, String desc) {
		this.value = value;
		this.name = name;
		this.desc = desc;
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

	public static TeamCountEnum getAction(int value) {
		for (TeamCountEnum state : values()) {
			if (value == state.getValue())
				return state;
		}
		return null;
	}
}
