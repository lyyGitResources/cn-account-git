/* 
 * Created by baozhimin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.group.GroupDAO;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.util.GroupUtil;
import com.hisupplier.cn.account.util.ImageAlt;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author baozhimin
 */
public class TradeService {
	private static Pattern number_pattern = Pattern.compile("^\\d{1}$");
	private CompanyDAO companyDAO;
	private TradeDAO tradeDAO;
	private UserDAO userDAO;
	private ProductDAO productDAO;
	private GroupDAO groupDAO;

	/**
	 * �������
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 *  state
	 *  company
	 *  userList
	 *  tradeSellCount
	 *  tradeBuyCount
	 * </pre>
	 */
	public Map<String, Object> getTradeList(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		Company company = this.companyDAO.findCompany(comId);
		
		// ����������δͨ����Ʒ�����ǵ�һ�ν����Ʒ�б�ҳ�棬���ÿ������Ϊ���δͨ��
		if (params.isShowReject() && company.getTradeRejectCount() > 0 
				&& params.getLoginUser().isAdmin()) {
			params.setState(CN.STATE_REJECT);
		}

		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
		result.put("listResult", listResult);
		for (Product product : listResult.getList()) {
			if (params.getLoginUser().isAdmin() || userId == product.getUserId()) {
				product.addOperate(new Button("/trade/trade_edit.htm")
					.appendParam("proId", product.getProId())
					.setName("button.editTrade").getLink());
				product.addOperate("<br />");
			}
			product.addOperate(new Button("/trade/trade_add.htm")
				.appendParam("proId", product.getProId())
				.setName("button.addSameTrade").getLink());
		}

		if (params.isAjax()) {
			return result;
		}

		int tradeSellCount = this.tradeDAO.findTradeSellCount(comId);

		// ���ƻ�Ա���Ҵ������ʺ�
		if (company.getMemberType() == CN.GOLD_SITE && company.getUserCount() > 0) {
			result.put("userList", this.userDAO.findUserList(comId));
		}
		result.put("company", company);
		result.put("tradeDatedCount", tradeDAO.findTradeDatedCount(comId));
		result.put("tradeSellCount", tradeSellCount);
		result.put("tradeBuyCount", company.getTradeCount() - tradeSellCount);
		return result;
	}

	/**
	 * �������վ����
	 * @param params
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getTradeRecycleList(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = this.companyDAO.findCompany(comId);

		params.setState(CN.STATE_RECYCLE);
		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
		for (Product product : listResult.getList()) {
			product.addOperate(new Button("/trade/trade_edit.htm")
				.appendParam("proId", product.getProId())
				.setName("button.restore").getLink());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("recycleMax", ProductService.RECYCLE_MAX);
		result.put("tradeDelCount", company.getTradeDelCount());
		return result;
	}

	/**
	 * �������
	 * @param request
	 * @param params
	 * <pre>
	 * 1) loginUser ��¼��Ϣ
	 * 2) proId	����ID�����ͬ������ʱ����
	 * 3) copyProId	��ƷID����ƷתΪ����ʱ����
	 * 4) groupId ��ͨ����ID���ڷ������������ʱ����
	 * </pre>
	 * @return
	 * <pre>
	 * full: �����������
	 * num: ��ӵ�����
	 * product��������Ϣ
	 * formAction
	 * tradeCount ��������
	 * tradeMax �����������
	 * </pre>
	 */
	public Map<String, Object> getTradeAdd(HttpServletRequest request, QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		Company company = this.companyDAO.findCompanyMemberType(comId);

		Map<String, Object> result = new HashMap<String, Object>();
		// ��������
		if (company.getTradeCount() >= company.getTradeMax()) {
			result.put("full", "trade.full");
			result.put("num", company.getTradeCount()+ "");
			return result;
		}
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		// ���ͬ������
		Product product = new Product();
		product.setComId(comId);
		product.setUserId(userId);
		// Ĭ��������Ч��Ϊ 1��
		product.setValidDay(365);
		if (params.getProId() != null && params.getProId()[0] > 0) {
			product = this.tradeDAO.findTrade(comId, params.getProId()[0]);
			product.setCopyProId(0);
			product.setProId(0);
			product.setImgId(0);
			product.setImgPath("");
			product.setProName("");
			product.setDescription("");
			// תΪ����
		} else if (params.getCopyProId() > 0) {
			Product oldProduct = this.productDAO.findProduct(comId, params.getCopyProId());
			product.setCatId(oldProduct.getCatId());
			product.setProName(oldProduct.getProName());
			product.setProType(CN.TRADE_SELL);
			product.setImgId(oldProduct.getImgId());
			product.setImgPath(oldProduct.getImgPath());
			product.setGroupId(oldProduct.getGroupId());
			product.setKeywords(oldProduct.getKeywords());
			product.setBrief(oldProduct.getBrief());
			product.setDescription(oldProduct.getDescription());
		}

		// Ĭ�ϲ�Ʒ���ֶ�
		int groupId = params.getGroupId() > 0 ? params.getGroupId() : product.getGroupId();
		if (groupId > 0) {
			product.setGroupName(
				new GroupUtil(this.groupDAO.findGroupList(comId))
					.getNamePath(groupId, " >> "));
			product.setGroupId(groupId);
		}

		// ����Ŀ¼����
		if (product.getCatId() > 0) {
			product.setCatName(CategoryUtil.getNamePath(product.getCatId(), " >> "));
		}

		// ����ؼ���
		this.parseKeywords(product);
		product.setTradeCount(company.getTradeCount());
		product.setTradeMax(company.getTradeMax());

		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("product", product);
		result.put("image", image);
		return result;
	}

