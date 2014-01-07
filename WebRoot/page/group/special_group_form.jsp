<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		//绑定表单验证
		$("#groupForm").validate({
			submitHandler: function(form){ 
				if(!groupNameBlur()){
					return;
				}
				$(form).ajaxSubmit({
				    beforeSubmit: function (formData, jqForm, options){
				   	 	$("#formSubmitLoading").show();
						return true;
				    },  
					success: function (response, status){
						$("#formSubmitLoading").hide(); 
						$("#dialogMessage").show().html(response.msg);
						if(response.tip == "addSuccess" || response.tip == "editSuccess"){
							$('#formDialog').dialog('close');
							if(ListTable.url.indexOf("product_add") != -1 || ListTable.url.indexOf("product_edit") != -1){
								var obj = $("#specialGroupId ul li:last-child");
								if(obj.size() > 0){
									obj.after(getSpecialGroupModel(response.model.groupId, response.model.groupName));
								}else{
									$("#specialGroupId ul").append(getSpecialGroupModel(response.model.groupId, response.model.groupName));
									$("#specialGroupId").show();
								}
								$("#groupAddDialog").dialog('close');
							}else{
								//ListTable.reload({'msg':response.msg,"random":Math.random()},true);
								document.location.href = "/specialGroup/group_list.htm";
							}
						}else if(response.tip == "repeat"){
							$("#dialogMessage1").css("display","inline").html("组名不能重复");
						}
					},
				    type: 'post',        		
				    dataType: 'json'   
				}); 
			},
			rules: {
				//groupName: {required:true, maxlength:60}
			},
			messages: {
				//groupName: "<s:text name='specialGroup.groupName.required'/>"
			}
		});
	});
	
	function getSpecialGroupModel(groupId, groupName){
		var buffer = "<li><input type='hidden' name='specialGroupId' value='" + groupId + "' />";
		buffer += "<input type='checkbox' name='specialGroupBox' />&nbsp;";
		buffer += groupName + "</li>";
		return buffer;
	}
	
	function groupNameBlur(){
		var groupName=$("#groupName").val();
		if(groupName==""){
			$("#dialogMessage1").css("display","inline").html("<s:text name='group.groupName.maxlength'/>");
			return false;
			}else{
				$("#dialogMessage1").css("display","none");
			return true;
			}
		return false;
	}
</script>
<form id="groupForm" name="groupForm" action='<s:property value="result.formAction"/>' method="post">
	<input type="hidden" id="groupId" name="groupId" value='<s:property value="result.group.groupId"/>'/>
	<div id="formSubmitLoading" style="display:none;">
		<img src="/img/loading1.gif" /> &nbsp;Loading...
	</div>
	<div style="margin:10px auto;">
		<table style="width:100%;">
			<s:if test="result.group.groupId > 0">
				<tr>
					<th width="20%" height="30px;" style="text-align: right; padding-right: 10px;"><s:text name="specialGroup.oldGroupName"/>:</th>
					<td>${result.group.groupName }</td>
				</tr>
			</s:if>
			<tr>
				<th width="20%" height="30px" style="text-align: right; padding-right: 10px;"><s:text name="specialGroup.groupName"/>:</th>
				<td><input type="text" id="groupName" name="groupName" onblur="groupNameBlur()" value='${result.group.groupName}' style="width:220px;"/><label id="dialogMessage1" class="error" for="groupName" generated="true" style="display: none;"></label></td>
			</tr>
			<tr><td colspan="2" style="text-align: center;"><input type="submit" value="<s:text name='button.submit'/>" class="popButton"/></td></tr>
		</table>
	</div>
</form>