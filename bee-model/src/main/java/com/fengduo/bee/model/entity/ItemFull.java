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

import com.fengduo.bee.commons.util.StringFormatter;

/**
 * 产品基础信息 产品融资信息连表查询对象
 * 
 * @author zxc Jun 18, 2015 1:46:12 PM
 */
public class ItemFull implements Serializable {

    private static final long serialVersionUID = 7420105902893570077L;

    private Long              itemId;
    private Date              createDate;
    private Long              userId;

    // 基础信息
    private String            tags;
    private String            name;
    private String            introduce;
    private Integer           stage;
    private Integer           teamCount;
    private String            province;
    private String            city;
    private String            videoUrl;
    private String            content;
    private String            imgCf;
    private String            imgZf;
    private String            img;

    private Integer           progress;
    private Integer           isCompany;
    private String            companyName;
    private Float             resgisterCaptial;
    private Integer           employee;

    private Integer           verifyStatus;
    private Date              verifyDate;
    private Integer           itemType;
    private Integer           delFlag;                                // 0=正常,1=删除

    // 融资信息
    private Integer           amount;
    private Float             percent;
    private Integer           stock;
    private String            capitalUses;
    private String            pdfUrl;
    private String            exFinance;
    private Float             perStock;
    private Float             perPercent;

    private Float             realSub;
    private Float             realPay;
    private Integer           suberNum;
    private String            realPercent;                            // 投资人实际已经认购金额

    public String getImgCf() {
        return StringUtils.isEmpty(imgCf) ? "/images/Spang_1.jpg" : imgCf;
    }

    public void setImgCf(String imgCf) {
        this.imgCf = imgCf;
    }

    public String getImgZf() {
        return StringUtils.isEmpty(imgZf) ? "/images/Spang_2.jpg" : imgZf;
    }

    public void setImgZf(String imgZf) {
        this.imgZf = imgZf;
    }

    public String getImg() {
        return StringUtils.isEmpty(img) ? "/images/Spang_3.jpg" : img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Float getPerStock() {
        Float realPerStock = 0.00f;
        if (stock == null || stock <= 0) {
            return realPerStock;
        }
        if (amount == null || amount <= 0) {
            return realPerStock;
        }
        realPerStock = (float) (amount / stock);
        perStock = realPerStock;
        return perStock;
    }

    public void setPerStock(Float perStock) {
        this.perStock = perStock;
    }

    public Float getPerPercent() {
        Float realPerPercent = 0.00f;
        if (stock == null || stock <= 0) {
            return realPerPercent;
        }
        if (percent == null || percent <= 0) {
            return realPerPercent;
        }
        realPerPercent = (float) (percent / stock);
        perPercent = realPerPercent;
        return perPercent;
    }

    public void setPerPercent(Float perPercent) {
        this.perPercent = perPercent;
    }

    public String getRealPercent() {
        realPercent = "0";
        if (realSub == null || realSub <= 0) {
            return realPercent;
        }
        if (amount == null || amount <= 0) {
            return realPercent;
        }
        Float realPercentFloat = (realSub / amount) * 100;
        realPercent = StringFormatter._formatFloat(realPercentFloat);
        return realPercent;
    }

    public void setRealPercent(String realPercent) {
        this.realPercent = realPercent;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTags() {
        return tags;
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

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
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

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getPercent() {
        return percent == null ? 0.0f : percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCapitalUses() {
        return capitalUses;
    }

    public void setCapitalUses(String capitalUses) {
        this.capitalUses = capitalUses;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getExFinance() {
        return exFinance;
    }

    public void setExFinance(String exFinance) {
        this.exFinance = exFinance;
    }

    public Float getRealSub() {
        return realSub == null ? 0.0f : realSub;
    }

    public void setRealSub(Float realSub) {
        this.realSub = realSub;
    }

    public Float getRealPay() {
        return realPay == null ? 0.0f : realPay;
    }

    public void setRealPay(Float realPay) {
        this.realPay = realPay;
    }

    public Integer getSuberNum() {
        return suberNum == null ? 0 : suberNum;
    }

    public void setSuberNum(Integer suberNum) {
        this.suberNum = suberNum;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
