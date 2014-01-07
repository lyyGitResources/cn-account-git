package com.hisupplier.cn.account.member;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;

	private CompanyService companyService;
	private QueryParams params = new QueryParams();
	private Map<String, Object> result;


	public String contact_more_delete() throws Exception {
		if (companyService.deleteContactMore(params))	
			this.addMessage(getText("deleteSuccess"));
		else 
			this.addActionError(getText("operateFail"));
		return SUCCESS;
	}
	
	public String contact_edit() throws Exception {
		result = companyService.getContactEdit(params,0);
		return SUCCESS;
	}
	
	public String contact_view() throws Exception {
		result = companyService.getContactEdit(params,1);
		User user =(User)result.get("user");
		if(user.getState() == 10){
			response.sendRedirect("/member/contact_edit.htm");
		} 
		return SUCCESS;
	}
	public String contact_more() throws Exception {
		result = companyService.getMoreContactEdit(params);
		return SUCCESS;
	}
	
	public String passwd_edit() throws Exception {
		result = companyService.getPasswdEdit(params, request);
		return SUCCESS;
	}

	public String user_list() throws Exception {
		result = companyService.getUserList(params);
		return SUCCESS;
	}

	public String user_add() throws Exception {
		result = companyService.getUserAdd(params);
		if (result == null) {
			tip = "user.count.limit";
			msg = getText(tip);
			this.addActionError(msg);
			return "error";
		}
		return SUCCESS;
	}

	public String user_edit() throws Exception {
		result = companyService.getUserEdit(params);
		return SUCCESS;
	}
	public String user_detail() throws Exception{
		result = companyService.getUserEdit(params);
		return "select";
	}

	public String user_delete() throws Exception {
		tip = companyService.deleteUser(params);
		msg = getText(tip);
		if (tip.equals("deleteSuccess")) {
			this.addMessage(msg);
		} else {
			this.addActionError(msg);
			return "error";
		}
		return SUCCESS;
	}

	public String forget_passwd() throws Exception {
		result = null;
		return SUCCESS;
	}

	public String check_email() throws Exception {
		tip = "false";
		if(StringUtil.isNotEmpty(params.getFromEmail())){
			params.setEmail(params.getFromEmail());
		}
		if (!Validator.isEmail(params.getEmail())) {
			msg = getText("email.required");

		} else if (companyService.checkEmail(params).equals("used")) {
			msg = getText("email.used");

		} else {
			tip = "true";
		}
		return SUCCESS;
	}
	
	/**
	 * 修改密码时，验证当前密码是否正确
	 * @return
	 * @throws Exception
	 */
	public String check_password() throws Exception {
		tip = "false";
		if (StringUtil.isEmpty(params.getOldPasswd())) {
			msg = "请输入您的当前密码。";
		} else if (!companyService.checkUserPassword(request, params.getOldPasswd())){
			msg = "当前密码错误。";
		} else {
			tip = "true";
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

	public QueryParams getModel() {
		return params;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

}
