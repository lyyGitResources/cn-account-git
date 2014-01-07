<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<table style="margin-top: 0;" cellspacing="1" class="listTable clearFloat">
	<tr>
		<th nowrap="nowrap" width="5%">编号</th>
		<th nowrap="nowrap" width="35%">违规词</th>
		<th nowrap="nowrap" width="20%">有效凭证</th>
		<th nowrap="nowrap" width="30%">状态</th>
		<th nowrap="nowrap" width="10%">选项</th>
	</tr>
	<s:iterator value="result.listResult.list" id="patent" status="st">
		<tr style="height: 40px">
			<td nowrap="nowrap"><s:property value="result.listResult.page.startIndex + #st.index +1" /></td>
			<td nowrap="nowrap"><s:property value="#patent.keywords" /></td>
			<td nowrap="nowrap"><s:property value="#patent.license" escape="false"/></td>
			<td nowrap="nowrap"><s:property value="#patent.stateName" escape="false" /></td>
			<td nowrap="nowrap">
				<a href="javascript:deletePatent('<s:property value="#patent.id" />','<s:property value="#patent.state" />');" ><s:text name='button.delete' /></a>&nbsp;&nbsp;
				<s:if test="#patent.state != 15">
					<a href="javascript:updatePatent('<s:property value="#patent.id" />','<s:property value="#patent.state" />');" ><s:text name='button.edit' /></a>
				</s:if>
			</td>
		</tr>
	</s:iterator>
</table>
<%@ include file="/page/inc/pagination.jsp"%>