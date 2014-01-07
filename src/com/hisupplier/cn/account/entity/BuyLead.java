/* 
 * Created by baozhimin at Dec 7, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;


/**
 * @author baozhimin
 */
public class BuyLead extends com.hisupplier.commons.entity.cn.Product {

	private static final long serialVersionUID = 6462792753847895723L;
	
	private String[] keywordArray;
	private boolean loginUser;
	private boolean newUser = true;
	private String newUserEmail;
	private String newUserPasswd;
	private String confirmPasswd;
	private String comName;
	private String catName;
	private String province;
	private String city;
	private String town;
	private String contact;
	private int sex;
	private String tel;
	private String tel1;
	private String tel2;
	private String email;
	private String passwd;
	private String validateCodeKey;
	private String validateCode;
	
	public String getCountryCode(){
		if(Register.isRigthLocal(this.getTown())){
			return this.getTown();
		}else if(Register.isRigthLocal(this.getCity())){
			return this.getCity();
		}else if(Register.isRigthLocal(this.getProvince())) {
			return this.getProvince();
		}else{
			return "";
		}
	}
	
	public String[] getKeywordArray() {
		if(this.keywordArray == null || this.keywordArray.length < 0){
			return new String[]{"", "", ""};
		}else{
			for (int i = this.keywordArray.length - 1; i < 3; i++) {
				keywordArray[i] = "";
			}
			return keywordArray;
		}
	}

	public void setKeywordArray(String[] keywordArray) {
		this.keywordArray = keywordArray;
	}

	public boolean isLoginUser() {
		return loginUser;
	}

	public void setLoginUser(boolean loginUser) {
		this.loginUser = loginUser;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public String getNewUserEmail() {
		return newUserEmail;
	}

	public void setNewUserEmail(String newUserEmail) {
		this.newUserEmail = newUserEmail;
	}

	public String getNewUserPasswd() {
		return newUserPasswd;
	}

	public void setNewUserPasswd(String newUserPasswd) {
		this.newUserPasswd = newUserPasswd;
	}

	public String getConfirmPasswd() {
		return confirmPasswd;
	}

	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getTel1() {
		return tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getValidateCodeKey() {
		return validateCodeKey;
	}

	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
