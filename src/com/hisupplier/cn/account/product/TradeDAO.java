/* 
 * Created by baozhimin at Nov 16, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author baozhimin
 */
public class TradeDAO extends DAO {

	/**
	 * ��������ϲ�ѯ
	 * @param params
	 * <pre>
	 * 1) queryText
	 * 2) userId
	 * 3) state
	 * 4) proType
	 * 5) groupId
	 * 6) proId[]
	 * </pre>
	 * @return listResult
	 * 	 Product{
	 * 		proId,
	 * 		comId,
	 * 		userId,
	 * 		proName,
	 * 		imgId,
	 * 		imgPath,
	 * 		viewCount,
	 * 		state,
	 * 		modifyTime
	 * }
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Product> findTradeList(QueryParams params) {
		params.setDefaultSort(new String[] { "modifyTime", "viewCount" }, "modifyTime", "desc");
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("trade.findTradeListCount", params);

		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Product> list = this.getSqlMapClientTemplate().queryForList("trade.findTradeList", params);
		for (int i = 0; i < list.size(); i++) {
			Product product = list.get(i);
			product.setModifyTime(DateUtil.formatDate(product.getModifyTime()));
			product.setCreateTime(DateUtil.formatDate(product.getCreateTime()));
		}
		return new ListResult<Product>(list, page);
	}

	/**
	 * ��ѯ��Ӧ��������
	 * @param params
	 * @return
	 */
	public int findTradeSellCount(int comId) {
		return StringUtil
				.toInt((Integer) getSqlMapClientTemplate()
						.queryForObject("trade.findTradeSellCount", comId), 0);
	}

	/**
	 * ��comId��proId��ѯ����
	 * @param comId
	 * @param proId 
	 * @return
	 */
	public Product findTrade(int comId, int proId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("comId", comId);
		map.put("proId", proId);
		
		return (Product) this.getSqlMapClientTemplate().queryForObject("trade.findTrade", map);
	}

	/**
	 * ��comId��ѯ��������copyProId
	 * @param comId
	 * @return Map<copyProId, proId>
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> findTradeCopyProId(int comId) {
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		Map<Integer, Product> map = (HashMap<Integer, Product>)this.getSqlMapClientTemplate().queryForMap("trade.findTradeCopyProId", comId, "copyProId");
		for(Entry<Integer, Product> e : map.entrySet()){
			result.put(e.getKey(), e.getValue().getProId());
		}
		return result;
	}
	
	/**
	 * �������
	 * @param product
	 * @return
	 */
	public int addTrade(Product product) {
		if (product.getValidDay() == 0) {
			product.setValidDay(365);
		}
		return (Integer) this.getSqlMapClientTemplate().insert("trade.addTrade", product);
	}

	/**
	 * ���������չ��Ϣ
	 * @param product
	 * @return
	 */
	public int addTradeExtra(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("trade.addTradeExtra", product);
	}

	/**
	 * ��������
	 * @param product
	 * @return
	 */
	public int updateTrade(Product product) {
		return this.getSqlMapClientTemplate().update("trade.updateTrade", product);
	}

	/**
	 * ��ѯ�������ʹ�õ�Ŀ¼ID
	 * @param comId
	 * @param count ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCatIdList(int comId, int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("count", count);
		return this.getSqlMapClientTemplate().queryForList("trade.findCatIdList", map);
	}

	/**
	 * ���ع�����������
	 * @param comId
	 * @return
	 */
	public int findTradeDatedCount(int comId) {
		return StringUtil
				.toInt((Integer) getSqlMapClientTemplate()
						.queryForObject("trade.findTradeDatedCount", comId), 0);
	}
}
