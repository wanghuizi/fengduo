/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.utils;

import com.fengduo.bee.commons.util.StringUtils;
import com.fengduo.bee.web.cons.PicSizeEnum;

/**
 * 图片尺寸大小工具类
 * 
 * @author zxc Jun 12, 2015 4:11:00 PM
 */
public class PicTools {

    private static final String UNDERLINE = "_";

    private static final String DOT       = ".";

    private static final String X         = "x";

    public static void main(String[] args) {
        String url = " http://img1.fengduo.co/member/20/wKgCNlSKyDKZPPhZAAAmivdKdOo698_37x37.jpg";
        System.out.println(url);
        System.out.println(get48PicUrl(url));
        String _url = " http://img1.fengduo.co/member/20/wKgCNlSKyDKZPPhZAAAmivdKdOo698.jpg";
        System.out.println(get200PicUrl(_url));
    }

    /**
     * 处理缩放后的图的路径
     * 
     * @param url
     * @param picSize
     * @return
     */
    public static String processURL(String url, String suffix) {
        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.isEmpty(suffix)) {
            return url;
        }
        String pefix = url;
        if (StringUtils.contains(url, UNDERLINE)) {
            pefix = StringUtils.substringBeforeLast(url, UNDERLINE);
        } else if (StringUtils.contains(url, "=C")) {
            pefix = StringUtils.substringBeforeLast(url, "=C");
        } else {
            pefix = StringUtils.substringBeforeLast(url, DOT);
        }
        String dotStr = StringUtils.substringAfterLast(url, DOT);
        return pefix + suffix + DOT + dotStr;
    }

    /**
     * 得到主图的超大图url
     * 
     * @param mainPics
     * @return
     */
    public static String get400PicUrl(String mainPics) {
        return processURL(mainPics, PicSizeEnum.SIZE_M.getDesc());
    }

    /**
     * 得到主图的大图url
     * 
     * @param mainPics
     * @return
     */
    public static String get200PicUrl(String mainPics) {
        return processURL(mainPics, PicSizeEnum.SIZE_M.getDesc());
    }

    /**
     * 得到主图的中图url
     * 
     * @param mainPics
     * @return
     */
    public static String get100PicUrl(String mainPics) {
        return processURL(mainPics, PicSizeEnum.SIZE_S.getDesc());
    }

    /**
     * 得到主图的小图url
     * 
     * @param mainPics
     * @return
     */
    public static String get48PicUrl(String mainPics) {
        return processURL(mainPics, PicSizeEnum.SIZE_C.getDesc());
    }

    /**
     * 处理缩放后的图的路径
     * 
     * @param url
     * @param picSize
     * @return
     */
    protected static String processURL(String url, PicSizeEnum picSize) {
        if (url == null || url.length() < 0) {
            return null;
        }
        if (!url.endsWith(".jpg")) {
            return url + UNDERLINE + picSize.getWidth() + X + picSize.getHeight() + ".jpg";
        }
        int index = url.lastIndexOf(DOT);
        return url + UNDERLINE + picSize.getWidth() + X + picSize.getHeight() + url.substring(index);
    }
}
