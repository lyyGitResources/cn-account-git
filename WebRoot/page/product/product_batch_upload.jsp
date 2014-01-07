<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.WebUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>批量上传普通产品</title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="/js/colorPalette/functions.js"></script>
		<script type="text/javascript" src="/js/jquery.simplemodal.js"></script>
		<script type="text/javascript">
			var maxNum = 20;
			var index = 0;
			var multiImgInstance;
			var food_pact_modal;
			var food_pact = 0;
			var $foodPact_hidden;
			var $product_more_form;
			
			<s:if test="!result.company.foodPact">
			function similar_return(data) {
				if (data.indexOf("农业食品") == 0 && food_pact == 0) {
					food_pact_modal = $("#food_pack_div").modal({
						onClose: function() {
							if (food_pact == 0) {
								$("#step_3, #upload_form").hide();
							}
							this.close();
						}
					});
				} else {
					$("#step_3, #upload_form").show();
				}
			}
			</s:if>
			
			function uploadStartValid(){//在用swfupload上传文件开始时的检验函数
				if($("[name='_multiDiv']").size() > 0){
					index = $("[name='_multiDiv']").size();
				}
				index = parseInt(index,10);
				return maxNum - index;
			}
			function processSuccess(file, jsonDataArray) {//在用swfupload上传文件后的接收数据函数
				if(jsonDataArray[0].msg && jsonDataArray[0].msg == "fail"){
					if(jsonDataArray[0].errorTip){
						alert(decodeURIComponent(jsonDataArray[0].errorTip));
					}else {
						alert("数据存储出错，请确定是否选择了合适的文件格式和文件大小");
					}
				}else{
					$("#imgBox").show();
					if($("[name='_multiDiv']").size() > 0){
						index = $("[name='_multiDiv']:last").attr("id").replace("_multiDiv","");
					}
					index = parseInt(index,10);
					if($("[name='_multiDiv']").size() < maxNum){
						buffer = getImgDiv(jsonDataArray[0].imgId,jsonDataArray[0].imgPath,'proImgArray',(index+1),'',decodeURIComponent(jsonDataArray[0].imgName));
						if($("[name='_multiDiv']").size() > 0){
							$("[name='_multiDiv']:last").after(buffer);
						}else{
							$("#_imageMulti").after(buffer);
						}
					}else{
						alert("不能再添加了！");
					}
				}
			}
			/**
			 * 生成单个图片模块
			 * @param imgPath 
			 * @param imgPathTag 图片路径保存位置
			 * @param index 生成的图片模块序号，从0开始
			 * @param tmpValue 是否临时图片，即未保存到数据库
			 */
			function getImgDiv(imgId, imgPath, imgPathTag, index, tmpValue, imgName){
				if(imgPath.length == 0){
					return "";
				}
				var imgSrc = "<%=Config.getString("img.link") %>" + imgPath;
				if(!tmpValue){
					tmpValue = imgPath;
				}
				var buffer = "";
				if(index == 0){
					buffer += "<div style='width:100%;float:left;'></div>";
				}
				buffer += "<center id='_multiDiv"+ index +"' name='_multiDiv' style='float:left;margin-right:10px;height:120px;'>";
				buffer += "<div class='imgBox75' style='margin-bottom:5px;'>";
				buffer += "<img src='" + imgSrc + "' onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(75) %>')\" onload='Util.setImgWH(this, 75, 75)' />";
				buffer += "</div>";
				buffer += "<a href='javascript:multiRemove(" + index + "," + imgId+ ")' name='multiDelete'>删除</a><br /><label title='"+imgName+"'>" + imgName.substring(0,6) + "</label>";
				if(imgName != undefined && imgName != ''){
					buffer += "<input type='hidden' name='" + imgPathTag + "' value='imgName:" +imgName + ";imgPath:" + tmpValue + "' />";
				}else{
					buffer += "<input type='hidden' name='" + imgPathTag + "' value='" + tmpValue + "' />";
				}
				buffer += "</center>";
				//添加图片ID到隐藏字段，供下次选择时不重复选择图片
				if(imgId > 0){
					$("#imgIdsStr").val(imgId +","+ $("#imgIdsStr").val());
				}
				return buffer;
			}
			function multiRemove(index,imgId){
				$("#_multiDiv" + index).remove();
				if(imgId > 0){
					var imgIdArray = window.parent.$("#imgIdsStr").val().split(",");
					var imgIdsStrTmp = "";
					for(var i=0;i<imgIdArray.length-1;i++){
						if(imgIdArray[i] == imgId){
							continue;
						}
						imgIdsStrTmp = imgIdsStrTmp + imgIdArray[i] + ",";
					}
					$("#imgIdsStr").val(imgIdsStrTmp);//移除一张图片时同时删掉隐藏字段，以便可以重新添加进来
				}
			}
			$(function () {
				function initMultiImgUpload() {
					multiImgInstance = new HisupplierUpload({
						file_types : "*.jpg;*.jpeg;*.gif",
						upload_complete_handler : uploadComplete,//多图片自动上传必须开启此函数
						button_placeholder_id : "multiImgButtonId",
						flash_url : "/img/swf/swfupload.swf",
						custom_settings : {
							onlyStoreToDisk : false,//存储到数据库中
							uploadStartValid : true,
							watermark : document.getElementById("watermark").checked,
							watermarkFlag : true,//生成水印是否取customSettings里的watermark值
							watermarkText : $("#watermarkText").val(),
							watermarkTextColor : $("#watermarkTextColor").val(),
							textFontSize : $("#textFontSize").val(),
							comId : "<s:property value="loginUser.comId"/>",
							imgType : 3,
							imgExts : "*.jpg;*.jpeg;*.gif",//要检验的文件格式
							comId : <s:property value="loginUser.comId"/>,
							uploadStartValid: uploadStartValid,
							processData : processSuccess
						}
					});
				};
				initMultiImgUpload();
				$("#colorSrc").attr("src","/js/colorPalette/rectNoColor.gif").click(function(){getColorPalette('colorSrc','watermarkTextColor');});
				
				$("#watermark").click(function() {
					multiImgInstance.customSettings.watermark = this.checked;
					if (this.checked) {
						$("#div_watermark").show();
					} else {
						$("#div_watermark").hide();
					}
				});
				
			 	$("#productDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 390,
					width: 460,
					modal: true,
					close: function(){
						$("#productDialog").empty();
					}
				});
 				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 770,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}
				});
				$("#selectMulti").click(function(){
					// 使用imgIdTag来保存能添加的最大数量
					var maxCount = 0 ;
					if($("[name='_multiDiv']").size() > 0){
						maxCount = $("[name='_multiDiv']").size();
					}
					maxCount = 20 - parseInt(maxCount,10);
					
					$("#selectImgDialog").load("/image/image_select.do?batch=true&imgType=3&imgSrcTag=&imgPathTag=proImgArray&imgIdTag="+maxCount);
					$("#selectImgDialog").dialog('option', 'title', '<s:text name="button.selectImage" />');
					$("#selectImgDialog").dialog('option', 'height', '670');
					$("#selectImgDialog").dialog('open');
				});
				
				// 全部删除
				$("#_multiRemoveAll").click(function(){
					$("[name='_multiDiv']").each(function(){
						$(this).remove();
					});
					$("#imgIdsStr").val("");//移除所有图片同时清除隐藏图片ID串
				});
				$("#catSimilarSelect").click(function(){
					$("#productDialog").load("/product/similar_category.do");
					$("#productDialog").dialog('option', 'title', '<s:text name="button.selectSimilar"/>');
					$("#productDialog").dialog('open');
				});
				
				$("#food_pack_div .sing").click(function() {
					$foodPact_hidden.val('true');
					food_pact = 1;
					food_pact_modal.close();
					$("#step_3, #upload_form").show();
				});
				
				$foodPact_hidden = $("#foodPact_hidden");
				$("#_catList").category({
					contId: "catName",
					contType: "text"
					<s:if test="!result.company.foodPact">
					,ext_click: function(root_id) {
						if (root_id == 1 && food_pact == 0) {
							food_pact_modal = $("#food_pack_div").modal({
								onClose: function() {
									if (food_pact == 0) {
										$foodPact_hidden.val('false');
										$("#step_3, #upload_form").hide();
									}
									this.close();
								}
							});
						} else {
							$("#step_3, #upload_form").show();
						}
					}
					</s:if>
				});
				$("#stepOne").toggle(function(){
					$("#showSetpOne").val(false);
					$('#buttonDiv').hide();
					$('#imgBox').hide();
					$("#stepOne").html("显示这一步");
				},function(){
					$("#showSetpOne").val(true);
					$('#buttonDiv').show();
					$('#imgBox').show();
					$("#stepOne").html("跳过这一步");
				});
				$("#stepOneTip").tooltip({
					title:'跳过这一步',
					detail:'选择跳过批量上传图片这步，可在其他产品信息上传成功后。对每个产品进行修改，添加图片操作。',
					toolTipwidth:150
				});
			});
			function checkForm(){
				if($('#catId').val() == '' || $('#catName').val() == ''){
					alert("请选择分类");
					return;
				}
				$('#downLoadExcelForm').submit();
			}
		</script>
		<style type="text/css">
		#stepOneTip img{position:relative;}
		</style>
	</head>
	<body>
		<div class="pageTips">
			提示：<br/>
			♦  填写电子表格时，图片名列表，可根据第一步显示的图片和图片名选取以保证正确匹配<br/>
			♦  单次批量上传最多20个产品<br/>
		</div>
		<div class="batch_product_box">
			<input type="hidden" id="imgIdsStr" value="" />
			<form id="downLoadExcelForm" method="post" action="/product/downloadExcel.htm">
				<input type="hidden" id="showSetpOne" name="showSetpOne" value="true" />
				<div class="batch_product_lable" style="height: auto; min-height:30px">
					<div id="stepOneTip" style="float:right;margin:8px 15px 0 0;"></div>
					<span style="float:right;color:blue;font-size:13px;font-weight:normal;padding-right:15px;"><a href="#" id="stepOne">跳过这一步</a></span>
					<span>第一步：添加图片</span>
					<div id="buttonDiv">
						<s:if test="result.isImgFull">
							<div style="display:none;float:left;padding-right:2px;"><span id="multiImgButtonId"></span></div>
								<div style="display:block;float:left;padding-right:2px;">
									<img src="/img/button_disable.gif" onmousemove="javascript:$(this).next().show();" onmouseout="javascript:$(this).next().hide();" style="margin-top:5px;"/>
									<div style="display:none;position: absolute;margin-top:-48px;*margin-top:-25px;*margin-left:-72px;padding:0px 4px;width:140px;height:22px;border:1px solid #C4D8E6;background: #E2EBF7;line-height: 22px;">已达到图片上传数量限制</div>
								</div>
						</s:if>
						<s:else>
							<div style="display:block;float:left;padding-right:2px;margin-top:4px;"><span id="multiImgButtonId"></span></div>
						</s:else>
						<input id="selectMulti" type="button" value="从图库选取" style="*margin-top:3px;*width:71px;height:24px;font-size:12px;*padding-top:2px;"/>
						<input id="_multiRemoveAll" type="button" value="全部删除" style="*margin-top:3px;*width:60px;height:24px;font-size:12px;*padding-top:2px;"/>
						<input type="checkbox" checked="checked" id="watermark" name="image.watermark">&nbsp;&nbsp;是否给图片加水印
						<div id="div_watermark" style="line-height: 30px; vertical-align: text-top;">
								<input type="text" id="watermarkText" name="image.watermarkText" value="${result.image.watermarkTextReal}" style="width:200px;" />
								<img id="colorSrc" style="width:20px;height:20px;cursor: pointer;vertical-align: middle">
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
				</div>
				<div id="imgBox" class="imgBox"> 
					<div id="_imageMulti" style="margin-bottom:5px;">
					</div>
				</div>
				<div class="batch_product_lable">
					<span>第二步：所属产品目录</span>
				</div>
				<div style="margin-left:80px;">
					<div>
						<input type="hidden" id="catId" name="catId" value="<s:property value='result.product.catId'/>" />
						<input type="text" id="catName" name="catName" value="<s:property value='result.product.catName'/>" style="width:350px" readonly />
						<input type="button" id="catSimilarSelect" value="<s:text name='button.selectSimilar'/>" style="*margin-top:3px;*width:84px;height:24px;font-size:12px;*padding-top:2px;"/>
						<input type="button" id="_cat" value="<s:text name="button.selectCat" />" style="*margin-top:3px;*width:71px;height:24px;font-size:12px;*padding-top:2px;"/>
					</div>
					<div id="_catList" ></div>
				</div>
				<div id="step_3">
				<div class="batch_product_lable">
					<span>第三步：下载/上传生成的电子表格</span>
				</div>
				<div style="float:left;">
					<span style="margin-left:80px;margin-right:5px;">下载电子表格</span>
					<input type="button" id="downloadExcel" value="下载电子表格" onclick="checkForm();" style="*margin-top:3px;*width:84px;height:24px;font-size:12px;*padding-top:2px;"/>
					<span style="margin-left:5px;">按照要求填写产品信息</span>
					<br />
				</div>
				</div>
			</form>
			<br />
			<br />
			<br />
			<div id="upload_form">
			<form id="uploadExcelForm" method="post" enctype="multipart/form-data" action="/product/batch_add_submit.htm" >
				<input type="hidden" id="foodPact_hidden" name="foodPact" />
				<span style="margin-left:80px;margin-right:30px;">上传填写后的表格：</span>
				<input type="file" name="attachment" value="" style="*height:24px;" onchange="this.nextSibling.nextSibling.disabled='';" />
				<input type="submit" value="上传" disabled="disabled"/>
			</form>
			</div>
			<br />
		</div>
		<div id="selectImgDialog"></div>
		<div id="productDialog"></div>
		<%@ include file="/page/product/inc/food_pack.jsp" %>
	</body>
</html>