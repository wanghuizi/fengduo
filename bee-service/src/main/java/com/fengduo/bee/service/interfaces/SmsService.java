package com.fengduo.bee.service.interfaces;

import java.util.Map;

/**
 * 类SmsService.java的实现描述：短信服务
 * 
 * @author jie.xu
 * @date 2015年6月13日 下午3:17:48
 */
public interface SmsService {

    /**
     * 发送短信验证码
     */
    boolean sendCheckCode(String phone);

    /**
     * 从缓存中获取验证码
     */
    String getCheckCodeCache(String phone);

    /**
     * 发送短信，内容自定义
     * 
     * @param msgMap msgKey,msgValue
     * @param project 短信模板
     */
    boolean sendMsg(String phone, Map<String, String> msgMap, String project);
}
