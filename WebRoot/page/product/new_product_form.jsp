<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.product.proId > 0">
	<s:set name="pageTitle" value="%{getText('newproduct.editTitle')}" />
	<s:set name="formAction" value="'/newproduct/new_product_edit_submit.htm'" />
</s:if>
<s:else>
	<s:set name="pageTitle" value="%{getText('newproduct.addTitle')}" />
	<s:set name="formAction" value="'/newproduct/new_product_add_submit.htm'" />
</s:else>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:property value="#pageTitle"/>
			<s:if test="0 >= result.product.proId">
				<span class="gray">
					<s:text name="product.addTitleNotice" >
						<s:param><s:property value="result.newProCount"/></s:param>
						<s:param><s:property value="result.newProMax"/></s:param>
					</s:text>
				</span>
			</s:if>	
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<!--	
		<script type="text/javascript" src="/tiny_mce/tiny_mce.js"></script>
		<script type="text/javascript" src="/tiny_mce/tiny.extend.js"></script>
		-->
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript">
			var fckeditor = null;
			$(document).ready(function () {
				$("#phototooltip").tooltip({
					title:'<s:text name="product.imgPath" />',
					detail:'为您的产品添加清晰的产品图片，能使买家对您的产品有更直观的认识。'
				});
				$("#proNametooltip").tooltip({
					title:'<s:text name="product.proName" />',
					detail:'产品名称务必准确，避免太笼统。例如“皮质宠物项圈”',
					toolTipwidth:150
				});
				$("#breftooltip").tooltip({
					title:'<s:text name="product.brief"/>',
					detail:'建议您填写产品的优势，信息显示在搜索结果中，有助于吸引采购商眼球。'
				});
				$("#descriptiontooltip").tooltip({
					title:'<s:text name="product.description"/>',
					detail:'请填写产品的详细描述，可以包括用途、功能、使用寿命、注意事项和出口方向等，有助于采购商更好地了解您的产品。请不要添加个人联系信息和网址，否则审核人员会退回您的信息！',
					toolTipwidth:300
				});
			
				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 780,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}			
				});
			
				countNum(150, $("#brief"), $('#sumTip'));
				getProvince("province","city","town","code","townCode","notChina");
				
				$("#newproductForm").validateForm({
					rules: {
						proName:{required:true, maxlength:120},
						model:{maxlength:50},
						place:{maxlength:100},
						brief:{required:true, maxlength:150}
					},
					messages: {
						proName:{required:'<s:text name="product.proName.required" />', maxlength:'<s:text name="product.proName.required" />'},
						model:{maxlength:'<s:text name="product.model.maxlength" />'},
						place:{maxlength:'<s:text name="product.place.required" />'},
						brief:{required:'<s:text name="product.brief.required" />', maxlength:'<s:text name="product.brief.required" />'}
					}
				});
				
				$("#descriptionDiv").html($("#description").val());
				if($("#descriptionDiv").height() > 300){
					$("#descriptionDiv").css({height:"300px",overflow:"scroll"});
				}
				$("#description").hide();
				$("#fckButton").click(function(){
					$("#descriptionDiv").hide();
					fckeditor = new FCKeditor("description", 600, 300, "Admin") ;
					fckeditor.ReplaceTextarea();
					// TinyMCE_initEditer("description", "zh", 600, 300);
					// $("#description").show();
					$(this).hide();
				});
				$("#province").change(function(){
					if($(this).val() == " "){
						$(this).val("");
						$("[name='place']").rules("add", {maxlength:100,
				 				messages: { maxlength:'<s:text name="product.place.required" />'}});
					}
				});
				$("#toChina").click(function(){
					$("#notChina").hide();
					$("#china").show();
					
					$("[name='place']").rules("remove");
				});
				<s:if test="0 >= result.product.proId">
					$("#fckButton").click();
				</s:if>
				<s:if test="result.product.proId>0">
					$("#newproductForm").checkform({
						ignoreTag: "#colorSrc",
						url: "/newproduct/new_product_list.htm"
					});
				</s:if>
			});
			
			function checkNewProductForm(){
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				// 产地选择时，清空place字段
				if($("#notChina").css("display") == "none"){
					$("#place").val("");
				}
//				if(tinyMCE){
//					$('#description').val(TinyMCE_CleanWord(tinyMCE.get('description').getContent()));
//				}
				if(fckeditor){
					$('#description').val(FCKeditor_CleanWord(FCKeditorAPI.GetInstance('description').GetXHTML()));
					var desc=$('#description').val();
					var showlist1 = desc.match(regEmail);
					var isemail=true;
					if(showlist1 != null && showlist1.length > 0){
					    for (var k = 0; k < showlist1.length; k++) {
					    	if(showlist1[k].indexOf(HI_DOMAIN)==-1){
						    	alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交! \n  错误信息："+showlist1[k]);
					    		return false;
					    	}
					   }
					 }
					var showlist2 = desc.match(regUrl);
					var isurl=true;
				    if(showlist2 != null && showlist2.length > 0 ){
				    	for (var j = 0; j < showlist2.length; j++) {
				    		if(showlist2[j].indexOf(HI_DOMAIN)==-1){
				    			if(isurl){
				    				alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交! \n 错误信息："+showlist2[j]);
				    				return false;
				    			}
				    		}
				    	}
				    }
				}
				if(!briefCheck()) {
					return false;
				} 
			}
			function briefCheck(){
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var brief = $("#brief").val();
				var showlist1 = brief.match(regEmail);
				var isemail=true;
				if(showlist1 != null && showlist1.length > 0){
				    for (var k = 0; k < showlist1.length; k++) {
				    	if(showlist1[k].indexOf(HI_DOMAIN)==-1){
				    		$("#briefTip").html("<label class='error' for='brief' generated='true'>请不要在 摘要 中添加网址和邮箱，这将导致信息无法提交！</label>");
							$("#brief").focus(); 
								return false;
				    	}
				   }
				 }
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				var showlist2 = brief.match(regUrl);
				var isurl=true;
			    if(showlist2 != null && showlist2.length > 0 ){
			    	for (var j = 0; j < showlist2.length; j++) {
			    		if(showlist2[j].indexOf(HI_DOMAIN)==-1){
			    			if(isurl){
			    				$("#briefTip").html("<label class='error' for='brief' generated='true'>请不要在 摘要 中添加网址和邮箱，这将导致信息无法提交！</label>");
								$("#brief").focus(); 
								return false;
			    			}
			    		}
			    	}
			    }
				return true;
			}
		</script>
	</head>
	<body>
		<h2>
			<s:text name="input.notice" />
		</h2>
		<form id="newproductForm" name="newproductForm" action="<s:property value="#formAction"/>" method="post" onsubmit="return checkNewProductForm();" >
			<s:token />
			<input type="hidden" name="proId" value="<s:property value='result.product.proId'/>" />
			<input type="hidden" id="townCode" value="<s:property value='result.product.countryCode'/>" />
			<table class="formTable">
				<tr>
					<th>
						<s:text name="product.imgPath" />：
					</th>
					<td>
					<div id="phototooltip" style="position:absolute; margin-top:61px;margin-left:210px; width:16px; height:16px;"></div>
						<%
							int imgType = Image.PRODUCT;
							String imgSrcTag = "imgSrc";
							String imgPathTag = "imgPath";
							String imgIdTag = "imgId";
						%>
						<s:set name="imgSrc" value="result.product.imgPath75" />
						<s:set name="imgPath" value="result.product.imgPath" />
						<s:set name="imgId" value="result.product.imgId" />
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_bar.jsp" %>
						<%@ include file="/page/product/inc/watermark.jsp" %>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="product.proName" />：</th>
					<td>
						<input type="text" name="proName" value="<s:property value='result.product.proName'/>" style="width:300px;"/><span id="proNametooltip"></span>
					</td>
				</tr>
				<tr>
					<th><s:text name="product.model" />：</th>
					<td>
						<input name="model" value="<s:property value='result.product.model'/>" style="width:300px;"/>
					</td>
				</tr>
					<tr>
						<th><s:text name="product.origin" />：</th>
						<td>
							<span id="china" <s:if test="result.product.place != null && result.product.place != ''">style="display:none;"</s:if>>
								<select id="province" name="province" ></select>
								<select id="city" name="city" ></select>
								<select id="town" name="town" ></select><%--
								<a href="#position;" id="chinaToNot"><s:text name="product.other" /></a>
								--%><span id="provinceTip"></span>
							</span>
							<span id="notChina" <s:if test="result.product.place == null || result.product.place == ''">style="display:none;"</s:if>>
								<input id="place" name="place" value="<s:property value='result.product.place'/>" />
								<a href="#position;" id="toChina"><s:text name="product.china" /></a>
								<span id="placeTip"></span>
							</span>
						</td>
					</tr>
				<tr>
					<th><span class="red">*</span><s:text name="product.brief" />：</th>
					<td class="fieldTips">
						<textarea id="brief" onblur="briefCheck()" style="width: 450px; height: 70px;" onkeyup="countNum(150, $('#brief'), $('#sumTip'))" onbeforepaste="countNum(150, $('#brief'), $('#sumTip'))" onbeforeeditfocus="countNum(150, $('#brief'), $('#sumTip'))" name="brief"><s:property value='result.product.brief'/></textarea><br />
						剩余<span class="red" id="sumTip">150</span>个字符。不支持HTML代码。<span id="breftooltip"></span>
						<div id="briefTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="product.description"/>：</th>
					<td>
						<input id="fckButton" type="button" value="<s:text name="button.fckEdit" />" /><br />
						<div id="descriptionDiv" style="width:600px;"></div>
						<textarea name="description" id="description" ><s:property value='result.product.description' /></textarea>
						<span class="fieldTips">编辑框内HTML源代码最多10000个字符。<span id="descriptiontooltip"></span></span>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name='button.submit' />"/>
				<input type="reset" value="<s:text name='button.reset' />"/>
			</div>
		</form>
		<div id="selectImgDialog"></div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>