package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.util.PasswordCheck;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class PasswdFormAction extends BasicAction implements ModelDriven<User> {
	private static final long serialVersionUID = 4027291650576314322L;
	private CompanyService companyService;
	private User user = new User();
	private Map<String, Object> result;

	public PasswdFormAction() {
		super();
		this.currentMenu = "member";
	}

	public String passwd_edit_submit() throws Exception {
		if (!Validator.isPassword(user.getPasswd()) 
				|| !Validator.isPassword(user.getOldPasswd()) 
				|| !Validator.isPassword(user.getConfirmPasswd())){ 
			this.addActionError(getText("user.passwd.required"));
		} else if (!user.getPasswd().equals(user.getConfirmPasswd())){
			this.addActionError(getText("user.confirmPasswd.error"));
		} else if (PasswordCheck.check(user.getPasswd()) == 1) {
			this.addActionError(getText("user.passwd.easy"));
		}

		if (this.hasActionErrors()) {
			result = new HashMap<String, Object>();
			result.put("user", user);
			return "input";
		}

		tip = companyService.updatePasswd(user, this.getLoginUser());
		msg = getText(tip);
		if (tip.equals("editSuccess")) {
			this.addMessage(msg);
		} else {
			if(tip.equals("passwdError")){
				this.addActionError(getText("user.passwd.error"));
			}else{
				this.addActionError(msg);
			}

			result = new HashMap<String, Object>();
			result.put("user", user);
			return "input";
		}
		return SUCCESS;
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public User getModel() {
		return user;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
}
