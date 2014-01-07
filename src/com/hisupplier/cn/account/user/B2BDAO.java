/* 
 * Created by baozhimin at Dec 7, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.Friend;
import com.hisupplier.commons.entity.cn.CategorySuggest;

/**
 * @author baozhimin
 */
public class B2BDAO extends DAO{

	/**
	 * 添加目录建议
	 * @param product
	 * @return
	 */
	public int addCategorySuggest(CategorySuggest categorySuggest) {
		return (Integer) this.getSqlMapClientTemplate().insert("b2b.addCategorySuggest", categorySuggest);
	}
	
	/**
	 * 友情链接最大排序值，state为
	 * @return
	 */
	public int findFriendMaxListOrder(){
		Integer listOrder = (Integer) this.getSqlMapClientTemplate().queryForObject("b2b.findFriendMaxListOrder");
		return listOrder == null ? 0 : listOrder;
	}
	
	/**
	 * 添加友情链接
	 * @param product
	 * @return
	 */
	public int addFriend(Friend friend) {
		return (Integer) this.getSqlMapClientTemplate().insert("b2b.addFriend", friend);
	}
}
