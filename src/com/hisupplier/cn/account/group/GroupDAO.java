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
	 * ��state in (15,20)��ѯ��������
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
	 * ͨ��comId��parentId��groupName��ѯ
	 * @param group
	 * @return
	 */
	public int findGroupCount(Group group){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("group.findGroupCount",group);
	}

	/**
	 * ��state in (15,20)��ѯ�����б�
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
	 * ��state in (15,20)��ѯ�����б�
	 * @param comId
	 * @param feature
	 * 	<pre>
	 * 		feature == 1000 �����β�Ʒ����==0
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
	 * ��ѯ�����������Ƿ����ظ�
	 * @param group
	 *<pre>
	 *		comID
	 *		ParentId
	 *		groupName
	 *</pre>
	 * @return �ظ�����
	 */
	public int findGroupNameRepeat(Group group){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("group.findGroupNameRepeat", group);
	}

	/**
	 * ��ӷ���
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
