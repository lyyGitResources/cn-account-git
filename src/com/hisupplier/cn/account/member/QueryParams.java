/* 
 * Created by linliuwei at 2009-10-27 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.member;

import com.hisupplier.commons.util.StringUtil;

/**
 * @author linliuwei
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {

	private int userId = -1;//���ʺ�Id
	private String email;
	private String newUserEmail; // ��֤�ʼ�ʹ�ã���Ϊ��js�У�ȡ����email��ֵ
	private String fromEmail; // ��֤�ʼ�ʹ��
	private boolean adminUser = false;
	private String memberId;
	private String fromCompany; // ��֤��˾ʹ��
	private String comName;
	
	private String oldPasswd; // �޸�����ʹ��

	private String address; // ���ӵ�ͼ��ַ

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		if (StringUtil.isNotBlank(this.newUserEmail)) {
			return this.newUserEmail;
		}
		if (StringUtil.isNotBlank(this.fromEmail)) {
			return this.fromEmail;
		}
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdminUser() {
		return this.adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public String getNewUserEmail() {
		return newUserEmail;
	}

	public void setNewUserEmail(String newUserEmail) {
		this.newUserEmail = newUserEmail;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFromCompany() {
		return fromCompany;
	}

	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}

	public String getOldPasswd() {
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}

}
