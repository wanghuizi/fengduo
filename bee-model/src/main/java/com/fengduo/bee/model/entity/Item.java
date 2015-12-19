/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.cons.ProgressEnum;
import com.fengduo.bee.model.cons.VerifyStatusEnum;

/**
 * 产品基础信息表
 * 
 * <pre>
 *     id                  bigint              auto_increment,
 *     create_date         timestamp not null  default 0,
 *     update_date         timestamp not null  default 0,
 * 
 *     user_id             bigint  not null    COMMENT '用户id',
 *     tags                varchar(255)        COMMENT '产品标签',
 *     name                varchar(255)        COMMENT '名称',
 *     introduce           varchar(255)        COMMENT '项目简介',
 *     stage               int(2)              COMMENT '所属阶段',
 *     team_count          int(2)              COMMENT '团队人数',
 *     province            varchar(255)        COMMENT '省',
 *     city                varchar(255)        COMMENT '市',
 *     video_url           varchar(255)        COMMENT '视频地址',
 *     content             text                COMMENT '项目信息',
 *     img_cf              varchar(255)        COMMENT '首屏图片',
 *     img_zf              varchar(255)        COMMENT '列表图片',
 *     img                 varchar(255)        COMMENT '项目图片',
 *     progress            int(2)              COMMENT '众筹进展',
 *     
 *     is_company          int(2)              COMMENT '是否是公司运营:0=是公司;1=不是公司', 
 *     company_name        varchar(512)        COMMENT '公司名称',
 *     register_capital    float               COMMENT '注册资本(单位万元)',
 *     employee            int(5)              COMMENT '正式员工数',
 *     
 *     verify_status       int(2)              COMMENT '状态: 0=未审核,1=正常,2=停止,3=产品未发布完全',
 *     verify_date         timestamp           COMMENT '审核成功通过时间',
 *     item_type           int(2)              COMMENT '类型',
 *     del_flag            int(2)              COMMENT '逻辑删除:0=正常;1=删除',
 * </pre>
 * 
 * @author zxc Jun 9, 2015 1:05:56 PM
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 6878104326584530362L;

    private Long              id;
    private Date              createDate;
    private Date              updateDate;

    private Long              userId;
    private String            tags;                                   // 只填写一个行业
    @NotEmpty(message = "项目名称不可以是空!")
    private String            name;
    @NotEmpty(message = "项目介绍不可以是空!")
    private String            introduce;
    private Integer           stage;
    private Integer           teamCount;
    private String            province;
    private String            city;
    private String            videoUrl;
    @NotEmpty(message = "项目信息不可以是空!")
    private String            content;
    @NotEmpty(message = "项目首屏图片不可以是空!")
    private String            imgCf;
    @NotEmpty(message = "项目列表页图片不可以是空!")
    private String            imgZf;
    @NotEmpty(message = "项目图片不可以是空!")
    private String            img;

    private Integer           progress;

    private Integer           isCompany;
    private String            companyName;
    private Float             resgisterCaptial;
    private Integer           employee;

    private Integer           verifyStatus;
    private Date              verifyDate;                             // 审核时间
    private Integer           itemType;
    private Integer           delFlag;                                // 0=正常,1=删除

    private ItemFinance       itemFinance;

    public Item() {

    }

    public Item(Long id, DelFlagEnum delFlag) {
        setId(id);
        setDelFlag(delFlag.getValue());
    }

    public Item(Long id, VerifyStatusEnum verifyStatusEnum) {
        setId(id);
        setVerifyStatus(verifyStatusEnum.getValue());
    }

    public Long getId() {
        return id;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Float getResgisterCaptial() {
        return resgisterCaptial;
    }

    public void setResgisterCaptial(Float resgisterCaptial) {
        this.resgisterCaptial = resgisterCaptial;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
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

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgCf() {
        return imgCf;
    }

    public void setImgCf(String imgCf) {
        this.imgCf = imgCf;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getImgZf() {
        return imgZf;
    }

    public void setImgZf(String imgZf) {
        this.imgZf = imgZf;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 根据当前时间判断是否在基石投资进入期
     */
    public boolean isInCornerstoneTime() {
        if (verifyDate == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(verifyDate);
        cal.add(Calendar.DATE, ProgressEnum.RESERVATION.getDays());
        Date start = cal.getTime();
        cal.add(Calendar.DATE, ProgressEnum.CORNERSTONE.getDays());
        Date end = cal.getTime();
        Date now = new Date();
        if (now.getTime() >= start.getTime() && now.getTime() <= end.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据当前时间判断是否在预售期
     */
    public boolean isInPresell() {
        if (verifyDate == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(verifyDate);
        cal.add(Calendar.DATE, (ProgressEnum.RESERVATION.getDays() + ProgressEnum.CORNERSTONE.getDays()));
        Date start = cal.getTime();
        cal.add(Calendar.DATE, ProgressEnum.PRESELL.getDays());
        Date end = cal.getTime();
        Date now = new Date();
        if (now.getTime() >= start.getTime() && now.getTime() <= end.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取全额支付开始时间
     */
    public Date getPayAllStartDate(int subType) {
        if (subType == 1) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(verifyDate);
            cal.add(Calendar.DATE, ProgressEnum.RESERVATION.getDays());
            return cal.getTime();
        } else if (subType == 2) {
            Calendar cal = Calendar.getInstance();
            int days = ProgressEnum.RESERVATION.getDays() + ProgressEnum.CORNERSTONE.getDays()
                       + ProgressEnum.PRESELL.getDays();
            cal.setTime(verifyDate);
            cal.add(Calendar.DATE, days);
            return cal.getTime();
        }
        return null;
    }

    public ItemFinance getItemFinance() {
        return itemFinance;
    }

    public void setItemFinance(ItemFinance itemFinance) {
        this.itemFinance = itemFinance;
    }
}
