package com.hisupplier.cn.account.basic;

import java.util.Map;

import com.hisupplier.commons.entity.cn.UserSuggest;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class UserSuggestFormAction extends BasicAction implements ModelDriven<UserSuggest>{
	
	private static final long serialVersionUID = -4036317617542417484L;
	private BasicService basicService;
	private Map<String, Object> result ;
	private UserSuggest userSuggest = new UserSuggest();
	
	public String user_suggest_add() throws Exception {
		if(StringUtil.isEmpty(userSuggest.getTitle()) || userSuggest.getTitle().length() > 120){
			tip = "userSuggest.title.required";
			msg = this.getText(tip);
			return SUCCESS;
		}
		if(StringUtil.isEmpty(userSuggest.getContent()) || userSuggest.getContent().length() > 3000){
			tip = "userSuggest.content.required";
			msg = this.getText(tip);
			return SUCCESS;
		}
		QueryParams params = new QueryParams();
		params.setLoginUser(this.getLoginUser());
		tip = basicService.addUserSuggest(userSuggest);
		params.setPageSize(15);
		result = basicService.getUserSuggestList(params);
		msg = this.getText(tip);
		return SUCCESS;
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

	public UserSuggest getModel() {
		return userSuggest;
	}

	public void setBasicService(BasicService basicService) {
		this.basicService = basicService;
	}


}
