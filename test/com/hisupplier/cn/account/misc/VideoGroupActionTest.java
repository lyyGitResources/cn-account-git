/* 
 * Created by sunhailin at Nov 4, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.misc.VideoGroupAction;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.entity.cn.Group;

/**
 * @author sunhailin
 *
 */
public class VideoGroupActionTest extends ActionTestSupport {
	private String namespace = "/video";
	private VideoGroupAction action = null;
	private QueryParams params = null;

	public void test_video_group_list() throws Exception {
		String method = "video_group_list";
		action = this.createAction(VideoGroupAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_video_group_add() throws Exception {
		String method = "video_group_add";
		action = this.createAction(VideoGroupAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(0);
		this.execute("video_group_form", "success");
	}

	public void test_video_group_edit() throws Exception {
		String method = "video_group_edit";
		Group group = this.getGroup();
		if (group != null) {
			action = this.createAction(VideoGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute("video_group_form", "success");
		}
	}

	public void test_video_group_add_submit() throws Exception {
		String method = "video_group_add_submit";
		action = this.createAction(VideoGroupAction.class, namespace, method);
		params = action.getModel();
		params.setGroupName("ÊÓÆµ·Ö×é5");
		params.setGroupId(0);
		this.execute("video_group_form_submit", "success");
	}

	public void test_video_group_edit_submit() throws Exception {
		String method = "video_group_edit_submit";
		Group group = this.getGroup();
		if (group != null) {
			action = this.createAction(VideoGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			params.setGroupName("this is test");
			this.execute("video_group_form_submit", "success");
		}
	}

	public void test_video_group_delete() throws Exception {
		String method = "video_group_delete";
		Group group = this.getGroup();
		if (group != null) {
			action = this.createAction(VideoGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}

}
