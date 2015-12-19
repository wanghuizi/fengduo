package com.fengduo.bee.service.cache.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fengduo.bee.commons.redis.JedisUtils;
import com.fengduo.bee.model.entity.User;
import com.fengduo.bee.service.cache.interfaces.UserCacheService;
import com.fengduo.bee.service.constants.UserConstants;
import com.fengduo.bee.service.interfaces.UserService;

/**
 * 
 * 类UserCacheServiceImpl.java的实现描述：TODO 类实现描述
 * 
 * @author jie.xu
 * @date 2015年7月7日 上午10:58:56
 */
@Service("userCacheService")
public class UserCacheServiceImpl implements UserCacheService {

	private static int USERCACHE_EXPIRE = 60 * 60 * 3; // 3小时

	@Autowired
	private UserService userService;

	@Override
	public User getById(Long userId) {
		String key = UserConstants.USER_CACHE_KEY + userId;
		Object user = JedisUtils.getObject(key);
		if (user != null && user instanceof User) {
			return (User) user;
		}
		User u = userService.getUserById(userId);
		if (u != null) {
			JedisUtils.setObject(key, u, USERCACHE_EXPIRE);
		}
		return u;
	}

	@Override
	public void clearUserCache(Long userId) {
		String key = UserConstants.USER_CACHE_KEY + userId;
		JedisUtils.delObject(key);
	}

}
