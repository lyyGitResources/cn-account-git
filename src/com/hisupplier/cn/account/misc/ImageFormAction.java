/* 
 * Created by baozhimin at Apr 16, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Image;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class ImageFormAction extends BasicAction implements ModelDriven<Image>{

	private static final long serialVersionUID = -5954628495236354736L;
	private Image image = new Image();
	private ImageService imageService;
	private Map<String, Object> result = new HashMap<String, Object>();
	
	@Override
	public String execute() throws Exception {
		imageService.uploadImage(response, image);
		return SUCCESS;
	}

	@Override
	public void validate() {
		if(image.getAttachment() == null){
			this.addActionError("请选择一张图片！");
		}else{
			String mimeType = image.getMimeType();
			if(mimeType == null || ("," + mimeType + ",").indexOf("," + image.getAttachmentContentType() + ",") == -1){
				this.addActionError("图片类型应该为：" + image.getType());
			}
			
//			if(image.getAttachment().length() / 1024 > image.getMaxSize()){
//				this.addActionError("图片大小不超过" + image.getMaxSize() + "K");
//			}
		}

		if(this.hasActionErrors()){
			result.put("image", image);
		}
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

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public Image getModel() {
		return image;
	}
}
