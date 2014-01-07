/* 
 * Created by taofeng at Dec 24, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.WebSiteModule;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author taofeng
 *
 */
public class WebsiteModuleDAO extends DAO {

	/**
	 * 添加用户模块
	 * @param modules
	 * @return int
	 */
	public int addModules(final List<WebSiteModule> modules) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (WebSiteModule mod : modules) {
					executor.insert("module.addModules", mod);
				}
				return executor.executeBatch();
			}
		});
	}

	/**
	 * 删除指定公司ID下的所有用户模块
	 * @param comId
	 * @return int
	 */
	public int deleteModules(int comId) {
		return (Integer) this.getSqlMapClientTemplate().delete("module.deleteModules", comId);
	}

	/**
	 * 查询指定公司ID下用户所选择的模块
	 * @param comId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<WebSiteModule> findModules(int comId) {
		List<WebSiteModule> modules = (ArrayList<WebSiteModule>) this.getSqlMapClientTemplate().queryForList("module.findModules", comId);
		if (modules == null) {
			modules = new ArrayList<WebSiteModule>(0);
		}
		return modules;
	}
}
