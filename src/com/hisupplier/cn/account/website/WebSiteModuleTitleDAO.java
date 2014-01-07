/* 
 * Created by sunhailin at Apr 27, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.website;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.commons.entity.cn.WebSiteModuleTitle;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * @author sunhailin
 *
 */
public class WebSiteModuleTitleDAO extends DAO {

	/**
	 * 添加标题模块
	 * @param modules
	 * @return int
	 */
	public int addModuleTitle(final List<WebSiteModuleTitle> moduleTitleList) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (WebSiteModuleTitle moduleTitle : moduleTitleList) {
					executor.insert("module_title.addModuleTitle", moduleTitle);
				}
				return executor.executeBatch();
			}
		});
	}

	/**
	 * 删除指定公司ID下的所有标题模块
	 * @param comId
	 * @return int
	 */
	public int deleteModuleTitle(int comId) {
		return (Integer) this.getSqlMapClientTemplate().delete("module_title.deleteModuleTitle", comId);
	}

	/**
	 * 根据comId查询模块标题
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebSiteModuleTitle> findModuleTitleList(int comId) {
		return (ArrayList<WebSiteModuleTitle>) this.getSqlMapClientTemplate().queryForList("module_title.findModuleTitleList", comId);
	}
}
