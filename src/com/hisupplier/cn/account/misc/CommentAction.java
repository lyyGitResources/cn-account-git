/* 
 * Created by sunhailin at Oct 29, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Map;

import com.hisupplier.cn.account.basic.BasicAction;
import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author sunhailin
 *
 */
public class CommentAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -5349839875387557605L;
	private QueryParams params = new QueryParams();
	private CommentService commentService;
	private Map<String, Object> result;

	public String comment_list() throws Exception {
		result = this.commentService.getCommentList(params);
		params.setShowTitle(true);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String comment_reply() throws Exception {
		result = this.commentService.getCommentReply(params);
		return SUCCESS;
	}

	public String comment_reply_submit() throws Exception {
		StringUtil.trimToEmpty(params, "content");
		if (params.getContent().length() > 2000 || params.getContent().length() <= 0) {
			msg = this.getText("commentReply.content.maxlength");
		} else if (params.getCommentId() == null || params.getCommentId().length > 1) {
			msg = this.getText("commentReply.addError");
		} else {
			tip = this.commentService.addCommentReply(params);
			if (tip.equals("addSuccess")) {
				msg = getText("commentReply.addSuccess");
			} else {
				msg = getText(tip);
			}
		}
		return SUCCESS;
	}

	public String getMsg() {
		return msg;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public String getTip() {
		return tip;
	}

	public QueryParams getModel() {
		return params;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

}
