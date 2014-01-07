package com.hisupplier.cn.account.member;

import java.util.Random;

import org.apache.commons.beanutils.PropertyUtils;

import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;

public class UserFormActionTest extends ActionTestSupport {
	private String namespace = "/member";

	public void test_user_add_submit() throws Exception {

		String method = "user_add_submit";
		User u = this.getContactEdit();
		if (u != null) {
			UserFormAction action = this.createAction(UserFormAction.class, namespace, method);
			this.setValidateToken();
			User user = action.getModel();
			PropertyUtils.copyProperties(user, u);
			user.setHeadImgPath("aa.jpg");
			user.setContact("陈先生");
			user.setPrivilege("/ad/");
			user.setSex(1);
			user.setProvince("101101");
			user.setCity("101101101");
			user.setTown("101101101101");
			user.setStreet("aa");
			user.setZip("");
			user.setDepartment("经理");
			user.setJob("经理");
			user.setEmail(new Random().nextInt(1000) + "@qq.com");
			user.setTel1("0574");
			user.setTel2("12345678");
			user.setFax1("0574");
			user.setFax2("12345678");
			user.setMobile("13912345678");
			user.setSms(true);
			user.setShowMobile(true);
			user.setQq("");
			user.setMsn("");
			user.setSkype("");
			user.setShow(true);

			user.setPasswd("000000");
			user.setPreLoginIP("127.0.0.1");
			user.setLastLoginIP("127.0.0.1");
			user.setLoginTimes(1);
			user.setCreateTime(new DateUtil().getDateTime());
			user.setModifyTime(new DateUtil().getDateTime());
			this.execute(method, "success");
		}
	}

	public void test_user_edit_submit() throws Exception {
		String method = "user_edit_submit";
		User u = this.getUserEdit();
		if (u != null) {
			UserFormAction action = this.createAction(UserFormAction.class, namespace, method);
			this.setValidateToken();
			User user = action.getModel();
			PropertyUtils.copyProperties(user, u);
			user.setHeadImgPath("aa.jpg");
			user.setContact("陈先生");
			user.setSex(1);
			user.setProvince("1");
			user.setCity("1");
			user.setTown("1");
			user.setStreet("aa");
			user.setZip("");
			user.setDepartment("经理");
			user.setJob("经理");
			user.setEmail(new Random().nextInt(1000) + "@qq.com");
			user.setTel1("0574");
			user.setTel2("12345678");
			user.setFax1("0574");
			user.setFax2("12345678");
			user.setMobile("13912345678");
			user.setSms(true);
			user.setShowMobile(true);
			user.setQq("");
			user.setMsn("");
			user.setSkype("");
			user.setShow(true);
			this.execute(method, "success");
		}
	}

	public void test_passwd_edit_submit() throws Exception {
		String method = "passwd_edit_submit";
		User u = this.getUserEdit();
		if (u != null) {
			PasswdFormAction action = this.createAction(PasswdFormAction.class, namespace, method);
			this.setValidateToken();
			User user = action.getModel();
			PropertyUtils.copyProperties(user, u);
			user.setPasswd("aaabbb");
			user.setConfirmPasswd("aaabbb");
			user.setOldPasswd(u.getPasswd());
			this.execute(method, "success");
		}
	}

	public void test_contact_edit_submit() throws Exception {
		String method = "contact_edit_submit";
		User u = this.getContactEdit();

		UserFormAction action = this.createAction(UserFormAction.class, namespace, method);
		this.setValidateToken();
		User user = action.getModel();
		PropertyUtils.copyProperties(user, u);
		user.setHeadImgPath("aa.jpg");
		user.setContact("陈先生");
		user.setSex(1);
		user.setProvince("1");
		user.setCity("1");
		user.setTown("1");
		user.setStreet("aa");
		user.setZip("");
		user.setDepartment("经理");
		user.setJob("经理");
		user.setEmail("guiyou@qq.com");
		user.setTel1("0574");
		user.setTel2("12345678");
		user.setFax1("0574");
		user.setFax2("12345678");
		user.setMobile("13912345678");
		user.setSms(true);
		user.setShowMobile(true);
		user.setQq("");
		user.setMsn("");
		user.setSkype("");
		this.execute(method, "success");
	}
}
