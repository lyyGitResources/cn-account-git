/* 
 * Created by sunhailin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.group.SpecialGroupAction;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class SpecialGroupActionTest extends ActionTestSupport {
	private String namespace = "/specialGroup";
	private SpecialGroupAction action = null;
	private QueryParams params = null;
	
	public void test_group_list() throws Exception {
		String method = "group_list";
		action = this.createAction(SpecialGroupAction.class, namespace, method);
		this.execute(method, "success");
	}
	
	public void test_group_add() throws Exception {
		String method = "group_add";
		action = this.createAction(SpecialGroupAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(0);
		this.execute(method, "success");
	}
	
	public void test_group_edit() throws Exception {
		String method = "group_edit";
		Group group = (com.hisupplier.cn.account.entity.Group)this.getSpecialGroup();
		if(group != null){
			action = this.createAction(SpecialGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}
	
	public void test_group_delete() throws Exception {
		String method = "group_delete";
		Group group = (com.hisupplier.cn.account.entity.Group)this.getSpecialGroup();
		if(group != null){
			action = this.createAction(SpecialGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}
	
	public void test_no_group_product_list() throws Exception {
		String method = "no_group_product_list";
		action = this.createAction(SpecialGroupAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(0);
		this.execute(method, "success");
	}
	
	public void test_select_product_group() throws Exception {
		//DOTO
//		String method = "select_product_group";
//		Group group = (com.hisupplier.cn.account.entity.Group)this.getSpecialGroup();
//		action = this.createAction(SpecialGroupAction.class, namespace, method);
//		params = action.getModel();
//		params.setGroupId(0);
//		this.execute(method, "success");
	}
	
	public void test_group_product_list() throws Exception {
		String method = "group_product_list";
		Group group = (com.hisupplier.cn.account.entity.Group)this.getSpecialGroup();
		if(group != null){
			action = this.createAction(SpecialGroupAction.class, namespace, method);
			params = action.getModel();
			params.setGroupId(group.getGroupId());
			this.execute(method, "success");
		}
	}
	
	
}
