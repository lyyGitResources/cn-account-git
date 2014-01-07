<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>网站设计<s:property value="result.stateName" escape="false"/></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="/js/iColorPicker.js"></script>
		<script type="text/javascript">
		$(document).ready(function () {
			<s:if test="location=='template'">selectTemplate();</s:if>
			<s:if test="result.modSelector != ''">
				$('<s:property value="result.modSelector"/>').each(function(){
					this.checked = true;
				});
			</s:if>
			$("#template div div").each(function(){
				$(this).click(function(){
					$("#template div").find("ul").each(function(){$(this).hide();});
					$(this).children().show();
					$("#template div div").each(function(i){$(this).removeClass("div2");if(i==2){$(this).addClass("div3");}});
					$(this).removeClass("div3").addClass("div2");
				});
			});
			if ($("#bannerSet").val() > 1) {
				$("#nbanner").show();
			} else {
				$("#nbanner").hide();
			}
			
			<s:if test="result.reject">
				$("#adminLogDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 200,
					width: 360,
					modal: true,
					title: '<s:text name="auditState.rejectRemark"/>'
				});
			</s:if>
			
			$("#adminLog").click(function(){
				$("#adminLogDialog").dialog('open');
			});
			if(maxNum > 1){
				$(".numTip").text("提示: 该模板最多可上传" + maxNum +"张自定义banner图片");
			}
			
			var bannerSrc = $.trim($("#bannerImgSrc").attr("src"));
			if (bannerSrc == "" || bannerSrc == "/img/tmp.jpg" || bannerSrc == "/img/tmp-home.jpg") {
				$("#bannerMulti").hide();
			}
			<s:if test="result.webSite.cnFontType == ''">
				$("#fontType").val('SimSun');
			</s:if>
			<s:if test="result.webSite.cnFontSize == ''">
				$("#fontSize").val('30');
			</s:if>
			$("#fontType").change(function(){
				setCnFontType($(this).val());
			});
		
			$("#fontSize").change(function(){
				setCnFontSize($(this).val());
			});
			<s:if test="result.isNewTemplate"> //设置新模板默认选中模块
				$("#m_company_info, #m_product_newlist, #m_contact").attr({checked : "checked", disabled :"disabled"});
			</s:if>
			<s:if test="result.webSite.templateNo == 90  || result.webSite.templateNo == 91">
			$("#m_company_info, #m_product_newlist, #m_contact, #m_feature_product").attr({checked : "checked", disabled :"disabled"});
			</s:if>
		});
		var maxNum = <s:property value="result.bannerMaxNum"/>; //设置上传banner最大数量 
		var bannerBase = '/css/banner';
		var isSaved = true;
		function beginTip(){
			action("li_tip","tip");
			$(".main").attr("style","");
		}
		function selectTemplate(){
			action("li_template","template");
			$(".main").attr("style","");
		}
		function selectLayout(){
			action("li_layout","layout");
			$(".main").attr("style","padding-left:50px; width:671px;");
		}
		function selectMod(){
			action("li_mod","mod");
			$(".main").attr("style","padding-left:50px; width:671px;");
		}
		function setCnFontType(type) {
			$(".comName").css("font-family", type);	
			$("#cnFontType").val(type);
		}
		function setCnFontSize(value) {
			$(".comName").css("font-size", value + "px");
			$("#cnFontSize").val(value);
		}
		function selectComSet(){
			action("li_comSet","comSet")
			$(".main").attr("style","padding-left:50px; width:671px;");
		}
		function action(currentId,contentId){
			$("#select ul li").each(function(i){$(this).removeClass("current");})
			$("#"+currentId).addClass("current");
			$(".main > div").hide();
			$("#"+contentId).show();
		}
		function setLayoutNo(img,num){
			isSaved = false;
			var rejectLayoutNoArray = $("#rejectLayoutNo").val().split(',');
			for(var rejectLayoutNo in rejectLayoutNoArray){
				if(num == rejectLayoutNoArray[rejectLayoutNo]){
					alert("该版式不适合第"+$("#templateNo").val()+"套模板，请重新选择");
					return;
				}
			}

			$("#layout > div img").each(function(i){
				this.src="/img/bg/0"+(i+1)+".jpg";
			});
			$(img).attr("src","/img/bg/0"+num+"_1.jpg");
			$("#layoutNo").val(num);
		}
		function setBannerNo(no){
			isSaved = false;
			$("#banner ul li").each(function(){
				$(this).removeClass("current");
			});
			$("#bannerNo_0").removeClass("current");
			$("#bannerNo_00").removeClass("current");
			$("#bannerNo_"+no).addClass("current");
			$("#bannerNo").val(no);
			if (no == 0) {
				$("#bannerNo_00").addClass("current");
			}
		}
		function getTemplateList(span,pageNo){
			$(".smallImg > li").hide();
			$("li[name=\"pageNo_"+pageNo+"\"]").show();
			$("div .pageNav>span").removeClass("current");
			$(span).addClass("current");
		}
		//rejectLayoutNo1 = '2,3';如：setTemplate(35,200,200,1022,320,2,2,3)
		function setTemplate(templateNo,bannerNum,newHome,bannerWidth,bannerHeight,nbannerWidth,nbannerHeight,bannerSet,rejectLayoutNo,rejectLayoutNo1){
			isSaved = false;
			var layoutNo = $("#layoutNo").val();
			if(layoutNo.indexOf(rejectLayoutNo)>-1){
				alert("该模板不适合第"+layoutNo+"套版式，请重新选择");
				return;
			};
			if(rejectLayoutNo1 != "undefined"){
				rejectLayoutNo = rejectLayoutNo + "," + rejectLayoutNo1;
				if(layoutNo.indexOf(rejectLayoutNo1)>-1){
					alert("该模板不适合第"+layoutNo+"套版式，请重新选择");
					return;
				};
			}
			$(".bigImg img").attr("src",bannerBase+"/style-"+templateNo+"/images/banner/preview-s.jpg");
			$(".smallImg li").each(function(){
				$(this).removeClass("current");
			});
			$("#templateNo_"+templateNo).addClass("current");
			$("#banner li img").each(function(i){
				this.src=bannerBase+"/style-"+templateNo+"/images/banner/banner"+(i+1)+"s.jpg";
			});
			$("#bannerWH").text(""+bannerWidth+"(宽) × "+bannerHeight+"(高)");
			$("#templateNo").val(templateNo);
			$("#rejectLayoutNo").val(rejectLayoutNo);
			$("#bannerSet").val(bannerSet);
			var bannerImgSrc = $("#bannerImgSrc").attr("src");
			if (bannerSet > 1) {
				$("#nbanner").show();
				if(bannerImgSrc != "" && bannerImgSrc == "/img/tmp.jpg"){
					$("#bannerImgSrc").attr("src","/img/tmp-home.jpg");
				}
				$("#nbannerWH").text(""+nbannerWidth+"(宽) × "+nbannerHeight+"(高)");
			} else {
				$("#nbanner").hide();
				if(bannerImgSrc != "" && bannerImgSrc == "/img/tmp-home.jpg"){
					$("#bannerImgSrc").attr("src","/img/tmp.jpg");
				}
			}
			setBannerNo(1);
			maxNum = bannerNum;//根据模板的不同改变banner上传数量
			if (maxNum == 1) {
				$("#bannerMulti").hide();
				$(".numTip").hide();
			} else if (bannerNum > 1){
				if ($("[name='_multiDiv']").size()== 0) {
					$("#bannerMulti").hide();
				} else {
					$("#bannerMulti").show();
				}
				$(".numTip").text("提示: 该模板最多可上传" + maxNum +"张自定义banner图片");
			}
			if (newHome) {
				$("#m_company_info, #m_product_newlist, #m_contact").attr({checked : "checked", disabled :"disabled"});
				$("#m_feature_product").removeAttr("disabled");
			} else if (templateNo == 90 || templateNo == 91) {
				$("#m_company_info, #m_product_newlist, #m_contact, #m_feature_product").attr({checked : "checked", disabled :"disabled"});
			} else {
				$("#m_company_info, #m_product_newlist, #m_contact, #m_feature_product").removeAttr("disabled");
			}
			//切换模板重置公司名称样式
			resetComStyle();
		}
		function resetComStyle(){
			$("#fontType").val('SimSun');
			$("#fontSize").val('30');
			$("#compNameId").css("font-family","SimSun").css("font-size","30px").css("color","black");
			$("#cnFontType, #cnFontSize, #cnFontColor, #colorEnter").each(function() {
				$(this).val("");
			});
		}
		function getCheckBoxValue(){
			var modValue = new Array();
			var mGValue = new Array();
			$("input[name='mod']").each(function(){
				if(this.checked){
					modValue.push(this.value);
				}
			});
			$("input[name='mG_checkbox']").each(function(){
				if(this.checked){
					mGValue.push(this.value);
				}
			});
			return {modStr:modValue.join(","),mGStr:mGValue.join(",")}
		}
		function setCheckBoxValue(){
			$("#modules").val(getCheckBoxValue().modStr);
			$("#menuGroupIds").val(getCheckBoxValue().mGStr);
		}
		function preview(){
			var bannerImgSrc = $.trim($("#bannerImgSrc").attr("src"));
			var nbannerImgSrc = $("#nBannerImgSrc").attr("src");
			var bannerNo = $("#bannerNo").val();
			if (bannerNo == 0) {
				if ((bannerImgSrc == "" || bannerImgSrc == "/img/tmp.jpg" || bannerImgSrc == "/img/tmp-home.jpg") && ($("#bannerSet").val() > 1 && nbannerImgSrc == "/img/tmp-page.jpg")) {
					alert("请上传首页banner和内页banner");
					return;
				}
				if (bannerImgSrc == "" || bannerImgSrc == "/img/tmp.jpg" || bannerImgSrc == "/img/tmp-home.jpg") {
					alert("请上传首页banner");
					return;
				}
				if ($("#bannerSet").val() > 1 && nbannerImgSrc == "/img/tmp-page.jpg") {
					alert("请上传内页banner");
					return;
				}
			}
			
			setCheckBoxValue();
			var bannerPaths = "";
			if ($("[name='bannerAddImg']").size() > 0){
				$("[name='bannerAddImg']").each(function(i){
					if (i > 0) {
						bannerPaths += ",";
					}
					bannerPaths += $(this).val();
				});
			}
			var nbannerPath = $("#nbannerPath").val();
			if (nbannerPath == "/img/tmp-page.jpg" || nbannerPath == "") {
				nbannerPath = "";
			}
			var url ="<s:property value='result.webSite.domain'/>/preview.htm?preview=true&layoutNo="+
			$("#layoutNo").val()+"&templateNo="+$("#templateNo").val()+"&bannerNo="+$("#bannerNo").val()+"&bannerPath="+bannerPaths+
			"&nbannerPath="+nbannerPath+"&modules="+$("#modules").val()+"&menuGroupIds="+$("#menuGroupIds").val();
			var param = "height=800px,width=1024px,left=0px,top=0px,resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=no,location=no";
			window.open(url, "", param);
		}
		function save(){
			var bannerImgSrc = $.trim($("#bannerImgSrc").attr("src"));
			var nbannerImgSrc = $("#nBannerImgSrc").attr("src");
			var bannerNo = $("#bannerNo").val();
			if (bannerNo == 0) {
				if ((bannerImgSrc == "" || bannerImgSrc == "/img/tmp.jpg" || bannerImgSrc == "/img/tmp-home.jpg") && ($("#bannerSet").val() > 1 && nbannerImgSrc == "/img/tmp-page.jpg")) {
					alert("请上传首页banner和内页banner");
					return;
				}
				if (bannerImgSrc == "" || bannerImgSrc == "/img/tmp.jpg" || bannerImgSrc == "/img/tmp-home.jpg") {
					alert("请上传首页banner");
					return;
				}
				if ($("#bannerSet").val() > 1 && nbannerImgSrc == "/img/tmp-page.jpg") {
					alert("请上传内页banner");
					return;
				}
			}
			
			isSaved = true;
			setCheckBoxValue();
			//把要提交的input拖到表单内
			if ($("[name='bannerAddImg']").size() > 0){
				var $bannerArray = $("[name='bannerAddImg']");
				var max;
				$bannerArray.size() >= maxNum ? max = maxNum : max = $bannerArray.size();
				for(var i = 1; i <= max; i++){
					$("#bannerType").after($bannerArray[max-i]);
				}
			}
			$("#designInfo").attr("action","/website/save.htm");

			$("#designInfo").submit();
		}
		function default_(){
			getTemplateList($("#pageNo_1").get(0),1);
			setBannerNo(1);
			setLayoutNo($("#layout_1").get(0),1);
			setTemplate(1,1,950,170,0,0,1,'-1');
			$("input[name='mod']").each(function(){
				this.checked = true;
			});
			$("input[name='mG_checkbox']").each(function(){
				this.checked = false;
			});
		}
		function showBigTemplate(templateNo){
			$("#bigTemplateDiv").show();
			$("#bigTemplateDiv > img").attr("src",bannerBase+"/style-"+templateNo+"/images/banner/preview.jpg");
		}

	</script>
	</head>

	<body>
		<div class="pageTips">
			<span><s:text name="website.feature.notice0"/>:</span>
			<s:if test="result.isReject">
				<div style="color:red;">
					<ul>
						<li>
							<span>您的自定义图片审核未通过，请根据<a id='adminLog' href="javascript:void(0);">未通过原因</a>修改</span>
						</li>
					</ul>
				</div>
			</s:if>			
			<ul>
				<li>
					<img style="border:0;" src='<%=Config.getString("img.base") %>/img/ico/group_new.gif'/>
					为最新添加的风格网站模板
				</li>
				<li><s:text name="website.feature.notice1"/></li>
				<s:if test="!(result.stateName == null || result.stateName == '')">
					<li>审核状态为”审核通过“的自定义图片可正常显示，只有”等待审核“和”审核未通过“的自定义图片才无法显示</li>
				</s:if>
			</ul>
		</div>
		<div class="webDesign">
			<div id="select" class="select">
				<ul>
					<li id="li_tip" class="current">
						<a onfocus="this.blur()" href="javascript:beginTip();">开始选择</a>
					</li>
					<li id="li_template">
						<a onfocus="this.blur()" href="javascript:selectTemplate();">模板</a>
					</li>
					<li id="li_layout">
						<a onfocus="this.blur()" href="javascript:selectLayout();">版式</a>
					</li>
					<li id="li_mod">
						<a onfocus="this.blur()" href="javascript:selectMod();">模块</a>
					</li>
					<li id="li_comSet">
						<a onfocus="this.blur()" href="javascript:selectComSet();">公司名称设置</a>
					</li>
				</ul>
				<div class="main">
					<div id="tip" style="padding-bottom:20px;">
						<div>
							<a href="javascript:selectTemplate();"><img src="/img/bg/bg_19.gif" border="0" /></a>
						</div>
						<div class="line"><img src="/img/bg/bg_22.gif" border="0" /></div>
						<div>
							<a href="javascript:selectLayout();"><img src="/img/bg/bg_20.gif" border="0" /></a>
						</div>
						<div class="line"><img src="/img/bg/bg_22.gif" border="0" /></div>
						<div>
							<a href="javascript:selectMod();"><img src="/img/bg/bg_21.gif" border="0" /></a>
						</div>
					</div>
					<div id="template" class="plate" style="display: none;padding-bottom:20px;">
						<div class="top">
							<div id="template_color" <s:if test="templateColor!=-1">class="div2"</s:if>>
								<s:if test="templateColor==1">红色</s:if>
								<s:elseif test="templateColor==2">黄色</s:elseif>
								<s:elseif test="templateColor==3">白色</s:elseif>
								<s:elseif test="templateColor==4">绿色</s:elseif>
								<s:elseif test="templateColor==5">蓝色</s:elseif>
								<s:elseif test="templateColor==6">橙色</s:elseif>
								<s:elseif test="templateColor==7">灰色</s:elseif>
								<s:elseif test="templateColor==8">紫色</s:elseif>
								<s:elseif test="templateColor==9">黑色</s:elseif>
								<s:else>颜色</s:else>
								<ul style="display: none;">
									<a href="/website/design.htm?location=template&templateColor=1">红色</a>
									<a href="/website/design.htm?location=template&templateColor=2">黄色</a>
									<a href="/website/design.htm?location=template&templateColor=3">白色</a>
									<a href="/website/design.htm?location=template&templateColor=4">绿色</a>
									<a href="/website/design.htm?location=template&templateColor=5">蓝色</a>
									<a href="/website/design.htm?location=template&templateColor=6">橙色</a>
									<a href="/website/design.htm?location=template&templateColor=7">灰色</a>
									<a href="/website/design.htm?location=template&templateColor=8">紫色</a>
									<a href="/website/design.htm?location=template&templateColor=9">黑色</a>
								</ul>
							</div>
							
							<div id="template_type" <s:if test="templateType!=-1">class="div2"</s:if>>
								<s:if test="templateType==1">行业型</s:if>
								<s:elseif test="templateType==2">节日型</s:elseif>
								<s:else>类型</s:else>
								<ul style="display: none;">
									<a href="/website/design.htm?location=template&templateType=1">行业型</a>
									<a href="/website/design.htm?location=template&templateType=2">节日型</a>
								</ul>
							</div>
							<div <s:if test="templateColor==-1&&templateType==-1">class="div2"</s:if>>
								<a href="/website/design.htm?location=template">全部</a>
							</div>
						</div>
						<div class="bigImg">
							<img src="<s:property value="result.bannerBase"/>/style-<s:property value="result.webSite.templateNo"/>/images/banner/preview-s.jpg" />
						</div>
						<div class="smallImg">
							<s:iterator value="result.templateListResult.list" id="template" status="st">
								<li onmouseout="$('#bigTemplateDiv').hide();" onmouseover="showBigTemplate(<s:property value="#template.templateNo"/>);" id="templateNo_<s:property value="#template.templateNo"/>" name="pageNo_<s:property value="#template.getPageNo(#st.getIndex()+1)"/>" 
									<s:if test="#template.templateNo==result.webSite.templateNo"> class="current"</s:if>
									<s:if test="#template.getPageNo(#st.getIndex()+1)!=result.templateListResult.page.pageNo"> style="display:none"</s:if>>
									<img src="<s:property value="result.bannerBase"/>/style-<s:property value="#template.templateNo"/>/images/banner/<s:if test="#template.compareTime > 0">preview-s-new.jpg</s:if><s:else>preview-s.jpg</s:else>" width="78" height="62" onclick="setTemplate(<s:property value="#template.templateNo"/>,<s:property value="#template.bannerNum"/>,<s:property value="#template.newHome"/>,<s:property value="#template.bannerWidth"/>,<s:property value="#template.bannerHeight"/>,<s:property value="#template.insideBannerWidth"/>,<s:property value="#template.insideBannerHeight"/>,<s:property value="#template.newHome ? 2 : 1"/>,<s:if test="#template.rejectLayoutNo==''">'-1'</s:if><s:else><s:property value="#template.rejectLayoutNo"/></s:else>);"/>
									<span>
										<s:property value="#template.templateName"/>
										<s:if test="#st.index lt 5">
										<img style="border:0;" src='<%=Config.getString("img.base") %>/img/ico/group_new.gif'/>
										</s:if>
									</span>
								</li>
							</s:iterator>
						</div>
						<div class="pageNav">
							<s:iterator value="new int[result.templateListResult.page.pageTotal]" status="st">
								<span id="pageNo_<s:property value="#st.index+1"/>" <s:if test="#st.index+1==result.templateListResult.page.pageNo"> class="current"</s:if> onclick="getTemplateList(this,<s:property value="#st.index+1"/>);" ><s:property value="#st.index+1"/></span>
							</s:iterator>						
						</div>
					</div>
					<div id="layout" style="display: none;padding-bottom:30px;">
						<div class="divBox">
							<img id="layout_1" src="<s:if test="result.webSite.layoutNo==1">/img/bg/01_1.jpg</s:if><s:else>/img/bg/01.jpg</s:else>" border="0" onclick="setLayoutNo(this,1);" />
						</div>
						<div class="divBox">
							<img id="layout_2" src="<s:if test="result.webSite.layoutNo==2">/img/bg/02_1.jpg</s:if><s:else>/img/bg/02.jpg</s:else>" border="0" onclick="setLayoutNo(this,2);" />
						</div>
						<div class="divBox">
							<img id="layout_3" src="<s:if test="result.webSite.layoutNo==3">/img/bg/03_1.jpg</s:if><s:else>/img/bg/03.jpg</s:else>" border="0" onclick="setLayoutNo(this,3);" />
						</div>
					</div>
					<div id="mod" class="block" style="display: none;padding-bottom:20px;">
						<p style="width100%; margin-bottom:10px; font-size:20px; color:#000; font-weight: bold;">首页</p>
						<ul style="padding-left:0;">
							<li title="<s:property value="result.moduleTitleMap['company.brief']"/>">
								<img src="/img/ico/ico_11.gif" />
								<span><s:property value="result.moduleTitleMapSub['company.brief']"/></span>
								<input id="m_company_info" name="mod" type="checkbox" value="m_company_info" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['product.feature']"/>">
								<img src="/img/ico/ico_10.gif" />
								<span><s:property value="result.moduleTitleMapSub['product.feature']"/></span>
								<input id="m_feature_product" name="mod" type="checkbox" value="m_feature_product" />
							</li>
							<li title="${result.moduleTitleMap['product.newlist'] }">
								<img src="/img/ico/ico_04.gif" />
								<span>${result.moduleTitleMapSub['product.newlist'] }</span>
								<input id="m_product_newlist" name="mod"  type="checkbox" value="m_product_newlist" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['service.contact']"/>">
								<img src="/img/ico/ico_09.gif" />
								<span><s:property value="result.moduleTitleMapSub['service.contact']"/></span>
								<input id="m_contact" name="mod" type="checkbox" value="m_contact" />
							</li>
						</ul>
						<p style="clear:both; display:block; width100%; margin:10px 0; font-size:20px; color:#000; font-weight: bold;">侧边导航</p>
						<ul style="padding-left:0;">
							<li title="<s:property value="result.moduleTitleMap['product.group']"/>">
								<img src="/img/ico/ico_04.gif" />
								<span><s:property value="result.moduleTitleMapSub['product.group']"/></span>
								<input id="m_product_group" name="mod" type="checkbox" value="m_product_group" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['product.special']"/>">
								<img src="/img/ico/ico_06.gif" />
								<span><s:property value="result.moduleTitleMapSub['product.special']"/></span>
								<input id="m_special_group" name="mod" type="checkbox" value="m_special_group" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['product.hot'] " />">
								<img style="margin-top:3px" src="/img/ico/hot.gif" />
								<span><s:property value="result.moduleTitleMap['product.hot'] " /></span>
								<input id="m_product_hot" name="mod" type="checkbox" value="m_product_hot" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['video.list']"/>">
								<img src="/img/ico/ico_07.gif" />
								<span><s:property value="result.moduleTitleMapSub['video.list']"/></span>
								<input id="m_video" name="mod" type="checkbox" value="m_video" />
							</li>
							<li style="display:none;">
								<input id="m_trade_group" name="mod" type="checkbox" checked value="m_trade_group" />
							</li>
							<li style="display:none;">
								<input id="m_menu_group" name="mod" type="checkbox" checked value="m_menu_group" />
							</li>
							<li style="display:none;">
								<input id="m_comment" name="mod" type="checkbox" checked value="m_comment" />
							</li>
							<li style="display:none;">
								<input id="m_profile" name="mod" type="checkbox" checked value="m_profile" />
							</li>
							<li title="<s:property value="result.moduleTitleMap['service.online']"/>">
								<img src="/img/ico/ico_08.gif" />
								<span><s:property value="result.moduleTitleMapSub['service.online']"/></span>
								<input id="m_online_service" name="mod" type="checkbox" value="m_online_service" />
							</li>
	
							<s:iterator value="result.menuGroupList" id="group" status="st">
								<s:if test="#group.menuCount-#group.menuRejectCount>0">
									<li title="<s:property value='#group.groupName' escape='true'/>">
										<s:if test="#group.listStyle==1"><img src="/img/ico/ico_12.gif" /></s:if><s:else><img src="/img/ico/ico_13.gif" /></s:else>
										<span><s:property value="#group.getShowGroupName()" escape="true"/></span>
										<input name="mG_checkbox" type="checkbox" <s:if test="#group.show==true">checked</s:if> value="<s:property value="#group.groupId"/>" />
									</li>
								</s:if>
							</s:iterator>
						</ul>
					</div>
					<div id="comSet" class="fontSet" style="display:none; padding-bottom:20px; width:100%;">
						<div class="editor">
							<s:select id="fontType" value="%{result.webSite.cnFontType}" 
								list="#{'FZShuTi':'方正舒体', 'FZYaoti':'方正姚体', 'FangSong':'仿宋_GB2312', 'SimHei':'黑体',
								'STCaiyun':'华文彩云', 'STFangsong':'华文仿宋', 'STXihei':'华文细黑', 'STXinwei':'华文新魏',
								'STXingkai':'华文行楷', 'TZhongsong':'华文中宋', 'KaiTi_GB2312':'楷体_GB2312', 'LiSu':'隶书',
								'SimSun':'宋体', '宋体-PUA':'宋体-PUA', '宋体-方正超大字符集':'宋体-方正超大字符集', 'Microsoft YaHei':'微软雅黑',
								'NSimSun':'新宋体', 'YouYuan':'幼圆' }" 
								cssStyle="float:left;width:160px;height:20px;" />
							<s:select id="fontSize" value="%{result.webSite.cnFontSize}" 
								list="#{'24':'24', '26':'26', '28':'28', '30':'30', '32':'32', '34':'34', '36':'36'}" 
								cssStyle="float:left;width:86px;height:20px;margin-left: 5px;" />
							
							<div style="margin-left: 3px; width: 22px;">
								<div id="colorSrc" onclick="iColorShow('fontColor','colorSrc','compNameId','cnFontColor');" style="clear:both; width:20px; font-family:georgia;font-weight: bold;font-size:15px;color:#597197;cursor:pointer;display: block;text-align: center;">A</div>
								<div id="fontColor" class="iColorPicker" name="fontColor"  maxlength="0" style="clear: both; width: 20px; height:2px; overflow:hidden; background-color:<s:if test="result.webSite.cnFontColor == ''">#000</s:if><s:else><s:property value="result.webSite.cnFontColor"/></s:else>;"></div>
							</div>
						</div>
						<div id="compNameId" class="comName" style="width: 100%; fontSize :<s:if test="result.webSite.cnFontSize == ''">30px</s:if><s:else><s:property value="result.webSite.cnFontSize"/>px</s:else>; font-family:<s:if test="result.webSite.cnFontType == ''">'sans-serif','Arial','Verdana'</s:if><s:else>'<s:property value="result.webSite.cnFontType"/>'</s:else>; color: <s:if test="result.webSite.cnFontColor == ''">#000</s:if><s:else><s:property value="result.webSite.cnFontColor"/></s:else>;">
							<s:property value="result.webSite.comName"/>
						</div>
					</div>
				</div>
				<div class="bottom"></div>
			</div>
			<div class="select">
				<div class="top">
					<li class="current">
						选择BANNER
					</li>
				</div>
				<div id="banner" class="banner">
					<ul>
						<s:iterator value="{1,2,3,4,5,6}" id="num">
							<li id="bannerNo_<s:property value="#num"/>" <s:if test="result.webSite.bannerNo==#num"> class="current"</s:if>>
								<img src="/css/banner/style-<s:property value="result.webSite.templateNo"/>/images/banner/banner<s:property value="#num"/>s.jpg" onclick="setBannerNo(<s:property value="#num"/>);"/>
							</li>
						</s:iterator>
					</ul>
					<div id="bannerNo_0" class="Img <s:if test="result.webSite.bannerNo==0">current</s:if>" >
						<img id="bannerImgSrc" src="<s:if test="result.webSite.bannerPath!='/img/tmp.jpg' && result.webSite.bannerPath!='/img/tmp-home.jpg'"><s:property value="result.imgLink"/></s:if>
						<s:property value="result.webSite.bannerPath.split(',')[0]"/>" onclick="setBannerNo(0);"/>
					</div>
					<%	
						int imgType = Image.BANNER;
						String imgSrcTag = "bannerImgSrc";
						String imgPathTag = "bannerAddImg";
						String imgIdTag = "bannerImgId";//为防止image_select.jsp的js错误，在本模块中暂不用
					%>
					<%@ include file="/page/website/banner_upload.jsp" %>
					<div style="clear: both;margin-bottom: 12px; padding-top: 12px;width: 100%;" id="nbanner">
						<div id="bannerNo_00" style="margin-bottom: 1em;" class="Img <s:if test="result.webSite.bannerNo==0">current</s:if>" >
							<img id="nBannerImgSrc" src="<s:if test="result.webSite.nbannerPath!='/img/tmp-page.jpg'"><s:property value="result.imgLink"/></s:if><s:property value="result.webSite.nbannerPath"/>" />
						</div>
						<%@ include file="/page/website/nBanner_upload.jsp" %>
					</div>
				</div>
			</div>
			<div style="text-align: center;">
				<a href="javascript:save();"><img src="/img/bg/bg_27.gif" border="0"/></a>
				<a href="javascript:default_();"><img src="/img/bg/bg_28.gif" border="0"/></a>
				<a href="javascript:preview();"><img src="/img/bg/bg_29.gif" border="0"/></a>
			</div>
			<div class="actionImg" id="bigTemplateDiv" style="display:none;"><img src="" width="400" height="450" /></div>
			<form id="designInfo" name="designInfo" method="post">
				<input type="hidden" id="layoutNo" name="layoutNo" value="<s:property value="result.webSite.layoutNo"/>"/>
				<input type="hidden" id="templateNo" name="templateNo" value="<s:property value="result.webSite.templateNo"/>"/>
				<input type="hidden" id="bannerNo" name="bannerNo" value="<s:property value="result.webSite.bannerNo"/>"/>
				<input type="hidden" id="bannerType" name="bannerType" value="1"/>
				<input type="hidden" id="nbannerPath" name="nbannerPath" value="<s:property value="result.webSite.nbannerPath"/>"/>
				<input type="hidden" id="modules" name="modules" />
				<input type="hidden" id="menuGroupIds" name="menuGroupIds" />
				<input type="hidden" id="cnFontType" name="cnFontType" value="<s:property value="result.webSite.cnFontType"/>"/>
				<input type="hidden" id="cnFontSize" name="cnFontSize" value="<s:property value="result.webSite.cnFontSize"/>"/>
				<input type="hidden" id="cnFontColor" name="cnFontColor" value="<s:property value="result.webSite.cnFontColor"/>"/>
			</form>
						<input type="hidden" id="bannerSet" value="<s:property value="result.bannerSet"/>"/>
			<input type="hidden" id="rejectLayoutNo" value="<s:property value="result.rejectLayoutNo"/>" />
		</div>
		
		<s:if test="result.isReject">
		<div id="adminLogDialog" style="display:none;">
			<table class="listTable" style="width: 330px;">
				<tbody>
					<tr>
						<th style="width:100px;"><s:text name="auditTime"/>：</th>
						<td>
							<s:property value="result.adminLog.createTime" />
						</td>
					</tr>
					<tr>
						<th><s:text name="auditState.rejectRemark"/>：</th>
						<td>
							<s:property value="result.adminLog.remark" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		</s:if>
	</body>
</html>
