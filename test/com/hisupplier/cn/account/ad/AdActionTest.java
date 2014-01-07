package com.hisupplier.cn.account.ad;

import com.hisupplier.cn.account.ad.AdAction;
import com.hisupplier.cn.account.ad.QueryParams;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class AdActionTest extends ActionTestSupport {

	//会员升级
	public void test_upgrade() throws Exception {
		QueryParams params = new QueryParams();
		String method = "upgrade";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		loginUser.setUserId(758);
		params.setLoginUser(loginUser);
		action.setParams(params);

		this.execute(method, "success");
	}

	public void test_ad_order_list() throws Exception {
		QueryParams params = new QueryParams();
		String method = "ad_order_list";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		params.setPageSize(1);
		action.setParams(params);

		this.execute(method, "success");
	}

	//广告申请
	public void test_ad_order() throws Exception {
		QueryParams params = new QueryParams();
		String method = "ad_order";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		loginUser.setUserId(758);
		params.setLoginUser(loginUser);
		action.setParams(params);

		this.execute(method, "success");
	}

	public void test_top_list() throws Exception {
		QueryParams params = new QueryParams();
		String method = "top_list";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		LoginUser loginUser = new LoginUser();
		loginUser.setComId(5170);
		params.setLoginUser(loginUser);
		params.setPageSize(15);
		action.setParams(params);

		this.execute(method, "success");
	}

	//Topsite服务
	public void test_top_edit() throws Exception {
		QueryParams params = new QueryParams();
		String method = "top_edit";

		AdAction action = this.createAction(AdAction.class, "/ad", method);
		params.setTopId(1);
		action.setParams(params);

		this.execute(method, "success");
	}

	public void test_top_order_list() throws Exception {
		QueryParams params = new QueryParams();
		String method = "top_order_list";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		action.setParams(params);

		this.execute(method, "success");
	}

	//Topsite服务
	public void test_top_order() throws Exception {
		QueryParams params = new QueryParams();
		String method = "top_order";

		AdAction action = this.createAction(AdAction.class, "/ad", method);

		LoginUser loginUser = new LoginUser();
		loginUser.setComId(5170);
		loginUser.setUserId(758);
		params.setLoginUser(loginUser);
		action.setParams(params);

		this.execute(method, "success");
	}
}
