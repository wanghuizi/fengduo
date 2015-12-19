/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fengduo.bee.commons.component.ComponentController;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.commons.util.StringUtils;
import com.fengduo.bee.service.cache.interfaces.UserCacheService;
import com.fengduo.bee.service.interfaces.ConsigneeAddrService;
import com.fengduo.bee.service.interfaces.FileService;
import com.fengduo.bee.service.interfaces.ItemService;
import com.fengduo.bee.service.interfaces.OrderService;
import com.fengduo.bee.service.interfaces.UserService;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;
import com.fengduo.bee.web.shiro.token.UsernamePasswordCaptchaToken;

/**
 * @author zxc Jun 9, 2015 1:05:56 PM
 */
public class BaseController extends ComponentController {

	protected static final Integer DEFAULT_PAGESIZE = 10;

	@Autowired
	protected FileService fileService;

	@Autowired
	protected UserService userService;
	@Autowired
	protected ItemService itemService;
	@Autowired
	protected OrderService orderService;
	@Autowired
	protected ConsigneeAddrService consigneeAddrService;
	@Autowired
	protected UserCacheService userCacheService;

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({ BindException.class,
			ConstraintViolationException.class, ValidationException.class })
	public String bindException(Model model) {
		model.addAttribute("errMsg", "参数绑定异常");
		return "error/404";
	}

	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({ AuthenticationException.class })
	public String authenticationException(Model model) {
		model.addAttribute("errMsg", "授权登录异常");
		return "error/404";
	}

	/**
	 * 返回未通过验证信息
	 * 
	 * @param result
	 * @return
	 */
	public Result showErrors(BindingResult result) {
		StringBuffer errorsb = new StringBuffer();
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				errorsb.append(error.getField());
				errorsb.append(error.getDefaultMessage());
				errorsb.append("|");
			}
			String errorsr = errorsb.toString().substring(0,
					errorsb.toString().length() - 1);
			return Result.failed(errorsr.replaceAll("null", StringUtils.EMPTY));
		}
		return Result.success();
	}

	/**
	 * 返回第一条未通过验证信息
	 * 
	 * @param result
	 * @return
	 */
	public String showFirstErrors(BindingResult result) {
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				return error.getDefaultMessage();
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user != null ? user.id : null;
	}

	/**
	 * 取出Shiro中的当前用户.
	 */
	public ShiroUser getCurrentUser() {
		return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	}

	/**
	 * 修改session中用户会话信息
	 */
	public void updateShiroUser(String phone, String pwd) {
		UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(
				phone, pwd.toCharArray());
		// 关闭图像验证码
		token.setUseCaptcha(false);
		SecurityUtils.getSubject().logout();
		SecurityUtils.getSubject().login(token);
	}
}
