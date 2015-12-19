/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.web.controller.product;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fengduo.bee.commons.cons.ResultCode;
import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.IPageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.result.JsonResultUtils.JsonResult;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.dto.ItemDTO;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemComment;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemFull;
import com.fengduo.bee.model.entity.ItemMember;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.model.entity.UserSub;
import com.fengduo.bee.web.controller.BaseController;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;

/**
 * 众筹产品列表(筛选项),众筹产品详情
 * 
 * @author zxc May 28, 2015 11:54:13 PM
 */
@Controller
public class ProductController extends BaseController {

    /**
     * 我要下载pdf 检查登入、认证
     */
    @RequestMapping("/download/check")
    @ResponseBody
    public JsonResult checkDownload(Long itemId) {
        if (Argument.isNotPositive(itemId)) {
            return JsonResultUtils.error("请求不合法,产品参数为空!");
        }
        ShiroUser user = getCurrentUser();
        if (user == null) {
            return JsonResultUtils.buildJsonResult(ResultCode.NEED_LOGIN, null, "请先登录平台");
        }
        if (!user.isIdentity()) {
            return JsonResultUtils.buildJsonResult(ResultCode.NEED_IDENTITY, null, "请先进行实名认证");
        }

        Parameter query = Parameter.newParameter()//
        .pu("itemId", itemId)//
        .pu("verifyStatus", VerifyStatusEnum.NORMAL.getValue())//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
        Item findItem = itemService.findItem(query);
        if (findItem == null) {
            return JsonResultUtils.error("请求不合法,产品不存在!");
        }
        return JsonResultUtils.success();
    }

    /**
     * 生成下载pdf链接地址
     * 
     * @return
     */
    @RequestMapping(value = "/item/{id}/downloadPdf")
    @ResponseBody
    public JsonResult downloadpdf(@PathVariable("id")
    Long id) {
        if (Argument.isNotPositive(id)) {
            return JsonResultUtils.error("请求不合法,产品参数为空!");
        }
        ItemFinance itemFinance = itemService.getItemFinanceByItemId(id);
        if (itemFinance == null) {
            return JsonResultUtils.error("请求不合法,产品不存在!");
        }
        String url = itemFinance.getPdfUrl();
        return JsonResultUtils.success(url);
    }

    /**
     * 实际下载pdf的流
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping("/item/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable("id")
    Long id) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "商业计划书.pdf");
        String name = "商业计划书";
        name = new String(name.getBytes(), "ISO8859-1");
        headers.set("content-disposition", "attachment;filename=" + name + ".pdf");

        if (Argument.isNotPositive(id)) {
            return new ResponseEntity<byte[]>(null, headers, HttpStatus.CREATED);
        }
        ItemFinance itemFinance = itemService.getItemFinanceByItemId(id);
        if (itemFinance == null) {
            return new ResponseEntity<byte[]>(null, headers, HttpStatus.CREATED);
        }
        String url = itemFinance.getPdfUrl();
        File file = fileService.getFile(url);
        if (file == null) {
            return new ResponseEntity<byte[]>(null, headers, HttpStatus.CREATED);
        }

        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    /**
     * 项目列表页面(带筛选栏)
     * 
     * @return
     */
    @RequestMapping(value = "/projects")
    public ModelAndView list(ModelAndView mav, final ItemDTO itemDTO) {
        mav.setViewName("product/list");

        Parameter query = Parameter.newParameter()// 项目阶段
        .pu("stage", itemDTO.getStage())// 项目状态
        .pu("progress", itemDTO.getProgress())// 项目标签
        .pu("tags", itemDTO.getTag())// 审核状态
        .pu("verifyStatus", VerifyStatusEnum.NORMAL.getValue())// 删除状态
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue())// 页码
        .pu("page", itemDTO.getPage());

        PaginationList<ItemFull> listPaginationItemFull = itemService.listPaginationItemFull(query, new IPageUrl() {

            @Override
            public String parsePageUrl(Object... objs) {
                StringBuffer sf = new StringBuffer("/projects?page=" + (Integer) objs[1]);
                if (itemDTO.getTag() != null) {
                    sf.append("&tag=" + itemDTO.getTag());
                }
                if (itemDTO.getProgress() != null) {
                    sf.append("&progress=" + itemDTO.getProgress());
                }
                if (itemDTO.getStage() != null) {
                    sf.append("&stage=" + itemDTO.getStage());
                }
                return sf.toString();
            }
        });
        mav.addObject("itemFullList", listPaginationItemFull);
        mav.addObject("tag", itemDTO.getTag());
        mav.addObject("progress", itemDTO.getProgress());
        mav.addObject("stage", itemDTO.getStage());
        return mav;
    }

    /**
     * 众筹产品项目详情
     * 
     * @return
     */
    @RequestMapping(value = "/{id}/project")
    public ModelAndView detail(@PathVariable("id")
    Long id, ModelAndView mav) {
        mav.setViewName("product/detail");
        if (Argument.isNotPositive(id)) {
            return mav;
        }

        Parameter query = Parameter.newParameter().pu("itemId", id);
        // 查询产品基础信息与融资信息
        ItemFull itemFull = itemService.findItemFull(query);
        if (itemFull == null) {
            return mav;
        }
        // 查询产品团队信息
        List<ItemMember> itemMemberList = itemService.getItemMemberByItemId(id);
        // 查询发起人信息
        User itemUser = userService.getUserById(itemFull.getUserId());

        mav.addObject("itemFull", itemFull);
        mav.addObject("itemMemberList", itemMemberList);
        mav.addObject("itemUser", itemUser);
        mav.addObject("id", id);
        return mav;
    }

    /**
     * 众筹产品讨论详情
     * 
     * @return
     */
    @RequestMapping(value = "/{id}/topic")
    public ModelAndView topic(@PathVariable("id")
    Long id, ModelAndView mav) {
        mav.setViewName("product/topic");
        if (Argument.isNotPositive(id)) {
            return mav;
        }

        Parameter query = Parameter.newParameter().pu("itemId", id);
        List<ItemComment> listItemComment = itemService.listItemComment(query);
        ItemFull itemFull = itemService.findItemFull(query);
        if (itemFull == null) {
            return mav;
        }
        User itemUser = userService.getUserById(itemFull.getUserId());

        mav.addObject("itemFull", itemFull);
        mav.addObject("itemUser", itemUser);
        mav.addObject("listItemComment", listItemComment);
        mav.addObject("id", id);
        return mav;
    }

    /**
     * 众筹产品投资人详情
     * 
     * @return
     */
    @RequestMapping(value = "/{id}/investor")
    public ModelAndView investorList(@PathVariable("id")
    Long id, ModelAndView mav) {
        mav.setViewName("product/investor");
        if (Argument.isNotPositive(id)) {
            return mav;
        }

        Parameter query = Parameter.newParameter().pu("itemId", id);
        ItemFull itemFull = itemService.findItemFull(query);
        if (itemFull == null) {
            return mav;
        }
        List<UserSub> listUserSub = orderService.listUserSub(query);
        User itemUser = userService.getUserById(itemFull.getUserId());

        mav.addObject("itemFull", itemFull);
        mav.addObject("itemUser", itemUser);
        mav.addObject("listUserSub", listUserSub);
        mav.addObject("id", id);
        return mav;
    }
}
