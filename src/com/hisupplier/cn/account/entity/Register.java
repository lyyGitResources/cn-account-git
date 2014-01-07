/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.StringUtil;


/**
 * @author linliuwei
 */
public class Register implements java.io.Serializable {

	private static final long serialVersionUID = 8900301226239939099L;
	private int comId;
	private int userId;
	private int regMode;
	private String comName;
	private String memberId;
	private String catIds;
	private String keywords;
	private String description;
	//private int economyArea;
	private int domId;
	private String businessTypes;
	//private String mainExports;
	private String websites;
	private String email;
	private String passwd;
	private String confirmPasswd;
	private String province;
	private String city;
	private String town;
	private String street;
	private String contact;
	private int sex;
	private String tel;
	private String fax;
	private String tel1;
	private String tel2;
	private String fax1;
	private String fax2;
	private String validateCodeKey;
	private String validateCode;
	private String tradeAlertKeyword;// 用于商情订阅的注册邮件

	private String regImgPath; //营业执照或个人身份证
	private String regImgPath2;//营业执照或个人身份证
	private int regImgType; // 1企业  2个人
	
	private int joinUserType; // 1=总后台注册
	
	private String regNo;
	private String ceo;
	private String isValidateGongShang;

	public String getCountryCode() {
		if (isRigthLocal(this.getTown())) {
			return this.getTown();
		} else if (isRigthLocal(this.getCity())) {
			return this.getCity();
		} else if (isRigthLocal(this.getProvince())) {
			return this.getProvince();
		} else {
			return "";
		}
	}
	
	public static boolean isRigthLocal(String local) {
		if(StringUtil.isNotBlank(local) && !"0".equals(local) && !"000000".equals(local) && !"000000000".equals(local) && !"000000000000".equals(local)){
			return true;
		}else{
			return false;
		}
	}

	public int getComId() {
		return this.comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRegMode() {
		return this.regMode;
	}

	public void setRegMode(int regMode) {
		this.regMode = regMode;
	}

	public String getComName() {
		return this.comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCatIds() {
		return this.catIds;
	}

	public void setCatIds(String catIds) {
		this.catIds = catIds;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDomId() {
		return this.domId;
	}

	public void setDomId(int domId) {
		this.domId = domId;
	}

	public String getBusinessTypes() {
		return this.businessTypes;
	}

	public void setBusinessTypes(String businessTypes) {
		this.businessTypes = businessTypes;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getConfirmPasswd() {
		return confirmPasswd;
	}

	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}

	public String getProvince() {
		return this.province;
	}

	public int getJoinUserType() {
		return joinUserType;
	}

	public void setJoinUserType(int joinUserType) {
		this.joinUserType = joinUserType;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return this.town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getSex() {
		return this.sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTradeAlertKeyword() {
		return this.tradeAlertKeyword;
	}

	public void setTradeAlertKeyword(String tradeAlertKeyword) {
		this.tradeAlertKeyword = tradeAlertKeyword;
	}

	public String getWebsites() {
		return this.websites;
	}

	public void setWebsites(String websites) {
		this.websites = websites;
	}

	public String getTel1() {
		return this.tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}

	public String getTel2() {
		return this.tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public String getFax1() {
		return this.fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return this.fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getValidateCodeKey() {
		return this.validateCodeKey;
	}

	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}

	public String getValidateCode() {
		return this.validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getRegImgPath() {
		return regImgPath;
	}

	public void setRegImgPath(String regImgPath) {
		this.regImgPath = regImgPath;
	}

	public String getRegImgPath2() {
		return regImgPath2;
	}

	public void setRegImgPath2(String regImgPath2) {
		this.regImgPath2 = regImgPath2;
	}

	public int getRegImgType() {
		return regImgType;
	}

	public void setRegImgType(int regImgType) {
		this.regImgType = regImgType;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getIsValidateGongShang() {
		return isValidateGongShang;
	}

	public void setIsValidateGongShang(String isValidateGongShang) {
		this.isValidateGongShang = isValidateGongShang;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
}
