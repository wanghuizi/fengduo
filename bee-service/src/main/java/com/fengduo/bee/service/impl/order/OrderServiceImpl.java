/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.order;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.lang.ArrayUtils;
import com.fengduo.bee.commons.core.utils.Identities;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.data.dao.PayOrderDao;
import com.fengduo.bee.data.dao.UserSubDao;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.OrderStatusEnum;
import com.fengduo.bee.model.cons.OrderTypeEnum;
import com.fengduo.bee.model.cons.SubHandleStatusEnum;
import com.fengduo.bee.model.cons.SubUserTypeEnum;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.PayOrder;
import com.fengduo.bee.model.entity.UserSub;
import com.fengduo.bee.service.interfaces.ItemService;
import com.fengduo.bee.service.interfaces.OrderService;

/**
 * 订单服务实现
 * 
 * @author zxc Jun 16, 2015 5:15:06 PM
 */
@Service("orderService")
public class OrderServiceImpl extends BaseService implements OrderService {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMdd");

	@Autowired
	private UserSubDao userSubDao;

	@Autowired
	private PayOrderDao payOrderDao;

	@Autowired
	private ItemService itemService;

	// /////////////////////////////////////////////////////////////////////////////////////
	// ////
	// //// 投资人预定表(UserSub)
	// ////
	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public UserSub findUserSub(Parameter q) {
		if (q == null) {
			return null;
		}
		return userSubDao.find(q);
	}

