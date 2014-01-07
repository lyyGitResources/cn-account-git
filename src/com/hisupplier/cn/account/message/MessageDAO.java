package com.hisupplier.cn.account.message;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.PermissionDeniedDataAccessException;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Contact;
import com.hisupplier.cn.account.entity.ContactGroup;
import com.hisupplier.cn.account.entity.MessageTemplate;
import com.hisupplier.cn.account.sms.Message1;
import com.hisupplier.cn.account.sms.MessageUtil;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.entity.MessageChargeLog;
import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.StringUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

public class MessageDAO extends DAO {

	/**
	 * 添加短信账号
	 * 
	 * @param account
	 */
	public void addMessageAccount(MessageAccount account) {
		this.getSqlMapClientTemplate().insert("message.addMessageAccount", account);
	}

	public int addMessageChargeLog(MessageChargeLog charge) {
		return (Integer) this.getSqlMapClientTemplate().insert("message.addMessageChargeLog", charge);
	}

	/**
	 * 查询短信账号
	 * 
	 * @param comId
	 * @return MessageAccount or null
	 */
	public MessageAccount findMessageAccount(int comId) {
		return (MessageAccount) this.getSqlMapClientTemplate().queryForObject("message.findMessageAccount", comId);
	}

	/**
	 * 更新短信数量
	 * 
	 * @param comId
	 * @param num
	 * @return
	 */
	public int updateMessageNum(int comId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("num", num);
		return this.getSqlMapClientTemplate().update("message.updateMessageNum", params);
	}

	/**
	 * 添加用户自定义模板
	 * 
	 * @param template
	 * @return int
	 */
	public int addMessageTemplate(MessageTemplate template) {
		return (Integer) this.getSqlMapClientTemplate().insert("message.addMessageTemplate", template);
	}

	/**
	 * 按公司ID查询联系人组列表
	 * 
	 * @param comId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<ContactGroup> findContactGroupList(int comId) {
		List<ContactGroup> contactGroupList = (ArrayList<ContactGroup>) this.getSqlMapClientTemplate().queryForList("message.findContactGroupList", comId);
		if (contactGroupList == null) {
			contactGroupList = new ArrayList<ContactGroup>(0);
		}
		return contactGroupList;
	}

	/**
	 * 按不同条件组合查询联系人列表
	 * 
	 * @param comId
	 * @param groupId
	 *            0=未分组 -1=全部
	 * @param type
	 *            1=姓名,2=手机号
	 * @param keyword
	 * @param pageNo
	 *            -1=不分页
	 * @param pageSize
	 * @return ResultList
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Contact> findContactList(int comId, int groupId, int type, String keyword, int pageNo, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("groupId", groupId);
		if (StringUtil.isNotEmpty(keyword)) {
			params.put("keyword", keyword);
			params.put("type", type);
		}
		Page page = null;
		List<Contact> contactList = null;
		if (pageNo != -1) {
			int totalRecord = (Integer) this.getSqlMapClientTemplate().queryForObject("message.findContactListCount", params);
			page = PageFactory.createPage(totalRecord, pageNo, pageSize);
			params.put("startRow", page.getStartIndex());
			params.put("pageSize", pageSize);
			contactList = (ArrayList<Contact>) this.getSqlMapClientTemplate().queryForList("message.findContactList", params);
		} else {
			contactList = (ArrayList<Contact>) this.getSqlMapClientTemplate().queryForList("message.findAllContactList", params);
		}
		if (contactList == null) {
			contactList = new ArrayList<Contact>(0);
		}
		return new ListResult<Contact>(contactList, page);
	}

	/**
	 * 按不同条件组合查询短信模板列表
	 * 
	 * @param comId
	 *            0=系统模板
	 * @param type
	 *            -1=全部,1=春节,2=中秋,3=生日,4=健康问候,10=我的短语
	 * @param pageNo
	 * @param pageSize
	 * @return ResultList
	 */
	@SuppressWarnings("unchecked")
	public ListResult<MessageTemplate> findMessageTemplateList(int comId, int type, int pageNo, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("type", type);
		int totalRecord = (Integer) this.getSqlMapClientTemplate().queryForObject("message.findMessageTemplateListCount", params);
		Page page = PageFactory.createPage(totalRecord, pageNo, pageSize);
		params.put("startRow", page.getStartIndex());
		params.put("pageSize", pageSize);
		List<MessageTemplate> messageTemplateList = (ArrayList<MessageTemplate>) this.getSqlMapClientTemplate().queryForList("message.findMessageTemplateList", params);
		return new ListResult<MessageTemplate>(messageTemplateList, page);
	}

