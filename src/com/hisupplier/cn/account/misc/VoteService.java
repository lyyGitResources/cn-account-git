/* 
 * Created by Administrator at Aug 14, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Vote;
import com.hisupplier.cn.account.entity.VoteComment;
import com.hisupplier.cn.account.entity.VoteOption;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.cn.VoteLog;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author sunhailin
 */
public class VoteService {
	private VoteDAO voteDAO;

	/**
	 * 投票列表
	 * @param params
	 * <pre>
	 *  1) pageNo
	 *	2) pageSize
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 *   vote{
	 *		voteId,
	 *		title,
	 *		content,
	 *		voteType,
	 *		endTime,
	 *		tickets,
	 *		createTime,
	 *		modifyTime,
	 *		List<VoteOption> 
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getVoteList(QueryParams params) {
		ListResult<Vote> listResult = this.voteDAO.findVoteList(params);

		List<Vote> voteList = listResult.getList();
		for (Vote vote : voteList) {
			params.setVoteId(vote.getVoteId());
			List<VoteOption> optionList = this.voteDAO.findVoteOptionList(params);
			vote.setOptionList(optionList);
		}
		listResult.setList(voteList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);

		return result;
	}

	/**
	 * 投票详细页
	 * @param params
	 * <pre>
	 * 	1) comId
	 *  2) userId
	 * 	3) voteId
	 *  4) pageNo
	 *	5) pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   vote{
	 *		voteId,
	 *		title,
	 *		content,
	 *		voteType,
	 *		expiryDate,
	 *		tickets,
	 *		createTime,
	 *		modifyTime,
	 *		List<VoteOption> 
	 *    }
	 *    
	 *   VoteComment{
	 *		commemtId,
	 *		content,
	 *		voteId,
	 *		comId,
	 *		userId,
	 *		serId,
	 *		state,
	 *		createTime,
	 *		modifyTime,
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getVote(QueryParams params) {
		Vote vote = this.voteDAO.findVote(params);
		if (vote == null) {
			throw new PageNotFoundException();
			//throw new ServiceException("vote.noExist");
		}
		//根据截止时间判断是否允许投票
		Date expiryDate = DateUtil.parseDateTime(vote.getEndTime());
		Date currentDate = new Date();
		if (currentDate.before(expiryDate)) {
			vote.setEnableVote(true);
		} else if (currentDate.after(expiryDate)) {
			vote.setEnableVote(false);
		}
		//查询评论列表
		Map<String, Object> result = this.getVoteCommentList(params);
		//查询投票选项
		List<VoteOption> optionList = this.voteDAO.findVoteOptionList(params);
		//查询所有的投票记录
		List<VoteLog> voteLogList = this.voteDAO.findVoteLogList(params);

		//voteType == 1时，把投票记录保存到MAP中；voteType == 2时，把投票记录放到每个具体的选项中
		if (vote.getVoteType() == 1) {
			result.put("logListSize", voteLogList.size());
		} else if (vote.getVoteType() == 2) {
			for (VoteOption voteOption : optionList) {
				int logListSize = 0;
				for (VoteLog voteLog : voteLogList) {
					if (voteLog.getOptionId() == voteOption.getOptionId()) {
						logListSize++;
					}
				}
				voteOption.setLogListSize(logListSize);
			}
			result.put("logListSize", 0);
		}

		//统计每个选项的投票百分数
		for (VoteOption voteOption : optionList) {
			String percent = vote.getVoteCount() == 0 ? "0.00" : voteOption.getVoteCount() * 1.0 / vote.getVoteCount() * 100 + "";
			percent = percent.substring(0, percent.indexOf(".") + 2) + "%";
			voteOption.setPercent(percent);
		}

		vote.setOptionList(optionList);
		result.put("vote", vote);
		return result;
	}

	/**
	 * 投票评论列表
	 * @param params
	 * <pre>
	 * 	1) voteId
	 *  2) pageNo
	 *	3) pageSize
	 * </pre>
	 * @return
	 * <pre>
	 * listResult
	 *   VoteComment{
	 *		commemtId,
	 *		content,
	 *		voteId,
	 *		comId,
	 *		userId,
	 *		serId,
	 *		state,
	 *		createTime,
	 *		modifyTime,
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getVoteCommentList(QueryParams params) {
		ListResult<VoteComment> listResult = this.voteDAO.findVoteCommentList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * 投票
	 * @param params
	 * <pre>
	 * 	1) comId
	 *  2) userId
	 * 	3) voteId
	 *  4) voteType
	 *	5) optionId int[]
	 * </pre>
	 * @return
	 * <pre>
	 * 	addSuccess
	 *  vote.radio.tip
	 *  vote.checkBox.tip
	 *  vote.timeout
	 * </pre>
	 */
	public String addVoteOption(QueryParams params) {
		//查询所有投票记录
		List<VoteLog> voteLogList = this.voteDAO.findVoteLogList(params);
		//voteType == 1时，投票记录大于1时，返回已投票提示信息；voteType == 2时，根据投票选项查询投票记录
		if (params.getVoteType() == 1) {
			if (voteLogList.size() > 0) {
				return "vote.radio.tip";
			}
		} else if (params.getVoteType() == 2) {
			for (String optionId : params.getOptionId()) {
				int id = Integer.parseInt(optionId);
				for (VoteLog log : voteLogList) {
					if (log.getOptionId() == id) {
						return "vote.checkBox.tip";
					}
				}
			}
		}
		//根据投票主题查询投票截止时间
		Date expiryDate = DateUtil.parseDateTime(this.voteDAO.findVote(params).getEndTime());
		if (new Date().after(expiryDate)) {
			return "vote.timeout";
		}
		//更新单个投票选项的数量
		UpdateMap optionCount = new UpdateMap("VoteOption");
		optionCount.addField("voteCount", "+", 1);
		optionCount.addWhere("voteId", params.getVoteId());
		optionCount.addWhere("optionId", StringUtil.toString(params.getOptionId(), ","), "in");
		this.voteDAO.update(optionCount);
		//更新投票总数
		UpdateMap voteCount = new UpdateMap("Vote");
		voteCount.addField("voteCount", "+", params.getOptionId().length);
		voteCount.addWhere("voteId", params.getVoteId());
		this.voteDAO.update(voteCount);
		//添加操作记录
		this.voteDAO.addVoteLog(params);
		return "addSuccess";
	}

