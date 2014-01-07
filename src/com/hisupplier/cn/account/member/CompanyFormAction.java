package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class CompanyFormAction extends BasicAction implements ModelDriven<Company> {
	private static final long serialVersionUID = -7536541457724363003L;
	private CompanyService companyService;
	private Company company = new Company();
	private Map<String, Object> result;
	
	public CompanyFormAction() {
		super();
		this.currentMenu = "member";
	}

	public String company_edit_submit() throws Exception {
		company.setComId(this.getLoginUser().getComId());
		tip = this.companyService.updateCompany(company, this.getLoginUser());
		msg = getText(tip);
		if (tip.equals("editSuccess")) {
			if (company.getState() == CN.STATE_WAIT) {
				this.addMessage(msg + "  ������Ϣ�ѽ��빫˾��ˣ����ͨ���������ƽ̨��չʾ��");
			} else {
				this.addMessage(msg);
			}
		} else {
			this.addActionError(msg);
			result = this.companyService.getCompanyEdit(company.getComId());
			return "input";
		}
		return SUCCESS;
	}

	public void validateCompany_edit_submit() {
		super.validate();
		
		if (company.isEditMemberId()) {
			String tip = Validator.isMemberId(company.getMemberId());
			if (tip.equals("invalid")) {//����Ƿ���Ч
				this.addActionError(getText("memberId.required"));
			} else if (tip.equals("used")) {//����Ƿ�ʹ��
				this.addActionError(getText("memberId.used"));
			}
		}
		if (company.isEditComName()) {
			if (StringUtil.isEmpty(company.getComName()) || company.getComName().length() > 120) {
				this.addActionError(getText("comName.required"));
			}else{
				company.setComName(company.getComName().replace(" ", ""));
			}
		}

		Company companyInfo = companyService.getValidateCompany(this.getLoginUser().getComId());
		User userInfo = companyService.getValidateUser(this.getLoginUser().getMemberId());
		if (company.getRegImgType() == 1 && userInfo.getProvince().equals("103103")) {
			if (StringUtil.isEmpty(company.getRegNo()) && StringUtil.isBlank(companyInfo.getRegNo())) {
				this.addActionError("������Ӫҵִ��ע���");
			} else {
				if (StringUtil.isNotBlank(company.getRegNo()) || companyInfo.getState() == 10){
					company.setRegNo(company.getRegNo().replace(" ", ""));
					company.setEditRegNo(true);
				}
				if (StringUtil.isEmpty(companyInfo.getRegNo())) {
					company.setEditRegNo(true);
				}
			}
			if (StringUtil.isEmpty(company.getCeo()) && StringUtil.isBlank(companyInfo.getCeo())) {
				this.addActionError("�����뷨��");
			} else {
				if (StringUtil.isNotBlank(company.getCeo()) || companyInfo.getState() == 10){
					company.setCeo(company.getCeo().replace(" ", ""));
					company.setEditCeo(true);
				}
				if (StringUtil.isEmpty(companyInfo.getCeo())) {
					company.setEditCeo(true);
				}
			}
			company.setCheckRegNo(true);
		}
		
		if (StringUtil.isNotEmpty(company.getComNameEN()) && company.getComNameEN().length() > 120) {
			this.addActionError(getText("comName.required"));
		}
		if (company.getBusinessType() == null || company.getBusinessType().length <= 0) {
			this.addActionError(getText("company.businessTypes.required"));
		}

//		if (company.getEconomyArea() == 0) {
//			this.addActionError(getText("company.economyArea.required"));
//		}
//		if (company.getDomId() == 0) {
//			this.addActionError(getText("company.domId.required"));
//		}
		
		if(company.getWebsite() != null && company.getWebsite().length > 0){
			for(String site : company.getWebsite()){
				if(StringUtil.isNotBlank(site)){
					if(site.length() > 80){
						this.addActionError(getText("company.website.maxlength"));
					}else if(!Validator.isUrl(site)){
						this.addActionError(getText("url.required"));
					}
				}
			}
		}
		
		if (company.getKeyword() != null && company.getKeyword().length > 0) {
			int count = 0;
			Set<String> set = new HashSet<String>();
			
			Map<String, String> map = new HashMap<String, String>(); 
			map.put("comName", "��˾��,");
			map.put("keyword", "��ҵ�ؼ���,");
			String patentKeyword = PatentUtil.checkKeyword(company, map, this.getLoginUser().getComId());
			if (StringUtil.isNotEmpty(patentKeyword)) {
				this.addActionError("�ύʧ�ܣ����ύ��   '" + patentKeyword.split(",")[0] + "'  ����Υ���ʣ�" + patentKeyword.split(",")[1]);
			}
			for (String keyword : company.getKeyword()) {
				if (StringUtil.isEmpty(keyword)) {
					count++;
					continue;
				} else {//�ؼ��ʲ�Ϊ�ռ���ַ��������ظ�
					if (keyword.length() > 30) {
						this.addActionError(getText("company.keywords.required"));
					} else {
						set.add(keyword);//Ϊ�˼����Ƿ����ظ��Ĺؼ���
					}
				}
			}
			if (count == company.getKeyword().length) {//˵��ȫΪ���ַ���
				this.addActionError(getText("company.keywords.required"));
			} else if (count + set.size() < company.getKeyword().length) {//���ظ��ؼ���
				this.addActionError(getText("company.keywords.duplicate"));
			}

		} else {//�ؼ�������Ϊ��
			this.addActionError(getText("company.keywords.required"));
		}
		
		if (ArrayUtils.isEmpty(company.getCatId()) 
				|| ArrayUtils.indexOf(company.getCatId(), "") >= 0) {
			this.addActionError(getText("company.catIds.required"));
		}
		
		if(StringUtil.isEmpty(company.getRegImgPath()) && StringUtil.isEmpty(company.getRegImgPath2())){
			this.addActionError(getText("company.regImg.required"));
		}
		
		if (StringUtil.isEmpty(company.getDescription()) || company.getDescription().length() > 4000) {
			this.addActionError(getText("company.description.required"));
		}
		//	if (this.hasActionErrors() || this.hasFieldErrors()) {
		result = new HashMap<String, Object>();
		result.put("province", userInfo.getProvince());
		result.put("company", company);
		
		//	}
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

	public Company getModel() {
		return company;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
}
