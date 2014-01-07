package com.hisupplier.cn.account.inquiry;

import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.cn.account.inquiry.InquiryReplyAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class InquiryReplyActionTest extends ActionTestSupport {
	private InquiryReply inquiryReply = new InquiryReply();

	public void test_inquiry_reply_submit() throws Exception {
		String method = "inquiry_reply_add_submit";
		InquiryReplyAction action = this.createAction(InquiryReplyAction.class, "/inquiry", method);

		this.setValidateToken();
		
		inquiryReply.setInqId(2);
		inquiryReply.setComId(6125);
		inquiryReply.setUserId(6105);
		inquiryReply.setToName("chenzhong");
		inquiryReply.setToEmail("yaozhan189@163.com");
		inquiryReply.setSubject("test");
		inquiryReply.setContent("have a good day!");
		inquiryReply.setFilePath("");
		inquiryReply.setFromContent("Ô­ÓÊ¼þÄÚÈÝ");
		action.setInquiryReply(inquiryReply);

		this.execute(method, "success");
	}
}
