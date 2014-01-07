<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	function initSingleRegFileUpload() {
			var singleInstance = new HisupplierUpload({
				upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
				file_types : "*.jpg;*.gif;*.jpeg",
				button_placeholder_id : "singleRegImgButtonId",
				flash_url : "/img/swf/swfupload.swf",
				post_params : {
					"maxSize" : "2"
					},
				custom_settings : {
					fileType : 0,
					processData : processRegSingleFileData
				}
			});
	     };
    function processRegSingleFileData(file,jsonDataArray){
    	var msg = jsonDataArray[0].msg;
		if(msg == "fail") {
			alert("文件最大不能超过2MB");
		} else {
			$("#regImgPathSrc").attr("src", "<%=Config.getString("img.fileLink") %>" +  jsonDataArray[0].filePath);
			$("#regImgPath").val("filePath:" + jsonDataArray[0].filePath + ";fileType:" + jsonDataArray[0].fileType + ";fileName:" + jsonDataArray[0].fileName);
	   		$("#regImgPathTip").html($('<label for="regImgPath" generated="true" class="error success">OK</label>'));
		}
		
    } 

    $(function(){
    	initSingleRegFileUpload();
 		$("#removeRegImg").click(function(){
			$("#regImgPathSrc").attr("src", "<%=Config.getString( "img.default" )%>");
			$("#regImgPath").val("");
			$("#regImgPathTip").html('<label class="error" generated="true" for="regImgPath">营业执照或有效证件不能为空</label>');
		});
	
		$("#regImgPath").val("<s:property value='result.company.regImgPath'/>");
    });
</script>

<div class="imgBox75" style="margin-bottom:10px;">
	<img id="regImgPathSrc" src="<s:property value='result.company.regImgSrc'/>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
</div>
<div class="fieldTips" style="float:left;margin-left:85px; margin-top:-80px;*margin-left:85px !important; *margin-left:42px;">
	<b><s:text name="image.format" /></b>
	<br />
	1. <s:text name="image.format.type"><s:param><%=Config.getString("img." + imgType + ".type")%></s:param></s:text>
	<br />
	2. <s:text name="image.format.size"><s:param>500</s:param></s:text>
	<br/>
	3. 图片最大不得超过2M。
</div>

<input type="hidden" name="regImgPath" id="regImgPath" value="<s:property value='result.company.regImgPath'/>" />
	<div style="display:block;float:left;padding-right:2px;"><span id="singleRegImgButtonId"></span></div>
<input type="button" value="<s:text name='button.remove' />" id="removeRegImg" />
<div id="regImgPathTip" style="margin-top:5px;"></div>