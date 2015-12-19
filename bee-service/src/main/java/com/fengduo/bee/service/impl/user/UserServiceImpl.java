/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.user;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.utils.Encodes;
import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.commons.persistence.service.BaseService;
import com.fengduo.bee.commons.security.Digests;
import com.fengduo.bee.data.dao.IdentityInfoDao;
import com.fengduo.bee.data.dao.UserDao;
import com.fengduo.bee.data.dao.UserItemDao;
import com.fengduo.bee.model.cons.VerifyStatusEnum;
import com.fengduo.bee.model.entity.IdentityInfo;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.service.constants.UserConstants;
import com.fengduo.bee.service.interfaces.UserService;

/**
 * 会员服务实现
 * 
 * @author jie.xu
 * @date 2015年6月9日 下午5:27:41
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserDao         userDao;
    @Autowired
    private IdentityInfoDao identityInfoDao;
    @Autowired
    private UserItemDao     userItemDao;

    @Override
    public User insertUser(User user) {
        if (StringUtils.isEmpty(user.getPassword())) {
            return null;
        }
        user.setPassword(entryptText(user.getPassword()));
        userDao.insert(user);
        return user;
    }

    @Override
    public boolean updateUserPwd(Long userId, String password) {
        if (StringUtils.isEmpty(password) || Argument.isNotPositive(userId)) {
            return false;
        }
        User user = userDao.getById(userId);
        user.setPassword(entryptText(password));
        return userDao.update(user) > 0;
    }

    @Override
    public User getUserById(Long userId) {
        if (Argument.isNotPositive(userId)) {
            return null;
        }
        return userDao.getById(userId);
    }

    @Override
    public IdentityInfo insertIdentityInfo(IdentityInfo identityInfo) {
        identityInfoDao.insert(identityInfo);
        if (identityInfo.getId() != null) {
            updateUserById(new User(identityInfo.getUserId(), VerifyStatusEnum.UNAUDITED));
        }
        return identityInfo;
    }

    @Override
    public User queryUser(Parameter map) {
        return userDao.find(map);
    }

    @Override
    public boolean updateUserById(User user) {
        return userDao.updateById(user) > 0;
    }

    @Override
    public IdentityInfo getIdentityInfo(Long userId) {
        Parameter query = Parameter.newParameter()//
        .pu("userId", userId);
        return identityInfoDao.find(query);
    }

    @Override
    public boolean updateIdentityInfoById(IdentityInfo identityInfo) {
        boolean flag = identityInfoDao.updateById(identityInfo) > 0;
        if (flag) {
            updateUserById(new User(identityInfo.getUserId(), VerifyStatusEnum.UNAUDITED));
        }
        return flag;
    }

    @Override
    public boolean isIdentity(Long userId) {
        if (userId == null) {
            return false;
        }
        User user = getUserById(userId);
        boolean flag = false;
        if (user.getVerifyStatus() == VerifyStatusEnum.NORMAL.getValue()) {
            IdentityInfo identityInfo = getIdentityInfo(user.getId());
            if (identityInfo != null) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 对字符串text进行512位加密处理
     * 
     * @param text
     * @return
     */
    public String entryptText(String text) {
        if (StringUtils.isEmpty(text)) {
            return StringUtils.EMPTY;
        }
        byte[] hashText = Digests.sha512(text.getBytes(), UserConstants.SALT, 1024);
        return Encodes.encodeHex(hashText);
    }
}
