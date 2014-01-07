<%@ page language="java" contentType="text/html; charset=UTF-8"  errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<s:if test="result.group.groupId > 0">
	<s:set name="groupId" value="result.group.groupId"></s:set>
</s:if>
<s:else>
	<s:set name="groupId" value="result.groupId"></s:set>
</s:else>
<script type="text/javascript">
	$(function(){
		$("#menuGroupForm").validate({
			submitHandler: function(form){ 
				$(form).ajaxSubmit({
				    beforeSubmit: function (formData, jqForm, options){
				   	 	$("#formSubmitLoading").show();
						return true;
				    },  
					success: function (response, status){
						$("#formSubmitLoading").hide(); 
						$("#dialogMessage").show().html(response.msg);
						if(response.tip == "addSuccess" || response.tip == "editSuccess"){
 							$('#menuGroupFormDialog').dialog('close');
 							
 							if(response.tip == "addSuccess"){
 								/**
 								var num = $("[name='menuTrfalse']").size() + 1;
 								var buffer = "<tr id='false" + menu.groupId + "' name='menuTrfalse' style='height:40px;'>";
 								buffer += "<td>" + (num + 3) + "</td>";
 								buffer += "<td>" +  menu.groupName + "</td>";
 								buffer += "<td>0</td>";
 								buffer += "<td>" +  menu.modifyTime + "</td>";
 								buffer += "<td>";
 								buffer += "<a href='/menu/menu_add.htm?groupId=" + menu.groupId + "'>添加信息</a><br />";
								buffer += "<a href='#position;' onclick='javascript:showFormDialog(\"/menu/menu_group_edit.do?groupId=" + menu.groupId + "\", \"修改栏目\")' >修改</a> ";
								buffer += "<a href=\"javascript:deleteGroup('"+menu.groupId+"')\">删除</a>";
								buffer += "</td></tr>";
 								$("[name^='menuTr']:last").after(buffer);
 								
 								var title = $("h1").html();
 								$("h1").html(title.replace(/\d{1,}/i , num));
 								*/
 								
								window.location.href = "/menu/menu_group_list.htm";
 							}else{
	 							var menu = response.model;
	 							
 								id = "false" + menu.groupId;
 								var titleObj = $("#" + id + " td:eq(1) a");
 								if(titleObj.size() > 0){
 									titleObj.text(menu.groupName);
 								}else{
 									$("#" + id + " td:eq(1)").html(menu.groupName);
 								}
 								$("#" + id + " td:eq(4)").html(menu.modifyTime);
 							}
						} else {
 							$("#group_input_error").css("display","inline").html(response.msg);
						}
					},
				    type: 'post',        		
				    dataType: 'json'        
				}); 
			},
			rules: {
				groupName: {required:true, maxlength:60}
			},
			messages: {
				groupName: "<s:text name='group.groupName.required'/>"
			}	
		});
	});
	
</script>

<s:if test="result.msg == 'full'">
	<s:property value="result.tip"/>
</s:if>
<s:else>
<form id="menuGroupForm" name="menuGroupForm" action="<s:property value="result.formAction"/>" method="post">
	<input type="hidden" name="groupId" value="<s:property value='#groupId' />"/>
	<div id="formSubmitLoading" style="display:none;">
		<img src="/img/loading1.gif" /> &nbsp;Loading...
	</div>
	<table class="formTable" style="width:500px;">
		<tr>
	 		<th><span class="red">*&nbsp;</span><s:text name="group.groupName" />：</th>
	 		<td><input type="text" style="width:150px;" id="groupName_input" name="groupName" value="<s:property value='result.group.groupName' />" />
<!--	 		<span class="red" id="group_input_error"></span>-->
				<label id="group_input_error" class="error" for="groupName_input" generated="true" style="display: none;">请输入菜单名，长度60个字符内！</label>
	 		</td>
 		</tr>
 		<tr>
	 		<th><s:text name="group.isFold" />：</th>
	 		<td>
	 			<input type="radio" name="fold" value="true" <s:if test="result.group.fold == true">checked</s:if>/>&nbsp折叠&nbsp;&nbsp;&nbsp;
	 			<input type="radio" name="fold" value="false" <s:if test="result.group.fold == false">checked</s:if>/>&nbsp展开
	 		</td>
 		</tr>
 		<tr>
	 		<th><s:text name="group.listStyle" />：</th>
	 		<td>
	 			<input type="radio" name="listStyle" value="1" <s:if test="result.group.listStyle == 1 ">checked</s:if> />&nbsp<img src="/img/ico/list.gif"/>&nbsp;列表
	 			<input type="radio" name="listStyle" value="2" <s:if test="result.group.listStyle == 2">checked</s:if>/>&nbsp<img src="/img/ico/gallery.gif"/>&nbsp;橱窗
	 		</td>
 		</tr>
 		<tr>
	 		<th><s:text name="group.isShowDate" />：</th>
	 		<td>
	 			<input type="radio" name="showDate" value="true" <s:if test="result.group.showDate == true">checked</s:if>/>&nbsp<s:text name="yes" />&nbsp;&nbsp;&nbsp;
	 			<input type="radio"  name="showDate" value="false" <s:if test="result.group.showDate == false">checked</s:if>/>&nbsp<s:text name="no" />		
	 		</td>
 		</tr>
	</table>
	<input type="submit" value="<s:text name='button.submit'/>" class="popButton" style="margin-left:200px;*margin-left:200px !important;*margin-left:200px;">
</form>
</s:else>