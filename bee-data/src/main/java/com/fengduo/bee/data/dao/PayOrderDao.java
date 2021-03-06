/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.data.dao;

import com.fengduo.bee.commons.persistence.annotation.MyBatisRepository;
import com.fengduo.bee.commons.persistence.mapper.CrudMapper;
import com.fengduo.bee.model.entity.PayOrder;

/**
 * @author jie.xu
 * @date 2015年6月16日 下午8:16:48
 */
@MyBatisRepository
public interface PayOrderDao extends CrudMapper<PayOrder> {

	/**
	 * 根据订单编号获得订单
	 */
	PayOrder getOrderByOrderNo(String orderNo);

}
