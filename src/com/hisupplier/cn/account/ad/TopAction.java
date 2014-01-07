package com.hisupplier.cn.account.ad;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Top;
import com.opensymphony.xwork2.ModelDriven;

public class TopAction extends BasicAction implements ModelDriven<Top> {
	private static final long serialVersionUID = 8416000403688890665L;

	private AdService adService;
	private Top top = new Top();
	private Map<String, Object> result;

	public String top_edit_submit() throws Exception {
		tip = adService.updateTop(response, top);
		this.addMessage(getText(tip));
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

	public Top getModel() {
		return top;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	public void setTop(Top top) {
		this.top = top;
	}

}
