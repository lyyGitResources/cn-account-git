/* 
 * Created by linliuwei at 2009-1-14 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.util;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.cn.account.entity.Group;

/**
 * ���鹤����
 * @author linliuwei
 */
public class GroupUtil {

	private List<Group> list;

	/**
	 * @param list
	 */
	public GroupUtil(List<Group> list) {
		super();
		this.list = list;
	}

	/**
	 * ����ָ������
	 * @param groupId 
	 * @return Group
	 */
	public Group get(int groupId) {
		Group group = null;
		for (Group tmp : list) {
			if (tmp.getGroupId() == groupId) {
				group = tmp;
				break;
			}
		}
		return group;
	}

	/**
	 * ������·�����������Լ���
	 * @param groupId
	 * @return
	 */
	public List<Group> getListPath(int groupId) {
		List<Group> result = new ArrayList<Group>();
		Group group = get(groupId);
		if (group != null) {
			if (group.getDepth() == 1) {
				result.add(group);
			} else if (group.getDepth() == 2) {
				Group temp = get(group.getParentId());
				if (temp != null) {
					result.add(temp);
					result.add(group);
				}
			} else if (group.getDepth() == 3) {
				Group root = get(group.getRootId());
				Group parent = get(group.getParentId());
				if (root != null && parent != null) {
					result.add(get(group.getRootId()));
					result.add(get(group.getParentId()));
					result.add(group);
				}
			}	
		}
		return result;
	}

	/**
	 * ����root���µ�����
	 * @param groupId
	 * @return
	 */
	public List<Group> getListByRootId(int rootId) {
		return getListByRootId(rootId, false);
	}

	/**
	 * ����root���µ�����
	 * @param rootId
	 * @param last �Ƿ���ײ�
	 * @return
	 */
	public List<Group> getListByRootId(int rootId, boolean last) {
		List<Group> result = new ArrayList<Group>();
		for (Group group : list) {
			if (group.getRootId() == rootId) {
				if (last) {
					if (group.getChild() <= 0) {
						result.add(group);
					}
				} else {
					result.add(group);
				}
			}
		}
		return result;
	}

	/**
	 * ����parent���µ�����
	 * @param groupId
	 * @return
	 */
	public List<Group> getListByParentId(int parentId) {
		List<Group> result = new ArrayList<Group>();
		for (Group group : list) {
			if (group.getParentId() == parentId) {
				result.add(group);
			}
		}
		return result;
	}

	/**
	 * ��������·��
	 * @param groupId 
	 * @param split ·���ָ���
	 * @return
	 */
	public String getNamePath(int groupId, String split) {
		List<Group> listPath = getListPath(groupId);
		StringBuilder sb = new StringBuilder();
		for (Group group : listPath) {
			sb.append(group.getGroupName());
			sb.append(split);
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - split.length(), sb.length());
		}
		return sb.toString();
	}

	/**
	 * ����ID·�����ö��ŷָ�
	 * @param groupId
	 * @return
	 */
	public String getIdPath(int groupId) {
		List<Group> listPath = getListPath(groupId);
		StringBuilder sb = new StringBuilder();
		for (Group group : listPath) {
			sb.append(group.getGroupId());
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}

	/**
	 * ����ID���ö��ŷָ�
	 * @return
	 */
	public String getIds() {
		StringBuffer buff = new StringBuffer();
		for (Group group : list) {
			buff.append(group.getGroupId());
			buff.append(",");
		}
		if (buff.length() > 0) {
			buff.deleteCharAt(buff.length() - 1);
		}
		return buff.toString();
	}

	/**
	 * ��������ID���ö��ŷָ�
	 * @param groupId
	 * @return
	 */
	public String getChildIds(int groupId) {
		return getChildIds(groupId, false);
	}

	/**
	 * ��������ID���ö��ŷָ�
	 * @param groupId
	 * @param last �Ƿ�ֻ��Ҫ��ײ����
	 * @return
	 */
	public String getChildIds(int groupId, boolean last) {
		StringBuilder sb = new StringBuilder();
		Group group = get(groupId);
		// ��ǰ���ǵ�1��
		if (group.getDepth() == 1) {
			List<Group> result;
			if(last){
				result = getListByRootId(group.getRootId(), last);
			}else{
				result = getListByRootId(group.getRootId());
			}
			for (Group tmp : result) {
				sb.append(tmp.getGroupId());
				sb.append(",");
			}
			// ��ǰ���ǵ�2��
		} else if (group.getDepth() == 2) {
			List<Group> result = getListByParentId(group.getGroupId());
			for (Group tmp : result) {
				sb.append(tmp.getGroupId()).append(",");
			}
			sb.append(group.getGroupId()).append(",");

			// ��ǰ���ǵ�3��
		} else if (group.getDepth() == 3) {
			sb.append(group.getGroupId()).append(",");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}
	
	/**
	 * �����ID�Ƿ񶼴��ڣ����ڷ���true
	 * @param groupIds
	 * @return
	 */
	public boolean check(int[] groupIds) {
		int count = 0;
		for (int groupId : groupIds) {
			for (Group group : list) {
				if (group.getGroupId() == groupId) {
					count++;
					break;
				}
			}
		}
		return groupIds.length == count;
	}
}
