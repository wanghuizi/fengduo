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
import com.fengduo.bee.model.entity.EquityChange;

/**
 * 股权变更,股权交接,股权转让
 * 
 * @author zxc Jun 16, 2015 5:12:54 PM
 */
public interface EquityService {

    // ////////////////////////////////////////////////////////////////////////////////////////
    // //////////股权变更通知表(EquityChange)
    // ////////////////////////////////////////////////////////////////////////////////////////

    EquityChange findEquityChange(Parameter q);

    List<EquityChange> listEquityChange(Parameter q);

    PaginationList<EquityChange> listPaginationEquityChange(Parameter q, IPageUrl... i);

    EquityChange getEquityChangeById(Long id);

    Long add(EquityChange... t);

    boolean update(EquityChange t);

    boolean deleteEquityChange(Long id);

    boolean realDeleteEquityChange(Long id);
}
