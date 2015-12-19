/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.util.NumberParser;
import com.fengduo.bee.commons.util.ObjectUtils;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemMember;
import com.fengduo.bee.web.controller.BaseController;

/**
 * 产品项目编辑
 * 
 * @author zxc Jun 23, 2015 2:30:05 PM
 */
@Controller
@RequestMapping(value = "/item")
public class ItemEditController extends BaseController {

    /**
     * 编辑我的项目
     * 
     * @return
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id")
    Long id, ModelAndView mav) {
        mav.setViewName("item/add");
        // 验证id是否存在
        if (Argument.isNotPositive(id)) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            return mav;
        }
        // 验证id对应的产品基础信息是否存在
        Item item = itemService.getItemById(id);
        if (item == null) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            return mav;
        }
        // 验证是否是自己创建的产品
        if (!NumberParser.isEqual(getCurrentUserId(), item.getUserId())) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            mav.setViewName("error/404");
            return mav;
        }
        mav.addObject("itemId", id);
        mav.addObject("item", item);
        mav.addObject("postUrl", "/item/edit");
        return mav;
    }

    /**
     * 提交编辑的项目
     * 
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String create(@Valid
    Item item, BindingResult result, Model model) {
        model.addAttribute("item", item);
        ObjectUtils.trim(item);
        // 校验数据
        if (result.hasErrors()) {
            model.addAttribute("errMsg", showFirstErrors(result));
            return "item/add";
        }
        // 验证发起人信息
        Long currentUserId = getCurrentUserId();
        if (Argument.isNotPositive(currentUserId)) {
            model.addAttribute("errMsg", "您尚未登录,请登录!");
            return "account/login";
        }
        if (Argument.isNotPositive(item.getId())) {
            model.addAttribute("errMsg", "产品id不存在,编辑失败!");
            return "item/add";
        }
        // 验证项目信息是否已存在数据库中
        Parameter query = Parameter.newParameter()//
        .pu("userId", currentUserId)//
        .pu("id", item.getId());
        Item findItem = itemService.findItem(query);
        if (findItem == null) {
            model.addAttribute("errMsg", "该项目不存在,不可以编辑!");
            return "item/add";
        }
        // 验证是否是自己创建的产品
        if (!NumberParser.isEqual(currentUserId, findItem.getUserId())) {
            model.addAttribute("errMsg", "产品id不存在,请创建!");
            return "error/404";
        }
        // 设置用户信息,审核状态
        item.setUserId(currentUserId);
        if (findItem.getVerifyStatus() != null) {
            item.setVerifyStatus(findItem.getVerifyStatus());
        } else {
            item.setVerifyStatus(VerifyStatusEnum.UN_COMPLETELY.getValue());
        }

        // 插入项目基础信息
        itemService.update(item);
        // 跳转到融资信息编辑页面
        return "redirect:/item/finance/edit/" + item.getId();
    }

    /**
     * 添加融资信息
     * 
     * @return
     */
    @RequestMapping(value = "/finance/edit/{id}", method = RequestMethod.GET)
    public ModelAndView finance(@PathVariable("id")
    Long id) {
        ModelAndView mav = new ModelAndView("item/finance");
        mav.addObject("itemId", id);
        // 验证产品ID
        if (Argument.isNotPositive(id)) {
            mav.addObject("errMsg", "提交错误,产品ID为空!");
            mav.setViewName("error/404");
            return mav;
        }
        ItemFinance itemFinance = itemService.getItemFinanceByItemId(id);
        if (itemFinance == null) {
            itemFinance = new ItemFinance();
        }
        Item findItem = itemService.getItemById(id);
        // 验证是否是自己创建的产品
        if (findItem == null || !NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            mav.setViewName("error/404");
            return mav;
        }
        mav.addObject("itemFinance", itemFinance);
        mav.addObject("postUrl", "/item/finance/edit");
        return mav;
    }

