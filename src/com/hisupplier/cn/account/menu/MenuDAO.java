package com.hisupplier.cn.account.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

public class MenuDAO extends DAO {

	/**
	 * ��ѯ�˵���Ŀ�б�
	 * @param params
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Group> findMenuGroupList(QueryParams params) {
		params.setDefaultSort(new String[] { "viewCount", "listOrder", "modifyTime" }, "listOrder", "asc");

		List<Group> list = (List<Group>) this.getSqlMapClientTemplate().queryForList("menu.findMenuGroupList", params);
		for (Group group : list) {
			group.setModifyTime(DateUtil.formatDate(group.getModifyTime()));
		}
		return list;
	}

	public Group findGroup(int groupId, int comId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		map.put("comId", comId);
		return (Group) this.getSqlMapClientTemplate().queryForObject("menu.findGroup", map);
	}

	/**
	 * �����˵���Ŀ������Ӱ������
	 * @param group
	 * @return
	 */
	public int addMenuGroup(Menu menu) {
		 return (Integer) this.getSqlMapClientTemplate().insert("menu.addMenuGroup", menu);
	}

	/**
	 * ��������ϲ�ѯ�˵���Ϣ�б�
	 * @param params
	 * @return menuList
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Menu> findMenuList(QueryParams params) {
		params.setDefaultSort(new String[] { "viewCount", "listOrder", "modifyTime" }, "listOrder", "asc");
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("menu.findMenuListCount", params);

		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Menu> list = this.getSqlMapClientTemplate().queryForList("menu.findMenuList", params);
		for (int i = 0; i < list.size(); i++) {
			Menu menu = list.get(i);
			menu.setModifyTime(DateUtil.formatDate(menu.getModifyTime()));
		}
		return new ListResult<Menu>(list, page);
	}

	/**
	 * ��ѯ�����˵���Ϣ
	 * @param menuId
	 * @param comId
	 * @return menu
	 */
	public Menu findMenu(int menuId, int comId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuId", menuId);
		map.put("comId", comId);
		return (Menu) this.getSqlMapClientTemplate().queryForObject("menu.findMenu", map);
	}

	/**
	 * ��Ӳ˵���Ϣ
	 * @param menu
	 * @return int
	 */
	public int addMenu(Menu menu) {
		return (Integer) this.getSqlMapClientTemplate().insert("menu.addMenu", menu);
	}

	public void addMenuExtra(Menu menu) {
		this.getSqlMapClientTemplate().insert("menu.addMenuExtra", menu);
	}

	/**
	 * ��ѯ���в˵���Ŀ��˵���Ϣ���������ֵ
	 * @param comId
	 * @return
	 */
	public int findMaxListOrder(String tableName, int comId, int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableName", tableName);
		map.put("comId", comId);
		map.put("groupId", groupId);
		Integer maxListOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("menu.findMaxListOrder", map);
		return maxListOrder == null ? 0 : maxListOrder;
	}
	
	/**
	 * ͨ�����Ʋ�ѯmenuGroup����
	 * @param params
	 * @return
	 */
	public int findMenuGroupCountByGroupName(String groupName,int comId,int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupName", groupName);
		map.put("comId", comId);
		map.put("groupId", groupId);
		return (Integer)this.getSqlMapClientTemplate().queryForObject("menu.findMenuGroupCountByGroupName", map);
	}

}
