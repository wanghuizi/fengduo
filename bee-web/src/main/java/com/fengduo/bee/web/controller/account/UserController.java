/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.web.controller.account;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.result.JsonResultUtils.JsonResult;
import com.fengduo.bee.commons.util.IDCardUtils;
import com.fengduo.bee.commons.util.ObjectUtils;
import com.fengduo.bee.model.cons.AttentionSphereEnum;
import com.fengduo.bee.model.cons.DelFlagEnum;
import com.fengduo.bee.model.entity.IdentityInfo;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.web.controller.BaseController;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;

/**
 * 会员信息管理
 * 
 * @author jie.xu
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@InitBinder({ "user" })
	public void initBinder1(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("user.");
	}

	@InitBinder({ "identityInfo" })
	public void initBinder2(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("identityInfo.");
	}

	/**
	 * 编辑个人密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/resetpwd", method = RequestMethod.GET)
	public ModelAndView resetpwd() {
		ModelAndView mav = new ModelAndView("account/myresetpwd");
		return mav;
	}

	/**
	 * 保存个人密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/savepwd", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult savepwd(String oldPassword, String password,
			String confirmPwd) {
		password = StringUtils.trim(password);
		confirmPwd = StringUtils.trim(confirmPwd);
		oldPassword = StringUtils.trim(oldPassword);

		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPwd)
				|| StringUtils.isEmpty(oldPassword)) {
			return JsonResultUtils.error("输入的密码错误!");
		}
		if (!StringUtils.equals(password, confirmPwd)) {
			return JsonResultUtils.error("两次输入的密码不一样,请重新输入!");
		}
		if (!StringUtils
				.equals(oldPassword, getCurrentUser().getPasswordDesc())) {
			return JsonResultUtils.error("旧密码输入不正确!");
		}

		Long userId = getCurrentUserId();
		User userById = userService.getUserById(userId);
		if (null == userById) {
			return JsonResultUtils.error("用户账户异常");
		}
		userService.updateUserPwd(userId, password);
		// 修改密码成功自动退出登录
		SecurityUtils.getSubject().logout();
		return JsonResultUtils.error("修改密码成功");
	}

	/**
	 * 个人基础信息设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String profile(Model model) {
		List<String> attentionValueList = AttentionSphereEnum.getAllValues();
		model.addAttribute("attentionValueList", attentionValueList);
		Long userId = getCurrentUserId();
		User user = userService.getUserById(userId);
		getCurrentUser().setUser(user);
		model.addAttribute("u", user);
		return "account/profile";
	}

	/**
	 * <pre>
	 * 保存用户基础信息
	 * 检查 邮箱、昵称唯一性
	 * </pre>
	 */
	@RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult saveUserInfo(User user) {
		ObjectUtils.trim(user);

		Long userId = getCurrentUserId();
		User u = userService.getUserById(userId);
		String email = user.getEmail();
		String nick = user.getNick();
		if (StringUtils.isNotEmpty(email)) {
			if (!EmailValidator.getInstance().isValid(email)) {
				return JsonResultUtils.error("请正确填写邮箱！");
			}
			if (!StringUtils.equals(u.getEmail(), email)) {
				Parameter query = Parameter.newParameter()//
						.pu("email", email)//
						.pu("delFlag", DelFlagEnum.UN_DELETE.getValue());

				User existU = userService.queryUser(query);
				if (existU != null) {
					return JsonResultUtils.error("该邮箱已被注册！");
				}
			}
		}
		if (StringUtils.isNotEmpty(nick)
				&& !StringUtils.equals(nick, u.getNick())) {
			Parameter query = Parameter.newParameter()//
					.pu("nick", nick)//
					.pu("delFlag", DelFlagEnum.UN_DELETE.getValue());
			User existU = userService.queryUser(query);
			if (existU != null) {
				return JsonResultUtils.error("该昵称已被注册！");
			}
		}

		user.setId(userId);
		userService.updateUserById(user);
		updateShiroUser(getCurrentUser().getPhone(),
				String.valueOf(getCurrentUser().getPassword()));
		userCacheService.clearUserCache(userId);
		return JsonResultUtils.success(null, "个人信息保存成功");
	}

	/**
	 * 实名认证
	 * 
	 * @return
	 */
	@RequestMapping(value = "/identity")
	public ModelAndView identity() {
		ModelAndView mav = new ModelAndView("account/identity");
		Long userId = getCurrentUserId();
		IdentityInfo info = userService.getIdentityInfo(userId);
		mav.addObject("identityInfo", info);
		return mav;
	}

	/**
	 * <pre>
	 * 保存实名认证信息
	 * 
	 * </pre>
	 */
	@RequestMapping(value = "/saveIdentityInfo")
	@ResponseBody
	public JsonResult saveIdentityInfo(@Valid IdentityInfo identityInfo,
			BindingResult result) {
		ObjectUtils.trim(identityInfo);

		Long userId = getCurrentUserId();
		identityInfo.setUserId(userId);
		if (identityInfo.getId() != null) {
			userService.updateIdentityInfoById(identityInfo);
		} else {
			if (result.hasErrors()) {
				return JsonResultUtils.error(showFirstErrors(result));
			}
			// 验证身份证
			if (!IDCardUtils.validateCard(identityInfo.getIdCard())) {
				return JsonResultUtils.error("身份证不合法");
			}
			// 查询邮箱是否已填写
			ShiroUser shiroUser = getCurrentUser();
			if (StringUtils.isEmpty(shiroUser.getEmail())) {
				return JsonResultUtils.error("请去个人信息设置页填写邮箱！");

			}
			if (identityInfo.getInvestorCase() != null
					&& identityInfo.getInvestorCase() > 4) {
				return JsonResultUtils.error("请正确选择符合投资人条件的选项");
			}
			userService.insertIdentityInfo(identityInfo);
		}
		return JsonResultUtils.success(null, "实名认证信息保存成功！");
	}
}
