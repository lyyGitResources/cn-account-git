/* 
 * Created by linliuwei at 2009-11-12 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author linliuwei
 */
public class TradeAlert implements java.io.Serializable {

	private static final long serialVersionUID = -3155412352892098409L;
	private int id;
	private int comId;
	private int userId;
	private String keyword;
	private String catIds;
	private boolean product;
	private boolean sell;
	private boolean buy;
	private boolean company;
	private boolean enable;
	private String createTime;

	//主站页面-简易商情订阅用
	private String mode; //订阅方式: keyword, category
	private boolean member;
	private String email;//用于简易商情订阅     
	private String passwd;
	private String confirmPasswd;
	private String comName;
	private String province;
	private String city;
	private String town;

	private String contact;
	private int sex;
	private String tel;
	private String tel1;
	private String tel2;
	private String validateCodeKey;
	private String validateCode;
	
	private int joinUserType;//1=总后台注册

	/**
	 * 返回信息类型
	 * @return
	 */
	public String getInfoTypeImg() {
		StringBuffer imgBuffer = new StringBuffer();
		if (this.isProduct() || this.isSell()) {
			imgBuffer.append("<img src ='/img/ico/chosen.gif'>").append("供应信息 <br />");
		} else {
			imgBuffer.append("<img src ='/img/ico/chosen2.gif'>").append("供应信息 <br />");
		}
		if (this.isBuy()) {
			imgBuffer.append("<img src ='/img/ico/chosen.gif'>").append("求购信息<br />");
		} else {
			imgBuffer.append("<img src ='/img/ico/chosen2.gif'>").append("求购信息<br />");
		}
		if (this.isCompany()) {
			imgBuffer.append("<img src ='/img/ico/chosen.gif'>").append("公司信息");
		} else {
			imgBuffer.append("<img src ='/img/ico/chosen2.gif'>").append("公司信息");
		}
		return imgBuffer.toString();
	}

	public String getCountryCode() {
		if (Register.isRigthLocal(this.getTown())) {
			return this.getTown();
		} else if (Register.isRigthLocal(this.getCity())) {
			return this.getCity();
		} else if (Register.isRigthLocal(this.getProvince())) {
			return this.getProvince();
		} else {
			return "";
		}
	}

	/**
	 * 返回行业目录名称路径
	 * @param catId
	 * @return
	 */
	public String[] getCatNamePaths() {
		if (StringUtil.isEmpty(getCatIds())) {
			return null;
		}
		String[] ids = getCatIds().split(",");
		String[] names = new String[ids.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = CategoryUtil.getNamePath(Integer.parseInt(ids[i]), " >> ");
		}
		return names;
	}

	public String getCatName() {
		String catName = "";
		if (StringUtil.isEmpty(this.getKeyword()) && StringUtil.isNotEmpty(this.getCatIds())) {
			String[] catId = StringUtil.toArray(this.getCatIds(), ",");
			if (catId.length == 1) {
				catName = CategoryUtil.getNamePath(Integer.parseInt(catId[0]), " >> ");
			}
		}
		return catName;
	}

	public String getEnableName() {
		return this.isEnable() ? "订阅中" : "已取消";
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

	public String getValidateCodeKey() {
		return validateCodeKey;
	}

	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}

	public int getJoinUserType() {
		return joinUserType;
	}

	public void setJoinUserType(int joinUserType) {
		this.joinUserType = joinUserType;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isMember() {
		return this.member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCatIds() {
		return catIds;
	}

	public void setCatIds(String catIds) {
		this.catIds = catIds;
	}

	public boolean isProduct() {
		return product;
	}

	public void setProduct(boolean product) {
		this.product = product;
	}

	public boolean isSell() {
		return sell;
	}

	public void setSell(boolean sell) {
		this.sell = sell;
	}

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
