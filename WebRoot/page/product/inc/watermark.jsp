<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="/js/colorPalette/functions.js"></script>
<script type="text/javascript">
	$(function(){
		if($("#watermark").attr("checked")){
			$("#watermarkDiv").show();
		}

		$("#colorSrc").attr("src","/js/colorPalette/rectNoColor.gif").click(function(){getColorPalette('colorSrc','watermarkTextColor');});
		$("#watermark").click(function(){$("#watermarkDiv").toggle()});
	});
</script>
<div id="watermarkPart" style="display:none;margin-top:5px;">
	<input type="checkbox" id="watermarkRight" name="image.watermarkRight" value="true" <s:property value="result.image.watermarkRightCheck"/>/>&nbsp;图片右下角是否加上水印 cn.hisupplier.com? <br />
	
	<input type="checkbox" id="watermark" name="image.watermark" value="true" <s:property value="result.image.watermarkCheck"/> />&nbsp;给图片加水印：在文本框中输入需要添加的水印，<a href="<%=Config.getString("img.base")%>/img/example_watermark.jpg" target="_blank">查看示例</a>
										
	<div id="watermarkDiv" style="display:none;margin-top: 5px;">
		<input type="text" id="watermarkText" name="image.watermarkText" value="<s:property value="result.image.watermarkTextReal"/>" style="width:200px;" />
		<img id="colorSrc" style="width:20px;height:20px;cursor: pointer;vertical-align: bottom">
		<input type="text" id="watermarkTextColor" name="image.watermarkTextColor" value="<s:property value="result.image.watermarkTextColorReal"/>" maxlength="7" style="width: 60px; background-color:<s:property value="result.image.watermarkTextColorReal "/>;">
		字号
		<select id="textFontSize" name="image.textFontSize">
			<option value="0">自动</option>
			<s:iterator value="result.image.textFontSizeList" id="size">
				<option value="<s:property value="#size" />" <s:if test="#size == result.image.textFontSize">selected</s:if>><s:property value="#size" /></option>
			</s:iterator>
		</select>
		
		<div style="margin-top: 3px; color: #999999">
			<iframe width="260" height="165" id="colorPalette" src="/js/colorPalette/color.html" style="visibility: hidden; position: absolute; border: 1px gray solid" frameborder="0" scrolling="no"></iframe>
		</div>
	</div>
</div>