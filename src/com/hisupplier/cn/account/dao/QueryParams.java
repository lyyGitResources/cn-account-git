package com.hisupplier.cn.account.dao;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;

public class QueryParams {
	/**
	 * 升序和降序
	 */
	public static final String[] SORT_ORDER = new String[] { "asc", "desc" };

	private LoginUser loginUser; //登录用户
	private boolean ajax; //是否ajax调用
	private boolean distinct; //是否在SQL语句前加distinct，一般不用
	private boolean paging; //是否分页，此参数只在分页和不分页功能并存时用到
	private String queryBy;
	private String queryText;
	private String sortBy;
	private String sortOrder;
	private int pageNo = -1;
	private int pageSize = -1;
	private int startRow = -1;
	private int commentCount = -1; //评论
	
	private boolean showTitle;	// 是否在页面显示Title

	/**
	 * 设置默认排序条件
	 * @param sortFields 
	 * @param sortBy 
	 * @param sortOrder "asc" or "desc"
	 */
	public void setDefaultSort(String[] sortFields, String sortBy, String sortOrder) {
		if (StringUtil.isEmpty(this.sortBy)) {
			this.sortBy = sortBy;
		} else if (!Validator.containsValue(sortFields, this.sortBy)) {
			this.sortBy = sortBy;
		}

		if (StringUtil.isEmpty(this.sortOrder)) {
			this.sortOrder = sortOrder;
		} else if (!Validator.containsValue(SORT_ORDER, this.sortOrder)) {
			this.sortOrder = sortOrder;
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public LoginUser getLoginUser() {
		return this.loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	public boolean isAjax() {
		return this.ajax;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isDistinct() {
		return this.distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getQueryBy() {
		return this.queryBy;
	}

	public void setQueryBy(String queryBy) {
		this.queryBy = queryBy;
	}

	public String getQueryText() {
		return this.queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public String getSortBy() {
		return this.sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

}
