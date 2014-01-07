<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.StringUtil"%>
<%@page import="com.hisupplier.commons.util.WebUtil"%>
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
		<title>免费注册会员-海商网-全国领先的B2B电子商务交易平台</title>
		<meta name="description" content="免费注册海商网会员，免费发布供求信息，获得海量商机，海商网汇聚了丰富的采购商和供应商信息，已经形成一个全球性的网上贸易市场。"/>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/swfupload/handlers.js"></script>
		<link href="/css/join.css" type="text/css" rel="stylesheet" />
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
			$(function(){
				getProvince("province","city","country","tel1","countryCode");
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});
				
				//$("input[name='imgType']:checked").val()
				
				$("#joinForm").validateForm({
					rules: {
						email: {required:true, email:true, remoteEmail:"/user/check_email.htm"},
						memberId: {memberId:true, remote:"/user/check_memberId.htm"},
						passwd: {passwd:true},
						confirmPasswd: {required:true, equalTo:"#passwd"},
						comName: {required:true, maxlength:120, remote:"/user/check_comName.htm"},
						contact: {required:true, maxlength:20},
						tel1: {required:true, telAreaCode:"#tel2"},
						tel2: {required:true, telNumber:"#tel1"},
						validateCode:{required:true, maxlength:5, minlength:5}
					},
					messages: {
						email: {required: '请输入有效的电子邮箱，便于找回密码', email: '请输入有效的电子邮箱，便于找回密码', remoteEmail: '邮箱已存在，请直接<a href="javascript:login()">登录</a>'  },
						memberId: {memberId: '请输入有效的会员帐号，以字母开头，可以由4-20个字母、数字，或"-"组成，不能有空格或"_"'},
						passwd: '请输入密码，由6-20个字符组成，不能有空格',
						confirmPasswd: {required: '请输入确认密码', equalTo: '两次输入密码不匹配'},
						comName: {required: '请输入公司名称，长度120个字符内',remote:'该公司名称已注册，若忘记会员帐号和密码，请<a href="/user/contact_us.htm" target="_blank">联系我们</a>！'},
						contact: {required: '请输入联系人姓名，长度20个字符内'},
						tel1: {required:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telAreaCode:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'},
						tel2: {required:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔', telNumber:'请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔'},
						validateCode: '请输入图片中的字符'
					}
				});
				
				$("#province").change(function(){
					checklocal();
					gongshangDIV();
				});
				checklocal();
				
				$("#tel1,#tel2").focus(function(){
					if(this.value == "区号" || this.value=="电话号码"){
						this.value="";
						$(this).removeClass();
					}
				});
				
				gongshangDIV();
				
				$("#joinForm").submit(function() {
					var submitStr = true;
					if ($("#regImgPath").val() == "" && $("#regImgPath2").val() == "") {
						$("#regImgPathTip").html("<font color='red'>营业执照或有效证件不能为空</font>");
						submitStr = false;
					}
					if($("#rCompany").attr("checked")== true && $("#province").val() == "103103") {
						if($("#regNo").val() == "") {
							$("#gongshangTip").html("<font color='red'>营业执照注册号不能为空</font>");
							submitStr = false;
						}
						if ($("#ceo").val() == "") {
							$("#ceoTip").html("<font color='red'>法人不能为空</font>");
							submitStr = false;
						}
					}
					return submitStr;
				});
				
				$("#rCompany").click(function(){
					gongshangDIV();
				});
				$("#rIndividua").click(function(){
					gongshangDIV();
				});
			});
			
			function gongshangDIV() {
				if ($("#rCompany").attr("checked")== true && $("#province").val() == "103103") {
					$(".gongshang").show();
				} else {
					$(".gongshang").hide();
				}
			}
			
			function checklocal(){
				if($("#province").val() == ""){
					$("[name='province']").rules("add", {
						required:true,
						messages: { required: "请选择省份" }
					});
				}else{
					$("[name='city']").rules("add", {
						required:true,
						messages: { required: "请选择城市" }
					});
					$("[name='province']").rules("remove");
				}
			}
			
			function loadValidateCode(){
				Util.loadValidateCode(document.joinForm,"validateCodeImg","/validateCode/getImage");
			}
			function login(){
				location.href="http://my.<%=Config.getString("sys.domain")%>/cn/login?email="+$("#email").val()+"&return=<%=Config.getString("account.base")%>";
			}
		</script>
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>注册</span></div>
			<div class="regisTips">（请正确填写有效的信息，加入诚信商贸行列）<span class="s1">必填项</span><span class="s2"><a href="http://help.cn.hisupplier.com/supplier/accidence/info.php?bid=181&id=183" target="_blank">帮助中心</a></span></div>
				<form action="/user/join_submit.htm" id="joinForm" name="joinForm"  class="submitForm" method="post">
					<div class="boxRim">
						<div class="h1">加入海商网，打造属于您的个性化电子商务平台！</div>
						<div class="freeRegis">
							<input type="hidden" id="countryCode" name="countryCode" value="<s:property value='result.register.countryCode'/>">
							<input type="hidden" name="validateCodeKey" value="<s:property value='result.register.validateCodeKey'/>"/>
							<input type="hidden" name="regImgPath" id="regImgPath" value="<s:property value='result.register.regImgPath'/>" />
							<input type="hidden" name="regImgPath2" id="regImgPath2" value="<s:property value='result.register.regImgPath2'/>" />
							<s:if test="result.register.regMode == 20">
								<input type="hidden" name="regMode" value="20" />
							</s:if>
							<div class="h2"><div>选择公司所在地</div></div>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>公司所在地：</strong></label></th>
									<td>
										<select id="province" name="province" style="width:130px;">
											<option>省份</option>
										</select>
										<select id="city" name="city" style=" width:130px;">
											<option>城市</option>
										</select>
										<select id="country" name="town" style=" width:130px;">
											<option>地区</option>
										</select>
										<span id="cityTip"></span>
									</td>
									
								</tr>
							</table>
							<div class="blank8"></div>
							<div class="h2"><div>设置帐号和密码</div></div>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>电子邮箱：</strong></label></th>
									<td>
										<input type="text" id="email" name="email" style=" width:200px; height:17px;" value="<s:property value='result.register.email' />" />
										<span id="emailTip"></span>
									</span>
								</tr>
								<tr>
									<th style="padding-top:12px;"><label><strong>会员帐号：</strong></label></th>
									<td style="padding-top:12px;">
										<input name="memberId"	value="<s:property value='result.register.memberId'/>" style=" width:200px; height:17px;" >
										<span style="font-family: Arial;">.cn.hisupplier.com</span>
										<div class="t">以字母开头，由4-20个字符组成；会员帐号决定三级域名（会员网站）的地址，<br />请填写有意义的会员帐号，三级域名（会员网站）的链接地址为“会员帐号.cn.hisupplier.com”。</div>
										<div id="memberIdTip" style="margin-top:5px;"></div>
									</td>
								</tr>
								<tr>
									<th><label><strong>密&nbsp;&nbsp;&nbsp;&nbsp;码：</strong></label></th>
									<td>
										<input type="password" id="passwd" name="passwd" style=" width:200px; height:17px;" value="<s:property value='result.register.passwd' />" />
										<div class="t">由6-20个字符组成，不能有空格。</div>
										<span id="passwdTip"></span>
									</td>
								</tr>
								<tr>
									<th><label><strong>确认密码：</strong></label></th>
									<td>
										<input type="password" id="confirmPasswd" name="confirmPasswd" style=" width:200px; height:17px;" />
										<span id="confirmPasswdTip"></span>
									</td>
								</tr>
							</table>
							<div class="blank8"></div>
							<div class="h2"><div>填写联系信息</div></div>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>公司名称：</strong></label></th>
									<td>
									<input name="comName" value="<s:property value='result.register.comName'/>" style=" width:402px; height:17px;">
										<div class="t">请填写工商局注册的全称，无商号的个体经营者请填写执照上的姓名，并标注个体经营，<br />如张三（个体经营）公司名一旦注册，不可随意修改，如需修改请提供相关证明。</div>
										<div id="comNameTip" style="margin-top:5px;"></div>
									</td>
								</tr>
								<tr>
									<th><label><strong>联系人姓名：</strong></label></th>
									<td>
										<input type="text" name="contact" style=" width:200px; height:17px;" value="<s:property value='result.register.contact' />" />
										<s:if test="result.register.sex == 2">
											<input type="radio" id="Mr" name="sex" value="1" /><label for="Mr"> 先生</label>&nbsp;&nbsp;
											<input type="radio" id="Mrs" name="sex" value="2" checked="checked" /><label for="Mrs"> 女士</label>&nbsp;&nbsp;
											<input type="radio" id="Miss" name="sex" value="3" /><label for="Miss"> 小姐</label>
										</s:if>
										<s:elseif test="result.register.sex == 3">
											<input type="radio" id="Mr" name="sex" value="1" /><label for="Mr"> 先生</label>&nbsp;&nbsp;
											<input type="radio" id="Mrs" name="sex" value="2" /><label for="Mrs"> 女士</label>&nbsp;&nbsp;
											<input type="radio" id="Miss" name="sex" value="3" checked="checked" /><label for="Miss"> 小姐</label>
										</s:elseif>
										<s:else>
											<input type="radio" id="Mr" name="sex" value="1" checked="checked" /><label for="Mr"> 先生</label>&nbsp;&nbsp;
											<input type="radio" id="Mrs" name="sex" value="2" /><label for="Mrs"> 女士</label>&nbsp;&nbsp;
											<input type="radio" id="Miss" name="sex" value="3" /><label for="Miss"> 小姐</label>
										</s:else>
										
										<div id="contactTip" style="margin-top:5px;"></div>
									</td>
								</tr>
