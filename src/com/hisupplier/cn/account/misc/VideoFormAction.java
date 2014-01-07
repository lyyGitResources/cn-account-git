/* 
 * Created by sunhailin at Nov 9, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class VideoFormAction extends BasicAction implements ModelDriven<Video> {

	private static final long serialVersionUID = 3910293822373252681L;

	private Video video = new Video();
	private VideoService videoService;
	private Map<String, Object> result;

	public String video_edit_submit() throws Exception {
		StringUtil.trimToEmpty(video, "title");
		if (video.getTitle().length() < 0 || video.getTitle().length() > 120) {
			this.addError("video.title.maxlength");
		} else {
			tip = this.videoService.updateVideo(video, this.getLoginUser());
			if (tip.equals("editSuccess")) {
				this.addMessage(getText("video.editSuccess")+"<br/>"+getText("memo.info2"));
			} else {
				this.addError(tip);
			}
		}
		return SUCCESS;
	}

	public String video_upload_submit() throws Exception {
		tip = this.videoService.addVideo(video, this.getLoginUser());

		if (tip.equals("addSuccess")) {
			msg = this.getText("video.uploadSuccess");
		} else {
			msg = this.getText(tip);
		}
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	@JSON(serialize = false)
	public Map<String, Object> getResult() {
		return result;
	}

	public String getTip() {
		return tip;
	}

	public Video getModel() {
		return video;
	}

	public void setVideoService(VideoService videoService) {
		this.videoService = videoService;
	}

}
