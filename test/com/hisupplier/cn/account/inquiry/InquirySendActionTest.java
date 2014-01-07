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
	 * �ǻ�Ա��ѯ�̷���
	 * @throws Exception
	 */
	public void test_inquiry_send_member() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//����ģ������
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setNotLogin();
		this.setBasketItems();
		User user = this.getUser();

		Inquiry inquiry = action.getModel();
		inquiry.setNewUser(false); //����false��ʾ�ǻ�Ա
		inquiry.setSubject("���ԡ��ǻ�Ա��ѯ�̷���");
		inquiry.setContent("���ԡ��ǻ�Ա��ѯ�̷���\n\r����һ������");
		inquiry.setEmail(user.getEmail());
		inquiry.setPasswd(user.getPasswd());
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setValidateCodeKey("123");
		inquiry.setValidateCode("abc");
		this.execute(method, "success");
	}

	/**
	 * ���û���ѯ�̷���
	 * @throws Exception
	 */
	public void test_inquiry_send_newUser() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//����ģ������
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setNotLogin();
		this.setBasketItems();

		Inquiry inquiry = action.getModel();
		inquiry.setNewUser(true);
		inquiry.setSubject("���ԡ����û���ѯ�̷���");
		inquiry.setContent("���ԡ����û���ѯ�̷���\n\r����һ������");
		inquiry.setFromEmail(this.getRandom() + "@163.com");
		inquiry.setFromCompany(this.getRandom() + "����ѯ�̷���ע��");
		inquiry.setFromName(this.getRandom() + "�������");
		inquiry.setFromProvince("103105"); //����ʡ
		inquiry.setFromCity("103105101"); //����
		inquiry.setFromTown("103105101102"); //̨����
		inquiry.setTel1("0574");
		inquiry.setTel2("27886899");
		inquiry.setFax1("0574");
		inquiry.setFax2("27883899");
		inquiry.setFromStreet("۴�ش��1357��");
		inquiry.setFromWebsite("http://www.hisupplier.com");
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setSex(1);
		inquiry.setValidateCodeKey("123");
		inquiry.setValidateCode("abc");
		this.execute(method, "success");
	}

	/**
	 * ��¼�û���ѯ�̷���
	 * @throws Exception
	 */
	public void test_inquiry_send_loginUser() throws Exception {
		String method = "inquiry_send";
		InquirySendAction action = this.createAction(InquirySendAction.class, "/user", method);

		//����ģ������
		ValidateCode.setCode(request, "123", "abc");
		this.setValidateToken();
		this.setLogin();
		this.setBasketItems();

		Inquiry inquiry = action.getModel();
		inquiry.setSubject("���ԡ���¼�û���ѯ�̷���");
		inquiry.setContent("���ԡ���¼�û���ѯ�̷���\n\r����һ������");
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
