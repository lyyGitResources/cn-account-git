/* 
 * Created by linliuwei at 2009-1-15 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.basic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.cas.Ticket;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.cn.LocaleUtil;

/**
 * @author linliuwei
 */
public class LoginUser extends Ticket {

	private static final long serialVersionUID = -1194924674195896722L;
	private String domain;
	private String privilege;
	private int memberType;
	private String lastLoginIP;
	private boolean admin;
	private String comName;
	private String keywords;	// 公司关键词，用于判断公司信息是否完整；公司修改后需要覆盖该值
	private String websites;
	private String statSite;
	private String domainEN;
	private String memberId;
	private int state;
	private String tel;
	private String fax;
	private String province;
	private String city;
	private String town;
	private String mobile;
	private String street;
	private int sex;
	private int comCrmState;
	private int userCrmState;
	private int parentId;// >0 是第二联系人
	
	private int userState;//登录用户状态
	

	public String getStatSite() {
		return statSite;
	}

	public void setStatSite(String statSite) {
		this.statSite = statSite;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getWebsites() {
		return websites;
	}

	public void setWebsites(String websites) {
		this.websites = websites;
	}

	// uri地址是否存在于privilege中
	public boolean isHave(String uri) {
		boolean have = false;
		for (String pri : StringUtil.toArrayList(this.privilege, ",")) {
			// 特殊分组使用分组的链接验证
			if (uri.startsWith("/special")) {
				uri = "/group/";
			}
			if (uri.startsWith(pri)) {
				have = true;
			}
		}
		return have;
	}

	// 子帐号不存在菜单权限
	public boolean isShowMenu() {
		return !isSubAndNoPrivilege("/menu/");
	}

	public boolean isShowCompanyEdit() {
		return !isSubAndNoPrivilege("/member/company_edit_submit");
	}

	public boolean isShowGroup() {
		return !isSubAndNoPrivilege("/group/");
	}

	public boolean isShowFeatureProduct() {
		return !isSubAndNoPrivilege("/product/feature_product_list");
	}

	public boolean isShowAd() {
		return !isSubAndNoPrivilege("/ad/");
	}

	public boolean isShowAlert() {
		return !isSubAndNoPrivilege("/alert/");
	}

	public boolean isShowNewProduct() {
		return !isSubAndNoPrivilege("/newproduct/new_product_list");
	}

	public boolean isShowProductOrder() {
		return !isSubAndNoPrivilege("/product/product_order");
	}

	public boolean isShowWebsite() {
		Pattern pattern = Pattern.compile("\\d{17}");
		Matcher matcher = pattern.matcher(this.memberId);
		return !matcher.matches() && !isSubAndNoPrivilege("/website/");
		//	return memberType == CN.GOLD_SITE && StringUtil.isNotEmpty(domain) && !isSubAndNoPrivilege("/website/");
	}

	/**
	 * 取得CRM的同步公司信息xml
	 * @return
	 */
	public String getCompanyCrmXML() {
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding = \"utf-8\"?><request>");
		xmlStr.append("<type>corporationBinding</type>");
		xmlStr.append("<data>");
		xmlStr.append("<attr name=\"corpAccount\">" + this.getMemberId() + "</attr>");
		xmlStr.append("<attr name=\"corpName\">" + this.getComName() + "</attr>");
		xmlStr.append("<attr name=\"corpNickName\">" + this.getComName() + "</attr>");
		xmlStr.append("<attr name=\"corpPhone\">" + this.getTel() + "</attr>");
		xmlStr.append("<attr name=\"corpMobile\">" + this.getMobile() + "</attr>");
		xmlStr.append("<attr name=\"corpLinkman\">" + this.getContact() + "</attr>");
		xmlStr.append("<attr name=\"corpFax\">" + this.getFax() + "</attr>");
		xmlStr.append("<attr name=\"corpEmail\">" + this.getEmail() + "</attr>");
		xmlStr.append("<attr name=\"corpAddress\">" + this.getAddress() + "</attr>");
		xmlStr.append("</data>");
		xmlStr.append("</request>");
		return xmlStr.toString();
	}

