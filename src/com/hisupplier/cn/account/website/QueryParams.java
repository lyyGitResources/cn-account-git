/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import javax.servlet.http.HttpServletRequest;

/**
 * @author taofeng
 *
 */
public class QueryParams extends com.hisupplier.cn.account.dao.QueryParams {
	private int templateType = -1;//模板类型
	private int templateColor = -1;
	private String location;

	private boolean groupFold;
	private boolean chatTip;
	private String[] talkIds;
	private String chatUserId = "";
	private String chatMsg = "";
	private String siteName = "";
	private String siteLink = "";
	private int productListStyle = 2;//默认图库排列
	private int tradeListStyle = 2;
	private int layoutNo = 1;
	private int templateNo;
	private int bannerNo = 1;
	private String nbannerPath = "";
	private String modules = "";
	//private String showroomModules = "m_trade_group,m_menu_group,m_comment,m_profile";
	private String menuGroupIds = "";

	private String product_list;
	private String product_group;
	private String product_special;
	private String product_feature;
	private String company_introduce;
	private String company_brief;
	private String company_member;
	private String service_contact;
	private String service_online;
	private String offer_list;
	private String video_list;
	private String inquiry_online;
	private String search_alert;

	private HttpServletRequest request;
	private boolean indieQR;//风格网站二维码
	private boolean showroomQR;//三级域名二维码
	private String[] bannerAddImg;//风格网站多张banner图片路径 
	private String cnFontType;
	private String cnFontSize;
	private String cnFontColor;
	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getTemplateColor() {
		return templateColor;
	}

	public void setTemplateColor(int templateColor) {
		this.templateColor = templateColor;
	}

	public int getProductListStyle() {
		return productListStyle;
	}

	public void setProductListStyle(int productListStyle) {
		this.productListStyle = productListStyle;
	}

	public int getTradeListStyle() {
		return tradeListStyle;
	}

	public void setTradeListStyle(int tradeListStyle) {
		this.tradeListStyle = tradeListStyle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getLayoutNo() {
		return layoutNo;
	}

	public void setLayoutNo(int layoutNo) {
		this.layoutNo = layoutNo;
	}

	public int getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(int templateNo) {
		this.templateNo = templateNo;
	}

	public int getBannerNo() {
		return bannerNo;
	}

	public void setBannerNo(int bannerNo) {
		this.bannerNo = bannerNo;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	public String getMenuGroupIds() {
		return menuGroupIds;
	}

	public void setMenuGroupIds(String menuGroupIds) {
		this.menuGroupIds = menuGroupIds;
	}

	public boolean isGroupFold() {
		return groupFold;
	}

	public void setGroupFold(boolean groupFold) {
		this.groupFold = groupFold;
	}

	public boolean isChatTip() {
		return chatTip;
	}

	public void setChatTip(boolean chatTip) {
		this.chatTip = chatTip;
	}

	public String getChatMsg() {
		return chatMsg;
	}

	public void setChatMsg(String chatMsg) {
		this.chatMsg = chatMsg;
	}

	public String getChatUserId() {
		return chatUserId;
	}

	public void setChatUserId(String chatUserId) {
		this.chatUserId = chatUserId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteLink() {
		return siteLink;
	}

	public void setSiteLink(String siteLink) {
		this.siteLink = siteLink;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getProduct_list() {
		return product_list;
	}

	public void setProduct_list(String product_list) {
		this.product_list = product_list;
	}

	public String getProduct_group() {
		return product_group;
	}

	public void setProduct_group(String product_group) {
		this.product_group = product_group;
	}

	public String getProduct_special() {
		return product_special;
	}

	public void setProduct_special(String product_special) {
		this.product_special = product_special;
	}

	public String getProduct_feature() {
		return product_feature;
	}

	public String getNbannerPath() {
		return nbannerPath;
	}

	public void setNbannerPath(String nbannerPath) {
		this.nbannerPath = nbannerPath;
	}

	public void setProduct_feature(String product_feature) {
		this.product_feature = product_feature;
	}

	public String getCompany_introduce() {
		return company_introduce;
	}

	public void setCompany_introduce(String company_introduce) {
		this.company_introduce = company_introduce;
	}

	public String getCompany_brief() {
		return company_brief;
	}

	public void setCompany_brief(String company_brief) {
		this.company_brief = company_brief;
	}

	public String getCompany_member() {
		return company_member;
	}

	public void setCompany_member(String company_member) {
		this.company_member = company_member;
	}

	public String getService_contact() {
		return service_contact;
	}

	public void setService_contact(String service_contact) {
		this.service_contact = service_contact;
	}

	public String getService_online() {
		return service_online;
	}

	public void setService_online(String service_online) {
		this.service_online = service_online;
	}

	public String getOffer_list() {
		return offer_list;
	}

	public void setOffer_list(String offer_list) {
		this.offer_list = offer_list;
	}

	public String getVideo_list() {
		return video_list;
	}

	public void setVideo_list(String video_list) {
		this.video_list = video_list;
	}

	public String getInquiry_online() {
		return inquiry_online;
	}

	public void setInquiry_online(String inquiry_online) {
		this.inquiry_online = inquiry_online;
	}

	public String[] getTalkIds() {
		return talkIds;
	}

	public void setTalkIds(String[] talkIds) {
		this.talkIds = talkIds;
	}

	public String getSearch_alert() {
		return search_alert;
	}

	public void setSearch_alert(String search_alert) {
		this.search_alert = search_alert;
	}

	public boolean isIndieQR() {
		return indieQR;
	}

	public void setIndieQR(boolean indieQR) {
		this.indieQR = indieQR;
	}

	public boolean isShowroomQR() {
		return showroomQR;
	}

	public void setShowroomQR(boolean showroomQR) {
		this.showroomQR = showroomQR;
	}

	public String[] getBannerAddImg() {
		return bannerAddImg;
	}

	public void setBannerAddImg(String[] bannerAddImg) {
		this.bannerAddImg = bannerAddImg;
	}

	public String getCnFontType() {
		return cnFontType;
	}

	public void setCnFontType(String cnFontType) {
		this.cnFontType = cnFontType;
	}

	public String getCnFontSize() {
		return cnFontSize;
	}

	public void setCnFontSize(String cnFontSize) {
		this.cnFontSize = cnFontSize;
	}

	public String getCnFontColor() {
		return cnFontColor;
	}

	public void setCnFontColor(String cnFontColor) {
		this.cnFontColor = cnFontColor;
	}
}
