package com.hisupplier.cn.account.menu;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.commons.util.DateUtil;

public class MenuFormActionTest extends ActionTestSupport{
	
	private String nameSpace = "/menu";
	private Menu menu = null;
	
	public void test_menu_group_add_submit() throws Exception {
		String method = "menu_group_add_submit";
		MenuFormAction action = createAction(MenuFormAction.class, nameSpace, method);
		menu = action.getModel();
		menu.setComId(442);
		menu.setGroupName("��Ŀ��Ӳ�����Ŀ��"+ new DateUtil().getTime4());
		menu.setFix(false);
		menu.setListStyle(1);
		menu.setShow(true);
		menu.setShowDate(true);
		this.execute(method, "success");
	}
	
	public void test_menu_group_edit_submit() throws Exception {
		String method = "menu_group_edit_submit";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		MenuFormAction action = createAction(MenuFormAction.class, nameSpace, method);
		menu = action.getModel();
		menu.setComId(442);
		menu.setGroupId(group.getGroupId());
		menu.setGroupName("��Ŀ�޸Ĳ�����Ŀ��"+ new DateUtil().getTime4());
		menu.setFix(false);
		menu.setListStyle(1);
		menu.setShow(true);
		menu.setShowDate(true);
		this.execute(method, "success");		
	}

	public void test_menu_add_submit() throws Exception {
		String method = "menu_add_submit";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		MenuFormAction action = createAction(MenuFormAction.class, nameSpace, method);
		menu = action.getModel();
		menu.setComId(442);
		menu.setGroupId(group.getGroupId());
		menu.setTitle("��Ϣ��Ӳ��Ա���"+ new DateUtil().getTime4());
		menu.setImgId(0);
		menu.setImgPath("");
		menu.setVideoId(0);
		menu.setContent("��Ϣ��Ӳ�������"+ new DateUtil().getTime4());
		menu.setFilePath("");
		this.execute(method, "success");		
	}
	
	public void test_menu_edit_submit() throws Exception {
		String method = "menu_edit_submit";
		Group group = this.getMenuGroup(true);
		if(group == null){
			assertTrue(false);
			return;
		}
		int menuId = this.getMenu(group.getGroupId()).getMenuId();
		MenuFormAction action = createAction(MenuFormAction.class, nameSpace, method);
		menu = action.getModel();
		menu.setComId(442);
		menu.setGroupId(group.getGroupId());
		menu.setMenuId(menuId);
		menu.setTitle("��Ϣ�޸Ĳ��Ա���"+ new DateUtil().getTime4());
		menu.setImgId(0);
		menu.setImgPath("");
		menu.setVideoId(0);
		menu.setContent("��Ϣ�޸Ĳ�������"+ new DateUtil().getTime4());
		menu.setFilePath("");
		this.execute(method, "success");			
	}
}

