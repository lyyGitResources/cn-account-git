/* 
 * Created by sunhailin at Nov 25, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.CN;

/**
 * @author sunhailin
 *
 */
public class TradeFormActionTest extends ActionTestSupport {
	private String namespace = "/trade";
	private Product product = null;
	private TradeFormAction action = null;

	public void test_trade_add_submit() throws Exception {
		String method = "trade_add_submit";
		action = this.createAction(TradeFormAction.class, namespace, method);
		this.setValidateToken();
		product = action.getModel();
		product.setProId(0);
		product.setComId(442);
		product.setUserId(758);
		product.setCatId(61);
		product.setGroupId(0);
		product.setGroupIdBak(0);
		product.setProName("test005");
		product.setProType(1);
		product.setCopyProId(0);
		product.setBrief("这只是个测试");
		product.setImgId(0);
		product.setImgPath("");
		product.setKeywords("测试001,测试002,测试003");
		product.setValidDay(0);
		product.setDescription("这只是个测试");
		this.execute(method, "success");
	}

	public void test_trade_edit_submit() throws Exception {
		String method = "trade_edit_submit";
		Product p = this.getTrade();
		if (p != null) {
			action = createAction(TradeFormAction.class, namespace, method);
			this.setValidateToken();
			Product product = action.getModel();
			product.setProId(p.getProId());
			product.setComId(p.getComId());
			product.setUserId(p.getUserId());
			product.setCatId(41);
			product.setGroupId(p.getGroupId());
			product.setGroupIdBak(p.getGroupIdBak());
			product.setProName(p.getProName());
			product.setProType(CN.TRADE_SELL);
			product.setCopyProId(p.getCopyProId());
			product.setBrief("这只是个测试");
			product.setImgId(p.getImgId());
			product.setImgPath(p.getImgPath());
			product.setKeywords(p.getKeywords());
			product.setValidDay(p.getValidDay());
			product.setViewCount(p.getViewCount());
			product.setDescription("这只是个测试");
			this.execute(method, "success");
		}

	}

}
