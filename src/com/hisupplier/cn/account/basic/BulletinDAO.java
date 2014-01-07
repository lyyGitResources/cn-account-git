/* 
 * Created by ±«ÖÇÃô at Jan 13, 2011 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.basic;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.cn.account.dao.DAO;
import com.hisupplier.commons.entity.cn.Bulletin;
import com.hisupplier.commons.util.DateUtil;

/**
 * @author ±«ÖÇÃô
 *
 */
public class BulletinDAO  extends DAO {
	
	@SuppressWarnings("unchecked")
	public List<Bulletin> findBulletinList(QueryParams params) {
		params.setEndTime(new DateUtil().getDate());
		params.setPageSize(5);
		List<Bulletin> bulletinList = (ArrayList<Bulletin>) this.getSqlMapClientTemplate().queryForList("bulletin.findBulletinList", params);
		
		for (int i = 0; i < bulletinList.size(); i++) {
			Bulletin bulletin = bulletinList.get(i);
			bulletin.setCreateTime(DateUtil.formatDate(bulletin.getCreateTime()));
		}
		return bulletinList;
	}
}
