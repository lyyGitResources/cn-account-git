<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp" %>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.cn.account.basic.*"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>hisupplier.com</title>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<link rel="stylesheet" href="<%=Config.getString("img.base") %>/css/jquery.inputform.css" type="text/css"></link>
		<script type="text/javascript" src="<%=Config.getString("img.base") %>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base") %>/js/lib/jquery.form.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base") %>/js/lib/jquery.inputform.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base") %>/js/util.js"></script>
		<script type="text/javascript">
			$(function(){
				params = Util.getQueryParams();
				$("#imgSrcTag").val(params.imgSrcTag);
				$("#imgType").val(params.imgType);
				$("#tmpSaveId").val(params.tmpSaveId); // 临时存储文件大小，文件类型，文件名称，格式：imgPath:XX;imgType:XX;imgName:XX
				$("#imgIdTag").val(params.imgIdTag);
				$("#multi").val(params.multi);
				$("#maxNum").val(params.maxNum);

				$("#file").inputfile({button:"<button>从本地选取</botton>"});
				
				if($.browser.msie && $.browser.version == 8.0){
					$("#file").css({top: "0", left:"-150px"});
				}
				
				$("#file").change(function (){
					if(params.multi && window.parent.$("[name='_multiDiv']").size() >= params.maxNum){
						alert("不能再添加了！");
					}else{
						$("#uploadImageForm").submit();
					}
				});

		 		//上传成功
				if(params.imgPath && params.imgPath != ""){
					if(params.multi){
						var tmpValue = "imgPath:" + params.imgPath + ";imgType:" + params.imgType + ";imgName:" + params.imgName;
						var index = 0;
						if(window.parent.$("[name='_multiDiv']").size() > 0){
							index = window.parent.$("[name='_multiDiv']:last").attr("id").replace("_multiDiv","");
						}
						if(index == 0 && window.parent.$("[name='_multiDiv']").size() == 0){
							window.parent.$("#_imageMulti").after(window.parent.getImgDiv(params.imgPath, params.tmpSaveId, index, tmpValue));
						}else{
							window.parent.$("[name='_multiDiv']:last").after(window.parent.getImgDiv(params.imgPath, params.tmpSaveId, (parseInt(index,10)+1), tmpValue));
						}
					}else{
						window.parent.$("#"+params.imgSrcTag).attr("src", "<%=Config.getString("img.base") %>" + params.imgPath);
						window.parent.$("#"+params.tmpSaveId).val("imgPath:" + params.imgPath + ";imgType:" + params.imgType + ";imgName:" + params.imgName);
					}
					if(window.parent.$("#watermarkPart").attr("id")){
						window.parent.$("#watermarkPart").show();
					}
				}

		 		//上传失败		 		
				
				if(params.imgNotFound && params.imgNotFound == "true"){
					alert("请从您的计算上选取一张图片");
					return;
				}
				
				var errorMsg = new Array();
				if(params.imgExtsErr && params.imgExtsErr == "true"){
					errorMsg.push("图片必须是"+params.imgExts+"格式");
				}
				if(params.imgSizeErr && params.imgSizeErr == "true"){
					errorMsg.push("图片大小不超过"+params.imgSize+"KB");
				}
				
				if(errorMsg.length > 0){
					alert(errorMsg.join("\n\r"));
				}  
 			});
		</script> 	
	</head>
	<body style="margin:0;padding:0;">
		<form id="uploadImageForm" action="<%=Config.getString("img.uploadImage") %>" enctype="multipart/form-data" method="post">
			<input type="file" name="file" id="file" class="file" />
			<input type="hidden" name="imgSrcTag" id="imgSrcTag" />
			<input type="hidden" name="imgType" id="imgType" />
			<input type="hidden" name="imgName" id="imgName" />
			<input type="hidden" name="multi" id="multi" />
			<input type="hidden" name="maxNum" id="maxNum" />
			<input type="hidden" name="tmpSaveId" id="tmpSaveId" />
			<input type="hidden" name="imgIdTag" id="imgIdTag" />
			<input type="hidden" name="returnUrl" id="returnUrl" value="<%=WebUtil.getBasePath(request) + "/page/inc/upload_image.jsp"%>"/>
		</form>
	</body>
</html>