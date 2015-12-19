/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.order;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fengduo.bee.commons.cons.ResultCode;
import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.result.JsonResultUtils.JsonResult;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.OrderStatusEnum;
import com.fengduo.bee.model.cons.OrderTypeEnum;
import com.fengduo.bee.model.cons.PayTypeEnum;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.SubHandleStatusEnum;
import com.fengduo.bee.model.cons.SubUserTypeEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.ItemFull;
import com.fengduo.bee.model.entity.PayOrder;
import com.fengduo.bee.model.entity.UserSub;
import com.fengduo.bee.web.controller.BaseController;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;

/**
 * 订单控制层
 * 
 * @author jie.xu
 * @date 2015年6月17日 下午4:53:00
 */
@Controller
public class OrderController extends BaseController {

    /**
     * 我要领投，我要跟投检查登入、认证
     */
    @RequestMapping("/sub/check")
    @ResponseBody
    public JsonResult checkSub(Integer subType, Long itemId) {
        SubUserTypeEnum st = SubUserTypeEnum.getEnum(subType);
        if (st == null) {
            return JsonResultUtils.error("请求不合法");
        }
        Parameter query = Parameter.newParameter()//
        .pu("itemId", itemId)//
        .pu("userType", SubUserTypeEnum.CORNERSTONE_SUB.getValue())//
        .pu("handleStatus", SubHandleStatusEnum.DEPOSIT_SUCCESS_PAY.getValue());

        UserSub cornerstoneSub = orderService.findUserSub(query);
        Long userId = getCurrentUserId();
        // 检查是否实名认证
        boolean flag = userService.isIdentity(userId);
        if (!flag) {
            return JsonResultUtils.buildJsonResult(ResultCode.NEED_IDENTITY, null, "请先进行实名认证");
        }
        if (st == SubUserTypeEnum.CORNERSTONE_SUB) {
            // 基石投资人
            if (cornerstoneSub == null) {
                return JsonResultUtils.success();
            } else {
                return JsonResultUtils.error("该项目已有基石投资人，无法继续领投");
            }

        } else if (st == SubUserTypeEnum.FOLLOW_SUB) {
            // 跟投人
            if (cornerstoneSub == null) {
                return JsonResultUtils.error("该项目还没有基石投资人，无法跟投");
            } else {
                return JsonResultUtils.success();
            }
        }
        return JsonResultUtils.error("请求不合法");
    }

    /**
     * 跳到认购页
     */
    @RequestMapping(value = "/sub/index")
    public String gotoSub(Long itemId, Integer subType, Model model) {
        if (itemId == null || subType == null) {
            return "redirect:/";
        }

        Parameter query = Parameter.newParameter()//
        .pu("itemId", itemId)//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());

        ItemFull itemFull = itemService.findItemFull(query);
        if (itemFull == null) {
            return "redirect:/";
        }

        float subProgress = (itemFull.getRealSub() / itemFull.getAmount()) * 100;
        model.addAttribute("itemFull", itemFull);
        model.addAttribute("subType", subType);
        model.addAttribute("subProgress", subProgress);
        return "sub/index";
    }

