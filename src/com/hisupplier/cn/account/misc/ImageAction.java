/* 
 * Created by baozhimin at Oct 28, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ImageAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -6401690191913158896L;
	private QueryParams params = new QueryParams();
	private ImageService imageService;
	private Map<String, Object> result;
	
	public String image_list() throws Exception {
		result = imageService.getImageList(params);
		Image img = new Image();
		img.getWatermark(request, this.getLoginUser().getMemberId());
		result.put("image", img);
		params.setShowTitle(true);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String image_select() throws Exception {
		// 用于公用image_list的Service
		params.setSelect(true);
		result = imageService.getImageList(params);
		return  params.isAjax() ? AJAX_SUCCESS :SUCCESS;
	}
	
	public String image_delete() throws Exception {
		result = imageService.deleteImage(params);
		if((Integer) result.get("usedNum") > 0){
			this.addError(getText("image.deleteFile", new String[]{ result.get("model").toString() }));
		}else{
			this.addMessage(getText("image.deleteSuccess"));
		}
		if (params.getImgType() == Image.LICENSE) {
			result = imageService.getLicense(params);
			return "patentImgDelete";
		}
		return SUCCESS;
	}
	
	public String image_name_edit() throws Exception {
		if(StringUtil.isNotEmpty(params.getImgName()) && params.getImgName().length() < 120){
			tip = imageService.updateImageName(params);
		}else{
			tip = "operateFail";
		}
		msg = getText(tip);
		return SUCCESS;
	}

	public String image_upload() throws Exception {
		result = imageService.getImageUpload(request, params);
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

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

}
