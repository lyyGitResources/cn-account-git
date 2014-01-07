<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:if test="result.product.proId > 0">
				<s:text name="product.editTitle" />
			</s:if>
			<s:else>
				<s:text name="product.addTitle" />
				<span class="gray">
					<s:text name="product.addTitleNotice" >
						<s:param><s:property value="result.productCount"/></s:param>
						<s:param><s:property value="result.productMax"/></s:param>
					</s:text>
				</span>
			</s:else>	
			<span class="sapn1" style="margin:0;margin-top:12px;*margin-top:0px;">
				"<font class="red">*</font>" <s:text name="product.formTitleNotice" />
			</span>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/??lib/jquery.countDown.js,lib/jquery.category.js,lib/jquery.category.data_zh.js,locale_cn.js"></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.extend.js" ></script>
		<script type="text/javascript" src="/js/fckeditor/fckeditor.js"></script>
		<script type="text/javascript" src="/js/datePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/js/product.js?v=20120920"></script>
		<script type="text/javascript" src="/js/jquery.simplemodal.js"></script>
		<script type="text/javascript">
			var fckeditor = null;
			var validator = null;
			var multiSrc = "";
			var position = ""; // 判断光标所在元素ID，用于获得相关关键词
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
								$foodPact_hidden.val('false');
								$product_more_form.hide();
							}
							this.close();
						}
					});
				} else {
					$product_more_form.show();
				}
			}
			</s:if>
			
			
			$(function(){
				$("#brief").countDown();
				getProvince("province","city","town","code","countryCode","notChina");
				initDialog()
				issubmit = false;
				
				$foodPact_hidden = $("#foodPact_hidden");
				$product_more_form = $(".formTable tr:gt(1), .buttonCenter");
				
				$("#food_pack_div .sing").click(function() {
					$foodPact_hidden.val('true');
					food_pact = 1;
					food_pact_modal.close();
					$product_more_form.show();
				});
				
				$("#_catList").category({
					contId: "catName",
					tags: true, 
					contType: "text"
					<s:if test="!result.company.foodPact">
					,ext_click: function(root_id) {
						if (root_id == 1 && food_pact == 0) {
							food_pact_modal = $("#food_pack_div").modal({
								onClose: function() {
									if (food_pact == 0) {
										$foodPact_hidden.val('false');
										$product_more_form.hide();
									}
									this.close();
								}
							});
						} else {
							$product_more_form.show();
						}
					}
					</s:if>
					//hidden: "#proAttribute,#customAttribute"
				});
				
				<s:if test="result.product.optimizeProduct">
					bindKeyword();
				</s:if>
				
				$("#descriptionDiv").html($("#description").val());
				if($("#descriptionDiv").height() > 300){
					$("#descriptionDiv").css({height:"300px",overflow:"scroll"});
				}
				$("#fckButton").click(function(){
					$("#descriptionDiv").hide();
					fckeditor = new FCKeditor("description", 560, 300, "Admin") ;
					fckeditor.ReplaceTextarea();
					// TinyMCE_initEditer("description", "zh", 600, 300);
					// $("#description").show();
					$(this).hide();
				});
				<s:if test="result.product.proId == 0">
				 	// IE6下与城市选择js冲突
					if(!$.browser.msie || $.browser.version != 6.0){
						$("#fckButton").click();
					}
				</s:if>
				
				// 交易条件和供货能力显示情况
				if(<s:property value='result.product.trade'/> > 0){
					toggleTrade();
				}
				if(<s:property value='result.product.suppliable'/> > 0){
					toggleSuppliable();
				}
				$("#tradeDiv").click(function(){
					toggleTrade();
				});
				$("#suppliableDiv").click(function(){
					toggleSuppliable();
				});
				
				
				$("#proAddImg").click(function(){
					if($(this).attr("checked")){
						$("#proAddImgDiv").show();
					}else{
						$("#proAddImgDiv").hide();
					}
				});

				$("#catSimilarSelect").click(function(){
					$("#productDialog").load("/product/similar_category.do");
					$("#productDialog").dialog('option', 'title', '<s:text name="button.selectSimilar"/>');
					$("#productDialog").dialog('open');
				});
				$("#specialGroupAdd").click(function(){
					$("#groupAddDialog").load("/specialGroup/group_add.do");
					$("#groupAddDialog").dialog('option', 'title', '<s:text name="button.addSpecialGroup"/>');
					$("#groupAddDialog").dialog('option', 'height', '120');
					$("#groupAddDialog").dialog('open');
				});
				$("#groupAdd").click(function(){
					$("#groupAddDialog").load("/group/group_add.do");
					$("#groupAddDialog").dialog('option', 'title', '<s:text name="button.addFirstGroup"/>');
					$("#groupAddDialog").dialog('option', 'height', '250');
					$("#groupAddDialog").dialog('open');
				});
				$("#specialGroupSelect").click(function(){
					$("#specialGroupDialog").load("/product/select_special_group_list.do?optimize=1&random="+Math.random());
					$("#specialGroupDialog").dialog('option', 'title', '<s:text name="button.selectSpecialGroup" />');
					$("#specialGroupDialog").dialog('open');
				});
				$("#groupSelect").click(function(){
					$("#groupList").dialog('open');
				});
				$("#groupRemove").click(function(){
					$("#groupId").val(0);
					$("#groupName").val("");
				});
				$("#specialGroupRemove").click(function(){
					$("#specialGroupId [name='specialGroupBox']:checked").each(function(){
						$(this).parent().remove();
					});
				});
				
				$("#minOrderUnitOther").blur(function(){
					$('#minOrderUnit').val($(this).val())
					
					// 辅助验证起订量
					if($(this).val() == ""){
						$("#minOrderTip").html('<label class="error" for="minOrderNum" generated="true">请输入对应的计量单位</label>');
					}else{
						$("#minOrderTip").html('<label class="error success" for="price2" generated="true">OK</label>');
					}
				});
				
				validator = $("#productForm").validate({
					errorPlacement: function(error, element) {
						document.getElementById('btn_submit').disabled = false;
						fieldName = element.attr("name");
						
						// 最后一位为数字，并且前面字符一样的，提示使用同一个位置
						if(fieldName.length > 2){
							var num = fieldName.substring(fieldName.length - 1, fieldName.length);
							if(Util.isInt(num)){
								fieldName = fieldName.substring(0, fieldName.length - 1);
							}
						}
						
						// 城市省份需要判断province,city，提示使用同一个位置。原因对于存在disabled属性的input不进行验证
						if(fieldName.toLowerCase().indexOf("province") != -1 || fieldName.toLowerCase().indexOf("city") != -1){
							fieldName = "city";
						}

						// 包含Tag，tag的验证提示放在tagValueTip中的DIV内
						if(element.attr("id") && (element.attr("id").indexOf("Tag") != -1 || element.attr("id").indexOf("tag") != -1)){
							$("#tagValueTip").html(error);
						}else if(fieldName.indexOf("minOrder") != -1){
							var tipHtml = $("#minOrderTip").html();
							if(tipHtml == "" || tipHtml.indexOf("OK") != -1){
								$("#minOrderTip").html(error);
							}	
						}else{
							if(document.getElementById(fieldName + "Tip")){
								var tipHtml = $("#"+fieldName+"Tip").html();
								if(tipHtml == "" || tipHtml.indexOf("OK") != -1){
									$("#"+fieldName+"Tip").html(error);
								}
							}else{
								element.after(error);
							}
						}
					},
					success: function(label) {
						if(label.attr("for") != "tagValue3"){
							label.text("OK").addClass("success");
						}
					},
					rules: {
						proName:{required:true, maxlength:120},
						model:{maxlength:50},
						keyword1:{notEqual:"#keyword2,#keyword3", noComma: true, maxlength: 60},
						keyword2:{notEqual:"#keyword1,#keyword3", noComma: true, maxlength: 60},
						keyword3:{notEqual:"#keyword1,#keyword2", noComma: true, maxlength: 60},
						place:{maxlength:100},
						catName:{required:true},
						tagValue2:{ maxlength:120},
						brief:{required:true, maxlength:150},
						price1:{priceNumber:true},
						price2:{priceNumber:true},
						paymentType:{maxlength:50},
						minOrderNum:{digits: true, max:999999999, correspond: <s:if test="result.product.minOrderUnitOthers == 'false'">"#minOrderUnitSelect"</s:if><s:else>"#minOrderUnitOther"</s:else>},
						<s:if test="result.product.minOrderUnitOthers == 'false'">
						minOrderUnitSelect:{correspond: "#minOrderNum"},
						</s:if>
						<s:else>
						minOrderUnitOther:{maxlength:20, correspond: "#minOrderNum"},
						</s:else>
						transportation:{maxlength:100},
						productivity:{maxlength:50},
						packing:{maxlength:80},
						deliveryDate:{maxlength:50}
					},
					messages: {
						proName:{required:'<s:text name="product.proName.required" />', maxlength:'<s:text name="product.proName.required" />'},
						model:{maxlength:'<s:text name="product.model.maxlength" />'},
						keyword1:{notEqual:'<s:text name="product.keywords.equal" />', maxlength: '<s:text name="product.keywords.required" />', noComma: "不能含有逗号", remote: "网络异常"},
						keyword2:{notEqual:'<s:text name="product.keywords.equal" />', maxlength: '<s:text name="product.keywords.required" />', noComma: "不能含有逗号", remote: "网络异常"},
						keyword3:{notEqual:'<s:text name="product.keywords.equal" />', maxlength: '<s:text name="product.keywords.required" />', noComma: "不能含有逗号", remote: "网络异常"},
						place:{maxlength:'<s:text name="product.place.required" />'},
						catName:{required:'<s:text name="product.catName.required" />'},
						brief:{required:'<s:text name="product.brief.required" />', maxlength:'<s:text name="product.brief.required" />'},
						tagValue2:{ maxlength:'<s:text name="product.tags.maxlength" />'},
						price1:'<s:text name="product.price.number" />',
						price2:'<s:text name="product.price.number" />',
						paymentType:{maxlength:'<s:text name="product.paymentType.maxlength" />'},
						minOrderNum:{digits: '<s:text name="product.minOrderNum.int" />', max: '<s:text name="product.minOrderNum.int" />', correspond:'<s:text name="product.minOrderNum.correspond" />'},
						<s:if test="result.product.minOrderUnitOthers == 'false'">
						minOrderUnitSelect:{correspond: '<s:text name="product.minOrderUnit.correspond" />'},
						</s:if>
						<s:else>
						minOrderUnitOther:{maxlength:'<s:text name="product.minOrderUnit.maxlength" />', correspond: '<s:text name="product.minOrderUnit.correspond" />'},
						</s:else>
						transportation:{maxlength:'<s:text name="product.transportation.maxlength" />'},
						productivity:{maxlength:'<s:text name="product.productivity.maxlength" />'},
						packing:{maxlength:'<s:text name="product.packing.maxlength" />'},
						deliveryDate:{maxlength:'<s:text name="product.deliveryDate.maxlength" />'}
					}	
				});

				$("#province").change(function(){
					if($(this).val() == " "){
						$(this).val("");
						$("[name='place']").rules("add", { maxlength:100, messages: {maxlength:'<s:text name="product.place.required" />'}});
					}
				});
				$("#toChina").click(function(){
					$("#notChina").hide();
					$("#china").show();
					
					$("[name='place']").rules("remove");
				});
				
				<s:if test="result.product.proId > 0">
					// 执行在多个图片选择之后，不然无法获得多个图片的src值
					setTimeout("bindProductForm()", 500);
				</s:if>	
				$("#moreParametertooltip").tooltip({
					title:'<s:text name="product.customAttribute"/>',
					detail:'如提供的产品属性不能满足您的要求，请在自定义属性中添加想要的属性名和属性值。例如：属性名 重量, 属性值 0.44克。产品属性和自定义属性将会显示在产品详细页图片的右侧。请尽可能的填写您的产品属性，这将对您产品的搜索优化起到作用。'
				});
				$("#phototooltip").tooltip({
					title:'<s:text name="product.imgPath" />',
					detail:'为您的产品添加清晰的产品图片，能使买家对您的产品有更直观的认识。需要上传更多的产品图片，请打钩。您最多可以上传6张图片来展示您的产品。'
				});
				$("#proNametooltip").tooltip({
					title:'<s:text name="product.proName" />',
					detail:'产品名称清晰准确，符合用户搜索习惯，避免太笼统。例如：皮质宠物项圈。',
					toolTipwidth:150
				});
				$("#keywordstooltip").tooltip({
					title:'<s:text name="product.keywords"/>（会员网站产品名）',
					detail:'1、买家通常使用关键词来搜索产品，请按照买家的搜索习惯设置关键词。例如：产品"婚庆服务"，可将关键词设置为"婚庆服务工作室"或"XX地区一流的婚庆服务"。<br>2、勾选"自动搜索与此产品名称相匹配的关键词"，机器将为您自动匹配关键词。<br>3、更多的推荐关键词请点击<a href="https://adwords.google.com/select/KeywordToolExternal?forceLegacy=true" target="_blank">这里</a>。',
					toolTipwidth:400
				});
				$("#generaltooltip").tooltip({
					title:'<s:text name="product.groupId" />',
					detail:'为您的产品选择一个普通分组，一个产品只能选择一个普通分组，您可以将同一类的产品添加在相同的分组中，使买家在浏览产品分组时更加快捷。<a href="http://help.hisupplier.com/groups.html" target="_blank">了解更多产品组信息</a>。',
					toolTipwidth:300
				});
				$("#specialtooltip").tooltip({
					title:'<s:text name="product.specialGroupId" />',
					detail:'为您的产品选择需要的特殊分组，一个产品可同时选择多个特殊分组，您可以将同一类的产品添加在相同的分组中，使买家在浏览产品分组时更加快捷。',
					toolTipwidth:150
				});
				$("#breftooltip").tooltip({
					title:'<s:text name="product.customAttribute"/>',
					detail:'建议您填写产品的优势，信息显示在搜索结果中，有助于吸引采购商眼球。',
					toolTipwidth:230
				});
				$("#descriptiontooltip").tooltip({
					title:'<s:text name="product.description"/>',
					detail:'请填写产品的详细描述，可以包括用途、功能、使用寿命、注意事项和出口方向等，有助于采购商更好地了解您的产品。请不要添加个人联系信息和网址，否则审核人员会退回您的信息！',
					toolTipwidth:300
				});
			});
			
			function bindProductForm(){
				$("#productForm").checkform({
					changeClick: "#clearFile, #_multiRemoveAll, [name='multiDelete']",
					ignoreTag: "input[type='checkbox'],#tradeImg,#suppliableImg,#colorSrc",
					url: "/product/product_list.htm",
					validator: validator
				});
			}
			
			function selectToOther(){
				// 辅助验证起订量
				if($("#minOrderUnitSelect").val() != "" && $("#minOrderUnitSelect").val() != " "){
					$("#minOrderTip").html('<label class="error success" for="price2" generated="true">OK</label>');
				}else{
					$("#minOrderTip").html('<label class="error" for="minOrderNum" generated="true">请输入对应的计量单位</label>');
				}
			
				// 用空格来表示计量单位选择的是‘其他’，""表示没有选择计量单位
				if($("#minOrderUnitSelect").val() == " "){
					$("#minOrderUnitSelect").hide();
					$("#minOrderUnitOther").show();
					$("#otherToSelect").show();
					$("#minOrderUnitOther").val("");
					
					$("[name='minOrderNum']").rules("remove");
					$("[name='minOrderNum']").rules("add", {digits: true, correspond: "#minOrderUnitOther",
				 				messages: {digits: '<s:text name="product.minOrderNum.int" />', correspond:'<s:text name="product.minOrderNum.correspond" />'}});
					$("[name='minOrderUnitSelect']").rules("remove");
					$("[name='minOrderUnitOther']").rules("add", {maxlength:20, correspond: "#minOrderNum", 
							messages: {maxlength:'<s:text name="product.minOrderUnit.maxlength" />', correspond:'<s:text name="product.minOrderUnit.correspond" />'}});
				}
			}
			function otherToSelect(){
				$("#minOrderUnitSelect").show();
				$("#minOrderUnitOther").hide();
				$("#otherToSelect").hide();
				$("#minOrderUnitSelect").val("");
				
				$("[name='minOrderNum']").rules("remove");
				$("[name='minOrderNum']").rules("add", {digits: true, correspond: "#minOrderUnitSelect",
			 			messages: {digits: '<s:text name="product.minOrderNum.int" />', correspond:'<s:text name="product.minOrderNum.correspond" />'}});
				$("[name='minOrderUnitOther']").rules("remove");
				$("[name='minOrderUnitSelect']").rules("add", {correspond: "#minOrderNum", messages: {correspond:'<s:text name="product.minOrderUnit.correspond" />'}});
			}
	
			function loadTag(catId){
				$.ajax({
				   	type: "post",
				    url: "/product/product_tags.do",
				    cache:false, 
				    data: "catId=" + catId,
				    dataType: "json",
				    success:function(response){
				    	if(response != null && response.result != null){
							var buffer = "";
							var num = 0;
							var tagsList = response.result.tagsList;
							if(tagsList != null && tagsList.length > 0){
								num = tagsList.length;
								for (var i = 0; i < tagsList.length; i++) {
									if(tagsList[i].type == 1){
										buffer += "<tr>";
										buffer += "<input type='hidden' name='tagName1' value='" + tagsList[i].tagId + "' />";
										buffer += "<td style='width:227px;padding:0px;line-height:25px;' align='left'>" + tagsList[i].tagName + "</td>";
										buffer += "<td style='width:auto;padding:0;'>";
										if(tagsList[i].tagValueList != null){
											buffer += "<select name='tagValue1' style='width:230px;'>";
											buffer += "<option value='0'><s:text name='product.select'/></option>";
											for (var j = 0; j < tagsList[i].tagValueList.length; j++) {
												var tagValue = tagsList[i].tagValueList[j];
												buffer += "<option value='" + tagValue.valueId + "'>" + tagValue.valueName + "</option>";
											}
										}
										if(tagsList[i].unit != ""){
											buffer += "&nbsp;" + tagsList[i].unit;
										}
										buffer += "</td></tr>";
									}
									if(tagsList[i].type == 2){
										buffer += "<tr>";
										buffer += "<input type='hidden' name='tagName2' value='" + tagsList[i].tagId + "' />";
										buffer += "<td style='width:227px;padding:0px;line-height:25px;' align='left'>" + tagsList[i].tagName + "</td>";
										buffer += "<td style='width:auto;padding:0;'><input type='text' name='tagValue2' style='width:225px;' maxlength='120' />";
										if(tagsList[i].unit != ""){
											buffer += "&nbsp;" + tagsList[i].unit;
										}
										buffer += "</td></tr>";
									}
								}
							}
							$("#proAttrText").html(buffer);
							if(buffer != ""){
								$("#proAttribute").show();
							}else{
								$("#proAttribute").hide();
							}
						}	
				    }
				});	
				$("#customAttribute").show();
			}
			
			function delCheck(obj){
				if($(obj).val() == "请输入属性名" || $(obj).val() == "请输入属性值"){
						$(obj).val("");
					}
				$(obj).css("color", "#000000");
			}
			
			// 添加最多10个自定义标签
			function addTags3(obj){
				var size = $("[name='tagName3']").length;
				if(size >= 10){
					alert("不能再添加了！");
					return false;
				}
				if(size <= 9){
					var buffer = "<br id='br" + (size + 1) + "' /><input onblur='check_tag3(this)' value='请输入属性名' onclick='delCheck(this);'  type='text' id='customTagName" + (size + 1) + "'  name='tagName3' style='margin-bottom:2px;width:225px;color:#888888'/>  ";
					buffer += "<input onblur='check_tag3(this)' value='请输入属性值' onclick='delCheck(this)' type=\"text\"  id='customTagValue" + (size + 1) + "' name='tagValue3' style='margin-bottom:2px;width:225px;color:#888888'/>  ";
					
					$(obj).before(buffer);
					if(size == 9){
						$(obj).hide();
					}
				}
			}
			
			var bool_check_tag = true;
			function check_tag3(el) {
				if($.trim(el.value).length == 0) {
					el.value = el.defaultValue;
					if(el.name == 'tagName3') el.value = '请输入属性名';
					else el.value = "请输入属性值";
					el.style.color = "#888888";
				}
				var error = '';
				$(":input[name='tagName3']").each(function(){
					bool_check_tag = true;
					if ($.trim(this.value).length > 0 && ($.trim($("#customTagValue"+/\d+/.exec(this.id)).val()).length == 0 || $("#customTagValue"+/\d+/.exec(this.id)).val() == '请输入属性值') && $.trim(this.value)!="请输入属性名") {
						error += "请输入相应的属性值<br>";
						bool_check_tag = false;
						return false;
					} else if (($.trim(this.value).length == 0 || this.value == '请输入属性名') && $.trim($("#customTagValue"+/\d+/.exec(this.id)).val()).length > 0 && $.trim($("#customTagValue"+/\d+/.exec(this.id)).val())!="请输入属性值") {	
						error += "请输入相应的属性名<br>";
						bool_check_tag = false;
						return false;
					}else if($(":input[name='tagName3'][value='"+$.trim(this.value)+"']").length > 1 && this.value!="请输入属性名") {
						error += "属性名不能相同<br>";
						bool_check_tag = false;
						return false;
					}else if($.trim(this.value).indexOf("-") > -1 || $.trim(this.value).indexOf("，") > -1 || $.trim(this.value).indexOf(",") > -1 || $.trim($("#customTagValue"+/\d+/.exec(this.id)).val()).indexOf("-") > -1 || $.trim($("#customTagValue"+/\d+/.exec(this.id)).val()).indexOf("，") > -1 || $.trim($("#customTagValue"+/\d+/.exec(this.id)).val()).indexOf(",") > -1) {
						error += "自定义属性不可以包含“，”和“-”两种符号<br>";
						bool_check_tag = false;
						return false;
						}
				});
				if(!bool_check_tag) { 
					$("#tagValueTip").css('color','red').html(error);
					return false;
				}else{
					$("#tagValueTip").css('color','red').html("");
					return true;
					}
				
			}

			// 存在对应商情时
			function isModifyTrade(proId){
				if(confirm('<s:text name="product.tradeExist"/>')){
					location.href = "/trade/trade_edit.htm?proId=" + proId;
				}
			}
			
			function checkProductForm(){
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				var regDescFilter = /(&nbsp;|\<br \/\>)/gi;
				if(fckeditor){
					$('#description').val(FCKeditor_CleanWord(FCKeditorAPI.GetInstance('description').GetXHTML()));
					var desc=$('#description').val();
					if ($.trim(FCKeditorAPI.GetInstance('description').GetXHTML().replace(regDescFilter,'')).length == 0) {
						$("#description_tip").html("<label class='error'>请填写产品详细描述。</label>");
						$('html,body').animate({scrollTop: $("#description_th").offset().top}, 500);
						return false;
					} else {
						$("#description_tip").html("");
					}
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
				    				alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交!  \n 错误信息："+showlist2[j]);
				    				return false;
				    			}
				    		}
				    	}
				    }
				}
				if(!briefCheck()) {
					return false;
				} 
				// 产地选择时，清空place字段
				if($("#notChina").css("display") == "none"){
					$("#place").val("");
				}
				if($("#minOrderUnitOther").css("display") == "none"){
					$("#minOrderUnitOther").val("");
				}

				var tagName = $(":input[name='tagName3']");
				var tagValue = $(":input[name='tagValue3']");
				
				if(!bool_check_tag) {
					var result = true;
					for(var i=0; i<tagName.length;i++){
							if("请输入属性值"==$(tagValue[i]).val()){
								$(tagValue[i]).trigger("click");
								$(tagValue[i]).focus();
								return false;
							}
							if("请输入属性名"==$(tagName[i]).val()){
								$(tagName[i]).trigger("click");
								$(tagName[i]).focus();
								return false;
							}
					}
					return false;
				}
				document.getElementById('btn_submit').disabled = true;
				if (!issubmit) {
					if ($("#productForm").validate().form()) {
						issubmit = true;
					}
					$("#productForm").submit();
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
			    if (showlist2 != null && showlist2.length > 0 ) {
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
	<div id="chek"></div>
		<div class="buttonLeft">
			<s:if test="result.company.memberType > 1">
			<input type="button" onclick="window.location.href='/product/product_batch_add.htm'" value="批量上传产品" />
				<input type="button" onclick="window.location.href='/newproduct/new_product_add.htm'" value="<s:text name="button.addNewProduct" />" />
			</s:if>
			<s:if test="result.product.proId > 0">
				<input type="button" onclick="window.location.href='/product/product_add.htm?proId=<s:property value="result.product.proId" />'" value="<s:text name="button.addSameProduct" />" />
				<s:if test="result.copyProId != null && result.copyProId > 0">
					<input onclick='isModifyTrade(<s:property value="result.copyProId" />)' type='button' value='<s:text name="button.toTrade" />' />
				</s:if>
				<s:else>
					<input type="button" onclick="window.location.href='/trade/trade_add.htm?copyProId=<s:property value="result.product.proId" />'" value="<s:text name="button.toTrade" />" />
				</s:else>
				<input type="button" onclick="window.location.href='/product/product_delete.do?proId=<s:property value="result.product.proId" />'" value="<s:text name="button.deleteProduct" />" />
			</s:if>
		</div>
		
		<form id="productForm" name="productForm" method="post" enctype="multipart/form-data" >
			<input type="hidden" name="proId" value="<s:property value='result.product.proId'/>" />
			<input type="hidden" id="videoId" name="videoId" value="<s:property value='result.product.videoId'/>" />
			<input type="hidden" id="countryCode" name="countryCode" value="<s:property value='result.product.countryCode'/>" />
			<input type="hidden" name="oldGroupId" value="<s:property value='result.product.groupId'/>" />
			<input type="hidden" name="oldCatId" value="<s:property value='result.product.catId'/>" />
			<s:iterator value="result.product.specialGroupMap" >
				<input type="hidden" name="oldSpecialGroupId" value="<s:property value='key'/>" />
			</s:iterator>
			<input type="hidden" name="oldKeyword" value="<s:property value='result.product.keywords'/>" />
			<input type="hidden" name="addTrade" value="<s:property value='result.product.addTrade'/>" />
			<input type="hidden" name="optimizeProduct" value="<s:property value='result.product.optimizeProduct'/>" />
			<input type="hidden" id="foodPact_hidden" name="foodPact" />
			<input type="hidden" id="catId" name="catId" value="<s:property value='result.product.catId'/>" />
			<input type="hidden" id="groupId" name="groupId" value="<s:property value='result.product.groupId'/>" />
			<input type="hidden" id="filePath" name="filePath" value="<s:property value="result.product.filePath"/>"/>
			
			<table class="formTable">
				<tbody>
					<tr>
						<td class="subtitle" colspan="2">
							<span class="h1"><s:text name="product.base" /></span>
						</td>
					</tr>
					<tr>
						<th><span class="red">*</span><s:text name="product.catId" />：</th>
						<td>
							<input type="text" id="catName" name="catName" value="<s:property value='result.product.catName'/>" style="width:350px" readonly />
							<input type="button" id="catSimilarSelect" value="<s:text name='button.selectSimilar'/>" />
							<input type="button" id="_cat" value="<s:text name="button.selectCat" />" />
							<br />
							<span class="fieldTips">
								<s:text name="product.catNotice" />
							</span>
							<div id="catNameTip" ></div>
							<div id="_catList" style="z-index: 2"></div>
						</td>
					</tr>
					<%-- 产品属性 --%>
					<tr id="proAttribute" style="display:none;">
						<th><s:text name="product.proAttribute"/>：</th>
						<td>
							<table id="proAttrText" style="width:auto;">
								<s:iterator value="result.product.tagList" id="tag">
									<s:if test="#tag.type == 1">
										<tr>
											<input name="tagName1" type="hidden" value="<s:property value='#tag.tagId'/>"/>
											<td style="width:227px;padding:0px;line-height:25px;" align="left">
												<s:property value='#tag.tagName'/>
										  	</td>
											<td style="width:auto;padding:0;">
												<select name="tagValue1" style="width:230px;">
													<option value="0"><s:text name="product.select"/></option>
														<s:iterator value="#tag.tagValueList" id="tagValue">
															<option value="<s:property value='#tagValue.valueId'/>" <s:if test="#tagValue.valueId == #tag.tagValueId">selected</s:if>><s:property value='#tagValue.valueName'/></option>
														</s:iterator>
												</select>
												<s:property value='#tag.unit'/>
											</td>
										</tr>
									</s:if>
									<s:if test="#tag.type == 2">
										<tr>
											<input name="tagName2" type="hidden" value="<s:property value='#tag.tagId'/>"/>	
											<td style="width:227px;padding:0px;line-height:25px;" align="left">
												<s:property value='#tag.tagName'/>
											</td>
											<td style="width:auto;padding:0;">
												<input name="tagValue2" type="text" value="<s:property value='#tag.tagValueName'/>" style="width:225px;" maxlength="120" />
												<s:property value="#tag.unit" />
											</td>
										</tr>
									</s:if>
								</s:iterator>
							</table>
						</td>
					</tr>
					<%-- 自定义属性 --%>
					<tr id="customAttribute" style="display:none;">
						<th><s:text name="product.customAttribute"/>：</th>
						<td id="customAttrText">
							<div style="width:600px;">
								<s:set name="num" value="1" />
								<s:if test="result.product.tagList != null && result.product.tagList.size > 0">
									<s:iterator value="result.product.tagList" id="tag" status="st">
										<s:if test="#tag.type == 3">
											<input onclick="delCheck(this)" onblur="check_tag3(this)" id="customTagName<s:property value='#num'/>" name="tagName3" style="margin-bottom:2px;width:225px;" value="${tag.tagName}" />
										  	<input onclick="delCheck(this)" onblur="check_tag3(this)" id="customTagValue<s:property value='#num'/>" name="tagValue3" style="margin-bottom:2px;width:225px;" value="${tag.tagValueName}" />
										  	<s:set name="num" value="#num + 1" />						
										</s:if>
									</s:iterator>
								</s:if>
								<s:if test="#num == 1">	
									<input onblur="check_tag3(this)" value="请输入属性名" type="text" id="customTagName1" name="tagName3" style="margin-bottom:2px;width:225px;color: #888888;" />
									<input onblur="check_tag3(this)" value="请输入属性值" type="text" id="customTagValue1" name="tagValue3" style="margin-bottom:2px;width:225px;color: #888888;" />
								</s:if>
								<s:if test="#num < 11">	
									<input type="button" onclick="addTags3(this)"value="<s:text name="button.add" />" style="margin-left: 0px;"  />
								</s:if>
								<span id="moreParametertooltip"></span>	
								<div id="tagValueTip"></div>
								<script>
									// 非验证错误
									<s:if test="hasActionErrors() == false && hasFieldErrors() == false && hasActionMessages() == false">
										// 产品添加
										<s:if test="result.product.proId == 0 ">
											$("#productForm").attr("action", "/product/product_add_submit.htm");
										
											<s:if test="result.product.catId > 0">
												//loadTag(<s:property value='result.product.catId'/>);
												<s:if test="result.product.catId > 0">
													<s:if test="result.product.tagList.size > 0">
														$("#proAttribute").show();
													</s:if>
												$("#customAttribute").show();
												</s:if>
											</s:if>
											<s:else>
												if($("#catId").val() > 0){
													loadTag($("#catId").val());
												}
											</s:else>
										</s:if>
										<s:else>
											$("#productForm").attr("action", "/product/product_edit_submit.htm");
											<s:if test="result.product.catId > 0">
												<s:if test="result.product.tagList.size > 0">
													$("#proAttribute").show();
												</s:if>
												$("#customAttribute").show();
											</s:if>
										</s:else>
									</s:if>
									<s:else>
										<s:if test="result.product.tagList.size > 0">
											$("#proAttribute").show();
										</s:if>
										$("#customAttribute").show();
									</s:else>	
									
									$("#customTagName1").click(function(){
										if($(this).val() == "请输入属性名"){
											$(this).val("");
										}
										$(this).css("color", "#000000");
									});
									$("#customTagValue1").click(function(){
										if(this.value == "请输入属性值"){
											$(this).val("");
										}
										$(this).css("color", "#000000");
									});
								</script>
							</div>
							<div style="color: #888888">注：自定义属性不能输入“，”和“-”两种符号。</div>
						</td>
					</tr>
					<tr>
						<th><s:text name="product.model" />：</th>
						<td>
							<input name="model" id="model" value="<s:property value='result.product.model'/>" style="width:240px"/>
						</td>
					</tr>
					<tr id="origin">
						<th><s:text name="product.origin" />：</th>
						<td>
							<span id="china" <s:if test="result.product.place != null && result.product.place != ''">style="display:none;"</s:if>>
								<select id="province" name="province"></select>
								<select id="city" name="city"></select>
								<select id="town" name="town"></select><%--
								<a href="#position;" id="chinaToNot"><s:text name="product.other" /></a>
								--%><span id="cityTip"></span>
							</span>
							<span id="notChina" <s:if test="result.product.place == null || result.product.place == ''">style="display:none;"</s:if>>
								<input id="place" name="place" value="<s:property value='result.product.place'/>" />
								<a href="#position;" id="toChina"><s:text name="product.china" /></a>
								<span id="placeTip"></span>
							</span>
						</td>
					</tr>
					<tr>
						<th>
							<span class="red">*</span><s:text name="product.proName" />：<br />
						</th>
						<td class="fieldTips">
							<input type="text" name="proName" value="<s:property value='result.product.proName'/>" style="width:320px"/>
							<s:if test="result.product.optimizeProduct">
								<br />
								<input type="checkbox" id="autoSearch" style="margin-top:5px;margin-right:5px;"/>
								<s:text name="product.keywordsAuto" />
							</s:if>
							<span id="proNametooltip"></span>
						</td>
					</tr>
					<tr>
						<th><s:text name="product.keywords" />：<br /><span style="font-size: 12px;padding-right: 12px;font-weight: normal">(会员网站产品名)</span>
						</th>
						<td><div style="position: relative;">
							<s:if test="result.product.optimizeProduct">
								<s:if test="(result.product.keyword2 != '' && result.product.keyword2.length > 0) || (result.product.keyword3 != '' && result.product.keyword3.length > 0)">1.</s:if>
								<input name="keyword1" id="keyword1" value="<s:property value='result.product.keyword1'/>" style="width:240px;"/>
								<br />
								<s:if test="result.product.keyword2.length > 0">2. <input onblur="checkKeyword();" name="keyword2" id="keyword2" value="<s:property value='result.product.keyword2'/>" style="width:240px;"/><br /></s:if>
								<s:if test="result.product.keyword3.length > 0">3. <input onblur="checkKeyword();" name="keyword3" id="keyword3" value="<s:property value='result.product.keyword3'/>" style="width:240px;"/><br /></s:if>
								<div id="keywordList" style="background-color: #FFFFFF; border: 1px solid #000000;color: #000000;
   															 left: 0;line-height: 15px;margin-top: 0; position: absolute;width: 240px;  display: none;z-index: 10;"></div>
							</div>
								所有产品的关键词均不能重复。<span id="keywordstooltip"></span>
							</td>
							</s:if>
							<s:else>
								<s:text name="product.keywordsFull" />
								</div>
							</s:else>
					</tr>
					<tr>
						<th>
							<s:text name="product.imgPath" />：
						</th>
						<td class="fieldTips">
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
							<s:text name="product.imgPaths" /><input type="checkbox" id="proAddImg"/><span style="margin-left:4px" id="phototooltip"></span>
							<span id="proAddImgDiv" style="display:none;">
								<%
									imgPathTag = "proAddImg";
									int maxNum = 5;
								%>
								<s:set name="images" value="result.product.imgPaths" />
								<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
								<s:set name="imgSize" value="'100'" />
								<%@ include file="/page/inc/image_multi.jsp" %>
							</span>
							<%@ include file="/page/product/inc/watermark.jsp" %>
						</td>
					</tr>
					<s:if test="loginUser.memberType == 2">
					<tr>
						<th><s:text name="product.videoId" />：</th>
						<td>
							<s:set name="videoImgPath" value="result.product.videoImgPath"></s:set>
							<s:set name="playPath" value="result.product.playPath"></s:set>
							<s:set name="videoState" value="result.product.videoState"></s:set>
							<%@ include file="/page/inc/video_bar.jsp"%>
						</td>
					</tr>
					</s:if>
						<tr>
						<th><span class="red">*</span><s:text name="product.brief" />：</th>
						<td class="fieldTips">
							<textarea onblur="briefCheck()" id="brief" style="width: 450px; height: 70px;" name="brief"><s:property value='result.product.brief'/></textarea><br />
							剩余<span class="red" id="briefNum">150</span>个字符。不支持HTML代码。<span id="breftooltip"></span>
							<div id="briefTip"></div>
						</td>
					</tr>
					<tr>
						<th id="description_th"><span class="red">*</span><s:text name="product.description"/>：</th>
						<td>
						<a name="description_error" id="description_error"></a>
						<input id="fckButton" type="button" value="<s:text name="button.fckEdit" />" />
							<div id="descriptionDiv" style="width:600px;"></div>
							<textarea name="description" id="description" style="display:none;"><s:property value='result.product.description' escape="false"/></textarea>
							<span class="fieldTips">编辑框内HTML源代码最多10000个字符。<span id="descriptiontooltip"></span></span>
							<div id="description_tip"></div>
						</td>
					</tr>
					<tr>
						<th><s:text name="product.groupId" />：</th>
						<td class="fieldTips">
							<input type="text" id="groupName" value="<s:property value='result.product.groupName'/>" style="width:350px" readonly />
							<input type="button" id="groupSelect" value="<s:text name='button.selectGroup' />" />
							<input type="button" id="groupRemove" value="<s:text name='button.remove' />" /><br />
							<s:text name="product.groupNotice1" /><s:if test="loginUser.showGroup == true"><s:text name="product.groupNotice2" /><a id="groupAdd" href="#position"><s:text name="button.create" /></a></s:if>。
							<span id="generaltooltip"></span>
						</td>
					</tr>
					<tr>
						<th><s:text name="product.specialGroupId" />：</th>
						<td>
							<div id="specialGroupId" class="options" <s:if test="result.product.specialGroupMap.size ==0">style="display:none;"</s:if>>
								<ul>
									<s:iterator value="result.product.specialGroupMap" >
									<li>
										<input type="hidden" name="specialGroupId" value="<s:property value='key'/>" />
										<input type="checkbox" name="specialGroupBox" />&nbsp;<s:property value='value'/>
									</li>
									</s:iterator>
								</ul>
							</div>
							<input type="button" id="specialGroupSelect" value="<s:text name='button.selectSpecialGroup' />" />
							<input type="button" id="specialGroupRemove" value="<s:text name='button.remove' />" /><br />
							<span  class="fieldTips">
								<s:text name="product.specialGroupNotice1" /><s:if test="loginUser.showGroup == true"><s:text name="product.specialGroupNotice2" /><a id="specialGroupAdd" href="#position"><s:text name="button.create" /></a></s:if>。<span id="specialtooltip"></span>
							</span> 
						</td>
					</tr>
					<tr>
						<th><s:text name="attachment"/>：</th>
						<td class="fieldTips">
							<div id="filePathSelect" <s:if test="result.product.filePath != null && result.product.filePath != ''">style="display:none;"</s:if>>
								<div style="padding-left:1px;">
									<s:text name="product.filePathText"/>
									<input type="checkbox" id="check_3" onclick="$('#filePathUpload').toggle()" />
								</div>
								<div id="filePathUpload" style="display:none;">
									<table width="100%" border="0" align="center" cellpadding="5" cellspacing="1">
										<tr>
											<td>
												<input type="file" name="attachment" value=""/>
												<div class="gray">
													-&nbsp;<s:text name="attachment.format"><s:param>jpg、gif、txt、doc、pdf、xls</s:param><s:param>1024</s:param></s:text>
												</div>
											</td>
										</tr>
									</table>
								</div>
							</div>
							<s:if test="result.product.filePath != null && result.product.filePath != ''">
								<a id="filePathA" href="<s:property value='result.product.filePathUrl'/>" target="_blank" ><s:property value='result.product.filePathUrl'/></a>&nbsp;&nbsp;
								<input type="button" id="clearFile" value="<s:text name="button.delete" />" onclick="$('#filePathA').hide();$(this).hide();$('#filePathSelect').show();$('#filePath').val('');">	
							</s:if>
						</td>
					</tr>
					<tr>
						<td class="subtitle" colspan="2">
							<span class="h1">
								<span id="tradeDiv" style="cursor:pointer;">
									<img id="tradeImg" height="20" align="absmiddle" width="19" src="/img/ico/switch_open.gif"/>
									<s:text name="product.trade"/>
								</span>
							</span>
						</td>
					</tr>
					<tr name="tradeDiv">
						<th><s:text name="product.price"/>：</th>
						<td>
							<input type="text" id="price1" name="price1" value="<s:property value='result.product.price1Text'/>" style="float:left;"/><span style="float:left;">&nbsp;~&nbsp;</span>
							<input type="text" id="price2" name="price2" value="<s:property value='result.product.price2Text'/>" style="float:left;"/>&nbsp;元
							<div id="priceTip" style="clear:both;"></div>
						</td>
					</tr>
					<tr name="tradeDiv">
						<th><s:text name="product.paymentType"/>：</th>
						<td>
							<input type="text" id="paymentType" name="paymentType" value="<s:property value='result.product.paymentType'/>" />
						</td>
					</tr>
					<tr name="tradeDiv">
						<th><s:text name="product.minOrder"/>：</th>
						<td>
							<input type="text" id="minOrderNum" name="minOrderNum" value="<s:property value='result.product.minOrderNum'/>" />
							<select id="minOrderUnitSelect" name="minOrderUnitSelect" onchange="selectToOther();" <s:if test="result.product.minOrderUnitOthers == 'true'">style="display:none"</s:if> >
				                <option value=""><s:text name="product.minOrderUnit" /></option>
				                <s:iterator value="result.product.minOrderUnitList" id="unit">
				                	<option value="<s:property value='#unit'/>" <s:if test="#unit == result.product.minOrderUnit">selected</s:if>><s:property value='#unit'/></option>
				                </s:iterator>
                                <option value=" "><s:text name="product.other" /></option>
                            </select>
							<input type="text" id="minOrderUnitOther" name="minOrderUnitOther" value="<s:property value='result.product.minOrderUnit'/>" maxlength="20" style="width:80px;<s:if test="result.product.minOrderUnitOthers == 'false'">display:none</s:if>"/>&nbsp;
							<a href="javascript:otherToSelect();" id="otherToSelect" <s:if test="result.product.minOrderUnitOthers == 'false'">style="display:none;"</s:if>><s:text name="button.select"/></a>
							<span id="minOrderTip"></span>
						</td>
					</tr>
					<tr name="tradeDiv">
						<th><s:text name="product.transportation"/>：</th>
						<td>
							<input type="text" id="transportation" name="transportation" value="<s:property value='result.product.transportation'/>" />
						</td>
					</tr>
					<tr>
						<td class="subtitle" colspan="2">
							<span class="h1">
								<span id="suppliableDiv" style="cursor:pointer;">
									<img id="suppliableImg" height="20" align="absmiddle" width="19" src="/img/ico/switch_open.gif"/>
									<s:text name="product.suppliable"/>
								</span>
							</span>
						</td>
					</tr>
					<tr name="suppliableDiv">
						<th><s:text name="product.productivity"/>：</th>
						<td>
							<input type="text" id="productivity" name="productivity" value="<s:property value='result.product.productivity'/>" />
						</td>
					</tr>
					<tr name="suppliableDiv">
						<th><s:text name="product.packing"/>：</th>
						<td>
							<input type="text" id="packing" name="packing" value="<s:property value='result.product.packing'/>" />
						</td>
					</tr>
					<tr name="suppliableDiv">
						<th><s:text name="product.deliveryDate"/>：</th>
						<td>
							<input type="text" id="deliveryDate" name="deliveryDate" value="<s:property value='result.product.deliveryDate'/>" />
						</td>
					</tr>
				</tbody>
			</table>
			<div class="buttonCenter">
				<input id="btn_submit" type="button" value="<s:text name='button.submit' />" onclick="checkProductForm()"/>
				<input type="reset" value="<s:text name='button.reset' />"/>
			</div>
		</form>
		
		<div id="selectImgDialog"></div>
		<div id="productDialog"></div>
		<div id="specialGroupDialog"></div>
		<div id="groupAddDialog"></div>
		<div id="groupList" title='<s:text name='button.selectGroup' />'>
			<iframe src='/product/select_group_list.do' frameborder="no" scrolling="yes" style="width:100%; height:420px;*height:450px !important;*height:450px;"></iframe>
		</div>
		<%@ include file="/page/inc/image_error.jsp" %>
		<%@ include file="/page/product/inc/food_pack.jsp" %>
	</body>
</html>