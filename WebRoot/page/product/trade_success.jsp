<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link type="text/css" rel="stylesheet" href="/css/result.css" />
	</head>
	<body>
		<div class="success"><h2><s:property value="result.addMessage" /></h2>
		<p>此商情将进入商情审核，审核通过后才能在平台上展示。</p>
		<ul><li><a href="/trade/trade_add.htm"><img src="/img/jxfb.png" alt="继续发布" /></a></li>
		<li><a href="/trade/trade_edit.htm?proId=<s:property value="proId[0]" />">修改商情</a></li>
		<li><a href="/trade/trade_add.htm?proId=<s:property value="proId[0]" />">添加同类商情</a></li>
		<li><a href="/trade/trade_list.htm">返回商情列表</a></li></ul></div>
	</body>
</html>
