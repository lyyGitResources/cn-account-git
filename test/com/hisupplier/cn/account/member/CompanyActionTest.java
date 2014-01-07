package com.hisupplier.cn.account.member;

import com.hisupplier.cn.account.member.CompanyAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class CompanyActionTest extends ActionTestSupport {
	private String namespace = "/member";

	public void test_company_edit() throws Exception {
		String method = "company_edit";
		CompanyAction action = this.createAction(CompanyAction.class, namespace, method);
		action.getModel();
		this.execute(method, "success");
	}
}
