package com.hisupplier.cn.account.ad;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class UpgradeAction extends BasicAction implements ModelDriven<UpgradeMail> {
	private static final long serialVersionUID = 8416000403688890665L;

	private AdService adService;
	private UpgradeMail upgradeMail = new UpgradeMail();
	private Map<String, Object> result;

	public String upgrade_submit() throws Exception {
		StringUtil.trimToEmpty(upgradeMail, "remark");

		if (upgradeMail.getRemark().length() > 500) {
			this.addActionError(this.getText("upgrade.remark.required"));
			return INPUT;
		}

		tip = adService.addUpgrade(upgradeMail);
		if (tip.equals("addSuccess")) {
			//ÓÊ¼þ·¢ËÍ
			MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
			Mail email = MailFactory.getUpgrade(upgradeMail);
			emailSender.add(email);
			TaskExecutor.execute(emailSender);
			this.addActionMessage(getText("upgrade.success"));
		}else{
			this.addActionError(getText(tip));
		}

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

	public UpgradeMail getModel() {
		return upgradeMail;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	public void setUpgradeMail(UpgradeMail upgradeMail) {
		this.upgradeMail = upgradeMail;
	}
}
