/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 类EmailNotify.java的实现描述：邮件提醒对象
 * 
 * @author jie.xu
 * @date 2015年6月16日 下午7:39:51
 */
public class EmailNotify implements Serializable {

    private static final long serialVersionUID = 4429691439205163798L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Integer           status;                                 // 0:未发送、发送失败，1：发送成功
    private Long              itemId;                                 // 产品id
    private Long              receiverId;                             // 接收者id
    private Long              operatorId;                             // 平台操作者id

    // 用途类型:1=发送邮件通知跟投人支付全额订单;2=发送邮件通知发起人做股权交接;
    // 3=通知订单未支付的用户
    private Integer           handleType;
    private String            title;                                  // 标题
    private String            content;                                // 内容
    private String            memo;                                   // 备注

    public EmailNotify() {

    }

    public EmailNotify(Long itemId, Long receiverId, String title, String content, Integer handleType) {
        setItemId(itemId);
        setReceiverId(receiverId);
        setTitle(title);
        setContent(content);
        setHandleType(handleType);
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

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
