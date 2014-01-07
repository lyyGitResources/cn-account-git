/* 
 * Created by taofeng at Feb 4, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.basic.AdminLogDAO;
import com.hisupplier.cn.account.basic.BasicService;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.ChatItem;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Talk;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.entity.WebSiteModule;
import com.hisupplier.cn.account.entity.WebSiteTemplate;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.TalkDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.menu.MenuDAO;
import com.hisupplier.cn.account.util.ModuleTitle;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.entity.cn.Company;
import com.hisupplier.commons.entity.cn.WebSite;
import com.hisupplier.commons.entity.cn.WebSiteModuleTitle;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author taofeng
 *
 */
public class WebsiteService extends BasicService{

	private WebsiteDAO websiteDAO;
	private UserDAO userDAO;
	private WebsiteModuleDAO websiteModuleDAO;
	private MenuDAO menuDAO;
	private CompanyDAO companyDAO;
	private WebSiteModuleTitleDAO webSiteModuleTitleDAO;
	private TalkDAO talkDAO;
	private AdminLogDAO adminLogDAO;
	/**<p>选择模板
	 * <pre>
	 * 1)按comId查询WebSite，返回webSite
	 * 2)查询WebSiteTemplate，返回templateListResult
	 * 3)查询自定义菜单，返回menuGroupList
	 * 4)查询WebSiteModule，返回moduleList
	 * 5)返回当前用户设置的bannerWidth、bannerHeight
	 * 6)返回图片上传根路劲imgLink
	 * 7)返回bannerBase
	 * </pre>
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTemplateList(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int comId = params.getLoginUser().getComId();
		
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		result.put("isImgFull", false);
		// 图片已满
		result.put("isImgFull", company.getImgCount() >= company.getImgMax());
		
		WebSite webSite = websiteDAO.findWebsite(comId);
		webSite.setComName(company.getComName());
		if (StringUtil.isEmpty(webSite.getBannerPath())) {
			WebSiteTemplate webSiteTemplate = websiteDAO.findTemplate(webSite.getTemplateNo());
			if (webSiteTemplate.isNewHome()) {
				webSite.setBannerPath("/img/tmp-home.jpg");
			} else {
				webSite.setBannerPath("/img/tmp.jpg");
			}
		}
		if (StringUtil.isEmpty(webSite.getNbannerPath())) {
			webSite.setNbannerPath("/img/tmp-page.jpg");
		}
		
		params.setPageNo(-1);
		List<WebSiteTemplate> templateList = websiteDAO.findTemplateList(params).getList();
		int totalRecord = templateList.size();
		int pageNo = 0, index = 1, pageSize = 0;
		boolean isNewTemplate = false;
		if (params.getTemplateType() == -1 && params.getTemplateColor() == -1) {
			for (WebSiteTemplate template : templateList) {
				if (webSite.getTemplateNo() == template.getTemplateNo()) {
					isNewTemplate = template.isNewHome();
					pageNo = template.getPageNo(index);
					pageSize = template.getPageSize();
					result.put("bannerWidth", template.getBannerWidth());
					result.put("bannerHeight", template.getBannerHeight());
					result.put("insideBannerWidth", template.getInsideBannerWidth());
					result.put("insideBannerHeight", template.getInsideBannerHeight());
					result.put("bannerMaxNum", template.getBannerNum());//获得当前的模板所能上传banner的最大数量
					break;
				}
				index++;
			}
		} else {
			pageNo = 1;
			pageSize = 5;
			WebSiteTemplate currTemplate = websiteDAO.findTemplate(webSite.getTemplateNo());
			result.put("bannerWidth", currTemplate.getBannerWidth());
			result.put("bannerHeight", currTemplate.getBannerHeight());
			result.put("bannerWidth", currTemplate.getInsideBannerWidth());
			result.put("bannerHeight", currTemplate.getInsideBannerHeight());
			result.put("bannerMaxNum", currTemplate.getBannerNum());//获得当前的模板所能上传banner的最大数量
		}
		Page page = PageFactory.createPage(totalRecord, pageNo, pageSize);
		ListResult<WebSiteTemplate> templateListResult = new ListResult<WebSiteTemplate>(templateList, page);
		com.hisupplier.cn.account.menu.QueryParams params_ = new com.hisupplier.cn.account.menu.QueryParams();
		params_.setLoginUser(params.getLoginUser());
		List<Group> menuGroupList = menuDAO.findMenuGroupList(params_);
		List<WebSiteModule> moduleList = websiteModuleDAO.findModules(params.getLoginUser().getComId());
		StringBuilder modSelector = new StringBuilder();
		for (WebSiteModule mod : moduleList) {
			modSelector.append("#").append(mod.getModName()).append(",");
		}
		if (modSelector.length() == 0) {
			modSelector.append("#m_product_hot,#m_product_newlist,#m_product_group,#m_special_group,#m_video,#m_online_service,#m_contact,#m_feature_product,#m_company_info");
		}
		if (!webSite.getDomain().startsWith("http")) {
			webSite.setDomain("http://" + webSite.getDomain());
		}
		String rejectLayoutNo = "-1";
		int bannerSet = 1;
		for (WebSiteTemplate template : templateList) {
			if (template.getTemplateNo() == webSite.getTemplateNo()) {
				rejectLayoutNo = template.getRejectLayoutNo();
				bannerSet = template.isNewHome() ? 2 : 1;
				break;
			}
		}
		//获取用户自己定义的模块标题
		List<WebSiteModuleTitle> list = this.webSiteModuleTitleDAO.findModuleTitleList(params.getLoginUser().getComId());

		Map<String, String> moduleTitleMapSub = new HashMap<String, String>();
		Map<String, String> moduleTitleMap = new HashMap<String, String>();
		for (WebSiteModuleTitle webSiteModuleTitle : list) {
			moduleTitleMapSub.put(webSiteModuleTitle.getTitleKey(), StringUtil.substring(webSiteModuleTitle.getTitleContent(), 4, "...", false));
			moduleTitleMap.put(webSiteModuleTitle.getTitleKey(), webSiteModuleTitle.getTitleContent());
		}
		String stateName = "";
		boolean isReject = false;
		if (webSite.getBannerNo() == 0) {
			if (webSite.getBannerState() == CN.STATE_PASS ) {
				stateName = "<span class='red'>（审核通过）</span>";
			} else if (webSite.getBannerState() == CN.STATE_WAIT || webSite.getBannerState() == CN.STATE_REJECT_WAIT){
				stateName = "<span class='red'>（等待审核）</span>";
			} else if (webSite.getBannerState() == CN.STATE_REJECT) {
				stateName = "<span class='red'>（审核未通过）</span>";
				result.put("adminLog", adminLogDAO.findAdminLog(webSite.getComId(), "Banner"));
				isReject = true;
			}
		}
		result.put("isNewTemplate", isNewTemplate);
		result.put("stateName", stateName);
		result.put("isReject", isReject);
		result.put("webSite", webSite);
		result.put("rejectLayoutNo", rejectLayoutNo);
		result.put("bannerSet", bannerSet);
		result.put("templateListResult", templateListResult);
		result.put("menuGroupList", menuGroupList);
		result.put("modSelector", StringUtil.trimComma(modSelector.toString()));
		result.put("moduleTitleMap", moduleTitleMap);
		result.put("moduleTitleMapSub", moduleTitleMapSub);
		result.put("imgLink", Config.getString("img.link"));
		// result.put("bannerBase", "http://" + CASClient.getInstance().getUser(params.getRequest()).getMemberId() + ".cn." + Config.getString("sys.domain"));
		result.put("bannerBase", "/css/banner");
		return result;
	}

	/**<p>网站设置
	 * <pre>
	 * 1)按comId查询WebSite，返回website
	 * 2)按comId查询Users，返回userList（用于聊天帐号选择）
	 * </pre>
	 * @param params
	 * @return
	 */
	public Map<String, Object> getWebSite(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int comId = params.getLoginUser().getComId();

		WebSite webSite = websiteDAO.findWebsite(comId);
		Company company = companyDAO.findCompany(comId);
		User admin = userDAO.findUserByAdmin(comId);
		List<Talk> talkList = talkDAO.findCompanyTalks(comId);
		Talk.oldData(webSite, talkList);
		
		Map<String, String> moduleTitleMap = getWebSiteModuleTitle(comId);
		
		result.put("talkList", talkList);
		result.put("adminHasTalk", !StringUtil.isBlank(admin.getQq()));
		result.put("website", webSite);
		result.put("company", company);
		result.put("imgBase", Config.getString("img.base"));
		result.put("moduleTitleMap", moduleTitleMap);

		return result;
	}

