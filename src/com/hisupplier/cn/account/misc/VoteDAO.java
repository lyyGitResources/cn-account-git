/* 
 * Created by sunhailin at Oct 26, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Vote;
import com.hisupplier.cn.account.entity.VoteComment;
import com.hisupplier.cn.account.entity.VoteOption;
import com.hisupplier.commons.entity.cn.VoteLog;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author sunhailin
 *
 */
public class VoteDAO extends DAO {

	/**
	 * 查询单个投票
	 * @param params
	 * <pre>
	 *   voteId
	 * </pre>
	 * @return
	 * <pre>
	 *   Vote
	 * </pre>
	 */
	public Vote findVote(QueryParams params) {
		return (Vote) this.getSqlMapClientTemplate().queryForObject("vote.findVote", params);
	}
	
	/**
	 * 查询投票列表
	 * 登录时查询
	 * @return
	 */
	public int findVoteLog(){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("vote.findvote");
	}
	
	/**
	 * 查询投票列表
	 * @param params
	 * <pre>
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult<Vote>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Vote> findVoteList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("vote.findVoteListCount");
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<Vote> list = (ArrayList<Vote>) this.getSqlMapClientTemplate().queryForList("vote.findVoteList", params);
		for (Vote vote : list) {
			vote.setEndTime(DateUtil.formatDateTime(vote.getEndTime()));
			vote.setCreateTime(DateUtil.formatDateTime(vote.getCreateTime()));
			vote.setModifyTime(DateUtil.formatDateTime(vote.getModifyTime()));
		}
		return new ListResult<Vote>(list, page);
	}

	/**
	 * 查询评论列表
	 * @param params
	 * <pre>
	 *   voteId
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult<VoteComment>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public ListResult<VoteComment> findVoteCommentList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("vote.findVoteCommentListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<VoteComment> list = (ArrayList<VoteComment>) this.getSqlMapClientTemplate().queryForList("vote.findVoteCommentList", params);
		for (VoteComment comment : list) {
			comment.setCreateTime(DateUtil.formatDateTime(comment.getCreateTime()));
			comment.setModifyTime(DateUtil.formatDateTime(comment.getModifyTime()));
		}
		return new ListResult<VoteComment>(list, page);
	}

	/**
	 * 查询投票日志
	 * @param params
	 * <pre>
	 *   voteId
	 *   loginUse.comId
	 *   loginUse.userId
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult<VoteLog>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<VoteLog> findVoteLogList(QueryParams params) {
		return (ArrayList<VoteLog>) this.getSqlMapClientTemplate().queryForList("vote.findVoteLogList", params);
	}

	/**
	 * 查询投票选项
	 * @param params
	 * <pre>
	 *   voteId
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult<VoteOption>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<VoteOption> findVoteOptionList(QueryParams params) {
		return (ArrayList<VoteOption>) this.getSqlMapClientTemplate().queryForList("vote.findVoteOptionList", params);
	}

	/**
	 * 添加投票日志
	 * @param params
	 * <pre>
	 *   voteId
	 *   loginUse.comId
	 *   loginUse.userId
	 *   optionId String[]
	 * </pre>
	 * @return
	 * <pre>
	 *   int 添加数量
	 * </pre>
	 */
	public int addVoteLog(final QueryParams params) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (String id : params.getOptionId()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("voteId", params.getVoteId());
					map.put("comId", params.getLoginUser().getComId());
					map.put("userId", params.getLoginUser().getUserId());
					map.put("optionId", id);
					map.put("createTime", new DateUtil().getDateTime());
					executor.update("vote.addVoteLog", map);
				}
				return executor.executeBatch();
			}
		});
	}

	/**
	 * 添加评论
	 * @param voteComment
	 * @return int
	 */
	public int addVoteComment(VoteComment voteComment) {
		return this.getSqlMapClientTemplate().update("vote.addVoteComment", voteComment);
	}
}
