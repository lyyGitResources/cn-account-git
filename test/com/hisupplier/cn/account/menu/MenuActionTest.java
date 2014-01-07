package com.hisupplier.cn.account.menu;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.menu.MenuAction;
import com.hisupplier.cn.account.menu.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

public class MenuActionTest extends ActionTestSupport{
	private QueryParams params = null;
	private String namespace = "/menu";
	
	public void test_menu_group_list() throws Exception{
		String method = "menu_group_list";
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		this.execute(method, "success");
	}
	
	public void test_menu_group_add() throws Exception{
		String method = "menu_group_add";
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		this.execute(method, "success");
	}
	
	public void test_menu_group_edit() throws Exception{
		String method = "menu_group_edit";
		Group group = this.getMenuGroup(true); 
		if(group == null){
			assertTrue(false);
			return;
		}
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");		
	}
	
	public void test_menu_group_delete() throws Exception{
		Group group = this.getMenuGroup(false);
		if(group == null){
			assertTrue(false);
			return;
		}
		String method = "menu_group_delete";
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");		
	}
	
	public void test_menu_list() throws Exception{
		String method = "menu_list";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");		
	}
	
	public void test_menu_add() throws Exception{
		String method = "menu_add";
		MenuAction action = createAction(MenuAction.class,namespace,method);
		params = action.getModel();
		this.execute(method, "success");			
	}

	public void test_menu_edit() throws Exception{
		String method = "menu_edit";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		Menu menu = this.getMenu(group.getGroupId());
		MenuAction action = createAction(MenuAction.class,namespace,method);		
		params = action.getModel();
		params.setMenuId(menu.getMenuId());
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");					
	}

	public void test_menu_delete() throws Exception{
		String method = "menu_delete";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		Menu menu = this.getMenu(group.getGroupId());
		MenuAction action = createAction(MenuAction.class,namespace,method);		
		params = action.getModel();
		params.setMenuId(menu.getMenuId());
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");			
	}	
	
	public void test_menu_order() throws Exception{
		
	}
	
	public void test_menu_group_order() throws Exception{
		
	}
}
