/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 类Withdraw.java的实现描述：提现表
 * 
 * @author jie.xu
 * @date 2015年6月16日 下午7:30:36
 */
public class Withdraw implements Serializable {

    private static final long serialVersionUID = -9004476308358737341L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Long              itemId;

    private Long              payeeId;                                 // 提现者id
    private String            payeeCompany;                            // 公司名称
    private Long              operatorId;                              // 平台操作者id
    private Date              operateDate;                             // 操作时间
    private String            operatorName;                            // 操作者名称
    private Integer           status;                                  // 提现状态：0:成功；1：失败
    private Integer           amount;                                  // 提现金额，单位万元
    private Integer           withdrawType;                            // 提现方式，1：线上 2：线下
    private String            bankCard;                                // 银行卡
    private String            memo;                                    // 备注

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

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeCompany() {
        return payeeCompany;
    }

    public void setPayeeCompany(String payeeCompany) {
        this.payeeCompany = payeeCompany;
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

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
