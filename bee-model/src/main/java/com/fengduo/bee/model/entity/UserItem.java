/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 类UserItem.java的实现描述：用户和产品关联信息
 * 
 * @author jie.xu
 * @date 2015年6月9日 下午1:05:56
 */
public class UserItem implements Serializable {

    private static final long serialVersionUID = -1548507074741631964L;

    private Long              id;
    private Date              createDate;

    private Long              itemId;
    private Long              userId;
    private Integer           handleType;                              // 操作类型:1=发起,2=投资,3=关注

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

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

}
