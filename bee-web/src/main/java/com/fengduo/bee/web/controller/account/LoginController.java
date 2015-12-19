/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.web.controller.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.security.auth.login.AccountException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fengduo.bee.commons.cons.ResultCode;
import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.result.JsonResultUtils;
import com.fengduo.bee.commons.servlet.ValidateCodeServlet;
import com.fengduo.bee.commons.shiro.Permission;
import com.fengduo.bee.commons.velocity.CustomVelocityLayoutView;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.service.interfaces.SmsService;
import com.fengduo.bee.service.interfaces.UserService;
import com.fengduo.bee.web.controller.BaseController;
import com.fengduo.bee.web.shiro.ShiroDbRealm.ShiroUser;
import com.fengduo.bee.web.shiro.exception.CaptchaException;
import com.fengduo.bee.web.shiro.exception.CaptchaInvalidException;
import com.fengduo.bee.web.utils.InvokeTypeTools;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求), 真正登录的POST请求由Filter完成, 用户注册的Controller.
 * 
 * @author jie.xu
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private Permission  permission;
    @Autowired
    private SmsService  smsService;

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    /**
     * 蜂朵网首页
     */
    @RequestMapping(value = { "/index", "/" })
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");

        return mav;
    }

    /**
     * 蜂朵网首页v1版
     */
    @RequestMapping(value = { "/v1" })
    public ModelAndView v1() {
        ModelAndView mav = new ModelAndView("indexV1");
        mav.getModel().put(CustomVelocityLayoutView.USE_LAYOUT, "false");
        return mav;
    }

    /**
     * 404
     */
    @RequestMapping(value = { "/404" })
    public ModelAndView notFound() {
        ModelAndView mav = new ModelAndView("error/404");
        return mav;
    }

    /**
     * 登陆页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String errMsg) {
        // SecurityUtils.getSubject().logout();
        // ShiroUser currentUser = getCurrentUser();
        // if (currentUser != null || (currentUser != null &&
        // Argument.isNotPositive(currentUser.getId()))) {
        // return "redirect:/index";
        // }
        model.addAttribute("errMsg", errMsg);
        return "account/login";
    }

    /**
     * ajax跳到登入页面
     */
    @RequestMapping(value = "/ajaxLogin")
    public String ajaxLogin(Model model) {
        model.addAttribute(CustomVelocityLayoutView.USE_LAYOUT, "false");
        return "account/ajaxLogin";
    }

    /**
     * 登陆验证失败跳转
     * 
     * @param userName
     * @param model
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM)
    String userName, HttpServletRequest req, Model model) {
        String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "您还没有注册,请注册使用!";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码不正确!";
        } else if (CaptchaInvalidException.class.getName().equals(exceptionClassName)) {
            error = "图形验证码已经失效,请重新刷新页面!";
        } else if (CaptchaException.class.getName().equals(exceptionClassName)) {
            error = "图形验证码错误!";
        } else if (AccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码输入出错!";
        } else if (exceptionClassName != null) {
            error = "登录失败，请重试!";
        }

        if (StringUtils.isEmpty(exceptionClassName)) {
            String code = request.getParameter("captcha");
            String phone = request.getParameter("username");
            String pwd = request.getParameter("password");
            if (StringUtils.equalsIgnoreCase(code, (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE))) {
                updateShiroUser(phone, pwd);
                return "redirect:/user/setting";
            } else {
                error = "图形验证码错误!";
            }
        }
        model.addAttribute("errMsg", error);
        // ajax未登入情况
        if (InvokeTypeTools.isAjax(req)) {
            response.setContentType("application/json");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            writer.print(JsonResultUtils.createJsonResult(ResultCode.ERROR, "", error));
            writer.flush();
            writer.close();
        }
        return "account/login";
    }

    /**
     * 注册页面
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm() {
        return "account/register";
    }

    /**
     * 注册验证
     * 
     * @param user
     * @param result
     * @param checkCode 短信验证吗
     * @param confirmPassword
     * @param isRead 1:阅读蜂朵协议 0:未阅读蜂朵协议
     * @return
     * @description: 用户注册
     * @author jie.xu
     * @date 2015年6月9日 下午8:48:23
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid
    User user, BindingResult result, String checkCode, String confirmPassword, String isRead, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errMsg", showFirstErrors(result));
            return "account/register";
        }
        model.addAttribute("phone", user.getPhone());
        if (isRead == null || StringUtils.equals(isRead, "0")) {
            model.addAttribute("errMsg", "请勾选并同意蜂朵网服务协议!");
            return "account/register";
        }
        confirmPassword = StringUtils.trim(confirmPassword);
        if (StringUtils.isEmpty(confirmPassword)) {
            model.addAttribute("errMsg", "请填写您的个人密码,并妥善保管!");
            return "account/register";
        }
        String passwd = StringUtils.trim(user.getPassword());
        if (!StringUtils.equals(confirmPassword, passwd)) {
            model.addAttribute("errMsg", "两次输入的密码不一致!");
            return "account/register";
        }
        // 验证码校验
        String phone = StringUtils.trim(user.getPhone());
        String cacheCode = smsService.getCheckCodeCache(phone);
        if (!StringUtils.equals(checkCode, cacheCode)) {
            model.addAttribute("errMsg", "手机短信验证码错误,请重新点击发送验证码!");
            return "account/register";
        }

        Parameter query = Parameter.newParameter()//
        .pu("phone", phone);
        User existUser = userService.queryUser(query);
        if (existUser != null) {
            model.addAttribute("errMsg", "该手机已经注册过,请直接登录!");
            return "account/register";
        }
        user.setPhone(phone);
        user.setPassword(passwd);
        user = userService.insertUser(user);
        // 自动登录
        updateShiroUser(phone, passwd);
        return "redirect:/user/setting";
    }

    /**
     * 注册获取短信验证码
     */
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.GET)
    public ModelAndView getSmsCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return createJsonMav("请输入手机号...", ResultCode.ERROR);
        }
        phone = com.fengduo.bee.commons.util.StringUtils.trim(phone);
        // 判断该手机号是否已注册过了
        Parameter query = Parameter.newParameter()//
        .pu("phone", phone);
        User existUser = userService.queryUser(query);
        if (existUser != null) {
            return createJsonMav("该手机号已注册过了...", ResultCode.ERROR);
        }
        boolean flag = smsService.sendCheckCode(phone.trim());
        if (flag) {
            return createJsonMav("请注意查收手机验证码...", ResultCode.SUCCESS);
        } else {
            return createJsonMav("获取手机验证码失败", ResultCode.ERROR);
        }
    }

    /**
     * 找回密码
     */
    @RequestMapping(value = "/findpwd", method = RequestMethod.GET)
    public String findpwd() {
        ShiroUser currentUser = getCurrentUser();
        if (currentUser != null || (currentUser != null && Argument.isNotPositive(currentUser.getId()))) {
            return "redirect:/index";
        }
        return "account/findpwd";
    }

    /**
     * 找回密码的短信验证码
     */
    @RequestMapping(value = "/findPwdSms", method = RequestMethod.GET)
    public ModelAndView findPwdSms(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return createJsonMav("请输入手机号...", ResultCode.ERROR);
        }
        phone = com.fengduo.bee.commons.util.StringUtils.trim(phone);
        // 判断该手机号是否已注册过了
        Parameter query = Parameter.newParameter()//
        .pu("phone", phone);
        User existUser = userService.queryUser(query);
        if (existUser == null) {
            return createJsonMav("该手机号没有注册过,您可以直接注册...", ResultCode.ERROR);
        }
        boolean flag = smsService.sendCheckCode(phone.trim());
        if (flag) {
            return createJsonMav("请注意查收手机验证码...", ResultCode.SUCCESS);
        } else {
            return createJsonMav("获取手机验证码失败", ResultCode.ERROR);
        }
    }

    /**
     * 找回密码提交数据验证
     */
    @RequestMapping(value = "/findpwd", method = RequestMethod.POST)
    public String findpwd(Model model, String mobile, String code, String verify) {
        // 返回重置密码页面
        mobile = com.fengduo.bee.commons.util.StringUtils.trim(mobile);
        code = com.fengduo.bee.commons.util.StringUtils.trim(code);
        if (StringUtils.isEmpty(mobile)) {
            model.addAttribute("errMsg", "手机号为空,请重新输入!");
            return "account/findpwd";
        }
        if (StringUtils.isEmpty(code)) {
            model.addAttribute("errMsg", "手机验证码为空,请重新输入!");
            return "account/findpwd";
        }
        // 验证数据
        Parameter query = Parameter.newParameter()//
        .pu("phone", mobile);
        User existUser = userService.queryUser(query);
        if (existUser == null) {
            model.addAttribute("errMsg", "该手机号还没有注册,您可以直接注册使用!");
            return "redirect:/register";
        }
        String checkCodeCache = smsService.getCheckCodeCache(mobile);
        if (StringUtils.isEmpty(checkCodeCache)) {
            model.addAttribute("errMsg", "手机验证码已经失效,请重新点击发送手机验证码!");
            return "account/findpwd";
        }
        if (!StringUtils.equals(checkCodeCache, code)) {
            model.addAttribute("errMsg", "手机验证码输入不正确,请重新输入手机验证码!");
            return "account/findpwd";
        }
        session.setAttribute("MOBILE", mobile);
        return "redirect:/resetpwd";
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/resetpwd", method = RequestMethod.GET)
    public ModelAndView resetpwd(ModelAndView mav) {
        mav.setViewName("account/resetpwd");
        if (session == null) {
            mav.setViewName("account/findpwd");
            mav.addObject("errMsg", "验证码已经失效");
            return mav;
        }
        String phone = (String) session.getAttribute("MOBILE");
        if (StringUtils.isEmpty(phone)) {
            mav.setViewName("account/findpwd");
            mav.addObject("errMsg", "验证码已经失效");
            return mav;
        }
        String checkCodeCache = smsService.getCheckCodeCache(phone);
        if (StringUtils.isEmpty(checkCodeCache)) {
            mav.setViewName("account/findpwd");
            mav.addObject("errMsg", "验证码已经失效");
            return mav;
        }
        return mav;
    }

    /**
     * 重置密码提交数据验证
     */
    @RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
    public String resetpwd(Model model, String password, String confirmPwd) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPwd)) {
            model.addAttribute("errMsg", "输入的密码错误!");
            return "account/resetpwd";
        }
        if (!StringUtils.equals(password, confirmPwd)) {
            model.addAttribute("errMsg", "两次输入的密码不一样,请重新输入!");
            return "account/resetpwd";
        }
        if (session == null) {
            model.addAttribute("errMsg", "验证码已经失效");
            return "account/findpwd";
        }
        String phone = (String) session.getAttribute("MOBILE");
        if (StringUtils.isEmpty(phone)) {
            model.addAttribute("errMsg", "验证码已经失效");
            return "account/findpwd";
        }
        String checkCodeCache = smsService.getCheckCodeCache(phone);
        if (StringUtils.isEmpty(checkCodeCache)) {
            model.addAttribute("errMsg", "验证码已经失效");
            return "account/findpwd";
        }
        Parameter query = Parameter.newParameter()//
        .pu("phone", phone);
        User existUser = userService.queryUser(query);
        if (existUser == null) {
            model.addAttribute("errMsg", "该手机号还没有注册,您可以直接注册使用!");
            return "redirect:/register";
        }
        userService.updateUserPwd(existUser.getId(), password);
        model.addAttribute("errMsg", "密码修改成功,请使用新密码登陆!");

        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }
}
