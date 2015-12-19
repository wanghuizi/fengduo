/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.util.DateViewTools;
import com.fengduo.bee.job.ext.AbstractTask;
import com.fengduo.bee.model.cons.OrderStatusEnum;
import com.fengduo.bee.model.entity.EmailNotify;
import com.fengduo.bee.model.entity.PayOrder;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.service.impl.message.SmsServiceImpl.SmsProjectEnum;

/**
 * <pre>
 * 类NotifyPaymentTask.java的实现描述：通知付款任务。2天后未支付的订单 插入邮件内容 短信进行通知
 * 只处理生成的未支付的订单，比如跟投人交完保证金，但在交款期中未去触发交余额款操作这种情况不考虑
 * job时间建议下午时间段
 * </pre>
 * 
 * @author jie.xu
 * @date 2015年7月3日 上午11:59:50
 */
@Service("notifyPaymentJob")
public class NotifyPaymentTask extends AbstractTask {

    private static Logger LOG = LoggerFactory.getLogger(NotifyPaymentTask.class);

    @Override
    public void doTask() throws Exception {
        // 查询未支付的订单
        Parameter map = Parameter.newParameter()//
        .pu("status", OrderStatusEnum.NON_PAY.getValue());

        List<PayOrder> list = orderService.listOrder(map);
        if (list == null || list.isEmpty()) {
            LOG.info("查询到未支付的订单个数：0个，时间：{}", DateViewTools.formatFullDate(new Date()));
            return;
        }
        for (final PayOrder order : list) {
            LOG.info("查询到未支付的订单个数：{}个，时间：{}", list.size(), DateViewTools.formatFullDate(new Date()));
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    operate(order);
                }
            });

        }
    }

    /**
     * 真正的符号条件的邮件进行发送操作
     * 
     * @param order
     */
    private void operate(final PayOrder order) {
        Date createDate = order.getCreateDate();
        Long userId = order.getUserId();

        if (DateViewTools.isSameDay(createDate, DateViewTools.getDateBefore(2))) {
            // 当前时间的前2天
            // 插入email 表数据
            EmailNotify emailNotify = new EmailNotify();
            emailNotify.setItemId(order.getItemId());
            emailNotify.setReceiverId(userId);
            emailNotify.setTitle("蜂朵网股权众筹未支付订单通知");
            emailNotify.setHandleType(3);
            emailNotify.setContent("您认购的项目【" + order.getItemName() + "】有笔未支付的订单，订单号【" + order.getOrderNo()
                                   + "】，支付截止还剩1天，请尽快完成支付，逾期将作废！");
            emailNotify = emailNotifyService.insert(emailNotify);
            LOG.info("插入邮件内容，邮件id：{},时间：{}", emailNotify.getId(), DateViewTools.formatFullDate(new Date()));

            // 手机短信通知
            User user = userService.getUserById(userId);
            if (user != null) {
                String phone = user.getPhone();
                // TODO:jie.xu 模板内容
                Map<String, String> msgMap = new HashMap<String, String>();
                smsService.sendMsg(phone, msgMap, SmsProjectEnum.NONPAY.getValue());

            }

        } else if (DateViewTools.isSameDay(createDate, DateViewTools.getDateBefore(4))) {
            // 更新状态为【取消支付】
            LOG.info("该订单支付过期，订单号:{},时间：{}", order.getOrderNo(), DateViewTools.formatFullDate(new Date()));
            PayOrder updateOrder = new PayOrder();
            updateOrder.setId(order.getId());
            updateOrder.setStatus(OrderStatusEnum.CANCEL_PAY.getValue());
            orderService.updateOrderById(updateOrder);
        }
    }

    @Override
    public void before() throws Exception {
        // TODO Auto-generated method stub

    }
}
