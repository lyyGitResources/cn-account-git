<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(function () {
		//绑定表单验证
		$("#groupForm").validate({
			submitHandler: function(form){ 
				$(form).ajaxSubmit({
				    beforeSubmit: function (formData, jqForm, options){
				   	 	$("#formSubmitLoading").show();
						return true;
				    },  
					success: function (response, status){
						$("#formSubmitLoading").hide(); 
						$("#dialogMessage1").show().html(response.msg);
						if(response.tip == "addSuccess" || response.tip == "editSuccess"){
							$("#dialogMessage1").addClass("success");
							group = response.model;
							$('#formDialog').dialog('close');
							if(ListTable.url.indexOf("product_add") != -1 || ListTable.url.indexOf("product_edit") != -1 || ListTable.url.indexOf("trade_add") != -1 || ListTable.url.indexOf("trade_edit") != -1){
								$("#groupId").val(group.groupId);
								$("#groupName").val(group.groupName);
								$("#groupAddDialog").dialog('close');
							}else{
								window.location.href = "/group/group_list.htm";
							}
						}
					},
				    type: 'post',        		
				    dataType: 'json'        
				}); 
			},
			rules: {
				//groupName: {required:true, maxlength:60},
				brief: {required:true, maxlength:250}
			},
			messages: {
				//groupName: "<s:text name='group.groupName.maxlength'/>",
				brief: "<s:text name='group.brief.maxlength'/>"
			}
		});
	});

	function groupNameBlur(){
		var groupName=$("#groupName").val();
		if(groupName==""){
			$("#dialogMessage1").css("display","inline").html("<s:text name='group.groupName.maxlength'/>");
			}else{
				$("#dialogMessage1").css("display","none");
			}
		}

	
</script>
<form id="groupForm" name="groupForm" action="<s:property value="result.formAction"/>" method="post">
	<input type="hidden" id="parentId" name="parentId" value='<s:property value="result.group.parentId"/>'/>
	<input type="hidden" id="groupId" name="groupId" value='<s:property value="result.group.groupId"/>'/>
<!-- 	<div id="formSubmitLoading" style="display:none;"><img src="/img/loading1.gif" /> &nbsp;Loading...</div> -->
	<table style="width:100%">
		<s:if test="result.group.parentId > 0 && result.addGroup == 'addGroup'">
			<tr>
				<th style="width:80px;"><s:text name="group.parentName"/>:</th>
				<td><s:property value="result.group.parentName"/></td>
			</tr>
		</s:if>
		<s:elseif test="result.group.groupId > 0 && result.editGroup == 'editGroup'">
			<tr>
				<th style="width:80px;"><s:text name="group.oldGroupName"/>:</th>
				<td><s:property value="result.group.groupName"/></td>
			</tr>
		</s:elseif>
		<tr>
			<s:if test="result.group.groupId > 0 && result.editGroup == 'editGroup'">
				<th style="width:80px;"><s:text name="group.changeGroupName"/>:</th>
			</s:if>
			<s:else>
				<th style="width:80px;"><s:text name="group.groupName"/>:</th>
			</s:else>
			<td><input type="text" id="groupName" onblur="groupNameBlur()"  name="groupName" value='<s:property value="result.group.groupName"/>' style="width:190px;"/>
<!--			<span id="dialogMessage1" style="font-size:12px;color:red;padding-left:19px;"></span>-->
				<label id="dialogMessage1" class="error" for="groupName_input" generated="true" style="display: none;"></label>
			</td>
		</tr>
		<tr>&nbsp;</tr>
		<tr>
			<th style="width:80px;"><s:text name="group.brief"/>:</th>
			<td><textarea id="brief" name="brief" style="width: 190px;height: 70px;  margin-right: 6px;"><s:property value="result.group.brief"/></textarea>&nbsp;</td>
		</tr>
		<tr>
			<th style="width: 80px;">&nbsp;</th>
			<td><span style="font-size:12px;"><s:text name="group.briefMessage"/></span></td>
		</tr>
		<tr><td colspan="2" style="text-align:center"><input type="submit" value="<s:text name='button.submit'/>" class="popButton" style=""></td></tr>
	</table>
	
</form>