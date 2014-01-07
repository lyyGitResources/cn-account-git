<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="com.hisupplier.commons.Config"%>
<%@page import="com.hisupplier.commons.util.StringUtil"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String sysBase = Config.getString("sys.base");
	if(StringUtil.equalsIgnoreCase("true",Config.getString("isBig5"))){
		sysBase = "http://big5."+Config.getString("sys.domain");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>取回忘记密码-海商网-全国领先的B2B电子商务交易平台</title>
		<meta name="description" content="取回密码输入需取回密码的账号或所捆绑的电子邮箱，确认后系统会将将把注册信息发送到该账号您所捆绑的电子邮箱。"/>
		<style type="text/css">
			label.error {
				width:180px;
				float: none;
			}	
			label.success {
				width:180px;
				float: none;
			}
		</style>
		<script type="text/javascript">
			$(function (){
				$("#forgetPasswdForm").validateForm({
					rules: {
						email: {required:true},
						validateCode: {required:true, maxlength:5, minlength:5}
					},
					messages: {
						email: "请填写会员帐号或者电子邮箱",
						validateCode: "请输入图片中的字符"
					}
				});
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});
			});
			
			function loadValidateCode(){
				Util.loadValidateCode(document.forgetPasswdForm,"validateCodeImg","/validateCode/getImage");
			}
		</script>
	</head>
	<body>
	<!--产品列表部分-->
		<div class="area">
			<div class="mainNav2"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a> &gt; <span>忘记密码</span></div>
			<div class="blank6"></div>
			<div class="forgotPassword">
				<div class="top"></div>
				<div class="center">
					<div class="box">
						<div class="tips1">如果您忘记密码，请输入您注册的会员帐号或者电子邮箱，我们将发送密码到您的电子邮箱中。</div>
						<%@ include file="/page/inc/messages.jsp"%>
						<form id="forgetPasswdForm" name="forgetPasswdForm" action="/user/forget_passwd_send.htm" method="post">
							<input type="hidden" name="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th><label><strong>会员帐号/电子邮箱：</strong></label></th>
									<td>
										<input type="text" name="email" value="<s:property value="email"/>" style=" width:200px; height:17px;"/>
										<span id="emailTip"></span>
									</td>
								</tr>
								<tr>
									<th><label><strong>验证码：</strong></label></th>
									<td>
										<input type="text" id="validateCode" name="validateCode" maxlength="5" autocomplete="off" value="<s:property value="validateCode"/>" style=" width:200px; height:17px;"/>
										<a href="#loadValidateCode" id="loadValidateCode" >看不清，换一张</a>
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
											<img id="validateCodeImg" />	
										</s:if>
										<s:else>
											<img id="validateCodeImg" src="/validateCode/getImage?hi_vc_key=<s:property value="validateCodeKey"/>"/>	
										</s:else>
									</td>
								</tr>
								<tr>
									<th>&nbsp;</th>
									<td class="B"><a onclick="$('#forgetPasswdForm').submit();" href="#position"><img src="<%=Config.getString("sys.base") %>/img/forgotButton.gif" border="0"/></a></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<div class="buttom"></div>
			</div>
		</div>
		<div class="blank2"></div>
	</body>
</html>	