	/**
	 * 添加评论
	 * @param voteComment
	 * @return 
	 * addSuccess
	 * operateFail
	 */

	/**
	 * 添加评论
	 * @param voteComment
	 * @return
	 * <pre>
	 * 	 addSuccess
	 *   operateFail
	 * </pre>
	 */
	public String addVoteComment(QueryParams params) {
		VoteComment voteComment = new VoteComment();
		voteComment.setContent(params.getContent());
		voteComment.setVoteId(params.getVoteId());
		voteComment.setComId(params.getLoginUser().getComId());
		voteComment.setUserId(params.getLoginUser().getUserId());
		voteComment.setSerId(0);
		voteComment.setState(CN.STATE_WAIT);
		voteComment.setCreateTime(new DateUtil().getDateTime());
		voteComment.setModifyTime(voteComment.getCreateTime());

		int num = this.voteDAO.addVoteComment(voteComment);
		if (num > 0) {
			UpdateMap commentCount = new UpdateMap("Vote");
			commentCount.addField("commentCount", "+", 1);
			commentCount.addWhere("voteId", params.getVoteId());
			this.voteDAO.update(commentCount);
			return "addSuccess";
		} else {
			return "operateFail";
		}
	}

	public void setVoteDAO(VoteDAO voteDAO) {
		this.voteDAO = voteDAO;
	}

}
