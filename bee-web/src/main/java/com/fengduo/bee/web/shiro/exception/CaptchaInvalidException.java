/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author zxc Jul 1, 2015 12:36:36 PM
 */
public class CaptchaInvalidException extends AuthenticationException {

    private static final long serialVersionUID = 3618750163708338899L;

    public CaptchaInvalidException() {
        super();
    }

    public CaptchaInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaInvalidException(String message) {
        super(message);
    }

    public CaptchaInvalidException(Throwable cause) {
        super(cause);
    }
}
