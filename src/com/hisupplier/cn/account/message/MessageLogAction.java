package com.hisupplier.cn.account.message;

import java.util.Map;
import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.exception.ServiceException;

import com.opensymphony.xwork2.ModelDriven;

public class MessageLogAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;

	public String list() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (this.params.getType() != 1 && this.params.getType() != 2 && this.params.getType() != 3 && this.params.getType() != -1) {
			this.params.setType(-1);
		}
		if (this.params.getResultType() != 1 && this.params.getResultType() != -1 && this.params.getResultType() != -2) {
			this.params.setResultType(-1);
		}
		this.result = this.messageService.messageLogList(this.getLoginUser().getComId(), 
				params.getType(), params.getKeyword(), params.getResultType(), params.getPageNo(), params.getPageSize());
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String delete() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (params.getMsgId() > 0) {
			if (this.messageService.deleteMessageLog(this.getLoginUser().getComId(), params.getMsgId()) > 0) {
				return SUCCESS;
			}
		}
		this.addActionError("删除失败");
		return INPUT;
	}

	public String repeat() throws Exception {
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		if (params.getMsgId() > 0) {
			if (this.messageService.repeatSendMessage(this.getLoginUser().getComId(), params.getMsgId()) > 0) {
				return SUCCESS;
			}
		}
		this.addActionError("重发失败");
		return INPUT;
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

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public QueryParams getModel() {
		return params;
	}

	@Override
	public String getMsg() {
		return null;
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
