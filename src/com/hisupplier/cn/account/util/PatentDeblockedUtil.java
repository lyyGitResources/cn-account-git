package com.hisupplier.cn.account.util;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;

/**
 * @author liuyuyang
 * 
 */
public class PatentDeblockedUtil {

	public static List<String> getPatentDeblockedList(int comId) {
		JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
		List<String> list = new ArrayList<String>();
		try {
			jd.createPreparedStatement("select keywords from PatentDeblocked where state=20 and comId= ?");
			jd.setInt(1, comId);
			jd.preQuery();
			while (jd.resultNext()) {
				list.add(jd.getString("keywords"));
			}
		} finally {
			jd.closePreparedStatement();
			JdbcUtilFactory.getInstance().closeJdbcUtil(jd);
		}
		return list;
	}
	
}
