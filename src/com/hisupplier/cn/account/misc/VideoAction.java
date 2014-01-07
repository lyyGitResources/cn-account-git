/* 
 * Created by sunhailin at Nov 5, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class VideoAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -143210973521386112L;
	private QueryParams params = new QueryParams();
	private VideoService videoService;
	private Map<String, Object> result;
	private int menuType=1;//main。jsp  sitemesh装饰时判断是否是视频管理
	

	public String video_list() throws Exception {
		if (StringUtil.equals(params.getQueryText(), getText("video.title.required"))) {
			params.setQueryText("");
		} else {
			StringUtil.trimToEmpty(params, "queryText");
		}
		
		result = this.videoService.getVideoList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String video_upload() throws Exception {
		result = this.videoService.getVideoUpload(params);
		return SUCCESS;
	}

	public String video_edit() throws Exception {
		result = this.videoService.getVideoEdit(params);
		return SUCCESS;
	}

	public String video_delete() throws Exception {
		tip = this.videoService.deleteVideo(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("video.deleteSuccess");
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String video_menu_list() throws Exception {
		result = this.videoService.getVideoMenuList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String video_product_list() throws Exception {
		//params.setPageSize(1);
		result = this.videoService.getVideoProductList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
	
	public String video_select() throws Exception{
		result = this.videoService.getVideoSelect(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
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

	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

}
