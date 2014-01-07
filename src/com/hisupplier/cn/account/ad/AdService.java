package com.hisupplier.cn.account.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Top;
import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

public class AdService {

	private UserDAO userDAO;
	private CompanyDAO companyDAO;
	private AdDAO adDAO;
 
	/**
	 * 广告申请列表
	 * @param params
	 * @return
	 */
	public Map<String, Object> getAdOrderList(QueryParams params) {
		ListResult<AdOrder> listResult = adDAO.findAdOrderList(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * Topsite服务列表
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTopList(QueryParams params) {
		ListResult<Top> listResult = adDAO.findTopList(params);

		//把topId拼成topIds
		StringBuffer topIds = new StringBuffer();
		for (Top top : listResult.getList()) {
			topIds.append(top.getTopId()).append(",");
		}

		//查询并注入关键词
		if (topIds.length() > 0) {
			List<Top> topKeywordList = adDAO.findTopKeywordList(topIds.deleteCharAt(topIds.length() - 1).toString());
			for (Top top : listResult.getList()) {
				StringBuffer keywords = new StringBuffer();
				for (Top topKeyword : topKeywordList) {
					if (top.getTopId() == topKeyword.getTopId()) {
						keywords.append(topKeyword.getKeyword()).append(",");
					}
				}
				if (keywords.length() > 0) {
					keywords.deleteCharAt(keywords.length() - 1);
				}
				top.setKeyword(keywords.toString());
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * Topsite订购列表
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTopOrderList(QueryParams params) {
		ListResult<TopOrder> listResult = adDAO.findTopOrderList(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * 返回会员升级页面
	 * @param params
	 * @return
	 */
	public Map<String, Object> getUpgrade(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();

		Company company = companyDAO.findCompany(comId);
		User user = userDAO.findUser(userId, comId);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("company", company);
		result.put("user", user);
		return result;
	}

	/**
	 * 返回广告申请页面
	 * @param params
	 * @return
	 */
	public Map<String, Object> getAdOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();

		Company company = companyDAO.findCompany(comId);
		User user = userDAO.findUser(userId, comId);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("company", company);
		result.put("user", user);
		return result;
	}

	/**
	 * 返回Topsite服务修改页面
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTop(HttpServletRequest request, QueryParams params) {
		Top top = adDAO.findTop(params.getLoginUser().getComId(), params.getTopId());
		Company company = companyDAO.findCompany(params.getLoginUser().getComId());
		if (top != null) {
			top.setMemberId(company.getMemberId());
		}
		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("image", image);
		result.put("top", top);
		return result;
	}

	/**
	 * 返回Topsite订购页面
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTopOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();

		Company company = companyDAO.findCompany(comId);
		User user = userDAO.findUser(userId, comId);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("company", company);
		result.put("user", user);
		return result;
	}

	/**
	 * 新增会员升级
	 * @param upgradeMail
	 * @return addSuccess or operateFail
	 */
	public String addUpgrade(UpgradeMail upgradeMail) {
		upgradeMail.setCreateTime(new DateUtil().getDateTime());
		int num = adDAO.addUpgradeMail(upgradeMail);
		if (num > 0) {
			return "addSuccess";
		}
		return "operateFail";
	}

	/**
	 * 新增广告服务
	 * @param adOrder
	 * @return addSuccess or operateFail
	 */
	public String addAdOrder(AdOrder adOrder) {
		adOrder.setCreateTime(new DateUtil().getDateTime());
		int num = adDAO.addAdOrder(adOrder);
		if (num > 0) {
			return "addSuccess";
		}
		return "operateFail";
	}

	/**
	 * 新增Topsite订购
	 * @param topOrder
	 * @return addSuccess or operateFail
	 */
	public String addTopOrder(TopOrder topOrder) {
		topOrder.setCreateTime(new DateUtil().getDateTime());
		int num = adDAO.addTopOrder(topOrder);
		if (num > 0) {
			return "addSuccess";
		}
		return "operateFail";
	}

	/**
	 * 更新Topsite服务修改
	 * @param top
	 * @return
	 */
	public String updateTop(HttpServletResponse response, Top top) {
		// 上传产品图片
		if (StringUtil.isNotEmpty(top.getImgPath())) {
			Map<String, String> map = UploadUtil.getImgParam(top.getImgPath());
			if (Boolean.parseBoolean(map.get("isUpload"))) {
				Image image = top.getImage();
				image.saveWatermark(response);
				image.setComId(top.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "top.uploadFail";
				}
				top.setImgPath(img.getImgPath());
			}
		}
		
		UpdateMap Map = new UpdateMap("Top");
		Map.addField("imgPath", top.getImgPath());
		Map.addField("proName", top.getProName());
		Map.addField("brief", top.getBrief());
		if(StringUtil.isNotBlank(top.getLink())){
			Map.addField("link", top.getLink());
		}else{
			Map.addField("link", top.getLink2());
		}

		Map.addWhere("comId", top.getComId());
		Map.addWhere("topId", top.getTopId());
		int num = adDAO.update(Map);
		if (num > 0) {
			return "editSuccess";
		}
		return "operateFail";
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setAdDAO(AdDAO adDAO) {
		this.adDAO = adDAO;
	}
}
