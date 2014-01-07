<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<form>
	<table cellspacing="1" class="listTable" style="width:480px">
		<tr>
			<th width="5%"><s:text name="serialNumber"/></th>
			<th width="10%"><s:text name="specialGroup.groupName"/></th>
		</tr>
		<s:iterator value="result.groupList" id="group" status="st">
			<tr>
				<td><s:property value="#st.count"/></td>
				<td>
					<a href="<s:property value="#group.operate"/>">
						<s:property value="#group.groupName"/>(<s:property value="#group.productCount"/>)
					</a>
				</td>
			</tr>
		</s:iterator>
	</table>
</form>