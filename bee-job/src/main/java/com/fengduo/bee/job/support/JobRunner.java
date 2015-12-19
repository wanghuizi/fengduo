/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.job.support;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

/**
 * Job启动基类
 * 
 * @author zxc Jul 2, 2015 2:22:11 PM
 */
public class JobRunner {

    static final Logger logger = LoggerFactory.getLogger(JobRunner.class);

    // 使用请配置日志路径
    static {
        try {
            Log4jConfigurer.initLogging("src/main/resources/log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/spring/spring-*.xml");
        context.start();
        logger.info("JobRunner service server start !");
    }
}
