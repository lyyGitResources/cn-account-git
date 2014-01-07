<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>
<s:iterator value="result.listResult.list" id="userSuggest">
	<table class="formTable">
		<tr name="userSuggest">
			<th><s:text name="userSuggest.subject" />：</th>
			<td><s:property value="#userSuggest.title" /></td>
		</tr>
		<tr name="userSuggest">
			<th><s:text name="userSuggest.content" />：</th>
			<td><s:property value="#userSuggest.content" /></td>
		</tr>
		<tr name="userSuggest">
			<th><s:text name="createTime" />：</th>
			<td><s:property value="#userSuggest.createTime" /></td>
		</tr>
		<s:if test="userSuggest.reply != null">
			<tr name="userSuggest">
				<th><s:text name="userSuggest.reply" />：</th>
				<td><s:property value="#userSuggest.reply" /></td>
			</tr>			
		</s:if>
	</table>
</s:iterator>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>