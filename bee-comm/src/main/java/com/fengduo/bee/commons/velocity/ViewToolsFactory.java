/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.commons.velocity;

import java.util.Map;

/**
 * @author zxc May 28, 2015 12:19:00 PM
 */
public class ViewToolsFactory {

    private Map<String, Object> viewTools;

    public Map<String, Object> getViewTools() {
        return viewTools;
    }

    public void setViewTools(Map<String, Object> viewTools) {
        this.viewTools = viewTools;
    }
}
