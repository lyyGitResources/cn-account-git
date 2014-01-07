<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='top.editTitle'/></title>
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
				validatorLink: function(value, element) {
					return /^http:\/\/[a-zA-Z]{1}[a-zA-Z0-9_\\-]{3,19}\.cn\.hisupplier\.com\/product\/detail-[1-9]\d*\.html$/i.test(value);
				}
			});

			$(function(){
 				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 770,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}		
				});
				
 				$("#topForm").validateForm({
					rules: {
						proName:{required:true},
						brief:{required:true}
					},
					messages: {
						proName: {required:'<s:text name="top.proName.required"/>'},
						brief: {required:'<s:text name="top.brief.required"/>'}
					}
				});
				
				changeLink2Show();
 			});
 			
 			function changeLink2Show(){
				if($("#link").val() != ""){
					$("#link2Div").css("display","none");
					$("#link2TipDiv").css("display","none");
					$("#link2").rules("remove");
				}else{
					$("#link2Div").css("display","");
					$("#link2TipDiv").css("display","");
					$("#link2").rules("add", {required:true,validatorLink:true,messages: {required:'<s:text name="top.link.required"/>', validatorLink:'<s:text name="top.link.required"/>'}});

				}
			}

		</script>
	</head>
	<body>
		<%@ include file="/page/inc/image_error.jsp" %>
		<form id="topForm" name="topForm" method="post" action="/ad/top_edit_submit.htm" enctype="multipart/form-data" >
			<input type="hidden" name="comId" value="<s:property value="result.top.comId"/>"/>
			<input type="hidden" name="topId" value="<s:property value="result.top.topId"/>"/>
			<s:token />
			<table class="formTable">
				<tr>
					<th>
						<s:text name="top.proImg" />：
					</th>
					<td class="fieldTips">
						<%
							int imgType = Image.PRODUCT;
							String imgSrcTag = "imgSrc";
							String imgPathTag = "imgPath";
							String imgIdTag = "imgId";
						%>
						<s:set name="imgSrc" value="result.top.imgPath75" />
						<s:set name="imgPath" value="result.top.imgPath" />
						<s:set name="imgId" value="" />
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_bar.jsp" %>
						<br />
						<%@ include file="/page/product/inc/watermark.jsp" %>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='top.proName'/>：</th>
					<td>
						<input type="text" name="proName" value="<s:property value="result.top.proName"/>" style="width: 300px;"/><br />
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='upgrade.remark'/>：</th>
					<td>
						<textarea id="brief" name="brief" style="width: 450px; height: 70px;"><s:property value="result.top.brief"/></textarea>
						<div id="remarkTip"></div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='top.link'/>：</th>
					<td>
						<select name="link" id="link" onchange="changeLink2Show();" style="width:200px;float:left;">
							<option value="<s:property value="result.top.webBase"/>" <s:if test="result.top.link1 == result.top.webBase">selected</s:if>><s:property value="result.top.webBase"/></option>
							<option value="<s:property value="result.top.webProList"/>" <s:if test="result.top.link1 == result.top.webProList">selected</s:if>><s:property value="result.top.webProList"/></option>
							<option value="<s:property value="result.top.webAboutUs"/>" <s:if test="result.top.link1 == result.top.webAboutUs">selected</s:if>><s:property value="result.top.webAboutUs"/></option>
							<option value="" <s:if test="result.top.link1 == ''">selected</s:if>><s:text name="top.otherLink" /></option>
						</select>
						<div id="link2Div" style="float:left;display:none">
							&nbsp;<s:text name="top.webSite" />:
							<input type="text" id="link2"  name="link2" value="<s:property value="result.top.link2"/>" style="width:250px;"/>
						</div>
						<div id="link2Tip" style="float:left"></div>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit"  value="<s:text name='button.submit' />"/> &nbsp;&nbsp;
				<input type="reset"  value="<s:text name='button.reset' />"/> 
			</div>
		</form>
		<div id="selectImgDialog"></div>
	</body>
</html>
