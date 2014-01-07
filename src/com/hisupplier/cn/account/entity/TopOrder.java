package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

public class TopOrder extends com.hisupplier.commons.entity.cn.TopOrder {

	private static final long serialVersionUID = 1322847227511999078L;

	//用于Topsite订购邮件
	private String memberId;
	private String comName;
	private String contact;
	private String email;
	private String tel;
	private String fax;
	private String mobile;
	
	public String getTopTypeName() {
		String topTypeName = "";
		if (this.getTopType() == 1) {
			topTypeName = "第1页";
		} else if (this.getTopType() == 2) {
			topTypeName = "第1页第1位";
		}
		return topTypeName;
	}
	
	public String getShortRemark() {
		return StringUtil.substring(this.getRemark(), 25, "...", false);
	}
	
	public String getShortKeyword() {
		return StringUtil.substring(this.getKeyword(), 9, "...", false);
	}
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
