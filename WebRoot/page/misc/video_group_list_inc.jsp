<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript">
	function deleteVideoGroup(groupId){
		deleteConfirm("/video/video_group_delete.htm?groupId="+groupId);
	}
</script>
<table cellspacing="1" class="listTable">
	<tr>
		<th width="10%"><s:text name="serialNumber"/></th>
		<th width="65%"><s:text name="videoGroup.groupName"/></th>
		<th width="10%"><s:text name="videoGroup.videoCount"/></th>
		<th width="15%"><s:text name="operate"/></th>
	</tr>
	<s:iterator value="result.groupList" id="group" status="st">
	<tr style="height:40px;">
		<td>
			<s:property value="#st.count"/>
		</td>
		<td>
			<s:if test="#group.videoCount > 0">
				<a href="/video/video_list.htm?groupId=<s:property value="#group.groupId"/>">
					<s:property value="#group.groupName"/>
				</a>
			</s:if>
			<s:else>
				<s:property value="#group.groupName"/>
			</s:else>
		</td>
		<td>
			<s:property value="#group.videoCount"/>
		</td>
		<td>
			<a href="javaScript:showFormDialog('/video/video_group_edit.do?groupId=<s:property value="#group.groupId"/>','<s:text name="videoGroup.editTitle"/>')"><s:text name="button.edit"/></a>&nbsp;
			<s:if test="#group.videoCount == 0">
				<a href="javaScript:deleteVideoGroup(<s:property value='#group.groupId'/>)"> <s:text name="button.delete"/></a>
			</s:if>
		</td>
	</tr>
	</s:iterator>
</table>