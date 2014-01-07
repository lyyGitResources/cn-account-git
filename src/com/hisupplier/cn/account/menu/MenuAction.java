package com.hisupplier.cn.account.menu;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Menu;
import com.opensymphony.xwork2.ModelDriven;

public class MenuAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private QueryParams params = new QueryParams();
	private MenuService menuService;
	private Map<String, Object> result;

	public String menu_group_list() throws Exception {
		result = menuService.getMenuGroupList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String menu_group_add() throws Exception {
		result = menuService.getMenuGroupAdd(params);
		if (result.get("msg").equals("full")) {
			result.put("tip", getText("group.full"));
		}
		return SUCCESS;
	}

	public String menu_group_edit() throws Exception {
		result = menuService.getMenuGroupEdit(params);
		result.put("result", "");
		return SUCCESS;
	}

	public String menu_group_delete() throws Exception {
		tip = menuService.deleteMenuGroup(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("deleteSuccess");
		} else {
			this.addActionError(getText("operateFail"));
		}
		return SUCCESS;
	}

	public String menu_list() throws Exception {
		result = menuService.getMenuList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}
 
	public String menu_add() throws Exception {
		Menu menu = new Menu();
		menu.setGroupId(params.getGroupId());
		result = menuService.getMenuAdd(params);
		result.put("menu", menu);
		return SUCCESS;
	}

	public String menu_edit() throws Exception {
		result = menuService.getMenuEdit(params);
		return SUCCESS;
	}

	public String menu_delete() throws Exception {
		tip = menuService.deleteMenu(params);
		if (tip.equals("deleteSuccess")) {
			this.addMessage("deleteSuccess");
		} else {
			this.addActionError(getText("operateFail"));
		}
		return SUCCESS;
	}

	public String menu_group_order() throws Exception {
		result = menuService.getMenuGroupList(params);
		return SUCCESS;
	}

	public String menu_order() throws Exception {
		params.setPageSize(30);//×î¶à30Ìõ
		result = menuService.getMenuList(params);
		return SUCCESS;
	}

	public String menu_order_submit() throws Exception {
		tip = menuService.updateMenuOrder(params);
		if (tip.equals("orderSuccess")) {
			this.addMessage(tip);
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	public String menu_group_order_submit() throws Exception {
		tip = menuService.updateMenuGroupOrder(params);
		if (tip.equals("orderSuccess")) {
			this.addMessage(tip);
		} else {
			this.addError(tip);
		}
		return SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public String getTip() {
		return null;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
