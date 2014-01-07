/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import javax.jws.WebService;

import com.hisupplier.cn.account.entity.Register;

/**
 * 注册会员接口
 * @author linliuwei
 */
@WebService
public interface RegisterService {

	/**
	 * 简易注册远程调用接口
	 * @param register
	 * <pre>
	 *   //公司信息
	 *   regMode        注册方式，默认值主站正常注册
	 *   comName        公司名
	 *   memberId       (可选)会员帐号，默认值17位数的数字(当前时间精确到毫秒)
	 *   catIds         (可选)行业目录，默认值空(最底层目录ID，多个用逗号分隔)
	 *   keywords       (可选)产品关键词，默认值空
	 *   description    (可选)详细描述，默认值空
	 *   domId          (可选)产业带，默认值空
	 *   economyArea    (可选)经济区域，默认值华北地区
	 *   businessTypes  (可选)公司类型，默认值空
	 *   mainExports    (可选)主要销售地区，默认值空
	 *   websites       (可选)公司网址，默认值空
	 *   
	 *   //管理员帐号信息
	 *   email          邮箱
	 *   passwd         密码
	 *   province       省份的数字代号
	 *   city           城市的数字代号
	 *   town           地区的数字代号，如果没有就用12个0代替
	 *   contact        联系人姓名
	 *   sex            性别
	 *   tel            电话
	 *   fax            (可选)传真，默认值空
	 *   street         (可选)街道地址，默认值空
	 * </pre>
	 * @return 
	 * <pre>
	 *   memberId.used     会员帐号已存在
	 *   email.used        邮箱已存在
	 *   comName.used      公司名已存在
	 *   addSuccess        注册成功
	 *   operateFail       注册失败
	 * </pre>
	 */
	String join(Register register);
}
