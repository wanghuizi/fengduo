/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.filter.xss;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.fengduo.bee.commons.exception.ServiceException;

/**
 * XSS清洁工 用于过滤XSS
 * 
 * @author zxc Jun 26, 2015 2:56:51 PM
 */
public class XssClean {

    private static final Logger logger = LoggerFactory.getLogger(XssClean.class);

    private static Policy       policy = null;

    public static Policy getPolicy() throws PolicyException, ServiceException {
        if (policy == null) {
            ClassPathResource classPathResource = new ClassPathResource("/antisamy/spark-antisamy.xml");
            if (classPathResource == null || !classPathResource.exists()) {
                throw new ServiceException("spark-antisamy.xml is not exists!");
            }
            InputStream policyFile = XssClean.class.getResourceAsStream("/antisamy/spark-antisamy.xml");
            if (policyFile == null) {
                throw new ServiceException("spark-antisamy.xml is not exists!");
            }
            policy = Policy.getInstance(policyFile);
        }
        return policy;
    }

    public static String clean(String value) {
        if (StringUtils.isNotEmpty(value)) {
            AntiSamy antiSamy = new AntiSamy();
            try {
                final CleanResults cr = antiSamy.scan(value, getPolicy());
                // 安全的HTML输出
                value = cr.getCleanHTML();
            } catch (ScanException e) {
                logger.error("过滤XSS异常");
            } catch (PolicyException e) {
                logger.error("加载XSS规则文件异常: " + e.getMessage());
            } catch (ServiceException e) {
                logger.error("antisamy配置文件没有找到!");
            }
        }
        return value;
    }
}
