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
		<title>询盘</title>  
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<link type="text/css" rel="stylesheet" href="<%=Config.getString("sys.base") %>/css/css.css" />
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav">&nbsp;</div>
			<div class="blank6"></div>
			<div class="forgotPassword" sizcache="10" sizset="24">
				<div class="top"></div>
				<div class="center">
					<div class="explain" style="background:none;">
						<div class="box2">
							<div class="div">恭喜您！您已经成功发送询盘。</div>
							<div style="text-align: center;">
							<s:if test="inquiry.fromEmail != '' && inquiry.fromEmail != null">
								同时您已成为中国海商网的会员，会员帐号和密码已经发送至您的邮箱：<s:property value="inquiry.fromEmail"/><br />
							</s:if>
<%-- 							您可以点击以下按钮直接跳转到首页或返回上一页面。 --%>
页面将在<span id="time" style="color:red;">5</span>秒后自动跳转至<a style="color: blue; text-decoration: underline;" href="<%=Config.getString("sys.base") %>">首页</a>。
							</div>
						</div>
					</div>
<%--	
					<table border="0" cellspacing="0" cellpadding="0" class="table">
						<tr>
							<td><a href="<%=sysBase%>"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_01.gif" border="0" style="margin-right:30px;" /></a><a href="javascript:window.history.back();"><img src="<%=Config.getString("sys.base")%>/img/forgotButton_02.gif" border="0" /></a></td>
						</tr>
					</table>
 --%>
				</div>
				<div class="buttom"></div>
			</div>
		</div>
		<div class="blank2"></div>
		<script>
			var span_time = $("#time");
			function change_time() {
				var time = Number(span_time.html()) - 1;
				if (time == 0) {
					location.href = "<%=Config.getString("sys.base")%>";
				} else {
					span_time.html(Number(span_time.html() - 1));
				}
			}
			window.setInterval("change_time()", 999);
		</script>
	</body>
</html>