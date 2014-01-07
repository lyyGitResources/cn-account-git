package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;

public class ForgetPasswdAction extends BasicAction {
	private static final long serialVersionUID = -7536541457724363003L;

	private String email;
	private String encode;
	private Boolean limit = true;
	private String validateCode;
	private String validateCodeKey;
	private CompanyService companyService;
	private Map<String, Object> result;

	public String forget_passwd() throws Exception {
		result = new HashMap<String,Object>();
		result.put("headDescription", "������ȡ���������룬����ȡ�أ�ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ɹ��̣���Ӧ��");
		result.put("navbar", "��������");
		return SUCCESS;
	}

	public String forget_passwd_send() throws Exception {
		if (StringUtil.isNotEmpty(encode)) {
			email = new String(Base64.decodeBase64(encode.getBytes()));
			limit = false;
		}
		this.email = StringUtil.trimToEmpty(this.email);
		if (StringUtil.isEmpty(this.email)) {
			this.addActionError("����д��Ա�ʺŻ��ߵ�������");
		}
		if (!ValidateCode.isValid(request, validateCodeKey, validateCode)) {
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}
		if (this.hasActionErrors()) {
			return INPUT;
		}

		result = companyService.getForgetPasswd(email, limit);
		result.put("headDescription", "������ȡ������ɹ�������ȡ�أ�ó�׻��ᣬ��ҵ��Ϣ��������Ϣ���ɹ��̣���Ӧ��");
		tip = (String) result.get("tip");
		if (tip != "success") {
			this.addActionError(tip);
			return "error";
		}
		return SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public Boolean isLimit() {
		return limit;
	}

	public void setLimit(Boolean limit) {
		this.limit = limit;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getValidateCodeKey() {
		return validateCodeKey;
	}

	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}
}
