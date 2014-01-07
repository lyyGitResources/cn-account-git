package com.hisupplier.cn.account.basic;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.hisupplier.cn.account.dao.QueryParams;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * @author linliuwei
 */
public class BasicInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -1201705395098961295L;
	private BasicService basicService;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final HttpServletRequest request = ServletActionContext.getRequest();
		final Locale locale = Locale.CHINESE;
		String uri = request.getRequestURI().toLowerCase();
		// 设置Action使用的Locale
		ActionContext.getContext().setLocale(locale);
		Action action = (Action) invocation.getAction();

		//设置查询参数
		if (action instanceof ModelDriven) {
			ModelDriven<?> modelDriven = (ModelDriven<?>) action;
			Object model = modelDriven.getModel();
			if (model instanceof QueryParams) {
				QueryParams params = (QueryParams) model;
				if (StringUtil.isNotEmpty(params.getQueryText())) {
					params.setQueryText(StringUtil.filterSearchText(params.getQueryText()));
				}
				params.setPageNo(params.getPageNo() <= 0 ? 1 : params.getPageNo());
				params.setPageSize(params.getPageSize() <= 0 ? 
						StringUtil.toInt(WebUtil.getCookieValue(request, "hs_account_pageSize"), 15) : 
							params.getPageSize() > 100 ? 15 : params.getPageSize());
				LoginUser loginUser = (LoginUser) CASClient.getInstance().getUser(request);
				if (loginUser != null) {
					params.setLoginUser(loginUser);
					//存入公司评论数量
					if (params.getCommentCount() == -1) {
						params.setCommentCount(basicService.getCommentCount(loginUser));
					}
				}
			}
		}

		// 设置提示信息
		if (action instanceof BasicAction) {
			BasicAction basicAction = (BasicAction) action;
			String message = (String) ActionContext.getContext().getSession().get("com.hisupplier.cn.account.message");
			if (StringUtil.isNotEmpty(message)) {
				String[] tmpKey = StringUtil.toArray(message, ",");
				if (tmpKey.length > 1) {
					String[] tmpKey2 = new String[tmpKey.length - 1];
					System.arraycopy(tmpKey, 1, tmpKey2, 0, tmpKey2.length);
					basicAction.addActionMessage(basicAction.getText(tmpKey[0], tmpKey2));
				} else {
					basicAction.addActionMessage(basicAction.getText(message));
				}
				ActionContext.getContext().getSession().remove("com.hisupplier.cn.account.message");
			}
			String error = (String) ActionContext.getContext().getSession().get("com.hisupplier.cn.account.error");
			if (StringUtil.isNotEmpty(error)) {
				String[] tmpKey = StringUtil.toArray(error, ",");
				if (tmpKey.length > 1) {
					String[] tmpKey2 = new String[tmpKey.length - 1];
					System.arraycopy(tmpKey, 1, tmpKey2, 0, tmpKey2.length);
					basicAction.addActionError(basicAction.getText(tmpKey[0], tmpKey2));
				} else {
					basicAction.addActionError(basicAction.getText(error));
				}
				ActionContext.getContext().getSession().remove("com.hisupplier.cn.account.error");
			}
			//设置当前菜单
			if (uri.endsWith(".htm")) {
				if (uri.startsWith("/member/")) {
					basicAction.currentMenu = "member";
					// uri 中只有小写字母
				} else if (uri.startsWith("/group/") || uri.startsWith("/specialgroup/")) {
					basicAction.currentMenu = "group";
				} else if (uri.startsWith("/product/")) {
					basicAction.currentMenu = "product";
				} else if (uri.startsWith("/newproduct/")) {
					basicAction.currentMenu = "product";
				} else if (uri.startsWith("/trade/")) {
					basicAction.currentMenu = "trade";
				} else if (uri.startsWith("/menu/")) {
					basicAction.currentMenu = "menu";
				} else if (uri.startsWith("/ad/")) {
					basicAction.currentMenu = "ad";
				} else if (uri.startsWith("/inquiry/")) {
					basicAction.currentMenu = "inquiry";
				} else if (uri.startsWith("/alert/")) {
					basicAction.currentMenu = "alert";
				} else if (uri.startsWith("/website/")) {
					basicAction.currentMenu = "website";
				} else {
					basicAction.currentMenu = "home";
				}
			}
		}

		return invocation.invoke();
	}

	public void setBasicService(BasicService basicService) {
		this.basicService = basicService;
	}

}
