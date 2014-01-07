<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>上传图片</title>
		<style type="text/css">
			body {
				background:none repeat scroll 0 0 #F9F9F9;
				color:black;
				font-family:Arial,Helvetica,sans-serif;
				font-size:12px;
				margin:1em;
				padding:0;
				padding: 10px 40px;
			}
			form {
				line-height:20px;
			}
			a:link {
				color:#084B8F;
				text-decoration:none;
			}
			a:visited {
				color:#084B8F;
				text-decoration:none;
			}
			a:hover {
				color:#F26616;
				text-decoration:underline;
			}
			table {
				font-family:Arial,Helvetica,sans-serif;
				font-size:12px;
			}
			.notice {
				color:#999999;
				font-size:12px;
				line-height:20px;
			}
			.error {
				color:#FF0000;
			}
		</style>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.validate.messages_zh.js"></script>
		<script type="text/javascript" src="/js/colorPalette/functions.js"></script>
		<script type="text/javascript">
			$(function(){
				if($("#watermark").attr("checked")){
					$("#watermarkDiv").show();
				}
				if($("#textOption").attr("checked")){
					$("#textOptionDiv").show();
				}
				$("#colorSrc").attr("src","/js/colorPalette/rectNoColor.gif").click(function(){getColorPalette('colorSrc','watermarkTextColor');});
				$("#watermark").click(function(){$("#watermarkDiv").toggle()});
				
				$("#uploadForm").validate({
					rules: {
						attachment: {required:true}
					},
					messages: {
						attachment: "请选择一张图片！"
					}
				});
			});
		</script>
	</head>
	<body>
		<div style="margin-top: 10px;">
			<div class="notice">
				<b><s:text name="image.format" /></b>
				<br />
				1. <s:text name="image.format.type"><s:param><s:property value="result.image.type"/></s:param></s:text>
				<br />
				2. <s:text name="image.format.size"><s:param><s:property value="result.image.maxSize"/></s:param></s:text>
				<br />
				3. <s:text name="image.format.scale"><s:param><s:property value="result.image.bestScale"/></s:param></s:text>
			</div>
		</div>
		<div class="error">
			<s:actionerror/>
		</div>
		
		<form id="uploadForm" action="/image/image_upload_submit.do" method="post" enctype="multipart/form-data">
			<input type="hidden" name="comId" value="<s:property value="loginUser.comId"/>"/>
			<input type="hidden" name="imgType" value="<s:property value="imgType"/>"/>
			<input type="hidden" name="imgSrcTag" value="<s:property value="imgSrcTag"/>"/>
			<input type="hidden" name="imgIdTag" value="<s:property value="imgIdTag"/>"/>
			<input type="hidden" name="imgPathTag" value="<s:property value="imgPathTag"/>"/>
			<table width="100%" cellpadding="5" cellspacing="0">
				<tr>
					<td>
						<span style="color:red;">*</span> 图片 <input id="attachment" type="file" name="attachment" style="width:240px;"/>
					</td>
				</tr>
				<s:if test="imgType ==3">
				<tr>
					<td>
						<input type="checkbox" name="watermarkRight" value="true" <s:property value="result.image.watermarkRightCheck"/>/>&nbsp;图片右下角是否加上水印 cn.hisupplier.com? <br />
	
						<input type="checkbox" id="watermark" name="watermark" value="true" <s:property value="result.image.watermarkCheck"/> />&nbsp;给图片加水印：在文本框中输入需要添加的水印，<a href="<%=Config.getString("img.base")%>/img/example_watermark.jpg" target="_blank">查看示例</a>
															
						<div id="watermarkDiv" style="display:none;margin-top: 5px;">
							&nbsp;<input type="text" name="watermarkText" value="<s:property value="result.image.watermarkTextReal"/>" style="width:200px;" />
							<img id="colorSrc" style="width:22px;height:22px;cursor: pointer;vertical-align: bottom">
							<input type="text" id="watermarkTextColor" name="watermarkTextColor" value="<s:property value="result.image.watermarkTextColorReal"/>" maxlength="7" style="width: 60px; background-color:<s:property value="result.image.watermarkTextColorReal "/>;">
							
							字号
							<select id="textFontSize" name="textFontSize">
								<option value="0">自动</option>
								<s:iterator value="result.image.textFontSizeList" id="size">
									<option value="<s:property value="#size" />" <s:if test="#size == result.image.textFontSize">selected</s:if>><s:property value="#size" /></option>
								</s:iterator>
							</select>
								
							<div style="margin-top: 3px; color: #999999">
								<iframe width="260" height="165" id="colorPalette" src="/js/colorPalette/color.html" style="visibility: hidden; position: absolute; border: 1px gray solid" frameborder="0" scrolling="no"></iframe>
							</div>
						</div>
					</td>
				</tr>
				</s:if>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td align="center" style="top: 30px;"> 
						<input type="submit" id="submit" style="width: 80px;" value="提交">	
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>