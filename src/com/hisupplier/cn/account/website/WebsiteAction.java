/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.WebSiteModule;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author taofeng
 *
 */
public class WebsiteAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = 6294967420455581328L;
	private QueryParams params = new QueryParams();
	private WebsiteService websiteService;
	Map<String, Object> result;

	public WebsiteAction() {
		super();
		this.currentMenu = "website";
	}

	public String design() throws Exception {
		params.setRequest(ServletActionContext.getRequest());
		result = websiteService.getTemplateList(params);
		return SUCCESS;
	}

	public String save() throws Exception {
		int comId = params.getLoginUser().getComId();
		
		if (params.getTemplateNo() == 0) {
			return SUCCESS;
		}
		
		WebSite webSite = new WebSite();
		webSite.setComId(comId);
		webSite.setLayoutNo(params.getLayoutNo());
		webSite.setTemplateNo(params.getTemplateNo());
		webSite.setBannerNo(params.getBannerNo());
		webSite.setBannerType(1);
		webSite.setNbannerPath(params.getNbannerPath());
		webSite.setCnFontType(params.getCnFontType());
		webSite.setCnFontSize(params.getCnFontSize());
		webSite.setCnFontColor(params.getCnFontColor());

		String[] modNames = StringUtil.toArray(params.getModules(), ",");
		List<WebSiteModule> modules = new ArrayList<WebSiteModule>();
		for (String modName : modNames) {
			WebSiteModule module = new WebSiteModule();
			module.setComId(comId);
			module.setModName(modName);
			modules.add(module);
		}
		tip = websiteService.saveWebSiteDesign(comId, params.getMenuGroupIds(), webSite, modules, params);
		if ("fail".equals(tip)) {
			this.addActionError("图片上传错误");
			params.setRequest(ServletActionContext.getRequest());
			result = websiteService.getTemplateList(params);
			return "input";
		} else if ("short".equals(tip)) {
			this.addMessage("设置成功");
		} else {
			this.addMessage("设置成功，新的展示方案将在几分钟内生效");
		}
		return SUCCESS;
	}

	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public String getTip() {
		return null;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setWebsiteService(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}
}
