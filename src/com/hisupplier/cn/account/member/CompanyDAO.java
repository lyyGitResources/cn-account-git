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
	 * ��ѯ��˾��ǰ�����״̬
	 * @param comId
	 * @return
	 */
	public int findCompanyState(int comId) {
		return (Integer) getSqlMapClientTemplate().queryForObject("company.findCompanyState", comId);
	}

	/**
	 * <p>��ѯ��˾������Ҳ�������null
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
	 * <p>����userId��email���ҹ�˾������Ҳ�������null
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
	 * <p>��ѯ��˾������Ҳ�������null
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
	 * ��ѯ��˾������Ҳ�������null�������˾�ǷǶ����ٰ�memberType��ѯMemberType
	 * @param comId
	 * @return
	 */
	public Company findCompanyMemberType(int comId) {
		Company company = this.findCompany(comId);
		if (company != null && !company.isCustom()) {
			// ��Config�ж�ȡ��Ա�ȼ�������
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
	 * ��ѯ��ԱID������Ѵ��ڷ��ع�˾ID�����򷵻�0
	 * @param memberId
	 * @return
	 */
	public int findMemberId(String memberId) {
		return this.findMemberId(-1, memberId);
	}

	/**
	 * ��ѯ��ԱID������Ѵ��ڷ��ع�˾ID�����򷵻�0
	 * @param comId �ų��Ĺ�˾ID
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
	 * ��ѯ��˾���ƣ�����Ѵ��ڷ��ع�˾ID�����򷵻�0
	 * @param comName
	 * @return
	 */
	public int findComName(String comName) {
		return this.findComName(-1, comName);
	}

	/**
	 * ��ѯ��˾���ƣ�����Ѵ��ڷ��ع�˾ID�����򷵻�0
	 * @param comId �ų��Ĺ�˾ID
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
	 * ������˾���ɹ����ع�˾ID�����򷵻�0
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
	 * ������˾Ӫҵִ��
	 * @param companyProfile
	 * @return
	 */
	public int addCompanyProfile(CompanyProfile companyProfile) {
		Integer id = (Integer) this.getSqlMapClientTemplate()
				.insert("company.addCompanyProfile", companyProfile);
		return id == null ? 0 : id;
	}
	/**
	 * ������˾Ŀ¼��ϵ���ɹ��������������������򷵻�0
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
