package com.hisupplier.cn.account.alert;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;

import com.hisupplier.cn.account.alert.TradeAlertFormAction;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;

public class AlertTradeFormActionTest extends ActionTestSupport {
	private String namespace = "/alert";
	private TradeAlert tradeAlert = null;

	@SuppressWarnings("unchecked")
	public void test_trade_alert_add_submit() throws Exception {
		String method = "trade_alert_add_submit";
		TradeAlertFormAction formAction = this.createAction(TradeAlertFormAction.class, namespace, method);
		String token = TokenHelper.generateGUID();
		ServletActionContext.getContext().getParameters().put(TokenHelper.TOKEN_NAME_FIELD, new String[] { TokenHelper.DEFAULT_TOKEN_NAME });
		ServletActionContext.getContext().getParameters().put(TokenHelper.DEFAULT_TOKEN_NAME, new String[] { token });
		ServletActionContext.getContext().getSession().put(TokenHelper.DEFAULT_TOKEN_NAME, token);
		
		TradeAlert	tradeAlert = formAction.getModel();
		tradeAlert.setComId(442);
		tradeAlert.setUserId(758);
		tradeAlert.setKeyword(new DateUtil().getTime() + "²âÊÔÌí¼ÓºÍÉ¾³ý");
		tradeAlert.setCatIds("1,2,3");
		tradeAlert.setMode("keyword");
		tradeAlert.setProduct(true);
		this.execute(method, "success");
	}
	
	@SuppressWarnings("unchecked")
	public void test_trade_alert_edit_submit() throws Exception {
		String method = "trade_alert_edit_submit";
		int id = this.getTradeAlert().getId();
		TradeAlertFormAction action = this.createAction(TradeAlertFormAction.class, namespace, method);
		String token = TokenHelper.generateGUID();
		ServletActionContext.getContext().getParameters().put(TokenHelper.TOKEN_NAME_FIELD, new String[] { TokenHelper.DEFAULT_TOKEN_NAME });
		ServletActionContext.getContext().getParameters().put(TokenHelper.DEFAULT_TOKEN_NAME, new String[] { token });
		ServletActionContext.getContext().getSession().put(TokenHelper.DEFAULT_TOKEN_NAME, token);
		
		tradeAlert = action.getModel();
		tradeAlert.setComId(442);
		tradeAlert.setUserId(758);
		tradeAlert.setKeyword(new DateUtil().getDateTime() + "²âÊÔÐÞ¸Ä");
		tradeAlert.setCatIds("2");
		tradeAlert.setId(id);
		tradeAlert.setMode("keyword");
		tradeAlert.setBuy(true);
		tradeAlert.setProduct(true);
		tradeAlert.setCompany(true);
		this.execute(method, "success");
	}
	
	/**
	 * ÒÑ×¢²á
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void test_subscibe_register() throws Exception {
		TradeAlertService service = (TradeAlertService)this.applicationContext.getBean("tradeAlertService");
		TradeAlert alert = new TradeAlert();
		alert.setEmail("acat2009@163.com");
		alert.setKeyword("admin");
		alert.setCatIds("1,2");
		alert.setCompany(true);
		service.subscibe(alert);
	}

	/**
	 * Î´×¢²á
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void test_subscibe_noRegister() throws Exception {
		TradeAlertService service = (TradeAlertService)this.applicationContext.getBean("tradeAlertService");
		TradeAlert alert = new TradeAlert();
		alert.setComName("admin_comName"+new DateUtil().getTime4());
		alert.setEmail("acat2009@"+new DateUtil().getTime4()+".com");
		alert.setKeyword("admin_noregister");
		alert.setProvince("101101");
		alert.setCity("101101101");
		alert.setTown("101101101101");
		alert.setContact("admin");
		alert.setCatIds("1,2");
		alert.setTel("0574-88888888");
		alert.setSex(2);
		alert.setCompany(true);
		service.subscibe(alert);
	}
}
