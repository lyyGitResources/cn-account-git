package com.hisupplier.cn.account.message;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Contact;
import com.hisupplier.cn.account.entity.ContactGroup;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.exception.ServiceException;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class PhoneBookAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private static final Pattern patternMobile = Pattern.compile("^((13|15|18)[0-9]{9})|(106[0-9]{11,12})$");
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;

	public String list() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		params.setPageSize(15);
		this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(),
				params.getGroupId(), params.getType(), params.getKeyword(),
				params.getPageNo(), params.getPageSize());
		return SUCCESS;
	}

	public String addContact() throws Exception {
		int comId = getLoginUser().getComId();
		this.validateContact();
		if (this.getActionErrors().size() <= 0) {
			Contact contact = new Contact();
			contact.setComId(comId);
			contact.setGroupId(params.getGroupId());
			contact.setMobile(params.getMobile());
			contact.setContactName(params.getContactName());
			msg = messageService.addContact(contact);
			if (msg.equals("succ")) {
				this.addMessage("添加联系人成功。");
				return SUCCESS;
			} else {
				this.addActionError(msg);
			}
		}
		this.result = this.messageService.getPhoneBook(comId, -1, -1, "",
				params.getPageNo(), params.getPageSize());
		return INPUT;
	}

	public String deleteContact() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (params.getContactId() <= 0) {
			this.addActionError("请选择要删除的联系人");
			return INPUT;
		}
		this.messageService.deleteContact(getLoginUser().getComId(), params.getContactId());
		this.addMessage("删除联系人成功");
		return SUCCESS;
	}
	
	public String batchDeleteContact() throws Exception {
		if (!this.isOpen()) { throw new ServiceException("短信群发尚未开通"); }
		msg = messageService.deleteContacts(getLoginUser().getComId(), params.getContactIds());
		if (msg.equals("fail")) {
			addActionError("删除失败。");
		} else {
			addMessage(this.msg);
		}
		return SUCCESS;
	}

	public String updateContact() throws Exception {
		this.validateContact();
		if (this.getActionErrors().size() <= 0) {
			Contact contact = new Contact();
			contact.setId(params.getContactId());
			contact.setComId(this.getLoginUser().getComId());
			contact.setGroupId(params.getGroupId());
			contact.setMobile(params.getMobile());
			contact.setContactName(params.getContactName());
			this.messageService.updateContact(contact);
		} else {
			this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
			return INPUT;
		}
		this.addMessage("修改联系人成功");
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String addContactGroup() throws Exception {
		this.validateContactGroup();
		this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
		List<ContactGroup> list = (List<ContactGroup>) result.get("contactgroup");
		if (list.size() >= 10) {
			this.addActionError("联系人组最多添加10个");
		}
		if (this.getActionErrors().size() <= 0) {
			this.messageService.addContactGroup(this.getLoginUser().getComId(), params.getGroupName());
		} else {
			return INPUT;
		}
		this.addMessage("添加组成功");
		return SUCCESS;
	}

	public String deleteContactGroup() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (params.getGroupId() <= 0) {
			this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
			this.addActionError("请选择要删除的组");
			return INPUT;
		}
		this.messageService.deleteContactGroup(this.getLoginUser().getComId(), params.getGroupId());
		this.addMessage("删除组成功");
		return SUCCESS;
	}

	public String updateContactGroup() throws Exception {
		this.validateContactGroup();
		if (this.getActionErrors().size() <= 0) {
			ContactGroup group = new ContactGroup();
			group.setComId(this.getLoginUser().getComId());
			group.setGroupId(params.getGroupId());
			group.setGroupName(params.getGroupName());
			this.messageService.upateContactGroup(group);
		} else {
			this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
			return INPUT;
		}
		this.addMessage("修改组成功");
		return SUCCESS;
	}

	private boolean isOpen() {
		int comId = this.getLoginUser().getComId();
		MessageAccount account = this.messageService.getMessageAccount(comId);
		if (account == null)
			return false;

		return true;
	}

	private void validateContact() {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		Matcher matched = patternMobile.matcher(params.getMobile());
		if (!matched.matches()) {
			this.addActionError("请正确填写手机号码");
		}
		if (params.getContactName().length() == 0 || params.getContactName().length() > 40) {
			this.addActionError("请正确填写联系人姓名，字符长度在1~40之间");
		}
		if (params.getGroupId() == -1) {
			params.setGroupId(0);
		}
	}

	private void validateContactGroup() {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (StringUtil.isEmpty(params.getGroupName())) {
			this.addActionError("请填写组名");
		} else if (params.getGroupName().length() > 30) {
			this.addActionError("组名长度不能超过30个字符");
		}
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public QueryParams getModel() {
		return params;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}
}
