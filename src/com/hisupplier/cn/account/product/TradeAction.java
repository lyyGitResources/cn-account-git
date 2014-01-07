/* 
 * Created by baozhimin at Nov 23, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class TradeAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = 98418995792857575L;
	private QueryParams params = new QueryParams();
	private TradeService tradeService;
	private Map<String, Object> result;

	public String trade_list() throws Exception {
		result = this.tradeService.getTradeList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String trade_repost() throws Exception {
		throw new PageNotFoundException();
//		tip = this.tradeService.updateTradeRepost(params);
//		if (tip.equals("editSuccess")) {
//			this.addMessage("trade.repostSuccess");
//		} else {
//			this.addError(tip);
//		}
//		return SUCCESS;
	}
	public String trade_repostAll() throws Exception {
		throw new PageNotFoundException();
//		tip = this.tradeService.updateTradeRepostAll(params);
//		if (tip.equals("editSuccess")) {
//			this.addMessage("trade.repostSuccess");
//		} else {
//			this.addError(tip);
//		}
//		return SUCCESS;
	}
	
	public String trade_delete() throws Exception {
		tip = this.tradeService.deleteTrade(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("trade.deleteSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String trade_change_user() throws Exception {
		tip = this.tradeService.updateTradeUser(params);
		if (tip.equals("editSuccess")) {
			this.addMessage("trade.changeUserSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String trade_add() throws Exception {
		result = this.tradeService.getTradeAdd(request, params);
		if(result.containsKey("full")){
			this.addActionError(getText((String) result.get("full"), new String[]{(String) result.get("num")}));
			return MESSAGE;
		}
		return SUCCESS;
	}

	public String trade_edit() throws Exception {
		result = this.tradeService.getTradeEdit(request, params, true);
		return SUCCESS;
	}
	public String trade_detail() throws Exception {
		result = this.tradeService.getTradeEdit(request, params, false);
		return "select";
	}

	public String trade_recycle_list() throws Exception {
		result = this.tradeService.getTradeRecycleList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String trade_recycle_delete() throws Exception {
		tip = this.tradeService.updateRecycleDelete(params, false);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("trade.recycle.deleteSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String trade_recycle_empty() throws Exception {
		tip = this.tradeService.updateRecycleEmpty(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("trade.recycle.emptySuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}
	
	public String trade_success() throws Exception {
		result = this.tradeService.tradeSuccess(params);
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

	public void setTradeService(TradeService tradeService) {
		this.tradeService = tradeService;
	}

}
