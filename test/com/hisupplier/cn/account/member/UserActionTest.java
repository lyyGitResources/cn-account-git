package com.hisupplier.cn.account.member;

import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.QueryParams;
import com.hisupplier.cn.account.member.UserAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class UserActionTest extends ActionTestSupport {
	private String namespace = "/member";
	private UserAction action = null;

	public void test_contact_edit() throws Exception {
		String method = "contact_edit";
		action = this.createAction(UserAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_passwd_edit() throws Exception {
		String method = "passwd_edit";
		action = this.createAction(UserAction.class, namespace, method);
		this.execute(method, "success");
	}

	/**
	 * 子帐号已达数量限制则能通不过
	 * @throws Exception
	 */
	public void test_user_add() throws Exception {
		String method = "user_add";
		String result = "success";
		action = this.createAction(UserAction.class, namespace, method);
		this.execute(method, result);
	}

	public void test_user_edit() throws Exception {
		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}
		String method = "user_edit";
		action = this.createAction(UserAction.class, namespace, method);
		QueryParams params = action.getModel();
		params.setUserId(user.getUserId());
		this.execute(method, "success");
	}

	public void test_user_list() throws Exception {
		String method = "user_list";
		action = this.createAction(UserAction.class, namespace, method);
		this.execute(method, "success");
	}

	/**
	 * 当这个用户在trade表有数据的时候删除不了
	 * @throws Exception
	 */
	public void test_user_delete() throws Exception {
		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}
		String method = "user_delete";
		action = this.createAction(UserAction.class, namespace, method);
		QueryParams params = action.getModel();
		params.setUserId(user.getUserId());
		this.execute(method, "success");
	}
}
