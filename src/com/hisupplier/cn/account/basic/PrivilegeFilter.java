/* 
 * Created by baozhimin at Dec 9, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.spring2.ServiceLocator;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.Validator;

/**
 * @author baozhimin
 */
public class PrivilegeFilter implements Filter {

	private List<String> ignorePrefixs;
	private List<String> goldsiteUriList = new ArrayList<String>();
	private List<String> privilegeUriList = new ArrayList<String>();
	// ���δͨ���û������޸ĵ�ַ
	private List<String> rejectCanEditUriList = new ArrayList<String>();
	
	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
		String ignorePrefixs_ = config.getInitParameter("ignorePrefixs");
		if (StringUtil.isNotEmpty(ignorePrefixs_)) {
			String[] tmps = StringUtil.toArray(ignorePrefixs_, ",");
			for (String prefix : tmps) {
				if(ignorePrefixs == null){
					ignorePrefixs =  new ArrayList<String>();
				}
				ignorePrefixs.add(prefix);
			}
		}
		
		// ��Ҫ���ƻ�Ա�����ĵ�ַǰ׺
		goldsiteUriList.add("/member/user");
		goldsiteUriList.add("/product/product_change_user");
		goldsiteUriList.add("/trade/trade_change_user");
		goldsiteUriList.add("/inquiry/inquiry_set");
		goldsiteUriList.add("/video/");
		
		// ��Ҫ����Ա�����ĵ�ַǰ׺
		// ���ɷ����ַ
		privilegeUriList.add("/member/user");
		privilegeUriList.add("/product/product_change_user");
		privilegeUriList.add("/trade/trade_change_user");
		privilegeUriList.add("/inquiry/inquiry_delete");
		privilegeUriList.add("/inquiry/inquiry_recycle");
		privilegeUriList.add("/inquiry/inquiry_set");
		
		// �ɷ����ַ
		privilegeUriList.add("/member/company_edit_submit");  
		privilegeUriList.add("/specialGroup/");
		privilegeUriList.add("/group/");
		privilegeUriList.add("/menu/");
		privilegeUriList.add("/ad/");
		privilegeUriList.add("/alert/");
		privilegeUriList.add("/website/");
		
		rejectCanEditUriList.add("/member/company_edit");
		rejectCanEditUriList.add("/member/sonUser_edit_submit");
		rejectCanEditUriList.add("/member/user_edit");
		rejectCanEditUriList.add("/member/contact_");
		rejectCanEditUriList.add("/inquiry/");
		rejectCanEditUriList.add("/alert/");
		rejectCanEditUriList.add("/image/");
		rejectCanEditUriList.add("/basic/");
		rejectCanEditUriList.add("/ad/");
	}
	
	public static boolean isLimit(HttpServletRequest request){
		return false;
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filter) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String uri = request.getRequestURI();
		
		LoginUser loginUser = (LoginUser) CASClient.getInstance().getUser(request);
		if(loginUser == null){
			filter.doFilter(request, response);
			return;
		}
		if (ignorePrefixs != null) {
			for (String prefix : ignorePrefixs) {
				if (uri.startsWith(prefix)) {
					filter.doFilter(request, response);
					return;
				}
			}
		}
		
		
		CompanyDAO companyDAO = (CompanyDAO) ServiceLocator.getService("companyDAO");
		int companyState = companyDAO.findCompanyState(loginUser.getComId());
		
		// ��˾���δͨ������Ա�˺Ų��Ϸ�����˾��Ϣ������
		if(companyState == CN.STATE_REJECT
				|| !"ok".equals(Validator.isMemberId(loginUser.getMemberId())) 
				|| StringUtil.isBlank(loginUser.getKeywords())){
			// ֻ��֤��.htm��β����ҳ�ĵ�ַ�������Ĳ���֤
			if(uri.endsWith(".htm") || "/".equals(uri)){
				boolean canEdit = false;
			
				for (String prefix : rejectCanEditUriList) {
					if(uri.startsWith(prefix)){
						canEdit = true;
					}
				}
				
				if(!canEdit){
					request.getRequestDispatcher("/member/company_edit.htm").forward(request, response);
					return;
				}
			}
		}
		 //��ϵ����˲�ͨ��
		if(loginUser.getUserState() == CN.STATE_REJECT){
			// ֻ��֤��.htm��β����ҳ�ĵ�ַ�������Ĳ���֤
			if(uri.endsWith(".htm") || "/".equals(uri)){
				boolean canEdit = false;
			
				for (String prefix : rejectCanEditUriList) {
					if(uri.startsWith(prefix)){
						canEdit = true;
					}
				}
				
				if(!canEdit){
					if(loginUser.isAdmin()){
						request.getRequestDispatcher("/member/contact_edit.htm").forward(request, response);
					}else {
						request.getRequestDispatcher("/member/user_edit.htm?userId=" + loginUser.getUserId()).forward(request, response);
					} 
					return;
				}
			}
		}
		
		// ���ʺŵ�¼
		if(loginUser.getMemberType() == CN.GOLD_SITE && !loginUser.isAdmin()){
			for (String prefix : privilegeUriList) {
				if (uri.startsWith(prefix) && !loginUser.isHave(uri)) {
					request.getRequestDispatcher("/limit.htm").forward(request, response);
					return;
				}
			}
		}
		
		if(loginUser.getMemberType() == CN.FREE_SITE){
			if(loginUser.isAdmin()){
				for (String prefix : goldsiteUriList) {
					if (uri.startsWith(prefix)) {
						request.getRequestDispatcher("/limit.htm").forward(request, response);
						return;
					}
				}
			}else{
				request.getRequestDispatcher("/limit.htm").forward(request, response);
				return;
			}
		}
		
		filter.doFilter(request, response);
	}

}
