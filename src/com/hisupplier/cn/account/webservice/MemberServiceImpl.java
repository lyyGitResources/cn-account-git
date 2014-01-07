/* 
 * Created by taofeng at 2010-7-14 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.webservice;

import java.util.Iterator;
import java.util.Map;

import javax.jws.WebService;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.commons.entity.cn.Company;
import com.hisupplier.commons.entity.cn.User;

/**
 * @author taofeng
 */
@WebService(endpointInterface = "com.hisupplier.cn.account.webservice.MemberService")
public class MemberServiceImpl implements MemberService {

	private CompanyDAO companyDAO;
	private UserDAO userDAO;

	public Company getCompany(int comId) {
		return this.companyDAO.findCompany(comId);
	}

	public User getUser(int userId, int comId) {
		if (userId > 0) {
			return this.userDAO.findUserById(userId);
		}
		return this.userDAO.findUserByAdmin(comId);
	}

	public String updateComExtra(int comId, Map<String, Integer> column) {
		UpdateMap updateMap = new UpdateMap("CompanyExtra");
		Iterator<Map.Entry<String, Integer>> it = column.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> e = it.next();
			updateMap.addField(e.getKey(), e.getValue());
		}
		updateMap.addWhere("comId", comId);
		return this.companyDAO.update(updateMap) > 0 ? "suc" : "err";
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
