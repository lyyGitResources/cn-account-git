package com.hisupplier.cn.account.basic;

import java.util.concurrent.TimeUnit;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;

import com.hisupplier.cn.account.basic.ServiceFormAction;
import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class ServirceFormActionTest extends ActionTestSupport {
	ServiceMail serviceMail = null;

	@SuppressWarnings("unchecked")
	public void test_service_mail_send() throws Exception {
		String method = "service_mail_send";

		ServiceFormAction action = this.createAction(ServiceFormAction.class, "/basic", method);

		String token = TokenHelper.generateGUID();
		ServletActionContext.getContext().getParameters().put(TokenHelper.TOKEN_NAME_FIELD, new String[] { TokenHelper.DEFAULT_TOKEN_NAME });
		ServletActionContext.getContext().getParameters().put(TokenHelper.DEFAULT_TOKEN_NAME, new String[] { token });
		ServletActionContext.getContext().getSession().put(TokenHelper.DEFAULT_TOKEN_NAME, token);

		serviceMail = action.getModel();
		serviceMail.setToEmail("acat2009@163.com");
		serviceMail.setToName("adsa");
		serviceMail.setComName("asd");
		serviceMail.setContact("asd");
		serviceMail.setContactMode("asdadasd");
		serviceMail.setSubject("asd");
		serviceMail.setContent("asdasd");
		serviceMail.setEmail("asdads");
		serviceMail.setTel("asdasd");
		serviceMail.setFax("asdads");
		serviceMail.setMemberId("asdads");
		this.execute(method, "success");

		TimeUnit.SECONDS.sleep(5);
	}
}
