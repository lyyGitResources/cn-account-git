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
			addMessage("更新成功。");
		}
		return SUCCESS;
	}
	
	public void validateEdit() {
		if (StringUtil.isEmpty(companyProfile.getCurrency())) {
			addFieldError("currency", "币种不能为空");
		}
		if (StringUtil.isEmpty(companyProfile.getRegAuthority())) {
			addFieldError("regAuthority", "登记机关不能为空。");
		}
		if (StringUtil.isEmpty(companyProfile.getRegAddress())) {
			addFieldError("regAddress", "住所不能为空。");
		}
		if (StringUtil.isEmpty(companyProfile.getRegCapital())) {
			addFieldError("regCapital", "注册资金不能为空。");
		}
		if (StringUtil.isEmpty(companyProfile.getBusinessScope())) {
			addFieldError("businessScope", "经营范围不能为空。");
		} else if (companyProfile.getBusinessScope().length() > 400) {
			addFieldError("businessScope", "经营范围字数不能大于200");
		}
		if (StringUtil.isEmpty(companyProfile.getEstablishDate())) {
			addFieldError("establishDate", "成立日期不能为空。");
		}
		if (StringUtil.isEmpty(companyProfile.getRegDate()) 
				|| StringUtil.isEmpty(companyProfile.getRegExpiry())) {
			addFieldError("", "营业期限不能为空。");
		}
		if (StringUtil.isEmpty(companyProfile.getReviewTime())) {
			addFieldError("reviewTime", "年审时间不能为空。");
		}
		if (companyProfile.isEditCeo()) {
			if (StringUtil.isEmpty(companyProfile.getCeo())) {
				addFieldError("ceo", "法定代表人不能为空。");
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
