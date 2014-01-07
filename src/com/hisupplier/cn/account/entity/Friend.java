/* 
 * Created by baozhimin at Dec 8, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

/**
 * @author baozhimin
 */
public class Friend extends com.hisupplier.commons.entity.cn.Friend {

	private static final long serialVersionUID = -493566897704109376L;
	private String validateCode; 	// 验证码
	private String validateCodeKey; // 验证码key

	public String getDefaultLink() {
		if(StringUtil.isBlank(this.getLink())){
			return "http://";
		}else{
			return this.getLink();
		}
	}
	
	public String getDefaultLinkus() {
		if(StringUtil.isBlank(this.getLinkus())){
			return "http://";
		}else{
			return this.getLinkus();
		}
	}
	
	public String getValidateCode() {
		return validateCode;
	}
	
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	
	public String getValidateCodeKey() {
		return validateCodeKey;
	}
	
	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}
}
