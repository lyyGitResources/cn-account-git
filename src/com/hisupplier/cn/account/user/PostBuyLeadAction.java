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
		result.put("headDescription", "����������Ϣ����������Ϣ�����̻���ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		result.put("navbar", "���׷�������Ϣ");
		return SUCCESS;
	}

	public String post_buy_lead_submit() throws Exception {
		tip = this.b2BService.addBuyLead(request, buyLead);
		if (tip.equals("isBlocked")) {
			this.addActionError("�����˺��ѱ����ᣬ��������ѯ�ͷ���Ա��Kitty  ����QQ��239183271   ���䣺service10@hi.cc");
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
		result.put("headDescription", "����������Ϣ�����ɹ�������Ϣ�����̻���ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		return super.execute();
	}

	public String post_buy_lead_success() throws Exception {
		result.put("headDescription", "����������Ϣ�����ɹ�������Ϣ�����̻���ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		return super.execute();
	}

	public void validatePost_buy_lead_submit() {
		result.put("headDescription", "����������Ϣ����������Ϣ�����̻���ó�׻��ᣬ��ҵ��Ϣ��������Ϣ");
		result.put("navbar", "���׷�������Ϣ");
		/*
		if (buyLead.getKeywordArray() == null) {
			buyLead.setKeywords("");
		} else {
			String keywords = "";
			for (String keyword : buyLead.getKeywordArray()) {
				if (keyword.trim().length() > 60) {
					this.addFieldError("buyLead.keywords.maxlength", getText("buyLead.keywords.maxlength","ÿ���ؼ��ʳ������Ϊ60���ַ�"));
				} else {
					keywords += "," + keyword.trim();
				}
			}
			buyLead.setKeywords(StringUtil.trimComma(keywords));
		}
		*/

		StringUtil.trimToEmpty(buyLead, "proName,email,comName,contact,tel1,tel2,brief,description,keywords");

		if (buyLead.getProName().length() > 120 || buyLead.getProName().length() < 1) {
			this.addFieldError("buyLead.proName.required", getText("buyLead.proName.required","�����������⣬����120���ַ���"));
		}
		if (buyLead.getCatId() <= 0) {
			this.addFieldError("buyLead.catId.required", getText("buyLead.catId.required","��ѡ����ҵĿ¼"));
		}
		if (buyLead.getBrief().length() > 150) {
			this.addFieldError("buyLead.brief.maxlength", getText("buyLead.brief.maxlength","����д����ժҪ������150���ַ���"));
		}
		if (buyLead.getDescription().length() > 20000) {
			this.addFieldError("buyLead.description.maxlength", getText("buyLead.description.maxlength"," ��ϸ�����������Ϊ10000���ַ�"));
		}
		if (buyLead.getKeywords().length() > 60) {
			this.addFieldError("buyLead.keywords.maxlength", getText("buyLead.keywords.maxlength", "�ؼ�����󳤶�Ϊ60���ַ�"));
		}
		// δ��¼
		if (!buyLead.isLoginUser()) {
			// ���û�
			if (buyLead.isNewUser()) {
				if (StringUtil.isBlank(buyLead.getNewUserEmail()) || !Validator.isEmail(buyLead.getNewUserEmail())) {
					this.addFieldError("email.required", "��������Ч�ĵ������䣬�����һ�����");
				}
				if (!Validator.isPassword(buyLead.getNewUserPasswd())) {
					this.addFieldError("buyLead.passwd.required", "���������룬��6-20���ַ���ɣ������пո�");
				} else if (!StringUtil.equals(buyLead.getNewUserPasswd(), buyLead.getConfirmPasswd())) {
					this.addFieldError("buyLead.confirmPasswd.error", getText("buyLead.confirmPasswd.error","�������벻ƥ��"));
				}

				if (StringUtil.isBlank(buyLead.getComName()) || buyLead.getComName().length() > 120) {
					this.addFieldError("buyLead.comName.required", "�����빫˾���ƣ�����120���ַ���");
				} else {
					buyLead.setComName(buyLead.getComName().replace(" ", ""));
				}

				if (StringUtil.isBlank(buyLead.getContact()) || buyLead.getContact().length() > 20) {
					this.addFieldError("buyLead.contact.required","��������ϵ������������20���ַ���");
				}
				if (buyLead.getSex() != 1 && buyLead.getSex() != 2 && buyLead.getSex() != 3) {
					this.addFieldError("buyLead.sex.required", getText("buyLead.sex.required","��ѡ���Ա�"));
				}
				if (StringUtil.isBlank(buyLead.getProvince())) {
					this.addFieldError("buyLead.province.required", getText("buyLead.province.required","��ѡ��˾���ڵ���"));
				}
				if (StringUtil.isBlank(buyLead.getCity())) {
					this.addFieldError("buyLead.province.required", getText("buyLead.province.required","��ѡ��˾���ڵ���"));
				}

				if (Validator.isTel(buyLead.getTel1(), buyLead.getTel2())) {
					buyLead.setTel(buyLead.getTel() + "-" + buyLead.getTel2());
				} else {
					this.addFieldError("buyLead.tel.required","���������ź͵绰���룬���ų���3-5λ���֣��绰���볤����7-26���ַ��ڣ�����绰��\",\"��\"/\"�ָ����ֻ���������\"-\"�ָ�");
				}

				buyLead.setEmail(buyLead.getNewUserEmail());
			} else {
				if (StringUtil.isBlank(buyLead.getEmail()) || (!"ok".equals(Validator.isMemberId(buyLead.getEmail())) && !Validator.isEmail(buyLead.getEmail()))) {
					this.addFieldError("emailOrMemberId.required","�������Ա�ʺŻ�����");
				}
				if (!Validator.isPassword(buyLead.getPasswd())) {
					this.addFieldError("buyLead.passwd.required", "���������룬��6-20���ַ���ɣ������пո�");
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
