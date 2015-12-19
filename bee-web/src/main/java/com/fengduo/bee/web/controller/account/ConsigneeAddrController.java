/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.account;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.result.JsonResultUtils.JsonResult;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.commons.util.StringFormatter;
import com.fengduo.bee.model.entity.ConsigneeAddr;
import com.fengduo.bee.web.controller.BaseController;

/**
 * 收货人地址
 * 
 * @author jie.xu
 * @date 2015年6月23日 上午11:25:44
 */
@Controller
@RequestMapping("/address")
public class ConsigneeAddrController extends BaseController {

    // @InitBinder({ "addr" })
    // public void initBinder(WebDataBinder binder) {
    // binder.setFieldDefaultPrefix("addr.");
    // }

    /**
     * 地址页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        Long userId = getCurrentUserId();
        List<ConsigneeAddr> addrList = consigneeAddrService.listAddrByUserId(userId);
        model.addAttribute("addrList", addrList);
        return "address/index";
    }

    /**
     * 添加地址页面
     */
    @RequestMapping(value = "/add")
    public String add() {
        return "address/add";
    }

    /**
     * 保存地址
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult saveAddr(ConsigneeAddr addr) {
        Long userId = getCurrentUserId();
        Result result = checkLegalParams(addr, userId);
        if (result.isFailed()) {
            return JsonResultUtils.error(result.getMessage());
        }
        addr.setUserId(userId);
        addr = consigneeAddrService.insert(addr);
        if (addr.getId() != null) {
            return JsonResultUtils.success("地址添加成功！");
        } else {
            return JsonResultUtils.error("操作出错！");
        }
    }

    /**
     * @param addr
     * @param userId
     * @description:
     * @author jie.xu
     * @date 2015年6月26日 下午5:38:32
     */
    private Result checkLegalParams(ConsigneeAddr addr, Long userId) {
        if (userId == null) {
            return Result.failed("请先登入！");
        }
        if (StringUtils.isEmpty(addr.getConsigneeName())) {
            return Result.failed("请填写收货人！");
        }
        if (StringUtils.isEmpty(addr.getProvince()) || StringUtils.isEmpty(addr.getCity())) {
            return Result.failed("请选择省市！");
        }
        if (StringUtils.isEmpty(addr.getDetailAddr())) {
            return Result.failed("请填写详细地址！");
        }
        if (StringUtils.isEmpty(addr.getPhone())) {
            return Result.failed("请填写手机号！");
        }

        boolean flag = StringFormatter.matchsRegex(addr.getPhone(), StringFormatter.PHONE_REG);
        if (!flag) {
            return Result.failed("请正确填写手机号");
        }
        return Result.success();
    }

    /**
     * 地址设为默认
     */
    @RequestMapping(value = "/setDefault")
    @ResponseBody
    public JsonResult setDefault(Long id) {
        if (Argument.isNotPositive(id)) {
            return JsonResultUtils.error("参数不合法！");
        }
        boolean flag = consigneeAddrService.setAddrDefault(id);
        if (flag) {
            return JsonResultUtils.success();
        } else {
            return JsonResultUtils.error("操作出错");
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public JsonResult delete(Long id) {
        if (Argument.isNotPositive(id)) {
            return JsonResultUtils.error("参数不合法！");
        }
        boolean flag = consigneeAddrService.delAddr(id);
        if (flag) {
            return JsonResultUtils.success(id);
        } else {
            return JsonResultUtils.error("操作出错");
        }
    }
}
