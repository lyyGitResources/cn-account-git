/* 
 * Created by taofeng at 2010-7-14 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import java.util.Map;

import javax.jws.WebService;

import com.hisupplier.commons.entity.cn.Company;
import com.hisupplier.commons.entity.cn.User;

/**
 * @author taofeng
 */
@WebService
public interface MemberService {

	/**
	 * 按公司ID获取公司详细信息
	 * @param comId
	 * @return Company
	 */
	public Company getCompany(int comId);

	/**
	 * <pre>
	 * 按公司ID或用户ID获取用户信息
	 * ps:当用户ID>0且公司ID=0时按用户ID获取用户信息
	 *    当公司ID>0且用户ID=0时按公司ID获取管理员用户信息
	 * </pre>
	 * @param userId
	 * @param comId
	 * @return User
	 */
	public User getUser(int userId, int comId);

	/**
	 * <pre>
	 * 更新公司扩展表指定字段
	 * ps:在原有值上+value
	 * </pre>
	 * @param comId
	 * @param column key=字段名，value=增减值
	 * @return String suc=成功 err=失败
	 */
	public String updateComExtra(int comId, Map<String, Integer> column);

}
