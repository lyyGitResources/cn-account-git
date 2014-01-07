package com.hisupplier.cn.account.entity;

import java.io.File;
import java.util.List;

import com.hisupplier.cas.Ticket;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.cn.LocaleUtil;

public class Inquiry implements java.io.Serializable {

	private static final long serialVersionUID = 5109790643362013996L;
	private int inqId;
	private int comId;
	private int userId;
	private String subject;
	private String content;
	private int fromComId;
	private int fromUserId;
	private String fromCompany;
	private String fromName;
	private String fromEmail;
	private String fromTel;
	private String fromFax;
	private String fromProvince;
	private String fromCity;
	private String fromTown;
	private String fromStreet;
	private String fromWebsite;
	private String fromIP;
	private int fromSite;
	private String filePath;
	private int replyDay;
	private int replyCount;
	private boolean read;
	private boolean recommend;
	private int state;
	private String createTime;

	//其他扩展字段
	private boolean adminUser; //询盘接收者是否管理员用户

	//询盘报表用
	private String yearMonth; //年月，格式:yyyy-mm
	private int number; //yearMonth 的询盘数量
	private int period; //yearMonth 与当前的月份间隔

	//询盘发送与商情订阅用
	private String toName;
	private String toEmail;
	private String fromPage;// 询盘来源页面
	private boolean tradeAlert;// 是否订阅
	private String tradeAlertKeyword;// 订阅关键词
	private String tradeAlertInfoType;// 订阅信息类型

	//询盘登录和注册用
	private Ticket loginUser; //null为未登录，未登录时"新用户"和"是会员"的字段全部不显示
	private boolean newUser = true; //是否新用户，一开始默认为新用户
	private int sex;
	private String tel1;
	private String tel2;
	private String fax1;
	private String fax2;
	private String email; //邮箱帐号	
	private String passwd; //登录验证密码

	private File[] upload;
	private String[] uploadContentType;
	private String[] uploadFileName;
	private String validateCodeKey;
	private String validateCode;
	private String fromCompanyType;

	private List<BasketItem> basketItemList;
	
	private String fromIPShow;
	public String getFromIPShow() {
		if (StringUtil.isEmpty(fromIPShow)) {
			String[] path = null;
			if (StringUtil.isNotEmpty(fromIP)) {
				path = StringUtil.split(fromIP, '.');
				if (path.length == 4) {
					path[2] = "*";
					path[3] = "*";
					fromIPShow = StringUtil.join(path, '.');
				}
			}
		}
		return fromIPShow;
	}

	public String getCountryCode() {
		if (StringUtil.isNotBlank(this.getFromTown())) {
			return this.getFromTown();
		} else if (StringUtil.isNotBlank(this.getFromCity())) {
			return this.getFromCity();
		} else if (StringUtil.isNotBlank(this.getFromProvince())) {
			return this.getFromProvince();
		} else {
			return "";
		}
	}

	public String getSubjectName() {
		return StringUtil.substring(this.getSubject(), 15, "...", false);
	}

	public String getFromProvinceName() {
		String provinceName = "";
		provinceName = LocaleUtil.getProvince(this.getFromProvince());
		return provinceName;
	}

	public String getUserIdName() {
		String userIdName = "";
		if (this.isAdminUser()) {
			userIdName = "管理员";
		} else {
			userIdName = this.getEmail();
		}
		return userIdName;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getEmail() {
		return email;
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

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getToName() {
		return this.toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToEmail() {
		return this.toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getFromPage() {
		return this.fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public boolean isTradeAlert() {
		return this.tradeAlert;
	}

	public void setTradeAlert(boolean tradeAlert) {
		this.tradeAlert = tradeAlert;
	}

	public String getTradeAlertKeyword() {
		return this.tradeAlertKeyword;
	}

	public void setTradeAlertKeyword(String tradeAlertKeyword) {
		this.tradeAlertKeyword = tradeAlertKeyword;
	}

	public String getTradeAlertInfoType() {
		return this.tradeAlertInfoType;
	}

	public void setTradeAlertInfoType(String tradeAlertInfoType) {
		this.tradeAlertInfoType = tradeAlertInfoType;
	}

	public boolean isNewUser() {
		return this.newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public int getSex() {
		return this.sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
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

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public File[] getUpload() {
		return this.upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}



	public String[] getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String[] getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
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

	public int getInqId() {
		return inqId;
	}

	public void setInqId(int inqId) {
		this.inqId = inqId;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFromComId() {
		return fromComId;
	}

	public void setFromComId(int fromComId) {
		this.fromComId = fromComId;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromCompany() {
		return fromCompany;
	}

	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getFromTel() {
		return fromTel;
	}

	public void setFromTel(String fromTel) {
		this.fromTel = fromTel;
	}

	public String getFromFax() {
		return fromFax;
	}

	public void setFromFax(String fromFax) {
		this.fromFax = fromFax;
	}

	public String getFromProvince() {
		return fromProvince;
	}

	public void setFromProvince(String fromProvince) {
		this.fromProvince = fromProvince;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getFromTown() {
		return fromTown;
	}

	public void setFromTown(String fromTown) {
		this.fromTown = fromTown;
	}

	public String getFromStreet() {
		return fromStreet;
	}

	public void setFromStreet(String fromStreet) {
		this.fromStreet = fromStreet;
	}

	public String getFromWebsite() {
		return fromWebsite;
	}

	public void setFromWebsite(String fromWebsite) {
		this.fromWebsite = fromWebsite;
	}

	public String getFromIP() {
		return fromIP;
	}

	public void setFromIP(String fromIP) {
		this.fromIP = fromIP;
	}

	public int getFromSite() {
		return fromSite;
	}

	public void setFromSite(int fromSite) {
		this.fromSite = fromSite;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getReplyDay() {
		return replyDay;
	}

	public void setReplyDay(int replyDay) {
		this.replyDay = replyDay;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<BasketItem> getBasketItemList() {
		return this.basketItemList;
	}

	public void setBasketItemList(List<BasketItem> basketItemList) {
		this.basketItemList = basketItemList;
	}

	public Ticket getLoginUser() {
		return this.loginUser;
	}

	public void setLoginUser(Ticket loginUser) {
		this.loginUser = loginUser;
	}

	public String getFromCompanyType() {
		return fromCompanyType;
	}

	public void setFromCompanyType(String fromCompanyType) {
		this.fromCompanyType = fromCompanyType;
	}
}
