/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fengduo.bee.model.cons.DelFlagEnum;

/**
 * 产品评论信息表
 * 
 * <pre>
 *     id                  bigint not null auto_increment,
 *     create_date         timestamp not null default 0,
 *     update_date         timestamp not null default 0,
 *     
 *     item_id             bigint  not null    COMMENT '众筹产品项目id',
 *     user_id             bigint  not null    COMMENT '用户id,评论者id',
 *     user_name           varchar(255)        COMMENT '评论者名称',
 *     avatar              varchar(255)        COMMENT '评论者头像',
 *     context             varchar(2048)       COMMENT '评论的具体内容(144个字数限制)',
 *     parent_id           bigint              COMMENT '父评论id',
 *     parent_context      varchar(2048)       COMMENT '父评论内容',
 *     comment_type        int(2)              COMMENT '评论类型:0=评论;1=回复',
 *     del_flag            int(2)              COMMENT '逻辑删除:0=正常;1=删除',
 * </pre>
 * 
 * @author zxc Jun 17, 2015 4:29:20 PM
 */
public class ItemComment implements Serializable {

    private static final long serialVersionUID = -8196737792676180534L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Long              itemId;
    private Long              userId;

    private String            userName;
    private String            avatar;
    private String            context;
    private Long              parentId;
    private String            parentContext;
    private Integer           commentType;
    private Integer           delFlag;

    public ItemComment() {

    }

    public ItemComment(Long itemId, Long userId, String userName, String avatar, String context) {
        this(itemId, userId, userName, avatar, context, null, null, 0);
    }

    public ItemComment(Long itemId, Long userId, String userName, String avatar, String context, Long parentId,
                       String parentContext) {
        this(itemId, userId, userName, avatar, context, parentId, parentContext, 1);
    }

    public ItemComment(Long itemId, Long userId, String userName, String avatar, String context, Long parentId,
                       String parentContext, Integer commentType) {
        setItemId(itemId);
        setUserId(userId);
        setUserName(userName);
        setAvatar(avatar);
        setContext(context);
        setParentId(parentId);
        setParentContext(parentContext);
        setCommentType(commentType);
    }

    public ItemComment(Long id, DelFlagEnum delFlagEnum) {
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

    public String getUserName() {
        return userName;
    }

    public String getParentContext() {
        return parentContext;
    }

    public void setParentContext(String parentContext) {
        this.parentContext = parentContext;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return StringUtils.isEmpty(avatar) ? "/images/defaultUser1.png" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
