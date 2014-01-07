package com.hisupplier.cn.account.entity;

public class UpgradeMail extends com.hisupplier.commons.entity.cn.UpgradeMail {

	private static final long serialVersionUID = 1322847227511999078L;

	//用于会员升级邮件
	private String upTypeName;//会员升级类型名
	private String memberId;
	private String comName;
	private String contact;
	private String email;
	private String tel;
	private String fax;
	private String mobile;

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

	public String getUpTypeName() {
		return upTypeName;
	}

	public void setUpTypeName(String upTypeName) {
		this.upTypeName = upTypeName;
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
