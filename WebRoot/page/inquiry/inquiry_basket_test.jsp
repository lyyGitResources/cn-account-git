<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.hisupplier.cas.CASClient"%>
<%@ page import="com.hisupplier.commons.*"%>
<%@ page import="com.hisupplier.commons.util.*"%>
<%@ page import="com.hisupplier.commons.page.*"%>
<%@ page import="com.hisupplier.commons.basket.*"%>
<%@ page import="com.hisupplier.cas.CASClient"%>
<%@ page import="org.apache.commons.lang.WordUtils"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
  <head>
	<script type="text/javascript">
	function addBasket(type) {
		form = document.inquiryForm;
		form.action = "http://accountcn.jiaming.com/user/inquiry_basket_add.htm";
		form.get_item_method.value = "get_all_item";
		form.submit();
	}
	</script>
  </head>
  
  <body>
    		<%-- 产品列表的询盘表单 --%>
			<form action="/inquiry" name="inquiryForm" method="post">
				<input type="hidden" name="tradeAlertCategoryId" value="" />
				<input type="hidden" name="tradeAlertCategoryName" value="" />
				<input type="hidden" name="inquiryFrom" value="b2b" />
				<%-- 询盘用 --%>
				<input type="hidden" name="fromSite" value="<%=WebUtil.getBasePath(request)%>"/>
				<input type="hidden" name="<%=Basket.GET_METHOD%>" />
				<%--
				<input type="checkbox" id="1" name="<%=Basket.PRODUCT%>" value="9430-1048" checked/>
				<input type="checkbox" id="2" name="<%=Basket.PRODUCT%>" value="9431-1048" checked/>
				<input type="checkbox" id="3" name="<%=Basket.PRODUCT%>" value="9433-1048" checked/>
				<input type="checkbox" id="4" name="<%=Basket.PRODUCT%>" value="9444-1049" checked/>
				<input type="checkbox" id="5" name="<%=Basket.PRODUCT%>" value="9447-1049" checked/>
				<input type="checkbox" id="6" name="<%=Basket.PRODUCT%>" value="9449-1049" checked/>
				<input type="checkbox" id="7" name="<%=Basket.PRODUCT%>" value="9455-1049" checked/>
				<input type="checkbox" id="8" name="<%=Basket.PRODUCT%>" value="9457-1049" checked/>
				<input type="checkbox" id="9" name="<%=Basket.PRODUCT%>" value="9458-1049" checked/>
				<input type="checkbox" id="10" name="<%=Basket.PRODUCT%>" value="9459-1049" checked/>
				<input type="checkbox" id="11" name="<%=Basket.PRODUCT%>" value="9460-1049" checked/>
				<input type="checkbox" id="12" name="<%=Basket.PRODUCT%>" value="9451-1049" checked/>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9453-1049" checked/>
				--%>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9443-442" checked/>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9448-442" checked/>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9450-442" checked/>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9452-442" checked/>
				<input type="checkbox" id="13" name="<%=Basket.PRODUCT%>" value="9454-442" checked/>
			</form>
			<a href="javascript:addBasket('<%=Basket.PRODUCT%>')">
				<img src="/images/ico/button-bask.gif" width="92" height="20" border="0" align="absmiddle"/>
			</a>
  </body>
</html>
