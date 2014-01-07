package com.hisupplier.cn.account.basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ModelDriven;

public class ServiceFormAction extends BasicAction implements ModelDriven<ServiceMail> {

	private static final long serialVersionUID = -7883621332635421281L;
	private BasicService basicService;
	private ServiceMail serviceMail = new ServiceMail();
	private Map<String, Object> result;

	public String service_mail_send() throws Exception {
		if(serviceMail.getSubject().equals(this.getText("serviceMail.subject7"))){
			serviceMail.setSubject(serviceMail.getOtherSubject());
		}
		if(serviceMail.getContactMode().equals(this.getText("serviceMail.tel"))){
			serviceMail.setContactMode(serviceMail.getContactMode() +"</br> 联系时间：" +serviceMail.getContactDate()+" "+serviceMail.getContactTime());
		}
		MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		Mail email = MailFactory.getServiceMail(serviceMail);
		emailSender.add(email);
		TaskExecutor.execute(emailSender);
		this.addActionMessage(this.getText("serviceMail.mailSend.success"));
		return SUCCESS;
	}

	public void validateService_mail_send() throws FileNotFoundException {
		if (StringUtil.isEmpty(serviceMail.getSubject())) {
			this.addActionError(this.getText("serviceMail.subject.required"));
		}
		if(serviceMail.getSubject().equals(this.getText("serviceMail.subject7"))&& StringUtil.isEmpty(serviceMail.getOtherSubject())){
			this.addActionError(this.getText("serviceMail.otherSubject.required"));
		}
		if (StringUtil.isEmpty(serviceMail.getContent()) || serviceMail.getContent().length() > 2000) {
			this.addActionError(this.getText("serviceMail.content.required"));
		}
		if (serviceMail.getUpload() != null) {
			if (!StringUtil.containsValue(ALLOW_MIME_TYPE, serviceMail.getUploadContentType())) {
				this.addActionError(this.getText("serviceMail.uploadContentType.error"));
			} else if (serviceMail.getUpload().length() / 1024 > 500) {
				this.addActionError(this.getText("serviceMail.uploadSize.maxlength"));
			} else {
				Attachment attachment = UploadUtil.uploadFileStream(this.getLoginUser().getComId(), 0, serviceMail.getUploadFileName(), new FileInputStream(serviceMail.getUpload()));
				if (attachment != null) {
					serviceMail.setFilePath(WebUtil.getFilePath(attachment.getFilePath().replace("\\", "/")));
				}
			}
		}
		if (this.getActionErrors().size() > 0) {
			QueryParams params = new QueryParams();
			params.setLoginUser(this.getLoginUser());

			result = new HashMap<String, Object>();
			result.put("serviceList", basicService.getServiceList(params).get("serviceList"));
			result.put("serviceMail", serviceMail);
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

	public ServiceMail getServiceMail() {
		return this.serviceMail;
	}

	public void setBasicService(BasicService basicService) {
		this.basicService = basicService;
	}

}
