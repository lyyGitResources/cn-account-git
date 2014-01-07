package com.hisupplier.cn.account.message;

import java.util.Map;

import com.hisupplier.cn.account.entity.MessageTemplate;
import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.exception.ServiceException;
import com.opensymphony.xwork2.ModelDriven;

public class TemplateAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -759494787232020280L;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;

	private void validateTemplate() {
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		if (params.getContent().length() > 60 || params.getContent().length() < 5) {
			this.addActionError("���ö������ݳ��ȱ�����5~60���ַ�֮��");
		}
	}

	public String list() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		params.setPageSize(5);
		if (params.getPageNo() == -1)
			params.setPageNo(1);
		this.result = this.messageService.getMessageTemplate(this.getLoginUser().getComId(), params.getType(), params.getPageNo(), params.getPageSize());
		return SUCCESS;
	}

	public String add() throws Exception {
		this.validateTemplate();
		result = this.messageService.getMessageTemplate(this.getLoginUser().getComId(), -1, params.getPageNo(), params.getPageSize());
		if (this.getActionErrors().size() > 0) {
			return INPUT;
		} else {
			this.messageService.addMessageTemplate(this.getLoginUser().getComId(), params.getContent());
			this.addMessage("��Ӷ���ɹ�");
		}
		return SUCCESS;
	}

	public String delete() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("����Ⱥ����δ��ͨ");
		}
		this.messageService.deleteTemplate(this.getLoginUser().getComId(), params.getTemplateId());
		this.addMessage("ɾ������ɹ�");
		return SUCCESS;
	}

	public String update() throws Exception {
		this.validateTemplate();
		result = this.messageService.getMessageTemplate(this.getLoginUser().getComId(), -1, params.getPageNo(), params.getPageSize());
		if (this.getActionErrors().size() > 0) {
			return INPUT;
		} else {
			MessageTemplate template = new MessageTemplate();
			template.setComId(this.getLoginUser().getComId());
			template.setId(params.getTemplateId());
			template.setContent(params.getContent());
			this.messageService.updateTemplate(template);
			this.addMessage("�޸Ķ���ɹ�");
		}
		return SUCCESS;
	}

	private boolean isOpen() {
		int comId = this.getLoginUser().getComId();
		MessageAccount account = this.messageService.getMessageAccount(comId);
		if (account == null)
			return false;

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
