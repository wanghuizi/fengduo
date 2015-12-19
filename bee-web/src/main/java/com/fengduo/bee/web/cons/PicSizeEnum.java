/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.cons;

/**
 * 图片尺寸大小枚举
 * 
 * @author zxc Jun 12, 2015 4:15:11 PM
 */
public enum PicSizeEnum {

    SIZE_L(400, 400, "_400x400"), SIZE_M(200, 200, "_200x200"), SIZE_S(100, 100, "_100x100"), SIZE_C(48, 48, "_48x48");

    int    width, height;
    String desc;

    PicSizeEnum(int w, int h, String desc) {
        this.width = w;
        this.height = h;
        this.desc = desc;
    }

    public int getWidth() {
        return width;
    }

    public String getDesc() {
        return desc;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSizeL() {
        return this == PicSizeEnum.SIZE_L;
    }

    public boolean isSizeM() {
        return this == PicSizeEnum.SIZE_M;
    }

    public boolean isSizeS() {
        return this == PicSizeEnum.SIZE_S;
    }

    public boolean isSizeC() {
        return this == PicSizeEnum.SIZE_C;
    }
}
