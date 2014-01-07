/* 
 * Created by baozhimin at Dec 9, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import com.hisupplier.cn.account.entity.BuyLead;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.cn.account.user.PostBuyLeadAction;
import com.hisupplier.commons.util.ValidateCode;

/**
 * @author baozhimin
 */
public class PostBuyLeadActionTest extends ActionTestSupport{
	private String namespace = "/user";
	private PostBuyLeadAction action;
	
	public void test_post_buy_lead() throws Exception{
		String method = "post_buy_lead";
		action = createAction(PostBuyLeadAction.class, namespace, method);
		this.execute(method, "success");
	}
	
	public void test_post_buy_lead_success() throws Exception{
		String method = "post_buy_lead_success";
		action = createAction(PostBuyLeadAction.class, namespace, method);
		this.execute(method, "success");
	}
	
	public void test_post_buy_lead_submit() throws Exception{
		String method = "post_buy_lead_submit";
		
		// ÒÑµÇÂ¼
		action = createAction(PostBuyLeadAction.class, "/user", method);
		BuyLead buyLead = action.getModel();
		request.setAttribute("com.hisupplier.cas.login", true);
		buyLead.setLoginUser(true);
		buyLead.setProName("bao1");
		buyLead.setKeywordArray(new String[]{"bao1", "bao2"});
		buyLead.setCatId(103);
		buyLead.setBrief("bao1bao1bao1");
		buyLead.setDescription("bao1bao1bao1bao1");
		buyLead.setValidDay(1);

		ValidateCode.setCode(request, "123", "abc");
		buyLead.setValidateCodeKey("123");
		buyLead.setValidateCode("abc");
		this.execute(method, "success");
	}
}
