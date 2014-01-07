<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<style>
<!--
	.tips2{ font-size:14px; color:#CC0000;background:url(<%=Config.getString("sys.base") %>/img/ico/ico_22.gif) no-repeat 0 10px;padding-left:35px; line-height:50px; margin:0px 0 10px 20px;}
-->
</style>
<s:if test="hasActionErrors() == true || hasFieldErrors() == true">
	<div class="tips2">
		<s:actionerror />
		<s:fielderror />
	</div>
</s:if>
<s:if test="hasActionMessages() == true">
	<div class="operateTips operateSuccess">
		<s:actionmessage escape="false"/>
	</div>
</s:if>
