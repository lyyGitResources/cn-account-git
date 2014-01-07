/* 
 * Created by sunhailin at Nov 16, 2009 
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
public class GroupFormAction extends BasicAction implements ModelDriven<Group> {
	private static final long serialVersionUID = 8801922754287740835L;
	private Group group = new Group();
	private GroupService groupService;

	public String group_add_submit() throws Exception {
		StringUtil.trimToEmpty(group, "groupName,brief");
		boolean isSuccess = true;
		if (group.getGroupName().length() <= 0 || group.getGroupName().length() > 60) {
			msg = this.getText("group.groupName.maxlength");
			isSuccess = false;
		}
		if (group.getBrief().length() <= 0 || group.getBrief().length() > 250) {
			msg = StringUtil.isNotEmpty(msg) ? 
					msg + this.getText("group.brief.maxlength") : this.getText("group.brief.maxlength");
			isSuccess = false;
		}
		
		
		if (isSuccess) {
			tip = this.groupService.addGroup(group, this.getLoginUser());
			if (tip.equals("addSuccess")) {
				msg = getText("group.addSuccess");
			}else {
				//msg = getText(tip);
				msg = getText("group.groupNameRepeat");
			}
		}
		return SUCCESS;
	}

	public String group_edit_submit() throws Exception {
		StringUtil.trimToEmpty(group, "groupName");
		StringUtil.trimToEmpty(group, "brief");
		boolean isSuccess = true;
		if (group.getGroupName().length() <= 0 || group.getGroupName().length() > 60) {
			msg = this.getText("group.groupName.maxlength");
			isSuccess = false;
		}
		if (group.getBrief().length() <= 0 || group.getBrief().length() > 250) {
			msg = StringUtil.isNotEmpty(msg) ? msg + this.getText("group.brief.maxlength") : this.getText("group.brief.maxlength");
			isSuccess = false;
		}
		if (isSuccess) {
			tip = this.groupService.updateGroup(group, this.getLoginUser());
			if (tip.equals("editSuccess")) {
				msg = getText("group.editSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String group_edit() throws Exception {

		return SUCCESS;
	}

	public Group getModel() {
		return group;
	}

	public String getMsg() {
		return msg;
	}

	@JSON(serialize = false)
	public Map<String, Object> getResult() {
		return null;
	}

	public String getTip() {
		return tip;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

}
