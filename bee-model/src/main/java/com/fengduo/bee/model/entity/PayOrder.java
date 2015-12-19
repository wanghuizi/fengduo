/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 类PayOrder.java的实现描述：支付订单表
 * 
 * @author jie.xu
 * @date 2015年6月16日 下午4:24:28
 */
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 5698001308892300726L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private String            orderNo;                                // 订单编号
    private Long              itemId;                                 // 产品id
    private Long              userId;                                 // 支付者id
    private Long              subId;                                  // 认购表id
    private Integer           subUserType;                            // 认购人类型
    private Integer           type;                                   // 类型：1：保证金 2：全额 3：退款保证金 4，退款全额
    private Float             amount;                                 // 订单金额
    private Float             handlingCost;                           // 投资人手续费
    private Float             platformCost;                           // 平台手续费
    private Integer           bankType;                               // 银行类型
    private Integer           status;                                 // 支付类型：0：未支付，1：支付成功，2：支付失败，3：取消支付，4：退款成功，5：退款失败'
    private Date              dealDate;                               // 交易完成时间
    private String            itemName;                               // 项目名称
    private String            itemLogo;                               // 项目缩略图
    private Integer           payType;                                // 支付类型：1：线上支付 2：线下支付
    private Long              operatorId;                             // 操作者id
    private Date              operateDate;                            // 操作时间

    public PayOrder() {

    }

    /**
     * 构造用户ID,认购ID,支付状态的PayOrder对象
     * 
     * @param userId
     * @param subId
     * @param orderStatus
     */
    public PayOrder(Long userId, Long subId, Integer orderStatus) {
        setUserId(userId);
        setSubId(subId);
        setStatus(orderStatus);
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getHandlingCost() {
        return handlingCost;
    }

    public void setHandlingCost(Float handlingCost) {
        this.handlingCost = handlingCost;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLogo() {
        return itemLogo;
    }

    public void setItemLogo(String itemLogo) {
        this.itemLogo = itemLogo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Float getPlatformCost() {
        return platformCost;
    }

    public void setPlatformCost(Float platformCost) {
        this.platformCost = platformCost;
    }

    /**
     * 用户实际支付金额
     */
    public Float getActualAmount2User() {
        if (amount == null) {
            amount = 0f;
        }
        if (handlingCost == null) {
            handlingCost = 0f;
        }
        return amount + handlingCost;
    }

    public Integer getSubUserType() {
        return subUserType;
    }

    public void setSubUserType(Integer subUserType) {
        this.subUserType = subUserType;
    }

    /**
     * 是否在3天支付日期范围内
     */
    public boolean isInValidPayTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        cal.add(Calendar.DATE, 3);
        Date now = new Date();
        if (now.getTime() >= createDate.getTime() && now.getTime() <= cal.getTime().getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
