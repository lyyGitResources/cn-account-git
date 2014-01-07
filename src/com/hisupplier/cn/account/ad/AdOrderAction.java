package com.hisupplier.cn.account.ad;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class AdOrderAction extends BasicAction implements ModelDriven<AdOrder> {
	private static final long serialVersionUID = 8416000403688890665L;

	private AdService adService;
	private AdOrder adOrder = new AdOrder();
	private Map<String, Object> result;

	public String ad_order_submit() throws Exception {
		if (StringUtil.isNotEmpty(adOrder.getKeyword()) && adOrder.getCatId() != 0) {
			adOrder.setAdType(3);
		} else if (StringUtil.isNotEmpty(adOrder.getKeyword())) {
			adOrder.setAdType(2);
		} else if (adOrder.getCatId() != 0) {
			adOrder.setAdType(1);
		}

		StringUtil.trimToEmpty(adOrder, "remark");
		if (adOrder.getRemark().length() > 500) {
			this.addActionError(this.getText("upgrade.remark.required"));
			return INPUT;
		}

		tip = adService.addAdOrder(adOrder);
		if (tip.equals("addSuccess")) {
			//ÓÊ¼þ·¢ËÍ
			MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
			Mail email = MailFactory.getAdOrder(adOrder);
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

	public AdOrder getModel() {
		return adOrder;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	public void setAdOrder(AdOrder adOrder) {
		this.adOrder = adOrder;
	}

}