	@Override
	public List<UserSub> listUserSub(Parameter q) {
		if (q == null) {
			return Collections.<UserSub> emptyList();
		}
		return userSubDao.list(q);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PaginationList<UserSub> listPaginationUserSub(Parameter q,
			IPageUrl... ipageUrls) {

		return pagination(q, userSubDao, ipageUrls);
	}

	@Override
	public UserSub getUserSubById(Long id) {
		if (Argument.isNotPositive(id)) {
			return null;
		}
		return userSubDao.getById(id);
	}

	@Override
	public Long add(UserSub... t) {
		t = ArrayUtils.removeNullElement(t);
		if (Argument.isEmptyArray(t)) {
			return 0l;
		}
		Integer count = 0;
		for (UserSub userSub : t) {
			count = userSubDao.insert(userSub);
			if (t.length == 1) {
				return 1l;
			}
		}
		return count == 0 ? 0l : 1l;
	}

	public UserSub add(UserSub t) {
		userSubDao.insert(t);
		return t;

	}

	@Override
	public boolean update(UserSub t) {
		if (t == null) {
			return false;
		}
		if (Argument.isNotPositive(t.getId())) {
			return false;
		}
		Integer count = userSubDao.updateById(t);
		return count == 0 ? false : true;
	}

	@Override
	public boolean deleteUserSub(Long id) {
		if (Argument.isNotPositive(id)) {
			return false;
		}
		boolean isSuccess = update(new UserSub(id, DelFlagEnum.DELETE));
		return isSuccess;
	}

	@Override
	public boolean realDeleteUserSub(Long id) {
		if (Argument.isNotPositive(id)) {
			return false;
		}
		Integer count = userSubDao.deleteById(id);
		return count == 0 ? false : true;
	}

	@Override
	public Integer countUserSub(Parameter map) {
		return userSubDao.count(map);
	}

	@Override
	public UserSub findUnCloseSub(Long userId, Long itemId) {
		// 构造查询条件
		Parameter query = Parameter.newParameter()//
				.pu("userId", userId)//
				.pu("itemId", itemId);
		return userSubDao.queryUnCloseSub(query);
	}

	@Override
	public boolean updateSub2Cancel(Long subId, Long userId, Integer orderStatus) {
		UserSub sub = new UserSub(subId, SubHandleStatusEnum.CLOSE);
		boolean flag = userSubDao.updateById(sub) > 0;
		if (flag) {
			// 取消订单
			payOrderDao.update(new PayOrder(userId, subId, orderStatus));
		}
		return flag;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// ////
	// ////支付订单表
	// ////
	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public PayOrder insertOrder(PayOrder order) {
		payOrderDao.insert(order);
		return order;
	}

	@Override
	public boolean updateOrderById(PayOrder order) {
		if (Argument.isNotPositive(order.getId())) {
			return false;
		}
		return payOrderDao.updateById(order) > 0;
	}

	@SuppressWarnings("unchecked")
	public PaginationList<PayOrder> listPaginationPayOrder(Parameter q,
			IPageUrl... ipageUrls) {
		return pagination(q, payOrderDao, ipageUrls);
	}

	@Override
	public List<PayOrder> listOrder(Parameter query) {
		return payOrderDao.list(query);
	}

	@Override
	public boolean insertSubAndOrder(UserSub userSub, PayOrder order) {
		int i = userSubDao.insert(userSub);
		order.setSubId(userSub.getId());
		int j = payOrderDao.insert(order);
		return i + j == 2;
	}

	public String generateOrderNo() {
		String prefix = dateFormat.format(new Date());
		String randomNum = Identities.randomNum(10);
		return prefix + randomNum;
	}

	@Override
	public PayOrder getOrderById(Long id) {
		return payOrderDao.getById(id);
	}

	@Override
	public List<PayOrder> listRefundOrder(Long subId, Long userId) {
		// 构造查询条件
		Parameter query = Parameter.newParameter()//
				.pu("subId", subId)//
				.pu("userId", userId)//
				.pu("status", OrderStatusEnum.SUCCESS_REFUND.getValue());
		return listOrder(query);
	}

	@Override
	public Result updatePayCallBack(String orderNo, Long operatorId, int model) {
		PayOrder order = getOrderByOrderNo(orderNo);
		if (order == null) {
			return Result.failed("订单不存在");
		}
		if (model != 1 && model != 2) {
			return Result.failed("请选择该订单是支付还是退款模式");
		}
		// 状态未支付的才可支付
		boolean isNonPay = OrderStatusEnum.isNonPay(order.getStatus());
		if (!isNonPay) {
			return Result.failed("该订单状态并不是未支付");
		}
		Long itemId = order.getItemId();
		Long subId = order.getSubId();
		Integer subUserType = order.getSubUserType();
		Float amount = order.getAmount();// 订单金额

		if (SubUserTypeEnum.isCornerstoneSub(subUserType)) {
			// 基石投资者
			// 修改领投人认购记录表状态为2
			UserSub sub = getUserSubById(subId);
			if (sub == null) {
				return Result.failed("基石投资者认购记录不存在");
			}
			sub.setSubDate(new Date());
			sub.setHandleStatus(SubHandleStatusEnum.DEPOSIT_SUCCESS_PAY
					.getValue());
			boolean updateFlag = update(sub);
			if (!updateFlag) {
				return Result.failed("更新基石投资者认购记录失败");
			}

			// 修改产品融资信息表(实际已融资金额，实际认购金融,认购人数)
			modifyItemFinanceByOrder(itemId, amount, order.getType(),
					subUserType);
			orderStatus2Success(operatorId, order);
			return Result.success();
		}

		if (SubUserTypeEnum.isFollowSub(subUserType)) {
			// 跟投人
			UserSub sub = getUserSubById(subId);
			if (sub == null) {
				return Result.failed("基石投资者认购记录不存在");
			}
			sub.setSubDate(new Date());
			if (OrderTypeEnum.isCashDeposit(order.getType())) {
				// 保证金类型
				// 认购表记录状态改4
				sub.setHandleStatus(SubHandleStatusEnum.ALL_NOT_PAY.getValue());

			} else if (OrderTypeEnum.isCashAll(order.getType())) {
				// 全额类型
				// 认购表记录状态改5
				sub.setHandleStatus(SubHandleStatusEnum.ALL_SUCCESS_PAY
						.getValue());
			} else {
				return Result.failed("订单类型不合法");
			}
			boolean updateFlag = update(sub);
			if (!updateFlag) {
				return Result.failed("更新跟投人认购记录失败");
			}

			// 修改产品融资信息表(实际已融资金额，实际认购金融,认购人数),支付余额款成功时认购人数不更新
			modifyItemFinanceByOrder(itemId, amount, order.getType(),
					subUserType);
			// 修改订单表状态
			orderStatus2Success(operatorId, order);
			return Result.success();
		}
		return Result.failed();
	}

	/**
	 * @param operatorId
	 * @param order
	 * @description: 更新订单状态为支付成功
	 * @author jie.xu
	 * @date 2015年7月6日 下午6:25:38
	 */
	private void orderStatus2Success(Long operatorId, PayOrder order) {
		PayOrder updateOrder = new PayOrder();
		updateOrder.setId(order.getId());
		updateOrder.setOperatorId(operatorId);
		updateOrder.setOperateDate(new Date());
		updateOrder.setStatus(OrderStatusEnum.SUCCESS_PAY.getValue());
		updateOrderById(updateOrder);
	}

	private void modifyItemFinanceByOrder(Long itemId, Float amount,
			Integer orderType, Integer subUserType) {
		ItemFinance finance = itemService.getItemFinanceByItemId(itemId);
		finance.setRealSub(finance.getRealSub() + amount);
		finance.setRealPay(finance.getRealPay() + amount);
		if (SubUserTypeEnum.isCornerstoneSub(subUserType)) {
			finance.setSuberNum(finance.getSuberNum() + 1);
		} else if (SubUserTypeEnum.isFollowSub(subUserType)) {
			if (OrderTypeEnum.isCashDeposit(orderType)) {
				finance.setSuberNum(finance.getSuberNum() + 1);
			}
		} else {
			return;
		}
		itemService.update(finance);
	}

	@Override
	public PayOrder getOrderByOrderNo(String orderNo) {
		return payOrderDao.getOrderByOrderNo(orderNo);
	}

}
