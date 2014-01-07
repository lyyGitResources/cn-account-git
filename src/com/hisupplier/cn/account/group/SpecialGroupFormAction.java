/* 
 * Created by sunhailin at Nov 18, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class SpecialGroupFormAction extends BasicAction implements ModelDriven<Group> {
	private static final long serialVersionUID = -34981873665415120L;
	private Group group = new Group();
	private SpecialGroupService specialGroupService;
	private Map<String, Object> result;

	public String group_add_submit() throws Exception {
		StringUtil.trimToEmpty(group, "groupName");
		if (group.getGroupName().length() <= 0 || group.getGroupName().length() > 60) {
			msg = this.getText("specialGroup.groupName.required");
		} else if (specialGroupService.checkSpecialGroupRepeat(group)) {
			tip = "repeat";
			msg = this.getText("group.groupNameRepeat");
		} else {
			tip = this.specialGroupService.addGroup(group, this.getLoginUser());
			if (tip.equals("addSuccess")) {
				msg = getText("specialGroup.addSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String group_edit_submit() throws Exception {
		StringUtil.trimToEmpty(group, "groupName");
		if (group.getGroupName().length() <= 0 || group.getGroupName().length() > 60) {
			msg = this.getText("specialGroup.groupName.required");
		}else if(specialGroupService.checkSpecialGroupRepeat(group)){
			tip = "repeat";
			msg = this.getText("group.groupNameRepeat");
		} else {
			tip = this.specialGroupService.updateGroup(group, this.getLoginUser());
			if (tip.equals("editSuccess")) {
				msg = getText("specialGroup.editSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	@JSON(serialize = false)
	public Map<String, Object> getResult() {
		return result;
	}

	public String getTip() {
		return tip;
	}

	public Group getModel() {
		return group;
	}

	public void setSpecialGroupService(SpecialGroupService specialGroupService) {
		this.specialGroupService = specialGroupService;
	}

}
