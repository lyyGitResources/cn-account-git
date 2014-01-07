package com.hisupplier.cn.account.ad;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class TopOrderAction extends BasicAction implements ModelDriven<TopOrder> {
	private static final long serialVersionUID = 8416000403688890665L;

	private AdService adService;
	private TopOrder topOrder = new TopOrder();
	private Map<String, Object> result;

	public String top_order_submit() throws Exception {
		StringUtil.trimToEmpty(topOrder, "keyword,remark");

		if (StringUtil.isNotEmpty(topOrder.getKeyword()) && topOrder.getKeyword().length() > 500) {
			this.addActionError(this.getText("adOrder.keyword.required"));
			return INPUT;
		}
		if (topOrder.getRemark().length() > 500) {
			this.addActionError(this.getText("upgrade.remark.required"));
			return INPUT;
		}

		tip = adService.addTopOrder(topOrder);
		if (tip.equals("addSuccess")) {
			//ÓÊ¼þ·¢ËÍ
			MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
			Mail email = MailFactory.getTopOrder(topOrder);
			emailSender.add(email);
			TaskExecutor.execute(emailSender);
		}
		this.addMessage(getText(tip));

		return SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public TopOrder getModel() {
		return topOrder;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	public void setTopOrder(TopOrder topOrder) {
		this.topOrder = topOrder;
	}

}
