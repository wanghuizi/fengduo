/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.data.dao;

import com.fengduo.bee.commons.persistence.annotation.MyBatisRepository;
import com.fengduo.bee.commons.persistence.mapper.CrudMapper;
import com.fengduo.bee.model.entity.EquityChange;

/**
 * @author zxc Jun 16, 2015 5:06:46 PM
 */
@MyBatisRepository
public interface EquityChangeDao extends CrudMapper<EquityChange> {

}
