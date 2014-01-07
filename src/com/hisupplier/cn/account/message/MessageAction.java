package com.hisupplier.cn.account.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.exception.ServiceException;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class MessageAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private static final Pattern patternMobile = Pattern.compile("^(\\(.+\\))?(((13|15|18)[0-9]{9})|(106[0-9]{11,12}))$");
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;
	MessageAccount account;
	private List<String> phoneList;

	public String addAccount() throws Exception {
		if (!this.isOpen()) {
			int comId = getLoginUser().getComId();
			int num = this.messageService.addMessageAccount(comId);
			account = this.messageService.getMessageAccount(comId);
			result = new HashMap<String, Object>();
			result.put("number", account.getNumber());
			result.put("gift", num);
		}
		return SUCCESS;
	}

	public String form() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			return "noAccount";
		}

		if (account.getNumber() == 0) {
			this.addActionError("�������㣬���ֵ");
		}
		return SUCCESS;
	}

	public String send() throws Exception {
		int comId = getLoginUser().getComId();
		this.validateSend();
		if (this.getActionErrors().size() > 0)
			return INPUT;
		int flag = this.messageService.addMessage(comId, phoneList,
				params.getContent() + params.getSignature());
		if (flag == 1) {
			this.addMessage("���ͳɹ�");
			if (params.isSave()) {
				this.messageService.addMessageTemplate(comId, params.getContent());
			}
		}
		if (flag == 0) {
			this.addActionError("�˻�����");
			return INPUT;
		}

		return SUCCESS;
	}

	public String ajaxPhoneBook() throws Exception {
		this.result = this.messageService.getContactGroup(this.getLoginUser().getComId());
		return SUCCESS;
	}

	public String ajaxTemplate() throws Exception {
		if (params.getPageNo() == -1) params.setPageNo(1);
		result = this.messageService.getMessageTemplate(this.getLoginUser().getComId(), params.getType(), params.getPageNo(), 7);
		result.put("type", params.getType());
		return SUCCESS;
	}

	private void validateSend() {
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		if (params.getPhoneStr().length() == 0) {
			this.addActionError("����д��Ҫ���͵��ֻ�����");
		} else {
			params.setPhoneStr(params.getPhoneStr().replaceAll("��", ","));
		}
		String[] phones = StringUtil.toArray(params.getPhoneStr(), ",");
		Matcher matched = null;
		phoneList = new ArrayList<String>();
		for (String phone : phones) {
			matched = patternMobile.matcher(phone);
			if (!matched.matches()) {
				this.addActionError("�ֻ������ʽ����ȷ��" + phone);
			} else {
				phoneList.add(matched.group(2));
			}
		}
		if (params.getSignature().length() > 5) {
			this.addActionError("ǩ��������5���ַ�֮��");
		}
		if (params.getContent().length() == 0 || params.getContent().length() > 60) {
			this.addActionError("���ݱ�����1~60���ַ�֮��");
		}
	}

	private boolean isOpen() {
		int comId = this.getLoginUser().getComId();
		account = this.messageService.getMessageAccount(comId);
		if (account == null)
			return false;
		result = new HashMap<String, Object>();
		result.put("number", account.getNumber());
		result.put("gift", -1);
		return true;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
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
