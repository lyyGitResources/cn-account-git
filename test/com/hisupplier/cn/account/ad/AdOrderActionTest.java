package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.ad.AdOrderAction;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class AdOrderActionTest extends ActionTestSupport {

	//���Ŀ¼���
	public void test_ad_order_submit_catId() throws Exception {
		String method = "ad_order_submit";

		AdOrderAction action = this.createAction(AdOrderAction.class, "/ad", method);

		this.setValidateToken();

		AdOrder adOrder = action.getModel();
		adOrder.setMemberId("���Բ���");
		adOrder.setAdType(1);
		adOrder.setCatId(42);
		adOrder.setCatName("���Բ���");
		adOrder.setComId(442);
		adOrder.setComName("���Բ���");
		adOrder.setKeyword("");
		adOrder.setRemark("Ŀ¼���ҳ");
		adOrder.setContact("Ŀ¼���ҳ");
		adOrder.setEmail("Ŀ¼���ҳ");
		adOrder.setTel("2323243");
		adOrder.setFax("146568565");
		adOrder.setMobile("1345698");
		this.execute(method, "success");
	}

	//��ӹؼ��ʹ��
	public void test_ad_order_submit_keyword() throws Exception {
		String method = "ad_order_submit";

		AdOrderAction action = this.createAction(AdOrderAction.class, "/ad", method);

		this.setValidateToken();

		AdOrder adOrder = action.getModel();
		adOrder.setMemberId("���Բ���");
		adOrder.setAdType(2);
		adOrder.setCatId(0);
		adOrder.setComId(442);
		adOrder.setComName("���Բ��Ե�����");
		adOrder.setCatName("������");
		adOrder.setKeyword("���Բ���");
		adOrder.setRemark("���Բ���");
		adOrder.setContact("Ŀ¼���ҳ");
		adOrder.setEmail("Ŀ¼���ҳ");
		adOrder.setTel("2323243");
		adOrder.setFax("146568565");
		adOrder.setMobile("1345698");
		
		this.execute(method, "success");
	}
}
