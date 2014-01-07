/* 
 * Created by gaoshan at Jun 1, 2010 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.plugin;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author gaoshan
 *
 */
public class CrmService {

	private CompanyDAO companyDAO;
	private UserDAO userDAO;

	/**
	 * 按comId和userId更新crm同步状态
	 * @param longinUser
	 * @param tableName
	 * @return success operateFail
	 */
	public String updateCRMState(LoginUser longinUser, String tableName) {
		UpdateMap updateMap = new UpdateMap(tableName);
		updateMap.addField("crmState", 1);
		if (StringUtil.equals("Users", tableName)) {
			updateMap.addWhere("userId", longinUser.getUserId());
		}
		updateMap.addWhere("comId", longinUser.getComId());
		return (Integer) userDAO.update(updateMap) > 0 ? "success" : "operateFail";
	}

	public Company getCompany(int comId) {
		return (Company) this.companyDAO.findCompany(comId);
	}

	public User getUser(int userId, int comId) {
		return (User) this.userDAO.findUser(userId, comId);
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
