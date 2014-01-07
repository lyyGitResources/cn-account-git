<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>联系我们</title>
		<style type="text/css">
			.operateTips {
				width:620px;
			}
			.operateTips ul {
				width:600px;
			}
			.operateTips ul li{
				width:570px;
			}
			label.error{
				width:240px;
			}
		</style>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/chat.js"></script>
		<script type="text/javascript">
			$(function (){
				QQChat.showMultiQQChatLinks('100','239183271:',"qqChar");
				HChat.showChatLink(7172305, 'onlineChar', null);
				$("#contactUsForm").validateForm({
					rules: {
						reason: {required: true},
						subject: {required: true, maxlength:120},
						content: {required: true, maxlength:500},
						contact: {required: true, maxlength:80},
						email: {required: true, email:true, maxlength:80},
						validateCode: {required: true, rangelength:[5,5]}
					},
					messages: {
						reason: "请选择一个联系原因",
						subject: "请填写主题，长度120个字符内",
						content: "请填写内容，长度500个字符内",
						contact: "请填写名称，长度80个字符内",
						email: "请填写一个有效的邮箱，长度80个字符内",
						validateCode: '<s:text name="validateCode.required" />'
					}
				}); 
				
				$("#loadValidateCode").click(function (){
					loadValidateCode();
				});	
			});	
			
			function loadValidateCode(){
				Util.loadValidateCode(document.contactUsForm,"validateCodeImg","/validateCode/getImage");
			}	
		</script>
	</head>
	<body>
		<div class="main">
			<div style="color: rgb(255, 103, 2); font-size: 14px; margin-top: 10px; margin-bottom: 10px;">
				<b>您可以通过以下两种方式联系我们：</b>
			</div>
			<p>
				<b style="color: rgb(49, 103, 150);">1、在线服务</b><br/>
				服务时间：09：00-17：00 (周一至周五，法定节假日除外)	
			</p>
			<p>
				<span id="qqChar100239183271">
					<img height="17" width="19" style="margin-bottom: -3px;" src="/img/ico/ico_05.gif"/>
					<a style="margin-left:-8px;" href="http://sighttp.qq.com/cgi-bin/check?sigkey=fd0d84b54a22c3af27836b63c3bb5a4022f285a788b6281d5941bf2a52328400"; target=_blank; onclick="var tempSrc='http://sighttp.qq.com/wpa.js?rantime='+Math.random()+'&sigkey=fd0d84b54a22c3af27836b63c3bb5a4022f285a788b6281d5941bf2a52328400';var oldscript=document.getElementById('testJs');var newscript=document.createElement('script');newscript.setAttribute('type','text/javascript'); newscript.setAttribute('id', 'testJs');newscript.setAttribute('src',tempSrc);if(oldscript == null){document.body.appendChild(newscript);}else{oldscript.parentNode.replaceChild(newscript, oldscript);}return false;"></a>
				</span>
			</p>
			<p>
				<b style="color: rgb(49, 103, 150);">2、发送邮件</b><br/>
				请填写下列表单，我们将尽快回复给您：
			</p>
			<form id="contactUsForm" name ="contactUsForm" action="/user/contact_us_submit.htm" method="post">
				<input type="hidden" name="validateCodeKey" value="<s:property value="validateCodeKey"/>"/>
				<%@ include file="/page/inc/messages.jsp"%>
				<table cellspacing="3" style="background: rgb(247, 247, 247) none repeat scroll 0% 0%; -moz-background-clip: border; -moz-background-origin: padding; -moz-background-inline-policy: continuous;">
					<tbody>
						<tr>
							<th style="width:138px;"><span>*</span> 联系原因：</th>
							<td style="width:270px;">
								<label>
									<select style="width: 265px;float:left;" name="reason">
						                <option value="">请选择联系原因</option>
						                <option value="如何买/搜索产品/联系卖家">如何买/搜索产品/联系卖家</option>
						                <option value="如何卖/寻找买家/联系买家">如何卖/寻找买家/联系买家</option>
						                <option value="注册/登录/密码/后台操作">注册/登录/密码/后台操作</option>
						                <option value="修改公司名称">修改公司名称</option>
						                <option value="注销帐号">注销帐号</option>
						                <option value="找不到产品及相关产品信息">找不到产品及相关产品信息</option>
										<option value="展会相关">展会相关</option>
										<option value="培训相关">培训相关</option>
					                </select>
								</label>
							</td>
							<td id="reasonTip" style="width: 283px;"></td>
						</tr>
						<tr>
                        	<th><span>*</span> 主&nbsp;&nbsp;&nbsp;&nbsp;题：</th>
							<td><input type="text" style="width: 260px;*width: 261px;float:left;" name="subject" value="<s:property value="subject"/>"/></td>
							<td id="subjectTip"></td>
						</tr>
						<tr>
                        	<th><span>*</span> 内&nbsp;&nbsp;&nbsp;&nbsp;容：</th>
							<td>
								<textarea style="width: 260px;*width: 263px; height: 150px;float:left;" name="content"><s:property value="content"/></textarea>
							</td>
							<td id="contentTip"></td>
						</tr>
						<tr>
                        	<th><span>*</span> 姓&nbsp;&nbsp;&nbsp;&nbsp;名：</th>
							<td><input type="text" name="contact" style="float:left;width:150px;" value="<s:property value="contact"/>"/></td>
							<td id="contactTip"></td>
						</tr>
						<tr>
                        	<th>会员账号：</th>
							<td><input type="text" name="memberId" value="<s:property value="memberId"/>" style="width:150px;"/></td>
						</tr>
						<tr>
                        	<th><span>*</span> 电子邮箱：</th>
							<td><input type="text" name="email" style="float:left;width:150px;" value="<s:property value="email"/>"/></td>
							<td id="emailTip"></td>
						</tr>
						<tr>
	                       	<th><span>*</span> 验证码：</th>
							<td>
								<input id="validateCode" type="text" style="width: 50px;float:left;margin-right:5px;" name="validateCode" value="<s:property value="validateCode"/>" autocomplete="off" maxlength="5"/>
								<a href="#" id="loadValidateCode" style="float:left;">看不清，换一张</a>
							</td>
							<td id="validateCodeTip"></td>
						</tr>
						<tr>
							<th></th>
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
							<td></td>
						</tr>
						<tr>
                           	<th> </th>
							<td style="padding-top: 20px;"><input type="submit" class="submit3" value="立即发送"/></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</body>
</html>