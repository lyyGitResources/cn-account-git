<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
		var maxNum = 1; //企业为1  个人为2
		var index = 0;
		var multiImgInstance;
		function initMultiImgUpload() {
				multiImgInstance = new HisupplierUpload({
				upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
				file_size_limit : "<s:property value='#imgSize' />", // 限制上传大小
				file_types : "<s:property value='#imgExts' />",
				upload_complete_handler : uploadComplete,//多图片自动上传必须开启此函数
				button_placeholder_id : "joinMultiImgButtonId",
				flash_url : "/img/swf/swfupload.swf",
				custom_settings : {
					uploadStartValid : true,
					fileType : 0,
					imgExts : "<s:property value='#imgExts' />",//要检验的文件格式
					imgSize : "<s:property value='#imgSize' />",//要检验的文件大小
					comId : "<s:property value='loginUser.comId' />",
					uploadStartValid: uploadStartValid,
					processData : processSuccess
				}
			});
	     };
	   function uploadStartValid(){//在用swfupload上传文件开始时的检验函数
			if($("[name='_multiDiv']").size() > 0){
				index = $("[name='_multiDiv']").size();
			}
			index = parseInt(index,10);
	    	return maxNum - index;
	    }
		function processSuccess(file, jsonDataArray) {//在用swfupload上传文件后的接收数据函数
			if(jsonDataArray[0].msg == "fail"){
				if(jsonDataArray[0].errorTip){
					alert(jsonDataArray[0].errorTip);
				}else {
					alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
				}
				return ;
			}
			if($("[name='_multiDiv']").size() > 0){
				index = $("[name='_multiDiv']:last").attr("id").replace("_multiDiv","");
			}

			//如果是第一张 name 属性为 regImgPath
			var filePathTag = 'regImgPath';
			if ($("#regImgPath").val() == "" && $("#regImgPath2").val() == "") {
			    filePathTag = 'regImgPath';
			}else if ($("#regImgPath").val() == "") {
				filePathTag = 'regImgPath';
			}else if ($("#regImgPath2").val() == "") {
				filePathTag = 'regImgPath2';
			}
			
			var tmpValue = "filePath:" + jsonDataArray[0].filePath + ";fileType:" + jsonDataArray[0].fileType + ";fileName:" + jsonDataArray[0].fileName;
			index = parseInt(index,10);
			if($("[name='_multiDiv']").size() < maxNum){
				buffer = getImgDiv(jsonDataArray[0].filePath,filePathTag,tmpValue);
				if($("[name='_multiDiv']").size() > 0){
					$("[name='_multiDiv']:last").after(buffer);
				}else{
					$("#_imageMulti").after(buffer);
				}
			}else{
				alert("不能再添加了！");
			}
		}
	/**
	 * 生成单个图片模块
	 * @param imgPath 
	 * @param imgPathTag 图片路径保存位置
	 * @param index 生成的图片模块序号，从0开始
	 * @param tmpValue 是否临时图片，即未保存到数据库
	 */
	function getImgDiv(filePath, filePathTag, tmpValue){
		if(filePath.length == 0){
			return "";
		}
		var imgSrc = "<%=Config.getString("img.fileLink") %>" + filePath;
		
			var sindex = imgSrc.indexOf("filePath:");
			if(sindex != -1) {
				pathIndex = imgSrc.indexOf("filePath") + 8 ;
				var end = imgSrc.indexOf(";");
				imgSrc = "<%=Config.getString("img.fileLink") %>" + imgSrc.substring(pathIndex,end);
			}
		
		if(!tmpValue){
			tmpValue = filePath;
		}
		var buffer = "";
		buffer += "<center id='_multiDiv_"+ filePathTag +"' name='_multiDiv' style='float:left;margin-right:10px;'>";
		buffer += "<div class='imgBox75' style='margin-bottom:5px;'>";
		buffer += "<img src='" + imgSrc + "' onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(75) %>')\" onload='Util.setImgWH(this, 75, 75)' />";
		buffer += "</div>";
		buffer += "<a href='javascript:multiRemove( \""+ filePathTag +"\" )' name='multiDelete'>删除</a>";
		if (filePathTag =="regImgPath") {
			$("#regImgPath").val(tmpValue);
	    	$("#regImgPathTip").html($('<label for="regImgPath" generated="true" class="error success">OK</label>'));
		}else if (filePathTag =="regImgPath2") {
			$("#regImgPath2").val(tmpValue);
		}
		//buffer += "<input type='hidden' name='" + filePathTag + "' id='" + filePathTag +"' value='" + tmpValue + "' />";
		buffer += "</center>";
		return buffer;
	}

	function multiRemove(filePathTag){
		if(filePathTag == "regImgPath") {
			$("#regImgPath").val("");
		}else {
			$("#regImgPath2").val("");
		}
		$("#_multiDiv_" + filePathTag).remove();
		if(filePathTag == "regImgPath") {
			$("#regImgPathTip").html("");
		}
	}

	$(function () {	
		$("#imgTips").html('请上传加盖贵司公章且有最新年检的营业执照。<br/>JPG，JPEG，GIF的格式；图片大小不能超过2M。');
		$("#rCompany").click(function(){
			maxNum = 1;		//企业时上传最大数量为1
			$("#regImgPath").val("");
			$("#regImgPath2").val("");
			
			if ($("[name='_multiDiv']").length == 1) {
				multiRemove("regImgPath");
			}else if ($("[name='_multiDiv']").length == 2) {
				multiRemove("regImgPath");
				multiRemove("regImgPath2");
			}
			$("#imgTips").html('请上传加盖贵司公章且有最新年检的营业执照。<br/>JPG，JPEG，GIF的格式；图片大小不能超过2M。');
		});
		$("#rIndividua").click(function(){
			maxNum = 2; 	//个人时上传最大数量为2
			
			$("#regImgPath").val("");
			if ($("[name='_multiDiv']").length == 1) {
				multiRemove("regImgPath");
			}
			$("#imgTips").html('请上传个人身份证（使用第二代身份证，请提供正反两面复印件）。JPG,JPEG,GIF的格式；图片大小不超过2M。');
		});
		$("#rCompany").click();
	});

</script>
<div style="display:block;"><span id="joinMultiImgButtonId"></span></div>
<div style="width:176px;padding:5px 5px 5px 5px;float:left"> 
	<div id="_imageMulti">
		<%--  
		<div style="display:none;float:left;padding-right:2px;"><span id="joinMultiImgButtonId"></span></div>
		<div style="display:block;float:left;padding-right:2px;">
			<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
			<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
		</div>
		--%>
		<%-- <input id="_multiRemoveAll" type="button" style="float:left;" value="全部删除" /> --%>
		<div style="clear: both;padding:0px;margin:0px;margin-bottom: 5px;"></div>
		<script type="text/javascript">initMultiImgUpload();</script>
	</div>
</div>
