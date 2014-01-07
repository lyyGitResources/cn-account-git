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
			throw new ServiceException("����Ⱥ����δ��ͨ");
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
				this.addMessage("�����ϵ�˳ɹ���");
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
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		if (params.getContactId() <= 0) {
			this.addActionError("��ѡ��Ҫɾ������ϵ��");
			return INPUT;
		}
		this.messageService.deleteContact(getLoginUser().getComId(), params.getContactId());
		this.addMessage("ɾ����ϵ�˳ɹ�");
		return SUCCESS;
	}
	
	public String batchDeleteContact() throws Exception {
		if (!this.isOpen()) { throw new ServiceException("����Ⱥ����δ��ͨ"); }
		msg = messageService.deleteContacts(getLoginUser().getComId(), params.getContactIds());
		if (msg.equals("fail")) {
			addActionError("ɾ��ʧ�ܡ�");
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
		this.addMessage("�޸���ϵ�˳ɹ�");
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String addContactGroup() throws Exception {
		this.validateContactGroup();
		this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
		List<ContactGroup> list = (List<ContactGroup>) result.get("contactgroup");
		if (list.size() >= 10) {
			this.addActionError("��ϵ����������10��");
		}
		if (this.getActionErrors().size() <= 0) {
			this.messageService.addContactGroup(this.getLoginUser().getComId(), params.getGroupName());
		} else {
			return INPUT;
		}
		this.addMessage("�����ɹ�");
		return SUCCESS;
	}

	public String deleteContactGroup() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		if (params.getGroupId() <= 0) {
			this.result = this.messageService.getPhoneBook(this.getLoginUser().getComId(), -1, -1, "", params.getPageNo(), params.getPageSize());
			this.addActionError("��ѡ��Ҫɾ������");
			return INPUT;
		}
		this.messageService.deleteContactGroup(this.getLoginUser().getComId(), params.getGroupId());
		this.addMessage("ɾ����ɹ�");
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
		this.addMessage("�޸���ɹ�");
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
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		Matcher matched = patternMobile.matcher(params.getMobile());
		if (!matched.matches()) {
			this.addActionError("����ȷ��д�ֻ�����");
		}
		if (params.getContactName().length() == 0 || params.getContactName().length() > 40) {
			this.addActionError("����ȷ��д��ϵ���������ַ�������1~40֮��");
		}
		if (params.getGroupId() == -1) {
			params.setGroupId(0);
		}
	}

	private void validateContactGroup() {
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		if (StringUtil.isEmpty(params.getGroupName())) {
			this.addActionError("����д����");
		} else if (params.getGroupName().length() > 30) {
			this.addActionError("�������Ȳ��ܳ���30���ַ�");
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
