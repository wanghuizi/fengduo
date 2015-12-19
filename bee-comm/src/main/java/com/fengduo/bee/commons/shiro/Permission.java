/*
 * Copyright 2015-2020 Fengduo.com All right reserved. This software is the confidential and proprietary information of
 * Fengduo.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.com.
 */
package com.fengduo.bee.commons.shiro;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 在velocity 模板文件中，实现shiro 权限验证，类似于shiro 的jsp-tag.
 * 
 * @author zxc May 28, 2015 10:17:41 AM
 */
@DefaultKey("shiro")
@ValidScope(Scope.APPLICATION)
public class Permission {

    private static final String ROLE_NAMES_DELIMETER       = ",";
    private static final String PERMISSION_NAMES_DELIMETER = ",";

    private static final Logger logger                     = LoggerFactory.getLogger(Permission.class);

    /* 验证是否为已认证通过的用户，不包含已记住的用户，这是与 isUser 标签方法的区别所在。 */
    public boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.isAuthenticated() == true;
    }

    /* 验证是否为未认证通过用户，与 isAuthenticated 标签相对应，与 isGuest 标签的区别是，该标签包含已记住用户。 */
    public boolean isNotAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject == null || subject.isAuthenticated() == false;
    }

    /* 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。 */
    public boolean isGuest() {
        Subject subject = SecurityUtils.getSubject();
        return subject == null || subject.getPrincipal() == null;
    }

    /* 验证当前用户是否认证通过或已记住的用户。 */
    public boolean isUser() {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.getPrincipal() != null;
    }

    /* 获取当前用户 Principal。 */
    public Object getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            return subject.getPrincipal();
        }

        return null;
    }

    /* 获取当前用户属性。 */
    public Object getPrincipalProperty(String property) {
        Subject subject = SecurityUtils.getSubject();
        Object value = null;

        if (subject != null) {
            Object principal = subject.getPrincipal();

            try {
                BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

                boolean foundProperty = false;
                for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                    if (pd.getName().equals(property)) {
                        value = pd.getReadMethod().invoke(principal, (Object[]) null);
                        foundProperty = true;
                        break;
                    }
                }

                if (!foundProperty) {
                    final String message = "Property [" + property + "] not found in principal of type ["
                                           + principal.getClass().getName() + "]";
                    logger.trace(message);
                }
            } catch (Exception e) {
                final String message = "Error reading property [" + property + "] from principal of type ["
                                       + principal.getClass().getName() + "]";
                logger.trace(message);
            }
        }

        return value;
    }

    /* 验证当前用户是否属于该角色 。 */
    public boolean hasRole(String role) {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.hasRole(role) == true;
    }

    /* 验证当前用户是否不属于该角色，与 hasRole 逻辑相反。 */
    public boolean lacksRole(String role) {
        return hasRole(role) != true;
    }

    /* 验证当前用户是否属于以下任意一个角色。 */
    public boolean hasAnyRoles(String roleNames, String delimeter) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            if (delimeter == null || delimeter.length() == 0) {
                delimeter = ROLE_NAMES_DELIMETER;
            }

            for (String role : roleNames.split(delimeter)) {
                if (subject.hasRole(role.trim()) == true) {
                    return true;
                }
            }
        }

        return false;
    }

    /* 验证当前用户是否属于以下任意一个角色。 */
    public boolean hasAnyRoles(String roleNames) {
        return hasAnyRoles(roleNames, ROLE_NAMES_DELIMETER);
    }

    /* 验证当前用户是否属于以下任意一个角色。 */
    public boolean hasAnyRoles(Collection<String> roleNames) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && roleNames != null) {
            for (String role : roleNames) {
                if (role != null && subject.hasRole(role.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    /* 验证当前用户是否属于以下任意一个角色。 */
    public boolean hasAnyRoles(String[] roleNames) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && roleNames != null) {
            for (int i = 0; i < roleNames.length; i++) {
                String role = roleNames[i];
                if (role != null && subject.hasRole(role.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    /* 验证当前用户是否拥有指定权限 */
    public boolean hasPermission(String permission) {
        Subject subject = SecurityUtils.getSubject();
        return subject != null && subject.isPermitted(permission);
    }

    /* 验证当前用户是否不拥有指定权限，与 hasPermission 逻辑相反。 */
    public boolean lacksPermission(String permission) {
        return hasPermission(permission) != true;
    }

    /* 验证当前用户是否拥有以下任意一个权限。 */
    public boolean hasAnyPermissions(String permissions, String delimeter) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            if (delimeter == null || delimeter.length() == 0) {
                delimeter = PERMISSION_NAMES_DELIMETER;
            }

            for (String permission : permissions.split(delimeter)) {
                if (permission != null && subject.isPermitted(permission.trim()) == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /* 验证当前用户是否拥有以下任意一个权限。 */
    public boolean hasAnyPermissions(String permissions) {
        return hasAnyPermissions(permissions, PERMISSION_NAMES_DELIMETER);
    }

    /* 验证当前用户是否拥有以下任意一个权限。 */
    public boolean hasAnyPermissions(Collection<String> permissions) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && permissions != null) {
            for (String permission : permissions) {
                if (permission != null && subject.isPermitted(permission.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* 验证当前用户是否拥有以下任意一个权限。 */
    public boolean hasAnyPermissions(String[] permissions) {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission != null && subject.isPermitted(permission.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
