/* 
 * Created by wuyaohui at 2011-8-2
 * Copyright HiSupplier.com 
 */
package com.hisupplier.cn.account.product;

import javax.servlet.http.HttpServletRequest;

import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.basic.CASClient;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.LikeRecord;

/**
 * @author wuyaohui
 *
 */
public class LikeRecordService {
	private LikeRecordDAO likeRecordDAO;
	private ProductDAO productDAO;

	/**
	 * 加赞
	 * @param request
	 * @param likeRecord
	 * 		<pre>
	 * 			proId
	 * 		</pre>
	 * @return likeRecord.id  > 0 成功 =0 未登录
	 */
	public LikeRecord addLikeRecord(HttpServletRequest request, LikeRecord likeRecord) {
		likeRecord.setComId(CASClient.getInstance().getUser(request).getComId());
		likeRecordDAO.addLikeRecord(likeRecord);
		likeRecord = getLikeRecord(likeRecord, true);
		return likeRecord;
	}

	public LikeRecord getLikeRecord(HttpServletRequest request, LikeRecord likeRecord) {
		Ticket ticket = CASClient.getInstance().getUser(request);
		if (ticket != null) likeRecord.setComId(CASClient.getInstance().getUser(request).getComId());
		return getLikeRecord(likeRecord, false);
	}

	/**
	 * 
	 * @param likeRecord
	 * 		<pre>
	 * 			comId,
	 * 			proId
	 * 		</pre>
	 * @param isUpdateLikeRecordCount 是否更新数量
	 * @return
	 */
	public LikeRecord getLikeRecord(LikeRecord likeRecord, boolean isUpdateLikeRecordCount) {
		int tempProId = likeRecord.getProId();
		int tempComId = likeRecord.getComId();
		likeRecord = this.likeRecordDAO.findLikeRecordByComIdAndProId(likeRecord);
		if (likeRecord.getProId() == 0) likeRecord.setProId(tempProId);
		if (likeRecord.getComId() == 0) likeRecord.setComId(tempComId);
		if (isUpdateLikeRecordCount) productDAO.updateProductLikeRecordCount(likeRecord.getCount(), likeRecord.getProId());
		return likeRecord;
	}

	/**
	 * 取消赞
	 * @param request
	 * <pre>
	 * 		user
	 * </pre>
	 * @param likeRecord
	 * <pre>
	 * 		likeRecord.id
	 * </pre>
	 */
	public LikeRecord deleteLikeRecord(HttpServletRequest request, LikeRecord likeRecord) {
		int comId = CASClient.getInstance().getUser(request).getComId();
		int tempProId = likeRecord.getProId();
		UpdateMap updateMap = new UpdateMap("LikeRecord");
		updateMap.addWhere("id", likeRecord.getId());
		updateMap.addWhere("comId", comId);
		likeRecordDAO.delete(updateMap);
		likeRecord.setComId(comId);
		likeRecord.setProId(tempProId);
		return getLikeRecord(likeRecord, true);
	}

	public void setLikeRecordDAO(LikeRecordDAO likeRecordDAO) {
		this.likeRecordDAO = likeRecordDAO;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}
