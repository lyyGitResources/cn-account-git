package com.hisupplier.cn.account.message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Contact;
import com.hisupplier.cn.account.entity.ContactGroup;
import com.hisupplier.cn.account.entity.MessageTemplate;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.sms.Message1;
import com.hisupplier.cn.account.sms.MessageUtil;
import com.hisupplier.cn.account.sms.SMSFactory;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.entity.MessageChargeLog;
import com.hisupplier.commons.exception.ServiceException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;

public class MessageService {

	public int addMessageAccount(int comId) {
		String currentTime = new DateUtil().getDateTime();
		MessageAccount account = new MessageAccount();
		account.setComId(comId);
		account.setCreateTime(currentTime);
		Company company = this.companyDAO.findCompany(comId);
		// 系统自动赠送给Goldsite会员20条短信，Freesite会员10条短信（黄金指数总分1500分以上的赠送）
		int giftNum = 0;
		if (company.getMemberType() == CN.GOLD_SITE) {
			account.setNumber(20);
			giftNum = 20;
		} else if (company.getGoldIndex() > 1500) {
			account.setNumber(10);
			giftNum = 10;
		}
		this.messageDAO.addMessageAccount(account);
		if (giftNum > 0) {
			MessageChargeLog chargeLog = new MessageChargeLog();
			chargeLog.setComId(comId);
			chargeLog.setNumber(0);
			chargeLog.setGiftNumber(giftNum);
			chargeLog.setMoneys(0);
			chargeLog.setChargeType(1);
			chargeLog.setComId(comId);
			chargeLog.setCreateTime(currentTime);
			chargeLog.setRemark("");
			this.messageDAO.addMessageChargeLog(chargeLog);
		}
		return account.getNumber();
	}

	/**
	 * <p>
	 * 短信群发 <br>
	 * 1.查询用户短信发送余额是否足够发送。<br>
	 * 2.调用短信群发API进行发送。<br>
	 * 3.记录每条短信发送状态入数据库<br>
	 * 
	 * @param mobiles
	 * @param content
	 * @return 1 发送成功，0 余额不足
	 */
	public int addMessage(int comId, List<String> mobiles, String content) {
		MessageAccount account = this.messageDAO.findMessageAccount(comId);
		if (account == null || account.getNumber() <= 0 || mobiles.size() > account.getNumber())
			return 0;
		if (account != null && account.getNumber() > 0) {
			SMSFactory.addMessage(comId, content, mobiles);
			if (mobiles.size() > 0) {
				this.messageDAO.updateMessageNum(comId, -(mobiles.size()));
			}
		}
		return 1;
	}

	/**
	 * <p>
	 * 添加用戶自定义模板
	 * 
	 * @param comId
	 * @param content
	 * @return int
	 */
	public int addMessageTemplate(int comId, String content) {
		MessageTemplate template = new MessageTemplate();
		content = MessageUtil.filterSentitiveWord(content);
		template.setComId(comId);
		template.setContent(content);
		template.setCreateTime(new DateUtil().getDateTime());
		template.setTypes(10);// 自定义类
		return (Integer) this.messageDAO.addMessageTemplate(template);
	}

	/**
	 * <p>
	 * 获取短信账号
	 * 
	 * @param comId
	 * @return MessageAccount
	 */
	public MessageAccount getMessageAccount(int comId) {
		return this.messageDAO.findMessageAccount(comId);
	}

