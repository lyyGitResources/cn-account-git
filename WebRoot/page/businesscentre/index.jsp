<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>商务中心</title>
	</head>

	<body>
		<div class="bus_patent_box">
			<div style="margin: 20px 0 15px 14px;"><img src="/img/bc/bg_bcC.gif" border="0"/></div>
			<div style="text-align: right;margin-right:5px;">
				<a href="/basic/user_suggest.htm">意见建议</a>&nbsp;&nbsp;
				<a href="/basic/service_mail.htm?queryBy=%E5%85%B3%E4%BA%8E%E5%95%86%E5%8A%A1%E4%B8%AD%E5%BF%83%E7%9A%84%E5%8A%9F%E8%83%BD%E6%8E%A8%E8%8D%90">功能推荐</a>
			</div>
			<table border="0" cellspacing="15" cellpadding="0" style="width:98%">
				<tr>
			     	<td>
			     		<s:if test="loginUser.memberType == 2">
			     			<a href="/businesscentre/legal_issues_consulting_gold_member.htm">
			     				<img src="/img/bc/bt04.gif" border="0">
			     			</a>
			     		</s:if>
			     		<s:else>
			     			<a href="/businesscentre/legal_issues_consulting_free_member.htm">
			     				<img src="/img/bc/bt04.gif" border="0">
			     			</a>
			     		</s:else>
			     	</td>
			    	<td>
			    		<s:if test="loginUser.memberType == 2">
			     			<a href="/businesscentre/patent_consulting_gold_member.htm">
			     				<img src="/img/bc/bt05.gif" border="0">
			     			</a>
			     		</s:if>
			     		<s:else>
			     			<a href="/businesscentre/patent_consulting_free_member.htm">
			     				<img src="/img/bc/bt05.gif" border="0">
			     			</a>
			     		</s:else>
			    	</td>
			    	<td>
			    		<s:if test="loginUser.memberType == 2">
			     			<a href="/businesscentre/financial_consulting_gold_member.htm">
			     				<img src="/img/bc/bt06.gif" border="0">
			     			</a>
			     		</s:if>
			     		<s:else>
			     			<a href="/businesscentre/financial_consulting_free_member.htm">
			     				<img src="/img/bc/bt06.gif" border="0">
			     			</a>
			     		</s:else>
			    	</td>
			   	</tr>
			    <tr>
			    	<%--
			    	<td>
		     			<a href="javascript:gotoCRM();"><img src="/img/bc/bt02.gif" border="0"></a>
			    	</td>
			    	--%>
					<td><a href="http://www.tuyaya.com" target="_blank"><img src="/img/bc/bg_10.gif" border="0" /></a></td>
                    <td><a href="/businesscentre/yingkebao.htm"><img src="/img/bg/bt07.gif" border="0" /></a></td>
                    <td><a href="/businesscentre/keyword.htm"><img src="/img/bg/bt08.png" border="0" /></a></td>
			  	</tr>
		  </table>
		</div>
	</body>
</html>