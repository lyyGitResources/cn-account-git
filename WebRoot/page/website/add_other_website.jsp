<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加网站</title>
  </head>
  
  <body>
    <form action="/website/add_other.htm" method="post">
    	<table class="formTable">
    		<tr><th>网站名称：</th><td><input type="text" name="siteName" maxlength=120 value="<s:property value="result.website.siteName"/>"/></td></tr>
    		<tr><th>网站地址：</th><td><input type="text" name="siteLink" maxlength=120 value="<s:property value="result.website.siteLink"/>"/></td></tr>
    	</table>
    	<div class="buttonCenter">
    	<input type="reset" value="重置" />
    	<input type="submit" value="提交" />
    	</div>
    </form>
  </body>
</html>
