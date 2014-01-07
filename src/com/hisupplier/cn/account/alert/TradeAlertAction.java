/* 
 * Created by gaoshan at Nov 12, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.alert;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author gaoshan
 *
 */
public class TradeAlertAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -1773203396038523377L;
	private QueryParams params = new QueryParams();
	private TradeAlertService tradeAlertService;
	private Map<String, Object> result;

	public String trade_alert_list() throws Exception {
		result = tradeAlertService.getTradeAlertList(params);
		return SUCCESS;
	}

	public String trade_alert_add() throws Exception {
		TradeAlert tradeAlert = new TradeAlert();
		if (StringUtil.isNotEmpty(WebUtil.getParam(request, "mode"))) {
			tradeAlert.setMode(WebUtil.getParam(request, "mode"));
			tradeAlert.setKeyword(WebUtil.getParam(request, "keyword"));
			tradeAlert.setCatIds(WebUtil.getParam(request, "catIds"));
		} else {
			tradeAlert.setMode("keyword");
		}
		tradeAlert.setProduct(true);
		result = new HashMap<String, Object>();
		result.put("tradeAlert", tradeAlert);
		return SUCCESS;
	}

	public String trade_alert_edit() throws Exception {
		result = tradeAlertService.getTradeAlert(params);
		return SUCCESS;
	}

	public String trade_alert_delete() throws Exception {
		tip = tradeAlertService.deleteTradeAlert(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("deleteSuccess");
		} else {
			this.addActionError(getText("operateFail"));
		}
		return SUCCESS;
	}

	public String trade_alert_enable() throws Exception {
		tip = tradeAlertService.updateTradeAlertEnable(params);
		if (tip.equals("editSuccess")) {
			if (params.isEnable()) {
				this.addMessage("alert.reuse");
			} else {
				this.addMessage("alert.cancel");
			}
		} else {
			this.addActionError(getText("operateFail"));
		}
		return SUCCESS;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setTradeAlertService(TradeAlertService tradeAlertService) {
		this.tradeAlertService = tradeAlertService;
	}
}
