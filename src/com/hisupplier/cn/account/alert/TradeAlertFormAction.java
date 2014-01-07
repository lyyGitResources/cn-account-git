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
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author gaoshan
 *
 */
public class TradeAlertFormAction extends BasicAction implements ModelDriven<TradeAlert> {

	private static final long serialVersionUID = 8552246735062029659L;
	private TradeAlert tradeAlert = new TradeAlert();
	private TradeAlertService tradeAlertService;
	private Map<String, Object> result;
	
	public TradeAlertFormAction() {
		super();
		this.currentMenu = "alert";
	}
	
	public String trade_alert_add_submit() throws Exception {
		String fields = "keyword,catIds";
		StringUtil.trimToEmpty(tradeAlert, fields);
		tip = tradeAlertService.addTradeAlert(tradeAlert);
		if (tip.equals("addSuccess")) {
			this.addMessage("alert.addSuccess");
		} else {
			result = new HashMap<String, Object>();
			result.put("tradeAlert", tradeAlert);
			this.addActionError(getText(tip));
			return "input";
		}
		return SUCCESS;
	}

	public String trade_alert_edit_submit() throws Exception {
		String fields = "keyword,catIds";
		StringUtil.trimToEmpty(tradeAlert, fields);
		tip = tradeAlertService.updateTradeAlert(tradeAlert);
		if (tip.equals("editSuccess")) {
			this.addMessage("alert.editSuccess");
		} else {
			result = new HashMap<String, Object>();
			result.put("tradeAlert", tradeAlert);
			this.addActionError(getText(tip));
			return "input";
		}
		return SUCCESS;
	}

	public void validate() {
//		if (tradeAlert.getCatIds() != null) {
//			tradeAlert.setCatIds(tradeAlert.getCatIds().replace(" ", ""));
//		}

		if (StringUtil.isEmpty(tradeAlert.getMode())) {
			this.addActionError(getText("alert.mode.required"));
		} else if (tradeAlert.getMode().equals("keyword")) {
			if (StringUtil.isEmpty(tradeAlert.getKeyword())) {
				this.addActionError(getText("alert.keyword.required"));
			} else {
				if (tradeAlert.getKeyword().length() > 60) {
					this.addActionError(getText("alert.keyword.required"));
				}
			}
		} else if (tradeAlert.getMode().equals("category")) {
			if (StringUtil.isEmpty(tradeAlert.getCatIds())) {
				this.addActionError(getText("alert.category.required"));
			}
		}

		if (!tradeAlert.isCompany() && !tradeAlert.isProduct() && !tradeAlert.isBuy()) {
			this.addActionError(getText("alert.content.required"));
		}
		if (this.hasActionErrors()) {
			result = new HashMap<String, Object>();
			result.put("tradeAlert", tradeAlert);
		}
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setTradeAlertService(TradeAlertService tradeAlertService) {
		this.tradeAlertService = tradeAlertService;
	}

	public TradeAlert getModel() {
		return tradeAlert;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}
}
