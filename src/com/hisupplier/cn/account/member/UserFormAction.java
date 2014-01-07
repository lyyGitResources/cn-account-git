package com.hisupplier.cn.account.member;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

public class UserFormAction extends BasicAction implements ModelDriven<User> {
	private static final long serialVersionUID = 4027291650576314322L;
	private CompanyService companyService;
	private User user = new User();
	private Map<String, Object> result = new HashMap<String, Object>();

	public UserFormAction() {
		super();
		this.currentMenu = "member";
	}

	public String contact_more_submit() throws Exception {
		String fields = "title,passwd,tqId,province,city,town,headImgPath,linkId,qq,qqcode,msn,msncode,skype," +
		"privilege,admin,sms,show,showMobile,notLoginAlertDate,preLoginTime,lastLoginTime,preLoginIP,lastLoginIP,loginTimes";
		StringUtil.trimToEmpty(user, fields);
		int returnVal = companyService.updateContactMore(user);
		switch (returnVal) {
		case 1 : this.addMessage(getText("addSuccess")); break;
		case 2 : this.addMessage(getText("editSuccess")); break;
		default : this.addActionError(getText("operateFail"));
		}
		result.put("user", user);
		return SUCCESS;
	}
	
	public String contact_edit_submit() throws Exception {
		String fields = "province,contact,street,zip,department,job,email,tel,tel1,tel2,fax,fax1,fax2,mobile,qq,msn,skype,qqcode,msncode,title";
		StringUtil.trimToEmpty(user, fields);

		tip = companyService.updateContact(user, this.getLoginUser());
		msg = getText(tip);
		if (tip.equals("editSuccess")) {
			this.addMessage(msg);
		} else {
			this.addActionError(msg);
			QueryParams params = new QueryParams();
			params.setLoginUser(this.getLoginUser());
			result = new HashMap<String, Object>();
			result.put("user", user);
			return "input";
		}
		return SUCCESS;
	}

	public String user_add_submit() throws Exception {
		if (!Validator.isPassword(user.getPasswd())) {
			this.addActionError(getText("user.passwd.required"));
			result.put("user", user);
			return "input";
		}
		user.setComId(this.getLoginUser().getComId());
		String fields = "contact,street,zip,department,job,email,tel,tel1,tel2," +
				"fax,fax1,fax2,mobile,qq,msn,skype,qqcode,msncode,linkId,title";
		StringUtil.trimToEmpty(user, fields);

		String ip = request.getRemoteAddr();
		user.setPreLoginIP(ip);
		user.setLastLoginIP(ip);

		tip = companyService.addUser(user, this.getLoginUser());
		msg = getText(tip);
		if (tip.equals("addSuccess")) {
			this.addMessage(msg);
		} else {
			this.addActionError(msg);
			result.put("user", user);
			return "input";
		}
		return SUCCESS;
	}

	public String user_edit_submit() throws Exception {
		if (!Validator.isPassword(user.getPasswd())) {
			this.addActionError(getText("user.passwd.required"));
			result = new HashMap<String, Object>();
			result.put("user", user);
			return "input";
		}
		user.setComId(this.getLoginUser().getComId());
		String fields = "contact,street,zip,department,job,email,tel,tel1,tel2," +
				"fax,fax1,fax2,mobile,qq,msn,skype,qqcode,msncode,linkId,title";
		StringUtil.trimToEmpty(user, fields);

		tip = companyService.updateUser(user, this.getLoginUser());
		msg = getText(tip);
		if (tip.equals("editSuccess")) {
			result.put("user", user);
			this.addMessage(msg);
			this.addActionMessage(msg);
		} else {
			this.addActionError(msg);
			result.put("user", user);
			return "input";
		}
		return SUCCESS;
	}
	
	
	@Override
	public void validate() {
		super.validate();

		if(Validator.isTel(user.getTel1(), user.getTel2())){
			user.setTel(user.getTel1() + "-" + user.getTel2());
		}else{
			this.addActionError("请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用\",\"或\"/\"分隔，分机号码请用\"-\"分隔");
		}
		
		if(StringUtil.isNotEmpty(user.getFax1()) && StringUtil.isNotEmpty(user.getFax2())){
			if(Validator.isTel(user.getFax1(), user.getFax2())){
				user.setFax(user.getFax1() + "-" + user.getFax2());
			}else{
				this.addActionError("请输入区号和传真号码，区号长度3-5位数字，传真号码长度在7-26个字符内，多个传真用','或\"/\"分隔，分机号码请用\"-\"分隔");
			}
		} else {
			user.setFax("");
		}

		if (user.getPrivilege() != null) {
			String[] tmps = StringUtil.split(user.getPrivilege(), ", ");
			user.setPrivilege(StringUtil.toString(tmps, ","));
		}
		if (this.hasActionErrors() || this.hasFieldErrors()) {
			result.put("user", user);
		}
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
