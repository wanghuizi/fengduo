/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.job;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.util.DateViewTools;
import com.fengduo.bee.job.ext.AbstractTask;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.SubHandleStatusEnum;
import com.fengduo.bee.model.cons.SubUserTypeEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.EmailNotify;
import com.fengduo.bee.model.entity.Item;
import com.fengduo.bee.model.entity.ItemFinance;
import com.fengduo.bee.model.entity.UserSub;

/**
 * 对Item表的全盘扫描线程
 * 
 * @author zxc Jul 3, 2015 10:58:30 AM
 */
@Service("itemPhaseJob")
public class ItemPhaseTask extends AbstractTask {

    @Override
    public void doTask() throws Exception {
        // 查询item表: 审核通过且未删除的记录
        Parameter queryItem = Parameter.newParameter()//
        .pu("verifyStatus", VerifyStatusEnum.NORMAL.getValue())//
        .pu("delFlag", DelFlagEnum.UN_DELETE.getValue());

        List<Item> itemList = itemService.listItem(queryItem);
        if (Argument.isEmpty(itemList)) {
            return;
        }
        for (Item item : itemList) {
            if (item == null) {
                continue;
            }
            Long itemId = item.getId();
            if (ProgressEnum.UNSTART.getValue() == item.getProgress()
                || ProgressEnum.RESERVATION.getValue() == item.getProgress()
                || ProgressEnum.SUCCESS.getValue() == item.getProgress()
                || ProgressEnum.FAILURE.getValue() == item.getProgress()) {
                continue;
            }
            // 审核通过日期
            Date verifyDate = item.getVerifyDate();
            if (verifyDate == null) {
                continue;
            }
            // 判断是否符合进入基石投资期时间
            if (DateViewTools.isSameDay(verifyDate,
                                        DateViewTools.getDateBefore(ProgressEnum.CORNERSTONE.getIntervalDays()))) {
                item.setProgress(ProgressEnum.CORNERSTONE.getValue());
                itemService.update(item);
            }
            // 判断是否符合进入预售期时间
            if (DateViewTools.isSameDay(verifyDate, DateViewTools.getDateBefore(ProgressEnum.PRESELL.getIntervalDays()))) {
                // 先查认购表有无基石投资者
                // 如果没有:筹资失败,如果有:更新预售状态(progress)
                Parameter map = Parameter.newParameter()//
                .pu("itemId", itemId)//
                .pu("userType", SubUserTypeEnum.CORNERSTONE_SUB.getValue())//
                .pu("handleStatus", SubHandleStatusEnum.DEPOSIT_SUCCESS_PAY.getValue());

                UserSub userSub = orderService.findUserSub(map);
                if (userSub == null) {
                    item.setProgress(ProgressEnum.FAILURE.getValue());
                    emailNotifyService.insert(new EmailNotify(itemId, item.getUserId(), "蜂朵网项目众筹进度通知",
                                                              "您发起的项目已筹资失败!没有基石投资者认购,请下次再努力!", 2));
                } else {
                    item.setProgress(ProgressEnum.PRESELL.getValue());
                }
                itemService.update(item);
            }
            // 判断是否符合进入交款期时间
            if (DateViewTools.isSameDay(verifyDate, DateViewTools.getDateBefore(ProgressEnum.PAYING.getIntervalDays()))) {
                // 查询融资信息表:判断已认购额是否大于等于拟融资额
                // 如果是:查询所有跟投人信息,并发送邮件通知跟投人支付全额订单
                // 更新项目状态为交款期
                ItemFinance itemFinance = itemService.getItemFinanceByItemId(itemId);
                Integer amount = itemFinance.getAmount();
                Float realSub = itemFinance.getRealSub();
                if (Argument.isNotPositive(amount) || realSub == null || amount > realSub) {
                    item.setProgress(ProgressEnum.FAILURE.getValue());
                    emailNotifyService.insert(new EmailNotify(itemId, item.getUserId(), "蜂朵网项目众筹进度通知",
                                                              "您发起的项目已筹资失败!实际认购额没有达到拟融资额,请下次再努力!", 2));
                    itemService.update(item);
                    continue;
                }

                Parameter map = Parameter.newParameter()//
                .pu("itemId", itemId)//
                .pu("userType", SubUserTypeEnum.FOLLOW_SUB.getValue())//
                .pu("handleStatus", SubHandleStatusEnum.DEPOSIT_SUCCESS_PAY.getValue());

                List<UserSub> userSubList = orderService.listUserSub(map);
                if (Argument.isEmpty(userSubList)) {
                    item.setProgress(ProgressEnum.FAILURE.getValue());
                    emailNotifyService.insert(new EmailNotify(itemId, item.getUserId(), "蜂朵网项目众筹进度通知",
                                                              "您发起的项目已筹资失败!实际认购额没有达到拟融资额,请下次再努力!", 2));

                    itemService.update(item);
                    continue;
                }
                for (UserSub userSub : userSubList) {
                    if (userSub == null || userSub.getId() == null) {
                        continue;
                    }
                    emailNotifyService.insert(new EmailNotify(itemId, userSub.getId(), "蜂朵网项目认购进度通知",
                                                              "您认购的项目已进入交款期,请在收到邮件三天内支付的认购尾款!", 1));
                }
                item.setProgress(ProgressEnum.PAYING.getValue());
                itemService.update(item);
            }
            // 判断是否符合进入交割期时间
            if (DateViewTools.isSameDay(verifyDate, DateViewTools.getDateBefore(ProgressEnum.TICKET.getIntervalDays()))) {
                // 查询融资信息表:判断实际融资额是否大于等于拟融资额
                // 如果是:查询出发起人信息,并发送邮件通知发起人做股权交接
                // 更新项目状态为成功
                ItemFinance itemFinance = itemService.getItemFinanceByItemId(itemId);
                Integer amount = itemFinance.getAmount();
                Float realPay = itemFinance.getRealPay();
                EmailNotify emailNotify = null;
                if (Argument.isNotPositive(amount) || realPay == null || amount > realPay) {
                    item.setProgress(ProgressEnum.FAILURE.getValue());
                    emailNotify = new EmailNotify(itemId, item.getUserId(), "蜂朵网项目众筹进度通知",
                                                  "您发起的项目已筹资失败!实际融资额没有达到拟融资额,请下次再努力!", 2);
                } else {
                    item.setProgress(ProgressEnum.TICKET.getValue());
                    emailNotify = new EmailNotify(itemId, item.getUserId(), "蜂朵网项目众筹进度通知",
                                                  "您发起的项目已进入交割期,请在收到邮件14天内完成股权交接,然后发送电子版至蜂朵众筹平台,并邮寄纸质版至蜂朵总部!", 2);
                }
                emailNotifyService.insert(emailNotify);
                itemService.update(item);
            }
        }
    }

    @Override
    public void before() throws Exception {

    }
}
