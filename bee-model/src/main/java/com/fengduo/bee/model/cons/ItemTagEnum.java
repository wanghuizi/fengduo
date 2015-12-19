/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

import org.apache.commons.lang.StringUtils;

/**
 * 产品标签枚举
 * 
 * <pre>
 * 电子商务
 * 移动互联网
 * 信息技术
 * 游戏
 * 教育
 * 金融
 * 旅游
 * 社交
 * 娱乐
 * 硬件
 * 能源
 * 医疗健康
 * 餐饮
 * 企业平台
 * 汽车数据
 * 房产酒店
 * 文化艺术
 * 体育运动
 * 生物科学
 * 媒体资讯
 * 广告营销
 * 节能环保
 * 消费生活
 * 工具软件
 * 咨询服务
 * 智能设备
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:29:57 PM
 */
public enum ItemTagEnum {

	//
	E_COMMERCE(1, "e_commerce", "电子商务"),
	//
	MOBILE_INTERNET(2, "mobile_internet", "移动互联网"),
	//
	INFORMATION_TECHNOLOGY(3, "information_technology", "信息技术"),
	//
	GAME(4, "game", "游戏"),
	//
	EDUCATION(5, "education", "教育"),
	//
	FINANCIAL(6, "financial", "金融"),
	//
	TRAVEL(7, "travel", "旅游"),
	//
	SOCIALLY(8, "socially", "社交"),
	//
	ENTERTAINMENT(9, "entertainment", "娱乐"),
	//
	HARDWARE(10, "hardware", "硬件"),
	//
	ENERGY(11, "energy", "能源"),
	//
	HEALTHCARE(12, "healthcare", "医疗健康"),
	//
	ENTERPRISE_PLATFORM(13, "enterprise_platform", "企业平台"),
	//
	AUTOMOTIVE_DATA(14, "automotive_data", "汽车数据"),
	//
	PROPERTY_HOTEL(15, "property_hotel", "房产酒店"),
	//
	CULTURE_ART(16, "culture_art", "文化艺术"),
	//
	SPORTS(17, "sports", "体育运动"),
	//
	BIOLOGICAL_SCIENCES(18, "biological_sciences", "生物科学"),
	//
	MEDIA_INFORMATION(19, "media_information", "媒体资讯"),
	//
	ADVERTISING_MARKETING(20, "advertising_marketing", "广告营销"),
	//
	ENERGY_SAVING(22, "energy_saving", "节能环保"),
	//
	CONSUMER_LIFE(23, "consumer_life", "消费生活"),
	//
	TOOL_SOFTWARE(24, "tool_software", "工具软件"),
	//
	CONSULTING_SERVICE(25, "consulting_service", "咨询服务"),
	//
	INTELLIGENT_DEVICE(26, "intelligent_device", "智能设备");

	public int value;

	public String name;

	public String desc;

	private ItemTagEnum(int value, String name, String desc) {
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

	public static ItemTagEnum getAction(String value) {
		for (ItemTagEnum state : values()) {
			if (StringUtils.isNotEmpty(value)
					&& StringUtils.equals(value, state.getValue() + ""))
				return state;
		}
		return MOBILE_INTERNET;
	}
}
