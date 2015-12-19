/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.util.DateViewTools;
import com.fengduo.bee.job.ext.AbstractTask;
import com.fengduo.bee.model.entity.EmailNotify;
import com.fengduo.bee.model.entity.User;

/**
 * 类EmailTask.java的实现描述：邮件发送任务 查邮件记录表中发送失败的邮件
 * 
 * @author jie.xu
 * @date 2015年7月3日 上午11:53:32
 */
@Service("emailJob")
public class EmailTask extends AbstractTask {

    private static Logger LOG = LoggerFactory.getLogger(EmailTask.class);

    @Override
    public void doTask() throws Exception {
        // 查询未发送的邮件
        Parameter map = new Parameter();
        map.put("status", 0);
        List<EmailNotify> list = emailNotifyService.list(map);
        if (list == null || list.isEmpty()) {
            LOG.info("查询到需要发送的邮件个数：0个，时间：{}", DateViewTools.formatFullDate(new Date()));
            return;
        }
        for (final EmailNotify email : list) {
            executor.execute(new Runnable() {

                @Override
                public void run() {

                    Long toId = email.getReceiverId();
                    User user = userService.getUserById(toId);
                    if (user != null) {
                        boolean falg = mailService.sendTxtMail(user.getEmail(), email.getTitle(), email.getContent());

                        if (falg) {
                            // update 邮件状态为1
                            LOG.info("邮件id：{} 发送成功：0个，时间：{}", email.getId(), DateViewTools.formatFullDate(new Date()));
                            EmailNotify updateEmail = new EmailNotify();
                            updateEmail.setId(email.getId());
                            updateEmail.setStatus(1);
                            emailNotifyService.updateById(updateEmail);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void before() throws Exception {
        // TODO Auto-generated method stub

    }
}
