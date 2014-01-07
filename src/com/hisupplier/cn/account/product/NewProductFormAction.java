/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class NewProductFormAction extends BasicAction implements ModelDriven<Product> {

	private static final long serialVersionUID = 7265404415988320150L;
	private Product product = new Product();
	private NewProductService newProductService;
	private Map<String, Object> result = null;

	public NewProductFormAction() {
		super();
		this.currentMenu = "product";
	}

	public String new_product_add_submit() throws Exception {
		product.setComId(this.getLoginUser().getComId());
		tip = this.newProductService.addNewProduct(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			msg = getText(tip);
			return INPUT;
		}
		this.addMessage(getText(tip) + "<br/>" + super.getMemoInfo());

		return SUCCESS;
	}

	public String new_product_edit_submit() throws Exception {
		product.setComId(this.getLoginUser().getComId());
		tip = this.newProductService.updateNewProduct(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "editSuccess")) {
			msg = getText(tip);
			this.addActionError(msg);
			result = new HashMap<String, Object>();
			result.put("product", product);
			return INPUT;
		}
		this.addMessage(getText(tip) + "<br/>" + super.getMemoInfo());
		return SUCCESS;
	}

	@Override
	public void validate() {
		StringUtil.trimToEmpty(product, "proName,model,province,city,town,place,brief");

		if (StringUtil.isBlank(product.getProName()) || product.getProName().length() > 120) {
			this.addActionError(getText("product.proName.required"));
		}
		if (product.getModel().length() > 50) {
			this.addActionError(getText("product.model.maxlength"));
		}
		if (product.getPlace().length() > 100) {
			this.addActionError(getText("product.place.required"));
		}
//		if (StringUtil.isBlank(product.getPlace()) && StringUtil.isEmpty(product.getProvince())) {
//			this.addActionError(getText("product.town.required"));
//		} else if (product.getPlace() != null && product.getPlace().length() > 100) {
//			this.addActionError(getText("product.place.required"));
//		}
		if (StringUtil.isBlank(product.getBrief()) || product.getBrief().length() > 150) {
			this.addActionError(getText("product.brief.required"));
		}
		if (StringUtil.isNotEmpty(product.getDescription()) && product.getDescription().length() > 20000) {
			this.addActionError(getText("product.description.maxlength"));
		}else{
			// 过滤超链接
			product.setDescription(StringUtil.filterHyperlink(product.getDescription()));
		}
		if (StringUtil.isNotEmpty(product.getBrief())) {
			if(!RegexUtil.RegexUrl(product.getBrief()) || !RegexUtil.RegexEmail(product.getBrief())) {
				this.addActionError("请不要在【摘要】中添加网址和邮箱，这将导致信息无法提交！");
			}
		}
		if (StringUtil.isNotEmpty(product.getDescription())) {
			if (!RegexUtil.RegexUrl(product.getDescription()) || RegexUtil.RegexEmail(product.getDescription())== false) {
				this.addActionError("请不要在【描述】中添加网址和邮箱，这将导致信息无法提交！");
			}
		}
		
		if (this.hasActionErrors()) {
			result = new HashMap<String, Object>();
			result.put("product", product);
		}
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

	public Product getModel() {
		return product;
	}

	public void setNewProductService(NewProductService newProductService) {
		this.newProductService = newProductService;
	}

}
