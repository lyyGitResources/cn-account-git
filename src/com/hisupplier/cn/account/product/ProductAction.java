/* 
 * Created by baozhimin at Nov 16, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ProductAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -6563940903849824587L;
	private QueryParams params = new QueryParams();
	private ProductService productService;
	private Map<String, Object> result;
	
	public String product_list() throws Exception {
		result = this.productService.getProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String product_repost() throws Exception {
		throw new PageNotFoundException();
//		tip = this.productService.updateProductRepost(params);
//		if(StringUtil.equals(tip, "product.repostSuccess")){
//			this.addMessage(getText(tip));
//		}else{
//			this.addError(getText(tip));
//		}
//		return SUCCESS;
	}
	
	public String product_repost_all() throws Exception {
		throw new PageNotFoundException();
//		tip = this.productService.updateProductRepostAll(params);
//		if(StringUtil.equals(tip, "product.repostAllSuccess")){
//			this.addMessage("Ыљга" + getText("product.repostSuccess"));
//		}else{
//			this.addError(getText(tip));
//		}
//		return SUCCESS;
	}
	
	public String product_delete() throws Exception {
		tip = this.productService.deleteProduct(params);
		if(StringUtil.equals(tip, "deleteSuccess")){
			this.addMessage(getText(tip));
		}else{
			this.addError(getText(tip));
		}
		return SUCCESS;
	}
	
	public String product_change_user() throws Exception {
		tip = this.productService.updateProductUser(params);
		if(!StringUtil.equals(tip, "operateFail")){
			this.addMessage(getText(tip));
		}else{
			this.addError(getText(tip));
		}

		return SUCCESS;
	}
	
	public String product_add() throws Exception {
		result = this.productService.getProductAdd(request, params);
		if(result.containsKey("full")){
			this.addActionError(getText((String) result.get("full"), 
					new String[]{(String) result.get("num")}));
			return MESSAGE;
		}
		return SUCCESS;
	}
	
	public String product_batch_add() throws Exception {
		Image img = new Image();
		img.getWatermark(request, this.getLoginUser().getMemberId());
		result = this.productService.getProductBatchAdd(request, params);
		result.put("image", img);
		return SUCCESS;
	}
	
	public String product_edit() throws Exception {
		result = this.productService.getProductEdit(request, params);
		return SUCCESS;
	}
	public String product_detail() throws Exception {
		result = this.productService.getProductEdit(request, params);
		return "select";
	}
	
	public String similar_category() throws Exception {
		result = this.productService.getSimilarCategory(params);
		return SUCCESS;
	}
	
	public String product_tags() throws Exception {
		result = this.productService.getProductTags(params);
		return SUCCESS;
	}
	
	public String check_product_keyword() throws Exception {
		tip = this.productService.checkProductKeyword(params);
		if(!tip.equals("true")){
			msg = this.getText(tip);
			tip = "false";
		}
		return SUCCESS;
	}
	
	public String product_recycle_list() throws Exception {
		result = this.productService.getProductRecycleList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
	
	public String product_recycle_delete() throws Exception {
		tip = this.productService.updateRecycleDelete(params, false);
		if(StringUtil.equals(tip, "deleteSuccess")){
			this.addMessage(getText(tip));
		}else{
			this.addError(getText(tip));
		}
		return SUCCESS;
	}
	
	public String product_recycle_empty() throws Exception {
		tip = this.productService.updateRecycleEmpty(params);
		msg = this.getText(tip);
		return SUCCESS;
	}
	
	public String feature_product_list() throws Exception{
		result = this.productService.getFeatureProductList(params);
		return SUCCESS;
	}
	
	public String no_feature_product_list() throws Exception{
		result = this.productService.getNoFeatureProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
	
	public String feature_product_add() throws Exception {
		tip = this.productService.updateFeatureAdd(params);
		if(tip.equals("addSuccess")){
			this.addMessage("product.featureAddSuccess");
		}else{
			this.addError(tip);
		}
		return SUCCESS;
	}
	
	public String feature_product_remove() throws Exception{
		tip = this.productService.updateFeatureRemove(params);
		if(tip.equals("deleteSuccess")){
			this.addMessage("product.featureDeleteSuccess");
		}else{
			this.addError(tip);
		}
		return SUCCESS;
	}
	
	public String feature_product_set() throws Exception{
		tip = this.productService.updateFeatureSet(params);
		if(tip.equals("editSuccess")){
			this.addMessage("product.featureEditSuccess");
		}else{
			this.addError(tip);
		}
		return SUCCESS;
	}
	
	public String feature_product_order() throws Exception{
		tip = this.productService.updateFeatureOrder(params);
		if(tip.equals("orderSuccess")){
			this.addMessage(tip);
		}else{
			this.addError(tip);
		}
		return SUCCESS;
	}
	
	public String product_order() throws Exception{
		if(params.getGroupId()>0){
			result=this.productService.getProductByGroupId(params);
			if(result.containsKey("groupError")){
				this.addError(getText((String) result.get("groupError")));
				return MESSAGE;
			}
		}else{
			result = this.productService.getProductOrderList(params);
			if(result.containsKey("groupError")){
				this.addError(getText((String) result.get("groupError")));
				return MESSAGE;
			}
		}
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
	
	public String product_single_order_submit() throws Exception{
		params.setPageNo(params.getListOrder() / (params.getPageSize() + 1) + 1);
		tip = this.productService.updateProductSingleOrder(params);
		if(tip.equals("orderSuccess")){
			this.addMessage(tip);
		}else{
			this.addError(getText(tip));
		}
		return SUCCESS;
	}
	
	public String product_order_submit() throws Exception{
		tip = this.productService.updateProductOrder(params);
		if(tip.equals("orderSuccess")){
			this.addMessage(tip);
		}else{
			this.addError(getText(tip));
		}
		return SUCCESS;
	}
	
	public String update_product_ico() throws Exception {
		tip = this.productService.updateProductIco(params);
		return SUCCESS;
	}
	
	public String product_success() throws Exception {
		result = this.productService.productSuccess(params);
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

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
