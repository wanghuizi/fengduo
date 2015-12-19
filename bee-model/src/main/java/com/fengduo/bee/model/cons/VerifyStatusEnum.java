/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 审核状态枚举（0=未审核，1=正常，2=停止）
 * 
 * @author zxc Jun 10, 2015 3:24:30 PM
 */
public enum VerifyStatusEnum {

	/**
	 * 未审核
	 */
	UNAUDITED(0, "unaudited", "未审核"),
	/**
	 * 正常
	 */
	NORMAL(1, "normal", "正常"),
	/**
	 * 停止
	 */
	STOP(2, "stop", "停止"),
	/**
	 * 黑名单
	 */
	BLACK(3, "black", "黑名单"),
	/**
	 * 产品未发布完全
	 */
	UN_COMPLETELY(4, "un_completely", "产品未发布完全"),

	/**
	 * 审核不通过
	 */
	UN_PASS(5, "un_pass", "审核不通过");

	private int value;
	private String name;
	private String desc;

	private VerifyStatusEnum(int value, String name, String desc) {
		this.value = value;
		this.name = name;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	/**
	 * 根据value获取类型
	 */
	public static VerifyStatusEnum getEnum(int value) {
		for (VerifyStatusEnum current : values()) {
			if (current.value == value) {
				return current;
			}
		}
		return null;
	}

	/**
	 * 是否审核通过
	 */
	public static boolean isNormal(int value) {
		VerifyStatusEnum vse = getEnum(value);
		return NORMAL == vse;
	}
}
