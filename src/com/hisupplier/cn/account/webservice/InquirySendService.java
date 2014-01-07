/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.Inquiry;

/**
 * 询盘发送接口
 * @author linliuwei
 */
@WebService
public interface InquirySendService {

	/**
	 * <p>询盘发送远程调用接口
	 * <p>此接口依赖两个包 hisupplier-cas.jar, hisupplier-commons.jar
	 * @param inquiry
	 * <pre>
	 *   //必选字段
	 *   subject             主题    	
	 *   content  			 内容
	 *   fromIP              询盘发送者IP
	 *   fromSite            询盘来自站点
	 *   basketItemList      
	 *   loginUser           是否登录，null为未登录，未登录时“新用户”和“是会员”的字段都不需要
	 *   newUser             是否新用户
	 * </pre>
	 * @return 
	 * <pre>
	 *   ipLimit       IP限制(每IP每24小时内只能群发最多15封邮件，超过15封后每次只能发送1封)
	 *   passwdError   是会员：帐号或密码错误
	 *   email.used    新用户：邮箱已存在
	 *   comName.used  新用户：公司名已存在
	 *   addSuccess    发送成功
	 *   operateFail   发送失败
	 *   inquiry.repeat 15分钟内不能发送相同内容的询盘
	 * </pre>
	 */
	String send(Inquiry inquiry);
}
