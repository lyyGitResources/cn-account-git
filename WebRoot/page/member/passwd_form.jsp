<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="user.editPasswdTitle" /></title>
		<meta name="memoinfo" content="<a target='_blank' href='<%=Config.getString("sys.base") %>/page/notice/notice.html'>点击查看</a>为何要提高密码复杂度"/>
		<script type="text/javascript">
			$(function (){
				$("#passwdForm").validateForm({
					rules: {
						oldPasswd: {passwd:true, remote:"/user/check_password.do"},
						passwd:{passwd:true, checkpassword: true},
						confirmPasswd:{passwd:true,equalTo:"#passwd", checkpassword: true}
					},
					messages: {
						oldPasswd: {passwd:'<s:text name="passwd.required" />'},
						passwd: { 
							passwd:'<s:text name="passwd.required" />',
							checkpassword: '<s:text name="user.passwd.easy" />'
							},
						confirmPasswd: {
							equalTo: '<s:text name="user.confirmPasswd.error" />',
							checkpassword: '<s:text name="user.passwd.easy" />'
						}
					}
				});
			});
		</script>
	</head>
	<body>
		<s:if test="result.alert">
		<div class="operateTips operateTip">
			您的密码过于简单，存在被盗号风险，请修改您的密码。</li>
		</div>
		</s:if>
		<h2><s:text name="input.notice" /></h2>
		<div style="color: red; margin-top: 20px;">重要提示：每天互联网都会有大量用户的帐号存在着严重被盗的风险。如果你正在使用与其他网站相同的密码，建议你尽快修改。</div>
		<form id="passwdForm" name="passwdForm" action="/member/passwd_edit_submit.htm">
			<s:token />
			<table class="formTable">
				<tr>
					<th>
						<span class="red">*</span>
						<s:text name="user.oldPasswd" />：
					</th>
					<td>
						<input autocomplete="off" type="password" id="oldPasswd" name="oldPasswd" style="width: 160px; height: 18px;" />
						<div></div>
					</td>
				</tr>
				<tr>
					<th>
						<span class="red">*</span>
						<s:text name="user.passwd" />：
					</th>
					<td>
						<input type="password" id="passwd" name="passwd" style="width: 160px; height: 18px;" />
						<div class="fieldTips">
请在您的密码中使用字母和编号以提高帐户的安全。输入6至20个字符，不包含空格。<br/>
不要在您的密码中使用以下信息：
<br/>1. 您的会员ID或邮箱地址;
<br/>2. 您的电话/传真/手机号码或邮编/邮政编码;
<br/>3. 连续或重复数字和字母 - 例如： 123456，abcdef，aaaaaa，888888，789456。
						</div>
					</td>
				</tr>
				<tr>
					<th>
						<span class="red">*</span>
						<s:text name="user.confirmPasswd" />：
					</th>
					<td>
						<input type="password" id="confirmPasswd" name="confirmPasswd" style="width: 160px; height: 18px;" />
						<div></div>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name="button.submit"/>" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="reset" value="<s:text name="button.reset"/>" />
			</div>
		</form>
	</body>
</html>
