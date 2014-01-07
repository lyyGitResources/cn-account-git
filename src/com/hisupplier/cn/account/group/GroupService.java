/* 
 * Created by sunhailin at Nov 13, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.product.ProductDAO;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.product.TradeDAO;
import com.hisupplier.cn.account.util.GroupUtil;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.TextUtil;

/**
 * @author sunhailin
 *
 */
public class GroupService {
	private GroupDAO groupDAO;
	private CompanyDAO companyDAO;
	private ProductDAO productDAO;
	private TradeDAO tradeDAO;
	private UserDAO userDAO;

	/**
	 * ������ͨ��������б��Լ�δ����Ĳ�Ʒ����������
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 *   groupList
	 *   noGroupProductCount
	 *   noGroupTradeCount
	 * </pre>
	 */
	public Map<String, Object> getGroupList(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = this.companyDAO.findCompany(comId);
		//����comId��ѯ���з���
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		for (Group group : groupList) {
			group.setOperate(new StringBuffer().append("javascript:showOptions(" + group.getGroupId() + ");"));
			StringBuffer href = new StringBuffer();
			if (group.getProductCount() > 0 && group.getChild() <= 0) {
				href.append(this.getLink("/group/group_product_list.htm?groupId=" + group.getGroupId(), TextUtil.getText("button.managerProduct", "zh"), false));
			}
			if (group.getTradeCount() > 0 && group.getChild() <= 0) {
				href.append(this.getLink("/group/group_trade_list.htm?groupId=" + group.getGroupId(), TextUtil.getText("button.managerTrade", "zh"), false));
			}
			if (group.getChild() <= 0) {
				href.append(this.getLink("/product/product_add.htm?groupId=" + group.getGroupId(), TextUtil.getText("button.addProduct", "zh"), false));
				href.append(this.getLink("/trade/trade_add.htm?groupId=" + group.getGroupId(), TextUtil.getText("button.addTrade", "zh"), false));
				href.append(this.getLink("deleteGroup('" + group.getGroupId() + "');", TextUtil.getText("button.delete", "zh"), true));
			}
			href.append(this.getLink("showFormDialog('/group/group_edit.do?groupId=" + group.getGroupId() + "','" + TextUtil.getText(this.getClass(), "group.modifyGroupTitle", "zh") + "');", TextUtil
					.getText("button.edit", "zh"), true));
			if (group.getDepth() == 1 || group.getDepth() == 2) {
				href.append(this.getLink("showThirdChoose('/group/group_add.do?parentId=" + group.getGroupId() + "','" + TextUtil.getText(this.getClass(), "group.createChildGroupTitle", "zh")
						+ "',depth=" + group.getDepth() + ");", TextUtil.getText("button.addChildGroup", "zh"), true));
			}
			if (group.getChild() > 1) {
				href.append(this.getLink("/group/group_order.htm?parentId=" + group.getGroupId(), TextUtil.getText("button.orderChildGroup", "zh"), false));
			}

			if (group.getProductCount() > 0 || group.getTradeCount() > 0) {
				if (group.isShowNewIco()) {
					href.append("<span id=\"groupNewIco" + group.getGroupId() + "\">").append(
							this.getLink("showNewIco('" + group.getGroupId() + "',false);", TextUtil.getText(this.getClass(), "group.new", "zh"), true)).append("</span>");
				} else {
					href.append("<span id=\"groupNewIco" + group.getGroupId() + "\">").append(
							this.getLink("showNewIco('" + group.getGroupId() + "',true);", TextUtil.getText(this.getClass(), "group.new", "zh"), true)).append("</span>");
				}
			}
			if(group.getProductCount() > 1 && group.getChild()<1){
				href.append(this.getLink("/product/product_order.htm?showReject=true&groupId=" + group.getGroupId(), "<br/>���ڲ�Ʒ����", false));
			}

			group.setOption(this.getOptionModel(group.getGroupId(), group.getGroupName(), href));
		}
		int productCount = 0;
		int tradeCount = 0;

		if (company != null && groupList.size() >= 0) {
			productCount = company.getProductCount();
			tradeCount = company.getTradeCount();
			for (Group group : groupList) {
				//����Ǹ����飬�ͼ�ȥ�������е�����
				if (group.getParentId() == 0) {
					productCount -= group.getProductCount();
					tradeCount -= group.getTradeCount();
				}
			}
		}

		//���Other��
		Group other = new Group();
		other.setGroupId(-2);
		other.setParentId(0);
		other.setGroupName("������");
		other.setProductCount(productCount);
		other.setTradeCount(tradeCount);
		other.setOperate(new StringBuffer().append("javascript:showOptions(-2);"));
		StringBuffer otherHref = new StringBuffer();
		if (productCount > 0) {
			otherHref.append(this.getLink("/group/no_group_product_list.htm?groupId=0", TextUtil.getText("button.managerProduct", "zh"), false));
		}
		if (tradeCount > 0) {
			otherHref.append(this.getLink("/group/no_group_trade_list.htm?groupId=0", TextUtil.getText("button.managerTrade", "zh"), false));
		}
		otherHref.append(this.getLink("/product/product_add.htm?groupId=0", TextUtil.getText("button.addProduct", "zh"), false));
		otherHref.append(this.getLink("/trade/trade_add.htm?groupId=0", TextUtil.getText("button.addTrade", "zh"), false));
		other.setOption(this.getOptionModel(other.getGroupId(), other.getGroupName(), otherHref));
		groupList.add(other);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		//result.put("noGroupProductCount", productCount);
		//result.put("noGroupTradeCount", tradeCount);
		return result;
	}

