/* 
 * Created by baozhimin at Nov 16, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.page.Page;
import com.hisupplier.commons.page.PageFactory;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author baozhimin
 */
public class NewProductDAO extends DAO {
	
	/**
	 * 添加加密产品
	 * @param 
	 * @return int
	 */
	public int addNewProduct(Product product) {
		return (Integer) this.getSqlMapClientTemplate().insert("newproduct.addNewProduct", product);
	}
	
	/**
	 * 按参数组合查询
	 * @param params
	 * <pre>
	 * 1) queryBy queryText
	 * 2) state
	 * 3) proId 产品ID
	 * </pre>
	 * @return listResult
	 * 	 Product{
	 * 		proId,
	 * 		comId,
	 * 		proName,
	 * 		model,
	 * 		imgId,
	 * 		imgPath,
	 * 		viewCount,
	 * 		state,
	 * 		modifyTime
	 * }
	 */ 
	@SuppressWarnings("unchecked")
	public ListResult<Product> findNewProductList(QueryParams params) {
		params.setDefaultSort(new String[] { "modifyTime", "viewCount"}, "modifyTime", "desc");
		int resultTotal = (Integer) this.getSqlMapClientTemplate().queryForObject("newproduct.findNewProductListCount", params);
		
		Page page = PageFactory.createPage(resultTotal, params.getPageNo(), params.getPageSize());
		params.setStartRow(page.getStartRow());
		List<Product> list = this.getSqlMapClientTemplate().queryForList("newproduct.findNewProductList", params);
		for (int i = 0; i < list.size(); i++) {
			Product product = list.get(i);
			product.setModifyTime(DateUtil.formatDate(product.getModifyTime()));
		}
		return new ListResult<Product>(list, page);
	}

	/**
	 * 按comId、newProId查询加密产品
	 * @param comId
	 * @param proId
	 * @return
	 */
	public Product findNewProduct(int comId, int proId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comId", comId);
		map.put("proId", proId);
		
		return (Product) this.getSqlMapClientTemplate().queryForObject("newproduct.findNewProduct", map);
	}
	
	/**
	 * 查询所有加密产品的最大排序值
	 * @param comId
	 * @return
	 */
	public int findMaxListOrder(int comId) {
		Integer maxListOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("newproduct.findMaxListOrder", comId);
		return maxListOrder == null ? 0 : maxListOrder;
	}
}
