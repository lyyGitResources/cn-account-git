/* 
 * Created by linliuwei at 2009-11-9 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.util.GroupUtil;
import com.hisupplier.commons.entity.cn.Tag;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author linliuwei
 */
public class ProductDAO extends DAO {

	/**
	 * ��Ӳ�Ʒ
	 * @param 
	 * @return int
	 */
	public int addProduct(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("product.addProduct", product);
	}

	/**
	 * ��Ӳ�Ʒ��չ��Ϣ
	 * @param 
	 * @return int
	 */
	public int addProductExtra(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("product.addProductExtra", product);
	}

	/**
	 * ���²�Ʒ
	 * @param 
	 * @return int
	 */
	public int updateProduct(Product product) {
		return (Integer) this.getSqlMapClientTemplate().update("product.updateProduct", product);
	}

	/**
	 * ��ѯ���в�Ʒ����С����ֵ
	 * @param comId
	 * @return
	 */
	public int findMinListOrder(int comId) {
		Integer minListOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findMinListOrder", comId);
		return minListOrder == null ? 100000 : minListOrder;
	}

	/**
	 * ��ѯ���ڲ�Ʒ����С����ֵ
	 * @param groupUtil
	 * @param comId
	 * @param rootId ����ö��ŷָ�
	 * @return
	 */
	public int findMinGroupOrder(GroupUtil groupUtil, int comId, int rootId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("groupIds", groupUtil.getChildIds(rootId, true));

		Integer minGroupOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findMinGroupOrder", map);
		return minGroupOrder == null ? 100000 : minGroupOrder;
	}

	/**
	 * ��ѯչʾ��Ʒ���������ֵ
	 * @param comId
	 * @return
	 */
	public int findMaxFeatureOrder(int comId) {
		Integer maxFeatureOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findMaxFeatureOrder", comId);
		return maxFeatureOrder == null ? 0 : maxFeatureOrder;
	}

	/**
	 * ��������ϲ�ѯ
	 * @param params
	 * <pre>
	 * 1) queryText
	 * 2) userId
	 * 3) optimize 
	 * 4) state
	 * 5) groupId ����ͨ����Ĳ�Ʒ�б�
	 * 6) groupIds ���ڲ�Ʒ����ʱʹ�� null��listOrder���� >0��groupOrder����
	 * 7) featureOrder Ĭ��-1����ѯչ̨��Ʒʱʹ�� >0��featureOrder���� =0modifyTime����
	 * 8) proId ��ƷID
	 * </pre>
	 * @return listResult
	 * 	 Product{
	 * 		proId,
	 * 		comId,
	 * 		userId,
	 * 		proName,
	 * 		model,
	 * 		keywords,
	 * 		imgId,
	 * 		imgPath,
	 * 		viewCount,
	 * 		state,
	 * 		modifyTime
	 * }
	 */
	@SuppressWarnings("unchecked")
	public ListResult<Product> findProductList(QueryParams params) {
		params.setDefaultSort(new String[] { "modifyTime", "listOrder", "groupOrder", "featureOrder", "viewCount", "likeRecordCount" }, "modifyTime", "desc");
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findProductListCount", params);

		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Product> list = this.getSqlMapClientTemplate().queryForList("product.findProductList", params);
		for (Product product : list) {
			product.setCreateTime(DateUtil.formatDate(product.getCreateTime()));
			product.setModifyTime(DateUtil.formatDate(product.getModifyTime()));
		}
		//		for (int i = 0; i < list.size(); i++) {
		//			Product product = list.get(i);
		//			product.setModifyTime(DateUtil.formatDate(product.getModifyTime()));
		//		}
		return new ListResult<Product>(list, page);
	}

	/**
	 * ����˾ID������ID��ѯ���в�ƷID
	 * @param comId
	 * @param groupIds ����ö��ŷָ�;���в�Ʒ����ʱΪnull
	 * @param state Ĭ��-1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findProductIdList(int comId, String groupIds, int state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("groupId", groupIds);
		map.put("state", state);
		map.put("sortBy", groupIds == null ? "listOrder" : "groupOrder");

		return (ArrayList<Integer>) this.getSqlMapClientTemplate().queryForList("product.findProductIdList", map);
	}

	/**
	 * ��ѯ�ؼ����б�
	 * @param comId
	 * @param proId ����ǰ�Ĳ�Ʒ
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findProductKeywordList(int comId, int proId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("comId", comId);
		map.put("proId", proId);

		return (ArrayList<String>) this.getSqlMapClientTemplate().queryForList("product.findProductKeywordList", map);
	}

	/**
	 * ��comId��proId��ѯ��Ʒ
	 * @param comId
	 * @param proId
	 * @return
	 */
	public Product findProduct(int comId, int proId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("proId", proId);

		return (Product) this.getSqlMapClientTemplate().queryForObject("product.findProduct", map);
	}

	/**
	 * ��comId��ѯ��Ʒ������Ŀ¼(��ǰѡ����Ŀ¼)
	 * @param comId
	 * @return ���10����0��Ŀ¼ID
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCatIdList(int comId, int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("count", count);
		return (ArrayList<Integer>) this.getSqlMapClientTemplate().queryForList("product.findCatIdList", map);
	}

	/**
	 * ��Ŀ¼ID����ѯ��ǩ�б�
	 * @param catId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> findTagList(int catId) {
		return (ArrayList<Tag>) this.getSqlMapClientTemplate().queryForList("product.findTagList", catId);
	}
	
	/**
	 * 	����LikeRecordCount
	 * @author wuyaohui
	 * @param likeRecordCount ���µ�����
	 * @param proId ��ƷId
	 */
	public void updateProductLikeRecordCount(int likeRecordCount, int proId) {
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("likeRecordCount", likeRecordCount);
		updateMap.addWhere("proId", proId);
		this.update(updateMap);
	}
}
