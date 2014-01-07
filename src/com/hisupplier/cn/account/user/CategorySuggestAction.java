/* 
 * Created by baozhimin at Dec 8, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.entity.cn.CategorySuggest;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class CategorySuggestAction extends BasicAction implements ModelDriven<CategorySuggest>{

	private static final long serialVersionUID = -6074352664245715821L;
	private CategorySuggest categorySuggest = new CategorySuggest();
	private B2BService b2BService;
	
	
	public String category_suggest() throws Exception {
		currentMenu = "categorySuggest";
		return super.execute();
	}
	
	public String category_suggest_submit() throws Exception {
		StringUtil.trimToEmpty(categorySuggest, "content1,content2,content3");
		
		if((StringUtil.isBlank(categorySuggest.getContent1()) && StringUtil.isBlank(categorySuggest.getContent2()) && StringUtil.isBlank(categorySuggest.getContent3())) 
				|| categorySuggest.getContent1().length() > 3000 || categorySuggest.getContent2().length() > 3000 || categorySuggest.getContent3().length() > 3000){
			tip = "categorySuggest.content.required";
		}else{
			tip = b2BService.addCategorySuggest(request, categorySuggest);
		}

		msg = getText(tip);
		return SUCCESS;
	}


	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return null;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public CategorySuggest getModel() {
		return categorySuggest;
	}

	public void setB2BService(B2BService service) {
		b2BService = service;
	}
}