	/**
	 * 获取联系人组(组下包含联系人列表)
	 * 
	 * @param comId
	 * @return List
	 */
	public Map<String, Object> getContactGroup(int comId) {
		List<ContactGroup> groupList = this.messageDAO.findContactGroupList(comId);
		List<ContactGroup> groupList_ = new ArrayList<ContactGroup>(groupList.size());
		for (ContactGroup g : groupList) {
			groupList_.add(g.clone());
		}
		ContactGroup otherGroup = new ContactGroup();
		otherGroup.setComId(comId);
		otherGroup.setGroupName("未分组");
		otherGroup.setGroupId(0);
		groupList_.add(otherGroup);
		List<Contact> contactList = this.messageDAO.findContactList(comId, -1, 0, "", -1, 0).getList();
		for (ContactGroup g_ : groupList_) {
			for (Contact c : contactList) {
				if (g_.getGroupId() == c.getGroupId()) {
					if (g_.getContactList() == null) {
						g_.setContactList(new ArrayList<Contact>());
					}
					g_.getContactList().add(c);
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("contactnum", contactList.size());
		result.put("groupList", groupList_);
		return result;
	}

	/**
	 * <p>
	 * 常用短语
	 * 
	 * @param comId
	 *            0=系统模板
	 * @param type
	 *            -1=全部,1=春节,2=中秋,3=生日,4=健康问候,10=我的短语
	 * @param pageNo
	 * @param pageSize
	 * @return ResultList
	 */
	public Map<String, Object> getMessageTemplate(int comId, int type, int pageNo, int pageSize) {
		if (type < 10 && type > 0) {
			comId = 0;// 系统模板
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", this.messageDAO.findMessageTemplateList(comId, type, pageNo, pageSize));
		return result;
	}

	/**
	 * <p>
	 * 短信发送记录,同时检测发送失败的短信条数并返回
	 * 
	 * @param comId
	 * @param type
	 *            1=手机号 2=短信内容内容 与keyword联合使用
	 * @param keyword
	 * @param result
	 *            0=等待发送 1=已经发送 -1=所有 -2=发送失败
	 * @param pageNo
	 * @param pageSize
	 * @return List
	 */
	public Map<String, Object> messageLogList(int comId, int type, String keyword, int state, int pageNo, int pageSize) {
		keyword = StringUtil.filterSearchText(keyword);
		Map<String, Object> result = new HashMap<String, Object>();
		ListResult<Message1> msgResultList = this.messageDAO.findMessageLogList(comId, type, keyword, state, pageNo, pageSize);
		int failedCount = 0;

		for (Message1 msg : msgResultList.getList()) {
			if (msg.getResult().equals("a") || msg.getResult().equals("b")) {
				failedCount++;
			}
		}
		if (failedCount > 0) {
			this.messageDAO.updateMessageNum(comId, failedCount);
		}
		result.put("listResult", msgResultList);
		// result.put("mypage", msgResultList.getPage());
		return result;
	}

	/**
	 * <p>
	 * 电话薄
	 * 
	 * @param comId
	 * @param groupId
	 *            0=未分组 -1=所有组
	 * @param type
	 *            1=姓名,2=手机号
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return MessageResult
	 */
	public Map<String, Object> getPhoneBook(int comId, int groupId, int type, String keyword, int pageNo, int pageSize) {
		keyword = StringUtil.filterSearchText(keyword);
		Map<String, Object> result = new HashMap<String, Object>();
		ListResult<Contact> contactResultList = this.messageDAO.findContactList(comId, groupId, type, keyword, pageNo, pageSize);
		List<ContactGroup> contactGroupList = this.messageDAO.findContactGroupList(comId);
		result.put("listResult", contactResultList);
		result.put("contactgroup", contactGroupList);
		return result;
	}

	/**
	 * 删除短信记录
	 * 
	 * @param comId
	 * @param msgId
	 * @return int
	 */
	public int deleteMessageLog(int comId, int msgId) {
		int[] msgIds = new int[] { msgId };
		return this.messageDAO.deleteMessageLog(comId, msgIds);
	}

	/**
	 * 短信重发
	 * 
	 * @param comId
	 * @param msgId
	 * @return int
	 */
	public int repeatSendMessage(int comId, int msgId) {
		return this.messageDAO.updateMessageLog(comId, msgId);
	}

	/**
	 * <p>
	 * 删除联系人
	 * 
	 * @param comId
	 * @param contactId
	 * @return int
	 */
	public int deleteContact(int comId, int contactId) {
		return this.messageDAO.deleteContact(comId, contactId);
	}
	
	/**
	 * 批量删除联系人
	 * @param comId
	 * @param contactIds
	 */
	public String deleteContacts(int comId, String contactIds) {
		if (StringUtil.isBlank(contactIds)) {
			return "fail";
		}
		
		String[] contactIdArray = StringUtil.split(contactIds, ',');
		if (contactIdArray.length <= 0) { return "fail"; }
		
		int count = messageDAO.deleteContacts(comId, contactIdArray);
		if (count > 0) {
			return "已成功删除" + count + "条记录。";
		} else {
			return "fail";
		}
	}

	/**
	 * <p>
	 * 更新联系人信息
	 * 
	 * @param contact
	 * @return int
	 */
	public int updateContact(Contact contact) {
		this.isGroupIdExist(contact);
		String currentTime = new DateUtil().getDateTime();
		contact.setModifyTime(currentTime);
		return this.messageDAO.updateContact(contact);
	}

//	/**
//	 * <p>
//	 * 添加联系人
//	 * 
//	 * @param contact
//	 * @return int
//	 */
//	public int addContact(Contact contact) {
//		String currentTime = new DateUtil().getDateTime();
//		contact.setCreateTime(currentTime);
//		contact.setModifyTime(currentTime);
//		this.isGroupIdExist(contact);
//		return this.messageDAO.addContact(contact);
//	}
	
	
	/**
	 * 添加联系人
	 * @param contact
	 * @return
	 */
	public String addContact(Contact contact) {
		String currentTime = new DateUtil().getDateTime();
		contact.setCreateTime(currentTime);
		contact.setModifyTime(currentTime);
		this.isGroupIdExist(contact);
		
		if (isContactExist(contact)) {
			return "手机号已存在";
		}
		if (messageDAO.addContact(contact) == 0) {
			return "联系人添加失败";
		}
		return "succ";
	}
	
	private boolean isContactExist(Contact contact) {
		return messageDAO.findContactByMobile(contact) > 0 ? true : false;
	}

	/**
	 * 检测联系人组是否存在
	 * 
	 * @param contact
	 */
	private void isGroupIdExist(Contact contact) {
		List<ContactGroup> groupList = this.messageDAO.findContactGroupList(contact.getComId());
		boolean exist = false;
		if (contact.getGroupId() == 0) {
			exist = true;
		} else {
			for (ContactGroup group : groupList) {
				if (group.getGroupId() == contact.getGroupId()) {
					exist = true;
					break;
				}
			}
		}
		if (!exist) {
			throw new ServiceException("选择的分组不存在");
		}
	}

	/**
	 * <p>
	 * 添加联系人组
	 * 
	 * @param comId
	 * @param groupName
	 * @return int
	 */
	public int addContactGroup(int comId, String groupName) {
		String currentTime = new DateUtil().getDateTime();
		ContactGroup group = new ContactGroup();
		group.setComId(comId);
		group.setGroupName(groupName);
		group.setCreateTime(currentTime);
		group.setModifyTime(currentTime);
		return this.messageDAO.addContactGroup(group);
	}

	/**
	 * <p>
	 * 删除联系组
	 * 
	 * @param comId
	 * @param groupId.
	 * @return int
	 */
	public int deleteContactGroup(int comId, int groupId) {
		this.messageDAO.deleteContactGroup(comId, groupId);
		return this.messageDAO.updateGroupId(comId, groupId, 0);
	}

	/**
	 * 更新联系人组
	 * 
	 * @param group
	 * @return int
	 */
	public int upateContactGroup(ContactGroup group) {
		String currentTime = new DateUtil().getDateTime();
		group.setModifyTime(currentTime);
		return this.messageDAO.updateContactGroup(group);
	}

	/**
	 * 获取需要导出的联系人列表
	 * 
	 * @param comId
	 * @return
	 */
	public List<Contact> getContactList(int comId, int groupId) {
		return (List<Contact>) this.messageDAO.findContactListNoPage(comId, groupId);
	}

	/**
	 * 导入联系人到电话簿(txt)
	 * 
	 * @param countact
	 * @return int
	 */
	public HashMap<String, Object> exportTXT(QueryParams queryParams, int comId) {
		HashMap<String, Object> logSet;
		List<Contact> contactList = new ArrayList<Contact>();
		String currentTime = new DateUtil().getDateTime();
		try {
			//FileReader read = new FileReader(queryParams.getUpload());
			InputStreamReader reader=new InputStreamReader(new FileInputStream(queryParams.getUpload()),"GB2312");
			BufferedReader br = new BufferedReader(reader);
			String row = "";
			String[] cows;

			// 相同的号码不导入
			Set<String> set = new HashSet<String>();
			List<Contact> contactList2 = this.messageDAO.findContactListNoPage(comId, -1);
			for (Contact c : contactList2) {
				set.add(c.getMobile());
			}
			logSet = new HashMap<String, Object>();
			// List<String> sameMobile = new ArrayList<String>();
			int samecount = 0, wrongcount = 0;
			while ((row = br.readLine()) != null) {
				cows = StringUtil.split(row, ",");
				if (!Validator.isMobile(cows[1])) {
					wrongcount++;
					continue;
				}
				if (set.contains(cows[1])) {
					samecount++;
					continue;
				}
				set.add(cows[1]);

				Contact con = new Contact();
				con.setComId(comId);
				con.setGroupId(queryParams.getGroupId());
				con.setContactName(cows[0]);
				con.setMobile(cows[1]);
				con.setCreateTime(currentTime);
				con.setModifyTime(currentTime);
				contactList.add(con);
			}
			br.close();
			reader.close();
			logSet.put("samecount", samecount);
			logSet.put("wrongcount", wrongcount);
		} catch (Exception e) {
			e.printStackTrace();
			logSet = new HashMap<String, Object>(0);
			return logSet;
		}
		int count = this.messageDAO.addContact(contactList);
		logSet.put("count", count);
		return logSet;
	}

	public HashMap<String, Object> exportXLS(QueryParams params, String filePath, int comId) {
		HashMap<String, Object> logSet;
		List<Contact> contactList = new ArrayList<Contact>();
		String currentTime = new DateUtil().getDateTime();
		Workbook rwb = null;
		int irows = 0;
		try {
			InputStream is = new FileInputStream(filePath);
			rwb = Workbook.getWorkbook(is);
			Sheet rs = rwb.getSheet(0);
			irows = rs.getRows();

			// 相同的号码不导入
			Set<String> set = new HashSet<String>();
			List<Contact> contactList2 = this.messageDAO.findContactListNoPage(comId, -1);
			for (Contact c : contactList2) {
				set.add(c.getMobile());
			}
			logSet = new HashMap<String, Object>();
			// List<String> sameMobile = new ArrayList<String>();
			// List<String>
			int samecount = 0, wrongcount = 0;

			for (int i = 0; i < irows; i++) {
				Cell[] cell = rs.getRow(i);
				String mobile = cell[Integer.parseInt(params.getMobile())].getContents();
				if (!Validator.isMobile(mobile)) {
					wrongcount++;
					continue;
				}
				if (set.contains(mobile)) {
					samecount++;
					continue;
				}
				set.add(mobile);
				Contact con = new Contact();
				con.setComId(comId);
				con.setGroupId(params.getGroupId());
				con.setContactName(cell[Integer.parseInt(params.getName())].getContents());
				con.setMobile(mobile);
				con.setCreateTime(currentTime);
				con.setModifyTime(currentTime);
				contactList.add(con);
			}
			rwb.close();
			is.close();
			logSet.put("samecount", samecount);
			logSet.put("wrongcount", wrongcount);
		} catch (Exception e) {
			e.printStackTrace();
			logSet = new HashMap<String, Object>(0);
			return logSet;
		} finally {
			java.io.File file = new java.io.File(filePath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
		int count = this.messageDAO.addContact(contactList);
		logSet.put("count", count);
		return logSet;
	}

	public Map<String, Object> getFileCount(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		String[] letters = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		if (params.getFormat() == 1) {
			jxl.Workbook rwb = null;
			int icols = 0;
			try {
				InputStream is = new FileInputStream(params.getUpload());
				rwb = Workbook.getWorkbook(is);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			Sheet rs = rwb.getSheet(0);
			icols = rs.getColumns();
			for (int i = 0; i < icols; i++) {
				list.add(letters[i]);
			}
			rwb.close();
		} else {
			try {
				FileReader read = new FileReader(params.getUpload());
				BufferedReader br = new BufferedReader(read);
				String row = "";
				String[] cows;
				if ((row = br.readLine()) != null) {
					cows = StringUtil.split(row, ",");
					for (int i = 0; i < cows.length; i++) {
						list.add(letters[i]);
					}
				}
				br.close();
				read.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		result.put("list", list);
		return result;
	}

	/**
	 * 删除模板
	 * 
	 * @param comId
	 * @param templateId
	 * @return int
	 */
	public int deleteTemplate(int comId, int templateId) {
		int[] templateIds = new int[] { templateId };
		return this.messageDAO.deleteMessageTemplate(comId, templateIds);
	}

	/**
	 * 更新模板
	 * 
	 * @param template
	 * @return int
	 */
	public int updateTemplate(MessageTemplate template) {
		template.setContent(MessageUtil.filterSentitiveWord(template.getContent()));
		return this.messageDAO.updateMessageTemplate(template);
	}

	/**
	 * <p>
	 * 充值记录
	 * 
	 * @param comId
	 * @param pageNo
	 * @param pageSize
	 * @return ResultList
	 */
	public ListResult<MessageChargeLog> getMessageChargeLog(int comId, int pageNo, int pageSize) {
		return this.messageDAO.findMessageChargeLogList(comId, pageNo, pageSize);
	}

	private MessageDAO messageDAO;
	private CompanyDAO companyDAO;

	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public MessageDAO getMessageDAO() {
		return messageDAO;
	}

	public void setMessageDAO(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

}
