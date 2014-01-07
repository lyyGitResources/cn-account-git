package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

public class AdOrder extends com.hisupplier.commons.entity.cn.AdOrder {

	private static final long serialVersionUID = 1322847227511999078L;

	//用于广告升级邮件
	private String catName;//目录名
	private String memberId;
	private String comName;
	private String contact;
	private String email;
	private String tel;
	private String fax;
	private String mobile;

	public String getAdTypeName() {
		String adTypeName = "";
		if (this.getAdType() == 1) {
			adTypeName = "目录结果页";
		} else if (this.getAdType() == 2) {
			adTypeName = "搜索结果页";
		} else if (this.getAdType() == 3) {
			adTypeName = "目录和搜索结果页";
		}
		return adTypeName;
	}

	public String getShortRemark() {
		return StringUtil.substring(this.getRemark(), 35, "...", false);
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

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

}
