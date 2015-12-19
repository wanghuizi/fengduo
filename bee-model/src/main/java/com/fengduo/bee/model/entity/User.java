/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;

/**
 * 用户基础信息
 * 
 * @author jie.xu
 */
public class User implements Serializable {

    private static final long serialVersionUID = 6776204725558788088L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private boolean           isIdentity;                             // 是否实名认证过；true:认证，false:未认证
    private String            nick;                                   // 用户名
    private String            email;                                  // 注册邮箱
    private String            password;
    @NotEmpty(message = "手机号不能为空")
    private String            phone;                                  // 注册手机号
    private String            avatar;                                 // 用户头像
    private String            attentions;                             // 关注的领域
    private String            province;                               // 省
    private String            city;                                   // 市
    private String            wechat;                                 // 微信号
    private String            weibo;                                  // 微博
    private String            memo;                                   // 简介
    private Integer           verifyStatus;                           // 状态: 0=未审核,1=正常,2=停止,5=审核不通过,null=普通用户
    private Integer           userType;                               // 类型:0:普通用户，1:认证投资用户
    private Integer           delFlag;                                // 删除标志:0:未删除 1：删除(:0=正常;1=删除)

    private String            realName;                               // 真实姓名

    public User() {

    }

    /**
     * 构造ID,DelFlagEnum删除状态的User对象
     * 
     * @param id
     * @param delFlagEnum
     */
    public User(Long id, DelFlagEnum delFlagEnum) {
        setId(id);
        setDelFlag(delFlagEnum.getValue());
    }

    /**
     * 构造ID,VerifyStatusEnum审核状态的User对象
     * 
     * @param id
     * @param verifyStatusEnum
     */
    public User(Long id, VerifyStatusEnum verifyStatusEnum) {
        setId(id);
        setVerifyStatus(verifyStatusEnum.getValue());
    }

    public String getDisplayName() {
        return StringUtils.isNotEmpty(nick) ? nick : phone;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public void setIdentity(boolean isIdentity) {
        this.isIdentity = isIdentity;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAttentions() {
        return attentions;
    }

    public void setAttentions(String attentions) {
        this.attentions = attentions;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
