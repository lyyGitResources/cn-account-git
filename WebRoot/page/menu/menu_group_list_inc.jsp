<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/gb2big5-converter" prefix="gb2big5"%>
<gb2big5:Gb2Big5JspWrapper>
<script type="text/javascript">
	$(function (){
		$("[name='menuTrtrue']").each(function(){
			$(this).children().css("background-color", "#DDEFFF");
		});
	});
	function deleteGroup(id){
		deleteConfirm("/menu/menu_group_delete.htm?groupId="+id);
	}
</script>
<table cellspacing="1" class="listTable">
	<tr>
		<th width="5%"><s:text name="serialNumber" /></th>
		<th width="45%"><s:text name="group.groupName" /></th>
		<th width="20%">审核状态</th>
		<th width="10%"><s:text name="group.menuCount" /></th>
		<th width="10%" onclick="ListTable.sort('modifyTime')" class="sort" id="sort_modifyTime"><s:text name="modifyTime" /></th>
		<th width="10%"><s:text name='operate' /></th>
	</tr>
	<s:iterator value="result.groupList" id="group" status="st">
		<tr style="height:40px;" name="menuTr<s:property value="#group.fix" />" id="<s:property value="#group.fix" /><s:property value="#group.groupId" />">
			<td><s:property value="#st.count"/></td>
			<td>
				<s:if test="#group.menuCount > 0">
					<a href="/menu/menu_list.htm?groupId=<s:property value="#group.groupId" />">
						<s:property value="#group.groupName" />
					</a>
				</s:if>	
				<s:else>
					<s:property value="#group.groupName" />
				</s:else>
			</td>
			<td>
				<s:if test="#group.state == 10">
				<a href="javascript:show_audit_remark(<s:property value='#group.groupId' />) ">审核未通过</a>
				<s:property value="#group.adminLog_remark"/>
				</s:if>
				<s:elseif test="#group.state == 14">
				正在审核
				</s:elseif>
				<s:elseif test="#group.state == 15">
				等待审核
				</s:elseif>
				<s:elseif test="#group.state == 20">
				审核通过
				</s:elseif>
			</td>
			<td ><s:property value="#group.menuCount" /></td>
			<td ><s:property value="#group.modifyTime" /></td>
			<td >
				<a href="/menu/menu_add.htm?groupId=<s:property value='#group.groupId'/>"><s:text name='group.addMenu' /></a><br />
				<s:if test="#group.fix == false"><a href="#position;" onclick="javascript:showFormDialog('/menu/menu_group_edit.do?groupId='+<s:property value='#group.groupId' />,'<s:text name="button.editGroup" />')"><s:text name="button.edit" /></a>
					<s:if test="#group.menuCount <= 0"><a href="javascript:deleteGroup('<s:property value="#group.groupId" />')"><s:text name="button.delete" /></a><br /></s:if>
				</s:if>
			</td>
		</tr>
	</s:iterator>
</table>
<div id="adminLogDialog"></div>
<script>
$(function() {
	$("#adminLogDialog").dialog({
		bgiframe: true,
		autoOpen: false,
		height: 200,
		width: 360,
		modal: true,
		close: function(){
			$("#adminLogDialog").empty();
		}
	});
});
	function show_audit_remark(id) {
		$("#adminLogDialog").load("/basic/admin_log.do?tableName=MenuGroup&tableId=" + id,{random: Math.random()});
		$("#adminLogDialog").dialog('option', 'title', '未通过原因');
		$("#adminLogDialog").dialog('open');
	}
</script>
</gb2big5:Gb2Big5JspWrapper>