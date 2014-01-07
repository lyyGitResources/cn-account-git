/* 
 * Created by sunhailin at Nov 5, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.misc.VideoAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.entity.cn.Group;

/**
 * @author sunhailin
 *
 */
public class VideoActionTest extends ActionTestSupport {
	private String namespace = "/video";
	private VideoAction action = null;
	private QueryParams params = null;

	public void test_video_list_byGroup() throws Exception {
		String method = "video_list";
		Group group = this.getGroup();
		if (group != null) {
			action = this.createAction(VideoAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}

	public void test_video_list_byQueryText() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		params = action.getModel();
		params.setQueryBy("title");
		params.setQueryText("สำฦต");
		this.execute(method, "success");
	}

	public void test_video_list_all() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_video_list_stateReject() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		params = action.getModel();
		params.setVideoType("stateReject");
		this.execute(method, "success");
	}

	public void test_video_list_Company() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		params = action.getModel();
		params.setVideoType("Company");
		this.execute(method, "success");
	}

	public void test_video_list_Product() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		params = action.getModel();
		params.setVideoType("Product");
		this.execute(method, "success");
	}

	public void test_video_list_Menu() throws Exception {
		String method = "video_list";
		action = this.createAction(VideoAction.class, namespace, method);
		params = action.getModel();
		params.setVideoType("Menu");
		this.execute(method, "success");
	}

	public void test_video_upload() throws Exception {
		String method = "video_upload";
		action = this.createAction(VideoAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_video_edit() throws Exception {
		Video video = this.getVideo();
		if (video != null) {
			String method = "video_edit";
			action = this.createAction(VideoAction.class, namespace, method);
			params = action.getModel();
			params.setVideoId(video.getVideoId());
			this.execute(method, "success");
		}
	}

	public void test_video_delete() throws Exception {
		Video video = this.getVideo();
		if (video != null) {
			String method = "video_delete";
			action = this.createAction(VideoAction.class, namespace, method);
			params = action.getModel();
			params.setVideoId(video.getVideoId());
			this.execute(method, "success");
		}
	}

	public void test_video_menu_list() throws Exception {
		Video video = this.getVideo();
		if (video != null) {
			String method = "video_menu_list";
			action = this.createAction(VideoAction.class, namespace, method);
			params = action.getModel();
			params.setVideoId(video.getVideoId());
			this.execute(method, "success");
		}
	}

	public void test_video_product_list() throws Exception {
		Video video = this.getVideo();
		if (video != null) {
			String method = "video_product_list";
			action = this.createAction(VideoAction.class, namespace, method);
			params = action.getModel();
			params.setVideoId(video.getVideoId());
			this.execute(method, "success");
		}
	}

}
