<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function(){
		var groupIds = ",";
		 $("[name='specialGroupId']").each(function(){
		 	groupIds += $(this).val() + ",";
		 });

		$("[name='liInc']").each(function(){
		 	if(groupIds.indexOf("," + $(this).attr("groupId") + ",") != -1){
		 		$(this).hide();
		 	}
		 });
	});

	function selectSpecialGroup(){
		var obj = $("#specialGroupIdInc [name='specialGroupBox']:checked");
		if(obj.size <= 0){
			alert("请选择一个特殊分组");
		}else{
			obj.each(function(){
				var obj2 = $("#specialGroupId ul li:last-child");
				if(obj2.size() > 0){
					obj2.after(getSpecialGroupModel($(this).attr("groupId"), $(this).attr("groupName")));
				}else{
					$("#specialGroupId ul").append(getSpecialGroupModel($(this).attr("groupId"), $(this).attr("groupName")));
					$("#specialGroupId").show();
				}
			});
		}
		$("#specialGroupDialog").dialog('close');
	}
	
	function getSpecialGroupModel(groupId, groupName){
		var buffer = "<li><input type='hidden' name='specialGroupId' value='" + groupId + "' />";
		buffer += "<input type='checkbox' name='specialGroupBox' />&nbsp;";
		buffer += groupName + "</li>";
		return buffer;
	}
</script>
<div class="formTable" style="width:580px;border:1px">
	<div id="specialGroupIdInc" class="options" <s:if test="result.groupList.size == 0" >style="display:none;"</s:if>>
		<ul>
			<s:iterator value="result.groupList" id="group" >
				<li name="liInc" groupId="<s:property value='#group.groupId'/>">
					<input type="checkbox" groupId="<s:property value='#group.groupId'/>" groupName="<s:property value='#group.groupName'/>" name="specialGroupBox" />&nbsp;
					<s:property value='#group.groupName'/>
				</li>
			</s:iterator>
		</ul>
	</div>
</div>

<input type="button" value="<s:text name='button.confirm' />" onclick="selectSpecialGroup();" class="popButton" style="margin-left:200px;*margin-left:200px !important;*margin-left:200px;"/>

