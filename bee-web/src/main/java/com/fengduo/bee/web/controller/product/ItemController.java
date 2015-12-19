/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.web.controller.product;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.util.NumberParser;
import com.fengduo.bee.commons.util.ObjectUtils;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemMember;
import com.fengduo.bee.web.controller.BaseController;

/**
 * 发起项目页面(项目基础信息页面,融资信息页面,项目资料页面)
 * 
 * @author zxc May 28, 2015 11:54:13 PM
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController extends BaseController {

    /**
     * 发起项目
     * 
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(ModelAndView mav) {
        mav.setViewName("item/add");
        mav.addObject("postUrl", "/item/create");
        return mav;
    }

    /**
     * 提交发起项目
     * 
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid
    Item item, BindingResult result, Model model) {
        model.addAttribute("item", item);
        // 校验数据
        if (result.hasErrors()) {
            model.addAttribute("errMsg", showFirstErrors(result));
            return "item/add";
        }
        ObjectUtils.trim(item);
        // 验证发起人信息
        Long currentUserId = getCurrentUserId();
        if (Argument.isNotPositive(currentUserId)) {
            model.addAttribute("errMsg", "您尚未登录,请登录!");
            return "account/login";
        }
        // 设置用户信息,审核状态
        item.setUserId(currentUserId);
        item.setVerifyStatus(VerifyStatusEnum.UN_COMPLETELY.getValue());
        item.setProgress(ProgressEnum.UNSTART.getValue());
        // 验证项目信息是否已存在数据库中
        Parameter query = Parameter.newParameter()//
        .pu("userId", currentUserId)//
        .pu("name", item.getName());
        Item findItem = itemService.findItem(query);
        if (findItem != null) {
            model.addAttribute("errMsg", "该项目已经存在,可以直接编辑!");
            return "redirect:/item/" + findItem.getId() + "/edit";
        }
        // 插入项目基础信息
        itemService.add(item);
        // 跳转到融资信息编辑页面
        return "redirect:/item/finance/" + item.getId();
    }

    /**
     * 添加融资信息
     * 
     * @return
     */
    @RequestMapping(value = "/finance/{id}", method = RequestMethod.GET)
    public ModelAndView finance(@PathVariable("id")
    Long id) {
        ModelAndView mav = new ModelAndView("item/finance");

        // 验证产品ID
        if (Argument.isNotPositive(id)) {
            mav.addObject("errMsg", "提交错误,产品ID为空!");
            mav.setViewName("error/404");
            return mav;
        }
        Item findItem = itemService.getItemById(id);
        // 验证是否是自己创建的产品
        if (findItem == null || !NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            mav.addObject("errMsg", "产品id不存在,请创建!");
            mav.setViewName("error/404");
            return mav;
        }

        mav.addObject("itemId", id);
        mav.addObject("postUrl", "/item/finance");
        return mav;
    }

    @RequestMapping(value = "/finance", method = RequestMethod.POST)
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
        if (findItem == null || !NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            model.addAttribute("errMsg", "产品id不存在,请创建!");
            return "error/404";
        }
        // 设置产品基础信息ID
        itemFinance.setItemId(itemId);
        // 保存融资信息
        itemService.add(itemFinance);
        // 跳转到团队信息编辑页面
        return "redirect:/item/member/" + itemId;
    }

    /**
     * 添加项目背景团队介绍
     * 
     * @return
     */
    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
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
        mav.addObject("postUrl", "/item/success");
        return mav;
    }

    /**
     * @param projectId 项目ID
     * @param teamImg 团队成员头像
     * @param teamName 团队成员姓名
     * @param teamTitle 团队成员职位
     * @param teamIntroduce 团队成员介绍
     * @param ptId 团队成员ID
     * @return
     */
    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ModelAndView member(Long projectId, String teamImg, String teamName, String teamTitle, String teamIntroduce,
                               Long ptId) {
        if (Argument.isNotPositive(projectId)) {
            return createJsonMav(false, 0l, "项目ID为空,提交错误!");
        }
        if (StringUtils.isEmpty(teamTitle)) {
            return createJsonMav(false, 0l, "团队成员职位不能为空!");
        }
        if (StringUtils.isEmpty(teamIntroduce)) {
            return createJsonMav(false, 0l, "团队成员简介不能为空!");
        }
        if (StringUtils.isEmpty(teamName)) {
            return createJsonMav(false, 0l, "团队成员姓名不能为空!");
        }
        if (StringUtils.isEmpty(teamImg)) {
            return createJsonMav(false, 0l, "团队成员头像不能为空!");
        }
        // 验证是否是自己创建的产品
        Item findItem = itemService.getItemById(projectId);
        if (findItem == null) {
            return createJsonMav(false, 0l, "该项目不存在!");
        }
        if (!NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            return createJsonMav(false, 0l, "产品id不存在,请创建!");
        }

        ItemMember itemMember = new ItemMember(projectId, teamName, teamImg, teamTitle, teamIntroduce);
        ObjectUtils.trim(itemMember);
        if (Argument.isNotPositive(ptId)) {
            itemService.add(itemMember);
        } else {
            itemMember.setId(ptId);
            itemService.update(itemMember);
        }
        return createJsonMav(true, itemMember.getId(), "添加成员信息成功!");
    }

    @RequestMapping(value = "/member/delete", method = RequestMethod.POST)
    public ModelAndView member(Long itemId, Long ptId) {
        Parameter query = Parameter.newParameter()//
        .pu("itemId", itemId)//
        .pu("id", ptId);
        ItemMember findItemMember = itemService.findItemMember(query);
        if (findItemMember == null) {
            return createJsonMav(false, 0l, "删除失败!");
        }
        // 验证是否是自己创建的产品
        Item findItem = itemService.getItemById(itemId);
        if (findItem == null) {
            return createJsonMav(false, 0l, "该项目不存在!");
        }
        if (!NumberParser.isEqual(getCurrentUserId(), findItem.getUserId())) {
            return createJsonMav(false, 0l, "产品id不存在,请创建!");
        }

        itemService.deleteItem(ptId);
        return createJsonMav(true, 1l, "添加成员信息成功!");
    }

    /**
     * 添加项目成功页面
     * 
     * @return
     */
    @RequestMapping(value = "/success")
    public ModelAndView success(Long itemId) {
        ModelAndView mav = new ModelAndView("item/success");
        if (itemId == null) {
            mav.addObject("errMsg", "产品id不存在,提交失败!");
            return mav;
        }
        Item itemById = itemService.getItemById(itemId);
        if (itemById == null) {
            mav.addObject("errMsg", "产品不存在,提交失败!");
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
            mav.addObject("errMsg", "未填写完成融资信息,提交失败!");
            return mav;
        }
        List<ItemMember> itemMemberList = itemService.getItemMemberByItemId(itemId);
        if (Argument.isEmpty(itemMemberList)) {
            mav.addObject("errMsg", "未填写完成团队信息,提交失败!");
            return mav;
        }
        // 验证成功,安全提交进入后台审核
        itemService.update(new Item(itemId, VerifyStatusEnum.UNAUDITED));
        return mav;
    }

    // {"isSuccess":true,"code":"2200","info":"保存团队成员信息成功"}
    private ModelAndView createJsonMav(boolean isSuccess, Long code, String info) {
        ModelAndView mav = new ModelAndView();
        mav.setView(new MappingJackson2JsonView());
        mav.addObject("isSuccess", isSuccess);
        mav.addObject("code", code);
        mav.addObject("info", info);
        return mav;
    }
}
