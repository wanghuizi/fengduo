/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.commons.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * 类HttpClientUtils.java的实现描述：TODO 类实现描述
 * 
 * @author jie.xu
 * @date 2015年6月13日 下午2:12:18
 */
public class HttpClientUtils {

    public static int         DEFAULT_TIMEOUT    = 10000;
    private static int        CONNECTION_TIMEOUT = DEFAULT_TIMEOUT;
    private static String     CHARSET            = "UTF-8";
    private static HttpClient client             = new DefaultHttpClient();

    static {
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, CONNECTION_TIMEOUT);
    }

    public static String postRequest(String url, List<NameValuePair> postParams) throws Exception {
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        String result = null;
        try {
            uefEntity = new UrlEncodedFormEntity(postParams, CHARSET);
            post.setEntity(uefEntity);
            HttpResponse rep = client.execute(post);
            if (rep.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = rep.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity, "UTF-8");
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            post.abort();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://api.submail.cn/message/xsend.json";
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("appid", "10245"));
        list.add(new BasicNameValuePair("to", "13771073096"));
        list.add(new BasicNameValuePair("project", "44g4a4"));
        list.add(new BasicNameValuePair("signature", "db81f10a40953015f9e8fa1a16017e4a"));
        list.add(new BasicNameValuePair("vars", "{\"checkCode\":\"410037\"}"));
        String result = HttpClientUtils.postRequest(url, list);
        System.out.println("------result----:" + result);
    }
}
