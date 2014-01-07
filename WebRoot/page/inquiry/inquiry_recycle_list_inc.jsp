<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script>
	$(function() {
		Util.check_toggle("ckAll", "inqId");
	});
</script>
<s:include value="/page/inc/pagination.jsp"/>
<table cellspacing="1" class="listTable">
	<tr>
		<th width="5%"><input type="checkbox" name="ckAll"/></th>
		<th width="7%"><s:text name='serialNumber'/></th>
		<th width="29%"><s:text name='inquiry.subject'/></th>
		<th width="17%"><s:text name='inquiry.createTime'/></th>
		<th width="15%"><s:text name='inquiry.fromName'/></th>
		<th width="12%"><s:text name='inquiry.fromProvince'/></th>
		<th width="14%"><s:text name='inquiry.userId'/></th>
	</tr>
	<s:iterator value="result.listResult.list" id="inquiry" status="st">
	<tr style="height:40px;">
		<td><input type="checkbox" value='<s:property value="#inquiry.inqId"/>' name="inqId"/></td>
		<td><s:property value='result.listResult.page.startIndex + #st.index + 1' />
			<s:if test="#inquiry.readName > 0">
				<img src="/img/ico/ico_message_new.gif" align="absmiddle" />
			</s:if>
			<s:else>
				<img src="/img/ico/read1.gif" align="absmiddle" />
			</s:else>
		</td>
		<td title="<s:property value='#inquiry.subject'/>"><s:property value="#inquiry.subjectName"/></td>
		<td><s:property value="#inquiry.createTime"/></td>
		<td><s:property value="#inquiry.fromName"/></td>
		<td><s:property value="#inquiry.fromProvinceName"/></td>
		<td><s:property value="#inquiry.userIdName"/></td>
	</tr>						
	</s:iterator>
</table>
<s:include value="/page/inc/pagination.jsp"/>