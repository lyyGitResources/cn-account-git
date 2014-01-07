<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function() {
		Util.check_toggle("ckAll", "inqId");
		
	});
</script>
<s:include value="/page/inc/pagination.jsp"/>
<s:set name="admin" value="loginUser.admin" />
<table cellspacing="1" class="listTable">
	<tr>
		<s:if test="#admin">
			<th width="5%"><input type="checkbox" name="ckAll"/></th>
		</s:if>
		<th width="7%"><s:text name='serialNumber'/></th>
		<th width="26%"><s:text name='inquiry.subject'/></th>
		<th width="17%" class="sort" id="sort_inqId" onclick="ListTable.sort('inqId');"><s:text name='inquiry.createTime'/></th>
		<th width="13%" class="sort" id="sort_fromName" onclick="ListTable.sort('fromName');"><s:text name='inquiry.fromName'/></th>
		<th width="12%" class="sort" id="sort_fromProvince" onclick="ListTable.sort('fromProvince');"><s:text name='inquiry.fromProvince'/></th>
		<th width="10%" class="sort" id="sort_userId" onclick="ListTable.sort('userId');"><s:text name='inquiry.userId'/></th>
		<th width="10%">回复</th>
	</tr>
	<s:iterator value="result.listResult.list" id="inquiry" status="st">
		<tr style="height:40px">
			<s:if test="#admin">
				<td><input type="checkbox" value='<s:property value="#inquiry.inqId"/>' name="inqId"/></td>
			</s:if>	
			<td><s:property value='result.listResult.page.startIndex + #st.index + 1' />
				<s:if test="#inquiry.read">
					<img src="/img/ico/read1.gif" align="absmiddle" />	
				</s:if>
				<s:else>
					<img src="/img/ico/ico_message_new.gif" align="absmiddle" />
				</s:else>
			</td>
			<td title="<s:property value='#inquiry.subject'/>">
				<a href="/inquiry/inquiry_view.htm?inqId=<s:property value="#inquiry.inqId"/>">
					<s:property value="#inquiry.subjectName"/>
				</a>
			</td>
			<td><s:property value="#inquiry.createTime"/></td>
			<td><s:property value="#inquiry.fromName"/></td>
			<td><s:property value="#inquiry.fromProvinceName"/></td>
			<td><s:property value="#inquiry.userIdName"/></td>
			<td>
				<a href="/inquiry/inquiry_reply_add.htm?inqId=<s:property value="#inquiry.inqId"/>">
					<img src="../img/ico/ico_message_reply.gif" align="absmiddle"/>
				</a>
				<s:if test="#inquiry.replyCount > 0">
					<a href="/inquiry/inquiry_view.htm?inqId=<s:property value="#inquiry.inqId"/>">
						<s:text name='inquiry.replied'/>
					</a>
				</s:if>
			</td>
		</tr>						
	</s:iterator>
</table>
<s:include value="/page/inc/pagination.jsp"/>