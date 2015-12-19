/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.web.controller.cms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zxc
 */
@Controller
public class CMSController {

    /**
     * 关于我们
     * 
     * @return
     */
    @RequestMapping(value = "/aboutus")
    public ModelAndView aboutus() {
        ModelAndView mav = new ModelAndView("cms/aboutus");
        return mav;
    }

    /**
     * 帮助中心
     * 
     * @return
     */
    @RequestMapping(value = "/help/{name}")
    public ModelAndView help(@PathVariable("name")
    String name) {
        ModelAndView mav = new ModelAndView("cms/" + name);
        mav.addObject("name", name);
        return mav;
    }

    /**
     * 帮助中心
     * 
     * @return
     */
    @RequestMapping(value = "/news/{id}")
    public ModelAndView news(@PathVariable("id")
    String id) {
        ModelAndView mav = new ModelAndView("cms/" + id);
        return mav;
    }
}
