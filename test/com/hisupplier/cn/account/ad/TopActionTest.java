package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.ad.TopAction;
import com.hisupplier.cn.account.entity.Top;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class TopActionTest extends ActionTestSupport {

	public void test_top_edit_submit() throws Exception {
		String method = "top_edit_submit";

		TopAction action = this.createAction(TopAction.class, "/ad", method);

		this.setValidateToken();
		Top top = new Top();
		top.setImgPath("/20091117/123123");
		top.setProName("ºË´Å¹²Õñ");
		top.setBrief("ºË´Å¹²Õñ°¡");
		top.setLink("www.hisupplier.com");
		top.setComId(5170);
		top.setTopId(4);
		action.setTop(top);

		this.execute(method, "success");
	}
}
