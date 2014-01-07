package com.hisupplier.cn.account.basic;

import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.commons.entity.cn.Service;

public class ServiceDAO extends DAO {

	/**
	 * ��comId��ѯService������CompanyService������serviceList
	 * @param comId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Service> findCompanyServiceList(int comId) {
		return (List<Service>) this.getSqlMapClientTemplate().queryForList("service.findCompanyServiceList", comId);
	}

	/**
	 * ��memberType��ѯService,����serviceList
	 * @param comId
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<Service> findServiceList(int memberType) {
		return (List<Service>) this.getSqlMapClientTemplate().queryForList("service.findServiceList", memberType);
	}

}
