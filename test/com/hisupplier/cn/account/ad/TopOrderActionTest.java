package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class TopOrderActionTest extends ActionTestSupport {

	public void test_top_order_submit() throws Exception {
		String method = "top_order_submit";

		TopOrderAction action = this.createAction(TopOrderAction.class, "/ad", method);

		this.setValidateToken();
		TopOrder topOrder = new TopOrder();
		topOrder.setComId(5170);
		topOrder.setTopType(1);
		topOrder.setRemark("��һλŶ");
		topOrder.setKeyword("�˴Ź���");
		topOrder.setMemberId("��������");
		topOrder.setComName("�÷�");
		topOrder.setContact("������");
		topOrder.setEmail("yaozhan189@163.com");
		topOrder.setTel("112312312");
		topOrder.setMobile("123123");
		action.setTopOrder(topOrder);

		this.execute(method, "success");
	}
}
