/* 
 * Created by linliuwei at 2009-12-2 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.user;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;
import com.hisupplier.commons.util.WebUtil;
import com.hisupplier.commons.util.StringUtil;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author linliuwei
 */
public class ViewStatServlet extends HttpServlet {

	private static final long serialVersionUID = 8488393938332692586L;
	private static final ConcurrentLinkedQueue<String> sqlList = new ConcurrentLinkedQueue<String>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = WebUtil.getParam(request, "type");
		int id = WebUtil.getParamInt0(request, "id");
		int gid = WebUtil.getParamInt0(request, "gid");
		if (StringUtil.isEmpty(type) || id <= 0) {
			return;
		}

		if (type.equals("product")) {
			sqlList.add("update Product set viewCount=viewCount+1 where proId=" + id);
		} else if (type.equals("newproduct")) {
			sqlList.add("update NewProduct set viewCount=viewCount+1 where proId=" + id);

		} else if (type.equals("trade")) {
			sqlList.add("update Trade set viewCount=viewCount+1 where proId=" + id);

		} else if (type.equals("menu")) {
			sqlList.add("update Menu set viewCount=viewCount+1 where menuId=" + id);
			sqlList.add("update MenuGroup set viewCount=viewCount+1 where groupId=" + gid);

		} else if (type.equals("menugroup")) {
			sqlList.add("update MenuGroup set viewCount=viewCount+1 where groupId=" + id);

		} else if (type.equals("video")) {
			sqlList.add("update Video set viewCount=viewCount+1 where videoId=" + id);
		}

		if (sqlList.size() >= 25) {
			JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
			try {
				Iterator<String> it = sqlList.iterator();
				while (it.hasNext()) {
					String sql = it.next();
					jd.addBatch(sql);
					it.remove();
				}
				jd.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtilFactory.getInstance().closeJdbcUtil(jd);
			}
		}

	}
}
