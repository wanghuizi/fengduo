/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

/**
 * 所属阶段枚举
 * 
 * <pre>
 *      概念阶段
 *      研发中
 *      产品已发布
 *      产品已盈利
 *      其他
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:28:30 PM
 */
public enum StageEnum {

	//
	CONCEPT_PHASE(0, "concept_phase", "概念阶段"),
	//
	RESEARCH_IN(100, "research_in", "研发中"),
	//
	PRODUCTS_RELEASED(200, "products_released", "产品已发布"),
	//
	PRODUCTS_PROFITABLE(300, "products_profitable", "产品已盈利"),
	//
	OTHER(400, "other", "其他");

	public int value;

	public String name;

	public String desc;

	private StageEnum(int value, String name, String desc) {
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

	public static StageEnum getAction(int value) {
		for (StageEnum state : values()) {
			if (value == state.getValue())
				return state;
		}
		return null;
	}
}
