<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
		var maxNum = <%=maxNum %>;
		var index = 0;
		var multiImgInstance;
		var limitSize = 0;
		<%if (imgType == 9) { //只有上传营业执照和有效凭证时控制上传大小500KB%>
			limitSize = "<s:property value='#patentImgSize' />";
		<%} else { %> 
			limitSize = "<s:property value='#imgSize' />";
		<%}%>
		
		function initMultiImgUpload() {
				multiImgInstance = new HisupplierUpload({
				file_types : "<s:property value='#imgExts' />",
				upload_complete_handler : uploadComplete,//多图片自动上传必须开启此函数
				button_placeholder_id : "multiImgButtonId",
				file_size_limit : limitSize,
				flash_url : "/img/swf/swfupload.swf",
				custom_settings : {
					uploadStartValid : true,
					imgType : <%=imgType %>,
					imgExts : "<s:property value='#imgExts' />",//要检验的文件格式
					imgSize : limitSize,//要检验的文件大小
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

			var tmpValue = "imgPath:" + jsonDataArray[0].imgPath + ";imgType:" + jsonDataArray[0].imgType + ";imgName:" + jsonDataArray[0].imgName;
			index = parseInt(index,10);
			if($("[name='_multiDiv']").size() < maxNum){
				buffer = getImgDiv(jsonDataArray[0].imgPath,'<%=imgPathTag %>',(index+1),tmpValue);
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
		buffer += "<center id='_multiDiv"+ index +"' name='_multiDiv' style='float:left;margin-right:10px;'>";
		buffer += "<div class='imgBox75' style='margin-bottom:5px;'>";
		buffer += "<img src='" + imgSrc + "' onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(75) %>')\" onload='Util.setImgWH(this, 75, 75)' />";
		buffer += "</div>";
		buffer += "<a href='javascript:multiRemove(" + index + ")' name='multiDelete'>删除</a>";
		buffer += "<input type='hidden' name='" + imgPathTag + "' value='" + tmpValue + "' />";
		if (imgIds > 0){
			buffer += "<input type='hidden' name='imgIdsTag' value='" + imgIds + "' />";
		} 
		buffer += "</center>";
		return buffer;
	}

	function multiRemove(index){
		$("#_multiDiv" + index).remove();
		if($("#watermarkPart").attr("id") && $("[name='_multiDiv']").length == 0){
			$("#watermarkPart").hide();
		}
	}

	$(function () {	
		$("#selectMulti").click(function(){
			// 使用imgIdTag来保存能添加的最大数量
			$("#selectImgDialog").load("/image/image_select.do?imgType=<%=imgType %>&imgSrcTag=&imgPathTag=<%=imgPathTag %>&imgIdTag=<%=maxNum %>");
			$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
			$("#selectImgDialog").dialog('option', 'height', '670');
			$("#selectImgDialog").dialog('open');
		});
		
		// 全部删除
		$("#_multiRemoveAll").click(function(){
			$("[name='_multiDiv']").each(function(){
				$(this).remove();
			});
			if($("#watermarkPart").attr("id")){
				$("#watermarkPart").hide();
			}
		});

		if($.browser.msie && $.browser.version == 8.0){
			$("#_multiIframe").css("width", "90px");
		}
		
		var images = "<s:property value='#images' />";
		var imageIds_ ="<s:property value='#imagIds' />";
		if(images != ""){
			var buffer = "";
			var imageArray = images.split(",");
			var imageIdArray = imageIds_.split(",");
			// 空值个数
			var j = 0;
			for(var i = 0; i < imageArray.length; i++){
				if(imageArray[i].length == 0){
					j++;
				}else{
					buffer  += getImgDiv(imageArray[i], "<%=imgPathTag %>", i - j, '', imageIdArray[i]);	
				}
			}
			$("#_imageMulti").after(buffer);
		}
		
		if($("#proAddImg").size() > 0){
			if($("[name='_multiDiv']").length > 0){
				$("#proAddImg").attr("checked", "checked");
				if($("iframe:eq(1)").attr("src") == ""){
					$("iframe:eq(1)").attr("src", multiSrc);
				}
				$("#proAddImgDiv").show();
			}else{
				// 刷新时，移除选中
				$("#proAddImg").removeAttr("checked");
			}
		}
	});
</script>
<div style="border:1px solid #CBDDF1;height:150px;padding:5px 5px 10px;"> 
	<div id="_imageMulti">
		<s:if test="result.isImgFull">
			<div style="display:none;float:left;padding-right:2px;"><span id="multiImgButtonId"></span></div>
			<div style="display:block;float:left;padding-right:2px;">
				<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
				<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
			</div>
		</s:if>
		<s:else>
			<div style="display:block;float:left;padding-right:2px;"><span id="multiImgButtonId"></span></div>
		</s:else>
		<script type="text/javascript">
			initMultiImgUpload();
		</script>
		<s:if test="licenseMulti > 0">
			<input id="selectMulti" type="button" value="从有效凭证中选取" />
		</s:if>
		<s:else>
			<input id="selectMulti" type="button" value="从图库选取" />
		</s:else>
		<input id="_multiRemoveAll" type="button" value="全部删除" />
		<div id="div_showtip" style="font-size:12px; display:block; margin:10px 0 0 0;">
			<s:if test="licenseMulti > 0">
				文件格式支持jpg、jpeg、gif -  最大500KB<font color="red">&nbsp;&nbsp;&nbsp;(最多可上传3张)</font>
			</s:if>
			<s:else>
				不超过<%=Config.getString("img." + imgType + ".size")%>K，<%=Config.getString("img." + imgType + ".type")%>格式，最佳尺寸<%=Config.getString("img." + imgType + ".scale")%>
			</s:else>
		</div>
	</div>
</div>
