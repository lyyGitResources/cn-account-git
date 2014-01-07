/* 
 * Created by baozhimin at Dec 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.search.webservice.Search;
import com.hisupplier.commons.entity.Category;
import com.hisupplier.commons.entity.cn.Keyword;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class QueryAction extends BasicAction implements ModelDriven<QueryParams>{

	private static final long serialVersionUID = -5327295498157047789L;
	private Search searchService;
	private QueryParams queryParams = new QueryParams();
	private Map<String, Object> result;

	public String relate_category_list() throws Exception {
		List<Category> catList = searchService.findRelateCatList(queryParams.getQueryText(), queryParams.getPageSize());
		result = new HashMap<String, Object>();
		result.put("catList", catList);
		return super.execute();
	}

	public String relate_keyword_list() throws Exception {
		List<Keyword> keywordList = searchService.findRelateKeywordList("product", queryParams.getQueryText(), 0, 20);
		if(keywordList != null){
			List<Keyword> keywordListBak = new ArrayList<Keyword>();
			for(Keyword keyword : keywordList){
				if(!StringUtil.equals(keyword.getKeyName(), queryParams.getKeyword1()) 
						&& !StringUtil.equals(keyword.getKeyName(), queryParams.getKeyword2())
						&& !StringUtil.equals(keyword.getKeyName(), queryParams.getKeyword3())){
					keywordListBak.add(keyword);
				}
			}
			keywordList = keywordListBak;
		}
		result = new HashMap<String, Object>();
		result.put("keywordList", keywordList);
		return super.execute();
	}
	
	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return null;
	}

	public QueryParams getModel() {
		return queryParams;
	}
	
	public void setSearchService(Search searchService) {
		this.searchService = searchService;
	}
}
