/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.interfaces;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.model.entity.PayOrder;
import com.fengduo.bee.model.entity.UserSub;

/**
 * 投资人预定,基石投资人预定;预定订单生成
 * 
 * @author zxc Jun 16, 2015 5:11:35 PM
 */
public interface OrderService {

	// ////////////////////////////////////////////////////////////////////////////////////////
	// //////////投资人预定表(UserSub)
	// ////////////////////////////////////////////////////////////////////////////////////////

	UserSub findUserSub(Parameter q);

	List<UserSub> listUserSub(Parameter q);

	PaginationList<UserSub> listPaginationUserSub(Parameter q, IPageUrl... i);

	UserSub getUserSubById(Long id);

	Long add(UserSub... t);

	UserSub add(UserSub t);

	Integer countUserSub(Parameter map);

	boolean update(UserSub t);

	boolean deleteUserSub(Long id);

	boolean realDeleteUserSub(Long id);

	/**
	 * 查询用户未取消的认购记录
	 */
	UserSub findUnCloseSub(Long userId, Long itemId);

	/**
	 * 取消认购
	 * 
	 * @param order
	 */
	boolean updateSub2Cancel(Long subId, Long userId, Integer orderStatus);

	// //////////////////////////////////////////////////////////////////////////////////////////
	// /////////////支付订单表

	PayOrder insertOrder(PayOrder order);

	PayOrder getOrderById(Long id);

	PayOrder getOrderByOrderNo(String orderNo);

	boolean updateOrderById(PayOrder order);

	List<PayOrder> listOrder(Parameter query);

	PaginationList<PayOrder> listPaginationPayOrder(Parameter q,
			IPageUrl... ipageUrls);

	/**
	 * 添加认购记录和订单记录
	 */
	boolean insertSubAndOrder(UserSub userSub, PayOrder order);

	/**
	 * 查询该认购记录下的退款订单
	 */
	List<PayOrder> listRefundOrder(Long subId, Long userId);

	/**
	 * <pre>
	 * 生成订单编号 
	 * yyyyMMddxxxxxxx...()
	 * @param n 编号多少位数
	 * </pre>
	 */
	String generateOrderNo();

	/**
	 * 支付成功回调方法
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param operatorId
	 *            操作者Id
	 * @param model
	 *            1:支付；2:退款
	 */
	Result updatePayCallBack(String orderNo, Long operatorId, int model);
}
