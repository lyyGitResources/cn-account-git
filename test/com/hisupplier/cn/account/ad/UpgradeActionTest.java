package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class UpgradeActionTest extends ActionTestSupport {

	public void test_upgrade_submit() throws Exception {
		String method = "upgrade_submit";

		UpgradeAction action = this.createAction(UpgradeAction.class, "/ad", method);

		this.setValidateToken();

		UpgradeMail upgradeMail = new UpgradeMail();
		upgradeMail.setComId(442);
		upgradeMail.setUpType(1);
		upgradeMail.setRemark("套餐A：黄金会员GoldSite+SEO行销风格网站");
		upgradeMail.setMemberId("测试赛的");
		upgradeMail.setComName("得分");
		upgradeMail.setContact("第三方");
		upgradeMail.setEmail("yaozhan189@163.com");
		upgradeMail.setTel("112312312");
		upgradeMail.setMobile("123123");
		action.setUpgradeMail(upgradeMail);

		this.execute(method, "success");
	}
}