	/**
	 * 取得CRM的同步用户信息xml
	 * @return
	 */
	public String getUserCrmXML() {
		int gender = this.getSex() == 1 ? 0 : 1;
		String accountID = this.isAdmin() ? this.getMemberId() : this.getEmail();
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding = \"utf-8\"?><request>");
		xmlStr.append("<type>userSync</type>");
		xmlStr.append("<data>");
		xmlStr.append("<attr name=\"userAccount\">" + accountID + "</attr>");
		xmlStr.append("<attr name=\"realName\">" + this.getContact() + "</attr>");
		xmlStr.append("<attr name=\"gender\">" + gender + "</attr>");
		xmlStr.append("<attr name=\"email\">" + this.getEmail() + "</attr>");
		xmlStr.append("<attr name=\"mobile\">" + this.getMobile() + "</attr>");
		xmlStr.append("<attr name=\"phone\">" + this.getTel() + "</attr>");
		xmlStr.append("<attr name=\"address\">" + this.getAddress() + "</attr>");
		xmlStr.append("<attr name=\"bindTo\">" + this.getMemberId() + "</attr>");
		xmlStr.append("<attr name=\"bindAsAdmin\">" + this.isAdmin() + "</attr>");
		if (this.isAdmin()) {
			xmlStr.append("<attr name=\"freeTryProduct\">SERVICE_TYPE_CRM</attr>");
		}
		xmlStr.append("</data>");
		xmlStr.append("</request>");
		return xmlStr.toString();
	}

	/**
	 * 取得CRM的登陆xml
	 * @return
	 */
	public String getLoginCrmXML() {
		StringBuffer xmlStr = new StringBuffer();
		String accountID = this.isAdmin() ? this.getMemberId() : this.getEmail();
		xmlStr.append("<?xml version=\"1.0\" encoding = \"utf-8\"?><request>");
		xmlStr.append("<type>userLogin</type>");
		xmlStr.append("<data>");
		xmlStr.append("<attr name=\"userAccount\">" + accountID + "</attr>");
		xmlStr.append("<attr name=\"time\">" + System.currentTimeMillis() + "</attr>");
		xmlStr.append("<attr name=\"clientIp\">" + this.getLastLoginIP() + "</attr>");
		xmlStr.append("<attr name=\"productType\">SERVICE_TYPE_CRM</attr>");
		xmlStr.append("</data>");
		xmlStr.append("</request>");
		return xmlStr.toString();
	}

	/**
	 *取完整地址
	 * @return
	 */
	private String getAddress() {
		if (LocaleUtil.getProvince(this.province).indexOf(LocaleUtil.getCity(this.city)) != -1) {
			return LocaleUtil.getProvince(this.province) + LocaleUtil.getCounty(this.town) + this.street;
		}
		return LocaleUtil.getProvince(this.province) + LocaleUtil.getCity(this.city) + LocaleUtil.getCounty(this.town) + this.street;
	}

	// 是否是子帐号并且没有权限
	private boolean isSubAndNoPrivilege(String uri) {
		if (this.memberType == CN.GOLD_SITE && !this.admin && !isHave(uri)) {
			return true;
		} else {
			return false;
		}
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public int getMemberType() {
		return memberType;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getDomainEN() {
		return domainEN;
	}

	public void setDomainEN(String domainEN) {
		this.domainEN = domainEN;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getComCrmState() {
		return comCrmState;
	}

	public void setComCrmState(int comCrmState) {
		this.comCrmState = comCrmState;
	}

	public int getUserCrmState() {
		return userCrmState;
	}

	public void setUserCrmState(int userCrmState) {
		this.userCrmState = userCrmState;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
