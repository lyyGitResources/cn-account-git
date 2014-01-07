package com.hisupplier.cn.account.alert;

import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.ValidateCode;

public class AlertTradeStepActionTest extends ActionTestSupport {
	private String namespace = "/user";
	private TradeAlert alert = null;

	public void test_trade_alert_step1() throws Exception {
		String method = "trade_alert_step1";
		TradeAlertStepAction action = this.createAction(TradeAlertStepAction.class, namespace, method);
		alert = action.getModel();
		alert.setKeyword("test");
		this.execute(method, "success");
	}

	/**
	 * �ѵ�¼
	 * @throws Exception
	 */
	//TODO �û���̨-��ɽ������δͨ��
	public void test_trade_alert_step2_login() throws Exception {
		String method = "trade_alert_step2";
		request.setAttribute("com.hisupplier.cas.login", true);
		TradeAlertStepAction action = this.createAction(TradeAlertStepAction.class, namespace, method);
		this.setValidateToken();

		alert = action.getModel();
		alert.setMode("keyword");
		alert.setKeyword(this.getRandom() + "_test1");
		alert.setProduct(true);
		alert.setEmail("rd8@hi.cc");
		alert.setPasswd("000000");
		this.execute(method, "success");
	}

	/**
	 * δ��¼
	 * @throws Exception
	 */
	//TODO �û���̨-��ɽ������δͨ��
	public void test_trade_alert_step2_noLogin() throws Exception {
		request.setAttribute("com.hisupplier.cas.login", false);
		String method = "trade_alert_step2";
		TradeAlertStepAction action = this.createAction(TradeAlertStepAction.class, namespace, method);
		this.setValidateToken();

		alert = action.getModel();
		alert.setMode("keyword");
		alert.setKeyword(this.getRandom() + "_test2");
		alert.setProduct(true);
		alert.setEmail("rd8@hi.cc");
		alert.setPasswd("haibo2.2");
		this.execute(method, "success");
	}

	/**
	 * �ǻ�Ա
	 * @throws Exception
	 */
	public void test_trade_alert_step3() throws Exception {
		request.setAttribute("com.hisupplier.cas.login", false);
		String method = "trade_alert_step3";
		TradeAlertStepAction action = this.createAction(TradeAlertStepAction.class, namespace, method);
		this.setValidateToken();

		alert = action.getModel();
		alert.setMode("keyword");
		alert.setKeyword(this.getRandom() + "_test3");
		alert.setProduct(true);
		alert.setEmail(this.getRandom() + "@hi.cc");
		alert.setPasswd("000000");
		alert.setConfirmPasswd("000000");
		alert.setComName(this.getRandom() + "���Լ��׶���ע��");
		alert.setContact("abcd");
		alert.setTown("106105101102");
		alert.setTel1("1234");
		alert.setTel2("88888888");

		ValidateCode.setCode(request, "12345", "abcde");
		action.getModel().setValidateCodeKey("12345");
		action.getModel().setValidateCode("abcde");
		this.execute(method, "success");
	}

	private String getRandom() {
		DateUtil dateUtil = new DateUtil();
		return dateUtil.getDate2() + dateUtil.getTime2();
	}
}
