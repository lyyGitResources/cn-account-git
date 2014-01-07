/* 
 * Created by baozhimin at Dec 9, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.BuyLead;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class PostBuyLeadAction extends BasicAction implements ModelDriven<BuyLead> {

	private static final long serialVersionUID = 7586243040578276821L;
	private BuyLead buyLead = new BuyLead();
	private B2BService b2BService;
	private String autoLoginURL;
	private Map<String, Object> result = new HashMap<String, Object>();

	public String post_buy_lead() throws Exception {
		if (CASClient.getInstance().isLogin(request)) {
			buyLead.setLoginUser(true);
		}
		result.put("headDescription", "海商网求购信息发布，求购信息，求购商机，贸易机会，商业信息，供求信息");
		result.put("navbar", "简易发布求购信息");
		return SUCCESS;
	}

	public String post_buy_lead_submit() throws Exception {
		tip = this.b2BService.addBuyLead(request, buyLead);
		if (tip.equals("isBlocked")) {
			this.addActionError("您的账号已被冻结，详情请咨询客服人员：Kitty  工作QQ：239183271   邮箱：service10@hi.cc");
			return INPUT;
		}
		if (!StringUtil.equals(tip, "addSuccess")) {
			this.addActionError(getText(tip));
			return INPUT;
		}
		if (buyLead.isLoginUser()) {
			autoLoginURL = "/trade/trade_list.htm";
		} else {
			String autoUrl = Config.getString("account.base") + "/user/post_buy_lead_success.htm";
			if (buyLead.isNewUser()) {
				autoLoginURL = CASClient.getInstance().getAutoLoginURL(autoUrl + "?newUserEmail=" + buyLead.getNewUserEmail(), buyLead.getNewUserEmail(), buyLead.getNewUserPasswd());
			} else {
				autoLoginURL = CASClient.getInstance().getAutoLoginURL(autoUrl, buyLead.getEmail(), buyLead.getPasswd());
			}
		}
		result.put("headDescription", "海商网求购信息发布成功，求购信息，求购商机，贸易机会，商业信息，供求信息");
		return super.execute();
	}

	public String post_buy_lead_success() throws Exception {
		result.put("headDescription", "海商网求购信息发布成功，求购信息，求购商机，贸易机会，商业信息，供求信息");
		return super.execute();
	}

	public void validatePost_buy_lead_submit() {
		result.put("headDescription", "海商网求购信息发布，求购信息，求购商机，贸易机会，商业信息，供求信息");
		result.put("navbar", "简易发布求购信息");
		/*
		if (buyLead.getKeywordArray() == null) {
			buyLead.setKeywords("");
		} else {
			String keywords = "";
			for (String keyword : buyLead.getKeywordArray()) {
				if (keyword.trim().length() > 60) {
					this.addFieldError("buyLead.keywords.maxlength", getText("buyLead.keywords.maxlength","每个关键词长度最大为60个字符"));
				} else {
					keywords += "," + keyword.trim();
				}
			}
			buyLead.setKeywords(StringUtil.trimComma(keywords));
		}
		*/

		StringUtil.trimToEmpty(buyLead, "proName,email,comName,contact,tel1,tel2,brief,description,keywords");

		if (buyLead.getProName().length() > 120 || buyLead.getProName().length() < 1) {
			this.addFieldError("buyLead.proName.required", getText("buyLead.proName.required","请输入求购主题，长度120个字符内"));
		}
		if (buyLead.getCatId() <= 0) {
			this.addFieldError("buyLead.catId.required", getText("buyLead.catId.required","请选择行业目录"));
		}
		if (buyLead.getBrief().length() > 150) {
			this.addFieldError("buyLead.brief.maxlength", getText("buyLead.brief.maxlength","请填写商情摘要，长度150个字符内"));
		}
		if (buyLead.getDescription().length() > 20000) {
			this.addFieldError("buyLead.description.maxlength", getText("buyLead.description.maxlength"," 详细描述长度最大为10000个字符"));
		}
		if (buyLead.getKeywords().length() > 60) {
			this.addFieldError("buyLead.keywords.maxlength", getText("buyLead.keywords.maxlength", "关键词最大长度为60个字符"));
		}
		// 未登录
		if (!buyLead.isLoginUser()) {
			// 新用户
			if (buyLead.isNewUser()) {
				if (StringUtil.isBlank(buyLead.getNewUserEmail()) || !Validator.isEmail(buyLead.getNewUserEmail())) {
					this.addFieldError("email.required", "请输入有效的电子邮箱，便于找回密码");
				}
				if (!Validator.isPassword(buyLead.getNewUserPasswd())) {
					this.addFieldError("buyLead.passwd.required", "请输入密码，由6-20个字符组成，不能有空格");
				} else if (!StringUtil.equals(buyLead.getNewUserPasswd(), buyLead.getConfirmPasswd())) {
					this.addFieldError("buyLead.confirmPasswd.error", getText("buyLead.confirmPasswd.error","两次密码不匹配"));
				}

				if (StringUtil.isBlank(buyLead.getComName()) || buyLead.getComName().length() > 120) {
					this.addFieldError("buyLead.comName.required", "请输入公司名称，长度120个字符内");
				} else {
					buyLead.setComName(buyLead.getComName().replace(" ", ""));
				}

				if (StringUtil.isBlank(buyLead.getContact()) || buyLead.getContact().length() > 20) {
					this.addFieldError("buyLead.contact.required","请输入联系人姓名，长度20个字符内");
				}
				if (buyLead.getSex() != 1 && buyLead.getSex() != 2 && buyLead.getSex() != 3) {
					this.addFieldError("buyLead.sex.required", getText("buyLead.sex.required","请选择性别"));
				}
				if (StringUtil.isBlank(buyLead.getProvince())) {
					this.addFieldError("buyLead.province.required", getText("buyLead.province.required","请选择公司所在地区"));
				}
				if (StringUtil.isBlank(buyLead.getCity())) {
					this.addFieldError("buyLead.province.required", getText("buyLead.province.required","请选择公司所在地区"));
				}

				if (Validator.isTel(buyLead.getTel1(), buyLead.getTel2())) {
					buyLead.setTel(buyLead.getTel() + "-" + buyLead.getTel2());
				} else {
					this.addFieldError("buyLead.tel.required","请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用\",\"或\"/\"分隔，分机号码请用\"-\"分隔");
				}

				buyLead.setEmail(buyLead.getNewUserEmail());
			} else {
				if (StringUtil.isBlank(buyLead.getEmail()) || (!"ok".equals(Validator.isMemberId(buyLead.getEmail())) && !Validator.isEmail(buyLead.getEmail()))) {
					this.addFieldError("emailOrMemberId.required","请输入会员帐号或邮箱");
				}
				if (!Validator.isPassword(buyLead.getPasswd())) {
					this.addFieldError("buyLead.passwd.required", "请输入密码，由6-20个字符组成，不能有空格");
				}
				buyLead.setNewUserEmail(buyLead.getEmail());
			}
		}

		if (!ValidateCode.isValid(request, buyLead.getValidateCodeKey(), buyLead.getValidateCode())) {
			this.addFieldError("validateCode.error", TextUtil.getText("validateCode.error","zh"));
		}
	}

	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return null;
	}

	public BuyLead getModel() {
		return buyLead;
	}

	public String getAutoLoginURL() {
		return autoLoginURL;
	}

	public void setB2BService(B2BService service) {
		b2BService = service;
	}
}
