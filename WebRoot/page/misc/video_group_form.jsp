<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#videoGroupForm").validate({
			submitHandler: function(form){ 
				$(form).ajaxSubmit($.extend(ajaxSubmitOptions,{
					success: function (response, status){
						$("#dialogLoading").hide(); 
						if(response.tip == "addSuccess" || response.tip == "editSuccess"){
							$('#menuGroupFormDialog').dialog('close');
							ListTable.reload({'msg':response.msg});
						}else{
							$("#dialogMessage").show().html(response.msg);
						}
					},
					type: 'post',        		
				    dataType: 'json'  
				})); 
			},
			rules: {
				groupName: {required:true, maxlength:60}
			},
			messages: {
				groupName: "<s:text name='videoGroup.groupName.maxlength'/>"
			}
		});
	});
</script>
<form id="videoGroupForm" name="videoGroupForm" action="<s:property value="result.formAction"/>" method="post">
	<table>
		<tr>
			<th style="width:80px;"><s:text name="videoGroup.groupName"/>:</th>
			<td><input type="text" id="groupName" name="groupName" value='<s:property value="result.groupName"/>' style="width:220px;"/></td>
		</tr>
		<input type="hidden" id="groupId" name="groupId" value='<s:property value="result.groupId"/>'/>
	</table>
	<input type="submit" value="<s:text name='button.submit'/>" class="popButton" style="margin-left:12px;*margin-left:12px !important;*margin-left:12px;">
</form>