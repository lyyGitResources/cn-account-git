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
		upgradeMail.setRemark("�ײ�A���ƽ��ԱGoldSite+SEO���������վ");
		upgradeMail.setMemberId("��������");
		upgradeMail.setComName("�÷�");
		upgradeMail.setContact("������");
		upgradeMail.setEmail("yaozhan189@163.com");
		upgradeMail.setTel("112312312");
		upgradeMail.setMobile("123123");
		action.setUpgradeMail(upgradeMail);

		this.execute(method, "success");
	}
}