	/**
	 * ������ͨ����ѡ���б�
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 *   groupList
	 * </pre>
	 */
	public Map<String, Object> getSelectGroupList(QueryParams params) {
		List<Group> groupList = this.groupDAO.findGroupList(params.getLoginUser().getComId(), params.getFeature());
		GroupUtil util = new GroupUtil(groupList);
		if (params.getFeature() == 1000) {
			for (Group group : groupList) {
				if (group.getProductCount() > 0) {
					group.setOperate(new StringBuffer().append("javascript:parent.selectGroup(" + group.getGroupId() + ",'" + group.getGroupName() + "','"
							+ util.getNamePath(group.getGroupId(), " >> ") + "')"));
				} else {
					//group.setOperate(new StringBuffer().append("javascript:void(0)"));
					group.setOperate(null);
				}
			}
		} else {
			for (Group group : groupList) {
				if (group.getChild() > 0) {
					group.setOperate(new StringBuffer().append("javascript:selectChildNode()"));
				} else {
					group.setOperate(new StringBuffer().append("javascript:parent.selectGroup(" + group.getGroupId() + ",'" + group.getGroupName() + "','"
							+ util.getNamePath(group.getGroupId(), " >> ") + "')"));
				}
			}
		}

		//���Other��
		int productCount = 0;
		int tradeCount = 0;

		Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());

		if (company != null && groupList.size() > 0) {
			productCount = company.getProductCount();
			tradeCount = company.getTradeCount();
			for (Group group : groupList) {
				//����Ǹ����飬�ͼ�ȥ�������е�����
				if (group.getParentId() == 0) {
					productCount -= group.getProductCount();
					tradeCount -= group.getTradeCount();
				}
			}
		}

		Group other = new Group();
		other.setGroupId(-2);
		other.setParentId(0);
		other.setGroupName("������");
		other.setProductCount(productCount);
		other.setTradeCount(tradeCount);
		if (params.getFeature() == 1000) {
			//other.setOperate(new StringBuffer().append("javascript:void(0)"));
			other.setOperate(null);
		} else {
			other.setOperate(new StringBuffer().append("javascript:parent.selectGroup(" + 0 + ",'" + other.getGroupName() + "','" + other.getGroupName() + "')"));
		}

