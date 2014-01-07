/**
 * Created by wuyaohui at 2013-1-21 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.member;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Talk;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author wuyaohui
 *
 */
public class TalkDAO extends DAO {

	@SuppressWarnings("unchecked")
	public List<Talk> findTalks(int userId) {
		return getSqlMapClientTemplate().queryForList("talk.findTalks", userId);
	}
	

	/**
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Talk> findCompanyTalks(int comId) {
		return getSqlMapClientTemplate().queryForList("talk.findCompanyTalks", comId);
	}
	
	public void batchAdd(final List<Talk> talks) {
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			@Override
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				executor.startBatch();
				Talk talk = null;
				for (int i = 0, count = talks.size(); i < count; i++) {
					talk = talks.get(i);
					talk.setId(talk.generateId(i));
					executor.insert("talk.addTalk", talk);
				}
				return executor.executeBatch();
			}
		});
	}
}
