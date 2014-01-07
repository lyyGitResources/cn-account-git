/* 
 * Created by baozhimin at Dec 8, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.cn.account.entity.Friend;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.ValidateCode;
import com.hisupplier.commons.util.Validator;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author baozhimin
 */
public class FriendAction extends BasicAction implements ModelDriven<Friend>{

	private static final long serialVersionUID = 6509438725756770068L;
	private Friend friend = new Friend();
	private B2BService b2BService;
	
	public String friend_link() throws Exception {
		currentMenu = "friendLink";
		return super.execute();
	}
	
	public String friend_link_to_us() throws Exception {
		tip = b2BService.addFriend(request, friend);
		if(StringUtil.equals(tip, "operateFail")){
			this.addError(getText(tip));
		}else{
			this.addMessage(getText(tip));
		}

		return super.execute();
	}

	public void validateFriend_link_to_us() {
		super.validate();
		StringUtil.trimToEmpty(friend, "title,link,contact,email,linkus");
		
		if(StringUtil.isNotBlank(friend.getLinkus())){
			if (!Validator.isUrl(friend.getLinkus()) || friend.getLinkus().length() > 120) {
				this.addFieldError("url.required", getText("url.required"));
			}else {
				String website = friend.getLinkus().toLowerCase();
				if (!website.startsWith("http://") && !website.startsWith("https://")) {
					friend.setLinkus("http://" + website);
				}
			}
		}
		
		if (!Validator.isUrl(friend.getLink()) || friend.getLink().length() > 120) {
			this.addFieldError("url.required", getText("url.required"));
		}else {
			String website = friend.getLink().toLowerCase();
			if (!website.startsWith("http://") && !website.startsWith("https://")) {
				friend.setLink("http://" + website);
			}
		}
		
		if (!ValidateCode.isValid(request, friend.getValidateCodeKey(), friend.getValidateCode())){
			this.addFieldError("validateCode.error", TextUtil.getText("validateCode.error", "zh"));
		}
	}


	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Map<String, Object> getResult() {
		return null;
	}

	@Override
	public String getTip() {
		return tip;
	}

	public Friend getModel() {
		return friend;
	}

	public void setB2BService(B2BService service) {
		b2BService = service;
	}

}
