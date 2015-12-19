/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fengduo.bee.commons.util.StringUtils;
import com.fengduo.bee.model.cons.ItemTagEnum;
import com.fengduo.bee.model.cons.OrderStatusEnum;
import com.fengduo.bee.model.cons.PayTypeEnum;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.StageEnum;
import com.fengduo.bee.model.cons.SubHandleStatusEnum;
import com.fengduo.bee.model.cons.TeamCountEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;

/**
 * 枚举类页面显示效果
 * 
 * @author zxc Jun 12, 2015 2:27:21 PM
 */
public class EnumViewTools {

	@SuppressWarnings("unused")
	private static List<VerifyStatusEnum> verifyStatusEnumList; // 审核状态枚举
	private static List<ItemTagEnum> itemTagEnumList; // 产品标签枚举
	private static List<ProgressEnum> progressEnumList; // 众筹进展枚举
	private static List<StageEnum> stageEnumList; // 所属阶段枚举
	private static List<TeamCountEnum> teamCountEnumList; // 团队人数枚举

	// /////////////////////////////////////////////////////////////////////////////////////
	// ////
	// //// 自定义枚举集合
	// ////
	// /////////////////////////////////////////////////////////////////////////////////////

	public static List<ItemTagEnum> getallItemTagEnum() {
		if (itemTagEnumList == null) {
			itemTagEnumList = new ArrayList<ItemTagEnum>();
			for (ItemTagEnum _enum : ItemTagEnum.values()) {
				itemTagEnumList.add(_enum);
			}
			itemTagEnumList = Collections.unmodifiableList(itemTagEnumList);
		}
		return itemTagEnumList;
	}

	public static List<ProgressEnum> getallProgressEnum() {
		if (progressEnumList == null) {
			progressEnumList = new ArrayList<ProgressEnum>();
			for (ProgressEnum _enum : ProgressEnum.values()) {
				progressEnumList.add(_enum);
			}
			progressEnumList = Collections.unmodifiableList(progressEnumList);
		}
		return progressEnumList;
	}

	public static List<StageEnum> getallStageEnum() {
		if (stageEnumList == null) {
			stageEnumList = new ArrayList<StageEnum>();
			for (StageEnum _enum : StageEnum.values()) {
				stageEnumList.add(_enum);
			}
			stageEnumList = Collections.unmodifiableList(stageEnumList);
		}
		return stageEnumList;
	}

	public static List<TeamCountEnum> getallTeamCountEnum() {
		if (teamCountEnumList == null) {
			teamCountEnumList = new ArrayList<TeamCountEnum>();
			for (TeamCountEnum _enum : TeamCountEnum.values()) {
				teamCountEnumList.add(_enum);
			}
			teamCountEnumList = Collections.unmodifiableList(teamCountEnumList);
		}
		return teamCountEnumList;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// ////
	// //// 静态枚举转化
	// ////
	// /////////////////////////////////////////////////////////////////////////////////////

	public static String verifyStatusEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return VerifyStatusEnum.getEnum(v).getDesc();
	}

	public static String tagsEnumName(String v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return ItemTagEnum.getAction(v).getDesc();
	}

	public static String progressEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return ProgressEnum.getAction(v).getDesc();
	}

	public static String stageEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return StageEnum.getAction(v).getDesc();
	}

	public static String teamCountEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return TeamCountEnum.getAction(v).getDesc();
	}

	public static String subHandleStatusName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return SubHandleStatusEnum.getEnum(v).getName();
	}

	public static String orderStatusEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return OrderStatusEnum.getEnum(v).getName();
	}

	public static String payTypeEnumName(Integer v) {
		if (v == null) {
			return StringUtils.EMPTY;
		}
		return PayTypeEnum.getEnum(v).getName();
	}

	/**
	 * 判断认购表保证金未支付状态
	 */
	public static boolean isDepositNotPay(Integer v) {
		return SubHandleStatusEnum.isDepositNotPay(v);
	}

	public static boolean isDepositSuccessPay(Integer v) {
		return SubHandleStatusEnum.isDepositSuccessPay(v);
	}

	public static boolean isClose(Integer v) {
		return SubHandleStatusEnum.isClose(v);
	}

	public static boolean isAllSuccessPay(Integer v) {
		return SubHandleStatusEnum.isAllSuccessPay(v);
	}

	public static boolean isAllNotPay(Integer v) {
		return SubHandleStatusEnum.isAllNotPay(v);
	}

	/**
	 * 支付状态：未支付
	 */
	public static boolean isNonPay(Integer v) {
		return OrderStatusEnum.isNonPay(v);
	}
}
