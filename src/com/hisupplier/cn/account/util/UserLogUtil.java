/* 
 * Created by baozhimin at Dec 30, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.util;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author baozhimin
 */
public class UserLogUtil {

	/**
	 * 得到UserLog对象
	 * @param logType
	 * @param operate
	 * @param content
	 * @param loginUser
	 * @return
	 */
	public static UserLog getUserLog(String logType, int operate, String content, LoginUser loginUser){
		UserLog userLog = new UserLog();
		userLog.setComId(loginUser.getComId());
		userLog.setUserId(loginUser.getUserId());
		userLog.setUserIP(loginUser.getLastLoginIP());
		userLog.setLogType(logType);
		userLog.setOperate(operate);
		userLog.setContent(content);
		userLog.setCreateTime(new DateUtil().getDateTime());
		return userLog;
	}
}
