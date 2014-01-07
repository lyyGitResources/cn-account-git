<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.commons.util.WebUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="company.editTitle" /><s:property value="result.company.stateName" escape="false"/></title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<style type="text/css">
			.tipsSucceed {
				border: 1px solid #9FC1E7;
				padding-top: 30px;
				padding-left: 30px;
				padding-right: 20px;
				padding-bottom: 30px;
			}
			
			.tipsSucceed .content {
				background: url(/img/ico/suc.gif) no-repeat;
				padding-left: 80px;
				padding-top: 10px;
				line-height: 25px;
				font-size: 14px;
			}
			.tipsSucceed .h1 {
				color: #3A9805;
				font-size: 16px;
				font-weight: bold;
			}
		</style>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript">
			var regNoShowVal = 0; // 0不显示 1显示
			var ceoShowVal = 0; // 0不显示 1显示
			// 扩展验证方法
			jQuery.extend(jQuery.validator.methods, {
				// 是否相同，param中包含自己
				regNoEmpty: function(value, element, param) {
					if (regNoShowVal == 1) {
						if (Trim(value, "g") == "") {
							return false;
						}
					}
					return true;
				},
				ceoEmpty: function(value, element, param) {
					if (ceoShowVal == 1) {
						if (Trim($("#ceo").val(), "g") == "") {
							return false;
						}
					}
					return true;
				},
				notSame: function(value, element, param) {
					var num = 0;
					value = $.trim(value);
					
					$(param).each(function(){
						var otherValue = $.trim($(this).val());
						if(otherValue != "" && value != "" && otherValue == value){
							num++;
						}
					});
					if(num >= 2){
						return false;
					}else{
						return true;
					}
				},
				notAllEmpty: function(value, element, param) {
					var result = false;
					if($.trim(value) != ""){
						result = true;
					}else{
						$(param).each(function(){
							if($.trim($(this).val()) != ""){
								result = true;
							}
						});
					}
					return result;
				}
			});
			var validator = null;
			$(function (){
			//预览效果
				var $pic_dialog = $("#pic_dialog").dialog({
					bgiframe: true,
					autoOpen: false,
					width: 800,
					height: 600,
					modal: true
				});
				$pic_dialog.img = $("#img_preview");
				//  营业执照的预览
				$(".yulan").click(function() {
					if ($("#regImgPath").val()!= "" || $("#regImgPath2").val() != "") {
					var id = $(this).attr("data-img");
					var title = $(this).attr("data-title");
					var yulanDiv1 = "<div id='yulanDiv1' style='border: 1px solid #eee;text-align: center;display: table-cell;vertical-align: middle;*display: block;*font-family:Arial;*font-size: 480px;height: 550px;width: 770px;'><img style='vertical-align: middle;' src='" + $("#regImgPathSrc").attr("src") + "'   onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(240) %>')\"  onload='Util.setImgWH(this, 600, 500)' /></div>";
					var yulanDiv2 = "<br/><div id='yulanDiv2' style='border: 1px solid #eee;text-align: center;display: table-cell;vertical-align: middle;*display: block;*font-family:Arial;*font-size: 480px;height: 550px;width: 770px;'><img style='vertical-align: middle;' src='" + $("#regImgPathSrc2").attr("src") + "'  onerror=\"$(this).attr('src', '<%=WebUtil.getDefaultImgPath(240) %>')\"  onload='Util.setImgWH(this, 600, 500)' /></div>";
					
					if ($("#regImgPath").val() == "") {
						yulanDiv1 ="";
					}
					if ($("#regImgPath2").val() == "") {
						yulanDiv2 ="";
					}
					if ( $("#_multiImgDiv_regImgPath").css("display") == "none") {
						yulanDiv1 ="";
					}
					if ( $("#_multiImgDiv_regImgPath2").css("display") == "none") {
						yulanDiv2 ="";
					}
						$pic_dialog
						.empty()
						.append(yulanDiv1)
						.append(yulanDiv2)
						.dialog("option", "title", title)
						.dialog('open');
					}
				});
				
				//  商标图片、商标注册证书的预览
				$(".chakan").click(function() {
					var id = $(this).attr("data-img");
					var title = $(this).attr("data-title");
					$pic_dialog
						.empty()
						.append("<div style='border: 1px solid #eee;text-align: center;display: table-cell;vertical-align: middle;*display: block;*font-family:Arial;*font-size: 480px;height: 550px;width: 770px;'><img style='vertical-align: middle;' src='" + $("#" + id).attr("src") + "' onload='Util.setImgWH(this, 600, 500)' /></div>")
						.dialog("option", "title", title)
						.dialog('open');
				});
		
				// 绑定文件移除按钮
				$("#_buttonRemove").click(function(){
					$("#logoCertImgSrc").attr("src", "<%=Config.getString("img.default") %>");
					$("#logoCertImg").val("");
				});
				
				$("#_catList").category({
					triId: "_cat",
					catIdHidden: "catId",
					contType: "select",
					maxNum: 3
				});			 
				$("#mainKeywordstooltips").tooltip({
					title:'<s:text name="company.keywords" />',
					detail:'请按照从主到次的顺序输入至少3个主营产品或服务作为行业关键词，这将有助于您网站的整体优化。'
				});
				$("#catIdstooltips").tooltip({
					title:'<s:text name="company.catIds" />',
					detail:'您的公司将被罗列在网站所对应的行业目录下，请选择正确的行业目录。'
				});
				$("#descripTip").tooltip({
					title:'<s:text name="company.description" />',
					detail:'请填写详细的公司介绍，例如历史、业绩、经营范围等，这将有助于采购商们更好的了解您的公司。请不要添加个人联系信息和网址，否则审核人员会退回您的信息！',
					toolTipwidth:300
				});
				$("#faceImgtooltips").tooltip({
					title:'<s:text name="company.faceImg" />',
					detail:'为了更好地向客户展示您的公司形象，我们建议您上传公司形象图或厂房照片，请不要上传个人照片、产品图片或其他不相关的图片，这将影响您公司形象的展示。',
					toolTipwidth:150
				});
				$("#logotooltips").tooltip({
					title:'<s:text name="company.logoImg" />',
					detail:'为了更好地向客户展示您的公司形象，我们建议您上传公司商标，请不要上传个人照片、产品图片或其他不相关的图片，这将影响您公司形象的展示。'
				});
				$("#regtooltips").tooltip({
					title:'<s:text name="company.logoCertImg" />',
					detail:'为配合工商部门的检查工作，即日起，若上传的Logo带R(即通过商标注册的)，请您上传商标注册证书，若未提供证书，我司将自动删除已上传的Logo，不再另行通知',
					toolTipwidth:300
				});
				
				// 子帐号查看
				<s:if test="loginUser.showCompanyEdit == false">
					// 隐藏所有按钮和iframe
					$(":button").hide();
					$("iframe").attr("src", "");
					$("iframe").hide();
					
					// 调整部分样式
					$("#certHeight").css("height", "80px;");
				</s:if>
				
				$("[name='website']").blur(function(){
					if($(this).val() == "http://"){
						$("#websiteTip").html('<label class="error success" for="website" generated="true">OK</label>');
						$(this).val("");
					}
				}).focus(function(){
					if($(this).val() == ""){
						$(this).val("http://");
						if(this.createTextRange){  
					       	var r = this.createTextRange();  
					       	r.moveStart('character',  $(this).val().length);  
					       	r.collapse();  
					       	r.select();  
		   				}  
					}
				});
				
				validator = $("#companyForm").validateForm({
					rules: {
						<s:if test="result.company.editMemberId == true">
							memberId: {memberId:true,remote:"/user/check_memberId.htm"},
						</s:if>
						<s:if test="result.company.editComName == true">
							comName:{required:true,remote:"/user/check_comName.htm"},
						</s:if>
						comNameEN:{maxlength: 120, english: true},
						businessType: {required:true},
						//domId: {required:true},
						keyword: {notAllEmpty:"[name='keyword']", notSame:"[name='keyword']"},
						catId: {required:true},
						website: {maxlength:80, url:true},
						description: {required:true,maxlength:4000},
						regNo: {regNoEmpty:"#regNo"},
						ceo: {ceoEmpty:"#ceo"}
					},
					messages: {
						<s:if test="result.company.editMemberId == true">
							memberId: {memberId:'<s:text name='memberId.required' />'},
						</s:if>
						<s:if test="result.company.editComName == true">
							comName:{required:'<s:text name="comName.required" />'},
						</s:if>
						comNameEN: {maxlength: '请输入公司名称，长度120个字符内', english: "公司英文名只能填写英文，请勿输入中文和数字。"},
						businessType: {required:'<s:text name="company.businessTypes.required" />'},
						//domId: {required:'<s:text name="company.domId.required" />'},
						keyword: {notAllEmpty:'<s:text name="company.keywords.required" />', notSame:'行业关键词不能重复'},
						catId: {required:'<s:text name="company.catIds.required" />'},
						website: {maxlength: '<s:text name="company.website.maxlength" />', url: '<s:text name="url.required" />'},
						description: {required:'<s:text name="company.description.required" />',maxlength:'<s:text name="company.description.required" />'},
						regNo: {regNoEmpty:'请输入营业执照注册号'},
						ceo: {ceoEmpty: '请输入法人'}
					}
				});
				
				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 800,
					modal: true,
					close: function(){
						$("#selectImgDialog").empty();
					}		
				});
				
				$("#adminLogDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 200,
					width: 360,
					modal: true,
					close: function(){
						$("#adminLogDialog").empty();
					}
				});
			
				$("#adminLog").click(function(){
					$("#adminLogDialog").load("/basic/admin_log.do?tableName=Company&tableId=<s:property value='result.company.comId' />",{random: Math.random()});
					$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
					$("#adminLogDialog").dialog('open');
				});

				if($.browser.msie && $.browser.version == 8.0){
					$("#fileIframe").css("width", "90px");
				}
				
				// 调整验证样式
				$("[name='memberId']").blur(function(){
					$("#memberIdTip .error").css("margin-left", "0");
				});
				
				// 执行在多个图片选择之后，不然无法获得多个图片的src值
				setTimeout("bindCompanForm()",500);
				
				if ($("#province").val() == "103103" && $("#rCompany").attr("checked")== true) {
					regNoShow();
				} else {
					regNoHide();
				}
				
				$("#rCompany").click(function(){
					if ($("#province").val() == "103103") {
						regNoShow();
					}
				});
				$("#rIndividua").click(function(){
					regNoHide();
				});
				
				$("#companyForm").submit(function() {
					$("#categoryList option").each(function() { this.selected = true; });
					if ($("#regImgPath").val() == "" && $("#regImgPath2").val() == "") {
						$("#regImgPathTip").html("<font color='red'>营业执照或有效证件不能为空</font>");
						return false;
					}
				});
			});
			
			// 去空格
			function Trim(str,is_global)
			{
				var result;
				if (str != null) {
					result = str.replace(/(^\s+)|(\s+$)/g,"");
					if(is_global.toLowerCase()=="g") {
						result = result.replace(/\s/g,"");
					}
				} 
				return result;
			}
			
			function regNoShow() {
				$("#regNoShow").show();
				$("#ceoShow").show();
				regNoShowVal = 1;
				ceoShowVal = 1;
			}
			
			function regNoHide() {
				$("#regNoShow").hide();
				$("#ceoShow").hide();
				regNoShowVal = 0;
				ceoShowVal = 0;
			}
			
			function bindCompanForm(){
				$("#companyForm").checkform({
					changeClick: "#_multiRemoveAll, [name='multiDelete']",
					url:"/member/company_edit.htm",
					validator: validator
				});
			}
			
			// 提交表单时验证，如果使用validate插件的submitHandler，将与checkform插件的submit方法冲突
			function checkCompanyForm(){
				var values = new Array();
				var same = false;
				var allEmpty = true;
				$("[name='keyword']").each(function(){
					if($(this).val() != ""){
						var strValue = "," + values.join(",") + ",";
						if(strValue.indexOf("," + $(this).val()+ ",") != -1){
							$(this).focus();
							same = true;
							$("#keywordTip").html('<label for="keyword" generated="true" class="error">行业关键词不能重复</label>');
						}else{
							values.push($(this).val());
						}
						allEmpty = false;
					}
				});
				if(allEmpty){
					$("[name='keyword']:eq(0)").focus();
					$("#keywordTip").html('<label for="keyword" generated="true" class="error">请填写行业关键词，长度为30个字符内</label>');
				}
				
				var invalidFormat = false;
				$("[name='website']").each(function(){
					var url = $(this).val();
					if(url.length > 80){
						$(this).focus();
						invalidFormat = true;
						$("#websiteTip").html('<label for="website" generated="true" class="error">请填写公司网站，长度为80个字符内</label>');
					}else if(url.length > 0 && !/^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(url)){
						$(this).focus();
						invalidFormat = true;
						$("#websiteTip").html('<label for="website" generated="true" class="error">请输入有效的链接地址</label>');
					}
				});

				if(!same && !invalidFormat && !allEmpty){
					$("#categoryList option").attr("selected",true);
					return true;
				}else{
					return false;
				}
			}
			
			function removeOption(selectId){
				$("#" + selectId + " option:selected").each(function(){
					$(this).remove();
				});
				$("#" + selectId + " option").attr("selected", "selected");
			}
		</script>
	</head>
	<body>
		<s:if test="reg == true">
		<div class="tipsSucceed">
			<div class="content">
				<span class="h1">
					恭喜您！您已经成功注册成为HiSupplier会员，会员帐号及密码已发送至您的邮箱：<s:property value="loginUser.email"/>。
				</span>
				<br/><br/>
				为了贵公司更容易被搜索到，请完善以下公司信息！
			</div>
		</div>
		</s:if>
		<s:if test="result.company.state == 10">
		<div style="color:red;padding:10px 20px;">
			<ul>
				<li>
					<span>您的公司信息审核未通过，请根据<a id='adminLog' href="javascript:void(0);">未通过原因</a>修改</span>
				</li>
			</ul>
		</div>
		</s:if>
		<s:elseif test="loginUser.keywords == ''">
		<div style="color:red;padding:10px 20px;">
			<ul>
				<li><span>请完善以下公司信息，进行更多后台操作。</span></li>
			</ul>
		</div>
		</s:elseif>
		
		<form id="companyForm" action="/member/company_edit_submit.htm" method="post">
			<input type="hidden" name="editMemberId" value="<s:property value='result.company.editMemberId' />"/>
			<input type="hidden" name="editComName" value="<s:property value='result.company.editComName' />"/>
			<input type="hidden" name="oldCatIds" value="<s:property value='result.company.oldCatIds' />"/>
			<input type="hidden" name="videoId" id="videoId" value="<s:property value='result.company.videoId' />"/>
			<input type="hidden" name="memberType" value="<s:property value='result.company.memberType' />"/>
			<input type="hidden" name="imgCount" value="<s:property value='result.company.imgCount' />"/>
			<input type="hidden" name="regImgPath" id="regImgPath" value="<s:property value='result.company.regImgPath'/>" />
			<input type="hidden" name="regImgPath2" id="regImgPath2" value="<s:property value='result.company.regImgPath2'/>" />
			<input type="hidden" name="oldRegImgPath2" id="oldRegImgPath2" value="<s:property value='result.company.regImgPath2'/>" />
			<input type="hidden" name="oldImgType" id="oldImgType" value="<s:property value='result.company.regImgType'/>" />
			<input type="hidden" id="province" value="<s:property value='result.province'/>" /> 
			<s:token />
			<table class="formTable">
				<tr>
					<th><s:text name="modifyTime" />：</th>
					<td><s:property value="result.company.modifyTime" /></td>
				</tr>
				<tr>
					<th><s:text name="memberType" />：</th>
					<td><s:property value="result.company.memberTypeName"/></td>
				</tr>
				<tr>
					<th><s:text name="memberId" />：</th>
					<td>
						<s:if test="result.company.editMemberId == true">
						<input name="memberId" value="<s:property value='result.company.memberId'/>" style="width:150px;" title="<s:text name='memberId.required' />" >
						<div id="memberIdTip"></div>
						</s:if>
						<s:else>
						<s:property value='result.company.memberId'/>
						<input type="hidden" name="memberId" value="<s:property value='result.company.memberId' />"/>
						</s:else>
					</td>
				</tr>
				
				<tr>
					<th><s:text name="comName" />：</th>
					<td>
						<s:if test="result.company.editComName == true">
						<input name="comName" value="<s:property value='result.company.comName'/>" style="width:300px;" title="<s:text name='comName.required' />" >
						<div id="comNameTip"></div>
						</s:if>
						<s:else>
						<s:property value='result.company.comName'/>
						<input type="hidden" name="comName" value="<s:property value='result.company.ComName' />"/>
						<div class="fieldTips">
							如果您想更改公司名称，请联系客服专员。
						</div>
						</s:else>
					</td>
				</tr>
				<tr>
					<th>英文公司名称：</th>
					<td>
						<input name="comNameEN" style="width:400px; display: block; margin-bottom: 10px;" value="<s:property value='result.company.comNameEN' />"/>
						<div class="fieldTips">
							如果您是出口会员，可在此填写英文公司名称。
						</div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="company.businessTypes" />：</th>
					<td>
						<div class="options">
							<ul>
							<s:iterator value="result.company.businessTypeItems" id="item">
							<li>
								<input type="checkbox" name="businessType" <s:property value='#item.checked' /> value='<s:property value="#item.value"/>'>
								&nbsp;<s:property value="#item.name"/>
							</li>
							</s:iterator>
							</ul>
							<div id="businessTypeTip" style="clear:left;"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th><s:text name="company.qualityCerts" />：</th>
					<td>
						<div class="options">
							<ul>
							<s:iterator value="result.company.qualityCertItems" id="item">
							<li>
								<input type="checkbox" name="qualityCert" <s:property value='#item.checked' /> value='<s:property value="#item.value"/>'>
								&nbsp;<s:property value="#item.name"/>
							</li>
							</s:iterator>
							</ul>
						</div>
					</td>
				</tr>
				<tr>
					<th><s:text name="company.websites" />：</th>
					<td>
						<s:iterator value="result.company.websiteArray1" id="website">
						<input name="website" value="<s:property value='#website' />" style="margin-right:2px;"/>
						</s:iterator>
						<div id="websiteTip"></div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="company.keywords" />：</th>
					<td>
						<s:iterator value="result.company.keywordArray1" id="keywordItem">
						<input name="keyword" value="<s:property value='#keywordItem' />" style="margin-bottom:3px;margin-right:2px;"/>
						</s:iterator>
						<span class="fieldTips">一个文本框对应一个关键词，填写三个以上关键词将会更有助于风格网站的优化，点击<a target="_blank" href="http://help.cn.hisupplier.com/accidence/info.php?bid=326&id=328">查看</a>如何选择更好的关键词<span id="mainKeywordstooltips"></span></span>
						<div id="keywordTip" style="float:left;"></div>
					</td>
				</tr>
				
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="company.catIds" />：</th>
					<td>
						<div style="float:left;" id="companyCategoryList">
							<select id="categoryList"  name="catId" style="width:420px;height:70px;" size="10" multiple="multiple">
								<s:iterator value="result.company.catItems" id="item">
								<option value="<s:property value='#item.value'/>" selected >
									<s:property value='#item.name'/>
								</option>
								</s:iterator>
							</select>
						</div>
						<div style="float:left;margin-left:4px;">
							<input type="button" id="_cat" value="<s:text name="button.select" />" /><br /><br />
							<input type="button" onclick="removeOption('categoryList')" value="<s:text name="button.remove" />" />
						</div>
						<span class="fieldTips">请选择正确的行业目录，最多可选择3个。<span id="catIdstooltips"></span></span>
						<div id="catIdTip" style="clear:left;"></div>
						<div id="_catList"></div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="company.description" />：</th>
					<td>
						<textarea name="description" style="width:566px; height:200px; font-size:12px; line-height: 20px;"><s:property value="result.company.description"/></textarea>
						<span class="fieldTips">在公司描述中添加行业关键词将更好的优化风格网站，内容请控制在100~3000个字符之间，不支持HTML代码。<span id="descripTip"></span></span>
						<div id="descriptionTip" style="float:left;"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="company.faceImg" />：</th>
					<td>
						<%
							int imgType = Image.FACE;
							String imgPathTag = "face";
							int maxNum = 6;
						%>
						<s:set name="images" value="result.company.faceImgPaths" />
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_multi.jsp" %>
						<span id="faceImgtooltips" style="top:0;width:16px; height:16px;"></span>
						<script>
							$("#div_showtip").append($("#faceImgtooltips"));
						</script>
					</td>
				</tr>
				
				<tr>
					<th>
						<span class="red">*</span>上传证件：
					</th>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="regImgType" id="rCompany" value="1" <s:if test="result.company.regImgType==1">checked="checked"</s:if> >&nbsp;企业</input>&nbsp;&nbsp;
						<input type="radio" name="regImgType" id="rIndividua" value="2" <s:if test="result.company.regImgType==2">checked="checked"</s:if> >&nbsp;个人</input>
					</td>
				</tr>
				<tr>
					<th>
						<input type="button" value="预览" class="yulan" data-title="上传证件" data-img="regImgPathSrc" />
					</th>
					<td>
						<div style="display: block; margin-left:10px; padding-left: 2px; width: 580px;">
							<span id="proAddImgDiv">
							<s:set name="image1" value="result.company.regImgPath" />
							<s:set name="image2" value="result.company.regImgPath2" />
							<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
							<s:set name="imgSize" value="'2048'" /> 
							<%@ include file="/page/member/inc/regImage.jsp" %>
							</span>
							<div class="t" style="width: 358px; margin-top: 10px; height: 60px;clear: both;" id="imgTips"></div>
						</div>
						<div id="regImgPathTip" style="margin-top: 5px; clear: both;"></div>
					</td>
				</tr>
				<tr id="regNoShow">
					<th>
						<span class="red">*</span>营业执照注册号：
					</th>
					<td>
						<s:if test="result.company.editRegNo == true">
							<input name="regNo" id="regNo" value="<s:property value='result.company.regNo'/>" style="width:300px;">
							<div id="regNoTip" style="color: red;"></div>
						</s:if>
						<s:else>
							&nbsp;&nbsp;&nbsp;
							<s:property value="result.company.regNo" />
						</s:else>
					</td>
				</tr>
				<tr id="ceoShow">
					<th>
						<span class="red">*</span>法人：
					</th>
					<td>
						<s:if test="result.company.editCeo == true">
							<input name="ceo" id ="ceo" value="<s:property value='result.company.ceo'/>" style="width:300px;">
							<div id="ceoTip" style="color: red;"></div>
						</s:if>
						<s:else>
							&nbsp;&nbsp;&nbsp;
							<s:property value="result.company.ceo" />
						</s:else>
					</td>
				</tr>
				<tr>
					<th>
						<s:text name="company.logoImg" />：
						<br /><input type="button" value="预览" class="chakan" data-title="商标图片" data-img="imgSrc" />
					</th>
					<td>
					<div id="logotooltips" style="position:absolute; margin-top:61px;margin-left:210px; width:16px; height:16px;"></div> 
						<%
							imgType = Image.LOGO;
							String imgSrcTag = "imgSrc";
							imgPathTag = "logoImgPath";
							String imgIdTag = "logoImgId";
						%>
						<s:set name="imgSrc" value="result.company.logoImgSrc" />
						<s:set name="imgPath" value="result.company.logoImgPath" />
						<s:set name="imgId" value="0" />
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_bar.jsp" %>
					</td>
				</tr>
				<tr>
					<th>
						<s:text name="company.logoCertImg" />：
						<br /><input type="button" value="预览" class="chakan" data-title="商标注册证书" data-img="logoCertImgSrc" />
					</th>
					<td id="certHeight">
						<%@ include file="/page/member/inc/logoCertImg.jsp" %>
					</td>
				</tr>
				<s:if test="loginUser.memberType == 2">
					<tr>
						<th><s:text name="company.video" />：</th>
						<td>
							<s:set name="videoImgPath" value="result.company.videoImgPath"></s:set>
							<s:set name="playPath" value="result.company.playPath"></s:set>
							<s:set name="videoState" value="result.company.videoState"></s:set>
							<%@ include file="/page/inc/video_bar.jsp"%>
						</td>
					</tr>
				</s:if>
			</table>
			<s:if test="loginUser.showCompanyEdit == true">
				<div class="buttonCenter">
					<input type="submit" value="<s:text name="button.submit"/>" />
					<input type="reset" value="<s:text name="button.reset"/>" />
				</div>
			</s:if>
		</form>
		<div id="selectImgDialog"></div>
		<div id="adminLogDialog"></div>
		<div id="pic_dialog" style="vertical-align: middle;"></div>
		
		<%@ include file="/page/inc/image_error.jsp"%>
	</body>
</html>
