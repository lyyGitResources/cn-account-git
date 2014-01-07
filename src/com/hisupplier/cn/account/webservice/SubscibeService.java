/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.TradeAlert;

/**
 * 订阅接口
 * @author linliuwei
 */
@WebService
public interface SubscibeService {

	/**
	 * <p>商情订阅远程调用接口，分两种情况
	 * <p>1) 邮箱已注册，直接添加商情订阅
	 * <p>2) 未注册，先注册再添加商情订阅
	 * @param alert
	 * @return 
	 * <pre>
	 *   alert.limit           数量超过限制
	 *   alert.keyword.used    关键词已存在
	 *   addSuccess            订阅成功
	 *   operateFail           订阅失败
	 * </pre>
	 */
	String subscibe(TradeAlert alert);
}
