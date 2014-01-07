package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.misc.VideoFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class VideoFormActionTest extends ActionTestSupport  {
	private String namespace = "/video";
	private VideoFormAction action = null;
	private Video video = null;
	
	public void test_video_edit_submit() throws Exception {
		String method = "video_edit_submit";
		Video v = this.getVideo();
		if(v != null){
			action = this.createAction(VideoFormAction.class, namespace, method);
			video = this.action.getModel();
			video.setTitle(v.getTitle());
			video.setGroupId(v.getGroupId());
			video.setOldGroupId(v.getGroupId());
			video.setState(v.getState());
			video.setComId(v.getComId());
			video.setVideoId(v.getVideoId());
			
			this.execute(method, "success");
		}
	}
	
	public void test_video_upload_submit() throws Exception {
		String method = "video_upload_submit";
		action = this.createAction(VideoFormAction.class, namespace, method);
		video = this.action.getModel();
		video.setFileName("≤‚ ‘7.fgs");
		video.setFileSize(0);
		video.setTitle("≤‚ ‘7");
		this.execute(method, "success");
	}
}
