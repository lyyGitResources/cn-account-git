package com.hisupplier.cn.account.member;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.CompanyProfile;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.cn.MemberType;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public class CompanyDAO extends DAO {
	
	/**
	 * 查询公司当前的审核状态
	 * @param comId
	 * @return
	 */
	public int findCompanyState(int comId) {
		return (Integer) getSqlMapClientTemplate().queryForObject("company.findCompanyState", comId);
	}

	/**
	 * <p>查询公司，如果找不到返回null
	 * @param comId
	 * @return
	 */
	public Company findCompany(int comId) {
		Company company = (Company) getSqlMapClientTemplate()
				.queryForObject("company.findCompany", comId);
		if(company != null) {
			company.setModifyTime(DateUtil.formatDateTime(company.getModifyTime()));
		} else {
			throw new PageNotFoundException();
		}
		return company;
	}

	/**
	 * <p>根据userId和email查找公司，如果找不到返回null
	 * @param userId, email
	 * @return
	 */
	public Company findCompanyByUserId(int userId,String email){
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("email",email);
		map.put("userId",userId);
		return (Company) this.getSqlMapClientTemplate().queryForObject("company.findCompanyByUserId",map);
	}
	
	/**
	 * <p>查询公司，如果找不到返回null
	 * @param comId
	 * @return
	 */
	public CompanyProfile findCompanyProfile(int comId) {
		CompanyProfile companyProfile = (CompanyProfile) getSqlMapClientTemplate()
				.queryForObject("company.findCompanyProfile", comId);
		if(companyProfile != null){
			companyProfile.setModifyTime(DateUtil.formatDateTime(companyProfile.getModifyTime()));
		}
		return companyProfile;
	}
	
	/**
	 * 查询公司，如果找不到返回null。如果公司是非定制再按memberType查询MemberType
	 * @param comId
	 * @return
	 */
	public Company findCompanyMemberType(int comId) {
		Company company = this.findCompany(comId);
		if (company != null && !company.isCustom()) {
			// 从Config中读取会员等级的数据
			MemberType memberType = CN.getMemberType(company.getMemberType());
			company.setVideoMax(memberType.getVideoMax());
			company.setProductMax(memberType.getProductMax());
			company.setOptimizeProMax(memberType.getOptimizeProMax());
			company.setNewProMax(memberType.getNewProMax());
			company.setFeatureProMax(memberType.getFeatureProMax());
			company.setTradeMax(memberType.getTradeMax());
			company.setMenuMax(memberType.getMenuMax());
			company.setUserMax(memberType.getUserMax());
			company.setMenuGroupMax(memberType.getMenuGroupMax());
			company.setImgMax(memberType.getImgMax());
		}
		return company;
	}
	
	
	/**
	 * 查询会员ID，如果已存在返回公司ID，否则返回0
	 * @param memberId
	 * @return
	 */
	public int findMemberId(String memberId) {
		return this.findMemberId(-1, memberId);
	}

	/**
	 * 查询会员ID，如果已存在返回公司ID，否则返回0
	 * @param comId 排除的公司ID
	 * @param memberId
	 * @return
	 */
	public int findMemberId(int comId, String memberId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("memberId", memberId);

		Integer id = (Integer) this.getSqlMapClientTemplate().queryForObject("company.findMemberId", map);
		return id == null ? 0 : id;
	}

	/**
	 * 查询公司名称，如果已存在返回公司ID，否则返回0
	 * @param comName
	 * @return
	 */
	public int findComName(String comName) {
		return this.findComName(-1, comName);
	}

	/**
	 * 查询公司名称，如果已存在返回公司ID，否则返回0
	 * @param comId 排除的公司ID
	 * @param comName
	 * @return
	 */
	public int findComName(int comId, String comName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("comName", comName);

		Integer id = (Integer) this.getSqlMapClientTemplate().queryForObject("company.findComName", map);
		return id == null ? 0 : id;
	}

	/**
	 * 新增公司，成功返回公司ID，否则返回0
	 * @param company
	 * @return
	 */
	public int addCompany(Company company) {
		Integer id = (Integer) this.getSqlMapClientTemplate().insert("company.addCompany", company);
		if (id != null) {
			company.setComId(id);
			this.getSqlMapClientTemplate().insert("company.addCompanyExtra", company);
		}
		return id == null ? 0 : id;
	}
	/**
	 * 新增公司营业执照
	 * @param companyProfile
	 * @return
	 */
	public int addCompanyProfile(CompanyProfile companyProfile) {
		Integer id = (Integer) this.getSqlMapClientTemplate()
				.insert("company.addCompanyProfile", companyProfile);
		return id == null ? 0 : id;
	}
	/**
	 * 新增公司目录关系，成功返回新增的数量，否则返回0
	 * @param comId
	 * @param catId
	 * @return
	 */
	public int addCompanyCategory(final int comId, final String[] catIds) {
		final Set<String> set = new HashSet<String>();
		set.addAll(StringUtil.toArrayList(StringUtil.toString(catIds, ","), ","));
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String catId = it.next();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("comId", comId);
					map.put("catId", catId);
					executor.insert("company.addCompanyCategory", map);
				}
				return executor.executeBatch();
			}
		});
	}
}
