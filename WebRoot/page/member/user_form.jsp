<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.user.userId > 0">
	<s:set name="pageTitle" value="%{getText('user.editTitle')}" />
	<s:set name="formAction" value="'/member/user_edit_submit.htm'" />
	<s:set name="showGetButton" value="'false'" />
</s:if>
<s:else>
	<s:set name="pageTitle" value="%{getText('user.addTitle')}"></s:set>
	<s:set name="formAction" value="'/member/user_add_submit.htm'" />
	<s:set name="showGetButton" value="'true'" />
</s:else>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:if test="loginUser.admin"><s:property value="#pageTitle"/></s:if><s:else>修改我的账号</s:else></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.html5-placeholder-shim.js"></script>
		<style>
		.placeholder { line-height: 20px; }
		</style>
		<script type="text/javascript" src="/js/user.js"></script>
		<script type="text/javascript" src="/js/contact_im.js"></script>
		<script type="text/javascript">
			$(function (){
				var $qq_message = $("#qq_message");
				$(".qq_id, .qq_name").blur(function() {
					var message = validateQQGroup($(".qq_id"), $(".qq_name"));
					if (message !== true) {
						$qq_message.html(message).show();
					} else {
						$qq_message.hide();
					}
				});
				getProvince("province","city","country","tel1","countryCode");
				
				$("#selectImgDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 600,
					width: 770,
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
					$("#adminLogDialog").load("/basic/admin_log.do?tableName=Users&tableId=<s:property value='loginUser.userId' />",{random: Math.random()});
					$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
					$("#adminLogDialog").dialog('open');
				});
				
				$("#userForm").validateForm({
					submitHandler: function(form) {
						var message = validateQQGroup($(".qq_id"), $(".qq_name"));
						if (message !== true) {
							$qq_message.html(message).show();
							return false;
						} else {
							form.submit();
						}
					},
					rules: {
						contact: {required:true, maxlength:20},
						passwd:{passwd:true},
						province:{required:true},
						city:{required:true},
						street: {required:true, maxlength:200},
						zip: {number:true,minlength:6,maxlength:6},
						department: {maxlength:50},
						job: {maxlength:50},
						email: {required:true,maxlength:80,remote:"/user/check_email.htm?userId=" + $("#userId").val()},
						tel1: {required:true, telAreaCode:"#tel2"},
						tel2: {required:true, telNumber:"#tel1"},
						fax1: {faxAreaCode:"#fax2"},
						fax2: {faxNumber:"#fax1"},
						mobile: {digits :true, minlength:11, maxlength:11},
						qq: {number:true, maxlength:20},
						qqcode:{maxlength:1000},
						title: {maxlength: 5, required: function() { return $("input[name='sex']:checked").val() === '4' ? true : false; }}
					},
					messages: {
						contact: '<s:text name="user.contact.required" />',
						passwd: '<s:text name="passwd.required" />',
						province:'<s:text name="user.province.required" />',
						city:'<s:text name="user.city.required" />',
						street: '<s:text name="user.street.required" />',
						zip: '<s:text name="user.zip.maxlength" />',
						department: '<s:text name="user.department.maxlength" />',
						job:  '<s:text name="user.job.maxlength" />',
						email: {required:'<s:text name="email.required" />'},
						tel1: '<s:text name="tel.required" />',
						tel2: '<s:text name="tel.required" />',
						fax1: '<s:text name="fax.required" />',
						fax2: '<s:text name="fax.required" />',
						mobile: '<s:text name="user.mobile.maxlength" />',
						qq: '<s:text name="user.qq.maxlength" />',
						qqcode:'<s:text name="user.qqcode.maxlength"/>'
					}
					
				});
				$("#getStreetButton").click(function (){
					$("#street").val($("#adminStreet").val());
				});
				$("#getZipButton").click(function (){
					$("#zip").val($("#adminZip").val());
				});
				$("#getTelButton").click(function (){
					var adminTel = $("#adminTel").val()
					if(adminTel){
						tmps = adminTel.split("-");
						if(tmps.length == 2){
							$("#tel1").val(tmps[0]);
							$("#tel2").val(tmps[1]);
						}
					}
				});
				$("#getFaxButton").click(function (){
					var adminFax = $("#adminFax").val()
					if(adminFax){
						tmps = adminFax.split("-");
						if(tmps.length == 2){
							$("#fax1").val(tmps[0]);
							$("#fax2").val(tmps[1]);
						}
					}
				});

				$("[name='tel2']").blur(function(){
					$("#telTip .error").css("margin-left", "0");
				});

				if($("#userState").val() == "10"){
					$("#redText").text("（审核不通过）");
					}
			});
		</script>
	</head>
	<body>
		<h2>
			<s:text name="input.notice" />
		</h2>
		<s:if test="result.user.state == 10">
		<div style="color:red;padding:10px 20px;">
			<ul>
				<li>
					<span>子账号信息审核未通过，请根据<a id='adminLog' href="javascript:void(0);">未通过原因</a>修改</span>
				</li>
			</ul>
		</div>
		</s:if>
		<form id="userForm" action="<s:if test="loginUser.admin"> <s:property value="#formAction"/></s:if><s:else>/member/sonUser_edit_submit.htm</s:else>" method="post">
			<s:token />
			<input type="hidden" value="${result.user.state }" id="userState" />
			<input type="hidden" id="userId" name="userId" value="<s:property value='result.user.userId'/>"/>
			<input type="hidden" id="countryCode" name="countryCode" value="<s:property value='result.user.countryCode'/>"/>
			<input type="hidden" id="adminStreet" name="adminStreet" value="<s:property value='result.user.adminStreet'/>"/>
			<input type="hidden" id="adminTel" name="adminTel" value="<s:property value='result.user.adminTel'/>"/>
			<input type="hidden" id="adminFax" name="adminFax" value="<s:property value='result.user.adminFax'/>"/>
			<input type="hidden" id="adminZip" name="adminZip" value="<s:property value='result.user.adminZip'/>"/>
			<input type="hidden" name="admin" value="<s:property value='result.user.admin'/>"/>
			<input type="hidden" name="sms" value="<%=false%>" />
			
			<table class="formTable">
				<tr>
					<th><s:text name="user.headImg" />：</th>
					<td>
						<%
							int imgType = Image.HEAD;
							int maxNum = 1;
							String imgSrcTag = "imgSrc";
							String imgPathTag = "headImgPath";
							String imgIdTag = "headImgId";
						%>
						<s:set name="imgSrc" value="result.user.headImgSrc" />
						<s:set name="imgPath" value="result.user.headImgPath" />
						<s:set name="imgId" value="0" />
						<s:set name="imgExts" value="'*.jpg;*.jpeg;*.gif'" />
						<s:set name="imgSize" value="'100'" />
						<%@ include file="/page/inc/image_bar.jsp" %>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="user.contact" />：</th>
					<td>
						<input type="text" name="contact" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.contact' />" />
						<input type="radio" name="sex" value="1" <s:if test="result.user.sex == 1">checked="checked"</s:if> />&nbsp;先生&nbsp;&nbsp;
						<input type="radio" name="sex" value="2" <s:if test="result.user.sex == 2">checked="checked"</s:if> />&nbsp;女士&nbsp;&nbsp;
						<input type="radio" name="sex" value="3" <s:if test="result.user.sex == 3">checked="checked"</s:if>/>&nbsp;小姐&nbsp;&nbsp;
						<input type="radio" name="sex" value="4" <s:if test="result.user.sex == 4">checked="checked"</s:if>/>&nbsp;其他
						<input type="text" maxlength="5" size="15" name="title" value="<s:property value='result.user.title' />" />
						<div id="contactTip"></div>
					</td>
				</tr>
				
				<tr <s:if test="loginUser.admin">style=""</s:if><s:else>style="display: none"</s:else>>
					<th><span class="red">*</span>&nbsp;<s:text name="user.passwd" />：</th>
					<td>
						<input type="text" name="passwd" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.passwd' />" />
					</td>
				</tr>
				<s:if test="loginUser.admin">
				<tr>
					<th>&nbsp;权限：</th>
					<td>
						<div class="options">
							<ul>
							<s:iterator value="result.user.privilegeItems" id="item">
								<li>
									<input type="checkbox" name="privilege" <s:property value='#item.checked' /> value='<s:property value="#item.value"/>' />
									&nbsp;<s:property value="#item.name"/>
								</li>
							</s:iterator>
							</ul>
						</div>
					</td>
				</tr>
				</s:if>
				<tr>
					<th><span class="red">*</span>&nbsp;联系地址：</th>
					<td>
						<select id="province" name="province" style="width: 130px; margin-right: 5px;"></select>
						<select id="city" name="city" style="width: 130px; margin-right: 5px;"></select>
						<select id="country" name="town" style="width: 130px; margin-right: 5px;"></select>
						<span id="cityTip"></span>
						<textarea type="text" id="street" placeholder="街道地址，不需要重复填写省/市/区" name="street" style="width: 404px; margin-top: 4px; font-size: 12px;"><s:property value='result.user.street' /></textarea>
						<div id="streetTip"></div>
						<s:if test="#showGetButton == 'true'">
							<input type="button" id="getStreetButton" value="从主帐号获取" />
						</s:if>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.zip" />：</th>
					<td>
						<input type="text" id="zip" name="zip" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.zip' />" />
						<s:if test="#showGetButton == 'true'">
							<input stype="float:left;" type="button" id="getZipButton" value="从主帐号获取" class="button10" />
						</s:if>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.department" />：</th>
					<td>
						<input type="text" name="department" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.department' />" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.job" />：</th>
					<td>
						<input type="text" name="job" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.job' />" />
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="email" />：</th>
					<td>
						<input type="text" id="email" name="email" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.email' />" maxlength="80"/>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="user.tel" />：</th>
					<td>
						<span style="margin-right:70px;"><s:text name="user.cityCode" />：</span>
						<span><s:text name="user.number" />：</span>
						<br />
							<input type="text" id="tel1" name="tel1" style="width: 50px;" value="<s:property value='result.user.tel1' />" />
							&nbsp;&nbsp;-&nbsp;&nbsp;
							<input type="text" id="tel2" name="tel2" style="width: 100px;" value="<s:property value='result.user.tel2' />" />
						<s:if test="#showGetButton == 'true'">
							<input type="button" id="getTelButton" value="从主帐号获取" class="button10" />
						</s:if>
						<div id="telTip" style="width:580px;"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.fax" />：</th>
					<td>
						<input type="text"  id="fax1" name="fax1" style="width: 50px;" value="<s:property value='result.user.fax1' />" />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<input type="text" id="fax2" name="fax2" style="width: 100px;" value="<s:property value='result.user.fax2' />" />
						<s:if test="#showGetButton == 'true'">
							<input type="button" id="getFaxButton" value="从主帐号获取" class="button10" />
						</s:if>
						<div id="faxTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.mobile" />：</th>
					<td>
						<input type="text" name="mobile" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.mobile' />" />
					</td>
				</tr>
				<%-- 
				<tr>
					<th><s:text name="user.sms" />：</th>
					<td>
						<s:if test="result.user.sms == false">
							<input type="radio" name="sms" value="true" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="sms" value="false" checked="checked" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:if>
						<s:else>
							<input type="radio" name="sms" value="true" checked="checked" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="sms" value="false" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:else>
					</td>
				</tr>
				--%>
				<tr>
					<th><s:text name="user.showMobile" />：</th>
					<td>
						<s:if test="result.user.showMobile == false">
							<input type="radio" name="showMobile" value="true" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="showMobile" value="false" checked="checked" /><s:text name="no" />
						</s:if>
						<s:else>
							<input type="radio" name="showMobile" value="true" checked="checked" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="showMobile" value="false" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:else>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.qq" />：</th>
					<td>
						<ul id="qq_group" class="qq_group" <s:if test="result.user.qq_type">style="display: none;"</s:if>>
						<s:if test="result.user.talks.size == 0">
						<li>
						<input value="${talk.name }" class="qq_name" name="qq_name" type="text" autocomplete="false" placeholder="请输入网站显示名"/>
						<input value="${talk.code }" class="qq_id" name="qq_id" type="text" placeholder="请输入QQ账号"/>
						</li>
						</s:if>
						<s:iterator value="result.user.talks" id="talk">
						<li>
						<input value="${talk.name }" class="qq_name" name="qq_name" type="text" autocomplete="false" placeholder="请输入网站显示名"/>
						<input value="${talk.code }" class="qq_id" name="qq_id" type="text" placeholder="请输入QQ账号"/>
						</li>
						</s:iterator>
						</ul>
						<label id="qq_message" style="display: none" class="error"></label>
					</td>
				</tr>
				<%--
				<tr>
					<th><s:text name="user.msn" />：</th>
					<td>
						<input type="text" name="msn" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.msn' />" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.msncode" />：</th>
					<td>
						<textarea name="msncode" style="width:422px; height:80px; font-size:12px; line-height: 20px;"><s:property value="result.user.msncode"/></textarea>
						<br/><a href="http://im.live.cn/imme/about.htm" target="_blank">获取MSN在线状态代码</a>
					</td>
				</tr>
				 --%>
				<%-- 
				<tr>
					<th><s:text name="user.skype" />：</th>
					<td>
						<input type="text" name="skype" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.skype' />" />
					</td>
				</tr>
				--%>
				<tr>
					<th><s:text name="user.show" />：</th>
					<td>
						<s:if test="result.user.show == false">
							<input type="radio" name="show" value="true" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="show" value="false" checked="checked" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:if>
						<s:else>
							<input type="radio" name="show" value="true" checked="checked" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="show" value="false" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:else>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name="button.submit"/>" />
				<input type="reset" value="<s:text name="button.reset"/>" />
			</div>
		</form>
		<div id="selectImgDialog"></div>
		<div id="adminLogDialog"></div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>