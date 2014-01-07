/* 
 * Created by sunhailin at Nov 17, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.group.GroupFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class GroupFormActionTest extends ActionTestSupport {
	private String namespace = "/group";
	private GroupFormAction action = null;
	private Group group = null;

	public void test_group_add_submit() throws Exception {
		String method = "group_add_submit";
		action = this.createAction(GroupFormAction.class, namespace, method);
		group = action.getModel();
		group.setParentId(0);
		group.setGroupName("分组5");
		group.setComId(442);
		this.execute(method, "success");
	}

	public void test_group_add_submit_parentId() throws Exception {
		String method = "group_add_submit";
		Group g = this.getProductGroup(false, false);
		if (g != null) {
			action = this.createAction(GroupFormAction.class, namespace, method);
			group = action.getModel();
			group.setParentId(g.getGroupId());
			group.setGroupName("分组test");
			group.setComId(g.getComId());
			this.execute(method, "success");
		}
	}

	public void test_group_edit_submit() throws Exception {
		String method = "group_edit_submit";
		Group g = this.getProductGroup(false, false);
		if (g != null) {
			action = this.createAction(GroupFormAction.class, namespace, method);
			group = action.getModel();
			group.setGroupId(g.getGroupId());
			group.setGroupName(g.getGroupName());
			this.execute(method, "success");
		}
	}
}
