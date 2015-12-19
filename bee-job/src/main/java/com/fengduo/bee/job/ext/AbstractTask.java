/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.job.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fengduo.bee.service.interfaces.EmailNotifyService;
import com.fengduo.bee.service.interfaces.ItemService;
import com.fengduo.bee.service.interfaces.MailService;
import com.fengduo.bee.service.interfaces.OrderService;
import com.fengduo.bee.service.interfaces.SmsService;
import com.fengduo.bee.service.interfaces.UserService;

/**
 * Task 公用代码片段
 * 
 * <pre>
 *   任务调度基类，所有任务或者定时Job都需要继承，并实现doTask方法，调用before()方法
 * </pre>
 * 
 * @author zxc Jul 2, 2015 1:42:41 PM
 */
public abstract class AbstractTask {

    public static Logger             logger = LoggerFactory.getLogger(AbstractTask.class);

    @Autowired
    protected UserService            userService;
    @Autowired
    protected ItemService            itemService;
    @Autowired
    protected OrderService           orderService;
    @Autowired
    protected EmailNotifyService     emailNotifyService;
    @Autowired
    protected MailService            mailService;
    @Autowired
    protected SmsService             smsService;

    @Autowired
    protected ThreadPoolTaskExecutor executor;

    public AbstractTask() {

    }

    public boolean exec(Runnable runnable, Object... obj) {

        executor.submit(runnable);
        return true;
    }

    /**
     * 具体做任务的方法
     * 
     * @throws Exception
     */
    public abstract void doTask() throws Exception;

    /**
     * 做任务前的准备工作
     * 
     * @throws Exception
     */
    public abstract void before() throws Exception;
}
