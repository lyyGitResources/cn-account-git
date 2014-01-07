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
		<title>简易快速订阅商情</title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
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

				getProvince("province","city","country","tel1","countryCode");

				$("#alertForm").validateForm({
					submitHandler: function(form){
						$("#submitButton").attr("disabled", "disabled");
						$("#submitButton").val("提交中..");
						form.submit();
					},
					rules: {
						passwd: {passwd:true},
						confirmPasswd:{required:true,equalTo:"#passwd"},
						comName: {required: true, maxlength: 120, remoteCompany: "/user/check_comName.do"},
						contact: {required:true, maxlength:20},
						tel1: {required:true, telAreaCode:"#tel2"},
						tel2: {required:true, telNumber:"#tel1"},
						validateCode:{required:true, maxlength:5,minlength:5}
					},
					messages: {
						province:'请选择省份',
						city:'请选择城市',
						passwd: '请输入密码，由6-20个字符组成，不能有空格',
						confirmPasswd: {required: "请输入确认密码", equalTo: "两次输入密码不匹配"},
						comName:{required:'请输入公司名称，长度120个字符内',maxlength:'请输入公司名称，长度120个字符内',remoteCompany:'该公司名称已注册，若忘记会员帐号和密码，请<a href="/user/contact_us.htm" target="_blank">联系我们</a>！'},
						contact: '请输入联系人姓名，长度20个字符内',
						tel1: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔',
						tel2: '请输入区号和电话号码，区号长度3-5位数字，电话号码长度在7-26个字符内，多个电话用","或"/"分隔，分机号码请用"-"分隔',
						validateCode: "请输入图片中的字符"
					}
				});				
				
				// IE8中按下‘Backspace’会返回trade_alert_step1
				$("[name='email']").keydown(function(event){
					if(event.keyCode == 8){
						return false;
					}
				});
				
				$("#province").change(function(){
					checklocal();
				});
				checklocal();
				
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});			
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
				Util.loadValidateCode(document.alertForm, "validateCodeImg", "/validateCode/getImage");
			}		
		</script>		
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a>&nbsp;&gt;&nbsp;<span>商情订阅</span></div>
			<div class="blank7"></div>
			<div class="registerInfo">
				<div class="h1">您还不是中国海商网会员，请填写以下注册信息</div>
				<div class="offerLine"></div>
				<form class="submitForm" id="alertForm" action="/user/trade_alert_step3.htm" method="post" name="alertForm">
					<%@ include file="/page/inc/messages.jsp"%>
					<input type="hidden" name="product" value="<s:property value='result.tradeAlert.product' />"/>
					<input type="hidden" name="buy" value="<s:property value='result.tradeAlert.buy' />"/>
					<input type="hidden" name="company" value="<s:property value='result.tradeAlert.company' />"/>
					<input type="hidden" name="countryCode" id="countryCode" value="<s:property value='result.tradeAlert.countryCode' />"/>
					<input type="hidden" name="validateCodeKey" value="<s:property value="result.tradeAlert.validateCodeKey"/>"/>
					<input type="hidden" name="mode" value="<s:property value='result.tradeAlert.mode' />" />
					<input type="hidden" name="keyword" value="<s:property value='result.tradeAlert.keyword' />" />
					<input type="hidden" name="catIds" value="<s:property value='result.tradeAlert.catIds' />" />
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td colspan="2">
								<div class="tips"><span class="s1">必填项</span><span class="s2"><a href="http://help.cn.hisupplier.com/accidence/info.php?bid=149&id=160" target="_blank">帮助中心</a></span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="t">
								<s:if test="result.tradeAlert.mode == 'keyword'">
									您订阅的关键词：<strong><s:property value='result.tradeAlert.keyword' /></strong>
								</s:if>
								<s:elseif test="result.tradeAlert.mode == 'category'">
									您订阅的目录：<strong><s:property value='result.tradeAlert.catNamePaths' /></strong>
								</s:elseif>
							</td>
						</tr>
						<tr>
							<th><label><strong>公司所在地：</strong></label></th>
							<td>
								<select name="province" id="province" style=" width:130px;"></select>
								<select name="city" id="city" style=" width:130px;"></select>
								<select name="town" id="country" style=" width:130px;"></select>
								<span id="cityTip"></span>
							</td>
						</tr>
						<tr>
							<th><strong>公司名称：</strong></th>
							<td>
								<input name="comName" type="text" style=" width:402px; height:17px;" value="<s:property value='result.tradeAlert.comName' />"/>
								<div class="t">请填写工商局注册的全称，无商号的个体经营者请填写执照上的姓名，并标注个体经营，<br />如张三（个体经营）公司名一旦注册，不可随意修改，如需修改请提供相关证明。</div>
								<div id="comNameTip" style="margin-top:5px;"></div>
							</td>
							<td></td>
						</tr>
						<tr>
							<th><label><strong>联系人姓名：</strong></label></th>
							<td>
								<input type="text" name="contact" style=" width:200px; height:17px;" value="<s:property value='result.tradeAlert.contact' />"/>
								<s:if test="result.tradeAlert.sex == 1 || result.tradeAlert.sex == 2 || result.tradeAlert.sex == 3">
									<input type="radio"  name="sex" value="1" <s:if test="result.tradeAlert.sex == 1" >checked</s:if> />先生&nbsp;&nbsp;
									<input type="radio" name="sex" value="2" <s:if test="result.tradeAlert.sex == 2" >checked</s:if> />女士&nbsp;&nbsp;
									<input type="radio" name="sex" value="3" <s:if test="result.tradeAlert.sex == 3" >checked</s:if> />小姐
								</s:if>
								<s:else>
									<input type="radio"  name="sex" value="1" checked/>先生&nbsp;&nbsp;
									<input type="radio" name="sex" value="2"/>女士&nbsp;&nbsp;
									<input type="radio" name="sex" value="3"/>小姐
								</s:else>
								<span id="contactTip"></span>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>区号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电话</td>
						</tr>
						<tr>
							<th><label><strong>电话：</strong></label></th>
							<td>
								<input type="text" style=" width:54px; height:17px;"  name="tel1" id="tel1" value="<s:property value='result.tradeAlert.tel1'/>"/>-<input type="text" style=" width:131px; height:17px;" id="tel2" name="tel2" value="<s:property value='result.tradeAlert.tel2'/>"/>
								<div class="t">多个电话用","或"/"分隔，分机号码请用"-"分隔。<br />如：0574-27702770-123,27712771</div>
								<div id="telTip" style="margin-top:5px;"></div>
							</td>
						</tr>
						<tr>
							<th><label><strong>电子邮箱：</strong></label></th>
							<td>
								<input type="text" style="width:200px; height:17px;" name="email" readonly="readonly" value="<s:property value='result.tradeAlert.email' />"/>
								<a href="/user/trade_alert_step1.htm?keyword=<s:property value='result.tradeAlert.keyword' />&catIds=<s:property value='result.tradeAlert.catIds' />&product=<s:property value='result.tradeAlert.product' />&company=<s:property value='result.tradeAlert.company' />&buy=<s:property value='result.tradeAlert.buy' />&email=<s:property value='result.tradeAlert.email' />&member=true" >已经是会员!</a>
								<span id="emailTip"></span>
							</td>
						</tr>
						<tr>
							<th><label><strong>密码：</strong></label></th>
							<td>
								<input name="passwd" type="password" id="passwd" style=" width:200px; height:17px;"/>
								<div class="t">由6-20个字符组成，不能有空格。</div>
								<span id="passwdTip"></span>
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
							<th><label><strong>验证码：</strong></label></th>
							<td>
								<input type="text" id="validateCode" name="validateCode" maxlength="5"  style=" width:200px; height:17px;" autocomplete="off" value="<s:property value="result.tradeAlert.validateCode"/>"/>
								<a href="#position" id="loadValidateCode" >看不清，换一张</a>
								<span id="validateCodeTip"></span>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td>
								<s:if test="result.tradeAlert.validateCodeKey == null">
									<script type="text/javascript">
										$(function(){
											loadValidateCode();
										});
									</script>
									<img id="validateCodeImg" />	
								</s:if>
								<s:else>
									<img id="validateCodeImg" src="/validateCode/getImage?hi_vc_key=<s:property value="result.tradeAlert.validateCodeKey"/>"/>	
								</s:else>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td style="padding-top:30px;"><input type="submit" name="Submit" value="提&nbsp;&nbsp;交" class="button7"/></td>
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
