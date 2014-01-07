<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:include value="/page/inc/pagination.jsp"/>
<table cellspacing="1" class="listTable">
	<tr>
		<th><s:text name="userLog.operateContact" /></th>
		<th><s:text name="userLog.operateTable" /></th>
		<th><s:text name="userLog.operateOption" /></th>
		<th><s:text name="userLog.operateContent" /></th>
		<th><s:text name="userLog.userIP" /></th>
		<th><s:text name="createTime" /></th>
	</tr>
	<s:iterator value="result.listResult.list" id="userLog">
	<tr>
		<td ><s:property value="#userLog.user.contact"/></td>
		<td ><s:property value="#userLog.logTypeName"/></td>
		<td ><s:property value="#userLog.operateName"/></td>
		<td ><s:property value="#userLog.content"/></td>
		<td ><s:property value="#userLog.userIP"/></td>
		<td ><s:property value="#userLog.createTime"/></td>
	</tr>						
	</s:iterator>
</table>			
<s:include value="/page/inc/pagination.jsp"/>