	/**
	 * 按不同条件组合查询短信发送记录列表
	 * 
	 * @param comId
	 * @param type
	 *            1=手机号 2=短信内容内容 3=发送时间 与keyword联合使用
	 * @param keyword
	 * @param result
	 *            0=等待发送 1=已经发送 -1=所有 -2=发送失败
	 * @param pageNo
	 * @param pageSize
	 * @return List
	 */
	public ListResult<Message1> findMessageLogList(int comId, int type, String keyword, int result, int pageNo, int pageSize) {
		JdbcUtil jdbc = null;
		ListResult<Message1> resultList = null;
		try {
			String where = this.preWhere(type, keyword, result);
			jdbc = new JdbcUtil(MessageUtil.getSms());
			jdbc.begin();
			int totalRecord = 0;
			jdbc.createPreparedStatement("select count(*) totalRecord" + where);
			this.preQuery(comId, keyword, result, jdbc, true);
			if (jdbc.resultNext()) {
				totalRecord = jdbc.getInt("totalRecord");
			}
			Page page = PageFactory.createPage(totalRecord, pageNo, pageSize);
			resultList = new ListResult<Message1>(new ArrayList<Message1>(), page);
			StringBuilder sql = new StringBuilder();
			sql.append("select top ");
			sql.append(pageSize);
			sql.append(" id,contactId,mobile,content,result,plan_time,submit_time ");
			sql.append(where);
			sql.append(" and id not in(select top ");
			sql.append(page.getStartRow());
			sql.append(" id ");
			sql.append(where);
			sql.append(" order by id desc) order by id desc");
			jdbc.createPreparedStatement(sql.toString());
			this.preQuery(comId, keyword, result, jdbc, false);
			while (jdbc.resultNext()) {
				Message1 msg = new Message1();
				msg.setId(jdbc.getInt("id"));
				msg.setContactId(jdbc.getInt("contactId"));
				msg.setMobile(jdbc.getString("mobile"));
				msg.setContent(jdbc.getString("content"));
				msg.setResult(jdbc.getString("result"));
				msg.setPlan_time(jdbc.getString("plan_time"));
				msg.setSubmit_time(jdbc.getString("submit_time"));
				resultList.getList().add(msg);
			}
			jdbc.commit();
		} catch (Exception e) {
			if (jdbc != null) {
				jdbc.rollback();
			}
		} finally {
			if (jdbc != null) {
				jdbc.close();
			}
		}
		if (resultList == null) {
			Page page = PageFactory.createPage(0, pageNo, pageSize);
			resultList = new ListResult<Message1>(new ArrayList<Message1>(0), page);
		}
		return resultList;
	}

	/**
	 * 查询短信发送记录时的条件组合
	 * 
	 * @param type
	 * @param keyword
	 * @param result
	 * @return String
	 */
	private String preWhere(int type, String keyword, int result) {
		StringBuilder where = new StringBuilder();
		where.append(" from sms_send where comId = ?");
		if (StringUtil.isNotEmpty(keyword)) {
			if (type == 1) {
				where.append(" and mobile like ");
			} else if (type == 2) {
				where.append(" and content like ");
			} else {
				where.append(" and submit_time like ");
			}
			where.append("?");
		}
		if (result == 0 || result == 1 || result == -2) {
			if (result == -2) {// 发送失败
				where.append(" and (result = ? or result='b')");
			}
			if (result == 1) {
				where.append(" and (result = ? or result='2' or result='3' or result='c')");
			}
		}
		where.append(" and state = ?");
		return where.toString();
	}

