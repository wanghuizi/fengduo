/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fengduo.bee.commons.core.utils.Identities;
import com.fengduo.bee.commons.redis.JedisUtils;
import com.fengduo.bee.commons.util.HttpClientUtils;
import com.fengduo.bee.service.interfaces.SmsService;

/**
 * 短信服务类
 * 
 * @author jie.xu
 * @date 2015年6月13日 下午3:27:06
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService {

    private static Logger          logger         = LoggerFactory.getLogger(SmsServiceImpl.class);

    private static int             SMSCODE_EXPIRE = 60 * 5 + 10;                                  // 短信验证码过期时间

    @Value("${SMS_URL}")
    public String                  smsUrl;
    @Value("${SMS_APPID}")
    private String                 smsAppid;
    @Value("${SMS_SIGNATURE}")
    private String                 smsSignature;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    /**
     * 发送短信验证码
     * 
     * @param mobiles
     * @return
     */
    @Override
    public boolean sendCheckCode(final String phone) {
        executor.submit(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final String code = Identities.randomNum(6);
                Map<String, String> map = new HashMap<String, String>();
                map.put("checkCode", code);
                boolean flag = sendSelfDefiningMsg(phone, map, SmsProjectEnum.CHECKCODE.getValue());

                if (flag) {
                    // 写入缓存
                    JedisUtils.set(phone, code, SMSCODE_EXPIRE);
                }
                return flag;
            }

        });
        return true;
    }

    @Override
    public String getCheckCodeCache(String phone) {
        return JedisUtils.get(phone);
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsAppid() {
        return smsAppid;
    }

    public void setSmsAppid(String smsAppid) {
        this.smsAppid = smsAppid;
    }

    public String getSmsSignature() {
        return smsSignature;
    }

    public void setSmsSignature(String smsSignature) {
        this.smsSignature = smsSignature;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ////
    // //// 私有方法(实际的短信发送接口)
    // ////
    // /////////////////////////////////////////////////////////////////////////////////////

    private boolean sendSelfDefiningMsg(String phone, Map<String, String> msgMap, String project) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid", smsAppid));
        params.add(new BasicNameValuePair("to", phone));
        params.add(new BasicNameValuePair("project", project));
        params.add(new BasicNameValuePair("signature", smsSignature));
        for (Map.Entry<String, String> entry : msgMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            params.add(new BasicNameValuePair("vars", "{\"" + key + "\":\"" + value + "\"}"));
        }

        try {
            String result = HttpClientUtils.postRequest(smsUrl, params);
            logger.info("---send sms  result {},手机号{},推送成功！", result, phone);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.getString("status");
            if (StringUtils.equalsIgnoreCase(status, "success")) {
                return true;
            }
        } catch (Exception e) {
            logger.debug("http post error!{}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean sendMsg(final String phone, final Map<String, String> msgMap, final String project) {

        Future<Boolean> future = executor.submit(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return sendSelfDefiningMsg(phone, msgMap, project);
            }

        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static enum SmsProjectEnum {

        CHECKCODE("短信验证码", "44g4a4"), NONPAY("未支付订单短信通知", "xxxx");

        private String name;
        private String value;

        private SmsProjectEnum(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
