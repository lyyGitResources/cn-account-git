<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ page import="com.hisupplier.commons.util.WebUtil" %>
<script type="text/javascript">
	var index = 0;
	function initManyImgUpload() {
		var manyImgInstance = new HisupplierUpload({
			file_types : "*.jpg;*.jpeg;*.gif",
			upload_complete_handler : uploadComplete,
			button_placeholder_id : "manyImgButtonId",
			flash_url : "/img/swf/swfupload.swf",
			custom_settings : {
				uploadStartValid : true,
				imgType : <%=imgType %>,
				imgExts : "jpg,jpeg,gif",//要检验的文件格式
				imgSize : "300",//要检验的文件大小
				comId : <s:property value="loginUser.comId"/>,
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
		return maxNum - index;//maxNum为design页面定义的全局变量
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

		var tmpValue = "imgPath:" + jsonDataArray[0].imgPath + ";imgType:" + jsonDataArray[0].imgType + ";imgName:" + jsonDataArray[0].imgName;
		index = parseInt(index,10);
		if($("[name='_multiDiv']").size() < maxNum){
				buffer = getImgDiv(jsonDataArray[0].imgPath,'<%=imgPathTag %>',(index+1),tmpValue);
				if($("[name='_multiDiv']").size() > 0){
					$("[name='_multiDiv']:last").after(buffer);
				}else{
					$("#_imageMulti").after(buffer);
					$("#<%=imgSrcTag %>").attr("src", "<%=Config.getString("img.link") %>" + jsonDataArray[0].imgPath);
					$("#<%=imgPathTag %>").val("imgPath:" + jsonDataArray[0].imgPath + ";imgType:" + jsonDataArray[0].imgType + ";imgName:" + jsonDataArray[0].imgName);
				}
				if(maxNum > 1){
					$("#bannerMulti").show();
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
	function getImgDiv(imgPath, imgPathTag, index, tmpValue, imgIds){
		if(imgPath.length == 0){
			return "";
		}
		var imgSrc = "<%=Config.getString("img.link") %>" + imgPath;
		
			var sindex = imgSrc.indexOf("imgPath:");
			if(sindex != -1) {
				pathIndex = imgSrc.indexOf("imgPath") + 8 ;
				var end = imgSrc.indexOf(";");
				imgSrc = "<%=Config.getString("img.link") %>" + imgSrc.substring(pathIndex,end);
			}
		
		if(!tmpValue){
			tmpValue = imgPath;
		}
		var buffer = "";
		buffer += "<center id='_multiDiv"+ index +"' name='_multiDiv' style='float:left;'>";
		buffer += "<div class='Img' style='margin-top:2px; height:88px;'>";
		buffer += "<img src='" + imgSrc + "' onerror=\"$(this).attr('src', '<%=Config.getString("img.default") %>')\" style='border:none;'/>";
		buffer += "<a href='javascript:multiRemove(" + index + ")' name='multiDelete'>删除</a>";
		buffer += "</div>";
		buffer += "<input type='hidden' name='" + imgPathTag + "' value='" + tmpValue + "' />";
		if (imgIds > 0){
			buffer += "<input type='hidden' name='imgIdsTag' value='" + imgIds + "' />";
		} 
		buffer += "</center>";
		return buffer;
	}
	function updateBannerSet(src) {
		var bannerSet = $("#bannerSet").val();
		if (bannerSet == 1) {
			$("#<%=imgSrcTag %>").attr("src", src ? src : "/img/tmp.jpg");
		}else {
			$("#<%=imgSrcTag %>").attr("src", src ? src : "/img/tmp-home.jpg");
		}
	}

	function multiRemove(index){
		var $removeDiv = $("#_multiDiv" + index).remove();
		if ($("[name='_multiDiv']").size() == 0){
			updateBannerSet();
			$("#bannerMulti").hide();
		} else if ($removeDiv.find("img").attr("src").replace(/[\s|　]+/g, "") == $("#<%=imgSrcTag %>").attr("src").replace(/[\s|　]+/g, "")) {
			//当_multiDiv内与bannerSet相同的图片被删除则替换为_multiDiv中index最小的图片
			updateBannerSet($("[name='_multiDiv']").find("img").attr("src"))
		}
	}

	$(document).ready(function () {	
		initManyImgUpload();
		$("#selectImgDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 600,
			width: 780,
			modal: true,
			close: function(){
				if($("[name='_multiDiv']").size() > 0 && $("#<%=imgSrcTag %>").attr("src").indexOf("<%=Config.getString("img.link") %>") == -1){
					$("#<%=imgSrcTag %>").attr("src", $("#_imageMulti").next().find("img").attr("src"));
					if(maxNum > 1){
						$("#bannerMulti").show();
					}
				}
				$("#selectImgDialog").empty();
			}		
		});
		$("#selectInc").click(function(){
			$("#selectImgDialog").load("/image/image_select.do?imgType=<%=imgType %>&imgSrcTag=&imgPathTag=<%=imgPathTag %>&imgIdTag=" + maxNum);
			$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			$("#selectImgDialog").dialog('open');
		});
		// 全部删除
		$("#_removeAll").click(function(){
			$("[name='_multiDiv']").each(function(){
				$(this).remove();
			});
			updateBannerSet();
			$("#bannerMulti").hide();
		});		
		var images = "<s:property value='result.webSite.bannerPath'/>";
		if(images != '/img/tmp-home.jpg' && images != '/img/tmp.jpg'){
			var buffer = "";
			var imageArray = images.split(",");
			// 空值个数
			var j = 0;
			for(var i = 0; i < imageArray.length; i++){
				if(imageArray[i].length == 0){
					j++;
				}else{
					buffer  += getImgDiv(imageArray[i], "<%=imgPathTag %>", i - j, '', 0);	
				}
			}
			$("#_imageMulti").after(buffer);
		}
		//如果该模板只能上传一个banner图片就隐藏多选图片显示区域
		if(maxNum == 1){
			$("#bannerMulti").hide();
		}
	});
</script>
<div class="text">
	1.图片尺寸规格为：<span id="bannerWH"><s:property value="result.bannerWidth"/>(宽) × <s:property value="result.bannerHeight"/>(高)</span>像素。
	<br />
	2.图片类型必须为：JPG(JPEG)或GIF，大小不超过300K。
	<br />
	注意：不同展示方案，形象图片尺寸不同，上传的形象图片可能仅适用于当前模板。
</div>
<div class="buttonBox" style="margin-bottom: 12px; line-height: 20px; color: #4E6591;">
	<s:if test="result.isImgFull">
		<div style="display:none;float:left;padding-right:10px;*padding-right:5px;"><span id="manyImgButtonId"></span></div>
			<div style="display:block;float:left;padding-right:10px;*padding-right:5px;">
				<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
				<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
			</div>
	</s:if>
	<s:else>
		<div style="display:block;float:left;padding-right:10px;*padding-right:5px;"><span id="manyImgButtonId"></span></div>
	</s:else>
	<input type="button" value="从图库选取" id="selectInc" style="float:left; margin-right:3px; margin-left:-7px;*margin-left:0 !important;*margin-left:0;" >
	<input type="button" value="移除" id="_removeAll" style="float:left; margin-right:10px;"><div class="numTip"></div>
</div>
<div id="bannerMulti">
	<div class="bannerbox1"></div>
	<div class="bannerbox2"> 
		<div id="_imageMulti"></div>
	</div>
	<div class="bannerbox3"></div>
</div>
<div id="selectImgDialog"></div>