		groupList.add(other);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		return result;
	}

	/**
	 * ��ӷ���ʱȡ����Ϣ
	 * @param params
	 * <pre>
	 *   loginUser.comId
	 *   parentId
	 * </pre>
	 * @return
	 * <pre>
	 *   formAction
	 *   group
	 * </pre>
	 */
	public Map<String, Object> getGroupAdd(QueryParams params) {
		Group group = new Group();
		if (params.getParentId() > 0) {
			Group g = this.groupDAO.findGroup(params.getLoginUser().getComId(), params.getParentId());
			if (g == null) {
				throw new PageNotFoundException();
			}
			group.setParentName(g.getGroupName());
		}
		group.setParentId(params.getParentId() <= 0 ? 0 : params.getParentId());
		group.setComId(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("formAction", "/group/group_add_submit.htm");
		result.put("group", group);
		result.put("addGroup", "addGroup");
		return result;
	}

	/**
	 * �޸ķ���ʱȡ����Ϣ
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   formAction
	 *   group
	 * </pre>
	 */
	public Map<String, Object> getGroupEdit(QueryParams params) {
		Group group = this.groupDAO.findGroup(params.getLoginUser().getComId(), params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("group", group);
		result.put("formAction", "/group/group_edit_submit.htm");
		result.put("editGroup", "editGroup");
		return result;
	}

	/**
	 * ����parentIdȡ�÷����б�
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * parentId
	 * </pre>
	 * @return result
	 * <pre>
	 * </pre>
	 */
	public Map<String, Object> getGroupOrderList(QueryParams params) {
		List<Group> list = this.groupDAO.findGroupList(params.getLoginUser().getComId());
		GroupUtil groupUtil = new GroupUtil(list);
		List<Group> groupList = groupUtil.getListByParentId(params.getParentId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		result.put("parentId", params.getParentId());
		return result;
	}

	/**
	 * ��ѯ���ڲ�Ʒ
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 * group
	 * </pre>
	 */
	public Map<String, Object> getGroupProductList(QueryParams params) {
		Group group = this.groupDAO.findGroup(params.getLoginUser().getComId(), params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		ListResult<Product> listResult = this.productDAO.findProductList(params);
		this.getProductLink(listResult, params);
		List<User> userList = this.userDAO.findUserList(params.getLoginUser().getComId());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("group", group);
		result.put("userList", userList);
		return result;
	}

	/**
	 * ��ѯ��������
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 * group
	 * </pre>
	 */
	public Map<String, Object> getGroupTradeList(QueryParams params) {
		Group group = this.groupDAO.findGroup(params.getLoginUser().getComId(), params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
		this.getTradeLink(listResult, params);

		List<User> userList = this.userDAO.findUserList(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("group", group);
		result.put("userList", userList);
		return result;
	}

	/**
	 * ��ѯδ�����Ʒ
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 * </pre>
	 */
	public Map<String, Object> getNonGroupProductList(QueryParams params) {
		ListResult<Product> listResult = this.productDAO.findProductList(params);
		this.getProductLink(listResult, params);

		List<User> userList = this.userDAO.findUserList(params.getLoginUser().getComId());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("group", new Group());
		result.put("userList", userList);
		return result;
	}

	/**
	 * ��ѯδ��������
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 * </pre>
	 */
	public Map<String, Object> getNonGroupTradeList(QueryParams params) {
		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
		this.getTradeLink(listResult, params);

		List<User> userList = this.userDAO.findUserList(params.getLoginUser().getComId());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("group", new Group());
		result.put("userList", userList);
		return result;
	}

	/**
	 * ��ӷ���
	 * @param group
	 * @param loginUser
	 * @return
	 * <pre>
	 *   operateFail
	 *   addSuccess
	 * </pre>
	 */
	public String addGroup(Group group, LoginUser loginUser) {
		int comId = loginUser.getComId();
		String currentTime = new DateUtil().getDateTime();
		
 		group.setChild(0);
		group.setProductCount(0);
		group.setTradeCount(0);
		group.setState(CN.STATE_WAIT);
		group.setCreateTime(currentTime);
		group.setModifyTime(currentTime);
		group.setComId(comId);
		if (group.getParentId() <= 0) {
			group.setParentId(0);
		}
		//��ѯ���з����б�
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil groupUtil = new GroupUtil(groupList);
		//����parent���µ�����
		List<Group> subGroupList = groupUtil.getListByParentId(group.getParentId());
		// ��������ֵ
		int maxListOrder = 0;
		for (Group g : subGroupList) {
			maxListOrder = g.getListOrder() > maxListOrder ? g.getListOrder() : maxListOrder;
		}
		group.setListOrder(maxListOrder + 1);

		//���и�����ʱ
		if (group.getParentId() > 0) {
			//����parentIdʹ�ù�����GroupUtil��ѯ������parentGroup����parentGroupΪNULLʱ���׳��쳣 
			Group parentGroup = groupUtil.get(group.getParentId());
			if (parentGroup == null) {
				throw new PageNotFoundException();
			}
			//�����ڶ���
			if(parentGroup.getDepth()==1){
				for(Group groups:groupList){
					if(groups.getParentId()==0 || groups.getParentId()==parentGroup.getGroupId()){
						if(groups.getGroupName().equals(group.getGroupName())){
							return "operateFail";
						}
					}
				}
				if(groupDAO.findGroupNameRepeat(group)>0){
					return "operateFail";
				}
			}
			//����������
			if(parentGroup.getDepth()==2){
				for(Group groups:groupList){
					if(groups.getParentId()==0 || groups.getParentId()==parentGroup.getParentId() 
							|| groups.getParentId()==group.getParentId()){
						if(groups.getGroupName().equals(group.getGroupName())){
							return "operateFail";
						}
					}
				}
			}
			//���÷�������ֵ
			group.setRootId(parentGroup.getRootId());
			group.setDepth(parentGroup.getDepth() + 1);
			if (parentGroup.getChild() <= 0) {
				group.setProductCount(parentGroup.getProductCount());
				group.setTradeCount(parentGroup.getTradeCount());
			}
			//��ӷ���
			int groupId = this.groupDAO.addGroup(group);
			if (groupId <= 0) {
				return "operateFail";
			}
			
			//���������û�������ӷ���ʱ�����²�Ʒ������ķ����ϵ
			if (parentGroup.getChild() <= 0) {
				if (parentGroup.getProductCount() > 0) {
					UpdateMap product = new UpdateMap("Product");
					product.addField("groupId", groupId);
					product.addWhere("comId", comId);
					product.addWhere("groupId", parentGroup.getGroupId());
					this.groupDAO.update(product);
				}

				if (parentGroup.getTradeCount() > 0) {
					UpdateMap trade = new UpdateMap("Trade");
					trade.addField("groupId", groupId);
					trade.addWhere("comId", comId);
					trade.addWhere("groupId", parentGroup.getGroupId());
					this.groupDAO.update(trade);
				}
			}
				//���¸��������������
				UpdateMap childCount = new UpdateMap("Groups");
				childCount.addField("child", parentGroup.getChild() + 1);
				childCount.addWhere("comId", comId);
				childCount.addWhere("groupId", parentGroup.getGroupId());
				this.groupDAO.update(childCount);
			
		} else {
				if(groupDAO.findGroupCount(group)>0){
					return "operateFail";
				}
				group.setRootId(0);//������Ϊ0
				group.setDepth(1);
				int groupId = this.groupDAO.addGroup(group);
				if (groupId <= 0) {
					return "operateFail";
				}
				//����rootId
				UpdateMap g = new UpdateMap("Groups");
				g.addField("rootId", groupId);
				g.addWhere("comId", comId);
				g.addWhere("groupId", groupId);
				this.groupDAO.update(g);
			
			
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.ADD, 
				"�����ͨ���顪��" + group.getGroupName(), loginUser);
		groupDAO.addUserLog(userLog);
		return "addSuccess";
	}

	/**
	 * ���·���
	 * @param group
	 * <pre>
	 * groupName
	 * comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 *   operateFail
	 *   editSuccess
	 * </pre>
	 */
	public String updateGroup(Group group, LoginUser loginUser) {
		UpdateMap updateMap = new UpdateMap("Groups");
		updateMap.addField("groupName", group.getGroupName());
		updateMap.addField("brief", group.getBrief());
		updateMap.addField("modifyTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", loginUser.getComId());
		updateMap.addWhere("groupId", group.getGroupId());
		int num = this.groupDAO.update(updateMap);
		if (num <= 0) {
			return "operateFail";
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, "�޸���ͨ���顪��" + group.getGroupName(), loginUser);
		groupDAO.addUserLog(userLog);
		return "editSuccess";
	}

	/**
	 * ���·�������
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * parentId
	 * groupIds
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateGroupOrder(QueryParams params) {
		int[] groupIds = params.getGroupIds();
		int listOrder = 1;

		if (groupIds.length > 0) {
			List<UpdateMap> list = new ArrayList<UpdateMap>();
			for (int i = 0; i < groupIds.length; i++) {
				UpdateMap updateMap = new UpdateMap("Groups");
				updateMap.addField("listOrder", listOrder);
				updateMap.addWhere("comId", params.getLoginUser().getComId());
				updateMap.addWhere("groupId", groupIds[i]);
				updateMap.addWhere("parentId", params.getParentId());
				list.add(i, updateMap);
				listOrder++;
			}
			//��������ֵ
			int num = this.groupDAO.update(list);
			if (num <= 0) {
				return "operateFail";
			}
		} else {
			return "operateFail";
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, "�޸���ͨ��������", params.getLoginUser());
		groupDAO.addUserLog(userLog);
		return "orderSuccess";
	}

	/**
	 * ɾ������
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
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil groupUtil = new GroupUtil(groupList);
		Group group = groupUtil.get(params.getGroupId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		if (group.getChild() > 0) {
			return "group.delete.tip";
		}
		//ɾ������
		UpdateMap updateGroup = new UpdateMap("Groups");
		updateGroup.addField("rootId", 0);
		updateGroup.addField("parentId", 0);
		updateGroup.addField("child", 0);
		updateGroup.addField("listOrder", 0);
		updateGroup.addField("productCount", 0);
		updateGroup.addField("tradeCount", 0);
		updateGroup.addField("state", CN.STATE_DELETE);
		updateGroup.addField("modifyTime", new DateUtil().getDateTime());
		updateGroup.addWhere("comId", comId);
		updateGroup.addWhere("groupId", group.getGroupId());
		int num = this.groupDAO.update(updateGroup);
		if (num <= 0) {
			return "operateFail";
		}

		//���²�ƷfeatureGroupId = 0
		UpdateMap product = new UpdateMap("Product");
		product.addField("featureGroupId", 0);
		product.addWhere("comId", comId);
		product.addWhere("featureGroupId", group.getGroupId());
		this.groupDAO.update(product);

		//���²�ƷgroupId = 0
		product = new UpdateMap("Product");
		product.addField("groupId", 0);
		product.addWhere("comId", comId);
		product.addWhere("groupId", group.getGroupId());
		int proNum = this.groupDAO.update(product);

		product = new UpdateMap("Product");
		product.addField("groupIdBak", 0);
		product.addWhere("comId", comId);
		product.addWhere("groupIdBak", group.getGroupId());
		this.groupDAO.update(product);

		//��������groupId = 0
		UpdateMap trade = new UpdateMap("Trade");
		trade.addField("groupId", 0);
		trade.addField("groupIdBak", 0);
		trade.addWhere("comId", comId);
		trade.addWhere("groupId", group.getGroupId());
		int tradeNum = this.groupDAO.update(trade);

		trade = new UpdateMap("Trade");
		trade.addField("groupIdBak", 0);
		trade.addWhere("comId", comId);
		trade.addWhere("groupIdBak", group.getGroupId());
		this.groupDAO.update(trade);

		//ȡ�ø���ķ���·��
		String groupIds = "";
		if (group.getDepth() == 3) {
			groupIds += group.getParentId() + ",";
			groupIds += groupUtil.get(group.getParentId()).getParentId();
		} else if (group.getDepth() == 2) {
			groupIds += group.getParentId();
		}
		//���·���·���Ĳ�Ʒ����������
		if (!groupIds.trim().equals("") && (proNum > 0 || tradeNum > 0)) {
			UpdateMap updateCount = new UpdateMap("Groups");
			if (proNum > 0) {
				updateCount.addField("productCount", "-", proNum);
			}
			if (tradeNum > 0) {
				updateCount.addField("tradeCount", "-", tradeNum);
			}
			updateCount.addWhere("comId", comId);
			updateCount.addWhere("groupId", groupIds, "in");
			this.groupDAO.update(updateCount); //��
		}
		//���¸�������ӷ�������
		if (group.getParentId() > 0) {
			UpdateMap updateChild = new UpdateMap("Groups");
			updateChild.addField("child", "-", 1);
			updateChild.addWhere("comId", comId);
			updateChild.addWhere("groupId", group.getParentId());
			this.groupDAO.update(updateChild);
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.DELETE, "ɾ����ͨ���顪��" + group.getGroupName(), params.getLoginUser());
		groupDAO.addUserLog(userLog);
		return "deleteSuccess";
	}

	/**
	 * ѡ����ͨ����
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * addSuccess
	 * operateFail
	 * </pre>
	 */
	public String selectProductGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);
		Group group = util.get(params.getGroupId());
		if (group.getChild() > 0) {
			return "invalidRequest";
		}
		int minGroupOrder = this.productDAO.findMinGroupOrder(util, comId, group.getRootId()) - 1;

		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap product = new UpdateMap("Product");
			product.addField("groupId", params.getGroupId());
			product.addField("groupOrder", minGroupOrder);
			product.addWhere("comId", comId);
			product.addWhere("proId", params.getProId()[i]);
			list.add(i, product);
			minGroupOrder--;
		}
		//����
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ�ø���ķ���·��
		String groupIds = "";
		if (group.getDepth() == 3) {
			groupIds = group.getRootId() + "," + group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 2) {
			groupIds = group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 1) {
			groupIds = group.getGroupId() + "";
		}

		UpdateMap updateGroup = new UpdateMap("Groups");
		updateGroup.addField("productCount", "+", params.getProId().length);
		updateGroup.addWhere("comId", comId);
		updateGroup.addWhere("groupId", groupIds, "in");
		this.groupDAO.update(updateGroup);

		ListResult<Product> listResult = this.productDAO.findProductList(params);
		UserLog userLog = null;
		for (Product product : listResult.getList()) {
			userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, "��Ӳ�Ʒ��" + product.getProName() + "������ͨ���顰" + group.getGroupName() + "��", params.getLoginUser());
			groupDAO.addUserLog(userLog);
		}

		return "addSuccess";
	}

	/**
	 * ѡ����ͨ����
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * addSuccess
	 * operateFail
	 * </pre>
	 */
	public String selectTradeGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);
		Group group = util.get(params.getGroupId());
		if (group.getChild() > 0) {
			return "invalidRequest";
		}

		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap trade = new UpdateMap("Trade");
			trade.addField("groupId", params.getGroupId());
			trade.addWhere("comId", comId);
			trade.addWhere("proId", params.getProId()[i]);
			list.add(i, trade);
		}
		//���·���Id
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ�ø���ķ���·��
		String groupIds = "";
		if (group.getDepth() == 3) {
			groupIds = group.getRootId() + "," + group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 2) {
			groupIds = group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 1) {
			groupIds = group.getGroupId() + "";
		}

		UpdateMap updateGroup = new UpdateMap("Groups");
		updateGroup.addField("tradeCount", "+", params.getProId().length);
		updateGroup.addWhere("comId", comId);
		updateGroup.addWhere("groupId", groupIds, "in");
		this.groupDAO.update(updateGroup);

		ListResult<Product> listResult = this.tradeDAO.findTradeList(params);
		UserLog userLog = null;
		for (Product product : listResult.getList()) {
			userLog = UserLogUtil.getUserLog(UserLog.GROUP, UserLog.MODIFY, "������顰" + product.getProName() + "������ͨ���顰" + group.getGroupName() + "��", params.getLoginUser());
			groupDAO.addUserLog(userLog);
		}
		return "addSuccess";
	}

	/**
	 * ѡ��������ͨ����
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * newGroupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String changeProductGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);

		Group oldGroup = util.get(params.getGroupId());
		Group newGroup = util.get(params.getNewGroupId());

		if (oldGroup.getChild() > 0 || newGroup.getChild() > 0) {
			return "invalidRequest";
		}

		//��ѯ�������ֵ�������ͬһrootId�µķ��飬�Ͳ���
		int minGroupOrder = 0;
		if (oldGroup.getRootId() != newGroup.getRootId()) {
			minGroupOrder = this.productDAO.findMinGroupOrder(util, comId, newGroup.getRootId()) - 1;
		}

		String currentTime = new DateUtil().getDateTime();
		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap product = new UpdateMap("Product");
			product.addField("groupId", params.getNewGroupId());
			product.addField("updateTime", currentTime);
			if (oldGroup.getRootId() != newGroup.getRootId()) {
				product.addField("groupOrder", minGroupOrder);
				minGroupOrder--;
			}
			product.addWhere("comId", comId);
			product.addWhere("proId", params.getProId()[i]);
			list.add(i, product);
		}
		//��������ֵ
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ��groupId����ķ���·��
		String oldGroupIds = "";
		if (oldGroup.getDepth() == 3) {
			oldGroupIds = oldGroup.getRootId() + "," + oldGroup.getParentId() + "," + oldGroup.getGroupId();
		} else if (oldGroup.getDepth() == 2) {
			oldGroupIds = oldGroup.getParentId() + "," + oldGroup.getGroupId();
		} else if (oldGroup.getDepth() == 1) {
			oldGroupIds = oldGroup.getGroupId() + "";
		}

		UpdateMap updateOldGroup = new UpdateMap("Groups");
		updateOldGroup.addField("productCount", "-", params.getProId().length);
		updateOldGroup.addWhere("comId", comId);
		updateOldGroup.addWhere("groupId", oldGroupIds, "in");
		this.groupDAO.update(updateOldGroup);

		//ȡ��newGroupId����ķ���·��
		String newGroupIds = "";
		if (newGroup.getDepth() == 3) {
			newGroupIds = newGroup.getRootId() + "," + newGroup.getParentId() + "," + newGroup.getGroupId();
		} else if (newGroup.getDepth() == 2) {
			newGroupIds = newGroup.getParentId() + "," + newGroup.getGroupId();
		} else if (newGroup.getDepth() == 1) {
			newGroupIds = newGroup.getGroupId() + "";
		}

		UpdateMap updateNewGroup = new UpdateMap("Groups");
		updateNewGroup.addField("productCount", "+", params.getProId().length);
		updateNewGroup.addWhere("comId", comId);
		updateNewGroup.addWhere("groupId", newGroupIds, "in");
		this.groupDAO.update(updateNewGroup);

		return "addSuccess";
	}

	/**
	 * ѡ��������ͨ����
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * newGroupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String changeTradeGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);

		Group oldGroup = util.get(params.getGroupId());
		Group newGroup = util.get(params.getNewGroupId());

		if (oldGroup.getChild() > 0 || newGroup.getChild() > 0) {
			return "invalidRequest";
		}

		String currentTime = new DateUtil().getDateTime();
		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap trade = new UpdateMap("Trade");
			trade.addField("groupId", params.getNewGroupId());
			trade.addField("updateTime", currentTime);
			trade.addWhere("comId", comId);
			trade.addWhere("proId", params.getProId()[i]);
			list.add(i, trade);
		}
		//����groupId
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ��groupId����ķ���·��
		String oldGroupIds = "";
		if (oldGroup.getDepth() == 3) {
			oldGroupIds = oldGroup.getRootId() + "," + oldGroup.getParentId() + "," + oldGroup.getGroupId();
		} else if (oldGroup.getDepth() == 2) {
			oldGroupIds = oldGroup.getParentId() + "," + oldGroup.getGroupId();
		} else if (oldGroup.getDepth() == 1) {
			oldGroupIds = oldGroup.getGroupId() + "";
		}

		UpdateMap updateOldGroup = new UpdateMap("Groups");
		updateOldGroup.addField("tradeCount", "-", params.getProId().length);
		updateOldGroup.addWhere("comId", comId);
		updateOldGroup.addWhere("groupId", oldGroupIds, "in");
		this.groupDAO.update(updateOldGroup);

		//ȡ��newGroupId����ķ���·��
		String newGroupIds = "";
		if (newGroup.getDepth() == 3) {
			newGroupIds = newGroup.getRootId() + "," + newGroup.getParentId() + "," + newGroup.getGroupId();
		} else if (newGroup.getDepth() == 2) {
			newGroupIds = newGroup.getParentId() + "," + newGroup.getGroupId();
		} else if (newGroup.getDepth() == 1) {
			newGroupIds = newGroup.getGroupId() + "";
		}

		UpdateMap updateNewGroup = new UpdateMap("Groups");
		updateNewGroup.addField("tradeCount", "+", params.getProId().length);
		updateNewGroup.addWhere("comId", comId);
		updateNewGroup.addWhere("groupId", newGroupIds, "in");
		this.groupDAO.update(updateNewGroup);

		return "addSuccess";
	}

	/**
	 * �������Ƴ�
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String removeFromProductGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);
		Group group = util.get(params.getGroupId());
		if (group.getChild() > 0) {
			return "invalidRequest";
		}

		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap product = new UpdateMap("Product");
			product.addField("groupId", 0);
			product.addField("groupOrder", 0);
			product.addWhere("comId", comId);
			product.addWhere("proId", params.getProId()[i]);
			list.add(i, product);
		}
		//��������ֵ
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ�ø���ķ���·��
		String groupIds = "";
		if (group.getDepth() == 3) {
			groupIds = group.getRootId() + "," + group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 2) {
			groupIds = group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 1) {
			groupIds = group.getGroupId() + "";
		}

		UpdateMap updateGroup = new UpdateMap("Groups");
		updateGroup.addField("productCount", "-", params.getProId().length);
		updateGroup.addWhere("comId", comId);
		updateGroup.addWhere("groupId", groupIds, "in");
		this.groupDAO.update(updateGroup);

		return "deleteSuccess";
	}

	/**
	 * �������Ƴ�
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * invalidRequest
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String removeFromTradeGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		List<Group> groupList = this.groupDAO.findGroupList(comId);
		GroupUtil util = new GroupUtil(groupList);
		Group group = util.get(params.getGroupId());
		if (group.getChild() > 0) {
			return "invalidRequest";
		}

		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < params.getProId().length; i++) {
			UpdateMap trade = new UpdateMap("Trade");
			trade.addField("groupId", 0);
			trade.addWhere("comId", comId);
			trade.addWhere("proId", params.getProId()[i]);
			list.add(i, trade);
		}
		//���·�����Ϣ
		int num = this.groupDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}

		//ȡ�ø���ķ���·��
		String groupIds = "";
		if (group.getDepth() == 3) {
			groupIds = group.getRootId() + "," + group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 2) {
			groupIds = group.getParentId() + "," + group.getGroupId();
		} else if (group.getDepth() == 1) {
			groupIds = group.getGroupId() + "";
		}

		UpdateMap updateGroup = new UpdateMap("Groups");
		updateGroup.addField("tradeCount", "-", params.getProId().length);
		updateGroup.addWhere("comId", comId);
		updateGroup.addWhere("groupId", groupIds, "in");
		this.groupDAO.update(updateGroup);

		return "deleteSuccess";
	}

	/**
	 * ���·��� �Ƿ���ʾNewͼ��
	 * @param params
	 * @return operateFail
	 * addSuccess
	 */
	public String updateGroupIco(QueryParams params) {
		UpdateMap updateGroup = new UpdateMap("Groups");
		if (params.isShowNewIco()) {
			updateGroup.addField("isShowNewIco", 1);
		} else {
			updateGroup.addField("isShowNewIco", 0);
		}
		updateGroup.addWhere("comId", params.getLoginUser().getComId());
		updateGroup.addWhere("groupId", params.getGroupId());

		int num = this.groupDAO.update(updateGroup);
		if (num <= 0) {
			return "operateFail";
		}
		return "addSuccess";

	}

	private String getOptionModel(int groupId, String groupName, StringBuffer href) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"" + groupId + "\" style='display:none;margin:5px;'>");
		sb.append(TextUtil.getText(this.getClass(), "group.groupName", "zh") + "��" + groupName);
		sb.append("<br />");
		sb.append(href);
		sb.append("</div>");
		return sb.toString();
	}

	private String getLink(String url, String text, boolean javaScript) {
		String href = "<a href=\"#position\" onclick=\"";
		if (!javaScript) {
			href += "window.location.href = '";
			href += url;
			href += "'";
		} else {
			href += "javascript:";
			href += url;
		}
		href += "\">" + text + "</a>&nbsp;";
		return href;
	}

	private void getProductLink(ListResult<Product> listResult, QueryParams params) {
		Map<Integer, Integer> copyProIdMap = this.tradeDAO.findTradeCopyProId(params.getLoginUser().getComId());

		for (Product product : listResult.getList()) {
			// �ж��ܷ�תΪ����
			if (params.getLoginUser().isAdmin() || params.getLoginUser().getUserId() == product.getUserId()) {
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

	private void getTradeLink(ListResult<Product> listResult, QueryParams params) {
		for (Product product : listResult.getList()) {
			if (params.getLoginUser().isAdmin() || params.getLoginUser().getUserId() == product.getUserId()) {
				product.addOperate(new Button("/trade/trade_edit.htm").appendParam("proId", product.getProId()).setName("button.editTrade").getLink());
				product.addOperate("<br />");
			}
			product.addOperate(new Button("/trade/trade_add.htm").appendParam("proId", product.getProId()).setName("button.addSameTrade").getLink());
		}
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
