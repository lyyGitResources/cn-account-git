<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
		<title>忘记密码</title>
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>密码取回</span></div>
			<div class="blank6"></div>
			<div class="forgotPassword">
				<div class="top"></div>
				<div class="center">
					<div class="explain">
						<div class="box1">
							<b>亲爱的<span><s:property value="result.memberId" /></span>:</b><br /><br /><br />
							会员帐号和密码已发送至您的邮箱<s:property value="result.email" />，请查收！您可以点击以下按钮直接跳转到首页或返回上一页面。
						</div>
					</div>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td><a href="<%=sysBase%>"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_01.gif" border="0" style="margin-right:30px;" /></a><a href="<%=Config.getString("account.base")%>/user/forget_passwd.htm?email=<s:property value="result.email" />"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_02.gif" border="0" /></a></td>
						</tr>
					</table>
				</div>
				<div class="buttom"></div>
			</div>
		</div>
		<div class="blank2"></div>
	</body>
</html>