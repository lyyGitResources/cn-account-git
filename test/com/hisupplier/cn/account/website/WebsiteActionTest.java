package com.hisupplier.cn.account.website;

import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.cn.account.website.WebsiteAction;

@SuppressWarnings("unused")
public class WebsiteActionTest extends ActionTestSupport {
	private String namespace = "/website";

	public void test_template_list() throws Exception {
		String method = "template_list";
		WebsiteAction action = this.createAction(WebsiteAction.class, namespace, method);
		this.execute(method, "success");
	}
}
