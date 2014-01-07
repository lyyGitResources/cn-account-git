package com.hisupplier.cn.account.basic;

import com.hisupplier.cn.account.basic.HomeAction;
import com.hisupplier.cn.account.basic.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

@SuppressWarnings("unused")
public class HomeActionTest extends ActionTestSupport {
	private String namespace = "/basic";
	private QueryParams params = null;

	public void test_home() throws Exception {
		String method = "home";
		HomeAction action = this.createAction(HomeAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_user_log_list() throws Exception {
		String method = "user_log_list";
		HomeAction action = this.createAction(HomeAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_admin_log() throws Exception {
		String method = "admin_log";
		HomeAction action = this.createAction(HomeAction.class, namespace, method);
		params = action.getModel();
		params.setTableName("company");
		params.setTableId(442);
		params.setOperate(4);
		this.execute(method, "pageNotFound");
	}

	public void test_inquiry_unread_count() throws Exception {
		String method = "inquiry_unread_count";
		HomeAction action = this.createAction(HomeAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_user_suggest() throws Exception {
		String method = "user_suggest";
		HomeAction action = this.createAction(HomeAction.class, namespace, method);
		this.execute(method, "success");
	}
}
