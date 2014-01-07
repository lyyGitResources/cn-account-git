<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
;(function() {
		var maxNum = "<s:property value='result.company.regImgType' />"; //企业为1  个人为2
		var index = 0;
		var multiImgInstance;
		function initMultiImgUploads() {
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
			if($("[name='_regImgDiv']").size() > 0){
				index = $("[name='_regImgDiv']").size();
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
			if($("[name='_regImgDiv']").size() > 0){
				index = $("[name='_regImgDiv']:last").attr("id").replace("imgBox","");
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
			if($("[name='_regImgDiv']").size() < maxNum){
				buffer = getRegImgDiv(jsonDataArray[0].filePath,filePathTag,tmpValue);
				if($("[name='_regImgDiv']").size() > 0){
					$("[name='_regImgDiv']:last").after(buffer);
				}else{
					$("#regImgBox").after(buffer);
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

	function getRegImgDiv(filePath, filePathTag, tmpValue){
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
		buffer += "<center id='imgBox_"+ filePathTag +"' name='_regImgDiv' style='float:left;margin-right:10px;'>";
		buffer += "<div class='imgBox75' style='margin-bottom:5px;'>";
		buffer += "<img src='" + imgSrc + "'  id='" + filePathTag + "SrcId'  onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(75) %>')\" onload='Util.setImgWH(this, 75, 75)' />";
		buffer += "</div>";
		buffer += "<a href='#' class='delImg' data-filePathTag='" + filePathTag + "' name='multiDelete'>删除</a>";
		if (filePathTag =="regImgPath") {
			$("#regImgPath").val(tmpValue);
	    	$("#regImgPathTip").html($('<label for="regImgPath" generated="true" class="error success">OK</label>'));
	    	$("#regImgPathSrc").attr("src", imgSrc);
		}else if (filePathTag =="regImgPath2") {
			$("#regImgPath2").val(tmpValue);
			$("#regImgPathSrc2").attr("src", imgSrc);
		}
		//buffer += "<input type='hidden' name='" + filePathTag + "' id='" + filePathTag +"' value='" + tmpValue + "' />";
		buffer += "</center>";
		return buffer;
	}



	$(function () {	
		initMultiImgUploads();
		//multiRemove( \""+ filePathTag +"\" )
		
		var image1 = "<s:property value='#image1' />";
		var image2 = "<s:property value='#image2' />";
		var imageArray = [image1,image2];
		
		if(imageArray != ""){
			var buffer = "";
			var fileTags = ["regImgPath","regImgPath2"];
			for(var i = 0; i < imageArray.length; i++){
				if(imageArray[i].length != 0){
					buffer  += getRegImgDiv(imageArray[i], fileTags[i]);	
				}
			}
			$("#regImgBox").after(buffer);
		}
		
		$("#imgTips").css("color","#888");
		$("#imgTips").html('<b>图片格式</b><br /> 1. 上传加盖贵司公章且有最新年检的营业执照<br/> 2. JPG,JPEG,GIF<br/> 3. 图片最大不能超过2M');
		
		$("#rCompany").click(function(){
			maxNum = 1;		//企业时上传最大数量为1
			if ($("#regImgPath").val() != "" && $("#regImgPath2").val() != "") {
				$("#imgBox_regImgPath2").hide();
			}
			$("#regImgPathSrc2").removeAttr("src");
			$("#imgTips").html('<b>图片格式</b><br /> 1. 上传加盖贵司公章且有最新年检的营业执照<br/> 2. JPG,JPEG,GIF<br/> 3. 图片最大不能超过2M');
		});
		$("#rIndividua").click(function(){
			maxNum = 2; 	//个人时上传最大数量为2
			$("#imgBox_regImgPath2").show();
			var yulanPath2 = "";
			if ($("#regImgPath2").val().indexOf("filePath") == -1) {
				yulanPath2 = $("#regImgPath2").val();
			}else {
				var filePathTmp = $("#regImgPath2").val();
				yulanPath2 = filePathTmp.substring(9,filePathTmp.indexOf(";"));
			}
			$("#regImgPathSrc2").attr("src", "<%=Config.getString("img.fileLink") %>" + yulanPath2);
			$("#imgTips").html('<b>图片格式</b><br /> 1. 上传个人身份证（使用第二代身份证，请提供正反两面复印件）<br/> 2. JPG,JPEG,GIF的格式<br/> 3. 图片大小不超过2M');
		});
		
		$("#imgBox_regImgPath .delImg").live("click", function(){
			$("#imgBox_regImgPath").remove();
			$("#regImgPathTip").html("");
			$("#regImgPathSrc").removeAttr("src");
			$("#regImgPath").val("");
			return false;
		});
		$("#imgBox_regImgPath2 .delImg").live("click", function(){
			$("#imgBox_regImgPath2").remove();
			$("#regImgPathSrc2").removeAttr("src");
			$("#regImgPath2").val("");
			return false;
		});
		
	});
})();
</script>
<div style="display:block;"><span id="joinMultiImgButtonId"></span></div>
<div style="width:185px;padding:5px 5px 5px 0px;float:left"> 
	<div id="regImgBox">
		<%--  
		<div style="display:none;float:left;padding-right:2px;"><span id="joinMultiImgButtonId"></span></div>
		<div style="display:block;float:left;padding-right:2px;">
			<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
			<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
		</div>
		--%>
		<%-- <input id="_multiRemoveAll" type="button" style="float:left;" value="全部删除" /> --%>
		<div style="clear: both;padding:0px;margin:0px;margin-bottom: 5px;"></div>
	</div>
</div>
<img id="regImgPathSrc" style="display: none;"/>
<img id="regImgPathSrc2" style="display: none;"/>