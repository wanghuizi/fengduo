/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fengduo.bee.commons.util.HttpClientUtils;
import com.fengduo.bee.service.interfaces.MailService;

/**
 * 邮件发送接口实现
 * 
 * @author zxc Jun 19, 2015 3:11:18 PM
 */
@Service
public class MailServiceImpl implements MailService {

	private static Logger logger = LoggerFactory
			.getLogger(SmsServiceImpl.class);

	private static String mailUrl = "https://api.submail.cn/mail/send.json";
	private static String mailAppid = "10490";
	private static String signature = "85817b65c1443263c31bf1215fac7c1a";

	@Autowired
	private ThreadPoolTaskExecutor executor;

	/**
	 * curl -d 'appid=10490&to=zxc<zhangxiongcai337@163.com> &subject=testing
	 * Subject&text=testing text
	 * body&from=zhangxiongcai@fengduo.co&signature=85817
	 * b65c1443263c31bf1215fac7c1a' https://api.submail.cn/mail/send.json
	 */
	@Override
	public boolean sendTxtMail(final String toMember, final String subject,
			final String text) {
		if (StringUtils.isEmpty(toMember)) {
			logger.error("邮件接收人地址为空!发送失败");
			return false;
		}
		if (StringUtils.isEmpty(subject)) {
			logger.error("邮件主题为空!发送失败");
			return false;
		}
		if (StringUtils.isEmpty(text)) {
			logger.error("邮件内容为空!发送失败");
			return false;
		}
		Future<Boolean> future = executor.submit(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				return sendMail(toMember, subject, text);
			}

		});
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean sendHtmlMail(String toMember, String subject, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendAttachmentsMail(String toMember, String subject,
			String text, String file) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean sendMail(String toMember, String subject, String text) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", mailAppid));
		params.add(new BasicNameValuePair("to", toMember));
		params.add(new BasicNameValuePair("subject", subject));
		params.add(new BasicNameValuePair("text", text));
		params.add(new BasicNameValuePair("signature", signature));
		params.add(new BasicNameValuePair("from", "system@fengduo.co"));

		try {
			String result = HttpClientUtils.postRequest(mailUrl, params);
			logger.info("---send mail to get code result {},邮箱收件人{},推送成功！",
					result, toMember);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String status = jsonObject.getString("status");
			if (StringUtils.equalsIgnoreCase(status, "success")) {
				return true;
			}
		} catch (Exception e) {
			logger.debug("http post error!{}", e.getMessage());
			logger.info("邮箱收件人{},推送失败！", toMember);
		}
		return false;
	}

}
