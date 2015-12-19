/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.constants;

import com.fengduo.bee.commons.core.utils.Encodes;

/**
 * 默认的加密密钥(目前代码写死)
 * 
 * @author xujie Jun 18, 2015 10:31:52 PM
 */
public interface UserConstants {

	/**
	 * 默认的秘钥参数
	 */
	byte[] SALT = Encodes.decodeHex("c5894389dcda3e09");

	String USER_CACHE_KEY = "userCache_";
}
