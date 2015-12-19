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

/**
 * 产品团队信息表
 * 
 * <pre>
 *     id          bigint        auto_increment,
 *     create_date timestamp not null default 0,
 * 
 *     item_id     bigint  not null    COMMENT '众筹产品项目id',
 *     name        varchar(255)        COMMENT '姓名',
 *     avatar      varchar(255)        COMMENT '头像',
 *     title       varchar(255)        COMMENT '职位',
 *     memo        varchar(255)        COMMENT '简介',
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:06:56 PM
 */
public class ItemMember implements Serializable {

    private static final long serialVersionUID = -7303997284119682388L;

    private Long              id;
    private Date              createDate;

    private Long              itemId;

    private String            name;
    private String            avatar;
    private String            title;
    private String            memo;

    public ItemMember() {

    }

    public ItemMember(Long itemId, String name, String avatar, String title, String mome) {
        setItemId(itemId);
        setName(name);
        setAvatar(avatar);
        setTitle(title);
        setMemo(mome);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAvatar() {
        return StringUtils.isEmpty(avatar) ? "/images/defaultUser1.png" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
