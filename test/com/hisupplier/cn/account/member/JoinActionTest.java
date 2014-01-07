package com.hisupplier.cn.account.member;

import java.util.Random;

import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.member.JoinAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.ValidateCode;

public class JoinActionTest extends ActionTestSupport {
	private String namespace = "/user";

	public void test_join_submit() throws Exception {
		String method = "join_submit";

		JoinAction action = this.createAction(JoinAction.class, namespace, method);
		this.setValidateToken();
		Register register = action.getModel();
		register.setTown("10101101101");
		register.setProvince("101101");
		register.setCity("10101101");
		register.setEmail(new Random().nextInt(1000) + "@qq.com");
		register.setMemberId("test" + new Random().nextInt(1000));
		register.setPasswd("111111");
		register.setConfirmPasswd("111111");
		register.setComName("Äþ²¨²âÊÔ×¢²áÕÊºÅ" + new Random().nextInt(1000));
		register.setContact("ÁªÏµÈËtest");
		register.setTel1("0574");
		register.setTel2("12345678");
		ValidateCode.setCode(request, "123", "abcde");
		action.getModel().setValidateCodeKey("123");
		action.getModel().setValidateCode("abcde");

		this.execute(method, "success");
	}

}
