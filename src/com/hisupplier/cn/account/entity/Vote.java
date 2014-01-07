/* 
 * Created by sunhailin at Oct 26, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.entity;

import java.util.List;

/**
 * @author sunhailin
 *
 */
public class Vote extends com.hisupplier.commons.entity.cn.Vote {

	private static final long serialVersionUID = -7628777754029755007L;

	// «∑Ò‘ –ÌÕ∂∆±
	private boolean enableVote;

	private List<VoteOption> optionList;
	private List<VoteComment> commentList;

	public boolean isEnableVote() {
		return enableVote;
	}

	public void setEnableVote(boolean enableVote) {
		this.enableVote = enableVote;
	}

	public List<VoteOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<VoteOption> optionList) {
		this.optionList = optionList;
	}

	public List<VoteComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<VoteComment> commentList) {
		this.commentList = commentList;
	}

}
