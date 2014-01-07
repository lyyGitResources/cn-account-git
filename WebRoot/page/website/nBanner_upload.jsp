<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%
	int nImgType = Image.BANNER;
	String nImgSrcTag = "nBannerImgSrc";
	String nImgPathTag = "nbannerPath";
	String nImgIdTag = "nBannerImgId";//为防止image_select.jsp的js错误，在本模块中暂不用
%>
<script type="text/javascript">
		function initSingleImgUpload2() {
			var singleImgInstance = new HisupplierUpload({
				//file_size_limit : "300",
				file_types : "*.jpg;*.jpeg;*.gif",
				button_placeholder_id : "nSingleImgButtonId",
				flash_url : "/img/swf/swfupload.swf",
				custom_settings : {
					imgType : <%=imgType %>,
					imgExts : "jpg,jpeg,gif",//要检验的文件格式
					imgSize : "300",//要检验的文件大小
					comId : <s:property value="loginUser.comId"/>,
					processData : processData2
				}
			});
		 };
	     
		function processData2(file, jsonDataArray) {//在用swfupload上传文件后的接收数据函数
			if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
				if(jsonDataArray[0].errorTip){
					alert(jsonDataArray[0].errorTip);
				}else {
					alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
				}
			}
			$("#<%=nImgSrcTag %>").attr("src", "<%=Config.getString("img.link") %>" + jsonDataArray[0].imgPath);
			$("#<%=nImgPathTag %>").val("imgPath:" + jsonDataArray[0].imgPath + ";imgType:" + jsonDataArray[0].imgType + ";imgName:" + jsonDataArray[0].imgName);
		}
	$(document).ready(function () {	
		initSingleImgUpload2();
		$("#selectMulti").click(function(){
			$("#selectImgDialog").load("/image/image_select.do?imgType=<%=nImgType %>&imgSrcTag=<%=nImgSrcTag %>&imgPathTag=<%=nImgPathTag %>&imgIdTag=<%=nImgIdTag %>");
			$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			$("#selectImgDialog").dialog('open');
		});
		
		$("#nremoveInc").click(function(){
			$("#<%=nImgSrcTag %>").attr("src", "/img/tmp-page.jpg");
			$("#<%=nImgPathTag %>").val("/img/tmp-page.jpg");
		});
	});
</script>
<div class="text">
	1.图片尺寸规格为：<span id="nbannerWH"><s:property value="result.insideBannerWidth"/>(宽) × <s:property value="result.insideBannerHeight"/>(高)</span>像素。
	<br />
	2.图片类型必须为：JPG(JPEG)或GIF，大小不超过300K。
</div>
<div class="buttonBox">
		<s:if test="result.isImgFull">
			<div style="display:none;float:left;padding-right:10px;*padding-right:5px;"><span id="nSingleImgButtonId"></span></div>
				<div>
					<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
					<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
				</div>
		</s:if>
		<s:else>
			<div style="float: left;padding-right: 3px;"><span id="nSingleImgButtonId"></span></div>
		</s:else>
		<input style="float:left;" id="selectMulti" type="button" value="从图库选取" />
		<input style="float:left;" id="nremoveInc" type="button" value="移除" />
</div>

