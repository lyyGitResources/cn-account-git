/* 
 * Created by sunhailin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.product.TradeDAO;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.SpecialProduct;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.TextUtil;

/**
 * @author sunhailin
 *
 */
public class SpecialGroupService {
	private SpecialGroupDAO specialGroupDAO;
	private TradeDAO tradeDAO;
	
	/**
	 * 查询分组管理列表
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 *   groupList<Group>
	 *   noGroupProductCount
	 * </pre>
	 */
	public Map<String, Object> getGroupList(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.specialGroupDAO.findGroupList(comId);
		params.setGroupId(0);
		int noGroupProductCount = this.specialGroupDAO.findNoGroupProductCount(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		result.put("noGroupProductCount", noGroupProductCount);
		return result;
	}

	/**
	 * 查询分组选择列表
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 *   groupList<Group>
	 * </pre>
	 */
	public Map<String, Object> getSelectGroupList(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.specialGroupDAO.findGroupList(comId);
		for (Group g : groupList) {
			if (g.getState() > CN.STATE_REJECT) {
				g.setOperate(new StringBuffer().append("javascript:selectSpecialGroup(" + g.getGroupId() + ",'" + g.getGroupName() + "')"));
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		return result;
	}

	/**
	 * 查询组内产品列表
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * queryBy
	 * queryText
	 * groupId
	 * pageNo
	 * pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   group
	 *   listResult<Group>
	 * </pre>
	 */
	public Map<String, Object> getGroupProductList(QueryParams params) {
		Group group = this.specialGroupDAO.findGroup(params.getLoginUser().getComId(), params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		ListResult<Product> listResult = this.specialGroupDAO.findGroupProductList(params);
		this.getProductLink(listResult, params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("group", group);
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * 查询未分组产品列表
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * queryBy
	 * queryText
	 * pageNo
	 * pageSize
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   listResult<Group>
	 * </pre>
	 */
	public Map<String, Object> getNoGroupProductList(QueryParams params) {
		params.setGroupId(0);
		ListResult<Product> listResult = this.specialGroupDAO.findGroupProductList(params);
		this.getProductLink(listResult, params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * 取得添加特殊分组信息
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 *   group
	 *   fromAction
	 * </pre>
	 */
	public Map<String, Object> getSpecailGroupAdd(QueryParams params) {
		Group group = new Group();
		group.setComId(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("group", group);
		result.put("formAction", "/specialGroup/group_add_submit.htm");
		return result;
	}

	/**
	 * 取得修改特殊分组信息
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   group
	 *   fromAction
	 * </pre>
	 */
	public Map<String, Object> getSpecailGroupEdit(QueryParams params) {
		Group group = this.specialGroupDAO.findGroup(params.getLoginUser().getComId(), params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("group", group);
		result.put("formAction", "/specialGroup/group_edit_submit.htm");
		return result;
	}

	/**
	 * 添加特殊分组
	 * @param group
	 * @param loginUser
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   operateFail
	 *   addSuccess
	 * </pre>
	 */
	public String addGroup(Group group, LoginUser loginUser) {
		String currentTime = new DateUtil().getDateTime();
		group.setComId(loginUser.getComId());
		group.setProductCount(0);
		group.setState(CN.STATE_WAIT);
		group.setCreateTime(currentTime);
		group.setModifyTime(currentTime);
		int maxListOrder = this.specialGroupDAO.findMaxListOrder(group.getComId());
		group.setListOrder(maxListOrder + 1);
		int num = this.specialGroupDAO.addGroup(group);
		if (num <= 0) {
			return "operateFail";
		}
		
		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.ADD, 
				"添加特殊分组——" + group.getGroupName(), loginUser);
		specialGroupDAO.addUserLog(userLog);
		return "addSuccess";
	}

	/**
	 * 修改特殊分组
	 * @param group
	 * @param loginUser
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   operateFail
	 *   editSuccess
	 * </pre>
	 */
	public String updateGroup(Group group, LoginUser loginUser) {
		UpdateMap specialGroup = new UpdateMap("SpecialGroup");
		specialGroup.addField("groupName", group.getGroupName());
		specialGroup.addField("state", CN.STATE_WAIT);
		specialGroup.addField("modifyTime", new DateUtil().getDateTime());
		specialGroup.addWhere("groupId", group.getGroupId());
		specialGroup.addWhere("comId", loginUser.getComId());
		int num = this.specialGroupDAO.update(specialGroup);
		if (num <= 0) {
			return "operateFail";
		}
		
		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, 
				"修改特殊分组——" + group.getGroupName(), loginUser);
		specialGroupDAO.addUserLog(userLog);
		return "editSuccess";
	}
	
	/**
	 * 更新分组排序
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupIds
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateGroupOrder(QueryParams params){
		int[] groupIds = params.getGroupIds();
		int listOrder = 1;
		if (groupIds.length > 0) {
			List<UpdateMap> list = new ArrayList<UpdateMap>();
			for (int i = 0; i < groupIds.length; i++) {
				UpdateMap updateMap = new UpdateMap("SpecialGroup");
				updateMap.addField("listOrder", listOrder);
				updateMap.addWhere("comId", params.getLoginUser().getComId());
				updateMap.addWhere("groupId", groupIds[i]);
				list.add(i, updateMap);
				listOrder++;
			}
			//更新排序值
			int num = this.specialGroupDAO.update(list);
			if (num <=0) {
				return "operateFail";
			}
		} else {
			return "operateFail";
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, "修改特殊分组排序", params.getLoginUser());
		specialGroupDAO.addUserLog(userLog);
		return "orderSuccess";
	}

	/**
	 * 删除特殊分组
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   operateFail
	 *   deleteSuccess
	 * </pre>
	 */
	public String deleteGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int groupId = params.getGroupId();

		Group group = this.specialGroupDAO.findGroup(comId, groupId);
		if(group == null){
			throw new PageNotFoundException();
		}
		
		UpdateMap specialGroup = new UpdateMap("SpecialGroup");
		specialGroup.addField("listOrder", 0);
		specialGroup.addField("productCount", 0);
		specialGroup.addField("state", CN.STATE_DELETE);
		specialGroup.addField("modifyTime", new DateUtil().getDateTime());
		specialGroup.addWhere("groupId", groupId);
		specialGroup.addWhere("comId", comId);
		int num = this.specialGroupDAO.update(specialGroup);
		if (num <= 0) {
			return "operateFail";
		}
		//删除关系
		this.specialGroupDAO.deleteSpecialProduct(comId, null, groupId + "");

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.DELETE, "删除特殊分组——" + group.getGroupName(), params.getLoginUser());
		specialGroupDAO.addUserLog(userLog);
		return "deleteSuccess";
	}

	/**
	 * 添加产品到特殊分组
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * int[] proId
	 * groupId
	 * groupName
	 * </pre>
	 * @return
	 * <pre>
	 *   addSuccess
	 *   repeat
	 * </pre>
	 */
	public String selectProductGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<SpecialProduct> specialProductList = 
				this.specialGroupDAO.findSpecialProductList(comId, params.getGroupId());
		
		List<Integer> proIdList = new ArrayList<Integer>();	
		for(int i=0;i<params.getProId().length;i++){
			boolean contain = false;
			for(SpecialProduct specialProduct :specialProductList){
				if(params.getProId()[i] == specialProduct.getProId()){
					contain = true;
					break;
				}
			}
			if(!contain){
				proIdList.add(params.getProId()[i]);
			}
		}
		if(proIdList.size() <=0){
			return "repeat";
		}
		int[] proId = new int[proIdList.size()];
		for(int i=0;i<proIdList.size();i++){
			proId[i] = proIdList.get(i);
		}

		this.specialGroupDAO.addSpecialProduct(comId, proId, new int[] { params.getGroupId() });

		UpdateMap specialGroup = new UpdateMap("SpecialGroup");
		specialGroup.addField("productCount", "+", proId.length);
		specialGroup.addWhere("groupId", params.getGroupId());
		specialGroup.addWhere("comId", comId);
		this.specialGroupDAO.update(specialGroup);
		return "addSuccess";
	}

	/**
	 * 添加产品到特殊分组
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * int[] proId
	 * groupId
	 * groupName
	 * </pre>
	 * @return
	 * <pre>
	 *   deleteSuccess
	 * </pre>
	 */
	public String removeFromProducGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();

		this.specialGroupDAO.deleteSpecialProduct(comId, 
				params.getProId(), params.getGroupId());

		UpdateMap specialGroup = new UpdateMap("SpecialGroup");
		specialGroup.addField("productCount", "-", params.getProId().length);
		specialGroup.addWhere("groupId", params.getGroupId());
		specialGroup.addWhere("comId", comId);
		this.specialGroupDAO.update(specialGroup);

		return "deleteSuccess";
	}
	
	/**
	 * 查询名称是否重复
	 * @param group
	 *<pre> 
	 *	groupName 
	 *  comId
	 *  groupId
	 * </pre>
	 * @return
	 */
	public boolean checkSpecialGroupRepeat(Group group) {
		return this.specialGroupDAO.findSpecialGroupCountByName(group) > 0 
				? true : false;
	}

	private void getProductLink(ListResult<Product> listResult, QueryParams params){
		Map<Integer, Integer> copyProIdMap = this.tradeDAO.findTradeCopyProId(params.getLoginUser().getComId());
		
		for (Product product : listResult.getList()) {
			if(params.getLoginUser().isAdmin() || params.getLoginUser().getUserId() == product.getUserId()){
				product.addOperate(new Button("/product/product_edit.htm").appendParam("proId", product.getProId()).setName("button.editProduct").getLink());
				product.addOperate("<br />");
			}
			
			if (!copyProIdMap.containsKey(product.getProId())) {
				product.addOperate(new Button("/trade/trade_add.htm").appendParam("copyProId", product.getProId()).setName("button.toTrade").getLink());
				product.addOperate("<br />");
			} else {
				product.addOperate("<a onclick='isModifyTrade(" + copyProIdMap.get(product.getProId()) + ")' href='javascript:void(0)'>" + TextUtil.getText("button.toTrade", "zh") + "</a>");
				product.addOperate("<br />");
			}
			product.addOperate(new Button("/product/product_add.htm").appendParam("proId", product.getProId()).setName("button.addSameProduct").getLink());
		}
	}
	
	public void setSpecialGroupDAO(SpecialGroupDAO specialGroupDAO) {
		this.specialGroupDAO = specialGroupDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

}
