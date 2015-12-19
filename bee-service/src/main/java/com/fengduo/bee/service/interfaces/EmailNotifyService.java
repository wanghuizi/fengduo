package com.fengduo.bee.service.interfaces;

import java.util.List;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.model.entity.EmailNotify;

/**
 * 邮件提醒服务
 * 
 * @author jie.xu
 * @date 2015年6月16日 下午8:41:18
 */
public interface EmailNotifyService {

    EmailNotify insert(EmailNotify emailNotify);

    List<EmailNotify> list(Parameter query);

    PaginationList<EmailNotify> listPaginationEmailNotify(Parameter q, IPageUrl... i);

    boolean updateById(EmailNotify emailNotify);
}
