/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.commons.entity.SpecialProduct;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;
import com.ibatis.sqlmap.client.SqlMapExecutor;


/**
 * @author linliuwei
 */
public class SpecialGroupDAO extends DAO {

	/**
	 * ��state in (15,20)��ѯ�����������
	 * @param comId
	 * @param groupId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *  Group
	 * </pre>
	 */
	public Group findGroup(int comId, int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("groupId", groupId);
		return (Group) this.getSqlMapClientTemplate().queryForObject("specialGroup.findGroup", map);
	}

	/**
	 * ��state in (15,20)��ѯ��������б�
	 * @param comId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   List<Group>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<Group> findGroupList(int comId) {
		List<Group> list = (ArrayList<Group>) this.getSqlMapClientTemplate().queryForList("specialGroup.findGroupList", comId);
		for (Group group : list) {
			group.setCreateTime(DateUtil.formatDateTime(group.getCreateTime()));
			group.setModifyTime(DateUtil.formatDateTime(group.getModifyTime()));
		}
		return list;
	}

	/**
	 * ����˾ID����ƷID��ѯSpecialGroup
	 * @param comId
	 * @param proId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   �������
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public List<Group> findGroupList(int comId, int[] proId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("proId", proId);
		return (ArrayList<Group>) this.getSqlMapClientTemplate().queryForList("specialGroup.findGroupListById", map);
	}

	/**
	 * ��ѯ�������ֵ	 
	 * @param comId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int findMaxListOrder(int comId) {
		Integer listOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("specialGroup.findMaxListOrder", comId);
		return listOrder == null ? 0 : listOrder;
	}

	/**
	 * ��ѯδ�����Ʒ�б�
	 * @param params
	 * <pre>
	 *  comId
	 *  queryBy
	 *  queryText
	 *  pageNo
	 *  pageSize
	 * </pre>
	 * @return
	 * <pre>
	 *  ListResult<Product>
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Product> findGroupProductList(QueryParams params) {
		params.setDefaultSort(new String[] { "modifyTime", "viewCount" }, "modifyTime", "desc");

		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("specialGroup.findGroupProductListCount", params);
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());

		List<Product> list = (ArrayList<Product>) this.getSqlMapClientTemplate().queryForList("specialGroup.findGroupProductList", params);
		for (Product product : list) {
			product.setModifyTime(DateUtil.formatDateTime(product.getModifyTime()));
		}
		return new ListResult<Product>(list, page);
	}

	public int findNoGroupProductCount(QueryParams params) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("specialGroup.findGroupProductListCount", params);
	}

	/**
	 * ɾ����������Ʒ������ϵ
	 * @param comId
	 * @param proIds ��ƷID���Զ��ŷָ�����Ϊnull
	 * @param groupIds �������ID���Զ��ŷָ�����Ϊnull
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int deleteSpecialProduct(int comId, String proIds, String groupIds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("proIds", proIds);
		map.put("groupIds", groupIds);
		return this.getSqlMapClientTemplate().delete("specialGroup.deleteSpecialProduct", map);
	}

	public int deleteSpecialProduct(int comId, int[] proId, int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("proId", proId);
		map.put("groupId", groupId);
		return this.getSqlMapClientTemplate().delete("specialGroup.deleteSpecialProductById", map);

	}

	/**
	 * ����������
	 * @param group
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addGroup(Group group) {
		return (Integer) this.getSqlMapClientTemplate().insert("specialGroup.addGroup", group);
	}

	/**
	 * �����������Ʒ������ϵ
	 * @param comId
	 * @param proId
	 * @param groupId
	 * <pre>
	 * </pre>
	 * @return
	 * <pre>
	 *   int
	 * </pre>
	 */
	public int addSpecialProduct(final int comId, final int[] proId, final int[] groupId) {
		return (Integer) this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("comId", comId);
				if (proId.length == 1) {
					map.put("proId", proId[0]);
					for (int id : groupId) {
						map.put("groupId", id);
						executor.insert("specialGroup.addSpecialProduct", map);
					}
				} else if (groupId.length == 1) {
					map.put("groupId", groupId[0]);
					for (int id : proId) {
						map.put("proId", id);
						executor.insert("specialGroup.addSpecialProduct", map);
					}
				}
				return executor.executeBatch();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<SpecialProduct> findSpecialProductList(int comId, int groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("groupId", groupId);
		return (ArrayList<SpecialProduct>) this.getSqlMapClientTemplate().queryForList("specialGroup.findSpecialProductList", map);
	}
	
	/**
	 * ͨ�����Ʋ�ѯ
	 * @param group
	 * @return
	 */
	public int findSpecialGroupCountByName(Group group){
		 return(Integer) this.getSqlMapClientTemplate().queryForObject("specialGroup.findSpecialGroupCountByGroupName",group); 
	}
}
