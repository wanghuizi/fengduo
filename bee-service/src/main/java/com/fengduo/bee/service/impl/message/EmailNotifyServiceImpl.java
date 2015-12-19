/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.data.dao.EmailNotifyDao;
import com.fengduo.bee.model.entity.EmailNotify;
import com.fengduo.bee.service.interfaces.EmailNotifyService;

/**
 * 类EmailNotifyServiceImpl.java的实现描述：邮件通知表存储服务
 * 
 * @author jie.xu
 * @date 2015年6月17日 上午10:08:10
 */
@Service("emailNotifyService")
public class EmailNotifyServiceImpl extends BaseService implements EmailNotifyService {

    @Autowired
    private EmailNotifyDao emailNotifyDao;

    @Override
    public EmailNotify insert(EmailNotify emailNotify) {
        emailNotifyDao.insert(emailNotify);
        return emailNotify;
    }

    @Override
    public List<EmailNotify> list(Parameter map) {
        return emailNotifyDao.list(map);
    }

    @Override
    public PaginationList<EmailNotify> listPaginationEmailNotify(Parameter q, IPageUrl... i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean updateById(EmailNotify emailNotify) {
        return emailNotifyDao.updateById(emailNotify) > 0;
    }
}
