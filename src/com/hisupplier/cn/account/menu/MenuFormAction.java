package com.hisupplier.cn.account.menu;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.util.PatentUtil;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class MenuFormAction extends BasicAction implements ModelDriven<Menu> {
	private static final long serialVersionUID = 1039901568329098389L;
	private MenuService menuService;
	private Menu menu = new Menu();
	private Group group = new Group();
	private Map<String, Object> result = null;

	public String menu_group_add_submit() throws Exception {
		StringUtil.trimToEmpty(menu, "groupName");
		
		if (StringUtil.isEmpty(menu.getGroupName()) || 
				menu.getGroupName().length() > 60) {
			msg = getText("group.groupName.required");
		} else {
			tip = menuService.addMenuGroup(menu, this.getLoginUser());
			if (tip.equals("addSuccess")) {
				msg = getText("addSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String menu_group_edit_submit() throws Exception {
		StringUtil.trimToEmpty(menu, "groupName");
		
		if (StringUtil.isEmpty(menu.getGroupName()) || 
				menu.getGroupName().length() > 60) {
			msg = getText("group.groupName.required");
		} else {
			tip = menuService.updateMenuGroup(menu, this.getLoginUser());
			if (tip.equals("addSuccess")) {
				msg = getText("addSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String menu_add_submit() throws Exception {
		this.validateMenuForm();
		if (this.hasFieldErrors()) {
			result = menuService.getMenuSubmitError(menu, request);
			return INPUT;
		}
		tip = menuService.addMenu(menu, this.getLoginUser());
		if (!tip.equals("addSuccess")) {
			result = new HashMap<String, Object>();
			result.put("menu", menu);
			this.addActionError(getText(tip));
			return INPUT;
		}
		if (getLoginUser().getMemberType() == 2) {
			this.addMessage(getText(tip) + "<br/>" + getText("memo.company.info.gold"));
		}else {
			this.addMessage(getText(tip) + "<br/>" + getText("memo.company.info.free"));
		}
		return SUCCESS;
	}

	public String menu_edit_submit() throws Exception {
		this.validateMenuForm();
		if (this.hasFieldErrors()) {
			result = menuService.getMenuSubmitError(menu, request);
			return INPUT;
		}
		tip = menuService.updateMenu(menu, this.getLoginUser());
		if (!tip.equals("editSuccess")) {
			result = new HashMap<String, Object>();
			result.put("menu", menu);
			this.addActionError(getText(tip));
			return INPUT;
		}
		if (getLoginUser().getMemberType() == 2) {
			this.addMessage(getText(tip) + "<br/>" + getText("memo.company.info.gold"));
		}else {
			this.addMessage(getText(tip) + "<br/>" + getText("memo.company.info.free"));
		}
		return SUCCESS;
	}

	public void validateMenuForm() {
		StringUtil.trimToEmpty(menu, "title,content");
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("title", "标题,");
		String patentKeyword = PatentUtil.checkKeyword(menu, map, this.getLoginUser().getComId());
		if (StringUtil.isNotEmpty(patentKeyword)) {
			this.addFieldError("menu.content.error", this.getText("menu.content.error") + patentKeyword.split(",")[1]);
		}
		
		// 过滤超链接
		menu.setContent(StringUtil.filterHyperlink(menu.getContent()));
		
		if (StringUtil.isEmpty(menu.getTitle()) || menu.getTitle().length() > 120) {
			this.addFieldError("menu.title.required", this.getText("menu.title.required"));
		}
		if (menu.getAttachment() != null) {
			if (!StringUtil.containsValue(ALLOW_MIME_TYPE, menu.getAttachmentContentType())) {
				this.addFieldError("attachment.invalid", this.getText("attachment.invalid"));
			} else if (menu.getAttachment().length() / 1024 > 500) {
				this.addFieldError("attachment.invalid", this.getText("attachment.invalid"));
			}
		}

	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public Menu getModel() {
		return menu;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