<!-- 								<tr> -->
<!-- 									<th>&nbsp;</th> -->
<!-- 									<td style="padding:0; color:#999; vertical-align:bottom;">区号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</td> -->
<!-- 								</tr> -->
								<tr>
									<th><label><strong>电话号码：</strong></label></th>
									<td>
									<input type="text" id="tel1" name="tel1" style=" width:54px; height:17px;" <s:if test="result.register.tel1 == null || result.register.tel1 == ''">value="区号" class="tel"</s:if><s:else>value="<s:property value='result.register.tel1' />"</s:else>" />
									-
									<input type="text" id="tel2" name="tel2" style=" width:131px; height:17px;" <s:if test="result.register.tel2 == null || result.register.tel2 == ''">value="电话号码"  class="tel"</s:if><s:else>value="<s:property value='result.register.tel2' />"</s:else>" />
									<div class="t">多个电话用","或"/"分隔，分机号码请用"-"分隔。<br />如：0574-27702770-123,27712771</div>
									<div id="telTip" style="margin-top:5px;"></div>
									</td>
								</tr>
								<tr>
									<th><label><strong>上传证件：</strong></label></th>
									<td>
										<input type="radio" name="regImgType" id="rCompany" value="1" checked="checked">&nbsp;企业</input> &nbsp;&nbsp;
										<input type="radio" name="regImgType" id="rIndividua" value="2">&nbsp;个人</input>
									</td>
								</tr>
								<tr>
									<th></th>
									<td>
										<div style="display:block;padding-right:2px;width:580px;">
											<span id="proAddImgDiv">
											<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" /> 
											<s:set name="imgSize" value="'2048'" />
											<%@ include file="/page/inc/join_image_multi.jsp"%>
											</span> 
											<span style="margin-left: 4px; color: red;" id="phototooltip"></span>
											<div class="t" style="width: 358px; margin-top: 10px;clear: both;" id="imgTips"></div>
										</div>
										<div id="regImgPathTip" style="margin-top:5px; clear: both;"></div>
									</td>
								</tr>
							</table>
							
							<div class="gongshang">
								<div class="gongshangTitle">为配合网络工商部门监管，请输入您的营业执照注册号和法人。</div>
								<table class="gongshangTable" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>营业执照注册号：</strong></label></th>
									<td>
										<input name="regNo" id="regNo" value="<s:property value='result.register.regNo' />" style="width:200px; height:17px;" >
									</td>
									<td>
										<div id="gongshangTip"></div>
									</td>
								</tr>
								<tr>
									<th><label><strong>法人：</strong></label></th>
									<td>
										<input name="ceo" id="ceo" value="<s:property value='result.register.ceo' />" style="width:200px; height:17px;" >
									</td>
									<td>
										<div id="ceoTip"></div>
									</td>
								</tr>
							</table>
							</div>
							<div class="blank8"></div>
							<div class="h2"><div>输入验证码</div></div>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>验证码：</strong></label></th>
									<td>
										<input type="text" id="validateCode" name="validateCode" maxlength="5" autocomplete="off" style=" width:200px; height:17px;" value="<s:property value='result.register.validateCode' />"/>
										<a href="#position" id="loadValidateCode" >看不清，换一张</a>
										<span id="validateCodeTip"></span>
									</td>
								</tr>
								<tr>
									<th>&nbsp;</th>
									<td>
										<script type="text/javascript">
											$(function(){
												if($("[name='validateCodeKey']").val() == ""){
													loadValidateCode();
												}else{
													$("#validateCodeImg").attr("src", "/validateCode/getImage?hi_vc_key=" + $("[name='validateCodeKey']").val());
												}
											});
										</script>
										<img id="validateCodeImg" />	
									</td>
							     </tr>
							</table>
							<br/>
						</div>
					</div>
					 <div class="regisBtn">
					      <input type="hidden" name="regToken" value="<s:property value='regToken' />" />
					      <input type="submit" name="submit" value="提交注册" class="button7"/>
					      <p>会员资料一经提交，即表示您同意我们的<a href="/user/join_agreement.htm" target="_blank">用户协议</a>。</p>
				     </div>
				</form>
		         <div class="footerTips">
		         	<%--<div>需要帮助？请立即<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">联系我们</a>！</div>--%>
					<div>您在搜索卖家/买家信息过程中，有任何问题和建议？<a href="<%=accountBase%>/user/contact_us.htm" target="_blank">点此反馈</a></div>
		         </div>
<!-- 			</div> -->
		</div>
		<div class="blank2"></div>
	</body>
</html>
