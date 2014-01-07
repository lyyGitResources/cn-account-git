package com.hisupplier.cn.account.inquiry;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.webservice.InquirySendService;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.basket.BasketFactory;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class InquirySendAction extends ActionSupport implements ModelDriven<Inquiry> {

	private static final long serialVersionUID = 2271954573642365431L;
	public static final String[] ALLOW_MIME_TYPE = { "image/jpeg", "image/pjpeg", "image/gif", "image/png", "text/plain", "application/pdf", "application/msword", "application/vnd.ms-excel","application/x-msexcel",
			"application/zip" };
	private InquirySendService inquirySendService;
	private Inquiry inquiry = new Inquiry();
	private String autoLoginURL;

	public String inquiry() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String fromPage = request.getHeader("referer");
		if (StringUtil.isEmpty(fromPage)) {
			fromPage = Config.getString("sys.base");
		}
		inquiry.setFromPage(fromPage);
		inquiry.setFromWebsite("http://");
		inquiry.setLoginUser(CASClient.getInstance().getUser(request));
		List<BasketItem> basketItemList = BasketFactory.getBasket(request, "cn").getItems(request, response);
		for (int i = 0; i < basketItemList.size(); i++) {
			List<BasketItem.Product> productList = basketItemList.get(i).getProductList();
			String showroomUrl = "http://" + basketItemList.get(i).getMemberId() + ".cn." + Config.getString("sys.domain");
			for (int j = 0; j < productList.size(); j++) {
				productList.get(j).setProUrl(showroomUrl + "/product/detail-" + productList.get(j).getProId() + ".html");
			}

			List<BasketItem.Trade> tradeList = basketItemList.get(i).getTradeList();
			for (int k = 0; k < tradeList.size(); k++) {
				tradeList.get(k).setProUrl(showroomUrl + "/offer/detail-" + tradeList.get(k).getTradeId() + ".html");
			}
		}
		inquiry.setBasketItemList(basketItemList);
		if (inquiry.getBasketItemList().size() <= 0) {
			return "basket";
		}

		// 设置询盘主题
		String subject = "";
		if (inquiry.getBasketItemList().size() == 1) {
			BasketItem item = inquiry.getBasketItemList().get(0);
			if (item.getProductList().size() == 1 && item.getTradeList().size() == 0) {
				subject = "关于" + item.getProductList().get(0).getProName() + "的询盘";
			} else if (subject.equals("") && item.getTradeList().size() == 1 && item.getProductList().size() == 0) {
				subject = "关于" + item.getTradeList().get(0).getTradeName() + "的询盘";
			} else {
				subject = "关于产品的询盘";
			}
		} else {
			subject = "关于产品的询盘";
		}
		inquiry.setSubject(subject);

		//设置订阅商情参数
		if (StringUtil.isEmpty(inquiry.getTradeAlertKeyword()) && inquiry.getBasketItemList().size() > 0) {
			String tradeAlertKeyword = "";
			if (inquiry.getBasketItemList().get(0).getProductList().size() > 0) {
				tradeAlertKeyword = inquiry.getBasketItemList().get(0).getProductList().get(0).getProName();
			} else if (inquiry.getBasketItemList().get(0).getTradeList().size() > 0) {
				tradeAlertKeyword = inquiry.getBasketItemList().get(0).getTradeList().get(0).getTradeName();
			}
			inquiry.setTradeAlertKeyword(tradeAlertKeyword);
			inquiry.setTradeAlertInfoType("product");
		}
		return SUCCESS;
	}

	public String inquiry_send() throws Exception {
		/*
		//页面上fromEmail 和email的值在输入错误返回的页面需要相互取数据，因此这样赋值
		if (StringUtil.isNotEmpty(inquiry.getEmail())) {
			inquiry.setFromEmail(inquiry.getEmail());
		} else if (StringUtil.isNotEmpty(inquiry.getFromEmail())) {
			inquiry.setEmail(inquiry.getFromEmail());
		}
		*/
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		if (inquiry.getBasketItemList() == null || inquiry.getBasketItemList().size() <= 0) {
			return "basket";
		}
		String result = inquirySendService.send(inquiry);
		if (!result.equals("addSuccess")) {
			if (result.equals("ipLimit")) {
				this.addActionError("每IP每24小时内只能群发最多15封邮件，超过15封后每次只能发送1封");
			} else if (result.equals("passwdError")) {
				this.addActionError("会员帐号，邮箱不存在或者密码错误");
			} else if (result.equals("email.used")) {
				this.addActionError("邮箱地址已存在，请重新输入");
			} else if (result.equals("comName.used")) {
				this.addActionError("公司名称已存在，请重新输入");
			} else if (result.equals("operateFail")) {
				this.addActionError("询盘发送失败，请返回重试");
			} else if (result.equals("inquiry.repeat")) {
				this.addActionError("对不起，短时间内不能连续提交相同内容的询盘，请稍后再试。");
			} else if (result.equals("alert.limit")){
				this.addActionError("主账号和子账号共能订阅10条商情,您已订满10条商情");
			}
			return INPUT;
		}
		BasketFactory.removeBasket(request, response);
		// 已登录
		if (inquiry.getLoginUser() != null) {
			autoLoginURL = "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
		} else {
			// 是会员
			String returnUrl = "";
			if (!inquiry.isNewUser()) {
				if (Config.getString("isBig5").equalsIgnoreCase("true")) {
					returnUrl = "http://account.big5." + Config.getString("sys.domain") + "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
				} else {
					returnUrl = "http://account.cn." + Config.getString("sys.domain") + "/user/inquiry_success.htm?fromPage=" + Coder.encodeURL(inquiry.getFromPage());
				}
				autoLoginURL = CASClient.getInstance()
						.getAutoLoginURL(returnUrl, inquiry.getEmail(), inquiry.getPasswd());
			} else {
				// 新用户
				String account_domain = "http://account.cn." + Config.getString("sys.domain");
				if (Config.getString("isBig5").equalsIgnoreCase("true")) {
					account_domain = "http://account.big5." + Config.getString("sys.domain");
				}
				returnUrl = account_domain 
							+ "/user/inquiry_success.htm?fromPage="
							+ Coder.encodeURL(inquiry.getFromPage());
				autoLoginURL = returnUrl;
			}
		}
		return SUCCESS;
	}

	public String inquiry_success() throws Exception {
		return SUCCESS;
	}

	public void validateInquiry_send() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		inquiry.setFromIP(WebUtil.getRemoteIP(request));
		inquiry.setLoginUser(CASClient.getInstance().getUser(request));
		inquiry.setBasketItemList(BasketFactory.getBasket(request, "cn").getItems(request, response));

		StringUtil.trimToEmpty(inquiry, "subject,fromCompany,fromName,fromEmail,fromProvince,fromCity,fromTown,fromStreet,fromWebsite,email,passwd");

		if (StringUtil.isEmpty(inquiry.getSubject()) || inquiry.getSubject().length() > 120) {
			this.addActionError("请输入询盘主题，长度120个字符内");
		}
		if (StringUtil.isEmpty(inquiry.getContent()) || inquiry.getContent().length() > 3000) {
			this.addActionError("请输入询盘内容，长度3000个字符内");
		}
		if (inquiry.getUpload() != null) {
			for (int i = 0; i < inquiry.getUpload().length; i++) {
				if (!StringUtil.containsValue(ALLOW_MIME_TYPE, inquiry.getUploadContentType()[i]) || inquiry.getUpload()[i].length() / 1024 > 500) {
					this.addActionError(TextUtil.getText("attachment.invalid", "zh"));
				}
			}
		}
		
		// 未登录
		if (inquiry.getLoginUser() == null) {
			// 是会员
			if (!inquiry.isNewUser()) {
				if (!"ok".equals(Validator.isMemberId(inquiry.getEmail())) && !Validator.isEmail(inquiry.getEmail())) {
					this.addActionError("请输入会员帐号或邮箱");
				}
				if (!Validator.isPassword(inquiry.getPasswd())) {
					this.addActionError("请输入登录密码");
				}
			} else {
				// 新用户
				if (!Validator.isEmail(inquiry.getFromEmail())) {
					this.addActionError("请输入有效的电子邮箱，便于找回密码");
				}
				if ("company".equals(inquiry.getFromCompanyType())) {
					if (StringUtil.isEmpty(inquiry.getFromCompany()) 
							|| inquiry.getFromCompany().length() > 120) {
						this.addActionError("请输入公司名称，长度120个字符内");
					} else {
						inquiry.setFromCompany(
								inquiry.getFromCompany().replace(" ", ""));
					}
					/*
					if (StringUtil.isEmpty(inquiry.getFromProvince())) {
						this.addActionError("请选择省份");
					}
					if (StringUtil.isEmpty(inquiry.getFromCity())) {
						this.addActionError("请选择城市");
					}
					if (StringUtil.isEmpty(inquiry.getFromStreet()) || inquiry.getFromStreet().length() > 100) {
						this.addActionError("请输入街道地址，长度在120个字符内");
					}
					*/
				}

				if (StringUtil.isEmpty(inquiry.getFromName()) 
						|| inquiry.getFromName().length() > 20) {
					this.addActionError("请输入联系人姓名，长度20个字符内");
				}

				if (Validator.isTel(inquiry.getTel1(), inquiry.getTel2())) {
					inquiry.setFromTel(inquiry.getTel1() + "-" + inquiry.getTel2());
				} else {
					this.addActionError("请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用\",\"或\"/\"分隔，分机号码请用\"-\"分隔");
				}

				if (StringUtil.isNotEmpty(inquiry.getFax1()) && StringUtil.isNotEmpty(inquiry.getFax2())) {
					if (Validator.isTel(inquiry.getFax1(), inquiry.getFax2())) {
						inquiry.setFromFax(inquiry.getFax1() + "-" + inquiry.getFax2());
					} else {
						this.addActionError("请输入区号和传真号码，区号长度3-5位数字，传真号码长度在7-26个字符内，多个传真用','或\"/\"分隔，分机号码请用\"-\"分隔");
					}
				} else {
					inquiry.setFromFax("");
				}

				if (StringUtil.isNotEmpty(inquiry.getFromWebsite())) {
					if (!Validator.isUrl(inquiry.getFromWebsite()) || inquiry.getFromWebsite().length() > 100) {
						this.addActionError("请输入有效的公司网址，长度在100个字符内");
					} else {
						String website = inquiry.getFromWebsite().toLowerCase();
						if (!website.startsWith("http://") && !website.startsWith("https://")) {
							inquiry.setFromWebsite("http://" + website);
						}
					}
				}
			}
		}

		if (!ValidateCode.isValid(request, inquiry.getValidateCodeKey(), inquiry.getValidateCode())) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}

		if (this.hasActionErrors()) {
			inquiry.setPasswd("");
		}
	}

	public Inquiry getModel() {
		return inquiry;
	}

	public Inquiry getInquiry() {
		return inquiry;
	}

	public String getAutoLoginURL() {
		return autoLoginURL;
	}

	public void setInquirySendService(InquirySendService inquirySendService) {
		this.inquirySendService = inquirySendService;
	}

}
