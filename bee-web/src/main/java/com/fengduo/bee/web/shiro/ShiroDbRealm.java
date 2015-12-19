/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.shiro;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.security.Digests;
import com.fengduo.bee.commons.servlet.ValidateCodeServlet;
import com.fengduo.bee.commons.util.StringFormatter;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.IdentityInfo;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.service.constants.UserConstants;
import com.fengduo.bee.service.interfaces.UserService;
import com.fengduo.bee.web.shiro.exception.CaptchaException;
import com.fengduo.bee.web.shiro.exception.CaptchaInvalidException;
import com.fengduo.bee.web.shiro.token.UsernamePasswordCaptchaToken;
import com.google.common.base.Objects;

/**
 * @author jie.xu Jun 3, 2015 12:08:37 AM
 */
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	protected UserService userService;

	/**
	 * 构造器,设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(
				Digests.SHA512_ALGORITHM);
		matcher.setHashIterations(Digests.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	/**
	 * 认证回调函数,登录时调用.
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		if (null == authcToken) {
			throw new AccountException("登录出错!");
		}
		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
		String name = token.getUsername();
		if (StringUtils.isEmpty(name)) {
			throw new AccountException("用户名为空!");
		}
		char[] password = token.getPassword();
		if (password == null || password.length == 0) {
			throw new AccountException("密码为空!");
		}
		// 增加判断验证码逻辑
		String captcha = token.getCaptcha();
		boolean useCaptcha = token.isUseCaptcha();
		if (useCaptcha) {
			String exitCode = (String) SecurityUtils.getSubject().getSession()
					.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
			if (StringUtils.isEmpty(exitCode)) {
				throw new CaptchaInvalidException("图形验证码已经失效,请重新刷新页面!");
			}
			if (StringUtils.isEmpty(captcha)
					|| !captcha.equalsIgnoreCase(exitCode)) {
				throw new CaptchaException("图形验证码错误!");
			}
		}

		Parameter map = Parameter.newParameter();
		if (EmailValidator.getInstance().isValid(name)) {
			map.put("email", name);
		} else if (StringFormatter.isLegalPhone(name)) {
			map.put("phone", name);
		} else {
			map.put("nick", name);
		}
		final User user = userService.queryUser(map);
		if (null == user) {
			throw new UnknownAccountException("您还没有注册,请注册使用!");
		}
		// 审核通过
		if (user.getVerifyStatus() == VerifyStatusEnum.NORMAL.getValue()) {
			IdentityInfo identityInfo = userService.getIdentityInfo(user
					.getId());
			if (identityInfo != null) {
				user.setIdentity(true);
				user.setRealName(identityInfo.getRealName());
			}
		}
		return new SimpleAuthenticationInfo(new ShiroUser(user, password,
				captcha), user.getPassword(),
				ByteSource.Util.bytes(UserConstants.SALT), getName());
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@SuppressWarnings("unused")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		// User user = accountService.findUserByLoginName(shiroUser.loginName);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// info.addRoles(user.getRoleList());
		return info;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -1373760761780840081L;
		public Long id;
		private String phone;
		public String nick;
		public String email;
		public char[] password;
		@SuppressWarnings("unused")
		private String displayName; // 顺序:nick ,phone
		private User user;

		public ShiroUser(User user, char[] password, String captcha) {
			this(user.getId(), user.getPhone(), user.getNick(), user.getEmail());
			setUser(user);
			setPassword(password);
		}

		public ShiroUser(Long id, String phone, String nick, String email) {
			this.id = id;
			this.phone = phone;
			this.nick = nick;
			this.email = email;
		}

		public Long getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPassword(char[] password) {
			this.password = password;
		}

		public char[] getPassword() {
			return password;
		}

		public String getPasswordDesc() {
			return String.valueOf(password);
		}

		/**
		 * 是否实名认证
		 */
		public boolean isIdentity() {
			return user.isIdentity();
		}

		public String getDisplayName() {
			if (StringUtils.isNotEmpty(nick)) {
				return nick;
			} else {
				return phone;
			}
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return phone;
		}

		/**
		 * 重载hashCode,只计算nick;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(phone);
		}

		/**
		 * 重载equals,只计算nick;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (phone == null) {
				if (other.phone != null) {
					return false;
				}
			} else if (!phone.equals(other.phone)) {
				return false;
			}
			return true;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}
