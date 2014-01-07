package com.hisupplier.cn.account.member;

import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.CompanyProfile;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class CompanyProfileAction extends BasicAction
		implements ModelDriven<CompanyProfile> {
	private static final long serialVersionUID = 1489543241319813096L;
	private CompanyProfile companyProfile = new CompanyProfile();
	
	private CompanyService companyService;
	
	public String view() throws Exception {
		companyProfile = companyService.getProfileByLoginUser(getLoginUser());
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		if (companyService.updateProfile(companyProfile, getLoginUser())) {
			addMessage("���³ɹ���");
		}
		return SUCCESS;
	}
	
	public void validateEdit() {
		if (StringUtil.isEmpty(companyProfile.getCurrency())) {
			addFieldError("currency", "���ֲ���Ϊ��");
		}
		if (StringUtil.isEmpty(companyProfile.getRegAuthority())) {
			addFieldError("regAuthority", "�Ǽǻ��ز���Ϊ�ա�");
		}
		if (StringUtil.isEmpty(companyProfile.getRegAddress())) {
			addFieldError("regAddress", "ס������Ϊ�ա�");
		}
		if (StringUtil.isEmpty(companyProfile.getRegCapital())) {
			addFieldError("regCapital", "ע���ʽ���Ϊ�ա�");
		}
		if (StringUtil.isEmpty(companyProfile.getBusinessScope())) {
			addFieldError("businessScope", "��Ӫ��Χ����Ϊ�ա�");
		} else if (companyProfile.getBusinessScope().length() > 400) {
			addFieldError("businessScope", "��Ӫ��Χ�������ܴ���200");
		}
		if (StringUtil.isEmpty(companyProfile.getEstablishDate())) {
			addFieldError("establishDate", "�������ڲ���Ϊ�ա�");
		}
		if (StringUtil.isEmpty(companyProfile.getRegDate()) 
				|| StringUtil.isEmpty(companyProfile.getRegExpiry())) {
			addFieldError("", "Ӫҵ���޲���Ϊ�ա�");
		}
		if (StringUtil.isEmpty(companyProfile.getReviewTime())) {
			addFieldError("reviewTime", "����ʱ�䲻��Ϊ�ա�");
		}
		if (companyProfile.isEditCeo()) {
			if (StringUtil.isEmpty(companyProfile.getCeo())) {
				addFieldError("ceo", "���������˲���Ϊ�ա�");
			}
		}
	}
	
	@Override
	public CompanyProfile getModel() {
		return companyProfile;
	}

	@Override
	@JSON(serialize = false)
	public Map<String, Object> getResult() {
		return null;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

}