	/**
	 * �����޸�
	 * @param request
	 * @param params
	 * <pre>
	 * 1) loginUser.comId
	 * 2) params loginUser.comId proId[0]
	 * </pre>
	 * @return
	 * <pre>
	 * product
	 * formAction
	 * </pre>
	 */
	public Map<String, Object> getTradeEdit(HttpServletRequest request, QueryParams params, boolean isEdit) {
		Map<String, Object> result = new HashMap<String, Object>();
		int comId = params.getLoginUser().getComId();
		if (params.getProId() == null) {
			return null;
		}
		Company company = this.companyDAO.findCompanyMemberType(comId);
		Product product = this.tradeDAO.findTrade(comId, params.getProId()[0]);
		if (product == null) {
			throw new PageNotFoundException();
		}
		if (isEdit && product.getValidDay() == 0) {
			product.setValidDay(365);
		}
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		// ����ؼ���
		this.parseKeywords(product);
		// ������ͨ����
		if (product.getState() == CN.STATE_REJECT && product.getGroupIdBak() > 0) {
			product.setGroupId(product.getGroupIdBak());
		}
		if (product.getGroupId() > 0) {
			product.setGroupName(
				new GroupUtil(this.groupDAO.findGroupList(comId))
					.getNamePath(product.getGroupId(), " >> "));
		}
		// ����Ŀ¼
		if (product.getCatId() > 0) {
			product.setCatName(CategoryUtil.getNamePath(product.getCatId(), " >> "));
		}

		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("product", product);
		result.put("image", image);
		return result;
	}
	
	public String addTrade(HttpServletResponse response, Product product, 
			LoginUser loginUser, int comId, int userId) {
		Company company = this.companyDAO.findCompanyMemberType(comId);
		product.setComId(comId);
		product.setUserId(userId);
		
		// �����Ƿ����
		String groupIds = ""; // ���������������ϼ����飬��������ʹ��
		if (product.getGroupId() > 0) {
			GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(product.getComId()));
			Group group = groupUtil.get(product.getGroupId());
			if (group == null || group.getChild() > 0) {
				return "product.groupError";
			}
			groupIds = groupUtil.getIdPath(product.getGroupId());
		}

		// �ϴ�����ͼƬ
		Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				if (response != null) {
					image.saveWatermark(response);
				}
				image.setComId(product.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
				if(!number_pattern.matcher(map.get("imgType")).matches() 
						|| !"3".equals(map.get("imgType"))){
					map.put("imgType", "3");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgId(img.getImgId());
				product.setImgPath(img.getImgPath());
			}
		}
		// TODO ��֤ ͼƬ��ַ����
		if (StringUtil.length(product.getImgPath()) >= 60) {
			return "product.uploadFail";
		}

