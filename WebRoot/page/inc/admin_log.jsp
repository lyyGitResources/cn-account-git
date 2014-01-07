<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<table class="listTable" style="width: 330px;">
	<tbody>
		<tr>
			<th style="width:100px;"><s:text name="auditTime"/>：</th>
			<td>
				<s:property value="result.adminLog.createTime" />
			</td>
		</tr>
		<tr>
			<th><s:text name="auditState.rejectRemark"/>：</th>
			<td>
				<s:property value="result.adminLog.remark" />
			</td>
		</tr>
	</tbody>
</table>
