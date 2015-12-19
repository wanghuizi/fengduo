/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.interfaces;

/**
 * 邮件发送接口
 * 
 * @author zxc Jun 19, 2015 3:10:34 PM
 */
public interface MailService {

	/**
	 * 发送文本邮件
	 */
	boolean sendTxtMail(String toMember, String subject, String text);

	/**
	 * 发送Html邮件
	 */
	boolean sendHtmlMail(String toMember, String subject, String text);

	/**
	 * 发送Attachments邮件
	 */
	boolean sendAttachmentsMail(String toMember, String subject, String text,
			String file);
}
