/* 
 * Created by sunhailin at Oct 29, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Comment;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.cn.CommentReply;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author sunhailin
 *
 */
public class CommentService {
	private CommentDAO commentDAO;

	/**
	 * 查找评论列表
	 * @param params
	 * @return
	 * <pre>
	 *   listResult
	 *   commentType
	 * </pre>
	 */
	public Map<String, Object> getCommentList(QueryParams params) {
		ListResult<Comment> listResult = this.commentDAO.findCommentList(params);

		if (listResult.getList().size() > 0) {
			int[] commentId = new int[listResult.getList().size()];
			for (int i = 0; i < listResult.getList().size(); i++) {
				commentId[i] = listResult.getList().get(i).getCommentId();
			}
			params.setCommentId(commentId);
			List<CommentReply> replyList = this.commentDAO.findCommentReplyList(params);

			for (Comment comment : listResult.getList()) {
				List<CommentReply> list = new ArrayList<CommentReply>(listResult.getList().size());
				for (CommentReply reply : replyList) {
					if (comment.getCommentId() == reply.getCommentId()) {
						list.add(reply);
					}
				}
				comment.setReplyList(list);
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("commentType", params.getCommentType());
		return result;
	}

	/**
	 * 添加评论回复时取得的信息
	 * @param params
	 * <pre>
	 * 	commentId
	 * </pre>
	 * @return
	 * <pre>
	 *   commentId
	 * </pre>
	 */
	public Map<String, Object> getCommentReply(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("commentId", params.getCommentId()[0]);
		return result;
	}

	/**
	 * 添加评论回复时取得的信息
	 * @param params
	 * <pre>
	 * 	loginUser.comId
	 *  loginUser.userId
	 *  commentId
	 *  content
	 *  loginUser.lastLoginIp
	 * </pre>
	 * @return
	 * <pre>
	 *   addSuccess
	 * </pre>
	 */
	public String addCommentReply(QueryParams params) {
		int commentId = params.getCommentId()[0];
		CommentReply reply = new CommentReply();
		reply.setComId(params.getLoginUser().getComId());
		reply.setCommentId(commentId);
		reply.setContent(params.getContent());
		reply.setAuthorComId(params.getLoginUser().getComId());
		reply.setAuthorUserId(params.getLoginUser().getComId());
		reply.setAuthorIP(params.getLoginUser().getLastLoginIP());
		reply.setState(CN.STATE_PASS);
		String dateTime = new DateUtil().getDateTime();
		reply.setCreateTime(dateTime);
		reply.setModifyTime(dateTime);
		int num = this.commentDAO.addReply(reply);
		if (num < 0) {
			return "operateFail";
		}
		UpdateMap replyCount = new UpdateMap("Comment");
		replyCount.addField("replyCount", "+", 1);
		replyCount.addWhere("comId", params.getLoginUser().getComId());
		replyCount.addWhere("commentId", commentId);
		this.commentDAO.update(replyCount);
		return "addSuccess";
	}

	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
	}

}
