<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="com.hisupplier.commons.struts2.*"%>
<%@ page import="com.hisupplier.commons.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="serverError" /></title>
	</head>
	<body style="line-height:18px;font:13px Arial;">
		<h1></h1>
		<s:text name="serverError" />
		<br />
		<%  String log = ExceptionInterceptor.handleLogging(request, exception);
			if(Boolean.getBoolean(Global.DEBUG)){ %>
			<p>
			<%= log %>
			</p>
			<p>
			exception <br />
			<% 
				if(exception != null){
				exception.printStackTrace(new java.io.PrintWriter(out)); 
				}
			%>
			</p>
		<% 	} %>
	</body>
</html>
