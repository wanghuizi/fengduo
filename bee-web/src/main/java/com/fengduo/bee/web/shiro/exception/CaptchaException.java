/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author zxc Jul 1, 2015 12:15:34 PM
 */
public class CaptchaException extends AuthenticationException {

    private static final long serialVersionUID = -5772851691200567121L;

    public CaptchaException() {
        super();
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }
}