    /**
     * 认购
     * 
     * @param subType :1.基石投资者，2.跟投人
     * @param subAmount 认购金额
     * @param advances 保证金金额
     * @param handlingCost 投资人手续费
     */
    @RequestMapping(value = "/sub/add", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult sub(Long itemId, Integer subType, Float subAmount, Float advances, Float handlingCost) {
        if (itemId == null || subType == null || subAmount == null) {
            return JsonResultUtils.error("参数不合法！");
        }
        if (SubUserTypeEnum.getEnum(subType) == null) {
            return JsonResultUtils.error("参数不合法！");
        }
        if (subType == SubUserTypeEnum.FOLLOW_SUB.getValue()) {
            if (advances == null) {
                return JsonResultUtils.error("缺少保证金金额");
            }
        }

        ShiroUser currentUser = getCurrentUser();
        // 当前用户是否实名认证
        if (!currentUser.isIdentity()) {
            return JsonResultUtils.error("您还未进行实名认证！");
        }

        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return JsonResultUtils.error("项目不存在!");
        }
        // 判断产品状态--审核
        if (!VerifyStatusEnum.isNormal(item.getVerifyStatus())) {
            return JsonResultUtils.error("项目未通过审核!");
        }
        if (!ProgressEnum.isLegalSubProgress(item.getProgress(), subType)) {
            return JsonResultUtils.error("项目不在合法认购期中");
        }
        ItemFinance itemFinance = itemService.getItemFinanceByItemId(itemId);
        if (itemFinance != null) {
            // 判断产品是否已融资满
            if (itemFinance.isReachAmount()) {
                return JsonResultUtils.error("项目已达到融资额");
            }
        }
        float fold = subAmount / itemFinance.getPerStock();
        if ((fold - Math.floor(fold)) > 0) {
            return JsonResultUtils.error("请输入每份投资额的整数倍");
        }
        if (subAmount >= itemFinance.getAmount()) {
            return JsonResultUtils.error("认购金额不能大于筹资目标！");
        }
        if (subType == SubUserTypeEnum.CORNERSTONE_SUB.getValue()) {
            if (subAmount < itemFinance.getAmount() * 0.1) {
                return JsonResultUtils.error("基石投资者认购金额至少要融资额的10%");
            }
        }

        // 检查是否重复认购，查询状态未取消
        UserSub existSub = orderService.findUnCloseSub(currentUser.getId(), itemId);
        if (existSub != null) {
            return JsonResultUtils.error("无法进行重复认购");
        }
        // 查询是否有基石投资者认购
        Parameter query = Parameter.newParameter()//
        .pu("itemId", itemId)//
        .pu("userType", SubUserTypeEnum.CORNERSTONE_SUB.getValue())//
        .pu("handleStatus", SubHandleStatusEnum.DEPOSIT_SUCCESS_PAY.getValue());

        UserSub cornerstoneSub = orderService.findUserSub(query);
        // 添加认购记录和订单在同一事务里
        Result subResult = createUserSub(cornerstoneSub, currentUser, subType, subAmount, advances, item, itemFinance);
        if (subResult.isFailed()) {
            return JsonResultUtils.error(subResult.getMessage());
        }

        Result orderResult = createOrder(item, currentUser, subAmount, handlingCost, subType);

        UserSub userSub = (UserSub) subResult.getData();
        PayOrder order = (PayOrder) orderResult.getData();
        boolean flag = orderService.insertSubAndOrder(userSub, order);
        if (flag) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("itemId", itemId);
            dataMap.put("orderId", order.getId());
            dataMap.put("subId", userSub.getId());
            return JsonResultUtils.success(dataMap);
        } else {
            return JsonResultUtils.error("认购失败");
        }
    }

    /**
     * 取消认购
     */
    @RequestMapping("/sub/cancel")
    @ResponseBody
    public JsonResult cancelSub(Long subId, Integer subType, Long itemId) {
        boolean flag = false;
        UserSub sub = orderService.getUserSubById(subId);
        if (sub == null) {
            return JsonResultUtils.error();
        }
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return JsonResultUtils.error();
        }
        if (SubUserTypeEnum.isCornerstoneSub(subType)) {
            if (SubHandleStatusEnum.isDepositNotPay(sub.getHandleStatus())) {
                // 保证金未支付
                flag = orderService.updateSub2Cancel(subId, getCurrentUserId(), OrderStatusEnum.CANCEL_PAY.getValue());

            }
        } else if (SubUserTypeEnum.isFollowSub(subType)) {
            if (SubHandleStatusEnum.isDepositNotPay(sub.getHandleStatus())) {
                // 保证金未支付
                flag = orderService.updateSub2Cancel(subId, getCurrentUserId(), OrderStatusEnum.CANCEL_PAY.getValue());

            } else if (SubHandleStatusEnum.isAllNotPay(sub.getHandleStatus())) {
                // 保证金已支付，且7天内有效，没有退款订单
                // 在预售期
                if (item.getProgress() == ProgressEnum.PRESELL.getValue()) {
                    if (sub.isInRepentTime()) {
                        List<PayOrder> existList = orderService.listRefundOrder(subId, getCurrentUserId());
                        if (existList == null || existList.isEmpty()) {

                            flag = orderService.updateSub2Cancel(subId, getCurrentUserId(),
                                                                 OrderStatusEnum.WAIT_REFUND.getValue());
                        }
                    }
                }
            }
        }
        if (flag) {
            return JsonResultUtils.success();
        } else {
            return JsonResultUtils.error();
        }
    }

    /**
     * 添加认购表记录
     */
    private Result createUserSub(UserSub cornerstoneSub, ShiroUser currentUser, Integer subType, Float subAmount,
                                 Float advances, Item item, ItemFinance itemFinance) {
        Long itemId = item.getId();
        UserSub sub = new UserSub();

        if (subType == SubUserTypeEnum.CORNERSTONE_SUB.getValue()) {
            // 基石投资者
            if (!item.isInCornerstoneTime()) {
                return Result.failed("当前时期基石投资人无法进行认购");
            }
            if (cornerstoneSub != null) {
                return Result.failed("该项目已有基石投资者认投。");
            }
            sub.setAdvances(subAmount);
        } else if (subType == SubUserTypeEnum.FOLLOW_SUB.getValue()) {
            // 跟投人
            if (!item.isInPresell()) {
                return Result.failed("当前时期跟投人无法进行认购");
            }
            if (cornerstoneSub == null) {
                return Result.failed("该项目没有基石投资者认投，无法进行认购。");
            }
            sub.setAdvances(advances);

        }
        sub.setItemId(itemId);
        sub.setUserId(currentUser.getId());
        sub.setUserType(subType);
        sub.setRealName(currentUser.getUser().getRealName());
        sub.setAvatar(currentUser.getUser().getAvatar());
        sub.setSubAmount(subAmount);
        sub.setPayStart(item.getPayAllStartDate(subType));
        sub.setHandleStatus(SubHandleStatusEnum.DEPOSIT_NOT_PAY.getValue());
        float percent = (subAmount / itemFinance.getPerStock()) * itemFinance.getPerPercent();
        sub.setPercent(percent);
        return Result.success(sub);
    }

    /**
     * 添加订单记录
     */
    private Result createOrder(Item item, ShiroUser currentUser, Float subAmount, Float handlingCost, Integer subType) {
        PayOrder order = new PayOrder();
        order.setOrderNo(orderService.generateOrderNo());
        order.setItemId(item.getId());
        order.setUserId(currentUser.getId());
        order.setAmount(subAmount);
        order.setHandlingCost(handlingCost);
        order.setStatus(OrderStatusEnum.NON_PAY.getValue());
        order.setItemName(item.getName());
        order.setItemLogo(item.getImg());
        if (subType == SubUserTypeEnum.CORNERSTONE_SUB.getValue()) {
            order.setType(OrderTypeEnum.CASH_ALL.getValue());
            order.setSubUserType(SubUserTypeEnum.CORNERSTONE_SUB.getValue());
        } else if (subType == SubUserTypeEnum.FOLLOW_SUB.getValue()) {
            order.setType(OrderTypeEnum.CASH_DEPOSIT.getValue());
            order.setSubUserType(SubUserTypeEnum.FOLLOW_SUB.getValue());
        }

        return Result.success(order);
    }

    /**
     * 预定产品
     * 
     * @return
     */
    @RequestMapping(value = "/gosub", method = RequestMethod.POST)
    public String sub() {

        // 根据itemId查询产品基础信息(x)，
        // 判断产品是否存在，判断产品是否已通过审核，判断产品信息是否完整(x)

        // 判断产品是否在非预热期,非交割期(x)

        // 判断产品是否已融资满，判断产品是否已成功融资(x)

        // 判断当前登录用户是否具有投资人权限(x)

        // 判断当前登录用户的投资类型(1.基石投资者，2.跟投人)

        // 基石投资者预定
        // 判断当前时间是否在容许的基石投资人进入期的两周内(基于产品审核通过日期)(x)
        // 基石投资者(在认购表中插入基石投资人投资认购记录，同时生成基石投资人认购订单)(x)
        // 支付时间判断，3个工作日内支付，否则无法支付，订单状态改成支付过期(后台线程扫描)
        // 支付回调：基石投资者认购订单全额支付成功，修改产品融资信息表(实际已融资金额，实际认购金融,认购人数)
        // ps:更新认购人数--->统计认购表状态值为2的
        // 线下运营回调：生成运营人员操作记录存在支付表中
        // 修改订单表状态，修改认购表状态，成功认购日期

        // 跟投人预定
        // 判断是否有基石投资者(x)
        // 判断当前时间是否在容许的产品预售期内,2个月内(x)
        // 跟投人(在认购表中插入跟投人投资认购记录(仅保证金)，同时生成跟投人认购订单(仅保证金))(x)
        // 支付时间判断，3个工作日内支付，否则无法支付，订单状态改成支付过期(后台线程扫描)
        // 线上支付回调：跟投人认购订单保证金支付成功，修改产品融资信息表(实际已融资金额，实际认购金融,认购人数)
        // ps:更新认购人数--->统计认购表状态值为2的,支付余额款成功时不更新
        // 线下运营回调：生成运营人员操作记录存在支付表中
        // 修改订单表状态，修改认购表状态，成功认购日期

        // 付款期
        // 跟投人付款
        // 先判断认购表是否有认购记录
        // 判断认购是否成功，是否已交保证金
        // 判断是否在容许的付款期(项目失败或者已成功或者还在预售期不可支付)
        // 判断融资额是否已经超过需要的融资额

        // 付款期的付款操作流程
        // 跟投人--> 认购表支付开始日期，认购状态-->已付保证金，未全额支付-->生成未全额付款的待支付订单,同时生成支付截止日期
        // 支付回调：支付表的状态更新，认购表状态更改，产品融资信息表，

        // 后台扫描线程Job
        // 60天后扫描项目是否融资成功，通过查看产品融资信息表
        //
        // 融资成功--> 股权变更表生成记录

        // 股权交接完毕--> 产品基础信息表筹资状态更改
        return "item/add";
    }

    /**
     * 跳到订单支付页
     * 
     * @param itemId 项目id
     * @param orderId 订单id
     * @param subId 认购id
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    @RequestMapping(value = "/order/index")
    public String gotoOrder(Long itemId, Long orderId, Long subId, Model model) {
        Result result = orderInfos(itemId, orderId, subId);
        if (result.isFailed()) {
            return "redirect:/?errMsg=" + URLEncoder.encode(result.getMessage());
        }
        model.addAllAttributes((Map<String, Object>) result.getData());
        return "order/index";

    }

    private Result orderInfos(Long itemId, Long orderId, Long subId) {
        if (Argument.isNotPositive(itemId) || Argument.isNotPositive(orderId) || Argument.isNotPositive(subId)) {
            return Result.failed("参数不合法");
        }
        Long userId = getCurrentUserId();
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return Result.failed("项目不存在");
        }
        UserSub sub = orderService.getUserSubById(subId);
        if (sub == null) {
            return Result.failed("认购记录不存在");
        }
        // 检查orderId，subId 是否是本人自己认购的
        // 检查订单状态
        // 订单状态合法：未支付，支付失败
        if (sub.getUserId().longValue() != userId.longValue()) {
            return Result.failed("请求不合法");
        }

        PayOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.failed("订单不存在");
        }
        if (order.getUserId().longValue() != userId.longValue()) {
            return Result.failed("请求不合法");
        }
        if (!order.isInValidPayTime()) {
            return Result.failed("3天支付期已过，无法进行支付");
        }
        if ((order.getStatus() != OrderStatusEnum.NON_PAY.getValue())
            && (order.getStatus() != OrderStatusEnum.FAIL_PAY.getValue())) {
            return Result.failed("请求不合法");
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("item", item);
        model.put("sub", sub);
        model.put("order", order);
        return Result.success(model);
    }

    /**
     * 跳到线下支付确定页
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/order/offline")
    public String gotoOffline(Long itemId, Long orderId, Long subId, Model model) {
        Result result = orderInfos(itemId, orderId, subId);
        if (result.isFailed()) {
            return "redirect:/";
        }
        model.addAllAttributes((Map<String, Object>) result.getData());
        return "order/offline";
    }

    /**
     * <pre>
     * 线下支付确定完成操作
     * 更新订单支付的类型：线下
     * </pre>
     */
    @RequestMapping(value = "/order/offline/submit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult offlineSubmit(Long orderId) {
        PayOrder order = orderService.getOrderById(orderId);
        if (order == null) {
            return JsonResultUtils.error("不存在该订单");
        }
        if ((order.getStatus() != OrderStatusEnum.NON_PAY.getValue())
            && (order.getStatus() != OrderStatusEnum.FAIL_PAY.getValue())) {
            return JsonResultUtils.error("非未支付订单");
        }

        PayOrder updateOrder = new PayOrder();
        updateOrder.setId(orderId);
        updateOrder.setPayType(PayTypeEnum.OFFLINE.getValue());
        boolean flag = orderService.updateOrderById(updateOrder);
        if (flag) {
            return JsonResultUtils.success();
        } else {
            return JsonResultUtils.error();
        }
    }

    /**
     * <pre>
     * 跟投人去支付余下的款
     * 项目在付款期
     * </pre>
     */
    @RequestMapping("/order/payRemain")
    @ResponseBody
    public JsonResult payRemainAmount(Long subId, Long itemId) {
        if (Argument.isNotPositive(subId) || Argument.isNotPositive(itemId)) {
            return JsonResultUtils.error("参数不合法");
        }
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return JsonResultUtils.error("项目不存在");
        }
        UserSub sub = orderService.getUserSubById(subId);
        if (sub == null) {
            return JsonResultUtils.error("认购记录不存在");
        }
        if (!sub.isInPayRemainTime()) {
            return JsonResultUtils.error("不在支付期内");
        }
        float subAmount = sub.getSubAmount() - sub.getAdvances();
        // TODO:jie.xu手续费比例
        float handlingCost = subAmount * 0.005f;
        PayOrder order = createRemainOrder(item, getCurrentUserId(), subAmount, handlingCost);
        order = orderService.insertOrder(order);
        if (order.getId() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("itemId", itemId);
            map.put("orderId", order.getId());
            map.put("subId", subId);
            return JsonResultUtils.success(map);
        } else {
            return JsonResultUtils.error();
        }
    }

    private PayOrder createRemainOrder(Item item, Long userId, Float subAmount, Float handlingCost) {
        PayOrder order = new PayOrder();
        order.setOrderNo(orderService.generateOrderNo());
        order.setItemId(item.getId());
        order.setUserId(userId);
        order.setAmount(subAmount);
        order.setHandlingCost(handlingCost);
        order.setStatus(OrderStatusEnum.NON_PAY.getValue());
        order.setItemName(item.getName());
        order.setItemLogo(item.getImg());
        order.setType(OrderTypeEnum.CASH_ALL.getValue());
        order.setSubUserType(SubUserTypeEnum.FOLLOW_SUB.getValue());
        return order;
    }
}
