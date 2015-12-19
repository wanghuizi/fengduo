package com.fengduo.bee.service.interfaces;

import com.fengduo.bee.commons.persistence.Parameter;
import com.fengduo.bee.model.entity.IdentityInfo;
import com.fengduo.bee.model.entity.User;

/**
 * 会员服务接口
 * 
 * @author jie.xu
 * @date 2015年6月9日 下午4:49:49
 */
public interface UserService {

    /**
     * 插入用户基础信息
     */
    User insertUser(User user);

    /**
     * 插入认证信息
     */
    IdentityInfo insertIdentityInfo(IdentityInfo identityInfo);

    /**
     * 查询用户
     */
    User queryUser(Parameter map);

    /**
     * 根据ID查询用户
     */
    User getUserById(Long userId);

    /**
     * 更新用户
     */
    boolean updateUserById(User user);

    /**
     * 更新用户密码
     */
    boolean updateUserPwd(Long userId, String password);

    /**
     * 获得认证信息
     */
    IdentityInfo getIdentityInfo(Long userId);

    /**
     * 更新认证信息
     */
    boolean updateIdentityInfoById(IdentityInfo identityInfo);

    /**
     * 用户是否进行实名认证 true:认证过，false:未认证
     */
    boolean isIdentity(Long userId);
}
