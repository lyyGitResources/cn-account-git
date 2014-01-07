/* 
 * Created by wuyaohui at 2011-8-2
 * Copyright HiSupplier.com 
 */
package com.hisupplier.cn.account.product;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.cn.account.entity.LikeRecord;

/**
 * @author wuyaohui
 *
 */
public class LikeRecordDAO extends DAO {

	/**
	 * 添加赞
	 * @param like
	 * 		<pre>
	 * 			proId,	// 产品ID
	 * 			comId	// 公司ID
	 * 		</pre>
	 * @return id
	 */
	public void addLikeRecord(LikeRecord like) {
		this.getSqlMapClientTemplate().insert("likeRecord.addLikeRecord", like);
	}

	/**
	 * 根据用户及产品Id查询是否赞及赞总数
	 * @param like
	 * 		<pre>
	 * 			comId,	
	 * 			proId
	 * 		</pre>
	 * @return like
	 * 		<pre>
	 * 			count,	// 总赞数量
	 * 			like		// 是否赞了
	 * 		</pre>
	 */
	public LikeRecord findLikeRecordByComIdAndProId(LikeRecord likeRecord) {
		return (LikeRecord) this.getSqlMapClientTemplate().queryForObject("likeRecord.findLikeRecordByComIdAndProId", likeRecord);
	}
}
