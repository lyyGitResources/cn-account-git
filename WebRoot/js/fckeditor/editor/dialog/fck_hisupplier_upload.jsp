<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.hisupplier.commons.util.WebUtil"%>
<%@page import="com.hisupplier.cn.account.entity.Image"%>
<%@page import="com.hisupplier.cn.account.basic.LoginUser"%>
<%@page import="com.hisupplier.cn.account.basic.CASClient"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@taglib uri="/struts-tags" prefix="s" %>
	<%
		LoginUser loginUser = (LoginUser) CASClient.getInstance().getUser(request);
		String memberId= loginUser == null ? "" : loginUser.getMemberId();
		Image image = new Image();
		image.getWatermark(request,memberId);
	%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>海商网中文站用户后台管理系统</title>
		<style type="text/css">
				.Hand
				{
					cursor: pointer ;
					cursor: hand ;
				}
				.Sample { font-size: 24px; }
		</style>
		<script type="text/javascript">
		var HI_DOMAIN = "<%=Config.getString("sys.domain")%>";
		</script>
		<script src="common/fck_dialog_common.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/handlers.js"></script>
		<script type="text/javascript" src="/js/colorPalette/functions.js"></script>
		<script type="text/javascript">
			var fckUploadInstance;
			var oEditor = window.parent.InnerDialogLoaded() ;
			function setDefaults(){
				oEditor.FCKLanguageManager.TranslatePage(document) ;
				window.parent.SetAutoSize( true ) ;
			}
			function processFCKData(file, jsonDataArray) {//在用swfupload上传文件后的接收数据函数
				if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
					alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
					return ;
				}
				oEditor.FCKUndo.SaveUndoStep() ;
				oEditor.FCK.InsertHtml("<img src='<%=Config.getString("img.link")%>" + jsonDataArray[0].imgPath + "' align='absmiddle'>") ;
				window.parent.Cancel() ;
			}
			function setWatermark(el){
				fckUploadInstance.customSettings.watermark = el.checked;
				if (el.checked) {
					$("#div_watermark").show();
				} else {
					$("#div_watermark").hide();
				}
			}
			$(function() {
				$("#colorSrc").attr("src","/js/colorPalette/rectNoColor.gif").click(function(){
					getColorPalette2("colorSrc", "watermarkTextColor");
				});	
				getColorPalette2("colorSrc", "watermarkTextColor");
			});
		</script>
	</head>
	<body onload="setDefaults()">
	<div style="min-height: 310px">
		<div style="float: left">
			图片格式：<br />1. jpg.jpeg.gif格式<br />2. 超过500K的图片会被压缩处理 <br />3. 最佳尺寸 650 X 650 <br />
			<input type="checkbox" id="watermark" name="image.watermark" checked="checked" onclick="setWatermark(this);" />是否给图片加水印
		</div>
		<div><span id="singleFCKImgButtonId"></span></div>
		<div id="div_watermark">
			<input type="text" id="watermarkText" name="image.watermarkText" value="<%=image.getWatermarkTextReal()%>" style="width:170px;" />
			<br /><img id="colorSrc" style="width:20px;height:20px;cursor: pointer;vertical-align: bottom">
			<input type="text" id="watermarkTextColor" name="image.watermarkTextColor" value="<%=image.getWatermarkTextColorReal() %>" maxlength="7" style="width: 60px; background-color:<%=image.getWatermarkTextColorReal() %>;">
			字号
			<select id="textFontSize" name="image.textFontSize">
				<option value="0">自动</option>
				<%for(int size : image.getTextFontSizeList()){%>
					<option value="<%=size %>"><%=size %></option>
				<%} %>
			</select>
			<div>
			<iframe width="240px" height="165" id="colorPalette" src="/js/colorPalette/color.html" style="visibility:hidden;position: absolute; border: 1px gray solid" frameborder="0" scrolling="no"></iframe>
			</div>
		</div>
		
	</div>
		<script type="text/javascript">
			fckUploadInstance = new HisupplierUpload({
				file_types : "*.jpg;*.gif;*.jpeg",
				button_placeholder_id : "singleFCKImgButtonId",
				button_image_url : "common/images/XPButtonUploadText_Rang_71x22.png",
				button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,
				flash_url : "/img/swf/swfupload.swf",
				custom_settings : {
					imgType : 7,
					imgExts : "*.jpg;*.gif;*.jpeg",
					isFCK : true,
					watermark : true,
					watermarkFlag : true,
					watermarkText : $("#watermarkText").val(),
					watermarkTextColor : $("#watermarkTextColor").val(),
					textFontSize : $("#textFontSize").val(),
					memberId : "<%=memberId%>",
					processData : processFCKData
				}
			});
		</script>
	</body>
</html>