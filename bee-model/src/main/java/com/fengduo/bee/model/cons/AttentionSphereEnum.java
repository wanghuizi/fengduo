/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.model.cons;

import java.util.ArrayList;
import java.util.List;

/**
 * 类AttentionSphereEnum.java的实现描述：关注的领域
 * 
 * @author jie.xu
 * @date 2015年6月11日 上午10:56:04
 */
public enum AttentionSphereEnum {

    //
    MEDIA("媒体"),
    //
    MOBILE_INTERNET("移动互联网"),
    //
    O2O("O2O"),
    //
    NEW_ENERGY("新能源"),
    //
    GAME("游戏"),
    //
    EDUCATION("教育"),
    //
    ELECTRONIC_COMMERCE("电子商务"),
    //
    FINANCE("金融"),
    //
    HARDWARE("硬件"),
    //
    ENERGY("能源"),
    //
    MEDICAL_ATTENDANCE("医疗"),
    //
    REPAST("餐饮"),
    //
    FARMING("农业"),
    //
    AUTOCAR("汽车"),
    //
    BIGDATA("大数据"),
    //
    ENTERTAINMENT("文化娱乐"),
    //
    OTHERS("其他");

    private String name;

    private AttentionSphereEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得所有枚举值
     */
    public static List<String> getAllValues() {
        List<String> values = new ArrayList<String>();
        for (AttentionSphereEnum as : values()) {
            values.add(as.getName());
        }
        return values;
    }
}
