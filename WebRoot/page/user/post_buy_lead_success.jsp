<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.StringUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String sysBase = Config.getString("sys.base");
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		sysBase = "http://big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>简易发布求购信息</title>
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav">&nbsp;</div>
			<div class="blank6"></div>
			<div class="forgotPassword">
				<div class="top"></div>
				<div class="center">
					<div class="explain">
						<div class="box2" style="text-align: center;">
							<div class="div">恭喜您！您已经成功发布求购信息。</div>
							<s:if test="newUserEmail != null && newUserEmail != ''">
								同时您已成为中国海商网的会员，会员帐号和密码已经发送至您的邮箱：<s:property value="newUserEmail"/><br />
							</s:if>
							您可以点击以下按钮直接跳转到首页或登录后台管理求购信息。
						</div>
					</div>
					<table border="0" cellspacing="0" cellpadding="0" class="table">
						<tr>
							<td><a href="<%=sysBase%>"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_01.gif" border="0" style="margin-right:30px;" /></a><a href="/trade/trade_list.htm"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_03.gif" border="0" /></a></td>
						</tr>
					</table>
				</div>
				<div class="buttom"></div>
			</div>
		</div>
	</body>
</html>