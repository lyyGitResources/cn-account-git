<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiryReply.addTitle'/></title>
		<!--	
		<script type="text/javascript" src="/tiny_mce/tiny_mce.js"></script>
		<script type="text/javascript" src="/tiny_mce/tiny.extend.js"></script>
		-->
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript">
			var fckeditor = null;
			$(document).ready(function(){
				$("#descriptionDiv").hide();
				fckeditor = new FCKeditor("content", 600, 300, "Admin") ;
				fckeditor.ReplaceTextarea();
				// TinyMCE_initEditer("content", "zh", 600, 300);
				
				$("#inquiryReplyForm").validateForm({
					rules: {
						subject: {required:true, maxlength:120}
					},
					messages: {
						subject: '<s:text name="inquiryReply.subject.required"/>'
					}
				});
				
				$("#descriptionDiv").html($("#content").val());
				if($("#descriptionDiv").height() > 300){
					$("#descriptionDiv").css({height:"300px",overflow:"scroll"});
				}
				$("#showOriginal").click(function(){
					if($(this).attr("checked")){
						$("#original").show();
					}else{
						$("#original").hide();
					}
				});
			});
			function checkContent(){
				// if( $.trim(tinyMCE.get("content").getContent()) == "" || $.trim(tinyMCE.get("content").getContent()) == "<br />" ){
				if( $.trim(FCKeditorAPI.GetInstance("content").GetXHTML()) == "" || $.trim(FCKeditorAPI.GetInstance("content").GetXHTML()) == "<br />" ){
					alert('<s:text name="inquiryReply.content.required" />');
					return false;
				}else if(!showSpecialChar("content")){
					return false;
				}else{
					return true;
				}
			}
		</script>
	</head>
	<body>
		<div class="itemListSelect1">
	 		<input type="checkbox" id="showOriginal"/> <s:text name="inquiry.showInquriy" />
	 		<div id="original" style="display:none;">
	 			<iframe src="/page/inquiry/content.jsp?inqId=<s:property value='result.inquiry.inqId'/>" scrolling="auto" width="100%" height="500px" frameborder="0"></iframe>
	 		</div>
	  	</div>
		<form id="inquiryReplyForm" name="inquiryReplyForm" method="post" action="/inquiry/inquiry_reply_add_submit.htm" enctype="multipart/form-data" onsubmit="return checkContent();">
			<input type="hidden" name="inqId" value="<s:property value="result.inquiry.inqId"/>"/>
			<input type="hidden" name="comId" value="<s:property value="result.inquiry.comId"/>"/>
			<input type="hidden" name="userId" value="<s:property value="result.inquiry.userId"/>"/>
			<input type="hidden" name="toName" value="<s:property value="result.inquiry.fromName"/>"/>
			<input type="hidden" name="toEmail" value="<s:property value="result.inquiry.fromEmail"/>"/>
			<input type="hidden" name="fromName" value="<s:property value="result.fromName"/>"/>
			<input type="hidden" name="fromEmail" value="<s:property value="result.fromEmail"/>"/>
			<input type="hidden" name="fromContent" value="<s:property value="result.inquiry.content"/>"/>
			<s:token />
			<table class="formTable">
				<tr>
					<th><s:text name="inquiryReply.toName" />：</th>
					<td><s:property value="result.inquiry.fromName" /></td>
				</tr>
				<tr>
					<th><s:text name="inquiryReply.toEmail" />：</th>
					<td><s:property value="result.inquiry.fromEmail" /></td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="inquiryReply.subject" />：</th>
					<td>
						<input name="subject" style="width:300px" value="Re:<s:property value="result.inquiry.subject"/>"/>
					</td>
				</tr>
				<tr>
					<th><s:text name="attachment" />：</th>
					<td>
						<input type="file" name="upload" /><br />
						<input type="file" name="upload" /><br />	
						<input type="file" name="upload" /><br />	
						<div class="fieldTips">
							<s:text name="attachment.format" >
								<s:param>jpg、gif、txt、doc、pdf、xls</s:param>
								<s:param>500</s:param>
							</s:text>
						</div>
					</td>
				</tr>
				<tr>
	            	<th><span class="red">*</span>&nbsp;<s:text name="inquiryReply.content" />:</th>
	            	<td>
						<div id="descriptionDiv"></div>
						<textarea name="content" id="content"></textarea>
					</td>
	          	</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit"  value="<s:text name='button.submit' />"/> &nbsp;&nbsp;
				<input type="reset"  value="<s:text name='button.reset' />"/> 
			</div>
		</form>
	</body>
</html>
