package com.hisupplier.cn.account.basic;

import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.hisupplier.commons.util.StringUtil;
import com.opensymphony.xwork2.ModelDriven;

public class HomeAction extends BasicAction implements ModelDriven<QueryParams> {
	private static final long serialVersionUID = -7536541457724363003L;
	private QueryParams params = new QueryParams();
	private BasicService basicService;
	private Map<String, Object> result;

	public String home() throws Exception {
		result = basicService.getHome(params, request);
		// TODO √‹¬Î«ø∂» 2012-08-10
		if (StringUtil.equals("password", (String) result.get("msg"))) {
			return "password";
		}
		return SUCCESS;
	}

	public String user_log_list() throws Exception {
		result = basicService.getUserLogList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String admin_log() throws Exception {
		result = basicService.getAdminLog(params);
		return SUCCESS;
	}

	public String service_list() throws Exception {
		result = basicService.getServiceList(params);
		return SUCCESS;
	}

	public String inquiry_unread_count() throws Exception {
		result = basicService.getInquiryUnreadCount(params);
		return SUCCESS;
	}

	public String service_mail() throws Exception {
		result = basicService.getServiceMail(params);
		return SUCCESS;
	}

	public String user_suggest() throws Exception {
		result = basicService.getUserSuggestList(params);
		return params.isAjax() ? AJAX_SUCCESS : SUCCESS;
	}

	public String download_tq() throws Exception {
		return SUCCESS;
	}
	
	@JSON(serialize = false)
	public InputStream getTargetFile() throws Exception {
		return ServletActionContext.getServletContext().getResourceAsStream("/img/HiSetup(Standard).exe");
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getTip() {
		return tip;
	}

	@JSON(serialize = false)
	public QueryParams getModel() {
		return params;
	}

	public void setBasicService(BasicService basicService) {
		this.basicService = basicService;
	}
}
