/* 
 * Created by sunhailin at Oct 27, 2009 
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
public class VoteAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = 190296779822239255L;
	private QueryParams params = new QueryParams();
	private VoteService voteService;
	private Map<String, Object> result;

	public String vote_list() throws Exception {
		result = this.voteService.getVoteList(params);
		return SUCCESS;
	}

	public String vote_view() throws Exception {
		if (params.isAjax()) {
			result = this.voteService.getVoteCommentList(params);
		} else {
			result = this.voteService.getVote(params);
		}
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String vote_option_add() throws Exception {
		tip = this.voteService.addVoteOption(params);
		if (!StringUtil.equalsIgnoreCase(tip, "addSuccess")) {
			result = this.voteService.getVote(params);
			this.addActionError(getText(tip));
			return INPUT;
		}
		this.addMessage(getText("vote.addSuccess"));
		return SUCCESS;
	}

	public String vote_comment_add() throws Exception {
		StringUtil.trimToEmpty(params, "content");
		if (params.getContent().length() > 3000) {
			msg = getText("voteComment.content.maxlength");
		} else {
			tip = this.voteService.addVoteComment(params);
			if (tip.equals("addSuccess")) {
				msg = getText("voteComment.addSuccess");
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

	public void setVoteService(VoteService voteService) {
		this.voteService = voteService;
	}
}
