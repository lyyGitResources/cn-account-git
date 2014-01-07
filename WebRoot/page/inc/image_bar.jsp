<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.Global"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
		function initSingleImgUpload() {
			var singleImgInstance = new HisupplierUpload({
				//file_size_limit : "<s:property value='#imgSize' />",
				file_types : "<s:property value='#imgExts' />",
				button_placeholder_id : "singleImgButtonId",
				button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,
				flash_url : "/img/swf/swfupload.swf",
				debug:<%=Boolean.getBoolean(Global.DEBUG) %>,
				custom_settings : {
					imgType : <%=imgType %>,
					imgExts : "<s:property value='#imgExts' />",//要检验的文件格式
					imgSize : "<s:property value='#imgSize' />",//要检验的文件大小
					comId : <s:property value="loginUser.comId"/>,
					processData : processData
				}
			});
	     };
		function processData(file, jsonDataArray) {//在用swfupload上传文件后的接收数据函数
			if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
				if(jsonDataArray[0].errorTip){
					alert(jsonDataArray[0].errorTip);
				}else {
					alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
				}
			}
			$("#<%=imgSrcTag %>").attr("src", "<%=Config.getString("img.link") %>" + jsonDataArray[0].imgPath);
			$("#<%=imgPathTag %>").val("imgPath:" + jsonDataArray[0].imgPath + ";imgType:" + jsonDataArray[0].imgType + ";imgName:" + jsonDataArray[0].imgName);
			if($("#watermarkPart").attr("id")){
				$("#watermarkPart").show();
			}
		}
	$(document).ready(function () {	
		initSingleImgUpload();//初始化单个图片上传控件
		$("#selectInc").click(function(){
			$("#selectImgDialog").load("/image/image_select.do?imgType=<%=imgType %>&imgSrcTag=<%=imgSrcTag %>&imgPathTag=<%=imgPathTag %>&imgIdTag=<%=imgIdTag %>");
			$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			$("#selectImgDialog").dialog('option', 'height', '700');
			$("#selectImgDialog").dialog('open');
		});
		
		$("#selectFckInc").click(function(){
			$("#selectImgDialog").load("/image/image_select.do?imgType=<%=imgType %>&imgSrcTag=txtUrl&imgPathTag=txtUrl&imgIdTag=txtUrl");
			$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			$("#selectImgDialog").dialog('option', 'height', '740');
			$("#selectImgDialog").dialog('open');
		});
				
		$("#removeInc").click(function(){
			$("#<%=imgSrcTag %>").attr("src", "<%=Config.getString( "img.default" )%>");
			$("#<%=imgPathTag %>").val("");
			$("#<%=imgIdTag %>").val(0);
			if($("#watermarkPart").attr("id")){
				$("#watermarkPart").hide();
			}
		});
	});
</script>
<div style="margin-bottom:5px;">
	<div class="imgBox75">
		<img src="<s:property value='#imgSrc' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)" id="<%=imgSrcTag %>" />
	</div>
	<div class="fieldTips" style="float:left;margin-left:85px; margin-top:-80px;*margin-left:85px !important; *margin-left:42px;">
		<b><s:text name="image.format" /></b>
		<br />
		1. <s:text name="image.format.type"><s:param><%=Config.getString("img." + imgType + ".type")%></s:param></s:text>
		<br />
		2. <s:text name="image.format.size"><s:param><%=Config.getString("img." + imgType + ".size")%></s:param></s:text>
		<br />
		3. <s:text name="image.format.scale"><s:param><%=Config.getString("img." + imgType + ".scale")%></s:param></s:text>
	</div>
</div>
<div style="clear:left;margin-bottom:5px;">
	<s:if test="result.isImgFull">
		<div style="display:none;float:left;padding-right:2px;"><span id="singleImgButtonId"></span></div>
			<div style="display:block;float:left;padding-right:2px;height:21px">
				<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
				<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
			</div>
	</s:if>
	<s:else>
		<div style="display:block;float:left;padding-right:2px;height:21px"><span id="singleImgButtonId"></span></div>
	</s:else>
	<input type="button" value="<s:text name='button.selectImage' />" id="selectInc" />
	<input type="button" value="<s:text name='button.remove' />" id="removeInc" />
	<input type="hidden" name="<%=imgPathTag %>" id="<%=imgPathTag %>" value="<s:property value='#imgPath' />"/> 
	<input type="hidden" name="<%=imgIdTag %>" id="<%=imgIdTag %>" value="<s:property value='#imgId' />"/>
	<input type="button" style="display:none;" id="selectFckInc">
</div>