		// ��Ӳ�Ʒ
		this.getKeywords(product);
		product.setState(CN.STATE_WAIT);
		String currentTime = new DateUtil().getDateTime();
		product.setCreateTime(currentTime);
		product.setModifyTime(currentTime);
		product.setUpdateTime(currentTime);
		int proId = this.tradeDAO.addTrade(product);
		if (proId <= 0) {
			return "operateFail";
		}
		product.setProId(proId);
		this.tradeDAO.addTradeExtra(product);

		// ����CompanyExtra����
		UpdateMap updateMap = null;
		updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("tradeCount", "+", 1);
		updateMap.addWhere("comId", comId);
		productDAO.update(updateMap);

		// ����Groups����
		if (product.getGroupId() > 0) {
			updateMap = new UpdateMap("Groups");
			updateMap.addField("tradeCount", "+", 1);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", groupIds, "in");
			tradeDAO.update(updateMap);
		}

		if (loginUser != null) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.ADD, 
				"������顪��" + product.getProName(), loginUser);
			tradeDAO.addUserLog(userLog);
		}
		return "addSuccess";
	}

	/**
	 * ��������ύ
	 * @param response
	 * @param product
	 * @param loginUser
	 * @return
	 * <pre>
	 * product.groupError: ��ʾ�������
	 * product.uploadFail: ��ʾ�ļ���ͼƬ�ϴ�ʧ��
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String addTrade(HttpServletResponse response, Product product, LoginUser loginUser) {
		int comId = loginUser.getComId();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		product.setComId(comId);
		product.setUserId(loginUser.getUserId());
		
		// �����Ƿ����
		String groupIds = ""; // ���������������ϼ����飬��������ʹ��
		if (product.getGroupId() > 0) {
			GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(product.getComId()));
			Group group = groupUtil.get(product.getGroupId());
			if (group == null || group.getChild() > 0) {
				return "product.groupError";
			}
			groupIds = groupUtil.getIdPath(product.getGroupId());
		}

		// �ϴ�����ͼƬ
		Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				if (response != null) {
					image.saveWatermark(response);
				}
				image.setComId(product.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
				if(!number_pattern.matcher(map.get("imgType")).matches() 
						|| !"3".equals(map.get("imgType"))){
					map.put("imgType", "3");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgId(img.getImgId());
				product.setImgPath(img.getImgPath());
			}
		}
		// TODO ��֤ ͼƬ��ַ����
		if (StringUtil.length(product.getImgPath()) >= 60) {
			return "product.uploadFail";
		}

		// ��Ӳ�Ʒ
		this.getKeywords(product);
		product.setState(CN.STATE_WAIT);
		String currentTime = new DateUtil().getDateTime();
		product.setCreateTime(currentTime);
		product.setModifyTime(currentTime);
		product.setUpdateTime(currentTime);
		product.setDescription(ImageAlt.path(product.getDescription(), product.getProName()));
		int proId = this.tradeDAO.addTrade(product);
		if (proId <= 0) {
			return "operateFail";
		}
		product.setProId(proId);
		this.tradeDAO.addTradeExtra(product);

		// ����CompanyExtra����
		UpdateMap updateMap = null;
		updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("tradeCount", "+", 1);
		updateMap.addWhere("comId", comId);
		productDAO.update(updateMap);

		// ����Groups����
		if (product.getGroupId() > 0) {
			updateMap = new UpdateMap("Groups");
			updateMap.addField("tradeCount", "+", 1);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", groupIds, "in");
			tradeDAO.update(updateMap);
		}

		if (loginUser != null) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.ADD, 
				"������顪��" + product.getProName(), loginUser);
			tradeDAO.addUserLog(userLog);
		}
		return "addSuccess";
	}

	/**
	 * ��������ɾ��
	 * @param params proId[]
	 * @return
	 * <pre>
	 * trade.recycleFull: ����վ����
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String deleteTrade(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		if (params.getProId() == null) {
			return "operateFail";
		}
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company.getTradeDelCount() + params.getProId().length >= ProductService.RECYCLE_MAX) {
			return "trade.recycleFull";
		}

		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);

		UpdateMap updateMap = new UpdateMap("Trade");
		updateMap.addField("state", CN.STATE_RECYCLE);
		updateMap.addField("copyProId", 0);
		updateMap.addField("catId", 0);
		updateMap.addField("groupId", 0);
		updateMap.addField("groupIdBak", 0);
		updateMap.addField("keywords", "");
		String currentTime = new DateUtil().getDateTime();
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", StringUtil.toString(params.getProId(), ","), "in");
		int updateNum = this.tradeDAO.update(updateMap);
		if (updateNum <= 0) {
			return "operateFail";
		}

		// ��ѯɾ�������δͨ��������
		List<UpdateMap> updateMapList = new ArrayList<UpdateMap>();
		int tradeRejectNum = 0;
		for (Product product : listResult.getList()) {
			if (product.getState() == CN.STATE_REJECT) {
				tradeRejectNum++;
			}
			if (product.getGroupId() > 0 && product.getState() > CN.STATE_REJECT) {
				updateMap = new UpdateMap("Groups");
				updateMap.addField("tradeCount", "-", 1);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("groupId", new GroupUtil(this.groupDAO.findGroupList(comId))
					.getIdPath(product.getGroupId()), "in");
				updateMapList.add(updateMap);
			}
		}
		updateMap = new UpdateMap("CompanyExtra");
		if (tradeRejectNum > 0) {
			updateMap.addField("tradeRejectCount", "-", tradeRejectNum);
		}
		updateMap.addField("tradeCount", "-", updateNum);
		updateMap.addField("tradeDelCount", "+", updateNum);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("tradeCount", 0, ">");
		updateMapList.add(updateMap);
		this.tradeDAO.update(updateMapList); 

		UserLog userLog = null;
		for (Product product : listResult.getList()) {
			userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.RECYCLE, 
					"�������顪��" + product.getProName(), params.getLoginUser());
			tradeDAO.addUserLog(userLog);
		}

		return "deleteSuccess";
	}

	/**
	 * �����޸��ύ
	 * <pre>
	 * @param response
	 * @param product 
	 * PS:comId,userId�ڱ���ȡ��
	 * @param loginUser 
	 * </pre>
	 * @return
	 * <pre>
	 * product.groupError: ��ʾ�������
	 * product.uploadFail: ��ʾ�ļ���ͼƬ�ϴ�ʧ��
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateTrade(HttpServletResponse response, Product product, LoginUser loginUser) {
		int comId = loginUser.getComId();
		// ��֤����
		boolean isAddGroup = false;
		GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(comId));

		Group group = null;
		Group oldGroup = null;
		
		Product pro = tradeDAO.findTrade(comId, product.getProId());
		product.setState(pro.getState());

		if (product.getOldGroupId() > 0) {
			oldGroup = groupUtil.get(product.getOldGroupId());
			if (oldGroup == null || oldGroup.getChild() > 0) {
				return "product.groupError";
			}
		}

		if (product.getGroupId() > 0) {
			group = groupUtil.get(product.getGroupId());
			if (group == null || group.getChild() > 0) {
				return "product.groupError";
			}
			// ���δͨ���ͻ���վ״̬�����ݿ�ԭ�ȵ�groupIdΪ0��ֻҪ���ڷ���ID����������ӵ�
			if (product.getState() == CN.STATE_REJECT || product.getState() == CN.STATE_RECYCLE) {
				isAddGroup = true;
			}
		}
		Company company = this.companyDAO.findCompanyMemberType(comId);
		
		// �ϴ�����ͼƬ
		Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() < company.getImgMax()) {
				Image image = product.getImage();
				image.saveWatermark(response);
				image.setComId(comId);
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
				if(!number_pattern.matcher(map.get("imgType")).matches() 
						|| !"3".equals(map.get("imgType"))){
					map.put("imgType", "3");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgPath(img.getImgPath());
				product.setImgId(img.getImgId());
			}
		}
		// TODO ��֤ ͼƬ��ַ����
		if (StringUtil.length(product.getImgPath()) >= 60) {
			return "product.uploadFail";
		}

		int oldState = product.getState(); //ִ�и���ǰ��״̬
		int newState = 0;

		// ��������
		String currentTime = new DateUtil().getDateTime();
		this.getKeywords(product);
		product.setCopyProId(0);
		product.setModifyTime(currentTime);
		product.setUpdateTime(currentTime);
		//�ƽ��Ա���ͨ�����޸ĺ� ��Ϊ"�������"
		if (loginUser.getMemberType() == 2) {
			if (oldState == CN.STATE_PASS) {
				newState = CN.STATE_REJECT_WAIT; // �������
			} else if (oldState == CN.STATE_REJECT_WAIT) { // ������� �޸ĺ��� 
				newState = CN.STATE_REJECT_WAIT;
			} else {
				newState = CN.STATE_WAIT;
			}
		} else {
			newState= CN.STATE_WAIT;
		}
		product.setState(newState);
		
		product.setGroupIdBak(0);
		product.setComId(comId);
		product.setDescription(ImageAlt.path(product.getDescription(), product.getProName()));
		if (this.tradeDAO.updateTrade(product) <= 0) {
			return "operateFail";
		}

		UpdateMap updateMap = new UpdateMap("TradeExtra");
		updateMap.addField("description", product.getDescription());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", product.getProId());
		tradeDAO.update(updateMap);

		// ����ı䡣�¾ɷ���ID��ͬ���жϷ���ı�ֻ��״̬Ϊ�ȴ���˺����ͨ����Ч
		if (product.getGroupId() != product.getOldGroupId() 
				&& (oldState == CN.STATE_REJECT_WAIT || oldState == CN.STATE_PASS)) {
			if (product.getGroupId() > 0) {
				isAddGroup = true;
			}
			if (product.getOldGroupId() > 0) {
				updateMap = new UpdateMap("Groups");
				updateMap.addField("tradeCount", "-", 1);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("groupId", groupUtil.getIdPath(product.getOldGroupId()), "in");
				tradeDAO.update(updateMap);
			}
		}

		if (isAddGroup) {
			updateMap = new UpdateMap("Groups");
			updateMap.addField("tradeCount", "+", 1);
			updateMap.addWhere("comId", product.getComId());
			updateMap.addWhere("groupId", groupUtil.getIdPath(product.getGroupId()), "in");
			productDAO.update(updateMap);
		}

		// ����CompanyExtra����
		if (oldState == CN.STATE_RECYCLE || oldState == CN.STATE_REJECT) {
			updateMap = new UpdateMap("CompanyExtra");
			if (oldState == CN.STATE_RECYCLE) {
				updateMap.addField("tradeCount", "+", 1);
				updateMap.addField("tradeDelCount", "-", 1);
			}
			if (oldState == CN.STATE_REJECT) {
				updateMap.addField("tradeRejectCount", "-", 1);
			}
			updateMap.addWhere("comId", comId);
			productDAO.update(updateMap);
		}

		UserLog userLog = null;
		if (oldState == CN.STATE_RECYCLE) {
			userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.REUSE, 
					"��ԭ���顪��" + product.getProName(), loginUser);
		} else {
			userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.MODIFY, 
					"�޸����顪��" + product.getProName(), loginUser);
		}
		tradeDAO.addUserLog(userLog);
		
		if (oldState == 20 || oldState == 14) { //���ͨ������Ϣ����������޸��ύ����ֱ�Ӱ�ԭ����ʾ���޸ĳɹ���
			return "oldSuccess";
		}else {
			return "tradeSuccess"; //�µ��ύ�ɹ�ҳ��
		}
	}

	/**
	 * �������
	 * @param params loginUser.comId proId[] userId
	 * @return
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateTradeUser(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getUserId();
		UpdateMap updateMap = new UpdateMap("Trade");
		updateMap.addField("userId", userId);
		updateMap.addField("updateTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", StringUtil.toString(params.getProId(), ","), "in");
		if (this.tradeDAO.update(updateMap) > 0) {
			ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
			UserLog userLog = null;
			User user = this.userDAO.findUser(userId, comId);
			for (Product product : listResult.getList()) {
				userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.MODIFY, "" +
					"�������顰" + product.getProName() + "�������ʺ�" + 
					(user == null ? params.getUserId() : user.getEmail()), 
					params.getLoginUser());
				productDAO.addUserLog(userLog);
			}
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * �������
	 * @param params loginUser.comId proId[]
	 * @return
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateTradeRepost(QueryParams params) {
		if(params.getProId() == null){
			return "operateFail";
		}
		int comId = params.getLoginUser().getComId();
		String currentTime = new DateUtil().getDateTime();
		UpdateMap oldDate = new UpdateMap("Trade");
		oldDate.addField("modifyTime", currentTime);
		oldDate.addField("updateTime", currentTime);
		oldDate.addField("validDay", 365);
		oldDate.addWhere("proId", StringUtil.toString(params.getProId(), ","), "in");
		oldDate.addWhere("validDay", 0);
		oldDate.addWhere("comId", comId);
		
		UpdateMap updateMap = new UpdateMap("Trade");
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", StringUtil.toString(params.getProId(), ","), "in");
		
		if (this.tradeDAO.update(new UpdateMap[] { oldDate, updateMap }) > 0) {
			ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
			UserLog userLog = null;
			for (Product product : listResult.getList()) {
				userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.MODIFY, 
						"�������顪��" + product.getProName(), params.getLoginUser());
				productDAO.addUserLog(userLog);
			}
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * �����������
	 * @param params loginUser.comId proId[]
	 * @return
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateTradeRepostAll(QueryParams params) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		UpdateMap oldDate = new UpdateMap("Trade");
		oldDate.addField("modifyTime", currentTime);
		oldDate.addField("updateTime", currentTime);
		oldDate.addField("validDay", 365);
		oldDate.addWhere("state", 9,">");
		oldDate.addWhere("validDay", 0);
		oldDate.addWhere("comId", comId);
		
		UpdateMap updateMap = new UpdateMap("Trade");
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("state", 9,">");
		
		if (this.tradeDAO.update(new UpdateMap[] { oldDate, updateMap }) > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.MODIFY, 
					"������������", params.getLoginUser());
			productDAO.addUserLog(userLog);
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}
	
	/**
	 * ����վɾ������
	 * @param params loginUser.comId proId[]
	 * @param deleteAll �Ƿ���� ��ֹ������������ĸ��´���
	 * @return
	 * <pre>
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String updateRecycleDelete(QueryParams params, boolean deleteAll) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		UpdateMap updateMap = new UpdateMap("Trade");
		updateMap.addField("state", CN.STATE_DELETE);
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addField("imgId", 0);
		updateMap.addField("imgPath", "");
		updateMap.addWhere("state", CN.STATE_RECYCLE);
		updateMap.addWhere("comId", comId);
		if (!deleteAll) {
			updateMap.addWhere("proId", StringUtil.toString(params.getProId(), ","), "in");
		}
		int updateNum = this.tradeDAO.update(updateMap);
		if (updateNum <= 0) {
			return "operateFail";
		}

		UserLog userLog = null;
		updateMap = new UpdateMap("CompanyExtra");
		if (deleteAll) {
			updateMap.addField("tradeDelCount", 0);
			userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.DELETE, 
					"��ջ���վ", params.getLoginUser());
		} else {
			updateMap.addField("tradeDelCount", "-", updateNum);
			userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.DELETE, 
					"����ɾ������վ����", params.getLoginUser());
		}
		updateMap.addWhere("comId", comId);
		tradeDAO.update(updateMap);
		tradeDAO.addUserLog(userLog);
		return "deleteSuccess";
	}

	/**
	 * ��ջ���վ����
	 * @param params loginUser.comId
	 * @return
	 * <pre>
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String updateRecycleEmpty(QueryParams params) {
		return this.updateRecycleDelete(params, true);
	}

	private void getKeywords(Product product) {
		if (StringUtil.isNotBlank(product.getKeywords())) {
			return;
		}

		if (StringUtil.isNotBlank(product.getKeyword1())) {
			product.setKeywords(StringUtil.replace(product.getKeyword1(), ",", ""));
		}
	}
	
	public Map<String,Object> tradeSuccess(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Product product = this.tradeDAO.findTrade(params.getLoginUser().getComId(), params.getProId()[0]);
		if (product == null) {
			throw new PageNotFoundException();
		}
		if (params.getResultType() == 0) {
			result.put("addMessage", "��ϲ��������Ϣ���ύ�ɹ���");
		}else {
			result.put("addMessage", "��ϲ��������Ϣ���޸ĳɹ���");
		}
		return result;
	}

	/**
	 * �ֽ�ؼ���
	 * @param product
	 * @return
	 */
	private void parseKeywords(Product product) {
		String[] keywords = StringUtil.toArray(product.getKeywords(), ",");
		int length = keywords.length;
		product.setKeyword1(length > 0 ? keywords[0] : "");
		product.setKeyword2(length > 1 ? keywords[1] : "");
		product.setKeyword3(length > 2 ? keywords[2] : "");
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

}