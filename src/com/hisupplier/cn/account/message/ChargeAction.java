package com.hisupplier.cn.account.message;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.entity.MessageAccount;
import com.hisupplier.commons.exception.ServiceException;
import com.opensymphony.xwork2.ModelDriven;

public class ChargeAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -759494787232020280L;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private MessageService messageService;

	public String charge() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		return SUCCESS;
	}

	public String chargeLog() throws Exception {
		params.setShowTitle(true);
		if (!this.isOpen()) {
			throw new ServiceException("短信群发尚未开通");
		}
		this.result.put("listResult", this.messageService.getMessageChargeLog(this.getLoginUser().getComId(), params.getPageNo(), params.getPageSize()));
		return SUCCESS;
	}

	private boolean isOpen() {
		int comId = this.getLoginUser().getComId();
		MessageAccount account = this.messageService.getMessageAccount(comId);
		if (account == null)
			return false;
		result = new HashMap<String, Object>();
		result.put("number", account.getNumber());
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
