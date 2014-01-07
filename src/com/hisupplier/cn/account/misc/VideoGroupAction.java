/* 
 * Created by sunhailin at Nov 2, 2009 
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
public class VideoGroupAction extends BasicAction implements ModelDriven<QueryParams> {

	private static final long serialVersionUID = -2858245229469309363L;
	private QueryParams params = new QueryParams();
	private VideoService videoService;
	private Map<String, Object> result;

	public String video_group_list() throws Exception {
		result = this.videoService.getVideoGroupList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String video_group_form() throws Exception {
		result = this.videoService.getVideoGroup(params);
		return SUCCESS;
	}

	public String video_group_form_submit() throws Exception {
		StringUtil.trimToEmpty(params, "groupName");
		if (params.getGroupName().length() <= 0 || params.getGroupName().length() > 60) {
			msg = this.getText("videoGroup.name.maxlength");
		} else {
			if (params.getGroupId() > 0) {
				tip = this.videoService.updateVideoGroup(params);
				if (tip.equals("editSuccess")) {
					msg = this.getText("videoGroup.editSuccess");
				} else {
					msg = this.getText("操作失败，组名已经存在！");
				}
			} else if (params.getGroupId() <= 0) {
				tip = this.videoService.addVideoGroup(params);
				if (tip.equals("addSuccess")) {
					msg = this.getText("videoGroup.addSuccess");
					
				} else {
					//msg = this.getText(tip);
					msg = this.getText("操作失败，组名已经存在！");
				}
			}
		}

		return SUCCESS;
	}

	public String video_group_delete() throws Exception {
		tip = this.videoService.deleteVideoGroup(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("videoGroup.deleteSuccess");
		} else {
			this.addError(tip);
		}
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

	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}

}
