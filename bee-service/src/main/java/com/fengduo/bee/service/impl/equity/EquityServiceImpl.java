/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.equity;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.lang.ArrayUtils;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.data.dao.EquityChangeDao;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.entity.EquityChange;
import com.fengduo.bee.service.interfaces.EquityService;

/**
 * @author zxc Jun 16, 2015 5:15:47 PM
 */
@Service
public class EquityServiceImpl extends BaseService implements EquityService {

    @Autowired
    private EquityChangeDao equityChangeDao;

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 股权变更通知表(EquityChange)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public EquityChange findEquityChange(Parameter q) {
        if (q == null) {
            return null;
        }
        return equityChangeDao.find(q);
    }

    @Override
    public List<EquityChange> listEquityChange(Parameter q) {
        if (q == null) {
            return Collections.<EquityChange> emptyList();
        }
        return equityChangeDao.list(q);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<EquityChange> listPaginationEquityChange(Parameter q, IPageUrl... ipageUrls) {
        return pagination(q, equityChangeDao, ipageUrls);
    }

    @Override
    public EquityChange getEquityChangeById(Long id) {
        if (Argument.isNotPositive(id)) {
            return null;
        }
        return equityChangeDao.getById(id);
    }

    @Override
    public Long add(EquityChange... t) {
        t = ArrayUtils.removeNullElement(t);
        if (Argument.isEmptyArray(t)) {
            return 0l;
        }
        Integer count = 0;
        for (EquityChange equityChange : t) {
            count = equityChangeDao.insert(equityChange);
            if (t.length == 1) {
                return 1l;
            }
        }
        return count == 0 ? 0l : 1l;
    }

    @Override
    public boolean update(EquityChange t) {
        if (t == null) {
            return false;
        }
        if (Argument.isNotPositive(t.getId())) {
            return false;
        }
        Integer count = equityChangeDao.updateById(t);
        return count == 0 ? false : true;
    }

    @Override
    public boolean deleteEquityChange(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        boolean isSuccess = update(new EquityChange(id, DelFlagEnum.DELETE));
        return isSuccess;
    }

    @Override
    public boolean realDeleteEquityChange(Long id) {
        if (Argument.isNotPositive(id)) {
            return false;
        }
        Integer count = equityChangeDao.deleteById(id);
        return count == 0 ? false : true;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // ////
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////
}
