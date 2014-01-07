package com.hisupplier.cn.account.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.util.DateUtil;

public class UserDAO extends DAO {

	/**
	 * ��ѯ���䣬����Ѵ��ڷ����û�ID�����򷵻�0
	 * @param email
	 * @return
	 */
	public int findEmail(String email) {
		return this.findEmail(-1, email);
	}

	/**
	 * ��ѯ���䣬����Ѵ��ڷ����û�ID�����򷵻�0
	 * @param userId �ų����û�ID��ֻ�����ʺ���Ϣ�޸�ʱ
	 * @param email
	 * @return 
	 */
	public int findEmail(int userId, String email) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("email", email);
		Integer id = (Integer) this.getSqlMapClientTemplate().queryForObject("user.findEmail", map);
		return id == null ? 0 : id;
	}

	/**
	 * �����û�
	 * @param userId
	 * @param comId
	 * @return
	 */
	public User findUser(int userId, int comId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("comId", comId);
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUser", map);
		if (user != null) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
			user.setPreLoginTime(DateUtil.formatDateTime(user.getPreLoginTime()));
		}
		return user;
	}

	/**
	 * ���ع���Ա�û�
	 * @param comId
	 * @return
	 */
	public User findUserByAdmin(int comId) {
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUserByAdmin", comId);
		user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
		user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		return user;
	}

	/**
	 * ����Ա�ʺŲ�ѯ����Ա�û����Ҳ�������null
	 * @param memberId
	 * @return
	 */
	public User findUserByMemberId(String memberId) {
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUserByMemberId", memberId);
		if (user != null) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return user;
	}

	/**
	 * �������ѯ�û����Ҳ�������null
	 * @param memberId
	 * @return
	 */
	public User findUserByEmail(String email) {
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUserByEmail", email);
		if (user != null) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return user;
	}

	/**
	 * ����Id��ѯ�û����Ҳ�������null
	 * @param userId
	 * @return
	 */
	public User findUserById(int userId) {
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUserById", userId);
		if (user != null) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return user;
	}

	/**
	 * ����Id��ѯ�û����Ҳ�������null
	 * @param userId
	 * @return
	 */
	public User findUserByParentId(int userId,int comId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", userId);
		map.put("comId", comId);
		User user = (User) this.getSqlMapClientTemplate().queryForObject("user.findUserByParentId", map);
		if (user != null) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return user;
	}
	
	/**
	 * �����û��б�
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserList(int comId) {
		List<User> list = (ArrayList<User>) this.getSqlMapClientTemplate().queryForList("user.findUserList", comId);
		for (User user : list) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return list;
	}

	/**
	 * ����ƷĿ¼��ѯ�û���Ϣ��ֻ���ڷ����Ƽ�ѯ�̣�
	 * @param catIds ���ID�ö��ŷָ�
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserListByProCatId(String catIds) {
		List<User> list = (ArrayList<User>) this.getSqlMapClientTemplate().queryForList("user.findUserListByProCatId", catIds);
		for (User user : list) {
			user.setCreateTime(DateUtil.formatDateTime(user.getCreateTime()));
			user.setModifyTime(DateUtil.formatDateTime(user.getModifyTime()));
		}
		return list;
	}

	/**
	 * �����û����ɹ������û�ID�����򷵻�0
	 * @param user
	 * @return
	 */
	public int addUser(User user) {
		return (Integer) this.getSqlMapClientTemplate().insert("user.addUser", user);
	}
	/**
	 * �޸��û�
	 * @param user
	 * @return ��������
	 */
	public int updateUser(User user) {
		return this.getSqlMapClientTemplate().update("user.updateUser", user);
	}

	/**
	 * @param userId
	 * @param oldPasswd
	 * @return
	 */
	public boolean checkPassword(int userId, String oldPasswd) {
		Map<String, Object> map =  new HashMap<String, Object>(); 
		map.put("userId", userId);
		map.put("passwd", oldPasswd);
		return (Boolean) this.getSqlMapClientTemplate().queryForObject("user.checkPassword", map);
	}
	
	public String findProvince(int comId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("user.findProvince", comId);
	}
}
