/* 
 * Created by sunhailin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.group.SpecialGroupFormAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class SpecialGroupFormActionTest extends ActionTestSupport {
	private String namespace = "/specialGroup";
	private SpecialGroupFormAction action = null;
	private Group group = null;
	
	public void test_group_add_submit() throws Exception {
		String method = "group_add_submit";
		action = this.createAction(SpecialGroupFormAction.class, namespace, method);
		group = action.getModel();
		group.setGroupName("≤‚ ‘Ãÿ ‚∑÷◊È");
		group.setComId(442);
		this.execute(method, "success");
	}
	
	public void test_group_edit_submit() throws Exception {
		String method = "group_edit_submit";
		Group g = (com.hisupplier.cn.account.entity.Group)this.getSpecialGroup();
		if(group != null){
			action = this.createAction(SpecialGroupFormAction.class, namespace, method);
			group = action.getModel();
			group.setGroupId(g.getGroupId());
			group.setGroupName(g.getGroupName());
			group.setComId(g.getComId());
			this.execute(method, "success");
		}
	}
}
