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
			this.addActionError("��ӭ�ﳤ�ȱ�����50���ַ���");
			return INPUT;
		}
		
		params.setSiteName(StringUtil.trimToEmpty(params.getSiteName()));
		params.setSiteLink(StringUtil.trimToEmpty(params.getSiteLink()));

		boolean existSiteName = StringUtil.isNotEmpty(params.getSiteName()) ? true : false;
		boolean existSiteLink = StringUtil.isNotEmpty(params.getSiteLink()) ? true : false;

		if (this.params.getSiteName().length() > 120) {
			this.addActionError("��վ���Ƴ��ȱ�����1~120���ַ�֮��");
		}
		if (this.params.getSiteLink().length() > 120) {
			this.addActionError("��ַ���ȱ�����1~120���ַ�֮��");
		}
		if (existSiteLink) {
			if (!Validator.isUrl(this.params.getSiteLink())) {
				this.addActionError("��ַ��ʽ����ȷ");
			} else {
				if (!params.getSiteLink().startsWith("http")) {
					params.setSiteLink("http://" + params.getSiteLink());
				}
			}
		}
		if ((existSiteName == true && existSiteLink == false) || (existSiteName == false && existSiteLink == true)) {
			this.addActionError("���ƺ���ַ���߲�����ֻ��һ��");
		}

		if (this.getActionErrors().size() > 0) {
			result.putAll(websiteService.getWebsiteSubmitError(params));
			return INPUT;
		}
		
		this.websiteService.updateWebsite(website, params);
		this.addMessage("��վ���óɹ�");
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
			this.addActionError("��վ���Ƴ��ȱ�����1~120���ַ�֮��");
		}
		if (this.params.getSiteLink().length() > 120) {
			this.addActionError("��ַ���ȱ�����1~120���ַ�֮��");
		}
		if (existSiteLink) {
			if (!Validator.isUrl(this.params.getSiteLink())) {
				this.addActionError("��ַ��ʽ����ȷ");
			} else {
				if (!params.getSiteLink().startsWith("http")) {
					params.setSiteLink("http://" + params.getSiteLink());
				}
			}
		}
		if ((existSiteName == true && existSiteLink == false) || (existSiteName == false && existSiteLink == true)) {
			this.addActionError("���ƺ���ַ���߲�����ֻ��һ��");
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
		this.addMessage("��ӳɹ�");
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
