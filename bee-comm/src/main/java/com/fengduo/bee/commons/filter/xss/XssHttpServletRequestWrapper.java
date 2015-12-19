/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.filter.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author zxc Jun 23, 2015 9:29:13 PM
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) return null;
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        // String htmlEscape = HtmlUtils.htmlEscape(value);
        // You'll need to remove the spaces from the html entities below
        // value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        // value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        // value = value.replaceAll("'", "& #39;");
        // value = value.replaceAll("eval\\((.*)\\)", "");
        // value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        // value = value.replaceAll("script", "");
        String htmlEscape = XssClean.clean(value);
        return htmlEscape;
    }
}