	/**
	 * 获取用户自己定义的模块标题
	 * @param params
	 * @return
	 */
	private Map<String, String> getWebSiteModuleTitle(int comId) {
		List<WebSiteModuleTitle> list = this.webSiteModuleTitleDAO.findModuleTitleList(comId);
		
		Map<String, String> result = new HashMap<String, String>(list.size());
		
		for (WebSiteModuleTitle webSiteModuleTitle : list) {
			result.put(webSiteModuleTitle.getTitleKey(), webSiteModuleTitle.getTitleContent());
		}
		if (!result.containsKey("search.alert")) {
			result.put("search.alert", "请输入产品名称或型号");
		}
		
		return result;
	}

	/**<p>仅获取网站设置对象
	 * <pre>
	 * 1)按comId查询WebSite，返回website
	 * </pre>
	 * @param comId
	 * @return
	 */
	public Map<String, Object> getWebSiteOnly(int comId) {
		WebSite webSite = websiteDAO.findWebsite(comId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("website", webSite);
		return result;
	}

	/**
	 * <p>网站设置保存
	 * @param comId
	 * @param menuGroupIds
	 * @param website 所需属性layoutNo,templateNo,bannerNo,bannerType,bannerPath
	 * @param modules
	 */
	public String saveWebSiteDesign(int comId, String menuGroupIds, WebSite webSite, List<WebSiteModule> modules, QueryParams params) {

		String currentTime = new DateUtil().getDateTime();
		UpdateMap updateMap_website = new UpdateMap("WebSite");
		updateMap_website.addField("layoutNo", webSite.getLayoutNo());
		updateMap_website.addField("templateNo", webSite.getTemplateNo());
		updateMap_website.addField("bannerNo", webSite.getBannerNo());
		updateMap_website.addField("bannerType", webSite.getBannerType());
		updateMap_website.addField("cnFontType", webSite.getCnFontType());
		updateMap_website.addField("cnFontSize", webSite.getCnFontSize());
		updateMap_website.addField("cnFontColor", webSite.getCnFontColor());
		Image image = null;
		if (params.getBannerAddImg() != null && params.getBannerAddImg().length > 0) {
			String bannerAddImgs = "";
			for (String bannerImg :params.getBannerAddImg()) {
				Map<String, String> map = UploadUtil.getImgParam(bannerImg);
				if (Boolean.parseBoolean(map.get("isUpload"))) {
					image = new Image();
					image.setComId(comId);
					image.setImgName(map.get("imgName"));
					image.setImgPath(map.get("imgPath"));
					image.setImgType(Integer.parseInt(map.get("imgType")));
					com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
					if (img.getImgId() <= 0) {
						return "fail";
						//throw new ServiceException("图片上传错误");
					}
					bannerAddImgs += "," +img.getImgPath(); 
				}else{
					bannerAddImgs += "," + bannerImg;
				}
			}
			//设置bannerPath 用于后面banner审核状态的比较更改
			webSite.setBannerPath(StringUtil.trimComma(bannerAddImgs));
			updateMap_website.addField("bannerPath", webSite.getBannerPath());
		} else if (params.getBannerAddImg() == null && params.getBannerNo() > 0) {
			webSite.setBannerPath("");//使用模板banner下，删除 自定义模板banner
			updateMap_website.addField("bannerPath", "");
		}

		WebSiteTemplate webSiteTemplate = websiteDAO.findTemplate(webSite.getTemplateNo());
		if (StringUtil.isNotEmpty(webSite.getNbannerPath()) 
				&& !webSite.getNbannerPath().equalsIgnoreCase("/img/tmp-page.jpg") 
				&& webSiteTemplate.isNewHome()) {
			Map<String, String> map = UploadUtil.getImgParam(webSite.getNbannerPath());
			if (Boolean.parseBoolean(map.get("isUpload"))) {
				image = new Image();
				image.setComId(comId);
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "fail";
				}
				updateMap_website.addField("nbannerPath", img.getImgPath());
			}else{
				updateMap_website.addField("nbannerPath", webSite.getNbannerPath());
			}
		}
		
		if (webSite.getNbannerPath().equalsIgnoreCase("/img/tmp-page.jpg") 
				|| !webSiteTemplate.isNewHome()) {
			updateMap_website.addField("nbannerPath", "");
		}
		//添加模板、板式、BANNER用户日志
		WebSite webSite2 = websiteDAO.findWebsite(webSite.getComId());
		if (webSite.getTemplateNo() != webSite2.getTemplateNo() ||
				webSite.getLayoutNo() != webSite2.getLayoutNo() ||
				webSite.getBannerNo() != webSite2.getBannerNo()) {
			String content = null; //日志内容
			if (webSite.getBannerNo() != 0) {
				content = "网站设计——修改为第" + webSite.getTemplateNo() + "套模板;" 
					+ webSite.getLayoutNo() +"号板式;" + webSite.getBannerNo() + "号BANNER";
			}
			else if (webSite.getBannerNo() == 0) {
				content = "网站设计——修改为第" + webSite.getTemplateNo() + "套模板;" 
					+ webSite.getLayoutNo() +"号板式;" + "自定义BANNER";
			}
			UserLog userLog = UserLogUtil.getUserLog(UserLog.WEBSITE, UserLog.MODIFY, content, params.getLoginUser());
			websiteDAO.addUserLog(userLog);
 		}
		//更改BANNER审核状态
		int state = webSite2.getBannerState();
		if (webSite.getTemplateNo() != webSite2.getTemplateNo()) {
			state = CN.STATE_WAIT;
		} else if ((!StringUtil.isBlank(webSite2.getBannerPath()) && !webSite2.getBannerPath().equals(webSite.getBannerPath()))
				|| (!StringUtil.isBlank(webSite2.getNbannerPath()) && !webSite2.getNbannerPath().equals(webSite.getNbannerPath()))) {
			if (webSite2.getBannerState() == CN.STATE_PASS) {
				state = CN.STATE_WAIT;
			} else if (webSite2.getBannerState() == CN.STATE_REJECT) {
				state = CN.STATE_REJECT_WAIT;
			}
		}
		if (state != webSite2.getBannerState()) {
			updateMap_website.addField("bannerState", state);
		}
		updateMap_website.addField("modifyTime", currentTime);
		updateMap_website.addWhere("comId", comId);
		this.websiteDAO.update(updateMap_website);

		this.websiteModuleDAO.deleteModules(comId);

		UpdateMap updateMap_menuGroup01 = new UpdateMap("MenuGroup");
		updateMap_menuGroup01.addField("isShow", 0);
		updateMap_menuGroup01.addWhere("comId", comId);
		this.websiteDAO.update(updateMap_menuGroup01);

		String[] ids = StringUtil.toArray(menuGroupIds, ",");
		if (ids.length > 0) {
			UpdateMap updateMap_menuGroup02 = new UpdateMap("MenuGroup");
			updateMap_menuGroup02.addField("modifyTime", currentTime);
			updateMap_menuGroup02.addWhere("comId", comId);
			updateMap_menuGroup02.addField("isShow", 1);
			updateMap_menuGroup02.addWhere("groupId", menuGroupIds, "in");
			this.websiteDAO.update(updateMap_menuGroup02);
		}

		this.setModuleList(webSite.getLayoutNo(), modules);
		this.websiteModuleDAO.addModules(modules);
		return (state != CN.STATE_PASS && webSite.getBannerNo() == 0) ? "short" : "success";
	}

