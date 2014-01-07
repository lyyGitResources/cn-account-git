<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/sitemesh-decorator" prefix="decorator"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="image.listTitle" /></title>
		<link type="text/css" rel="stylesheet" href="/css/imagelist.css" />
		<script type="text/javascript" src="/js/colorPalette/functions.js"></script>
		<script type="text/javascript">
		var upload;
		window.onload = function() {
			
	     };

		    function processSuccess(file,jsonDataArray){
				if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
					if(jsonDataArray[0].errorTip){
						alert(decodeURIComponent(jsonDataArray[0].errorTip));
					}else {
						alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
					}
				}else{
			    	var params = {imgType:$("#imgType").val(),random:Math.random()};
			    	ListTable.reload(params);
				}
		    } 
			function setWatermark(el){
				upload.customSettings.watermark = el.checked;
				toggle_inner_watermark(el.checked);
			}
			

			function open_watermark(el, $el) {
				el.checked = true;
				$el.show();
			}
			function close_watermark(el, $el) {
				el.checked = false;
				$el.hide();
			}
			function toggle_inner_watermark(isShow) {
				$("#inner_watermark").css("display", isShow ? "block" : "none");
			}
			
			$(document).ready(function () {
				upload = new HisupplierUpload({
					//file_size_limit : "100",	// 100K
					file_types : "*.jpg;*.jpeg;*.gif",
					upload_complete_handler : uploadComplete,//多图片自动上传必须开启此函数
					button_placeholder_id : "spanButtonPlaceholder1",
					flash_url : "/img/swf/swfupload.swf",
					custom_settings : {
						onlyStoreToDisk : false,//存储到数据库中
						watermark : $("#watermark").attr("checked"),
						watermarkFlag : true,//生成水印是否取customSettings里的watermark值
						//watermarkText : "<s:property value='result.image.watermarkText' />",
						watermarkText : $("#watermarkText").val(),
						//watermarkTextColor : "<s:property value='result.image.watermarkTextColor' />",
						watermarkTextColor: $("#watermarkTextColor").val(),
						//textFontSize : "<s:property value='result.image.textFontSize' />",
						textFontSize:$("#textFontSize").val(),
						processData : processSuccess
					}
				});
				
				
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryBy: "imgName",
							queryText: $("#queryText").val(),
							pageNo:1
						}, true);
					}
				});
				
 				$("#imgDetailDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 350,
					width: 525,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}		
				});
				var ck_watermark = document.getElementById('watermark');
				if (ck_watermark) toggle_inner_watermark(ck_watermark.checked);
 				
 				$("#tabMenu li").click(function() {
 					var imgType = $(this).attr("imgType");
					var $div_watermark = $("#div_watermark");
					if (imgType == 3) {
						open_watermark(ck_watermark, $div_watermark);
						toggle_inner_watermark(true);
					} else {
						close_watermark(ck_watermark, $div_watermark);
					}
					//console.info(ck_watermark.checked);
				});
 				$("#colorSrc").attr("src","/js/colorPalette/rectNoColor.gif").click(function(){getColorPalette('colorSrc','watermarkTextColor');});
			});
			
		</script>
	</head>
	<body>
		<div id="tabMenu" class="tabMenu">
			<ul>
				<li imgType="3" <s:if test="imgType == -1">class="current"</s:if>><span onclick="tabMenu(1,{imgType:-1,random:Math.random()})"><s:text name="image.allImage" />[<s:property value='result.numMap.sum' />]</span></li>
				<li imgType="1" <s:if test="imgType == 1">class="current"</s:if>><span onclick="tabMenu(2,{imgType:1,random:Math.random()})"><s:text name="image.logo" />[<s:property value='result.numMap.imgType1' />]</span></li>
				<li imgType="2" <s:if test="imgType == 2">class="current"</s:if>><span onclick="tabMenu(3,{imgType:2,random:Math.random()})"><s:text name="image.face" />[<s:property value='result.numMap.imgType2' />]</span></li>
				<li imgType="3" <s:if test="imgType == 3">class="current"</s:if>><span onclick="tabMenu(4,{imgType:3,random:Math.random()})"><s:text name="image.product" />[<s:property value='result.numMap.imgType3' />]</span></li>
				<li imgType="4" <s:if test="imgType == 4">class="current"</s:if>><span onclick="tabMenu(5,{imgType:4,random:Math.random()})"><s:text name="image.menu" />[<s:property value='result.numMap.imgType4' />]</span></li>
				<li imgType="5" <s:if test="imgType == 5">class="current"</s:if>><span onclick="tabMenu(6,{imgType:5,random:Math.random()})"><s:text name="image.banner" />[<s:property value='result.numMap.imgType5' />]</span></li>
				<li imgType="6" <s:if test="imgType == 6">class="current"</s:if>><span onclick="tabMenu(7,{imgType:6,random:Math.random()})"><s:text name="image.head" />[<s:property value='result.numMap.imgType6' />]</span></li>
			</ul>
		</div>
		
		<form id="searchForm" class="searchForm"  method="post">
		<input type="hidden" id="comId" name="comId" value="<s:property value="loginUser.comId"/>"/>
		<input type="hidden" id="imgType" name="imgType" value="-1"/>
		<!--	生成默认水印	-->
		<input type="hidden" id="memberId" name="memberId" value="<s:property value="loginUser.memberId"/>"/>
			<table style="width:725px;">
				<tbody>
					<tr>
						<td  style="text-align: left;">
							<s:if test="result.isImgFull">
								<div style="display:none;float:left;padding-right:2px;"><span id="spanButtonPlaceholder1"></span></div>
								<div style="display:block;float:left;padding-right:2px;">
									<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" />
									<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
								</div>
							</s:if>
							<s:else>
								<div style="display:block;float:left;padding-right:2px;">
									<span id="spanButtonPlaceholder1"></span>
								</div>
								<s:if test="imgType == 3 || imgType == -1">
								<div id="div_watermark" style="float:left; padding: 0 0 0 5px">
									<input style="margin:5px 0 0 0;" checked="checked" type="checkbox" id="watermark" name="image.watermark" onclick="setWatermark(this);">&nbsp;&nbsp;是否给图片加水印
									<div id="inner_watermark">
										<input type="text" id="watermarkText" name="image.watermarkText" value="${result.image.watermarkTextReal}" style="width:200px;" />
										<img id="colorSrc" style="width:20px;height:20px;cursor: pointer;vertical-align: bottom">
										<input type="text" id="watermarkTextColor" name="image.watermarkTextColor" value="${result.image.watermarkTextColorReal}" maxlength="7" style="width: 60px; background-color:${result.image.watermarkTextColorReal};">
										字号
										<select id="textFontSize" name="image.textFontSize">
											<option value="0">自动</option>
											<s:iterator value="result.image.textFontSizeList" id="size">
												<option value="${size}" <s:if test="#size == result.image.textFontSize">selected</s:if>>${size }</option>
											</s:iterator>
										</select>
										<div style="margin-top: 3px; color: #999999"><iframe width="260" height="165" id="colorPalette" src="/js/colorPalette/color.html" style="visibility: hidden; position: absolute; border: 1px gray solid" frameborder="0" scrolling="no"></iframe></div>
									</div>
								</div>
								</s:if>
							</s:else>
						</td>
						<td style="text-align: right;"><input value="<s:text name='image.queryText' />" oriValue="<s:text name='image.queryText' />" name="imgName" id="queryText" type="text">
						<input class="searchButton" value="<s:text name="button.search" />" type="submit"></td>
					</tr>
				</tbody>
			</table>
		</form>
		<div class="pageTips">
			<div style="float: left; width: 60%" >
				温馨提示：<br/>
				♦  双击图片名称，可进行图片名称修改<br/>
				♦  上传图片时，选中多张图片（按住shfit或者ctrl键，鼠标点击<br/>选择需要的图片）可实现批量上传
			</div>
			<div>图片格式<br/>
				1.	jpg、jpeg、gif格式<br/>
				2.	超过100K的图片会被压缩处理，且无动态效果<br/>
				3.	产品图片最佳尺寸500×500 <br/>
			</div>
		</div>
		<div id="listTable" style="padding:10px 0 0 0;">
			<s:include value="/page/misc/image_list_inc.jsp"/>
		</div>
		<div id="formDialog"></div>
		<div id="imgDetailDialog"></div>
	</body>
</html>
