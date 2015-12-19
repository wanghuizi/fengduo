/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author zxc Jul 1, 2015 12:16:56 PM
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -85785596505169362L;

    private String            captcha;

    private boolean           useCaptcha;                            // 默认使用图像验证码

    public UsernamePasswordCaptchaToken() {
        super();
    }

    public UsernamePasswordCaptchaToken(String username, char[] password) {
        setUsername(username);
        setPassword(password);
        setRememberMe(true);
        setUseCaptcha(true);
    }

    public boolean isUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaToken(String username, char[] password, boolean rememberMe, String host,
                                        String captcha, boolean useCaptcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.useCaptcha = useCaptcha;
    }
}
