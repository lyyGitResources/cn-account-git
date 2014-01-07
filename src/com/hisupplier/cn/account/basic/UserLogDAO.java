package com.hisupplier.cn.account.basic;

import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

public class UserLogDAO extends DAO {
	/**
	 * ∞¥comId°¢userId ≤È—ØUserLog
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ListResult<UserLog> findUserLogList(QueryParams params) {
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("userLog.findUserLogListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<UserLog> userLogList = this.getSqlMapClientTemplate().queryForList("userLog.findUserLogList",params);
		
		for (int i = 0; i < userLogList.size(); i++) {
			UserLog userLog = userLogList.get(i);
			userLog.setCreateTime(DateUtil.formatDate(userLog.getCreateTime()));
		}
		return new ListResult<UserLog>(userLogList, page);
	}

}
