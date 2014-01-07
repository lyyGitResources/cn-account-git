package com.hisupplier.cn.account.basic;

import java.util.HashMap;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.AdminLog;

public class AdminLogDAO extends DAO {
	/**
	 * ∞¥tableName°¢tableId°¢operType=4≤È—ØAdminLog
	 * @param tableId
	 * @param tableName
	 * @return 
	 */
	public AdminLog findAdminLog(int tableId, String tableName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableId", tableId);
		map.put("tableName", tableName);
		return (AdminLog) this.getSqlMapClientTemplate().queryForObject("adminLog.findAdminLog", map);
	}
}
