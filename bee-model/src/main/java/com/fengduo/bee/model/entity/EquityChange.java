/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fengduo.bee.model.cons.DelFlagEnum;

/**
 * 发起人做股权转让交接记录信息表
 * 
 * <pre>
 *     id               bigint not null auto_increment,
 *     create_date     timestamp not null default 0,
 *     update_date     timestamp not null default 0,
 * 
 *     item_id         bigint  not null    COMMENT '众筹产品项目id',
 *     user_id         bigint  not null    COMMENT '用户id,发起人id',
 *     amount          float               COMMENT '已到达平台托管账户的融资金额(单位是万元)',
 *     success_date    timestamp           COMMENT '融资成功时间',
 *     deadline        timestamp           COMMENT '股权变更提交截止日期',
 *     equity_url      varchar(1024)       COMMENT '上传的电子版股权变更书url路径',
 *     is_receive      int(2)              COMMENT '平台是否接收到发起人邮寄的纸质版股权转让通知书: 0=尚未接收到,1=已接收到',
 *     operator_id     bigint              COMMENT '平台操作者id',
 *     operator_date   timestamp           COMMENT '平台操作时间',
 *     del_flag        int(2)              COMMENT '逻辑删除:0=正常;1=删除',
 * </pre>
 * 
 * @author zxc Jun 16, 2015 5:05:34 PM
 */
public class EquityChange implements Serializable {

    private static final long serialVersionUID = -2638214010924392943L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Long              itemId;
    private Long              userId;

    private Float             amount;
    private Date              successDate;
    private Date              deadline;
    private String            equityUrl;
    private Integer           isReceive;
    private Long              operatorId;
    private Date              operatorDate;
    private Integer           delFlag;

    public EquityChange() {

    }

    public EquityChange(Long id, DelFlagEnum delFlagEnum) {
        setId(id);
        setDelFlag(delFlagEnum.getValue());
    }

    public Long getId() {
        return id;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getEquityUrl() {
        return equityUrl;
    }

    public void setEquityUrl(String equityUrl) {
        this.equityUrl = equityUrl;
    }

    public Integer getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperatorDate() {
        return operatorDate;
    }

    public void setOperatorDate(Date operatorDate) {
        this.operatorDate = operatorDate;
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
}
