/* 
 * Created by baozhimin at Dec 24, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ContactUsAction extends BasicAction implements ModelDriven<ServiceMail> {

	private static final long serialVersionUID = 3305255728486604038L;
	private ServiceMail serviceMail = new ServiceMail();

	public String contact_us() throws Exception {
		currentMenu = "contactUs";
		return super.execute();
	}

	public String contact_us_submit() throws Exception {
		MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		Mail email = MailFactory.getContactUsMail(serviceMail);
		emailSender.add(email);
		TaskExecutor.execute(emailSender);

		this.addMessage("邮件发送成功！我们会及时处理您的邮件并给您答复，衷心感谢您对海商网的关注与支持！");
		return super.execute();
	}

	public void validateContact_us_submit() {
		if (StringUtil.isEmpty(serviceMail.getReason())) {
			this.addActionError("请选择一个联系原因");
		}
		if (StringUtil.isEmpty(serviceMail.getSubject())) {
			this.addActionError("请填写主题");
		}
		if (StringUtil.isEmpty(serviceMail.getContent())) {
			this.addActionError("请填写内容");
		}
		if (StringUtil.isEmpty(serviceMail.getContact())) {
			this.addActionError("请填写名称");
		}
		if (StringUtil.isEmpty(serviceMail.getEmail()) || !Validator.isEmail(serviceMail.getEmail())) {
			this.addActionError("请填写有效的邮箱");
		}
		if (!ValidateCode.isValid(request, serviceMail.getValidateCodeKey(), serviceMail.getValidateCode())) {
			this.addFieldError("validateCode.error", TextUtil.getText("validateCode.error", "zh"));
		}
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return null;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public ServiceMail getModel() {
		return serviceMail;
	}
}
