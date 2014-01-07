package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;

public class AdOrder extends com.hisupplier.commons.entity.cn.AdOrder {

	private static final long serialVersionUID = 1322847227511999078L;

	//���ڹ�������ʼ�
	private String catName;//Ŀ¼��
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
			adTypeName = "Ŀ¼���ҳ";
		} else if (this.getAdType() == 2) {
			adTypeName = "�������ҳ";
		} else if (this.getAdType() == 3) {
			adTypeName = "Ŀ¼���������ҳ";
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
