/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.data.dao;

import com.fengduo.bee.commons.persistence.annotation.MyBatisRepository;
import com.fengduo.bee.commons.persistence.mapper.CrudMapper;
import com.fengduo.bee.model.entity.IdentityInfo;

/**
 * @author jie.xu
 * @date 2015年6月9日 下午2:26:26
 */
@MyBatisRepository
public interface IdentityInfoDao extends CrudMapper<IdentityInfo> {

}
