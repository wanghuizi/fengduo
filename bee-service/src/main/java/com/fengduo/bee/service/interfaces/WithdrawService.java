package com.fengduo.bee.service.interfaces;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.model.entity.Withdraw;

/**
 * 类WithdrawService.java的实现描述：提现服务
 * 
 * @author jie.xu
 * @date 2015年6月16日 下午8:35:48
 */
public interface WithdrawService {

    Withdraw insert(Withdraw withdraw);

    boolean updateById(Withdraw withdraw);

    List<Withdraw> list(Parameter query);

    PaginationList<Withdraw> listPaginationWithdraw(Parameter q, IPageUrl... i);
}
