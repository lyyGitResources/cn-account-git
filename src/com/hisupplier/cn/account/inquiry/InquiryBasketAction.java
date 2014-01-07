package com.hisupplier.cn.account.inquiry;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.hisupplier.commons.basket.Basket;
import com.hisupplier.commons.basket.BasketFactory;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.basket.BasketMaxSizeException;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.WebUtil;
import com.opensymphony.xwork2.ActionSupport;

public class InquiryBasketAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {

	private static final long serialVersionUID = 7352284787340110169L;
	private HttpServletResponse response;
	private HttpServletRequest request;
	private String returnURL;
	private String fromPage;
	private List<BasketItem> basketItemList;
	
	private int basketCount;
	private String basketMsg;

	private boolean success;
	
	public String inquiry_basket() throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		if (StringUtil.isEmpty(fromPage)) {
			fromPage = request.getHeader("referer");
		}
		Basket basket = BasketFactory.getBasket(request,"cn");
		basket.setFromType(Basket.GET_ALL);
		basketItemList = basket.getItems(request, response);
		return SUCCESS;
	}

	public String inquiry_basket_add() throws Exception {
		BasketFactory.getBasket(request,"cn").addItems(request, response);
		// 重定向返回先前的地址
		returnURL = request.getHeader("referer");
		if (StringUtil.isEmpty(returnURL)) {
			returnURL = WebUtil.getBasePath(request);
		}
		return SUCCESS;
	}
	
	public String inquiry_basket_add_open() throws Exception {
		Basket basket = BasketFactory.getBasket(request, "cn");
		PrintWriter out = response.getWriter();
		try {
			basket.addItems(request, response);
			setBasketMsg("succ");
			setBasketCount(basket.getCompanySize());
			out.print("var basket = {msg:'succ',count:" + basket.getCompanySize() + "}");
		} catch (BasketMaxSizeException e) {
			setBasketMsg("您的询盘篮已满！");
			out.print("var basket= {msg:'full', count:100}");
			return null;
		}
		return null;
	}

	public String inquiry_basket_remove() throws Exception {
		BasketFactory.getBasket(request,"cn").removeItems(request, response);
		return SUCCESS;
	}
	
	/**
	 * 列表页 移除询盘篮 操作 
	 * @author wuyaohui
	 * @return jsonp 跨域
	 * @throws Exception
	 */
	public String inquiry_basket_remove_jsonp() throws Exception {
		Basket basket = BasketFactory.getBasket(request, "cn");
		basket.removeItems(request, response);
		setBasketCount(basket.getCompanySize());
		setSuccess(true);
		return "json";
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public List<BasketItem> getBasketItemList() {
		return basketItemList;
	}

	public void setBasketItemList(List<BasketItem> basketItemList) {
		this.basketItemList = basketItemList;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getBasketCount() {
		return basketCount;
	}

	public void setBasketCount(int basketCount) {
		this.basketCount = basketCount;
	}

	public String getBasketMsg() {
		return basketMsg;
	}

	public void setBasketMsg(String basketMsg) {
		this.basketMsg = basketMsg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
