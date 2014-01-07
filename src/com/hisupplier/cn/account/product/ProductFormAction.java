/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ProductFormAction extends BasicAction implements ModelDriven<Product> {

	private static final long serialVersionUID = 7265404415988320150L;
	private Product product = new Product();
	private ProductService productService;
	private Map<String, Object> result;

	public ProductFormAction() {
		super();
		this.currentMenu = "product";
	}

	public String product_add_submit() throws Exception {
		// ��place��Ϊ��ʱ�������town�ֶ�
		// �ж������ѡ��ʹ��place�ֶ�
		tip = this.productService.addProduct(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			result = this.productService.getProductSubmitError(product,this.getLoginUser());
			return INPUT;
		}
		return SUCCESS;
	}

	public String product_edit_submit() throws Exception {
		tip = this.productService.updateProduct(response, product, this.getLoginUser());
		if (!StringUtil.equalsIgnoreCase(tip, "oldSuccess") && !StringUtil.equalsIgnoreCase(tip, "productSuccess")) {
			result = this.productService.getProductSubmitError(product,this.getLoginUser());
			this.addActionError(getText(tip) + "<br/>" + super.getMemoInfo());
			return INPUT;
		}
		if (StringUtil.equalsIgnoreCase(tip, "oldSuccess")) {
			this.addMessage("�޸ĳɹ�"); // +"<br/>"+getText("memo.info")
		}
		return tip;
	}

	@Override
	public void validate() {
		super.validate();
		
		QueryParams keyparams = new QueryParams();
		keyparams.setKeyword1(product.getKeyword1());
		keyparams.setKeyword2(product.getKeyword2());
		keyparams.setKeyword3(product.getKeyword3());
		int[] proId = {product.getProId()};
		keyparams.setProId(proId);
		keyparams.setLoginUser(this.getLoginUser());
		tip = this.productService.checkProductKeyword(keyparams);
		if(!tip.equals("true")){
			msg = this.getText(tip);
			this.addFieldError("keyword.remote", "�ؼ����ظ�");
		}
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("keyword1", "�ؼ���,");
		map.put("proName", "����,");
		map.put("brief", "ժҪ,");
		map.put("description", "��ϸ����,");
		String patentKeyword = PatentUtil.checkKeyword(product, map, this.getLoginUser().getComId());
		if (StringUtil.isNotEmpty(patentKeyword)) {
			this.addFieldError("keyword.error", "�ύʧ�ܣ����ύ��   '��Ʒ" + patentKeyword.split(",")[0] + 
					"'  ����Υ���ʣ�" + patentKeyword.split(",")[1]);
		}
		
		// ���˲�Ʒ��ϸ������Ϣ ����
		String fStr = Pattern.compile("&nbsp;|<br />|<p>")
				.matcher(product.getDescription()).replaceAll("").trim();
		if (StringUtils.isEmpty(fStr)) {
			this.addFieldError("product.description.required", "����д��Ʒ��ϸ����");
		}
		StringUtil.trimToEmpty(product, "province,city,town,place");
		
		if(product.getPlace().length() > 100){
			this.addFieldError("product.place.required", this.getText("product.place.required"));
		}
		
//		if (StringUtil.isBlank(product.getCity()) && StringUtil.isBlank(product.getPlace())) {
//			this.addFieldError("product.town.required", this.getText("product.town.required"));
//		} else if (StringUtil.isNotBlank(product.getPlace())) {
//			if (product.getPlace().length() > 120) {
//				this.addFieldError("product.place.required", this.getText("product.place.required"));
//			}
//		}

		// ��֤��С�����Ƿ�ƥ��
		if (StringUtil.isNotBlank(product.getMinOrderUnitSelect())) {
			product.setMinOrderUnit(product.getMinOrderUnitSelect());
		} else if (StringUtil.isNotBlank(product.getMinOrderUnitOther())) {
			if (product.getMinOrderUnitOther().length() > 20) {
				this.addFieldError("product.minOrderUnit.maxlength", this.getText("product.minOrderUnit.maxlength"));
			}
			product.setMinOrderUnit(product.getMinOrderUnitOther());
		} else {
			product.setMinOrderUnit("");
		}

		if (product.getMinOrderNum() == 0 && StringUtil.isNotBlank(product.getMinOrderUnit())) {
			this.addFieldError("product.minOrderUnit.correspond", this.getText("product.minOrderUnit.correspond"));
		} else if (product.getMinOrderNum() > 0 && StringUtil.isBlank(product.getMinOrderUnit())) {
			this.addFieldError("product.minOrderNum.correspond", this.getText("product.minOrderNum.correspond"));
		}

		if (product.getPrice1() < 0 || product.getPrice2() < 0) {
			this.addFieldError("product.price.number", this.getText("product.price.number"));
		}		
		
		// ���˳�����
		product.setDescription(StringUtil.filterHyperlink(product.getDescription()));
		
		if (StringUtil.isNotEmpty(product.getBrief())) {
			if(!RegexUtil.RegexUrl(product.getBrief()) || !RegexUtil.RegexEmail(product.getBrief())) {
				this.addFieldError("product.brief", "�벻Ҫ�ڡ�ժҪ���������ַ�����䣬�⽫������Ϣ�޷��ύ��");
			}
		}
		if (StringUtil.isNotEmpty(product.getDescription())) {
			if (!RegexUtil.RegexUrl(product.getDescription()) || RegexUtil.RegexEmail(product.getDescription())== false) {
				this.addFieldError("product.description","�벻Ҫ�ڡ��������������ַ�����䣬�⽫������Ϣ�޷��ύ��");
			}
		}
		
		// ��ǩ��֤
		// ��ֹ��������ͬ��ʾ
		boolean tooLength = false; // ����120�ַ�
		boolean same = false; // ��ǩ�����ظ�
		boolean noOneToOne = false; // ��ǩ���ƺ�ֵ����Ӧ
		if (product.getTagValue2() != null && product.getTagValue2().length > 0) {
			for (String tag2Value : product.getTagValue2()) {
				if (!tooLength && tag2Value.length() > 120) {
					this.addFieldError("product.tags.tooLength", this.getText("product.tags.tooLength"));
					tooLength = true;
				}
			}
		}
		
		if (product.getTagName3() != null && product.getTagName3().length > 0) {
			if(StringUtil.equals(product.getTagName3()[0], "��ǩ����")){
				product.getTagName3()[0] = "";
			}
			
			String name = "";
			for (int i = 0; i < product.getTagName3().length; i++) {
				if (!tooLength && product.getTagName3()[i].length() > 120) {
					this.addFieldError("product.tags.tooLength", this.getText("product.tags.tooLength"));
					tooLength = true;
				}

				if (!same && StringUtil.isNotBlank(product.getTagName3()[i]) && StringUtil.equals(product.getTagName3()[i], name) && !"������������".equals(product.getTagName3()[i])) {
					this.addFieldError("product.tags.same", this.getText("product.tags.same"));
					same = true;
				}

				if (product.getTagName3()[i].length() > 0 && !StringUtil.equals(product.getTagName3()[i], name)) {
					name = product.getTagName3()[i].trim();
				}

				if (!noOneToOne && product.getTagName3()[i].length() > 0 && product.getTagValue3()[i].length() == 0) {
					this.addFieldError("product.tags.noOneToOne", this.getText("product.tags.noOneToOne"));
					noOneToOne = true;
				}
			}
		}
		if (product.getTagValue3() != null && product.getTagValue3().length > 0) {
			if(StringUtil.equals(product.getTagValue3()[0], "��ǩ����")){
				product.getTagValue3()[0] = "";
			}
			
			for (int i = 0; i < product.getTagValue3().length; i++) {
				if (!tooLength && product.getTagValue3()[i].length() > 120) {
					this.addFieldError("product.tags.tooLength", this.getText("product.tags.tooLength"));
					tooLength = true;
				}
				if (!noOneToOne && product.getTagValue3()[i].length() > 0 && product.getTagName3()[i].length() == 0) {
					this.addFieldError("product.tags.noOneToOne", this.getText("product.tags.noOneToOne"));
					noOneToOne = true;
				}
			}
		}

		if (product.getAttachment() != null) {
			if (!StringUtil.containsValue(ALLOW_MIME_TYPE, product.getAttachmentContentType())) {
				this.addFieldError("attachment.invalid", this.getText("attachment.invalid"));
			} else if (product.getAttachment().length() / 1024 > 1024) {
				this.addFieldError("attachment.invalid", this.getText("attachment.invalid"));
			}
		}
		if (this.hasFieldErrors()) {
			QueryParams params = new QueryParams();
			params.setLoginUser(this.getLoginUser());

			result = this.productService.getProductSubmitError(product,this.getLoginUser());
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

	@JSON(serialize = false)
	public Product getModel() {
		return product;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
