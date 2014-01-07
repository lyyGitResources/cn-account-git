/* 
 * Created by sunhailin at Oct 27, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import com.hisupplier.cn.account.entity.Vote;
import com.hisupplier.cn.account.misc.QueryParams;
import com.hisupplier.cn.account.misc.VoteAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author sunhailin
 *
 */
public class VoteActionTest extends ActionTestSupport {
	private String namespace = "/vote";
	private VoteAction action = null;
	private QueryParams params = null;

	public void test_vote_list() throws Exception {
		String method = "vote_list";
		action = this.createAction(VoteAction.class, namespace, method);
		this.execute(method, "success");
	}
	
	public void test_vote_view() throws Exception {
		String method = "vote_view";
		Vote vote = this.getVote();
		if(vote != null){
			action = this.createAction(VoteAction.class, namespace, method);
			params = action.getModel();
			params.setVoteId(vote.getVoteId());
			this.execute(method, "success");
		}
	}
	
	public void test_vote_option_add() throws Exception {
		String method = "vote_option_add";
		Vote vote = this.getVote();
		if(vote != null){
			action = this.createAction(VoteAction.class, namespace, method);
			params = action.getModel();
			params.setVoteId(vote.getVoteId());
			params.setOptionId(new String[]{vote.getOptionList().get(0).getOptionId()+""});
			this.execute(method, "error");
		}
	}
	
	public void test_vote_comment_add() throws Exception {
		String method = "vote_comment_add";
		Vote vote = this.getVote();
		if(vote != null){
			action = this.createAction(VoteAction.class, namespace, method);
			params = action.getModel();
			params.setVoteId(vote.getVoteId());
			params.setContent("tyesgs");
			this.execute(method, "success");
		}
		
	}
	
}
