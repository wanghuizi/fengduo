/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fengduo.bee.commons.pagination.PaginationList;
import com.fengduo.bee.commons.pagination.PaginationParser.UrlDefaultIpageUrl;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.entity.ItemFull;
import com.fengduo.bee.model.entity.PayOrder;
import com.fengduo.bee.model.entity.UserSub;
import com.fengduo.bee.web.controller.BaseController;

/**
 * 会员的关于我的页面控制逻辑
 * 
 * @author jie.xu
 * @date 2015年6月21日 下午3:53:16
 */
@Controller
public class MemberController extends BaseController {

    /**
     * 我发起的项目
     */
    @RequestMapping(value = { "/my/launch/{page}" }, method = RequestMethod.GET)
    public String myLaunch(@PathVariable("page")
    Integer page, Model model) {
        Long userId = getCurrentUserId();
        // 构造查询条件
        Parameter query = Parameter.newParameter()//
        .pu("page", page)// 页码
        .pu("pageSize", DEFAULT_PAGESIZE)// 每页多少个
        .pu("userId", userId)//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());

        PaginationList<ItemFull> result = itemService.listPaginationItemFull(query,
                                                                             new UrlDefaultIpageUrl("/my/launch"));

        model.addAttribute("itemList", result);
        model.addAttribute("userName", getCurrentUser().getDisplayName());
        return "member/launch";
    }

    /**
     * 我投资的项目
     */
    @RequestMapping(value = { "/my/invest/{page}" }, method = RequestMethod.GET)
    public String myInvestment(@PathVariable("page")
    Integer page, Model model) {
        Long userId = getCurrentUserId();
        // 构造查询条件
        Parameter query = Parameter.newParameter()//
        .pu("page", page)//
        .pu("pageSize", DEFAULT_PAGESIZE)//
        .pu("userId", userId)//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());

        PaginationList<UserSub> result = orderService.listPaginationUserSub(query, new UrlDefaultIpageUrl("/my/invest"));

        Parameter map = Parameter.newParameter()//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
        for (UserSub sub : result) {
            long itemId = sub.getItemId();
            map.put("itemId", itemId);
            sub.setItemFull(itemService.findItemFull(map));
            List<PayOrder> existList = orderService.listRefundOrder(sub.getId(), getCurrentUserId());
            if (existList != null && existList.size() > 0) {
                sub.setHasRefundOrder(true);
            }
        }
        model.addAttribute("subList", result);
        return "member/invest";
    }

    /**
     * 我关注的项目
     */
    @RequestMapping(value = { "/my/follow/{page}" }, method = RequestMethod.GET)
    public String myFollow(@PathVariable("page")
    Integer page, Model model) {

        return "member/follow";
    }

    /**
     * 我的订单
     */
    @RequestMapping(value = { "/my/order/{page}" }, method = RequestMethod.GET)
    public String myOrder(@PathVariable("page")
    Integer page, Model model) {
        Long userId = getCurrentUserId();
        // 构造查询条件
        Parameter query = Parameter.newParameter()//
        .pu("page", page)//
        .pu("pageSize", DEFAULT_PAGESIZE)//
        .pu("userId", userId);

        PaginationList<PayOrder> result = orderService.listPaginationPayOrder(query,
                                                                              new UrlDefaultIpageUrl("/my/order"));
        model.addAttribute("orderList", result);
        return "member/order";
    }
}
