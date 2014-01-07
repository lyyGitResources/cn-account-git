package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.ad.AdOrderAction;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class AdOrderActionTest extends ActionTestSupport {

	//添加目录广告
	public void test_ad_order_submit_catId() throws Exception {
		String method = "ad_order_submit";

		AdOrderAction action = this.createAction(AdOrderAction.class, "/ad", method);

		this.setValidateToken();

		AdOrder adOrder = action.getModel();
		adOrder.setMemberId("测试测试");
		adOrder.setAdType(1);
		adOrder.setCatId(42);
		adOrder.setCatName("测试测试");
		adOrder.setComId(442);
		adOrder.setComName("测试测试");
		adOrder.setKeyword("");
		adOrder.setRemark("目录结果页");
		adOrder.setContact("目录结果页");
		adOrder.setEmail("目录结果页");
		adOrder.setTel("2323243");
		adOrder.setFax("146568565");
		adOrder.setMobile("1345698");
		this.execute(method, "success");
	}

	//添加关键词广告
	public void test_ad_order_submit_keyword() throws Exception {
		String method = "ad_order_submit";

		AdOrderAction action = this.createAction(AdOrderAction.class, "/ad", method);

		this.setValidateToken();

		AdOrder adOrder = action.getModel();
		adOrder.setMemberId("测试测试");
		adOrder.setAdType(2);
		adOrder.setCatId(0);
		adOrder.setComId(442);
		adOrder.setComName("测试测试第三方");
		adOrder.setCatName("第三方");
		adOrder.setKeyword("测试测试");
		adOrder.setRemark("测试测试");
		adOrder.setContact("目录结果页");
		adOrder.setEmail("目录结果页");
		adOrder.setTel("2323243");
		adOrder.setFax("146568565");
		adOrder.setMobile("1345698");
		
		this.execute(method, "success");
	}
}
