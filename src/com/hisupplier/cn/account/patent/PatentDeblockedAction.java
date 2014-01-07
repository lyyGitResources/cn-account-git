/**
 * Created by liuyuyang at 2013-3-29 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.patent;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.misc.ImageService;
import com.hisupplier.cn.account.misc.QueryParams;
import com.opensymphony.xwork2.ModelDriven;

public class PatentDeblockedAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = 1323261959311579327L;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;
	private PatentDeblockedService patentDeblockedService;
	private ImageService imageService;
	private int licenseMulti = 1;//image_multi.jspҳ������ʾ
	
	public String patentDeblocked() throws Exception {
		super.currentMenu = "patentDeblocked"; // ͷ���˵����
		result = patentDeblockedService.getPatentDeblocked(getLoginUser(), null);
		return SUCCESS;
	}

	public String patentDeblocked_list() throws Exception {
		super.currentMenu = "patentDeblocked";
		result = patentDeblockedService.getPatentDeblockedList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String patentDeblocked_del() throws Exception {
		int num = patentDeblockedService.deletePatentDeblocked(params);
		if (num > 0) {
			this.addMessage("���Υ���ɾ���ɹ�");
		} else {
			this.addError("���Υ���ɾ��ʧ��");
		}
		return SUCCESS;
	}
	
	public String patentDeblocked_edit() throws Exception {
		super.currentMenu = "patentDeblocked";
		result = patentDeblockedService.getPatentDeblockedEdit(getLoginUser(), params);
		return SUCCESS;
	}
	
	public String patentDeblocked_license() throws Exception {
		super.currentMenu = "patentDeblocked";
		params.setImgType(Image.LICENSE);// Ӫҵִ�պ���Чƾ֤
		result = imageService.getImageList(params);
		result.put("licenseType", params.getImgType());
		Map<String, Object> license = imageService.getLicense(params);
		result.put("licenseCount", license.get("licenseCount"));
		result.put("isImgFull", license.get("isImgFull"));
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public QueryParams getModel() {
		return params;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public void setPatentDeblockedService(PatentDeblockedService patentDeblockedService) {
		this.patentDeblockedService = patentDeblockedService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public int getLicenseMulti() {
		return licenseMulti;
	}

	public void setLicenseMulti(int licenseMulti) {
		this.licenseMulti = licenseMulti;
	}

}
