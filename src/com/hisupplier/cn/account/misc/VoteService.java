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
	 * ͶƱ�б�
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
	 * ͶƱ��ϸҳ
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
		//���ݽ�ֹʱ���ж��Ƿ�����ͶƱ
		Date expiryDate = DateUtil.parseDateTime(vote.getEndTime());
		Date currentDate = new Date();
		if (currentDate.before(expiryDate)) {
			vote.setEnableVote(true);
		} else if (currentDate.after(expiryDate)) {
			vote.setEnableVote(false);
		}
		//��ѯ�����б�
		Map<String, Object> result = this.getVoteCommentList(params);
		//��ѯͶƱѡ��
		List<VoteOption> optionList = this.voteDAO.findVoteOptionList(params);
		//��ѯ���е�ͶƱ��¼
		List<VoteLog> voteLogList = this.voteDAO.findVoteLogList(params);

		//voteType == 1ʱ����ͶƱ��¼���浽MAP�У�voteType == 2ʱ����ͶƱ��¼�ŵ�ÿ�������ѡ����
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

		//ͳ��ÿ��ѡ���ͶƱ�ٷ���
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
	 * ͶƱ�����б�
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
	 * ͶƱ
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
		//��ѯ����ͶƱ��¼
		List<VoteLog> voteLogList = this.voteDAO.findVoteLogList(params);
		//voteType == 1ʱ��ͶƱ��¼����1ʱ��������ͶƱ��ʾ��Ϣ��voteType == 2ʱ������ͶƱѡ���ѯͶƱ��¼
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
		//����ͶƱ�����ѯͶƱ��ֹʱ��
		Date expiryDate = DateUtil.parseDateTime(this.voteDAO.findVote(params).getEndTime());
		if (new Date().after(expiryDate)) {
			return "vote.timeout";
		}
		//���µ���ͶƱѡ�������
		UpdateMap optionCount = new UpdateMap("VoteOption");
		optionCount.addField("voteCount", "+", 1);
		optionCount.addWhere("voteId", params.getVoteId());
		optionCount.addWhere("optionId", StringUtil.toString(params.getOptionId(), ","), "in");
		this.voteDAO.update(optionCount);
		//����ͶƱ����
		UpdateMap voteCount = new UpdateMap("Vote");
		voteCount.addField("voteCount", "+", params.getOptionId().length);
		voteCount.addWhere("voteId", params.getVoteId());
		this.voteDAO.update(voteCount);
		//��Ӳ�����¼
		this.voteDAO.addVoteLog(params);
		return "addSuccess";
	}

	/**
	 * �������
	 * @param voteComment
	 * @return 
	 * addSuccess
	 * operateFail
	 */

	/**
	 * �������
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
