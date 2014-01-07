/* 
 * Created by baozhimin at Nov 16, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class NewProductAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -6563940903849824587L;
	private QueryParams params = new QueryParams();
	private NewProductService newProductService;
	private Map<String, Object> result;

	public NewProductAction() {
		super();
		this.currentMenu = "product";
	}

	public String new_product_list() throws Exception {
		result = this.newProductService.getNewProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String new_product_add() throws Exception {
		result = this.newProductService.getNewProductAdd(request, params);
		if (result.containsKey("full")) {
			this.addActionError(getText((String) result.get("full"), new String[] { String.valueOf((Integer) result.get("num")) }));
			return MESSAGE;
		}
		return SUCCESS;
	}

	public String new_product_edit() throws Exception {
		result = this.newProductService.getNewProductEdit(request, params);
		return SUCCESS;
	}
	public String new_product_detail() throws Exception {
		result = this.newProductService.getNewProductEdit(request, params);
		return "select";
	}

	public String new_product_delete() throws Exception {
		tip = this.newProductService.deleteNewProduct(params);
		msg = getText(tip);
		this.addMessage(msg);
		return SUCCESS;
	}

	public String new_product_set() throws Exception {
		result = this.newProductService.getNewProductSet(params);
		return SUCCESS;
	}

	public String new_product_set_submit() throws Exception {
		StringUtil.trim(params.getNewProPass());
		StringUtil.trim(params.getNewProMenuName());
		
		if (StringUtil.isEmpty(params.getNewProPass()) || params.getNewProPass().length() < 6 || params.getNewProPass().length() > 20) {
			this.addActionError(getText("newproduct.newProPass.required"));
		}

		if (StringUtil.isEmpty(params.getNewProMenuName())) {
			params.setNewProMenuName("¼ÓÃÜ²úÆ·");
		} else if (params.getNewProMenuName().length() > 120) {
			this.addActionError(getText("newproduct.newProMenuName.maxlength"));
		}
		if (this.hasActionErrors()) {
			return MESSAGE;
		}
		tip = this.newProductService.updateNewProductSet(params);
		msg = getText(tip);
		if (tip == "editSuccess") {
			this.addMessage(msg);
		} else {
			this.addActionError(msg);
			return MESSAGE;
		}
		return SUCCESS;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@JSON(serialize = false)
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	@JSON(serialize = false)
	public QueryParams getModel() {
		return params;
	}

	public void setNewProductService(NewProductService newProductService) {
		this.newProductService = newProductService;
	}
}
