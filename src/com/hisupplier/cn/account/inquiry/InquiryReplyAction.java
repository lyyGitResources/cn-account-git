package com.hisupplier.cn.account.inquiry;

import java.io.FileNotFoundException;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.cn.account.util.MailFactory;
import com.hisupplier.commons.mail.InquiryMailSenderFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class InquiryReplyAction extends BasicAction implements ModelDriven<InquiryReply> {

	private static final long serialVersionUID = -226630269953389742L;
	private InquiryService inquiryService;
	private InquiryReply inquiryReply = new InquiryReply();
	private Map<String, Object> result;

	public String inquiry_reply_add_submit() throws Exception {
		tip = inquiryService.addInquiryReply(inquiryReply);

		if (!StringUtil.equalsIgnoreCase(tip, "replaySuccess")) {
			QueryParams params = new QueryParams();
			params.setLoginUser(this.getLoginUser());
			int[] inqId = { this.getInquiryReply().getInqId() };
			params.setInqId(inqId);
			result = inquiryService.getInquiryView(params);
			this.addActionError(getText(tip));
			return INPUT;
		}
		
		//ÓÊ¼þ·¢ËÍ
//		MailSender emailSender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		MailSender emailSender = InquiryMailSenderFactory.createSender();
		Mail email = MailFactory.getInquiryRely(inquiryReply);
		emailSender.add(email);
		TaskExecutor.execute(emailSender);
		
		this.addMessage(getText(tip));
		return SUCCESS;
	}

	public void validateInquiry_reply_add_submit() throws FileNotFoundException {
		if (StringUtil.isEmpty(inquiryReply.getSubject())) {
			this.addActionError(this.getText("inquiryReply.subject.required"));
		}
		if (StringUtil.isEmpty(inquiryReply.getContent())) {
			this.addActionError(this.getText("serviceMail.content.required"));
		}else{
			inquiryReply.setContent(StringUtil.filterHyperlink(inquiryReply.getContent()));
		}
		if (inquiryReply.getUpload() != null) {
			for (int i = 0; i < inquiryReply.getUpload().length; i++) {
				if (!StringUtil.containsValue(ALLOW_MIME_TYPE, inquiryReply.getUploadContentType()[i]) || inquiryReply.getUpload()[i].length() / 1024 > 500) {
					this.addActionError(this.getText("attachment.invalid"));
				}
			}
		}

		if (this.getActionErrors().size() > 0) {
			QueryParams params = new QueryParams();
			params.setLoginUser(this.getLoginUser());
			int[] inqId = { this.getInquiryReply().getInqId() };
			params.setInqId(inqId);
			result = inquiryService.getInquiryView(params);
		}
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

	public InquiryReply getModel() {
		return inquiryReply;
	}

	public void setInquiryService(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	public void setInquiryReply(InquiryReply inquiryReply) {
		this.inquiryReply = inquiryReply;
	}

	public InquiryReply getInquiryReply() {
		return inquiryReply;
	}

}
