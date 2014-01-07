/* 
 * Created by sunhailin at Nov 25, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class TradeFormAction extends BasicAction implements ModelDriven<Product> {

	private static final long serialVersionUID = -471501381151639647L;
	private Product product = new Product();
	private TradeService tradeService;
	private Map<String, Object> result;
	private String validateCodeKey;
	private String validateCode;
	
	public TradeFormAction() {
		super();
		this.currentMenu = "trade";
	}

	public String trade_add_submit() throws Exception {
		tip = this.tradeService.addTrade(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			this.addActionError(getText(tip));
			result = new HashMap<String, Object>();
			result.put("product", product);
			return INPUT;
		}
		return SUCCESS;
	}

	public String trade_edit_submit() throws Exception {
		tip = this.tradeService.updateTrade(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "oldSuccess") && !StringUtil.equalsIgnoreCase(tip, "tradeSuccess")) {
			this.addActionError(getText(tip));
			result = new HashMap<String, Object>();
			result.put("product", product);
			return INPUT;
		}
		if (StringUtil.equalsIgnoreCase(tip, "oldSuccess")) {
			this.addMessage(getText("trade.editSuccess")); // +"<br/>"+getText("memo.info"));
		}
		return tip;
	}

	@Override
	public void validate() {
		String fields = "proName,brief,imgPath,keywords,keyword1,keyword2,keyword3";
		StringUtil.trimToEmpty(product, fields);
		if (product.getProType() != 1 && product.getProType() != 2) {
			this.addActionError(this.getText("trade.proType.required"));
		}

		if (StringUtil.isBlank(product.getProName()) || product.getProName().length() > 120) {
			this.addActionError(this.getText("trade.proName.required"));
		}
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("keyword1", "关键词,");
		map.put("proName", "名称,");
		map.put("brief", "摘要,");
		map.put("description", "详细描述,");
		String patentKeyword = PatentUtil.checkKeyword(product, map, this.getLoginUser().getComId());
		if (StringUtil.isNotEmpty(patentKeyword)) {
			this.addActionError("提交失败！您提交的'商情" + patentKeyword.split(",")[0] + "'包含违禁词：" + patentKeyword.split(",")[1]);
		}

		if (StringUtil.isBlank(product.getBrief()) || product.getBrief().length() > 150) {
			this.addActionError(this.getText("trade.brief.required"));
		}
		if (product.getKeyword1().length() > 60 || product.getKeyword2().length() > 60 || product.getKeyword3().length() > 60) {
			this.addActionError(this.getText("trade.keywords.maxlength"));
		}

		if (product.getCatId() <= 0) {
			this.addActionError(this.getText("trade.catName.required"));
		}

		if (product.getDescription() != null && product.getDescription().length() > 20000) {
			this.addActionError(this.getText("trade.description.maxlength"));
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

		// 免费会员添加商情时需验证验证码
		if(this.getLoginUser().getMemberType() != 2 && product.getProId() == 0 && !ValidateCode.isValid(request, this.validateCodeKey, this.validateCode)){
			this.addActionError(TextUtil.getText("validateCode.error", "zh"));
		}
		
		if(this.hasActionErrors()){
			result = new HashMap<String, Object>();
			result.put("product", product);
		}
	}

	public String getMsg() {
		return msg;
	}

	public String getTip() {
		return tip;
	}

	public Product getModel() {
		return product;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setTradeService(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	public String getValidateCodeKey() {
		return validateCodeKey;
	}

	public void setValidateCodeKey(String validateCodeKey) {
		this.validateCodeKey = validateCodeKey;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

}
