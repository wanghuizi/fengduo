/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.data.dao;

import java.util.List;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.annotation.MyBatisRepository;
import com.fengduo.bee.commons.persistence.mapper.CrudMapper;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemFull;

/**
 * @author zxc Jun 9, 2015 1:05:56 PM
 */
@MyBatisRepository
public interface ItemDao extends CrudMapper<Item> {

    public ItemFull findFull(Parameter query);

    public List<ItemFull> listPaginationFull(Parameter query);

    public int countFull(Parameter query);

    public List<ItemFull> listFull(Parameter query);
}
