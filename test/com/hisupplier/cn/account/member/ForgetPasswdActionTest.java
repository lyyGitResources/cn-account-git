package com.hisupplier.cn.account.member;

import org.apache.commons.codec.binary.Base64;

import com.hisupplier.cn.account.member.ForgetPasswdAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.ValidateCode;

public class ForgetPasswdActionTest extends ActionTestSupport {
	private String namespace = "/user";
	private ForgetPasswdAction action = null;

	public void test_forget_passwd() throws Exception {
		String method = "forget_passwd_send";
		action = this.createAction(ForgetPasswdAction.class, namespace, method);
		this.setValidateToken();
		action.setEmail("guiyou");
		ValidateCode.setCode(request, "123", "abc");
		action.setValidateCodeKey("123");
		action.setValidateCode("abc");
		this.execute(method, "success");
	}

	public void test_forget_passwd_encode() throws Exception {
		String method = "forget_passwd_send";
		action = this.createAction(ForgetPasswdAction.class, namespace, method);
		this.setValidateToken();
		action.setEncode(new String(Base64.encodeBase64("guiyou".getBytes())));
		ValidateCode.setCode(request, "123", "abc");
		action.setValidateCodeKey("123");
		action.setValidateCode("abc");
		this.execute(method, "success");
	}
}
