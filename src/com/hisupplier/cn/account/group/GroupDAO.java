/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author linliuwei
 */
public class GroupDAO extends DAO {

	/**
	 * 按state in (15,20)查询单个分组
	 * @param comId
	 * @param groupId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   Group
	 * </pre>
	 */
	public Group findGroup(int comId, int groupId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("comId", comId);
		map.put("groupId", groupId);
		return (Group) this.getSqlMapClientTemplate().queryForObject("group.findGroup", map);
	}
	/**
	 * 通过comId，parentId，groupName查询
	 * @param group
	 * @return
	 */
	public int findGroupCount(Group group){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("group.findGroupCount",group);
	}

	/**
	 * 按state in (15,20)查询分组列表
	 * @param comId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   List<Group>
	 * </pre>
	 */
	public List<Group> findGroupList(int comId) {
		return findGroupList(comId, 0);
	}
	
	/**
	 * 按state in (15,20)查询分组列表
	 * @param comId
	 * @param feature
	 * 	<pre>
	 * 		feature == 1000 ：屏蔽产品数量==0
	 * </pre>
	 * @return List<Group>
	 */
	@SuppressWarnings("unchecked")
	public List<Group> findGroupList(int comId, int feature) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("comId", comId);
		map.put("feature", feature);
		List<Group> list = (ArrayList<Group>) this.getSqlMapClientTemplate().queryForList("group.findGroupList", map);
		if (list != null) {
			for (Group group : list) {
				group.setCreateTime(DateUtil.formatDateTime(group.getCreateTime()));
				group.setModifyTime(DateUtil.formatDateTime(group.getModifyTime()));
			}
		}
		return list;
	}
	/**
	 * 查询第三层组名是否有重复
	 * @param group
	 *<pre>
	 *		comID
	 *		ParentId
	 *		groupName
	 *</pre>
	 * @return 重复个数
	 */
	public int findGroupNameRepeat(Group group){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("group.findGroupNameRepeat", group);
	}

	/**
	 * 添加分组
	 * @param group
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addGroup(Group group) {
		return (Integer) this.getSqlMapClientTemplate().insert("group.addGroup", group);
	}
}
