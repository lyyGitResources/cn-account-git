/* 
 * Created by sunhailin at Nov 13, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.group.GroupAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class GroupActionTest extends ActionTestSupport {
	private String namespace = "/group";
	private GroupAction action = null;
	private QueryParams params = null;
 
	public void test_group_list() throws Exception {
		String method = "group_list";
		action = this.createAction(GroupAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_group_add_noParentId() throws Exception {
		String method = "group_add";
		action = this.createAction(GroupAction.class, namespace, method);
		params = action.getModel();
		params.setParentId(0);
		this.execute(method, "success");
	}

	public void test_group_add_parentId() throws Exception {
		String method = "group_add";
		Group group = this.getProductGroup(false, false);
		if (group != null) {
			action = this.createAction(GroupAction.class, namespace, method);
			params = action.getModel();
			params.setParentId(group.getGroupId());
			this.execute(method, "success");
		}
	}

	public void test_group_edit() throws Exception {
		String method = "group_edit";
		Group group = this.getProductGroup(false, false);
		if (group != null) {
			action = this.createAction(GroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}

	public void test_group_delete() throws Exception {
		String method = "group_delete";
		Group group = this.getProductGroup(true, false);
		if (group != null) {
			action = this.createAction(GroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}

	public void test_group_order() throws Exception {
		String method = "group_order";
		action = this.createAction(GroupAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_no_group_product_list() throws Exception {
		String method = "no_group_product_list";
		action = this.createAction(GroupAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(0);
		this.execute(method, "success");
	}

	public void test_group_product_list() throws Exception {
		String method = "group_product_list";
		Group group = this.getProductGroup(true, false);
		if (group != null) {
			action = this.createAction(GroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}
}
