/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 逻辑删除的状态枚举 (0=正常,1=删除)
 * 
 * @author zxc Jun 9, 2015 11:53:03 PM
 */
public enum DelFlagEnum {

	// 未删除,正常
	UN_DELETE(0, "unDelete", "正常"),
	// 已删除
	DELETE(1, "delete", "已删除");

	public int value;

	public String name;

	public String desc;

	private DelFlagEnum(int value, String name, String desc) {
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

	public static DelFlagEnum getAction(int value) {
		for (DelFlagEnum state : values()) {
			if (value == state.getValue())
				return state;
		}
		return null;
	}
}
