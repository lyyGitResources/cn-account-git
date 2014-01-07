package com.hisupplier.cn.account.alert;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.basic.CASClient;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class TradeAlertStepAction extends BasicAction implements ModelDriven<TradeAlert> {

	private static final long serialVersionUID = 3645757574547975608L;
	private TradeAlertService tradeAlertService;
	private TradeAlert tradeAlert = new TradeAlert();
	private Map<String, Object> result = new HashMap<String, Object>();
	private String autoLoginURL;

	public String trade_alert_step1() throws Exception {
		result.put("headDescription", "海商网订阅信息,海商网快速订阅，买卖信息，贸易机会，商业信息，供求信息，加工合作");
		if (StringUtil.isNotEmpty(tradeAlert.getCatIds()) && !tradeAlert.getCatIds().equals("-1")) {
			tradeAlert.setMode("category");
			tradeAlert.setKeyword("");
			result.put("title", "简易快速订阅" + tradeAlert.getCatName() + "商情-海商网-全国领先的B2B电子商务交易平台");
			result.put("pageDescription", "快速订阅" + tradeAlert.getCatName() + "商情，您即享受专属的" + tradeAlert.getCatName() + "订阅服务。包括：提供" + tradeAlert.getCatName() + "产品的商情信息发送到您指定的邮箱内。");
			result.put("headDescription", "海商网商情订阅，网上贸易，网上交易，交易市场，在线交易，买卖信息，贸易机会");
			result.put("navbar", "简易商情订阅");
		} else {
			tradeAlert.setMode("keyword");
			tradeAlert.setCatIds("");
			result.put("title", "简易快速订阅" + tradeAlert.getKeyword() + "商情-海商网-全国领先的B2B电子商务交易平台");
			result.put("pageDescription", "快速订阅" + tradeAlert.getKeyword() + "商情，您即享受专属的" + tradeAlert.getKeyword() + "订阅服务。包括：提供" + tradeAlert.getKeyword() + "产品的商情信息发送到您指定的邮箱内。");
			result.put("headDescription", "海商网快速订阅，买卖信息，贸易机会，商业信息，供求信息，加工合作");
			result.put("navbar", "简易商情订阅");
		}

		//已登录，转到用户后台订阅
		if (CASClient.getInstance().isLogin(request)) {
			if(StringUtil.isNotEmpty(tradeAlert.getKeyword())){
				tradeAlert.setKeyword(Coder.encodeURL(tradeAlert.getKeyword()));
			}
			result.put("tradeAlert", tradeAlert);
			return "tradeAlertAdd";
		} else {
			result.put("tradeAlert", tradeAlert);
			return SUCCESS;
		}
	}

	public String trade_alert_step2() throws Exception {
		result.put("headDescription", "海商网订阅信息,海商网快速订阅，买卖信息，贸易机会，商业信息，供求信息，加工合作");
		validateStep1Form();
		if (this.hasActionErrors()) {
			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}

		tip = tradeAlertService.addTradeAlertStep1(tradeAlert);
		if (!tip.equals("addSuccess")) {
			if (tip.equals("email.used")) {
				tradeAlert.setMember(true);//邮箱已存在，在页面上显示密码输入框
				this.addActionError("邮箱已存在，请输入密码");

			} else if (tip.equals("passwd.error")) {
				tradeAlert.setMember(true);
				this.addActionError("密码错误");

			} else if (tip.equals("email.error")) {
				tradeAlert.setMember(true);
				this.addActionError("邮箱不存在");

			} else if (tip.equals("alert.limit")) {
				this.addActionError("您最多只能同时订阅10次");

			} else if (tip.equals("alert.keyword.used")) {
				this.addActionError("关键词已存在，请重新输入");

			} else if (tip.equals("operateFail")) {
				this.addActionError("订阅失败，请返回重试");

			} else if (tip.equals("toStep2")) {
				//转到第二步表单
				result.put("tradeAlert", tradeAlert);
				return "step2";
			} else if (tip.equals("isBlocked")) {
				this.addActionError("您的账号已被冻结，详情请咨询客服人员：Kitty  工作QQ：239183271   邮箱：service10@hi.cc");
			}

			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}
		String returnUrl = Config.getString("account.base") + "/alert/trade_alert_list.htm";
		autoLoginURL = CASClient.getInstance().getAutoLoginURL(returnUrl, tradeAlert.getEmail(), tradeAlert.getPasswd());
		return SUCCESS;
	}

	public String trade_alert_step3() throws Exception {
		result.put("headDescription", "海商网订阅信息,海商网快速订阅，买卖信息，贸易机会，商业信息，供求信息，加工合作");
		validateStep1Form();
		validateStep2Form();
		if (this.hasActionErrors()) {
			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}

		tip = tradeAlertService.addTradeAlertStep2(tradeAlert);
		if (!tip.equals("addSuccess")) {
			if (tip.equals("email.used")) {
				this.addActionError("邮箱已存在，请重新输入");
			} else if (tip.equals("comName.used")) {
				this.addActionError("公司名已存在");
			} else {
				this.addActionError("简易订阅失败，请返回重试");
			}
			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}
		String returnUrl = Config.getString("account.base") + "/user/trade_alert_success.htm?email=" + tradeAlert.getEmail() + "&keyword=" + Coder.encodeURL(tradeAlert.getKeyword()) + "&catIds="
				+ tradeAlert.getCatIds();
		autoLoginURL = CASClient.getInstance().getAutoLoginURL(returnUrl, tradeAlert.getEmail(), tradeAlert.getPasswd());
		return SUCCESS;
	}

	public String trade_alert_success() throws Exception {
		result.put("headDescription", "海商网订阅信息,海商网快速订阅，买卖信息，贸易机会，商业信息，供求信息，加工合作");
		return SUCCESS;
	}

	public void validateStep1Form() {
		if (!StringUtil.containsValue(new String[] { "keyword", "category" }, tradeAlert.getMode())) {
			tradeAlert.setMode("keyword");
			tradeAlert.setCatIds("");
			tradeAlert.setKeyword("");
			this.addActionError("请输入关键词，长度60个字符内！");
		} else {
			if (StringUtil.equals(tradeAlert.getMode(), "keyword")) {
				if (StringUtil.isEmpty(tradeAlert.getKeyword()) || tradeAlert.getKeyword().length() > 60) {
					this.addActionError("请输入关键词，长度60个字符内！");
				} else {
					tradeAlert.setCatIds("");
				}
			} else if (StringUtil.equals(tradeAlert.getMode(), "category")) {
				if (StringUtil.isEmpty(tradeAlert.getCatIds())) {
					tradeAlert.setMode("keyword");
					tradeAlert.setCatIds("");
					tradeAlert.setKeyword("");
					this.addActionError("请输入关键词，长度60个字符内！");
				} else {
					tradeAlert.setKeyword("");
				}
			}
		}
		if (!Validator.isEmail(tradeAlert.getEmail())) {
			this.addActionError("请输入有效的电子邮箱，便于找回密码");
		}
		if (!tradeAlert.isCompany() && !tradeAlert.isProduct() && !tradeAlert.isBuy()) {
			this.addActionError("请选择订阅内容");
		}
	}

	public void validateStep2Form() {
		StringUtil.trimToEmpty(tradeAlert, "email,passwd,confirmPasswd,comName,contact,tel1,tel2,validateCode");

		if (!Validator.isPassword(tradeAlert.getPasswd())) {
			this.addActionError(getText("passwd.required"));
		} else if (!StringUtil.equals(tradeAlert.getPasswd(), tradeAlert.getConfirmPasswd())) {
			this.addActionError("两次输入密码不匹配");
		}

		if (StringUtil.isEmpty(tradeAlert.getComName()) || tradeAlert.getComName().length() > 120) {
			this.addActionError("请输入公司名称，长度120个字符内");
		} else {
			tradeAlert.setComName(tradeAlert.getComName().replace(" ", ""));
		}

		if (StringUtil.isEmpty(tradeAlert.getContact()) || tradeAlert.getContact().length() > 20) {
			this.addActionError("请输入联系人姓名，长度20个字符内");
		}
		if (StringUtil.isEmpty(tradeAlert.getProvince())) {
			this.addActionError("请选择公司所在地区");
		}
		if (StringUtil.isEmpty(tradeAlert.getCity())) {
			this.addActionError("请选择公司所在地区");
		}

		if (Validator.isTel(tradeAlert.getTel1(), tradeAlert.getTel2())) {
			tradeAlert.setTel(tradeAlert.getTel1() + "-" + tradeAlert.getTel2());
		} else {
			this.addActionError(getText("tel.required"));
		}

		if (!ValidateCode.isValid(request, tradeAlert.getValidateCodeKey(), tradeAlert.getValidateCode())) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public TradeAlert getModel() {
		return tradeAlert;
	}

	public String getAutoLoginURL() {
		return autoLoginURL;
	}

	public void setTradeAlertService(TradeAlertService tradeAlertService) {
		this.tradeAlertService = tradeAlertService;
	}

}
