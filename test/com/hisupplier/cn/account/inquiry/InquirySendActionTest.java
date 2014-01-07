package com.hisupplier.cn.account.inquiry;

import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.inquiry.InquirySendAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.ValidateCode;

public class InquirySendActionTest extends ActionTestSupport {

	@Override
	public void tearDown() throws Exception {
		java.util.concurrent.TimeUnit.SECONDS.sleep(3);
	}

	/**
	 * 是会员，询盘发送
	 * @throws Exception
	 */
	public void test_inquiry_send_member() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//设置模拟数据
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setNotLogin();
		this.setBasketItems();
		User user = this.getUser();

		Inquiry inquiry = action.getModel();
		inquiry.setNewUser(false); //设置false表示是会员
		inquiry.setSubject("测试《是会员》询盘发送");
		inquiry.setContent("测试《是会员》询盘发送\n\r这里一个换行");
		inquiry.setEmail(user.getEmail());
		inquiry.setPasswd(user.getPasswd());
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setValidateCodeKey("123");
		inquiry.setValidateCode("abc");
		this.execute(method, "success");
	}

	/**
	 * 新用户，询盘发送
	 * @throws Exception
	 */
	public void test_inquiry_send_newUser() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//设置模拟数据
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setNotLogin();
		this.setBasketItems();

		Inquiry inquiry = action.getModel();
		inquiry.setNewUser(true);
		inquiry.setSubject("测试《新用户》询盘发送");
		inquiry.setContent("测试《新用户》询盘发送\n\r这里一个换行");
		inquiry.setFromEmail(this.getRandom() + "@163.com");
		inquiry.setFromCompany(this.getRandom() + "测试询盘发送注册");
		inquiry.setFromName(this.getRandom() + "随机生成");
		inquiry.setFromProvince("103105"); //福建省
		inquiry.setFromCity("103105101"); //福州
		inquiry.setFromTown("103105101102"); //台江区
		inquiry.setTel1("0574");
		inquiry.setTel2("27886899");
		inquiry.setFax1("0574");
		inquiry.setFax2("27883899");
		inquiry.setFromStreet("鄞县大道1357号");
		inquiry.setFromWebsite("http://www.hisupplier.com");
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setSex(1);
		inquiry.setValidateCodeKey("123");
		inquiry.setValidateCode("abc");
		this.execute(method, "success");
	}

	/**
	 * 登录用户，询盘发送
	 * @throws Exception
	 */
	public void test_inquiry_send_loginUser() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//设置模拟数据
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setLogin();
		this.setBasketItems();

		Inquiry inquiry = action.getModel();
		inquiry.setSubject("测试《登录用户》询盘发送");
		inquiry.setContent("测试《登录用户》询盘发送\n\r这里一个换行");
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setValidateCodeKey("123");
		inquiry.setValidateCode("abc");
		this.execute(method, "success");
	}

	private String getRandom() {
		DateUtil dateUtil = new DateUtil();
		return dateUtil.getDate2() + dateUtil.getTime2();
	}

}
