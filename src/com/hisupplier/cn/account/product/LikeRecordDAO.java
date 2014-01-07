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
	 * �����
	 * @param like
	 * 		<pre>
	 * 			proId,	// ��ƷID
	 * 			comId	// ��˾ID
	 * 		</pre>
	 * @return id
	 */
	public void addLikeRecord(LikeRecord like) {
		this.getSqlMapClientTemplate().insert("likeRecord.addLikeRecord", like);
	}

	/**
	 * �����û�����ƷId��ѯ�Ƿ��޼�������
	 * @param like
	 * 		<pre>
	 * 			comId,	
	 * 			proId
	 * 		</pre>
	 * @return like
	 * 		<pre>
	 * 			count,	// ��������
	 * 			like		// �Ƿ�����
	 * 		</pre>
	 */
	public LikeRecord findLikeRecordByComIdAndProId(LikeRecord likeRecord) {
		return (LikeRecord) this.getSqlMapClientTemplate().queryForObject("likeRecord.findLikeRecordByComIdAndProId", likeRecord);
	}
}
