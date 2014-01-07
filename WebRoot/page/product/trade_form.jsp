<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.commons.CN"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:if test="result.product.proId > 0">
				<s:text name="trade.editTitle"/>
			</s:if>
			<s:else>
				<s:text name="trade.addTitle"/>
				<span class="gray">
					<s:text name="trade.exist">
					<s:param><s:property value="result.product.tradeCount"/></s:param>
					<s:param><s:property value="result.product.tradeMax"/></s:param></s:text>
				</span>
				<span class="sapn1" style="margin:0;margin-top:12px;*margin-top:0px;">
					&quot;<font class="red">*</font>
					&quot; 为必填项
				</span>
			</s:else>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<!--	
		<script type="text/javascript" src="/tiny_mce/tiny_mce.js"></script>
		<script type="text/javascript" src="/tiny_mce/tiny.extend.js"></script>
		-->
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript" src="/js/product.js?v=20120920"></script>
		<script type="text/javascript">
			var fckeditor = null;
			var validator = null;
			$(document).ready(function () {
				$("#tradeImgtooltip").tooltip({
					title:'<s:text name="trade.imgPath"/>',
					detail:'可以为您发布的商情添加图片，更好的展示您的供应/求购商情的需求。'
				});
				$("#tradeTypetooltip").tooltip({
					title:'<s:text name="trade.proType"/>',
					detail:'商情类型分为供应商情和求购商情，请根据您的需求选择相应的商情类型。',
					toolTipwidth:150
				});
				$("#tradeNametooltip").tooltip({
					title:'<s:text name="trade.proName"/>',
					detail:'商情主题务必准确，明示商情类型。例如：采购不锈钢厨具'
				});
				$("#keywordstooltip").tooltip({
					title:'<s:text name="trade.keywords"/>',
					detail:'1、买家通常使用关键词来搜索商情，请按照买家的搜索习惯设置关键词。例如：商情"提供宁波地区婚庆服务"，可将关键词设置为"宁波婚庆服务哪家好"或"宁波婚庆服务推荐"。<br>2、更多的推荐关键词请点击<a href="https://adwords.google.com/select/KeywordToolExternal?forceLegacy=true" target="_blank">这里</a>。',
					toolTipwidth:300
				});
				$("#generaltooltip").tooltip({
					title:'<s:text name="trade.groupId"/>',
					detail:'为您的商情选择一个普通分组，您可以将同一类的产品添加在相同的分组中，使买家在浏览商情分组时更加快捷。'
				});
				$("#brieftooltip").tooltip({
					title:'<s:text name="trade.brief"/>',
					detail:'建议您填写商情的优势，信息显示在搜索结果中，有助于吸引采购商/供应商眼球。'
				});
				$("#descriptiontooltip").tooltip({
					title:'<s:text name="trade.description"/>',
					detail:'请填写商情的详细描述，可以包括用途、功能、使用寿命等，有助于采购商/供应商更好地了解您的商情信息。请不要添加个人联系信息和网址，否则审核人员会退回您的信息！',
					toolTipwidth:300
				});
				$("#_catList").category({
					contId: "catName",
					contType: "text"
				});
				countNum(150, $("#brief"), $('#sumTip'));
				
				<s:if test="loginUser.memberType != 2 && result.product.proId == 0">
					$("#loadValidateCode").click(function (){
						loadValidateCode();
					});
				</s:if>
				
				$("#descriptionDiv").html($("#description").val());
				if($("#descriptionDiv").height() > 300){
					$("#descriptionDiv").css({height:"300px",overflow:"scroll"});
				}
				$("#fckButton").click(function(){
					$("#descriptionDiv").hide();
					fckeditor = new FCKeditor("description", 520, 300, "Admin") ;
					fckeditor.ReplaceTextarea();
					// TinyMCE_initEditer("description", "zh", 520, 300);
					// $("#description").show();
					$(this).hide();
				});
				
				<s:if test="result.product.proId > 0">
					$("#tradeForm").attr("action", "/trade/trade_edit_submit.htm");
				</s:if>
				<s:else>
					$("#tradeForm").attr("action", "/trade/trade_add_submit.htm");
				</s:else>

				$("#groupList").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 500,
					width: 520,
					modal: true
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
				
				$("#groupSelect").click(function(){
					$("#groupList").dialog('open');
				});
				
				$("#groupRemove").click(function(){
					$("#groupId").val(0);
					$("#groupName").val("");
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
				$("#groupAddDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 150,
					width: 600,
					modal: true,
					close: function(){
						$("#groupAddDialog").empty();
					}
				});
				
				$("#groupAdd").click(function(){
					$("#groupAddDialog").load("/group/group_add.do");
					$("#groupAddDialog").dialog('option', 'title', '<s:text name="button.addFirstGroup"/>');
					$("#groupAddDialog").dialog('option', 'height', '250');
					$("#groupAddDialog").dialog('open');
				});
				
				$("#catSimilarSelect").click(function(){
					$("#productDialog").load("/product/similar_category.do");
					$("#productDialog").dialog('option', 'title', '<s:text name="button.selectSimilar"/>');
					$("#productDialog").dialog('open');
				});
				
				<%--表单验证 --%>
				validator = $("#tradeForm").validate({
					errorPlacement: function(error, element) {
						fieldName = element.attr("name");
						if(fieldName.length > 2){
							var num = fieldName.substring(fieldName.length - 1, fieldName.length);
							// 是否以数字结尾
							if(!isNaN(num * 1)){
								fieldName = fieldName.substring(0, fieldName.length - 1);
							}
						}
						
						if(document.getElementById(fieldName + "Tip")){
							$("#"+fieldName+"Tip").html(error);
						}else{
							element.after(error);
						}
					},
					
					rules: {
						proType:{required:true},
						proName:{required:true, maxlength:120},
						keyword1:{notEqual:"#keyword2,#keyword3", noComma: true, maxlength: 60},
						keyword2:{notEqual:"#keyword1,#keyword3", noComma: true, maxlength: 60 },
						keyword3:{notEqual:"#keyword1,#keyword2", noComma: true, maxlength: 60},
						catName:{required:true},
						brief:{required:true, maxlength:150},
						validDay:{required:true}
						<s:if test="loginUser.memberType != 2 && result.product.proId == 0">
						,
						validateCode: {required: true, rangelength:[5,5]}
						</s:if>
					},
					messages: {
						proType:'<s:text name="trade.proType.required"/>',
						proName:'<s:text name="trade.proName.required"/>',
						keyword1:{notEqual:'商情关键字不能重复', maxlength: '<s:text name="trade.keywords.maxlength"/>', noComma: "不能含有逗号"},
						keyword2:{notEqual:'商情关键字不能重复', maxlength: '<s:text name="trade.keywords.maxlength"/>', noComma: "不能含有逗号"},
						keyword3:{notEqual:'商情关键字不能重复', maxlength: '<s:text name="trade.keywords.maxlength"/>', noComma: "不能含有逗号"},
						catName:'<s:text name="trade.catName.required"/>',
						brief:'<s:text name="trade.brief.required"/>',
						validDay:'<s:text name="trade.validDay.required"/>'
						<s:if test="loginUser.memberType != 2 && result.product.proId == 0">
						,
						validateCode: '<s:text name="validateCode.required" />'
						</s:if>
					}
				});

				<s:if test="result.product.proId == 0">
					$("#fckButton").click();
				</s:if>
			});
			
			<s:if test="result.product.proId > 0">
				$("#tradeForm").checkform({
					ignoreTag: "#colorSrc",
					url:"/trade/trade_list.htm",
					validator: validator
				});
			</s:if>
			
			jQuery.extend(jQuery.validator.methods, {
				notEqual: function(value, element, param) {
					var equal = true;
					value = $.trim(value);
					$(param).each(function(){
						var otherValue = $.trim($(this).val());
						if(otherValue != "" && value != "" && otherValue == value){
							equal = false;
							return;
						}
					});
					return equal;
				}
			});
			
			function selectGroup(groupId, groupName, groupPath){
				$("#groupId").val(groupId);
				$("#groupName").val(groupPath);
				$("#groupList").dialog('close');
			}

			function loadValidateCode(){
				Util.loadValidateCode(document.tradeForm, "validateCodeImg", "/validateCode/getImage");
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
			function checkForm(){
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				if(fckeditor){
					$('#description').val(FCKeditor_CleanWord(FCKeditorAPI.GetInstance('description').GetXHTML()));
					var desc=$('#description').val();
					var showlist1 = desc.match(regEmail);
					var isemail=true;
					if(showlist1 != null && showlist1.length > 0){
					    for (var k = 0; k < showlist1.length; k++) {
					    	if(showlist1[k].indexOf(HI_DOMAIN)==-1){
						    	alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交! \n 错误信息："+showlist1[k]);
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
				    				alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交 !\n  错误信息："+showlist2[j]);
				    				return false;
				    			}
				    		}
				    	}
				    }
				}
				if(!briefCheck()) {
					return false;
				}else{
					$("#tradeForm").submit();
				} 
				
			}
		</script>
	</head>
	<body>
		<form id="tradeForm" name="tradeForm" method="post">
			<input type="hidden" name="proId" value='<s:property value="result.product.proId"/>' />
			<input type="hidden" name="copyProId" value='<s:property value="result.product.copyProId"/>' /><%--添加时使用，修改时需在service中设置为0 --%>
			<input type="hidden" name="oldGroupId" value='<s:property value="result.product.groupId"/>' />
			<s:if test="loginUser.memberType != 2 && result.product.proId == 0">
				<input type="hidden" name="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
			</s:if>
			
			<s:if test="result.product.proId == 0">
				<input type="hidden" name="tradeCount" value='<s:property value="result.product.tradeCount"/>' />
				<input type="hidden" name="tradeMax" value='<s:property value="result.product.tradeMax"/>' />
			</s:if>
			<table class="formTable">
				<tbody>
					<tr>
						<th>
							<s:text name="trade.imgPath"/>：
						</th>
						<td>
						<div id="tradeImgtooltip" style="position:absolute; margin-top:61px;margin-left:210px; width:16px; height:16px;"></div> 
							<%
								int imgType = Image.PRODUCT;
								String imgSrcTag = "imgSrc";
								String imgPathTag = "imgPath";
								String imgIdTag = "imgId";
							%>
							<s:set name="imgSrc" value="result.product.imgPath75" />
							<s:set name="imgPath" value="result.product.imgPath" />
							<s:set name="imgId" value="result.product.imgId"/>
							<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
							<s:set name="imgSize" value="'100'" />
							<%@ include file="/page/inc/image_bar.jsp" %>
							<%@ include file="/page/product/inc/watermark.jsp" %>
						</td>
					</tr>
					
					<tr>
						<th><span class="red">*</span><s:text name="trade.proType"/>：</th>
						<td>
							<select name="proType" id="proType" >
	              				<option value='<%=CN.TRADE_SELL%>' <s:if test="result.product.proType == 1"> selected</s:if>><s:text name="trade.sell"/></option>
								<option value='<%=CN.TRADE_BUY%>' <s:if test="result.product.proType == 2"> selected</s:if>><s:text name="trade.buy"/></option>
							</select>
							<span id="tradeTypetooltip"></span>
						</td>
					</tr>

					<tr>
						<th><span class="red">*</span><s:text name="trade.proName"/>：</th>
						<td>
							<input type="text" id="proName" name="proName" value='<s:property value="result.product.proName"/>' style="width:350px;"/>
							<span id="tradeNametooltip"></span>
							<div id="proNameTip"></div>
						</td>
					</tr>
					
					<tr>
						<th><s:text name="trade.keywords"/>：</th>
						<td class="fieldTips">
							<input type="text" id="keyword1" name="keyword1" value="<s:property value='result.product.keyword1'/>" style="width:112px;" style="margin-bottom:2px;"/>
							<s:if test="result.product.keyword2 != ''"><input type="text" id="keyword2" name="keyword2" value="<s:property value='result.product.keyword2'/>" style="width:112px;" style="margin-bottom:2px;"/></s:if>
							<s:if test="result.product.keyword3 != ''"><input type="text" id="keyword3" name="keyword3" value="<s:property value='result.product.keyword3'/>" style="width:112px;" style="margin-bottom:2px;"/></s:if>
							<!--  所有商情的关键词均不能重复。 --><span id="keywordstooltip"></span>
							<div id="keywordTip"></div>
						</td>
					</tr>
					
					<tr>
						<th><s:text name="trade.groupId"/>：</th>
						<td class="fieldTips">
							<input type="hidden" id="groupId" name="groupId" value="<s:property value='result.product.groupId'/>" />
							<input type="text" id="groupName" name="groupName" value='<s:property value="result.product.groupName"/>' style="width:350px;" readonly />
							<input type="button" id="groupSelect" value="<s:text name='button.selectGroup' />" />
							<input type="button" id="groupRemove" value="<s:text name='button.remove' />" /><br />
							<s:text name="trade.groupNotice1" /><s:if test="loginUser.showGroup == true"><s:text name="trade.groupNotice2" /><a id="groupAdd" href="#position"><s:text name="button.create" /></a></s:if>。
							<span id="generaltooltip"></span>
						</td>
					</tr>
					
					<tr>
						<th><span class="red">*</span><s:text name="trade.catId"/>：</th>
						<td class="fieldTips">
							<input type="hidden" id="catId" name="catId" value="<s:property value='result.product.catId'/>" />
							<input type="text" id="catName" name="catName" value="<s:property value='result.product.catName'/>" style="width:350px;" readonly />
							<input type="button" id="catSimilarSelect" value="<s:text name='button.selectSimilar'/>" />
							<input type="button" id="_cat" value="<s:text name="button.selectCat" />" />
							<br />
							<s:text name="trade.catNotice"/>
							<div id="catNameTip" ></div>
							<div id="_catList" ></div>
						</td>
					</tr>
				
					<tr>
						<th><span class="red">*</span><s:text name="trade.brief"/>：</th>
						<td class="fieldTips">
							<textarea onblur="briefCheck()" id="brief" style="width: 520px; height: 100px;" onkeyup="countNum(150, $('#brief'), $('#sumTip'))" onbeforepaste="countNum(150, $('#brief'), $('#sumTip'))" onbeforeeditfocus="countNum(150, $('#brief'), $('#sumTip'))" name="brief"><s:property value='result.product.brief'/></textarea><br />
							剩余<span class="red" id="sumTip">150</span>个字符。不支持HTML代码。<span id="brieftooltip"></span>
							<div id="briefTip"></div>
						</td>
					</tr>

					<tr>
						<th><s:text name="trade.description"/>：</th>
						<td class="fieldTips">
							<input id="fckButton" type="button" value="<s:text name="button.fckEdit" />" />
							<div id="descriptionDiv" style="width:420px;"></div>
							<textarea name="description" id="description" style="display:none;"><s:property value='result.product.description' escape="false"/></textarea>
							<br />编辑框内HTML源代码最多10000个字符。<span id="descriptiontooltip"></span>
							<div id="desc_tip"></div>
						</td>
					</tr>
					
					<tr>
						<th><span class="red">*</span><s:text name="trade.validDay" />：</th>
						<td>
							<select name="validDay" id="validDay" >
	              				<option value="7" <s:if test="result.product.validDay == 7"> selected</s:if>><s:text name="trade.validDay7" /></option>
	              				<option value="15" <s:if test="result.product.validDay == 15"> selected</s:if>><s:text name="trade.validDay15" /></option>
	              				<option value="30" <s:if test="result.product.validDay == 30"> selected</s:if>><s:text name="trade.validDay30" /></option>
	              				<option value="90" <s:if test="result.product.validDay == 90"> selected</s:if>>90天</option>
	              				<option value="180" <s:if test="result.product.validDay == 180"> selected</s:if>>180天</option>
	              				<option value="365" <s:if test="result.product.validDay == 365"> selected</s:if>>1年</option>
							</select>
						</td>
					</tr>
					<s:if test="loginUser.memberType != 2 && result.product.proId == 0">
						<tr>
							<th><span class="red">*</span> 验证码：</th>
							<td>
								<input type="text" id="validateCode" name="validateCode" value="<s:property value="validateCode"/>" autocomplete="off" maxlength="5"/>
								<a href="#postion" id="loadValidateCode">看不清，换一张</a>
								<span id="validateCodeTip"></span>
							</td>
						</tr>
						<tr>
							<td width="13%"> </td>
							<td width="47%">
								<s:if test="validateCodeKey == null">
									<script type="text/javascript">
										$(function(){
											loadValidateCode();
										});
									</script>
									<img id="validateCodeImg" height="50" align="absmiddle" width="160" />	
								</s:if>
								<s:else>
									<img id="validateCodeImg" height="50" align="absmiddle" width="160" src="/validateCode/getImage?hi_vc_key=<s:property value="validateCodeKey"/>"/>	
								</s:else>
							</td>
						</tr>
					</s:if>
				</tbody>
			</table>
			<div class="buttonCenter">
				<input type="button" onclick="checkForm()" value="<s:text name='button.submit' />"/>
				<input type="reset" value="<s:text name='button.reset' />"/>
			</div>
		</form>
		<div id="selectImgDialog"></div>
		<div id="productDialog"></div>
		<div id="groupAddDialog"></div>
		<div id="groupList" title='<s:text name='button.selectGroup' />' >
			<iframe src='/product/select_group_list.do' frameborder="no" scrolling="yes" style="width:100%; height:420px;*height:450px !important;*height:450px;"></iframe>
		</div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>