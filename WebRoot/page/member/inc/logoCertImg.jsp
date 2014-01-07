<script type="text/javascript">
	function initSingleFileUpload() {
			var singleInstance = new HisupplierUpload({
				upload_url: "<%=Config.getString("img.base")%>/file/swfupload_file",
				//file_size_limit : "500",	// 500K
				file_types : "*.jpg;*.gif;*.jpeg",
				button_placeholder_id : "singleFileButtonId",
				flash_url : "/img/swf/swfupload.swf",
				custom_settings : {
					fileType : 0,
					processData : processSingleFileData
				}
			});
	     };
    function processSingleFileData(file,jsonDataArray){
		if(jsonDataArray[0].msg == "fail"){
			if(jsonDataArray[0].errorTip){
				alert(jsonDataArray[0].errorTip);
			}else {
				alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
			}
		}			
		$("#logoCertImgSrc").attr("src", "<%=Config.getString("img.fileLink") %>" +  jsonDataArray[0].filePath);
		$("#logoCertImg").val("filePath:" + jsonDataArray[0].filePath + ";fileType:" + jsonDataArray[0].fileType + ";fileName:" + jsonDataArray[0].fileName);
    } 
    $(function(){
    	initSingleFileUpload();
    });
</script>
<div class="imgBox75" style="margin-bottom:10px;">
	<img id="logoCertImgSrc" src="<s:property value='result.company.logoCertImgSrc'/>" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
</div>
<div class="fieldTips" style="float:left;margin-left:85px; margin-top:-80px;*margin-left:85px !important; *margin-left:42px;">
	<b><s:text name="image.format" /></b>
	<br />
	1. <s:text name="image.format.type"><s:param><%=Config.getString("img." + imgType + ".type")%></s:param></s:text>
	<br />
	2. <s:text name="image.format.size"><s:param>500</s:param></s:text>
	<br />
	3. <s:text name="image.format.scale"><s:param>500X500</s:param></s:text>
</div>
<input type="hidden" name="logoCertImg" id="logoCertImg" value="<s:property value='result.company.logoCertImg'/>" />
	<s:if test="result.isImgFull">
		<div style="display:none;float:left;padding-right:2px;"><span id="singleFileButtonId"></span></div>
		<div style="display:block;float:left;padding-right:2px;">
			<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
			<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
		</div>
	</s:if>
	<s:else>
		<div style="display:block;float:left;padding-right:2px;"><span id="singleFileButtonId"></span></div>
	</s:else>
<input type="button" value="<s:text name='button.remove' />" id="_buttonRemove" />
<div class="fieldTips" style="float:none;">
	为配合工商部门的检查工作,即日起，若上传的Logo带R(即通过商标注册的),请您上传商标注册证书,若未提供证书,我司将自动删除已上传的Logo,不再另行通知。
</div>