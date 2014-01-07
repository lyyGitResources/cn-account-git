package com.hisupplier.cn.account.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.hisupplier.cn.account.entity.UserLog;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author linliuwei
 */
public class DAO extends SqlMapClientDaoSupport {

	public int update(UpdateMap updateMap) {
		return this.getSqlMapClientTemplate().update("common.update", updateMap);
	}

	public int delete(UpdateMap updateMap) {
		return this.getSqlMapClientTemplate().update("common.delete", updateMap);
	}

	public int update(final List<UpdateMap> updateMapList) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (UpdateMap updateMap : updateMapList) {
					executor.update("common.update", updateMap);
				}
				return executor.executeBatch();
			}
		});
	}
	
	public int update(final UpdateMap[] updateMaps) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (UpdateMap updateMap : updateMaps) {
					executor.update("common.update", updateMap);
				}
				return executor.executeBatch();
			}
		});
	}

	public int addUserLog(UserLog userLog){
		return (Integer) this.getSqlMapClientTemplate().insert("userLog.addUserLog", userLog);
	}
}
