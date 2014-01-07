/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.ChatItem;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author taofeng
 *
 */
public class WebsiteFormAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = 4925814041299161090L;
	private QueryParams params = new QueryParams();
	private WebsiteService websiteService;
	Map<String, Object> result;
	private WebSite website = new WebSite();

	public WebsiteFormAction() {
		super();
		this.currentMenu = "website";
	}

	public String website_set() throws Exception {
//		if (params.getLoginUser().getMemberType() != CN.GOLD_SITE) {
//			throw new PageNotFoundException();
//		}
		result = websiteService.getWebSite(params);
		return SUCCESS;
	}

	public String website_set_save() throws Exception {
//		if (params.getLoginUser().getMemberType() != CN.GOLD_SITE) {
//			throw new PageNotFoundException();
//		}
		params.setChatMsg(StringUtil.trimToEmpty(params.getChatMsg()));
		
		String[] chatUserIds = StringUtil.toArray(params.getChatUserId(), ",");
		website.setComId(params.getLoginUser().getComId());
		website.setChatMsg(params.getChatMsg());
		website.setChatTip(params.isChatTip());
		website.setChatUserId(ChatItem.toCompose(chatUserIds));
		website.setGroupFold(params.isGroupFold());
		website.setSiteLink(params.getSiteLink());
		website.setSiteName(params.getSiteName());
		
		result = new HashMap<String, Object>(); 
		result.put("website", website);
		 

		if (params.getChatMsg().length() > 50) {
			result.putAll(websiteService.getWebsiteSubmitError(params));
			this.addActionError("欢迎语长度必须在50个字符内");
			return INPUT;
		}
		
		params.setSiteName(StringUtil.trimToEmpty(params.getSiteName()));
		params.setSiteLink(StringUtil.trimToEmpty(params.getSiteLink()));

		boolean existSiteName = StringUtil.isNotEmpty(params.getSiteName()) ? true : false;
		boolean existSiteLink = StringUtil.isNotEmpty(params.getSiteLink()) ? true : false;

		if (this.params.getSiteName().length() > 120) {
			this.addActionError("网站名称长度必须在1~120个字符之间");
		}
		if (this.params.getSiteLink().length() > 120) {
			this.addActionError("网址长度必须在1~120个字符之间");
		}
		if (existSiteLink) {
			if (!Validator.isUrl(this.params.getSiteLink())) {
				this.addActionError("网址格式不正确");
			} else {
				if (!params.getSiteLink().startsWith("http")) {
					params.setSiteLink("http://" + params.getSiteLink());
				}
			}
		}
		if ((existSiteName == true && existSiteLink == false) || (existSiteName == false && existSiteLink == true)) {
			this.addActionError("名称和网址两者不允许只填一项");
		}

		if (this.getActionErrors().size() > 0) {
			result.putAll(websiteService.getWebsiteSubmitError(params));
			return INPUT;
		}
		
		this.websiteService.updateWebsite(website, params);
		this.addMessage("网站设置成功");
		return SUCCESS;
	}

	public String add_other_form() throws Exception {
		if (StringUtil.isEmpty(params.getLoginUser().getDomain())) {
			throw new PageNotFoundException();
		}
		result = this.websiteService.getWebSiteOnly(params.getLoginUser().getComId());
		return SUCCESS;
	}

	public String add_other() throws Exception {
		if (StringUtil.isEmpty(params.getLoginUser().getDomain())) {
			throw new PageNotFoundException();
		}
		params.setSiteName(StringUtil.trimToEmpty(params.getSiteName()));
		params.setSiteLink(StringUtil.trimToEmpty(params.getSiteLink()));

		boolean existSiteName = StringUtil.isNotEmpty(params.getSiteName()) ? true : false;
		boolean existSiteLink = StringUtil.isNotEmpty(params.getSiteLink()) ? true : false;

		if (this.params.getSiteName().length() > 120) {
			this.addActionError("网站名称长度必须在1~120个字符之间");
		}
		if (this.params.getSiteLink().length() > 120) {
			this.addActionError("网址长度必须在1~120个字符之间");
		}
		if (existSiteLink) {
			if (!Validator.isUrl(this.params.getSiteLink())) {
				this.addActionError("网址格式不正确");
			} else {
				if (!params.getSiteLink().startsWith("http")) {
					params.setSiteLink("http://" + params.getSiteLink());
				}
			}
		}
		if ((existSiteName == true && existSiteLink == false) || (existSiteName == false && existSiteLink == true)) {
			this.addActionError("名称和网址两者不允许只填一项");
		}

		if (this.getActionErrors().size() > 0) {
			WebSite webSite = new WebSite();
			result = new HashMap<String, Object>();
			webSite.setSiteName(params.getSiteName());
			webSite.setSiteLink(params.getSiteLink());
			result.put("website", webSite);
			return INPUT;
		}
		this.websiteService.addOtherWebsite(params.getLoginUser().getComId(), params.getSiteName(), params.getSiteLink());
		this.addMessage("添加成功");
		return SUCCESS;
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
		return params;
	}

	public void setWebsiteService(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}

	public WebSite getWebsite() {
		return website;
	}

	public void setWebsite(WebSite website) {
		this.website = website;
	}
}
