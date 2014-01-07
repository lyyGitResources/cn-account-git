<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.CN"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/page/inquiry/functions.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.setTitle'/></title>
	</head>
	<body>
		<div id="chartCode"></div>
		<form id="inquirySetForm" name="inquirySetForm" method="post" action="/inquiry/inquiry_set_submit.htm">
			<s:token />	
			<div class="pageTips">
				<span>提示：</span>
				<ul>
					<li>管理员接收：所有询盘都由管理员接收</li>
					<li>子帐号接收：子帐号和管理员接收跟自己相关的询盘</li>
					<li>同时接收：子帐号接收到的询盘同时也发送给管理员</li>
				</ul>
			</div>
			<table class="formTable" style="margin-top:20px;">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="inquiry.inquiryReceive" />：</th>
					<td>
						<input type="radio" name="inquiryReceive" value="<%=CN.INQUIRY_ACCEPTER_ADMIN %>" <s:if test="result.company.inquiryReceive == 1">checked</s:if>/>&nbsp;<s:text name="inquiry.receiveAdmin" />
						<input type="radio" name="inquiryReceive" value="<%=CN.INQUIRY_ACCEPTER_USER %>" <s:if test="result.company.inquiryReceive == 2">checked</s:if>/>&nbsp;<s:text name="inquiry.receiveUser" />
						<input type="radio" name="inquiryReceive" value="<%=CN.INQUIRY_ACCEPTER_BOTH %>" <s:if test="result.company.inquiryReceive == 3">checked</s:if>/>&nbsp;<s:text name="inquiry.receiveBoth" />
					</td>
				</tr>
			</table>
				
			<div class="pageTips" style="margin-top:50px;">
				<span>提示：</span>
				<ul>
					<li>推荐询盘是指同行业免费会员推荐给您的询盘，直接发送至您的邮箱！</li>
				</ul>
			</div>
			<table class="formTable" style="margin-top:20px;">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="inquiry.isReceiveRecommend" />：</th>
					<td>
						<input type="radio" name="receiveRecommend" value="<%=CN.YES %>" <s:if test="result.company.receiveRecommend == true">checked</s:if>/>&nbsp;是&nbsp;&nbsp;
						<input type="radio" name="receiveRecommend" value="<%=CN.NO %>" <s:if test="result.company.receiveRecommend == false">checked</s:if>/>&nbsp;否
					</td>
				</tr>
			</table>

			<div class="buttonCenter">
				<input type="submit"  value="<s:text name='button.submit' />"/>
			</div>
		</form>
	</body>
</html>
