/* 
 * Created by baozhimin at Dec 8, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import com.hisupplier.cn.account.entity.Friend;
import com.hisupplier.cn.account.test.ActionTestSupport;
import com.hisupplier.cn.account.user.FriendAction;
import com.hisupplier.commons.util.ValidateCode;

/**
 * @author baozhimin
 */
public class FriendActionTest extends ActionTestSupport {

	public void test_friend_link_to_us() throws Exception{
		String method = "friend_link_to_us";
		FriendAction action = createAction(FriendAction.class, "/user", method);
		Friend friend = action.getModel();
		friend.setContact("bao");
		friend.setEmail("bao@163.com");
		friend.setCatId(1);
		friend.setTitle("bao");
		friend.setLink("http://www.bao.com");
		friend.setImgPath("http://www.bao.com/bao.jsp");
		friend.setBrief("baobao");

		ValidateCode.setCode(request, "123", "abc");
		friend.setValidateCodeKey("123");
		friend.setValidateCode("abc");
		
		this.execute(method, "success");
	}
}
