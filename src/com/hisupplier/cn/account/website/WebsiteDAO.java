/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.WebSiteTemplate;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;

/**
 * @author taofeng
 *
 */
public class WebsiteDAO extends DAO {

	/**
	 * 根据公司id查询webSite
	 * @param comId
	 * @return
	 */
	public WebSite findWebsite(int comId) {
		return (WebSite) this.getSqlMapClientTemplate().queryForObject("website.findWebsite", comId);
	}

	/**
	 * 查询Template，返回listResult
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<WebSiteTemplate> findTemplateList(QueryParams params) {
		Page page = null;
		List<WebSiteTemplate> templateList = null;
		if (params.getPageNo() != -1) {
			int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("template.findTemplateListCount", params);
			if (resultTotal == 0 || resultTotal == 1) {
				return new ListResult<WebSiteTemplate>(new ArrayList<WebSiteTemplate>(0), page);
			}
			resultTotal = resultTotal - 1;//第0套模板为免费用户使用
			page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
			params.setStartRow(page.getStartRow());
		}
		templateList = this.getSqlMapClientTemplate().queryForList("template.findTemplateList", params);
		List<WebSiteTemplate> templateList_ = new ArrayList<WebSiteTemplate>(templateList.size());
		for (WebSiteTemplate template : templateList) {
			if (template.getTemplateNo() == 0) {
				continue;
			}
			templateList_.add(template);
		}

		return new ListResult<WebSiteTemplate>(templateList_, page);
	}

	/**
	 * 新增WebSite
	 * @param webSite
	 * @return 
	 */
	public void addWebSite(WebSite webSite) {
		this.getSqlMapClientTemplate().insert("website.addWebSite", webSite);
	}

	/**
	 * 指定ID查询模板对象
	 * @param templateId
	 * @return
	 */
	public WebSiteTemplate findTemplate(int templateId) {
		return (WebSiteTemplate) this.getSqlMapClientTemplate().queryForObject("website.findTemplate", (Integer) templateId);
	}
}
