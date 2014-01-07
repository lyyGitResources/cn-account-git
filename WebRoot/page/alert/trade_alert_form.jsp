<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.tradeAlert.id > 0">
	<s:set name="pageTitle" value="%{getText('alert.editTitle')}"></s:set>
	<s:set name="formAction" value="'/alert/trade_alert_edit_submit.htm'" />
	<s:set name="comId" value="result.tradeAlert.comId" />
	<s:set name="userId" value="result.tradeAlert.userId" />
</s:if>
<s:else>
	<s:set name="pageTitle" value="%{getText('alert.addTitle')}" />
	<s:set name="formAction" value="'/alert/trade_alert_add_submit.htm'" />
	<s:set name="comId" value="loginUser.comId" />
	<s:set name="userId" value="loginUser.userId" />
</s:else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:property value="#pageTitle" /></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
				// 至少选中一个复选框
				oneChecked: function(value, element, param) {
					var result = false;
					if($(element).attr("checked")){
						result = true;
					}else{
						$(param).each(function(){
							if($(this).attr("checked")){
								result = true;
							}
						});
					}
					return result;
				}
			});
			
			$(function (){
				$("#keywordDiv").category({
					auto: true,
					catIds: "<s:property value='result.tradeAlert.catIds'/>",
					triId: "keyword",
					contId: "keywordDiv",
					contType: "div"
				});	
				
				$("#_catList").category({
					contId: "catName",
					contType: "text",
					catIdHidden: "catIds"
				});	
				
				$("#modeKeyword").click(function(){	
					$("#keywordContent,#keywordDiv").show();
					$("#catContent").hide();
					$("#catIds,#catName").val("");
					$("#catName").rules("remove");
					$("#keyword").rules("add", {required:true, messages: {required:'请输入关键词'}});
				});
			
				$("#modeCat").click(function(){
					$("#catContent").show();
					$("#keywordContent").hide();
					$("#keywordDiv").hide().html("");
					$("#keyword").val("");
					$("#keyword").rules("remove");
					$("#catName").rules("add", {required:true, messages: {required:'请选择一个目录'}});
				});
  				
 				$("#alertForm").validateForm({
					submitHandler: function(form){
						var catIds = new Array();
	 					$("#keywordDivCatList input:checked").each(function(){
	 						//$(this).attr("name", "catIds");
	 						catIds.push($(this).val());
	 					});
	 					if(catIds.length > 0){
	 						$("#catIds").val(catIds.join(","));
	 					}
						$("#submitButton").attr("disabled", "disabled");
						$("#submitButton").val("提交中..");
						form.submit();
					},
					rules: {
						product:{oneChecked: "#buy,#company"},
						keyword:{required: true}
					},
					messages: {
						product: "<s:text name='alert.content.required'/>",
						keyword: "<s:text name='alert.keyword.required'/>"
					}
				});
			});
		</script>
	</head>
	<body>
		<span class="sapn1">&quot;<font class="red">*</font>&quot; <s:text name="alert.tip" /></span>
		<form action='<s:property value="#formAction" />' name="alertForm" id="alertForm" method="post">
			<s:token />
			<input type="hidden" name="comId" value="<s:property value='#comId'/>" />
			<input type="hidden" name="userId" value="<s:property value='#userId'/>" />
			<input type="hidden" name="id" value="<s:property value='result.tradeAlert.id'/>" />
			<input type="hidden" name="catIds" id="catIds" value="<s:property value='result.tradeAlert.catIds'/>" />
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="alert.mode" />：</th>
					<td>
						<s:if test="result.tradeAlert.id > 0">
							<s:if test="result.tradeAlert.mode == 'keyword'">
								<s:text name="alert.keyword" />
								<div id="keywordContent" style="margin-top:10px;">
									<input type="text" name="keyword" id="keyword" style="width:350px;" value="<s:property value='result.tradeAlert.keyword' />"/>
									<span id="keywordTip"></span>
								</div>
								<div id="keywordDiv" style="margin-top:5px;"></div>
							</s:if>
							<s:else>
								<s:text name="alert.category" />
								<div id="catContent">
									<input type="text" id="catName" name="catName" style="width:350px;" value="<s:property value='result.tradeAlert.catNamePaths'/>" readonly/>
									<input type="button" id="_cat" value="<s:text name='button.select' />" />
									<br />
									<div id="_catList"></div>
									<span id="catNameTip"></span>
								</div>
							</s:else>
							<input type="hidden" name="mode" value="<s:property value='result.tradeAlert.mode'/>" />
						</s:if>
						<s:else>
							<input type="radio" id="modeKeyword" name="mode" value="keyword" checked />
							<s:text name="alert.keyword" />
							<input type="radio" id="modeCat" name="mode" value="category" />
							<s:text name="alert.category" />	
							<div id="keywordContent" style="margin-top:10px;">
								<input type="text" name="keyword" id="keyword" style="width:350px;" value="<s:property value='result.tradeAlert.keyword' />"/>
								<span id="keywordTip"></span>
							</div>
							<div id="keywordDiv" style="margin-top:5px;"></div>
							
							<div id="catContent" style="display:none">
								<input type="text" id="catName" name="catName" style="width:350px;" value="<s:property value='result.tradeAlert.catNamePaths'/>" readonly/>
								<input type="button" id="_cat" value="<s:text name='button.select' />" />
								<br />
								<div id="_catList"></div>
								<span id="catNameTip"></span>
							</div>
						</s:else>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="alert.content" />：</th>
					<td>
						<input name="product" type="checkbox" value="<s:if test="result.teadeAlert.id > 0"><s:property value='result.tradeAlert.product'/></s:if><s:else>true</s:else>" <s:if test="result.tradeAlert.sell == true || result.tradeAlert.product == true">checked</s:if> />
						&nbsp;<s:text name="alert.sell" />
						<br />
						<input name="buy" id="buy" type="checkbox" value="<s:if test="result.teadeAlert.id > 0"><s:property value='result.tradeAlert.buy'/></s:if><s:else>true</s:else>" <s:if test="result.tradeAlert.buy == true">checked</s:if> />
						&nbsp;<s:text name="alert.buy" />
						<br />						
						<input name="company" id="company" type="checkbox" value="<s:if test="result.teadeAlert.id > 0"><s:property value='result.tradeAlert.company'/></s:if><s:else>true</s:else>"  <s:if test="result.tradeAlert.company == true">checked</s:if> />
						&nbsp;<s:text name="alert.company" />
						<br />
						<span id="productTip"></span>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" id="submitButton" value="<s:text name='button.submit' />"/><%-- 
				<input type="reset" value="<s:text name='button.reset' />"/> 
			--%></div>
		</form>
	</body>
</html>
