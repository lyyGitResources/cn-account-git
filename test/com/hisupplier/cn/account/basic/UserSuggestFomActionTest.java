package com.hisupplier.cn.account.basic;

import com.hisupplier.cn.account.basic.UserSuggestFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.entity.cn.UserSuggest;

public class UserSuggestFomActionTest extends ActionTestSupport{
	private UserSuggest userSuggest = null;
	
	public void test_user_suggest_add() throws Exception {
		String method = "user_suggest_add";
		UserSuggestFormAction action = this.createAction( UserSuggestFormAction.class, "/basic", method);
		userSuggest = action.getModel();
		userSuggest.setTitle("这是测试标题");
		userSuggest.setContent("这是测试内容");
		userSuggest.setComId(442);
		this.execute(method, "success");
	}	
}