    @RequestMapping(value = "/finance/edit", method = RequestMethod.POST)
    public String finance(@Valid
    ItemFinance itemFinance, BindingResult result, Long itemId, Model model) {
        model.addAttribute("itemFinance", itemFinance);
        ObjectUtils.trim(itemFinance);
        // 验证产品ID
        if (Argument.isNotPositive(itemId)) {
            model.addAttribute("errMsg", "提交错误,产品ID为空!");
            return "item/finance";
        }
        // 校验数据
        if (result.hasErrors()) {
            model.addAttribute("errMsg", showFirstErrors(result));
            return "item/finance";
        }
        // 验证用户是否登录
        Long currentUserId = getCurrentUserId();
        if (Argument.isNotPositive(currentUserId)) {
            model.addAttribute("errMsg", "您尚未登录,请登录!");
            return "item/finance";
        }
        // 验证项目是否当前登录用户本人发起,项目编辑权限
        Parameter query = Parameter.newParameter()//
        .pu("userId", currentUserId)//
        .pu("id", itemId);
        Item findItem = itemService.findItem(query);
        if (findItem == null) {
            model.addAttribute("errMsg", "该项目不存在!");
            return "item/finance";
        }
        // 验证是否是自己创建的产品
        if (!NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            model.addAttribute("errMsg", "产品id不存在,请创建!");
            return "error/404";
        }
        // 设置产品基础信息ID
        itemFinance.setItemId(itemId);
        if (Argument.isNotPositive(itemFinance.getId())) {
            // 保存融资信息
            itemService.add(itemFinance);
        } else {
            itemService.update(itemFinance);
        }
        // 跳转到团队信息编辑页面
        return "redirect:/item/member/edit/" + itemId;
    }

    /**
     * 添加项目背景团队介绍
     * 
     * @return
     */
    @RequestMapping(value = "/member/edit/{id}", method = RequestMethod.GET)
    public ModelAndView member(@PathVariable("id")
    Long id) {
        ModelAndView mav = new ModelAndView("item/member");
        // 验证产品ID
        if (Argument.isNotPositive(id)) {
            mav.addObject("errMsg", "提交错误,产品ID为空!");
            mav.setViewName("error/404");
            return mav;
        }
        Item findItem = itemService.getItemById(id);
        if (findItem == null) {
            mav.addObject("errMsg", "该项目不存在!");
            mav.setViewName("error/404");
            return mav;
        }
        // 验证是否是自己创建的产品
        if (!NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            mav.setViewName("error/404");
            return mav;
        }

        mav.addObject("itemId", id);
        List<ItemMember> itemMemberList = itemService.getItemMemberByItemId(id);
        mav.addObject("itemMemberList", itemMemberList);
        mav.addObject("postUrl", "/item/edit/success");
        return mav;
    }

    /**
     * 编辑项目成功页面
     * 
     * @return
     */
    @RequestMapping(value = "/edit/success")
    public ModelAndView success(Long itemId) {
        ModelAndView mav = new ModelAndView("item/success");
        if (itemId == null) {
            mav.addObject("errMsg", "产品id不存在,提交编辑失败!");
            return mav;
        }
        Item itemById = itemService.getItemById(itemId);
        if (itemById == null) {
            mav.addObject("errMsg", "产品不存在,提交编辑失败!");
            return mav;
        }
        // 验证是否是自己创建的产品
        if (!NumberParser.isEqual(getCurrentUserId(), itemById.getUserId())) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            mav.setViewName("error/404");
            return mav;
        }
        ItemFinance itemFinanceByItemId = itemService.getItemFinanceByItemId(itemId);
        if (itemFinanceByItemId == null) {
            mav.addObject("errMsg", "未填写完成融资信息,提交编辑失败!");
            return mav;
        }
        List<ItemMember> itemMemberList = itemService.getItemMemberByItemId(itemId);
        if (Argument.isEmpty(itemMemberList)) {
            mav.addObject("errMsg", "未填写完成团队信息,提交编辑失败!");
            return mav;
        }
        // 验证成功,安全提交进入后台审核
        itemService.update(new Item(itemId, VerifyStatusEnum.UNAUDITED));
        return mav;
    }
}
