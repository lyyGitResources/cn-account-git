/* 
 * Created by Administrator at Aug 27, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.misc;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Comment;
import com.hisupplier.commons.entity.cn.CommentReply;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author sunhailin
 */
public class CommentDAO extends DAO {

	/**
	 * 查询评论
	 * @param params
	 * <pre>
	 *   comId		默认1
	 *   commentOption 区分产品评论与公司评论
	 *   pageNo
	 *   pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Comment> findCommentList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("comment.findCommentListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Comment> list = (ArrayList<Comment>) this.getSqlMapClientTemplate().queryForList("comment.findCommentList", params);
		for (Comment comment : list) {
			comment.setCreateTime(DateUtil.formatDateTime(comment.getCreateTime()));
			comment.setModifyTime(DateUtil.formatDateTime(comment.getModifyTime()));
		}
		return new ListResult<Comment>(list, page);
	}

	/**
	 * 查询留言条数
	 * @param comId
	 * @return
	 */
	public int findCommentCount(int comId){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("comment.findCommentCount",comId);
	}
	/**
	 * 查询评论回复
	 * @param params
	 * <pre>
	 *   comId		默认1
	 *   commentId int[]
	 * </pre>
	 * @return
	 * <pre>
	 *   ListResult
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<CommentReply> findCommentReplyList(QueryParams params) {
		return (ArrayList<CommentReply>) this.getSqlMapClientTemplate().queryForList("comment.findCommentReplyList", params);
	}

	/**
	 * 添加评论
	 * @param reply
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addReply(CommentReply reply) {
		return (Integer) this.getSqlMapClientTemplate().insert("comment.addReply", reply);
	}
}