	/**
	 * 添加其他版本的网站
	 * @param comId
	 * @param siteName
	 * @param siteLink
	 * @return int
	 */
	public int addOtherWebsite(int comId, String siteName, String siteLink) {
		String currentTime = new DateUtil().getDateTime();
		UpdateMap updateMap_website = new UpdateMap("WebSite");
		updateMap_website.addField("siteName", siteName);
		updateMap_website.addField("siteLink", siteLink);
		updateMap_website.addField("modifyTime", currentTime);
		updateMap_website.addWhere("comId", comId);
		return this.websiteDAO.update(updateMap_website);
	}

	/**
	 * 更新网站组展示设置、聊天账号设置
	 * @param website
	 * @return int
	 */
	public int updateWebsite(WebSite website, QueryParams params) {
		String currentTime = new DateUtil().getDateTime();
		UpdateMap updateMap_website = new UpdateMap("WebSite");
		updateMap_website.addField("chatMsg", website.getChatMsg());
		updateMap_website.addField("isChatTip", website.isChatTip() ? 1 : 0);
		updateMap_website.addField("modifyTime", currentTime);
		updateMap_website.addField("chatUserId", website.getChatUserId());
		updateMap_website.addField("isGroupFold", website.isGroupFold() ? 1 : 0);
		updateMap_website.addField("siteName", website.getSiteName());
		updateMap_website.addField("siteLink", params.getSiteLink());
		updateMap_website.addField("isShowQR", params.isIndieQR() ? 1 : 0);
		//判断风格网站域名是否被修改
		if (params.isIndieQR()) {
			WebSite website2 = websiteDAO.findWebsite(website.getComId());
			String domain = StringUtil.substringAfterLast(website2.getQrPath(), "|");
			if (!domain.equals(website2.getDomain())) {
					updateMap_website.addField("qrPath",
							qrcode(website2.getComId(), "http://" + website2.getDomain()) + "|" + website2.getDomain());	
			}
		}
		updateMap_website.addWhere("comId", website.getComId());
		this.websiteDAO.update(updateMap_website);

		this.webSiteModuleTitleDAO.deleteModuleTitle(params.getLoginUser().getComId());

		List<WebSiteModuleTitle> moduleTitleList = this.getWebSiteModuleTitleList(params);
		if (moduleTitleList.size() > 0) {
			this.webSiteModuleTitleDAO.addModuleTitle(moduleTitleList);
		}
		
		
		
		UpdateMap updateMap_talk = new UpdateMap("Talk");
		updateMap_talk.addField("isShow", 0);
		updateMap_talk.addWhere("comId", website.getComId());
		talkDAO.update(updateMap_talk);
		if (params.getTalkIds() != null && params.getTalkIds().length > 0) {
			updateMap_talk.addField("isShow", 1);
			updateMap_talk.addWhere("id", StringUtil.join(params.getTalkIds(), ",", "\"", "\"", null, null), "in");
			talkDAO.update(updateMap_talk);
		}

		UpdateMap updateMap_companyExtra = new UpdateMap("CompanyExtra");
		updateMap_companyExtra.addField("productListStyle", params.getProductListStyle());
		updateMap_companyExtra.addField("tradeListStyle", params.getTradeListStyle());
		updateMap_companyExtra.addField("isShowQR", params.isShowroomQR() ? 1 : 0);
		updateMap_companyExtra.addWhere("comId", website.getComId());
		return this.websiteDAO.update(updateMap_companyExtra);
	}

