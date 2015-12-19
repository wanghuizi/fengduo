/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.withdraw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.data.dao.WithdrawDao;
import com.fengduo.bee.model.entity.Withdraw;
import com.fengduo.bee.service.interfaces.WithdrawService;

/**
 * 类WithdrawServiceImpl.java的实现描述：TODO 类实现描述
 * 
 * @author jie.xu
 * @date 2015年6月17日 上午10:05:26
 */
@Service("withdrawService")
public class WithdrawServiceImpl extends BaseService implements WithdrawService {

    @Autowired
    private WithdrawDao withdrawDao;

    @Override
    public Withdraw insert(Withdraw withdraw) {
        withdrawDao.insert(withdraw);
        return withdraw;
    }

    @Override
    public boolean updateById(Withdraw withdraw) {
        if (Argument.isNotPositive(withdraw.getId())) {
            return false;
        }
        return withdrawDao.updateById(withdraw) > 0;
    }

    @Override
    public List<Withdraw> list(Parameter query) {

        return withdrawDao.list(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaginationList<Withdraw> listPaginationWithdraw(Parameter q, IPageUrl... i) {

        return pagination(q, withdrawDao, i);
    }
}
