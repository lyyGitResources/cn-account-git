<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.entity.MessageChargeLog"%>
<%@ page import="com.hisupplier.commons.entity.MessageAccount"%>
<%@ page import="com.hisupplier.commons.page.Page"%>
<%@ page import="com.hisupplier.commons.util.DateUtil "%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@page import="java.util.Map"%>
<%@page import="com.hisupplier.commons.page.ListResult"%>
<%@page import="java.util.List"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	ValueStack vs = (ValueStack) request.getAttribute("struts.valueStack");
	Map<String,Object> result = (Map<String,Object>)vs.findValue("result");
	ListResult<MessageChargeLog> chargeList=(ListResult<MessageChargeLog>)result.get("listResult");
	Page p = chargeList.getPage();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>短信群发</title>
	</head>
	<body>
				<div class="SMSbox">
				    <div class="tabMenu">
					<ul>
						<li>
							<span onclick="location.href='/message/form.htm'">发送短消息</span>
						</li>
						<li>
							<span onclick="location.href='/message/messageLog.htm'">已发短信</span>
						</li>
						<li>
							<span onclick="location.href='/message/phoneBook.htm'">电话簿</span>
						</li>
						<li>
							<span onclick="location.href='/message/template.htm'">常用短语</span>
						</li>
						<li class="current">
							<span onclick="location.href='/message/chargeLog.htm'">充值记录</span>
						</li>
						<li>
							<span onclick="location.href='/message/charge.htm'">充值</span>
						</li>
					</ul>
				</div>
					<div class="balance">剩余短信（条数）：<strong><s:property value="result.number" /></strong></div>
				    <table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank">
				        <tr>
				          <th>日期</th>
				          <th>付款方式</th>
				          <th>充值金额</th>
				          <th>折合短信</th>
				          <th>赠送短信</th>
				          <th>共计短信</th>
				        </tr>
				        <%
				        int i = 0;
				        List<MessageChargeLog> list=chargeList.getList();
				        for(MessageChargeLog log : list){ %>
				        <tr>
				          <td <%=i%2==0?"class='td1'":"class='td2 tdBg'" %>><%=DateUtil.formatDate(log.getCreateTime()) %></td>
				          <td <%=i%2==0?"":"class='tdBg'" %>><%=log.getChargeType()==1?"现金":"网银" %></td>
				          <td <%=i%2==0?"":"class='tdBg'" %>><%=new java.text.DecimalFormat("#0.00").format(log.getMoneys())  %></td>
				          <td <%=i%2==0?"":"class='tdBg'" %>><%=log.getNumber() %></td>
				          <td <%=i%2==0?"":"class='tdBg'" %>><%=log.getGiftNumber() %></td>
				          <td <%=i%2==0?"":"class='tdBg'" %>><%=log.getNumber()+log.getGiftNumber() %></td>
				        </tr>
				        <%i++;}%>
				    </table>
			        <%
					String url = "/message/chargeLog.htm?pageNo="+Page.PAGE_NO;
					p.setPagUrl(url);
					%>
					<%@include file="/page/message/phoneBookPageBar.jsp" %>
		    	</div>
	</body>
</html>