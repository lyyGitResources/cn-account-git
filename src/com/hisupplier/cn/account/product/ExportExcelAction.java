/* 
 * Created by baozhimin at Nov 19, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ExportExcelAction extends BasicAction implements ModelDriven<Product> {
	private static final long serialVersionUID = -2932694206219873281L;
	public static final String[] ALLOW_MIME_TYPE = {"application/vnd.ms-excel","application/x-msexcel", "application/octet-stream"};
	private Product product = new Product();
	private ProductService productService;
	private Map<String, Object> result;
	String fileName;
	String fileDir;
	public ExportExcelAction() {
		super();
		this.currentMenu = "product";
	}

	public String downloadExcel() throws Exception {
		fileDir = ServletActionContext.getServletContext().getRealPath("") + 
				"/productexcel/temp/" + this.getLoginUser().getMemberId();
		fileName = this.productService.createExcelFile(product, fileDir);
		if("error".equals(fileName)){
			this.addActionError("请重新选择目录");
			return "input";
		}
		return SUCCESS;
	}

	public InputStream getTargetFile() throws Exception {
		return new FileInputStream(new File(fileDir + "/" + fileName));
	}

	public String product_batch_add_submit() throws Exception {
		if (product.getAttachment() == null) {
			result = new HashMap<String,Object>();
			result.put("error", this.getText("attachment.invalid"));
			return SUCCESS;
		}
		if (product.getAttachment() != null) {
			if (!StringUtil.containsValue(ALLOW_MIME_TYPE, product.getAttachmentContentType())) {
				result = new HashMap<String,Object>();
				result.put("error", this.getText("attachment.invalid"));
				return SUCCESS;
			}
		}
		fileDir = ServletActionContext.getServletContext().getRealPath("") + 
				"/productexcel/temp/" + this.getLoginUser().getMemberId();
		result = this.productService.batchAddProduct(request, response, product, this.getLoginUser(), fileDir);
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

	@JSON(serialize = false)
	public Product getModel() {
		return product;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public String getFileName() {
		return fileName;
	}

}