	/**
	 * 模块所属容器号、排序号设置
	 * ps:需与预览风格网站时相同（参考风格网站默认布局）
	 * @param layoutNo
	 * @param modules
	 */
	private void setModuleList(int layoutNo, List<WebSiteModule> modules) {
		String currentTime = new DateUtil().getDateTime();
		int index = 1;
		Iterator<WebSiteModule> it = modules.iterator();
		while (it.hasNext()) {
			WebSiteModule module = it.next();
			switch (layoutNo) {
				//容器0，1，5允许填充内容更，0由css模块填充
				case 1:
					if (ModuleTitle.in(ModuleTitle.L1_C1, module.getModName())) {
						module.setContainerNo(1);
					} else if (ModuleTitle.in(ModuleTitle.L1_C5, module.getModName())) {
						module.setContainerNo(5);
					} else {
						it.remove();
					}
					break;
				//容器0，1，5允许填充内容更，0由css模块填充
				case 2:
					if (ModuleTitle.in(ModuleTitle.L2_C1, module.getModName())) {
						module.setContainerNo(1);
					} else if (ModuleTitle.in(ModuleTitle.L2_C5, module.getModName())) {
						module.setContainerNo(5);
					} else {
						it.remove();
					}
					break;
				//容器0，1，3,5允许填充内容更，0由css模块填充
				case 3:
					if (ModuleTitle.in(ModuleTitle.L3_C1, module.getModName())) {
						module.setContainerNo(1);
					} else if (ModuleTitle.in(ModuleTitle.L3_C3, module.getModName())) {
						module.setContainerNo(3);
					} else if (ModuleTitle.in(ModuleTitle.L3_C5, module.getModName())) {
						module.setContainerNo(5);
					} else {
						it.remove();
					}
					break;
			}
			module.setOrderNo(10 + index);//排序号从11开始，预留10个顺序给CSS_MODULE
			module.setCreateTime(currentTime);
			index++;
		}
	}

