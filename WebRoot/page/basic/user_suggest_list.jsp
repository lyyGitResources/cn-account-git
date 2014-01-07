<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<title><s:text name="userSuggest.title" /></title>
	<script type="text/javascript">
		$(function () {
			$("#userSuggestForm").validate({
				submitHandler: function(form){ 
					$(form).ajaxSubmit($.extend(ajaxSubmitOptions,{
						success: function (response, status){
								$("#dialogLoading").hide(); 
								if(response.tip == "addSuccess"){
									$('#replyFormDialog').dialog('close');
									$("#title").val("");
									ListTable.reload({'msg':response.msg});
								}else{
									$("#dialogMessage").show().html(response.msg);
								}
							},
						type: 'post',        		
						dataType: 'json',
						resetForm: true 
					}));
				},
				rules: {
					title: {required:true},
					content: {required:true}
				},
				messages: {
					title: {required:"<s:text name='userSuggest.title.required'/>"},
					content: "<s:text name='userSuggest.content.required'/>"
				}
			});
		});			
	</script>
	</head>	
	<body>
		<div class="serviceTips">
			<span><s:text name="userSuggest.tip1" /></span>
			<br /><br />
			<s:text name="userSuggest.tip2" />
		</div>
		<form id="userSuggestForm"  method="post" action="/basic/user_suggest_add.htm">
		<input type="hidden" value="<s:property value="result.comId"/>" name="comId"/>
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="userSuggest.subject" />：</th>
					<td><input type="text" <% if(request.getHeader("REFERER") != null && request.getHeader("REFERER").indexOf("businesscentre") != -1){ %>value="关于商务中心的意见/建议"<% } %> id="title" name="title" style="width:566px"/></td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="userSuggest.content" />：</th>
					<td><textarea name="content" style="width:566px;height:100px;"></textarea></td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name='button.submit' />"/> 
				<input type="reset" value="<s:text name='button.reset' />"/> 
			</div>
		</form>
		<div id="listTable">
			<%@ include file="/page/basic/user_suggest_list_inc.jsp" %>	
		</div>	
	</body>
</html>