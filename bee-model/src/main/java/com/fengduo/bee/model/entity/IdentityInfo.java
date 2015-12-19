/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 类IdentityInfo.java的实现描述：用户认证信息
 * 
 * @author jie.xu
 * @date 2015年6月9日 上午11:26:22
 */
public class IdentityInfo implements Serializable {

    private static final long serialVersionUID = 8525270664937681885L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;
    private Long              userId;

    @NotEmpty(message = "真实姓名不能为空")
    private String            realName;                               // 真实姓名
    @NotEmpty(message = "身份证不能为空")
    private String            idCard;                                 // 身份证
    @NotEmpty(message = "身份证正面照不能为空")
    private String            frontSide;                              // 身份证正面照
    @NotEmpty(message = "身份证反面照不能为空")
    private String            backSide;                               // 身份证反面照
    @NotEmpty(message = "银行卡不能为空")
    private String            bankCard;                               // 银行卡
    private String            bankPhone;                              // 银行预留手机号
    private String            bankNode;                               // 开户行网点
    private String            bankAddress;                            // 开户行具体地址

    @NotEmpty(message = "名片不能为空")
    private String            businessCard;                           // 名片
    private Integer           investorCase;                           // 投资人条件 ，value=1，2，3，4
    private String            investorCompany;                        // 投资人公司
    private String            investorTitle;                          // 投资人头衔
    private String            investorIntroduce;                      // 自我描述

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankPhone() {
        return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    public String getBankNode() {
        return bankNode;
    }

    public void setBankNode(String bankNode) {
        this.bankNode = bankNode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(String businessCard) {
        this.businessCard = businessCard;
    }

    public Integer getInvestorCase() {
        return investorCase;
    }

    public void setInvestorCase(Integer investorCase) {
        this.investorCase = investorCase;
    }

    public String getInvestorCompany() {
        return investorCompany;
    }

    public void setInvestorCompany(String investorCompany) {
        this.investorCompany = investorCompany;
    }

    public String getInvestorTitle() {
        return investorTitle;
    }

    public void setInvestorTitle(String investorTitle) {
        this.investorTitle = investorTitle;
    }

    public String getInvestorIntroduce() {
        return investorIntroduce;
    }

    public void setInvestorIntroduce(String investorIntroduce) {
        this.investorIntroduce = investorIntroduce;
    }
}
