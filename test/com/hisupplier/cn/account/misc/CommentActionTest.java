/* 
 * Created by sunhailin at Nov 2, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.entity.Comment;
import com.hisupplier.cn.account.misc.CommentAction;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class CommentActionTest extends ActionTestSupport {
	private String namespace = "/comment";
	private CommentAction action = null;
	private QueryParams params = null;

	public void test_comment_list_company() throws Exception {
		String method = "comment_list";
		action = this.createAction(CommentAction.class, namespace, method);
		this.execute(method, "success");
	}

	public void test_comment_list_product() throws Exception {
		String method = "comment_list";
		action = this.createAction(CommentAction.class, namespace, method);
		params = this.action.getModel();
		params.setCommentType("product");
		this.execute(method, "success");
	}

	public void test_comment_reply_company() throws Exception {
		String method = "comment_reply";

		Comment comment = this.getComment("company");
		if(comment != null){
			action = this.createAction(CommentAction.class, namespace, method);
			params = this.action.getModel();
			params.setCommentId(new int[] { comment.getCommentId() });
			params.setContent("this is test");
			this.execute(method, "success");
		}
	}
	
	public void test_comment_reply_product() throws Exception {
		String method = "comment_reply";

		Comment comment = this.getComment("product");
		if(comment != null){
			action = this.createAction(CommentAction.class, namespace, method);
			params = this.action.getModel();
			params.setCommentId(new int[] { comment.getCommentId() });
			params.setContent("this is test");
			this.execute(method, "success");
		}
	}
}