	/**
	 * 查询短信发送记录时的参数设置
	 * 
	 * @param comId
	 * @param keyword
	 * @param result
	 * @param jdbc
	 * @param queryTotal
	 */
	private void preQuery(int comId, String keyword, int result, JdbcUtil jdbc, boolean queryTotal) {
		int i = 1;
		jdbc.setInt(1, comId);
		if (StringUtil.isNotEmpty(keyword)) {
			i++;
			jdbc.setString(i, "%" + keyword + "%");
		}
		if (result == 0 || result == 1 || result == -2) {
			i++;
			jdbc.setString(i, result == -2 ? "a" : result + "");
		}
		i++;
		jdbc.setInt(i, 20);
		if (!queryTotal) {
			i++;
			jdbc.setInt(i, comId);
			if (StringUtil.isNotEmpty(keyword)) {
				i++;
				jdbc.setString(i, "%" + keyword + "%");
			}
			if (result == 0 || result == 1 || result == -2) {
				i++;
				jdbc.setString(i, result == -2 ? "a" : result + "");
			}
			i++;
			jdbc.setInt(i, 20);
		}
		jdbc.preQuery();
	}

	/**
	 * 删除短信记录(标记删除)
	 * 
	 * @param comId
	 * @param logIds
	 */
	public int deleteMessageLog(int comId, int[] logIds) {
		JdbcUtil jdbc = null;
		int count = 0;
		try {
			jdbc = new JdbcUtil(MessageUtil.getSms());
			jdbc.begin();
			jdbc.createPreparedStatement("update sms_send set state = ? where id=? and comId = ?");
			for (Integer id : logIds) {
				count++;
				jdbc.setInt(1, 0);
				jdbc.setInt(2, id);
				jdbc.setInt(3, comId);
				jdbc.preAddBatch();
				if (count % 25 == 0) {
					jdbc.preExecuteBatch();
				}
			}
			jdbc.preExecuteBatch();
			jdbc.commit();
		} catch (Exception e) {
			jdbc.rollback();
		} finally {
			if (jdbc != null) {
				jdbc.close();
			}
		}
		return count;
	}

	/**
	 * 更新短信状态为0(重新发送短信)
	 * 
	 * @param comId
	 * @param msgId
	 * @return int
	 */
	public int updateMessageLog(int comId, int msgId) {
		JdbcUtil jdbc = null;
		int count = 0;
		try {
			jdbc = new JdbcUtil(MessageUtil.getSms());
			jdbc.begin();
			count = jdbc.update("update sms_send set result = 0 where id=" + msgId + " and comId = " + comId);
			jdbc.commit();
		} catch (Exception e) {
			jdbc.rollback();
		} finally {
			if (jdbc != null) {
				jdbc.close();
			}
		}
		return count;
	}

	/**
	 * 删除联系人
	 * 
	 * @param comId
	 * @param contactId
	 * @return int
	 */
	public int deleteContact(int comId, int contactId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("contactId", contactId);
		return this.getSqlMapClientTemplate().delete("message.deleteContact", params);
	}

	public int deleteContacts(int comId, String[] contactIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("contactIds", contactIds);
		return this.getSqlMapClientTemplate().delete("message.deleteContacts", map);
	}

	/**
	 * 更新联系人
	 * 
	 * @param contact
	 * @return int
	 */
	public int updateContact(Contact contact) {
		return this.getSqlMapClientTemplate().update("message.updateContact", contact);
	}

	/**
	 * 添加联系人
	 * 
	 * @param contact
	 * @return int
	 */
	public int addContact(Contact contact) {
		return (Integer) this.getSqlMapClientTemplate().insert("message.addContact", contact);
	}

