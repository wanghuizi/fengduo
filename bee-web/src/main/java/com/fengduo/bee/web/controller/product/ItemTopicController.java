/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.product;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.result.JsonResultUtils.JsonResult;
import com.fengduo.bee.commons.util.ObjectUtils;
import com.fengduo.bee.commons.util.StringFormatter;
import com.fengduo.bee.commons.util.StringUtils;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemComment;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.web.controller.BaseController;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;

/**
 * 众筹产品项目讨论
 * 
 * @author zxc Jun 12, 2015 2:15:38 PM
 */
@Controller
@RequestMapping(value = "/item")
public class ItemTopicController extends BaseController {

    /**
     * 产品评论
     * 
     * @return
     */
    @RequestMapping(value = "/comment", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult comment(String context, Long itemId) {
        ObjectUtils.trim(context);
        StringEscapeUtils.escapeJavaScript(context);
        if (Argument.isNotPositive(itemId)) {
            return JsonResultUtils.error("传入参数错误!产品ID不可以为空!");
        }
        if (StringUtils.isEmpty(context)) {
            return JsonResultUtils.error("传入参数错误!评论内容为空!");
        }
        float contextSize = StringFormatter.getWordSize(context);
        if (contextSize > 140) {
            return JsonResultUtils.error("您发布的评论过长，请重新发布,超了" + (contextSize - 140) + "个字");
        }
        ShiroUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return JsonResultUtils.error("登录信息不存在!");
        }
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return JsonResultUtils.error("传入参数错误!产品记录不存在!");
        }

        User user = currentUser.getUser();

        itemService.add(new ItemComment(itemId, user.getId(), user.getNick(), user.getAvatar(), context));
        return JsonResultUtils.success("评论成功!");
    }

    /**
     * 评论回复
     * 
     * @return
     */
    @RequestMapping(value = "/reply", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult reply(String context, Long itemId, Long parentId) {
        ObjectUtils.trim(context);
        StringEscapeUtils.escapeJavaScript(context);
        if (Argument.isNotPositive(itemId)) {
            return JsonResultUtils.error("传入参数错误!产品ID不可以为空!");
        }
        if (Argument.isNotPositive(parentId)) {
            return JsonResultUtils.error("传入参数错误!产品ID不可以为空!");
        }
        if (StringUtils.isEmpty(context)) {
            return JsonResultUtils.error("传入参数错误!回复内容为空!");
        }
        float contextSize = StringFormatter.getWordSize(context);
        if (contextSize > 140) {
            return JsonResultUtils.error("您发布的评论过长，请重新发布,超了" + (contextSize - 140) + "个字");
        }
        ShiroUser currentUser = getCurrentUser();
        if (currentUser == null) {
            return JsonResultUtils.error("登录信息不存在!");
        }
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return JsonResultUtils.error("传入参数错误!产品记录不存在!");
        }
        ItemComment itemComment = itemService.getItemCommentById(parentId);
        if (itemComment == null) {
            return JsonResultUtils.error("传入参数错误!回应的评论不存在!");
        }

        User user = currentUser.getUser();

        itemService.add(new ItemComment(itemId, user.getId(), user.getDisplayName(), user.getAvatar(), context,
                                        itemComment.getId(), itemComment.getContext()));
        return JsonResultUtils.success("回复成功!");
    }
}
