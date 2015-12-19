package com.fengduo.bee.service.cache.interfaces;

import com.fengduo.bee.model.entity.User;

/**
 * 
 * 类UserCacheService.java的实现描述：用户信息缓存
 * 
 * @author jie.xu
 * @date 2015年7月7日 上午10:48:43
 */
public interface UserCacheService {

	/**
	 * 缓存中获取用户信息
	 */
	User getById(Long userId);

	/**
	 * 清除用户缓存
	 */
	void clearUserCache(Long userId);

}
