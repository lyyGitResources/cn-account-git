<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link type="text/css" rel="stylesheet" href="/css/result.css" />
	</head>
	<body>
		<div class="success"><h2><s:property value="result.addMessage" /></h2>
		<p>此产品将进入产品审核，审核通过后才能在平台上展示。</p>
		<ul><li><a href="/product/product_add.htm"><img src="/img/jxfb.png" alt="继续发布" /></a></li>
		<li><a href="/product/product_edit.htm?proId=<s:property value="proId[0]" />">修改产品</a></li>
		<li><a href="/product/product_add.htm?proId=<s:property value="proId[0]" />">添加同类产品</a></li>
		<li><a href="/product/product_list.htm">返回产品列表</a></li></ul></div>
	</body>
</html>
