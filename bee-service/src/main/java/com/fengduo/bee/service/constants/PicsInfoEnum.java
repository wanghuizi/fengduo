/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.constants;

/**
 * 类PicsInfoEnum.java的实现描述：图片信息
 * 
 * @author jie.xu
 * @date 2015年6月12日 上午11:34:25
 */
public enum PicsInfoEnum {

    /**
     * 头像
     */
    AVATAR_IMG("/avatar", "avatar", 1024 * 1024, 64, 64, new String[] { "jpg", "jpeg", "png" }),
    /**
     * 名片
     */
    BUSINESS_CARD("/businesscard", "businesscard", 1024 * 1024 * 2, 0, 0, new String[] { "jpg", "jpeg", "png" }),
    /**
     * 身份证证件照
     */
    IDCARD_IMG("/identity", "identity", 1024 * 1024 * 2, 0, 0, new String[] { "jpg", "jpeg", "png" }),
    /**
     * 产品图片
     */
    ITEM_IMG("/item", "item", 1024 * 1024 * 2, 0, 0, new String[] { "jpg", "jpeg", "png" }),
    /**
     * PDF
     */
    PDF("/pdf", "pdf", 1024 * 1024 * 2000, 0, 0, new String[] { "pdf", "jpeg", "jpg", "png" }),

    // ////////////////********富文本编辑器*********/////////////////

    /**
     * 编辑产品基础信息
     */
    EDIT_ITEM("/edit/item", "editItem", 1024 * 1024 * 2, 0, 0, new String[] { "jpg", "jpeg", "png" }),
    /**
     * 编辑产品融资信息
     */
    EDIT_ITEMFINANCE("/edit/itemFinance", "itemFinance", 1024 * 1024 * 2, 0, 0, new String[] { "jpg", "jpeg", "png" });

    // ////////////////*********富文本编辑器*********/////////////////

    private PicsInfoEnum(String dirPrefix, String type, int maxSize, int width, int height, String[] suffixs) {
        this.dirPrefix = dirPrefix;
        this.type = type;
        this.maxSize = maxSize;
        this.width = width;// 0:没有大小限制
        this.height = height;
        this.suffixs = suffixs;
    }

    private String   dirPrefix; // 图片目录前缀
    private String   type;     // 类型：应用名 + "_" + 类型
    private int      maxSize;  // 图片最大值 单位b
    private int      width;    // 宽
    private int      height;   // 高
    private String[] suffixs;  // 后缀

    public String[] getSuffixs() {
        return suffixs;
    }

    public void setSuffixs(String[] suffixs) {
        this.suffixs = suffixs;
    }

    public String getDirPrefix() {
        return dirPrefix;
    }

    public void setDirPrefix(String dirPrefix) {
        this.dirPrefix = dirPrefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static PicsInfoEnum getEnum(String type) {
        for (PicsInfoEnum pe : values()) {
            if (pe.getType().equals(type)) {
                return pe;
            }
        }
        return null;
    }
}
