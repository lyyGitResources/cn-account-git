package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class CompanyAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;

	private CompanyService companyService;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private boolean reg; 	// 是否注册成功
 
	public String company_edit() throws Exception {
		result = this.companyService.getCompanyEdit(params);
		return SUCCESS;
	}

	public String check_memberId() throws Exception {
		tip = "false";
		String result = Validator.isMemberId(params.getMemberId());
		if (result.equals("invalid")) {//检测是否无效
			msg = getText("memberId.required");

		} else if (result.equals("used")) {//检测是否使用
			msg = getText("memberId.used");

		} else if (companyService.checkMemberId(params).equals("used")) {
			msg = getText("memberId.used");

		} else {
			tip = "true";
		}
		return SUCCESS;
	}

	public String check_comName() throws Exception {
		if(StringUtil.isNotEmpty(params.getFromCompany())){
			params.setComName(params.getFromCompany());
		}
		if (StringUtil.isNotBlank(params.getComName())) {
			Company company = new Company();
			company.setComName(params.getComName());
			
			Map<String, String> map = new HashMap<String, String>(); 
			map.put("comName", "公司名,");
			map.put("keyword", "行业关键词,");
			int comId = params.getLoginUser() == null ? 0 : params.getLoginUser().getComId();
			String patentKeyword = PatentUtil.checkKeyword(company, map, comId);
			
			if (StringUtil.isNotEmpty(patentKeyword)) {
				tip = "false";
				msg = getText("company.comName.error") + ":" + patentKeyword.split(",")[1];
			} else if (companyService.checkComName(params).equals("used")) {
				tip = "false";
				msg = getText("comName.used");
			} else {
				tip = "true";
			}
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

	public QueryParams getModel() {
		return params;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public boolean isReg() {
		return reg;
	}

	public void setReg(boolean reg) {
		this.reg = reg;
	}
}
