package com.hisupplier.cn.account.member;

import org.apache.commons.beanutils.PropertyUtils;

import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.member.CompanyFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class CompanyFormActionTest extends ActionTestSupport {
	private String namespace = "/member";
	private CompanyFormAction action = null;

	public void test_company_edit_submit() throws Exception {
		String method = "company_edit_submit";
		Company company1 = this.getCompanyEdit();

		//���Թ�˾�ʺź͹�˾���ɱ༭�����
		action = this.createAction(CompanyFormAction.class, namespace, method);
		this.setValidateToken();

		Company company = action.getModel();
		PropertyUtils.copyProperties(company, company1);

		company.setEditMemberId(true);
		company.setEditComName(true);
		company.setMemberId("guiyou");
		company.setComName("��Ҧ��Ө�ѵ������޹�˾1");

		String[] businessType = new String[] { "1", "2" };
		//String[] mainExport = new String[] { "1", "2" };
		String[] qualityCert = new String[] { "1", "2" };
		String[] catId = new String[] { "1358", "1359" };
		String[] website = new String[] { "www.aa.bb", "www.bb.cc" };
		String[] keyword = new String[] { "����", "����" };
		String[] face = new String[] { "aa.jpg", "bb.jpg" };

		company.setBusinessType(businessType);
		//company.setMainExport(mainExport);
		company.setQualityCert(qualityCert);
		//company.setEconomyArea(1);
		company.setDomId(1);
		company.setWebsite(website);
		company.setKeyword(keyword);
		company.setCatId(catId);
		company.setOldCatIds(company1.getCatIds());
		company.setDescription("���طǻ�����");
		company.setFace(face);
		company.setLogoImgPath("aa.jpg");
		company.setLogoCertImg("aa.jpg");
		company.setVideoId(1);
		this.execute(method, "success");
	}
}
