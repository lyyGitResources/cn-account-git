<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%--
req:
	String type = "tax";
	String imgPath = "";
	String imgSrc = "";
 --%>
<script type="text/javascript">
function initSingle<s:property value="#type"/>FileUpload() {
	var single<s:property value="#type"/>Instance = new HisupplierUpload({
		upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
		file_types : "*.jpg;*.gif;*.jpeg",
		button_placeholder_id : "single<s:property value="#type"/>ImgButtonId",
		flash_url : "/img/swf/swfupload.swf",
		post_params : {
			"maxSize" : "2"
		},
		custom_settings : {
			fileType : 0,
			processData : process<s:property value="#type"/>SingleFileData
		}
	});
};

function process<s:property value="#type"/>SingleFileData(file,jsonDataArray){
  	var msg = jsonDataArray[0].msg;
	if(msg == "fail") {
		alert("文件最大不能超过2MB");
	} else {
		$("#<s:property value="#type"/>ImgPathSrc").attr("src", "<%=Config.getString("img.fileLink") %>" +  jsonDataArray[0].filePath);
		$("#<s:property value="#type"/>ImgPath").val("filePath:" + jsonDataArray[0].filePath 
				+ ";fileType:" + jsonDataArray[0].fileType + ";fileName:" + jsonDataArray[0].fileName);
	}
} 

$(function(){
 	initSingle<s:property value="#type"/>FileUpload();
	$("#remove<s:property value="#type"/>Img").click(function(){
	$("#<s:property value="#type"/>ImgPathSrc").attr("src", "<%=Config.getString("img.default")%>");
		$("#<s:property value="#type"/>ImgPath").val("");
	});
});
</script>
<div class="imgBox75" style="margin-bottom:10px;">
	<img id="<s:property value="#type"/>ImgPathSrc" src="<s:property value="imgSrc"/>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
</div>
<div class="fieldTips" style="float:left;margin-left:93px; margin-top:-88px;*margin-left:85px !important; *margin-left:42px;">
	<b><s:text name="image.format" /></b>
	<br />
	1. JPG, JPEG, GIF 格式
	<br />
	2. <s:text name="image.format.size"><s:param>500</s:param></s:text>
	<br/>
	3. 图片最大不得超过2M。
</div>

<div style="display:block;float:left;padding-right:2px;"><span id="single<s:property value="#type"/>ImgButtonId"></span></div>
<input type="button" value="<s:text name='button.remove' />" id="remove<s:property value="#type"/>Img" />
<p style="margin-top: 10px"></p>
<input type="hidden" name="<s:property value="#type"/>ImgPath" id="<s:property value="#type"/>ImgPath" value="<s:property value="#imgPath"/>" />