	/**
	 * 更新联系人的组ID
	 * 
	 * @param comId
	 * @param groupId
	 * @param newGroupId
	 * @return int
	 */
	public int updateGroupId(int comId, int groupId, int newGroupId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("groupId", groupId);
		params.put("newGroupId", newGroupId);
		return this.getSqlMapClientTemplate().delete("message.updateGroupId", params);
	}

	/**
	 * 删除联系人组
	 * 
	 * @param comId
	 * @param groupId
	 * @return int
	 */
	public int deleteContactGroup(int comId, int groupId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("groupId", groupId);
		return this.getSqlMapClientTemplate().delete("message.deleteContactGroup", params);
	}

	/**
	 * 更新联系人组
	 * 
	 * @param group
	 * @return int
	 */
	public int updateContactGroup(ContactGroup group) {
		return this.getSqlMapClientTemplate().update("message.updateContactGroup", group);
	}

	public int addContactGroup(ContactGroup group) {
		return (Integer) this.getSqlMapClientTemplate().insert("message.addContactGroup", group);
	}

	/**
	 * 获取不分页的联系人列表
	 * 
	 * @param comId
	 * @param groupId
	 *            -1表示所有
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contact> findContactListNoPage(int comId, int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("groupId", groupId);
		return (ArrayList<Contact>) this.getSqlMapClientTemplate().queryForList("message.findContactListNoPage", map);
	}

	public int addContact(List<Contact> contactList) {
		int count = 0;
		if (contactList != null) {
			try {
				SqlMapClient sqlMapClient = this.getSqlMapClientTemplate().getSqlMapClient();
				sqlMapClient.startBatch();

				for (Contact contact : contactList) {
					sqlMapClient.insert("message.addContact", contact);
					count++;
				}
				sqlMapClient.executeBatch();
			} catch (SQLException e) {
				throw new PermissionDeniedDataAccessException(e.getMessage(), e);
			}
		}
		return count;
	}

	/**
	 * 删除用户自定义短语
	 * 
	 * @param comId
	 * @param templateId
	 */
	public int deleteMessageTemplate(int comId, int[] templateIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		params.put("templateIds", templateIds);
		return this.getSqlMapClientTemplate().delete("message.deleteMessageTemplate", params);
	}

	/**
	 * 更新常用短语
	 * 
	 * @param template
	 * @return int
	 */
	public int updateMessageTemplate(MessageTemplate template) {
		return this.getSqlMapClientTemplate().update("message.updateMessageTemplate", template);
	}

	/**
	 * 按公司ID查询充值记录
	 * 
	 * @param comId
	 * @param pageNo
	 * @param pageSize
	 * @return ResultList
	 */
	@SuppressWarnings("unchecked")
	public ListResult<MessageChargeLog> findMessageChargeLogList(int comId, int pageNo, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comId", comId);
		int totalRecord = (Integer) this.getSqlMapClientTemplate().queryForObject("message.findMessageChargeLogCount", params);
		Page page = PageFactory.createPage(totalRecord, pageNo, pageSize);
		if (totalRecord < 1) {
			ArrayList<MessageChargeLog> arryList = new ArrayList<MessageChargeLog>(0);
			return new ListResult<MessageChargeLog>(arryList, page);
		}
		params.put("startRow", page.getStartIndex());
		params.put("pageSize", pageSize);
		List<MessageChargeLog> messageChargeLogList = (ArrayList<MessageChargeLog>) this.getSqlMapClientTemplate().queryForList("message.findMessageChargeLogList", params);
		return new ListResult<MessageChargeLog>(messageChargeLogList, page);
	}

	/**
	 * 查询该号码是否存在
	 * @param contact
	 * comId
	 * mobile
	 * @return
	 */
	public int findContactByMobile(Contact contact) {
		return (Integer) getSqlMapClientTemplate().queryForObject("message.findContactByMobile", contact);
	}
}
