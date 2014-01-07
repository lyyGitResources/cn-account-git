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
	 * 添加产品
	 * @param 
	 * @return int
	 */
	public int addProduct(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("product.addProduct", product);
	}

	/**
	 * 添加产品扩展信息
	 * @param 
	 * @return int
	 */
	public int addProductExtra(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("product.addProductExtra", product);
	}

	/**
	 * 更新产品
	 * @param 
	 * @return int
	 */
	public int updateProduct(Product product) {
		return (Integer) this.getSqlMapClientTemplate().update("product.updateProduct", product);
	}

	/**
	 * 查询所有产品的最小排序值
	 * @param comId
	 * @return
	 */
	public int findMinListOrder(int comId) {
		Integer minListOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findMinListOrder", comId);
		return minListOrder == null ? 100000 : minListOrder;
	}

	/**
	 * 查询组内产品的最小排序值
	 * @param groupUtil
	 * @param comId
	 * @param rootId 多个用逗号分隔
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
	 * 查询展示产品的最大排序值
	 * @param comId
	 * @return
	 */
	public int findMaxFeatureOrder(int comId) {
		Integer maxFeatureOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("product.findMaxFeatureOrder", comId);
		return maxFeatureOrder == null ? 0 : maxFeatureOrder;
	}

	/**
	 * 按参数组合查询
	 * @param params
	 * <pre>
	 * 1) queryText
	 * 2) userId
	 * 3) optimize 
	 * 4) state
	 * 5) groupId 求普通分组的产品列表
	 * 6) groupIds 组内产品排序时使用 null按listOrder升序 >0按groupOrder升序
	 * 7) featureOrder 默认-1，查询展台产品时使用 >0按featureOrder升序 =0modifyTime降序
	 * 8) proId 产品ID
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
	 * 按公司ID，分组ID查询所有产品ID
	 * @param comId
	 * @param groupIds 多个用逗号分隔;所有产品排序时为null
	 * @param state 默认-1
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
	 * 查询关键词列表
	 * @param comId
	 * @param proId 除当前的产品
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
	 * 按comId、proId查询产品
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
	 * 按comId查询产品的相似目录(以前选过的目录)
	 * @param comId
	 * @return 最多10条非0的目录ID
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCatIdList(int comId, int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("count", count);
		return (ArrayList<Integer>) this.getSqlMapClientTemplate().queryForList("product.findCatIdList", map);
	}

	/**
	 * 按目录ID，查询标签列表
	 * @param catId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tag> findTagList(int catId) {
		return (ArrayList<Tag>) this.getSqlMapClientTemplate().queryForList("product.findTagList", catId);
	}
	
	/**
	 * 	更新LikeRecordCount
	 * @author wuyaohui
	 * @param likeRecordCount 更新的数量
	 * @param proId 产品Id
	 */
	public void updateProductLikeRecordCount(int likeRecordCount, int proId) {
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("likeRecordCount", likeRecordCount);
		updateMap.addWhere("proId", proId);
		this.update(updateMap);
	}
}
