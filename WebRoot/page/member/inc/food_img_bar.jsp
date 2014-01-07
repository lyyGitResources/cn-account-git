<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.hisupplier.commons.Config"
%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<style>
.food_img_bar {width: 430px; margin-left: -28px; }
.food_img_bar td { width: 33%; text-align: center }
</style>
<div style="color: #888;">
1、图片 JPG, JPEG, GIF 格式; 2、超过 500k 的图片会被压缩处理; 3、图片最大不得超过 2M。<br />
<span style="color: red;">请根据您的行业要求，上传对应的生产经营许可证，无需上传所有许可证照片。</span>
</div>
<table border="0" class="food_img_bar">
<tr>
<td>食品生产许可证</td>
<td>食品流通许可证</td>
<td>工业产品生产许可证（针对食品添加剂经营者）</td>
</tr>

<tr>
<td><img id="f1Img" width="80" height="80" src="<s:property value='model.f1ImgSrc' />" /></td>
<td><img id="f2Img" width="80" height="80" src="<s:property value='model.f2ImgSrc' />" /></td>
<td><img id="f3Img" width="80" height="80" src="<s:property value='model.f3ImgSrc' />" /></td>
</tr>

<tr>
<td>
	<input id="f1Upload" type="button" value="从本地上传" /><br />
	<input class="img_rm" data-img="f1" type="button" value="移除" />
</td>
<td>
	<input id="f2Upload" type="button" value="从本地上传" /><br />
	<input class="img_rm" data-img="f2" type="button" value="移除" />
</td>
<td>
	<input id="f3Upload" type="button" value="从本地上传" /><br />
	<input class="img_rm" data-img="f3" type="button" value="移除" />
</td>
</tr>
</table>
<input id="f1ImgPath" name="f1ImgPath" type="hidden" value='<s:property value="model.f1ImgPath"/>' />
<input id="f2ImgPath" name="f2ImgPath" type="hidden" value='<s:property value="model.f2ImgPath"/>' />
<input id="f3ImgPath" name="f3ImgPath" type="hidden" value='<s:property value="model.f3ImgPath"/>' />
<script>

var $f1ImgPath = $("#f1ImgPath"),
	$f2ImgPath = $("#f2ImgPath"),
	$f3ImgPath = $("#f3ImgPath"),
	$f1Img = $("#f1Img"),
	$f2Img = $("#f2Img"),
	$f3Img = $("#f3Img"),
	imgDefault = "<%=Config.getString("img.default") %>",
	imgRoot = "<%=Config.getString("img.fileLink") %>";
	
$(".img_rm").bind("click", function() {
	var $el = $(this), img = $el.attr("data-img");
	switch(img) {
	case "f1":
		$f1Img.attr("src", imgDefault);
		$f1ImgPath.val("");
		break;
	case "f2":
		$f2Img.attr("src", imgDefault);
		$f2ImgPath.val("");
		break;
	case "f3":
		$f3Img.attr("src", imgDefault);
		$f3ImgPath.val("");
		break;
	default:
		aler("error");
	}
});


var f1upload = new HisupplierUpload({
	upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
	file_types : "*.jpg;*.gif;*.jpeg",
	button_placeholder_id : "f1Upload",
	flash_url : "/img/swf/swfupload.swf",
	post_params : { "maxSize" : "2" },
	custom_settings : { fileType : 0, processData : food_1_process }
});

var f2upload = new HisupplierUpload({
	upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
	file_types : "*.jpg;*.gif;*.jpeg",
	button_placeholder_id : "f2Upload",
	flash_url : "/img/swf/swfupload.swf",
	post_params : { "maxSize" : "2" },
	custom_settings : { fileType : 0, processData : food_2_process }
});

var f3upload = new HisupplierUpload({
	upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
	file_types : "*.jpg;*.gif;*.jpeg",
	button_placeholder_id : "f3Upload",
	flash_url : "/img/swf/swfupload.swf",
	post_params : { "maxSize" : "2" },
	custom_settings : { fileType : 0, processData : food_3_process }
});

function food_1_process(file, dataArray) {
	var data = dataArray[0];
	if (data.msg == "fail") { alert("上传失败"); }
	else {
		$f1Img.attr("src", imgRoot + data.filePath);
		$f1ImgPath.val(data.filePath);
	}
}

function food_2_process(file, dataArray) {
	var data = dataArray[0];
	if (data.msg == "fail") { alert("上传失败"); }
	else {
		$f2Img.attr("src", imgRoot + data.filePath);
		$f2ImgPath.val(data.filePath);
	}
}

function food_3_process(file, dataArray) {
	var data = dataArray[0];
	if (data.msg == "fail") { alert("上传失败"); }
	else {
		$f3Img.attr("src", imgRoot + data.filePath);
		$f3ImgPath.val(data.filePath);
	}	
}

</script>