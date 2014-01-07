package com.hisupplier.cn.account.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.cas.Constants;
import com.hisupplier.cas.Ticket;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BasicAction extends ActionSupport 
		implements ServletResponseAware, ServletRequestAware {

	private static final long serialVersionUID = 2540187530954241496L;
	public static final String MESSAGE = "message";
	public static final String LIMIT = "limit";
	public static final String PAGE_NOT_FOUND = "pageNotFound";
	public static final String AJAX_SUCCESS = "ajaxSuccess";//ajax请求对应的响应结果标识符
	public static final String[] ALLOW_MIME_TYPE = { "image/jpeg", "image/pjpeg", "image/gif", "image/png", "text/plain", "application/pdf", "application/msword", "application/vnd.ms-excel","application/x-msexcel",
			"application/zip" };

	protected HttpServletResponse response;
	protected HttpServletRequest request;
	protected String tip;
	protected String msg;
	protected String currentMenu;

	public void addMessage(String key) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put("com.hisupplier.cn.account.message", key);
	}

	public void addError(String key) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put("com.hisupplier.cn.account.error", key);
	}

	@JSON(serialize = false)
	public LoginUser getLoginUser() {
		return (LoginUser) CASClient.getInstance().getUser(ServletActionContext.getRequest());
	}

	/**
	 * 取得二级域名和风格网站预览链接
	 * @return
	 */
	@JSON(serialize = false)
	public Map<String, Object> getPreviewUrl() {
		Map<String, Object> urls = new HashMap<String, Object>();
		LoginUser loginUser = this.getLoginUser();
		String ticket = WebUtil.getCookieValue(request, Ticket.getKey(Constants.LANG_ZH));
		Pattern pattern = Pattern.compile("\\d{17}");
		Matcher matcher = pattern.matcher(loginUser.getMemberId());
		if (!matcher.matches()) {//排除memberId是17位数字的情况
			StringBuffer showroomUrl = new StringBuffer();
			if (StringUtil.equalsIgnoreCase("true", Config.getString("isBig5"))) {
				showroomUrl.append("http://").append(loginUser.getMemberId()).append(".big5.").append(Config.getString("sys.domain"));
			} else {
				showroomUrl.append("http://").append(loginUser.getMemberId()).append(".cn.").append(Config.getString("sys.domain"));
			}

			urls.put("showroomUrl", showroomUrl.toString());
			if (StringUtil.isNotEmpty(ticket)) {
				showroomUrl.append("/?").append(Constants.TICKET).append("=").append(ticket);
				urls.put("showroomAllUrl", showroomUrl.toString());
			}
		}

		StringBuffer websiteUrl = new StringBuffer();
		if (StringUtil.isNotEmpty(loginUser.getDomain())) {
			if (!loginUser.getDomain().startsWith("http")) {
				websiteUrl.append("http://");
			}
			websiteUrl.append(loginUser.getDomain());
			urls.put("websiteUrl", websiteUrl.toString());
			if (StringUtil.isNotEmpty(ticket)) {
				websiteUrl.append("/?").append(Constants.TICKET).append("=").append(ticket);
				urls.put("websiteAllUrl", websiteUrl.toString());
			}
		}
		return urls;
	}

	@JSON(serialize = false)
	public String getCurrentMenu() {
		return this.currentMenu;
	}

	/**
	 * 页面中  meta name="memoinfo" 中使用的温馨提示
	 * 会员类型不同 ，显示文字也不同
	 * @return
	 */
	public String getMemoInfo() { 
		LoginUser user = getLoginUser();
		if (user.getMemberType() == 2) {
			return getText("memo.company.info.gold");
		} else {
			return getText("memo.company.info.free");
		}
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public abstract Map<String, Object> getResult();

	public String getTip() {
		return tip;
	}

	public String getMsg() {
		return msg;
	}
}
