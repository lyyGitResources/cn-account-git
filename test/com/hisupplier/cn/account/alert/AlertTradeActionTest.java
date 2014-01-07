package com.hisupplier.cn.account.alert;

import java.sql.Date;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;

import com.hisupplier.cn.account.alert.QueryParams;
import com.hisupplier.cn.account.alert.TradeAlertAction;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;

@SuppressWarnings("unused")
public class AlertTradeActionTest extends ActionTestSupport {
	private String namespace = "/alert";
	private QueryParams params = null;

	public void test_trade_alert_list() throws Exception {
		String method = "trade_alert_list";
		TradeAlertAction action = this.createAction(TradeAlertAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}

	public void test_trade_alert_add() throws Exception {
		String method = "trade_alert_add";
		int id = this.getTradeAlert().getId();
		TradeAlertAction action = this.createAction(TradeAlertAction.class, namespace, method);
		params = action.getModel();
		params.setId(id);
		this.execute(method, "success");		
	}
	
	public void test_trade_alert_edit() throws Exception {
		String method = "trade_alert_edit";
		TradeAlert alert = this.getTradeAlert();
		if (alert != null) {
			TradeAlertAction action = this.createAction(TradeAlertAction.class, namespace, method);
			params = action.getModel();
			params.setId(alert.getId());
			this.execute(method, "success");
		}
	}

	public void test_trade_alert_delete() throws Exception {
			TradeAlert tradeAlert = new TradeAlert();
			tradeAlert = this.getTradeAlert();
			TradeAlertAction action = this.createAction(TradeAlertAction.class, namespace, "trade_alert_delete");
			params = action.getModel();
			params.setId(tradeAlert.getId());
			this.execute("trade_alert_delete", "success");
		}
	
	public void test_trade_alert_enable() throws Exception {
		String method = "trade_alert_enable";
		
		TradeAlert tradeAlert = this.getTradeAlert();
		if (tradeAlert == null) {
			assertTrue(false);
			return;
		}
		TradeAlertAction action = this.createAction(TradeAlertAction.class, namespace, method);
		params = action.getModel();
		params.setId(tradeAlert.getId());
		params.setEnable(false);
		this.execute(method, "success");
	}
}
