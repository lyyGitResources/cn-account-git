<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.StringUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String sysBase = Config.getString("sys.base");
	String accountBase = Config.getString("account.base");
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		sysBase = "http://big5."+Config.getString("sys.domain");
		accountBase = "http://account.big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>简易快速发布求购信息-海商网-全国领先的B2B电子商务交易平台</title>
		<meta name="description" content="采购商可在此发布求购信息,求购所需的各种产品, 让卖家可以主动找到您，寻找合适的贸易伙伴。" />
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.category.data_zh.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
				remoteEmail: function(value, element, param) {
					if ( this.optional(element) )
						return "dependency-mismatch";
					var previous = this.previousValue(element);
					if (!this.settings.messages[element.name] )
						this.settings.messages[element.name] = {};
					this.settings.messages[element.name].remote = typeof previous.message == "function" ? previous.message(value) : previous.message;
					param = typeof param == "string" && {url:param} || param; 
					if ( previous.old !== value ) {
						previous.old = value;
						var validator = this;
						this.startRequest(element);
						var data = {};
						data[element.name] = value;
						$.ajax($.extend(true, {
							url: param,
							mode: "abort",
							port: "validate" + element.name,
							dataType: "json",
							data: data,
							success: function(response) {
								var valid = response.tip == "true";
								if (valid) {
									var submitted = validator.formSubmitted;
									validator.prepareElement(element);
									validator.formSubmitted = submitted;
									validator.successList.push(element);
									validator.showErrors();
								} else {
									var errors = {};
									errors[element.name] = previous.message = $.validator.messages.remote = validator.defaultMessage( element, "remoteEmail" );
									validator.showErrors(errors);
								}
								previous.valid = valid;
								validator.stopRequest(element, valid);
							}
						}, param));
						return "pending";
					} else if( this.pending[element.name] ) {
						return "pending";
					}
					return previous.valid;
				},
				remoteCompany: function(value, element, param) {
					if ( this.optional(element) )
						return "dependency-mismatch";
					var previous = this.previousValue(element);
					if (!this.settings.messages[element.name] )
						this.settings.messages[element.name] = {};
					this.settings.messages[element.name].remote = typeof previous.message == "function" ? previous.message(value) : previous.message;
					param = typeof param == "string" && {url:param} || param; 
					if ( previous.old !== value ) {
						previous.old = value;
						var validator = this;
						this.startRequest(element);
						var data = {};
						data[element.name] = value;
						$.ajax($.extend(true, {
							url: param,
							mode: "abort",
							port: "validate" + element.name,
							dataType: "json",
							data: data,
							success: function(response) {
								var valid = response.tip == "true";
								if (valid) {
									var submitted = validator.formSubmitted;
									validator.prepareElement(element);
									validator.formSubmitted = submitted;
									validator.successList.push(element);
									validator.showErrors();
								} else {
									var errors = {};
									errors[element.name] = previous.message = $.validator.messages.remote = validator.defaultMessage( element, "remoteCompany" );
									validator.showErrors(errors);
								}
								previous.valid = valid;
								validator.stopRequest(element, valid);
							}
						}, param));
						return "pending";
					} else if( this.pending[element.name] ) {
						return "pending";
					}
					return previous.valid;
				}
			});
			$(function (){
				countNum(150, $("#brief"), $('#sumTip'));
				countNum(20000, $("#description"), $('#sumTip2'));

				getProvince("province","city","town","tel1","countryCode");
				
				$("#_catList").category({
					contId: "catName",
					contType: "text"
				});
				
				$("#descriptionMore").click(function(){
					$("[name='descriptionDiv']").show();
					$("#descriptionHref").hide();
				});
				
				$("#forget").click(function(){
					var email = $.trim($("[name='email']").val());
					$(this).attr("href", "/user/forget_passwd.htm?email=" + email);
					$(this).attr("target","_blank");
				});
				
				
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});
				
				$("#tradeForm").validateForm({
					rules: {
						proName: {required: true, maxlength:120},
						keywords: {maxlength:60},
						catId: {required: true, min: 1},
						brief: {required: true, maxlength:150},
						description: {maxlength:10000},
						<s:if test="loginUser == false">
							<s:if test="newUser == false">
								email: {required: true, maxlength:80},
								passwd: {passwd:true},
							</s:if>
							<s:else>
								newUserEmail: {required: true, maxlength:80, email: true, remoteEmail: "/user/check_email.do"},
								newUserPasswd: {passwd:true},
								confirmPasswd: {required: true,equalTo: "[name='newUserPasswd']"},
								comName: {required: true, maxlength: 120, remoteCompany: "/user/check_comName.do"},
								contact: {required: true, maxlength: 20},
								sex: {required: true},
								tel1: {required:true, telAreaCode:"#tel2"},
								tel2: {required:true, telNumber:"#tel1"},
							</s:else>
						</s:if>
						validateCode: {required: true, rangelength:[5,5]}
					},
					messages: {
						proName: '请输入求购主题，长度120个字符内',
						keywords: '关键词长度最大为60个字符',
						catId: '请选择行业目录',
						brief: '请填写商情摘要，长度150个字符内',
						description: '详细描述长度最大为10000个字符',
						<s:if test="loginUser == false">
							<s:if test="newUser == false">
								email: '请输入会员帐号或邮箱',
								passwd: '请输入登录密码',
							</s:if>
							<s:else>
								newUserEmail: {required:'请输入有效的电子邮箱，便于找回密码',maxlength:'请输入有效的电子邮箱，便于找回密码',email:'请输入有效的电子邮箱，便于找回密码',remoteEmail: '邮箱已存在，点击输入<a href="javascript:login()">登录</a>密码即可发布求购信息'},
								newUserPasswd: '请输入密码，由6-20个字符组成，不能有空格',
								confirmPasswd: '两次密码不匹配',
								comName: {required:'请输入公司名称，长度120个字符内',maxlength:'请输入公司名称，长度120个字符内',remoteCompany:'该公司名称已注册，若忘记会员帐号和密码，请<a href="/user/contact_us.htm" target="_blank">联系我们</a>！'},
								contact: '请输入联系人姓名，长度20个字符内',
								sex: '请选择性别',
								tel1: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔',
								tel2: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔',
							</s:else>
						</s:if>
						validateCode: '请输入图片中的字符'
					}
				}); 
				
				<s:if test="loginUser == false && newUser == true">
					$("#province").change(function(){
						checklocal();
					});
					checklocal();
				</s:if>
				
				$("[name='model']:first").click(function(){
					$("[name='newUser']").val("true");
					$("#newUserTable").show();
					$("#userTable").hide();
					$("[name='newUserEmail']").val($("[name='email']").val());
					$("[name='newUserEmail']").focus();
					
					$("[name='email']").rules("remove");
					$("[name='passwd']").rules("remove");

					$("[name='newUserEmail']").rules("add", {required: true, maxlength:80, email: true, remoteEmail: "/user/check_email.do",
								messages: {required: '请输入有效的电子邮箱，便于找回密码', maxlength:'请输入有效的电子邮箱，便于找回密码',email:'请输入有效的电子邮箱，便于找回密码',remoteEmail: '邮箱已存在，点击输入<a href="javascript:login()">登录</a>密码即可发布求购信息'}});
					$("[name='newUserPasswd']").rules("add", {passwd: true, messages: '请输入密码，由6-20个字符组成，不能有空格'});
					$("[name='confirmPasswd']").rules("add", {equalTo: "[name='newUserPasswd']", messages: '两次密码不匹配'});
					$("[name='comName']").rules("add", {required: true, maxlength: 120, remoteCompany: "/user/check_comName.do", 
								messages: {required: '请输入公司名称，长度120个字符内',maxlength: '请输入公司名称，长度120个字符内',remoteCompany:'该公司名称已注册，若忘记会员帐号和密码，请联系我们！'}});	
					$("[name='contact']").rules("add", {required: true, maxlength: 20, messages: {required:'请输入联系人姓名，长度20个字符内',maxlength:'请输入联系人姓名，长度20个字符内'}});
					$("[name='sex']").rules("add", {required: true, messages: '请选择性别'});
					$("#province").rules("add", {required: true, messages: '请选择省份'});
					$("#city").rules("add", {required: true, messages: '请选择城市'});
					$("[name='tel1']").rules("add", {required:true, telAreaCode:"#tel2", messages:{required: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telAreaCode:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}});
					$("[name='tel2']").rules("add", {required:true, telNumber:"#tel1", messages:{required: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telNumber:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'}});
				});
				
				$("[name='model']:last").click(function(){
					$("[name='newUser']").val("false");
					$("#newUserTable").hide();
					$("#userTable").show();
					$("[name='email']").val($("[name='newUserEmail']").val());
					$("[name='email']").focus();
					
					$("[name='newUserEmail']").rules("remove");
					$("[name='newUserPasswd']").rules("remove");
					$("[name='confirmPasswd']").rules("remove");
					
					$("[name='comName']").rules("remove");	
					$("[name='contact']").rules("remove");
					$("[name='sex']").rules("remove");
					$("#province").rules("remove");
					$("#city").rules("remove");
					$("[name='tel1']").rules("remove");
					$("[name='tel2']").rules("remove");

					
					$("[name='email']").rules("add", {required: true, maxlength:80, messages: {required:'请输入会员帐号或邮箱',maxlength:'请输入会员帐号或邮箱'}});
					$("[name='passwd']").rules("add", {passwd: true,  messages: {passwd:'请输入登录密码'}});
				});

				// 已登录隐藏
				<s:if test="loginUser == true">
					$("#noLoginDiv").hide();
					$("#textNotMemberDiv").hide();
					$("#newUserTable").hide();
					$("#textMemberDiv").hide();
					$("#userTable").hide();
				</s:if>
				<s:elseif test="newUser == false">
					$("[name='model']:last").click();
				</s:elseif>
				<s:else>
					$("[name='model']:first").attr("checked", "checked");
				</s:else>
			});
			
			function checklocal(){
				if($("#province").val() == ""){
					$("[name='province']").rules("add", {
						required:true,
						messages: {required:'请选择省份'}
					});
				}else{
					$("[name='city']").rules("add", {
						required:true,
						messages: {required:'请选择城市'}
					});
					$("[name='province']").rules("remove");
				}
			}
			
			function loadValidateCode(){
				Util.loadValidateCode(document.tradeForm, "validateCodeImg", "/validateCode/getImage");
			}
			
			function countNum(num, obj, showObj) {
				var spare = num - obj.val().length;
				if(spare >= 0){
					showObj.val(obj.val().length);
				}else{
					obj.val(obj.val().substring(0, num));
					showObj.val(num);
				}
			}
			function login(){
					$("[name='model']:last").attr("checked","checked");
					$("[name='newUser']").val("false");
					$("#newUserTable").hide();
					$("#userTable").show();
					$("[name='email']").val($("[name='newUserEmail']").val());
					$("[name='email']").focus();
					
					$("[name='newUserEmail']").rules("remove");
					$("[name='newUserPasswd']").rules("remove");
					$("[name='confirmPasswd']").rules("remove");
					
					$("[name='comName']").rules("remove");	
					$("[name='contact']").rules("remove");
					$("[name='sex']").rules("remove");
					$("#province").rules("remove");
					$("#city").rules("remove");
					$("[name='tel1']").rules("remove");
					$("[name='tel2']").rules("remove");

					
					$("[name='email']").rules("add", {required: true, maxlength:80, messages: {required:'请输入会员帐号或邮箱',maxlength:'请输入会员帐号或邮箱'}});
					$("[name='passwd']").rules("add", {passwd: true,  messages: {passwd:'请输入登录密码'}});
			}
			function briefCheck(){
				var brief = $("#brief").val();
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				var con1 = brief.match(regEmail);
				var con2 = brief.match(regUrl);
				if(con1 != null && con1.length > 0){
					for (var i = 0; i < con1.length; i++) {
						if(con1[i].indexOf(HI_DOMAIN)==-1){
							$("#briefTip").html("<label class='error' for='brief' generated='true'>请不要在 摘要 中添加网址和邮箱，这将导致信息无法提交！</label>");
							return con1[i];
						}
					}
				}
				if(con2 != null && con2.length > 0){
					for (var i = 0; i < con2.length; i++) {
						if(con2[i].indexOf(HI_DOMAIN)==-1){
							$("#briefTip").html("<label class='error' for='brief' generated='true'>请不要在 摘要 中添加网址和邮箱，这将导致信息无法提交！</label>");
							return con2[i];
						}
					}
				}
				return "pass";
			}
			function descriptionCheck(){
				var description = $("#description").val();
				var regEmail = /\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/gi;
				var regUrl = /((http\:\/\/)|(www\.))([\w-\.\/\?%&=]*)?/gi;
				var con1 = description.match(regEmail);
				var con2 = description.match(regUrl);
				if(con1 != null && con1.length > 0){
					for (var i = 0; i < con1.length; i++) {
						if(con1[i].indexOf(HI_DOMAIN)==-1){
							$("#descriptionTip").html("<label class='error' for='description' generated='true'>请不要在 详细描述 中添加网址和邮箱，这将导致信息无法提交！</label>");
							return con1[i];
						}
					}
				}
				if(con2 != null && con2.length > 0){
					for (var i = 0; i < con2.length; i++) {
						if(con2[i].indexOf(HI_DOMAIN)==-1){
							$("#descriptionTip").html("<label class='error' for='description' generated='true'>请不要在 详细描述 中添加网址和邮箱，这将导致信息无法提交！</label>");
							return con2[i];
						}
					}
				}
				return "pass";
			}
			function check(){
				var con1 = briefCheck();
				if(con1 != "pass"){
					alert("请不要在 【摘要】中添加网址和邮箱，这将导致信息无法提交! \n 错误信息：" + con1);
					$("#brief").focus();
						return false;
				} else {
					var	con2 = descriptionCheck();
					if(con2 != "pass"){
						alert("请不要在 【详细描述】中添加网址和邮箱，这将导致信息无法提交! \n 错误信息：" + con2);
						$("#description").focus();
						return false;
					}
				}
				$("#tradeForm").submit();
			}
		</script>
	</head>
	<body>

			<!--产品列表部分-->
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>发布求购信息</span></div>
			<div class="blank7"></div>
			<div class="releaseBuy">
				<div class="tips">(请正确填写有效的信息，加入诚信商贸行列)<span class="s1">必填项</span>
				<span class="s2"><a href="http://help.cn.hisupplier.com/supplier/accidence/info.php?bid=181&id=191" target="_blank">帮助中心</a></span></div>
				<%@ include file="/page/inc/messages.jsp"%>
				<form class="submitForm" id="tradeForm" name="tradeForm" method="post" action="/user/post_buy_lead_submit.htm" >
					<input id="countryCode" type="hidden" value="<s:property value='countryCode' />"/>
					<input name="loginUser" type="hidden" value="<s:property value='loginUser' />"/>
					<input name="newUser" type="hidden" value="<s:property value='newUser' />"/>
					<input type="hidden" name="catId" id="catId" value="<s:property value='catId'/>" />
					<input type="hidden" name="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
					<div class="h1">1.快速发布求购信息</div>
					<div class="blank4"></div>
					
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th><label><strong>求购主题：</strong></label></th>
							<td>
								<div style="width:420px;">
									<input type="text" style=" width:402px; height:17px;" name="proName" value="<s:property value='proName' />"/>
								</div>
								<div id="proNameTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr>
							<th><strong>关键词：</strong></th>
							<td>
								<div style="width:480px;">
								<%-- 
									<s:iterator value="keywordArray" id="keyword">
										<input type="text" name="keywordArray" value="<s:property value='#keyword'/>" style="width: width:127px;"/>
									</s:iterator>	
								--%>
									<input type="text" name="keywords" style="width:127px;"/>
								</div>
								<div id="keywordsTip" style="margin-top:5px;"></div>
							</td>
							
						</tr>
						<tr>
							<th><label><strong>行业目录：</strong></label></th>
							<td>
								<input type="button" id="_cat" value="选 择" style="width:70px;" />
								<input type="text" style=" width:322px; height:17px;" name="catName" id="catName" value="<s:property value='catName'/>" readonly />
								<br />
								<div id="_catList"></div>
								<div id="catIdTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr>
							<th><label><strong>有效时间：</strong></label></th>
							<td>
								<select class="tradePostSelect" name="validDay" id="validDay" >
		              				<option value="7">7天</option>
		              				<option value="15">15天</option>
		              				<option value="30">30天</option>
		              				<option value="90">90天</option>
		              				<option value="180">180天</option>
		              				<option value="365" selected="selected">1年</option>
								</select>	
								<span id="validDayTip"></span>
							</td>
						</tr>
						<tr>
							<th>
								<label><strong>摘要：</strong></label>
								<br />
								<input type="text" style=" width:20px; height:17px;" id="sumTip" disabled="disabled"/>
								&nbsp;&nbsp;
							</th>
							<td>
									<textarea id="brief" name="brief" onkeyup="countNum(150, $('#brief'), $('#sumTip'))" onbeforepaste="countNum(150, $('#brief'), $('#sumTip'))" 
										onblur="briefCheck()" onbeforeeditfocus="countNum(150, $('#brief'), $('#sumTip'))"  style="width: 400px; height: 80px; margin-bottom:5px;"><s:property value='brief'/></textarea>
								<br />
								<a href="#position;" id="descriptionMore">填写详细描述请点击&gt;&gt;</a>
								<div id="briefTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr id="descriptionHref">
							<th>&nbsp;</th>
							<td style="padding-left:280px;"></td>
						</tr>
						<tr style="display:none;" name="descriptionDiv">
							<th>
								<strong>详细描述：</strong><br/>
								<input id="sumTip2"  style="width: 30px; margin-right: 5px;" disabled="disabled" />		
							</th>
							<td>
									<textarea name="description" id="description" onkeyup="countNum(20000, $('#description'), $('#sumTip2'))" onbeforepaste="countNum(20000, $('#description'), $('#sumTip2'))"
										onblur="descriptionCheck()" onbeforeeditfocus="countNum(20000, $('#description'), $('#sumTip2'))" style="width: 400px; height: 160px;margin-bottom:5px;"><s:property value='description'/></textarea>					
								<br/>
								<div class="t">请您填写详细的商情描述，内容请控制在 10000 字符内。</div>
								<div id="descriptionTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr style="display:none;" name="descriptionDiv">
							<td></td>
							<td>
														
							</td>
							<td> </td>
						</tr>
					</table>
					<div class="h1" id="noLoginDiv">2.填写联系信息</div>
					<div class="blank4"></div>
					<div class="h2" id="textNotMemberDiv">
						<input type="radio" name="model" checked/>
						还不是会员
					</div>
					<table id="newUserTable" border="0" cellspacing="0" cellpadding="0" style="margin-bottom:30px;">
						<tr>
							<th><label><strong>电子邮箱：</strong></label></th>
							<td>
								<input type="text" name="newUserEmail" value="<s:property value='newUserEmail'/>" style=" width:200px; height:17px;"/>
								<span id="newUserEmailTip"></span>
							</td>
							
						</tr>
						<tr>
							<th><label><strong>密码：</strong></label></th>
							<td>
								<input name="newUserPasswd" type="password" style=" width:200px; height:17px;"/>
								<div class="t">由6-20个字符组成，不能有空格。</div>
								<span id="newUserPasswdTip"></span>
							</td>
							
						</tr>
						<tr>
							<th><label><strong>确认密码：</strong></label></th>
							<td>
								<input name="confirmPasswd" type="password" style=" width:200px; height:17px;"/>
								<span id="confirmPasswdTip"></span>
							</td>
						</tr>
						<tr>
							<th><label><strong>公司名称：</strong></label></th>
							<td>
								<input type="text" name="comName" value="<s:property value="comName" />" style=" width:402px; height:17px;"/>
								<div class="t">请填写工商局注册的全称，无商号的个体经营者请填写执照上的姓名，并标注个体经营，<br />如张三（个体经营）公司名一旦注册，不可随意修改，如需修改请提供相关证明。</div>
								<div id="comNameTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr>
							<th><label><strong>联系人姓名：</strong></label></th>
							<td>
								<input type="text" name="contact" value="<s:property value="contact" />" style=" width:200px; height:17px;"/>
								<s:if test="sex == 1 || sex == 2 || sex == 3">
									<input type="radio" value="1" name="sex" <s:if test="sex == 1">checked</s:if>/>先生&nbsp;&nbsp;
									<input type="radio" value="2" name="sex" <s:if test="sex == 2">checked</s:if>/>女士&nbsp;&nbsp;
									<input type="radio" value="3" name="sex" <s:if test="sex == 3">checked</s:if>/>小姐
								</s:if>
								<s:else>
									<input type="radio" value="1" name="sex" checked>先生&nbsp;&nbsp;
									<input type="radio" value="2" name="sex"/>女士&nbsp;&nbsp;
									<input type="radio" value="3" name="sex"/>小姐
								</s:else>
								<span id="contactTip"></span>
							</td>
						</tr>
						<tr>
							<th><label><strong>所在地：</strong></label></th>
							<td>
								<select id="province" name="province" style="width:129px;"></select>
								<select id="city" name="city" style="width:100px;"></select>
								<select id="town" name="town" style="width:100px;"></select>
								<span id="cityTip"></span>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td style="padding:0; color:#999; vertical-align:bottom;">区号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</td>
						</tr>
						<tr>
							<th><label><strong>电话号码：</strong></label></th>
							<td style="padding-top:0;">
								<div style="width:420px;">
									<input id="tel1" style="width: 50px;" value="<s:property value="tel1" />" name="tel1" style=" width:54px; height:17px;"/>
									-
									<input id="tel2" value="<s:property value="tel2" />" name="tel2" style=" width:131px; height:17px;"/>
								</div>
								<div class="t">多个电话用","或"/"分隔，分机号码请用"-"分隔。<br />如：0574-27702770-123,27712771</div>
								<div id="telTip" style="margin-top:5px;"></div>
							</td>
						</tr>
					</table>
						
					<div class="h2" id="textMemberDiv">
						<input type="radio" name="model" />
						已经是会员
					</div>
					<table id="userTable" style="display:none;" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th><label><strong>会员帐号或邮箱：</strong></label></th>
							<td>
								<input type="text" name="email" value="<s:property value='email'/>" style="width:200px; height:17px;"/>
								<a href="#position" id="forget">忘记密码？</a>
								<span id="emailTip"></span>
							</td>
						</tr>
						<tr>
							<th><label><strong>密码：</strong></label></th>
							<td>
								<input type="password" name="passwd" value="<s:property value='passwd'/>" style=" width:200px; height:17px;"/>
								<span id="passwdTip"></span>
							</td>
						</tr>
					</table>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th><label><strong>验证码：</strong></label></th>
							<td>
								<input type="text" id="validateCode" name="validateCode" value="<s:property value="validateCode"/>" autocomplete="off" maxlength="5"/>
								<a href="#loadValidateCode" id="loadValidateCode">看不清，换一张</a>
								<span id="validateCodeTip"></span>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>
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
						<tr>
							<th>&nbsp;</th>
							<td style="padding-top:30px;"><input type="button" name="Submit" onclick="check()" value="马上发布" class="button7"/></td>
						</tr>
					</table>
				</form>
		         <div class="footerTips">
		         	<%--<div>需要帮助？请立即<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">联系我们</a>！</div>--%>
					<div>您在搜索卖家/买家信息过程中，有任何问题和建议？<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">点此反馈</a></div>
		         </div>
			</div>
		</div>
		<div class="blank2"></div>
	</body>
</html>