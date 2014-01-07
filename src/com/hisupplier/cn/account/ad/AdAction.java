package com.hisupplier.cn.account.ad;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.opensymphony.xwork2.ModelDriven;

public class AdAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = 8416000403688890665L;

	private AdService adService;

	private QueryParams params = new QueryParams();
	private Map<String, Object> result;

	public AdAction() {
		super();
		this.currentMenu = "ad";
	}

	public String upgrade() throws Exception {
		result = adService.getUpgrade(params);
		return SUCCESS;
	}

	public String ad_order() throws Exception {
		result = adService.getAdOrder(params);
		return SUCCESS;
	}

	public String ad_order_list() throws Exception {
		result = adService.getAdOrderList(params);
		return SUCCESS;
	}

	public String top_list() throws Exception {
		result = adService.getTopList(params);
		return SUCCESS;
	}

	public String top_edit() throws Exception {
		result = adService.getTop(request, params);
		return SUCCESS;
	}

	public String top_order() throws Exception {
		result = adService.getTopOrder(params);
		return SUCCESS;
	}

	public String top_order_list() throws Exception {
		result = adService.getTopOrderList(params);
		return SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	public void setParams(QueryParams params) {
		this.params = params;
	}

}
