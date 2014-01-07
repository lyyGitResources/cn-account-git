/* 
 * Created by baozhimin at Dec 8, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.cn.account.user.CategorySuggestAction;
import com.hisupplier.commons.entity.cn.CategorySuggest;

/**
 * @author baozhimin
 */
public class CategorySuggestActionTest extends ActionTestSupport {
	private String namespace = "/user";
	private CategorySuggestAction action;
	
	public void test_category_suggest() throws Exception{
		String method = "category_suggest";
		action = createAction(CategorySuggestAction.class, namespace, method);
		this.execute(method, "success");
	}
	
	public void test_category_suggest_submit() throws Exception{
		String method = "category_suggest_submit";
		action = createAction(CategorySuggestAction.class, namespace, method);
		CategorySuggest categorySuggest = action.getModel();
		categorySuggest.setContent1("111111111111111");
		categorySuggest.setContent2("222222222222");
		categorySuggest.setContent3("3333333333");
		
		this.execute(method, "success");
	}
}