	/**
	 * 组合模块标题
	 * @param params
	 * @return
	 */
	private List<WebSiteModuleTitle> getWebSiteModuleTitleList(QueryParams params) {
		List<WebSiteModuleTitle> list = new ArrayList<WebSiteModuleTitle>(12);
		if (StringUtil.isNotBlank(params.getProduct_list())) {
			if(params.getProduct_list().length() > 120){
				params.setProduct_list(params.getProduct_list().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("product.list");
			moduleTitle.setTitleContent(params.getProduct_list());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getProduct_group())) {
			if(params.getProduct_group().length() > 120){
				params.setProduct_group(params.getProduct_group().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("product.group");
			moduleTitle.setTitleContent(params.getProduct_group());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getProduct_special())) {
			if(params.getProduct_special().length() > 120){
				params.setProduct_special(params.getProduct_special().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("product.special");
			moduleTitle.setTitleContent(params.getProduct_special());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getProduct_feature())) {
			if(params.getProduct_feature().length() > 120){
				params.setProduct_feature(params.getProduct_feature().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("product.feature");
			moduleTitle.setTitleContent(params.getProduct_feature());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getCompany_introduce())) {
			if(params.getCompany_introduce().length() > 120){
				params.setCompany_introduce(params.getCompany_introduce().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("company.introduce");
			moduleTitle.setTitleContent(params.getCompany_introduce());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getCompany_brief())) {
			if(params.getCompany_brief().length() > 120){
				params.setCompany_brief(params.getCompany_brief().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("company.brief");
			moduleTitle.setTitleContent(params.getCompany_brief());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getCompany_member())) {
			if(params.getCompany_member().length() > 120){
				params.setCompany_member(params.getCompany_member().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("company.member");
			moduleTitle.setTitleContent(params.getCompany_member());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getService_contact())) {
			if(params.getService_contact().length() > 120){
				params.setService_contact(params.getService_contact().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("service.contact");
			moduleTitle.setTitleContent(params.getService_contact());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getService_online())) {
			if(params.getService_online().length() > 120){
				params.setService_online(params.getService_online().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("service.online");
			moduleTitle.setTitleContent(params.getService_online());
			list.add(moduleTitle);
		}

		if (StringUtil.isNotBlank(params.getOffer_list())) {
			if(params.getOffer_list().length() > 120){
				params.setOffer_list(params.getOffer_list().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("offer.list");
			moduleTitle.setTitleContent(params.getOffer_list());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getVideo_list())) {
			if(params.getVideo_list().length() > 120){
				params.setVideo_list(params.getVideo_list().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("video.list");
			moduleTitle.setTitleContent(params.getVideo_list());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getInquiry_online())) {
			if(params.getInquiry_online().length() > 120){
				params.setInquiry_online(params.getInquiry_online().substring(0,120));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("inquiry.online");
			moduleTitle.setTitleContent(params.getInquiry_online());
			list.add(moduleTitle);
		}
		if (StringUtil.isNotBlank(params.getSearch_alert())) {
			if (params.getSearch_alert().length() > 20) {
				params.setSearch_alert(StringUtil.substring(params.getSearch_alert(), 0, 20));
			}
			WebSiteModuleTitle moduleTitle = new WebSiteModuleTitle();
			moduleTitle.setTitleKey("search.alert");
			moduleTitle.setTitleContent(params.getSearch_alert());
			list.add(moduleTitle);
		}

		String currentTime = new DateUtil().getDateTime();
		for (WebSiteModuleTitle moduleTitle : list) {
			moduleTitle.setComId(params.getLoginUser().getComId());
			moduleTitle.setCreateTime(currentTime);
			moduleTitle.setModifyTime(currentTime);
		}

		return list;
	}
	/**
	 * 验证错误时，按特殊分组ID取得特殊分组名称
	 * @param 
	 * @return
	 * <pre>
	 * </pre>
	 */
	public Map<String, Object> getWebsiteSubmitError(QueryParams params) {
		Map<String, Object>  result = new HashMap<String, Object>();
		Company company = new Company();
		company.setProductListStyle(params.getProductListStyle());
		company.setTradeListStyle(params.getTradeListStyle());
		
		WebSite webSite = websiteDAO.findWebsite(params.getLoginUser().getComId());
		List<User> userList_ = userDAO.findUserList(params.getLoginUser().getComId());
		List<User> userList = new ArrayList<User>();
		// 占位，管理员放在最前面
		userList.add(0, null);
		boolean hasTqId = false;
		for (User user : userList_) {
			if(user.getTqId() > 0){
				hasTqId = true;
			}
			if (user.getTqId() > 0 || StringUtil.isNotBlank(user.getQq()) || StringUtil.isNotBlank(user.getMsn())) {
				if (StringUtil.isNotEmpty(webSite.getChatUserId())) {
					String[] chatUserId = StringUtil.toArray(params.getChatUserId(), ",");
					String[] chatUserIds = ChatItem.toCompose(chatUserId).split(",");
					for (String chat : chatUserIds) {
						ChatItem chatItem = new ChatItem(); 
						chatItem.toSplit(chat);
						if (user.getUserId() == chatItem.getUserId()) {
							user.setChatItem(chatItem);
							break;
						}
					}
				}
				if (user.isAdmin()) {
					userList.remove(0);
					userList.add(0, user);
				} else {
					userList.add(user);
				}
			}
		}
		Map<String, String> moduleTitleMap = new HashMap<String, String>();
		moduleTitleMap.put("product.list", params.getProduct_list());
		moduleTitleMap.put("product.group", params.getProduct_group());
		moduleTitleMap.put("product.special", params.getProduct_special());
		moduleTitleMap.put("product.feature", params.getProduct_feature());
		moduleTitleMap.put("company.introduce", params.getCompany_introduce());
		moduleTitleMap.put("company.brief", params.getCompany_brief());
		moduleTitleMap.put("company.member", params.getCompany_member());
		moduleTitleMap.put("service.contact", params.getService_contact());
		moduleTitleMap.put("service.online", params.getService_online());
		moduleTitleMap.put("offer.list", params.getOffer_list());
		moduleTitleMap.put("video.list", params.getVideo_list());
		moduleTitleMap.put("inquiry.online", params.getInquiry_online());
		
		
		result.put("company", company);
		result.put("userList", userList);
		result.put("hasTqId", hasTqId);
		result.put("imgBase", Config.getString("img.base"));
		result.put("moduleTitleMap", moduleTitleMap);
		return result;
	}

	public void setWebsiteDAO(WebsiteDAO websiteDAO) {
		this.websiteDAO = websiteDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setWebsiteModuleDAO(WebsiteModuleDAO websiteModuleDAO) {
		this.websiteModuleDAO = websiteModuleDAO;
	}

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setWebSiteModuleTitleDAO(WebSiteModuleTitleDAO webSiteModuleTitleDAO) {
		this.webSiteModuleTitleDAO = webSiteModuleTitleDAO;
	}

	public void setTalkDAO(TalkDAO talkDAO) {
		this.talkDAO = talkDAO;
	}

	public void setAdminLogDAO(AdminLogDAO adminLogDAO) {
		this.adminLogDAO = adminLogDAO;
	}
}
