package com.hisupplier.cn.account.inquiry;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.inquiry.InquiryAction;
import com.hisupplier.cn.account.inquiry.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.cn.Inquiry;

public class InquiryActionTest extends ActionTestSupport {
	private InquiryAction action = null;
	private String namespace = "/inquiry";
	private QueryParams params = null;

	//按账号搜索
	public void test_inquiry_list_memberId() throws Exception {
		String method = "inquiry_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		//params.setPageSize(1);
		params.setUserId(7966);
		params.setRead(0);

		this.execute(method, "success");
	}

	//按询盘主题搜索
	public void test_inquiry_list_subject() throws Exception {
		String method = "inquiry_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setQueryBy("subject");
		params.setQueryText("中国海商网服务开通通知");
		params.setRead(0);

		this.execute(method, "success");
	}

	//按发件人搜索
	public void test_inquiry_list_fromName() throws Exception {
		String method = "inquiry_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setQueryBy("fromName");
		params.setQueryText("Anne");
		params.setRead(1);

		this.execute(method, "success");
	}

	//按省份搜索
	public void test_inquiry_list_fromProvince() throws Exception {
		String method = "inquiry_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setCountryCode("103101");
		params.setRead(1);

		this.execute(method, "success");
	}

	public void test_inquiry_view() throws Exception {
		String method = "inquiry_view";

		// 询盘ID正确
		Inquiry inquiry = this.getInquiry(CN.STATE_PASS);
		if (inquiry != null) {
			action = createAction(InquiryAction.class, namespace, method);
			params = action.getModel();
			params.setPageSize(1);
			//params.setComId(inquiry.getComId());//由其他地方得到
			int inqId[] = {inquiry.getInqId()};
			params.setInqId(inqId);
			this.execute(method, "success");
		}

		action = createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		// 询盘ID不存在
		//params.setComId(6125);
		int inqId1[] = {0};
		params.setInqId(inqId1);
		this.execute(method, "pageNotFound");
	}

	/**
	 * 注意询盘回复ID写死，有可能出错
	 * @throws Exception
	 */
/*	public void test_inquiry_reply_view() throws Exception {
		String method = "inquiry_reply_view";

		
		Inquiry inquiry = this.getInquiry(CN.STATE_PASS);
		if (inquiry != null) {
			
		}
		// 询盘回复ID存在
		action = createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		int inqId[] = {2};
		params.setInqId(inqId);
		params.setId(1);
		this.execute(method, "success");

		action = createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		// 询盘回复ID不存在
		int inqId1[] = {0};
		params.setInqId(inqId1);
		params.setId(0);
		this.execute(method, "pageNotFound");
	}*/

	//此方法同test_inquiry_view
	public void test_inquiry_reply_add() throws Exception {
		String method = "inquiry_reply_add";
		// 询盘ID正确
		Inquiry inquiry = this.getInquiry(CN.STATE_PASS);
		if (inquiry != null) {
			action = createAction(InquiryAction.class, namespace, method);
			params = action.getModel();
			params.setPageSize(1);
			//params.setComId(inquiry.getComId());//由其他地方得到
			int inqId[] = {inquiry.getInqId()};
			params.setInqId(inqId);
			this.execute(method, "success");
		}

		action = createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		// 询盘ID不存在
		//params.setComId(0);
		int inqId[] = {0};
		params.setInqId(inqId);
		this.execute(method, "pageNotFound");
	}

	public void test_inquiry_delete() throws Exception {
		String method = "inquiry_delete";

		Inquiry inquiry = this.getInquiry(CN.STATE_PASS);
		if (inquiry != null) {
			int inqIds[] = { inquiry.getInqId() };
			action = createAction(InquiryAction.class, namespace, method);
			params = action.getModel();
			params.setInqId(inqIds);
			this.execute(method, "success");
		}
	}

	public void test_inquiry_chart_memberId() throws Exception {
		String method = "inquiry_chart";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		this.execute(method, "success");
	}

	public void test_inquiry_download_fromName() throws Exception {
		String method = "inquiry_download";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setState(CN.STATE_PASS);
		params.setQueryBy("fromName");
		params.setQueryText("Anne");
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);

		this.execute(method, "success");
	}

	public void test_inquiry_download_fromProvince() throws Exception {
		String method = "inquiry_download";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setState(CN.STATE_PASS);
		params.setCountryCode("103101");
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);

		this.execute(method, "success");
	}

	public void test_inquiry_set() throws Exception {
		String method = "inquiry_set";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setInquiryReceive(1);
		params.setReceiveRecommend(1);

		this.execute(method, "success");
	}

	//询盘回收站按账号搜索
	public void test_inquiry_recycle_list_memberId() throws Exception {
		String method = "inquiry_recycle_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setUserId(17746);
		params.setRead(0);
		params.setState(CN.STATE_RECYCLE);

		this.execute(method, "success");
	}

	//询盘回收站按询盘主题搜索
	public void test_inquiry_recycle_list_subject() throws Exception {
		String method = "inquiry_recycle_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setQueryBy("subject");
		params.setQueryText("");
		params.setRead(0);
		params.setState(CN.STATE_RECYCLE);

		this.execute(method, "success");
	}

	//询盘回收站按发件人搜索
	public void test_inquiry_recycle_list_fromName() throws Exception {
		String method = "inquiry_recycle_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setQueryBy("fromName");
		params.setQueryText("");
		params.setRead(1);
		params.setState(CN.STATE_RECYCLE);

		this.execute(method, "success");
	}

	//询盘回收站按省份搜索
	public void test_inquiry_recycle_list_fromProvince() throws Exception {
		String method = "inquiry_recycle_list";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		params.setPageSize(1);
		params.setCountryCode("103101");
		params.setRead(1);
		params.setState(CN.STATE_RECYCLE);

		this.execute(method, "success");
	}

	public void test_inquiry_recycle_reuse() throws Exception {
		//向回收站添一条数据
		this.test_inquiry_delete();
		String method = "inquiry_recycle_reuse";

		Inquiry inquiry = this.getInquiryRecycle();
		if (inquiry != null) {
			int inqIds[] = { inquiry.getInqId() };
			action = this.createAction(InquiryAction.class, namespace, method);
			params = action.getModel();
			LoginUser loginUser = new LoginUser();
			loginUser.setComId(442);
			params.setLoginUser(loginUser);
			params.setInqId(inqIds);

			this.execute(method, "success");
		}
	}

	public void test_inquiry_recycle_delete() throws Exception {
		String method = "inquiry_recycle_delete";

		Inquiry inquiry = this.getInquiryRecycle();
		if (inquiry != null) {
			int inqIds[] = { inquiry.getInqId() };
			action = this.createAction(InquiryAction.class, namespace, method);
			params = action.getModel();
			LoginUser loginUser = new LoginUser();
			loginUser.setComId(442);
			params.setLoginUser(loginUser);
			params.setInqId(inqIds);

			this.execute(method, "success");
		}
	}

	public void test_inquiry_recycle_empty() throws Exception {
		String method = "inquiry_recycle_empty";

		action = this.createAction(InquiryAction.class, namespace, method);
		params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);

		this.execute(method, "success");
	}
}
