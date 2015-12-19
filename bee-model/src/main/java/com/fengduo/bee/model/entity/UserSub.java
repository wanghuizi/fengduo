/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.SubHandleStatusEnum;
import com.fengduo.bee.model.cons.SubUserTypeEnum;

/**
 * 投资人认购信息表
 * 
 * <pre>
 *     id                  bigint not null auto_increment,
 *     create_date         timestamp not null default 0,
 *     update_date         timestamp not null default 0,
 *     
 *     item_id             bigint  not null    COMMENT '众筹产品项目id',
 *     user_id             bigint  not null    COMMENT '用户id',
 *     user_type           int(2)              COMMENT '投资者类型(类型区分基石投资人和跟投人)',
 *     real_name           varchar(255)        COMMENT '真实姓名',
 *     avatar              varchar(255)        COMMENT '头像',
 *     sub_amount          float               COMMENT '认购金额(单位是万元)',
 *     advances            float               COMMENT '保证金金额(单位是万元)(已认购金额=保证金+剩余待全额支付金额)',
 *     percent             float                COMMENT '股份占有百分比(单位%)',
 *     sub_date            timestamp           COMMENT '认购成功时间(保证金支付成功时间)',
 *     pay_start           timestamp           COMMENT '全额支付开始日期',
 *     memo                varchar(255)        COMMENT '备注',
 *     handle_status       int(2)              COMMENT '状态: 1=保证金尚未支付,2=保证金已经支付成功,3=关闭，4=全额付款未支付，5=全额付款成功
 *     del_flag            int(2)              COMMENT '逻辑删除:0=正常;1=删除',
 * 
 * handle_status:领投人全额付完时状态为2，跟投人保证金交完时状态为4
 * </pre>
 * 
 * @author zxc Jun 16, 2015 5:03:56 PM
 */
public class UserSub implements Serializable {

    private static final long serialVersionUID = 8818530729026571366L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Long              itemId;
    private Long              userId;

    private Integer           userType;
    private String            realName;
    private String            avatar;
    private Float             subAmount;
    private Float             advances;
    private Float             percent;                                // 股份占有百分比(单位%)
    private Date              subDate;
    private Date              payStart;
    private String            memo;
    private Integer           handleStatus;
    private Integer           delFlag;

    private ItemFull          itemFull;
    private boolean           hasRefundOrder;                         // true:有退款单，false:没有退款单

    public UserSub() {

    }

    /**
     * 构造预定ID和删除状态的UserSub对象
     * 
     * @param id
     * @param subHandleStatusEnum
     */
    public UserSub(Long id, DelFlagEnum delFlagEnum) {
        setId(id);
        setDelFlag(delFlagEnum.getValue());
    }

    /**
     * 构造预定ID和预定状态的UserSub对象
     * 
     * @param id
     * @param subHandleStatusEnum
     */
    public UserSub(Long id, SubHandleStatusEnum subHandleStatusEnum) {
        setId(id);
        setHandleStatus(subHandleStatusEnum.getValue());
    }

    public Long getId() {
        return id;
    }

    public Float getAdvances() {
        return advances;
    }

    public void setAdvances(Float advances) {
        this.advances = advances;
    }

    public Date getPayStart() {
        return payStart;
    }

    public void setPayStart(Date payStart) {
        this.payStart = payStart;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return StringUtils.isEmpty(avatar) ? "/images/defaultUser1.png" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Float getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(Float subAmount) {
        this.subAmount = subAmount;
    }

    public Date getSubDate() {
        return subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public ItemFull getItemFull() {
        return itemFull;
    }

    public void setItemFull(ItemFull itemFull) {
        this.itemFull = itemFull;
    }

    public boolean isHasRefundOrder() {
        return hasRefundOrder;
    }

    public void setHasRefundOrder(boolean hasRefundOrder) {
        this.hasRefundOrder = hasRefundOrder;
    }

    /**
     * 是否是基石投资人认购类型
     */
    public boolean isCornerstoneSub() {
        return SubUserTypeEnum.isCornerstoneSub(userType);
    }

    /**
     * 是否是跟投人认购类型
     */
    public boolean isFollowSub() {
        return SubUserTypeEnum.isFollowSub(userType);
    }

    /**
     * 在7天反悔期内
     */
    public boolean isInRepentTime() {
        if (subDate == null) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(subDate);
        cal.add(Calendar.DATE, 7);
        Date now = new Date();
        if (now.getTime() >= subDate.getTime() && now.getTime() <= cal.getTime().getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否在支付余额款时间内
     */
    public boolean isInPayRemainTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(payStart);
        cal.add(Calendar.DATE, ProgressEnum.PAYING.getDays());
        Date now = new Date();
        if (now.getTime() >= payStart.getTime() && now.getTime() <= cal.getTime().getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
