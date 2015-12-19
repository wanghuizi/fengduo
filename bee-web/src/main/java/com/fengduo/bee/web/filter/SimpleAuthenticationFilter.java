/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fengduo.bee.commons.cons.ResultCode;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.web.shiro.token.UsernamePasswordCaptchaToken;
import com.fengduo.bee.web.utils.InvokeTypeTools;

/**
 * Shiro Ajax未登录过滤 图像验证码绑定
 * 
 * @author xujie Jun 23, 2015 1:05:56 PM
 */
public class SimpleAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log                   = LoggerFactory.getLogger(SimpleAuthenticationFilter.class);

    public static final String  DEFAULT_CAPTCHA_PARAM = "captcha";

    private String              captchaParam          = DEFAULT_CAPTCHA_PARAM;

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);

        return new UsernamePasswordCaptchaToken(username, password.toCharArray(), rememberMe, host, captcha, true);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // ajax未登入情况
        if (InvokeTypeTools.isAjax(httpRequest)) {
            httpResponse.setContentType("application/json");
            PrintWriter writer = httpResponse.getWriter();
            writer.print(JsonResultUtils.createJsonResult(ResultCode.SUCCESS, "", "登录成功"));
            writer.flush();
            writer.close();
        } else {
            return super.onLoginSuccess(token, subject, request, response);
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                // allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
                          + "Authentication url [" + getLoginUrl() + "]");
            }
            if (InvokeTypeTools.isAjax(httpRequest)) {
                // ajax未登入情况
                httpResponse.setContentType("application/json");
                PrintWriter writer = httpResponse.getWriter();
                writer.print(JsonResultUtils.createJsonResult(ResultCode.NEED_LOGIN, "", "请先登入!"));
                writer.flush();
                writer.close();
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }
}
