/* 
 * Created by sunhailin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class SpecialGroupAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -2533429138237803186L;
	private QueryParams params = new QueryParams();
	private SpecialGroupService specialGroupService;
	private Map<String, Object> result;

	public String group_list() throws Exception {
		result = this.specialGroupService.getGroupList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String select_group_list() throws Exception {
		result = this.specialGroupService.getSelectGroupList(params);
		return params.getOptimize() > 0 ? "product_success" : SUCCESS;
	}

	public String group_order_submit() throws Exception {
		tip = this.specialGroupService.updateGroupOrder(params);
		if (tip.equals("orderSuccess")) {
			this.addMessage("specialGroup.orderSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String group_add() throws Exception {
		result = this.specialGroupService.getSpecailGroupAdd(params);
		return SUCCESS;
	}

	public String group_edit() throws Exception {
		result = this.specialGroupService.getSpecailGroupEdit(params);
		return SUCCESS;
	}

	public String group_delete() throws Exception {
		tip = this.specialGroupService.deleteGroup(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("specialGroup.deleteSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String no_group_product_list() throws Exception {
		result = this.specialGroupService.getNoGroupProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String select_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("specialGroup.selectProduct");
		} else {
			tip = this.specialGroupService.selectProductGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("specialGroup.selectProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String group_product_list() throws Exception {
		result = this.specialGroupService.getGroupProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String remove_from_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("specialGroup.selectProduct");
		} else {
			tip = this.specialGroupService.removeFromProducGroup(params);
			if (tip.equals("deleteSuccess")) {
				this.addMessage(this.getText("specialGroup.removeFromProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else if (tip.equals("repeat")) {
				this.addError("specialGroup.selectRepeatProduct");
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String select_other_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("specialGroup.selectProduct");
		} else {
			tip = this.specialGroupService.selectProductGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("specialGroup.selectProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else if (tip.equals("repeat")) {
				this.addError("specialGroup.selectRepeatProduct");
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public String getTip() {
		return tip;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setSpecialGroupService(SpecialGroupService specialGroupService) {
		this.specialGroupService = specialGroupService;
	}

}
