package com.hisupplier.cn.account.basic;

import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.commons.entity.cn.UserSuggest;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

public class UserSuggestDAO extends DAO {

	/**
	 * ∞¥comId°¢id desc≤È—ØUserSuggest£¨∑µªÿsuggestList
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<UserSuggest> findUserSuggestList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("userSuggest.findUserSuggestListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<UserSuggest> userSuggestList = this.getSqlMapClientTemplate().queryForList("userSuggest.findUserSuggestList", params);

		for (int i = 0; i < userSuggestList.size(); i++) {
			UserSuggest userSuggest = userSuggestList.get(i);
			userSuggest.setCreateTime(DateUtil.formatDateTime(userSuggest.getCreateTime()));
		}
		return new ListResult<UserSuggest>(userSuggestList, page);
	}

	public int addUserSugggest(UserSuggest userSuggest) {
		return (Integer) this.getSqlMapClientTemplate().insert("userSuggest.addUserSugggest", userSuggest);
	}
}
