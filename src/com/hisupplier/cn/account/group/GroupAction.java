/* 
 * Created by sunhailin at Nov 13, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.product.ProductService;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.product.TradeService;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class GroupAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -4143878784671109813L;
	private QueryParams params = new QueryParams();
	private GroupService groupService;
	private ProductService productService;
	private TradeService tradeService;
	private Map<String, Object> result;

	public String group_list() throws Exception {
		result = this.groupService.getGroupList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String group_add() throws Exception {
		result = this.groupService.getGroupAdd(params);
		return SUCCESS;
	}

	public String group_edit() throws Exception {
		result = this.groupService.getGroupEdit(params);
		return SUCCESS;
	}

	public String group_delete() throws Exception {
		tip = this.groupService.deleteGroup(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("group.deleteSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String group_order() throws Exception {
		result = this.groupService.getGroupOrderList(params);
		return SUCCESS;
	}

	public String group_order_submit() throws Exception {
		tip = this.groupService.updateGroupOrder(params);
		if (tip.equals("orderSuccess")) {
			this.addMessage("group.orderSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String select_group_list() throws Exception {
		result = this.groupService.getSelectGroupList(params);
		return SUCCESS;
	}

	public String group_product_list() throws Exception {
		result = this.groupService.getGroupProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String no_group_product_list() throws Exception {
		result = this.groupService.getNonGroupProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
	
	public String product_delete()throws Exception {
		tip = this.productService.deleteProduct(params);
		if(StringUtil.equals(tip, "deleteSuccess")){
			this.addMessage(getText(tip));
		}else{
			this.addError(getText(tip));
		}
		if(params.getGroupId() > 0){
			return "group_product_list";
		}else {
			return "no_group_product_list";
		}
	}

	public String change_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectProduct");
		} else {
			tip = this.groupService.changeProductGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("group.selectProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String remove_from_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectProduct");
		} else {
			tip = this.groupService.removeFromProductGroup(params);
			if (tip.equals("deleteSuccess")) {
				this.addMessage(this.getText("group.removeFromProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String select_product_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectProduct");
		} else {
			tip = this.groupService.selectProductGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("group.selectProductGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}
	
	public String trade_delete()throws Exception{
		tip = this.tradeService.deleteTrade(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage(getText(tip));
		} else {
			this.addError(getText(tip));
		}
		
		if(params.getGroupId() > 0){
			return "group_trade_list";
		}else {
			return "no_group_trade_list";
		}
	}

	public String group_trade_list() throws Exception {
		result = this.groupService.getGroupTradeList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String no_group_trade_list() throws Exception {
		result = this.groupService.getNonGroupTradeList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String change_trade_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectTrade");
		} else {
			tip = this.groupService.changeTradeGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("group.selectTradeGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String remove_from_trade_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectTrade");
		} else {
			tip = this.groupService.removeFromTradeGroup(params);
			if (tip.equals("deleteSuccess")) {
				this.addMessage(this.getText("group.removeFromTradeGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}

	public String select_trade_group() throws Exception {
		if (params.getProId() == null || params.getProId().length <= 0) {
			this.addError("group.selectTrade");
		} else {
			tip = this.groupService.selectTradeGroup(params);
			if (tip.equals("addSuccess")) {
				this.addMessage(this.getText("group.selectTradeGroup", new String[] { params.getProId().length + "", params.getGroupName() }));
			} else {
				this.addError("operateFail");
			}
		}
		return SUCCESS;
	}
	
	public String update_group_ico() throws Exception {
		tip = this.groupService.updateGroupIco(params);
		return SUCCESS;
	}

	public QueryParams getModel() {
		return params;
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

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public void setTradeService(TradeService tradeService) {
		this.tradeService = tradeService;
	}

}
