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
		result.put("headDescription", "������������Ϣ,���������ٶ��ģ�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ӹ�����");
		if (StringUtil.isNotEmpty(tradeAlert.getCatIds()) && !tradeAlert.getCatIds().equals("-1")) {
			tradeAlert.setMode("category");
			tradeAlert.setKeyword("");
			result.put("title", "���׿��ٶ���" + tradeAlert.getCatName() + "����-������-ȫ�����ȵ�B2B����������ƽ̨");
			result.put("pageDescription", "���ٶ���" + tradeAlert.getCatName() + "���飬��������ר����" + tradeAlert.getCatName() + "���ķ��񡣰������ṩ" + tradeAlert.getCatName() + "��Ʒ��������Ϣ���͵���ָ���������ڡ�");
			result.put("headDescription", "���������鶩�ģ�����ó�ף����Ͻ��ף������г������߽��ף�������Ϣ��ó�׻���");
			result.put("navbar", "�������鶩��");
		} else {
			tradeAlert.setMode("keyword");
			tradeAlert.setCatIds("");
			result.put("title", "���׿��ٶ���" + tradeAlert.getKeyword() + "����-������-ȫ�����ȵ�B2B����������ƽ̨");
			result.put("pageDescription", "���ٶ���" + tradeAlert.getKeyword() + "���飬��������ר����" + tradeAlert.getKeyword() + "���ķ��񡣰������ṩ" + tradeAlert.getKeyword() + "��Ʒ��������Ϣ���͵���ָ���������ڡ�");
			result.put("headDescription", "���������ٶ��ģ�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ӹ�����");
			result.put("navbar", "�������鶩��");
		}

		//�ѵ�¼��ת���û���̨����
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
		result.put("headDescription", "������������Ϣ,���������ٶ��ģ�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ӹ�����");
		validateStep1Form();
		if (this.hasActionErrors()) {
			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}

		tip = tradeAlertService.addTradeAlertStep1(tradeAlert);
		if (!tip.equals("addSuccess")) {
			if (tip.equals("email.used")) {
				tradeAlert.setMember(true);//�����Ѵ��ڣ���ҳ������ʾ���������
				this.addActionError("�����Ѵ��ڣ�����������");

			} else if (tip.equals("passwd.error")) {
				tradeAlert.setMember(true);
				this.addActionError("�������");

			} else if (tip.equals("email.error")) {
				tradeAlert.setMember(true);
				this.addActionError("���䲻����");

			} else if (tip.equals("alert.limit")) {
				this.addActionError("�����ֻ��ͬʱ����10��");

			} else if (tip.equals("alert.keyword.used")) {
				this.addActionError("�ؼ����Ѵ��ڣ�����������");

			} else if (tip.equals("operateFail")) {
				this.addActionError("����ʧ�ܣ��뷵������");

			} else if (tip.equals("toStep2")) {
				//ת���ڶ�����
				result.put("tradeAlert", tradeAlert);
				return "step2";
			} else if (tip.equals("isBlocked")) {
				this.addActionError("�����˺��ѱ����ᣬ��������ѯ�ͷ���Ա��Kitty  ����QQ��239183271   ���䣺service10@hi.cc");
			}

			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}
		String returnUrl = Config.getString("account.base") + "/alert/trade_alert_list.htm";
		autoLoginURL = CASClient.getInstance().getAutoLoginURL(returnUrl, tradeAlert.getEmail(), tradeAlert.getPasswd());
		return SUCCESS;
	}

	public String trade_alert_step3() throws Exception {
		result.put("headDescription", "������������Ϣ,���������ٶ��ģ�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ӹ�����");
		validateStep1Form();
		validateStep2Form();
		if (this.hasActionErrors()) {
			result.put("tradeAlert", tradeAlert);
			return INPUT;
		}

		tip = tradeAlertService.addTradeAlertStep2(tradeAlert);
		if (!tip.equals("addSuccess")) {
			if (tip.equals("email.used")) {
				this.addActionError("�����Ѵ��ڣ�����������");
			} else if (tip.equals("comName.used")) {
				this.addActionError("��˾���Ѵ���");
			} else {
				this.addActionError("���׶���ʧ�ܣ��뷵������");
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
		result.put("headDescription", "������������Ϣ,���������ٶ��ģ�������Ϣ��ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ӹ�����");
		return SUCCESS;
	}

	public void validateStep1Form() {
		if (!StringUtil.containsValue(new String[] { "keyword", "category" }, tradeAlert.getMode())) {
			tradeAlert.setMode("keyword");
			tradeAlert.setCatIds("");
			tradeAlert.setKeyword("");
			this.addActionError("������ؼ��ʣ�����60���ַ��ڣ�");
		} else {
			if (StringUtil.equals(tradeAlert.getMode(), "keyword")) {
				if (StringUtil.isEmpty(tradeAlert.getKeyword()) || tradeAlert.getKeyword().length() > 60) {
					this.addActionError("������ؼ��ʣ�����60���ַ��ڣ�");
				} else {
					tradeAlert.setCatIds("");
				}
			} else if (StringUtil.equals(tradeAlert.getMode(), "category")) {
				if (StringUtil.isEmpty(tradeAlert.getCatIds())) {
					tradeAlert.setMode("keyword");
					tradeAlert.setCatIds("");
					tradeAlert.setKeyword("");
					this.addActionError("������ؼ��ʣ�����60���ַ��ڣ�");
				} else {
					tradeAlert.setKeyword("");
				}
			}
		}
		if (!Validator.isEmail(tradeAlert.getEmail())) {
			this.addActionError("��������Ч�ĵ������䣬�����һ�����");
		}
		if (!tradeAlert.isCompany() && !tradeAlert.isProduct() && !tradeAlert.isBuy()) {
			this.addActionError("��ѡ��������");
		}
	}

	public void validateStep2Form() {
		StringUtil.trimToEmpty(tradeAlert, "email,passwd,confirmPasswd,comName,contact,tel1,tel2,validateCode");

		if (!Validator.isPassword(tradeAlert.getPasswd())) {
			this.addActionError(getText("passwd.required"));
		} else if (!StringUtil.equals(tradeAlert.getPasswd(), tradeAlert.getConfirmPasswd())) {
			this.addActionError("�����������벻ƥ��");
		}

		if (StringUtil.isEmpty(tradeAlert.getComName()) || tradeAlert.getComName().length() > 120) {
			this.addActionError("�����빫˾���ƣ�����120���ַ���");
		} else {
			tradeAlert.setComName(tradeAlert.getComName().replace(" ", ""));
		}

		if (StringUtil.isEmpty(tradeAlert.getContact()) || tradeAlert.getContact().length() > 20) {
			this.addActionError("��������ϵ������������20���ַ���");
		}
		if (StringUtil.isEmpty(tradeAlert.getProvince())) {
			this.addActionError("��ѡ��˾���ڵ���");
		}
		if (StringUtil.isEmpty(tradeAlert.getCity())) {
			this.addActionError("��ѡ��˾���ڵ���");
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
