<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.cn.account.entity.Image"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.cn.account.basic.LoginUser"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:if test="loginUser.admin"><s:text name="user.contactTitle" /></s:if><s:else><s:text name="user.myUserTitle" /></s:else></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js?v=1"></script>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/lib/jquery.html5-placeholder-shim.js"></script>
		<style>
		.placeholder { line-height: 20px; }
		</style>
		<script type="text/javascript" src="/js/user.js"></script>
		<script type="text/javascript" src="/js/contact_im.js"></script>
		<script type="text/javascript">
			var group_item = '<li><input class="qq_name" name="qq_name" type="text" autocomplete="false" placeholder="请输入网站显示名" > <input class="qq_id" name="qq_id" type="text" placeholder="请输入QQ账号"> <input class="group_del" type="button" onclick="$(this).parent().remove()" value="删除"></li>';
			var $qq_group, $qq_message, $bigqq_group;

			$(function (){
				// QQ
				$qq_group = $("#qq_group");
				$qq_message = $("#qq_message");
				$bigqq_group = $("#bigqq_group");
				
				$("#group_add").click(function() {
					var count = $qq_group.find("li").length;
					var $group_item = $(group_item);
					$group_item.find(".qq_id, .qq_name").blur(function() {
						var message = validateQQGroup($(".qq_id"), $(".qq_name"));
						if (message !== true) {
							$qq_message.html(message).show();
						} else {
							$qq_message.hide();
						}
					});
					if (count < 5) $qq_group.append($group_item);
				});
				
				$("#qq_typetrue, #qq_typefalse").click(function() {
					if (this.value === 'true') {
						$bigqq_group.show();
						$qq_group.hide();
					} else {
						$bigqq_group.hide();
						$qq_group.show();
					}
				});
				
				$(".qq_id, .qq_name").blur(function() {
					var message = validateQQGroup($(".qq_id"), $(".qq_name"));
					if (message !== true) {
						$qq_message.html(message).show();
					} else {
						$qq_message.hide();
					}
				});
				$(".bigqq_id, .bigqq_name").blur(function() {
					var message = validateQQGroup($(".bigqq_id"), $(".bigqq_name"), true);
					if (message !== true) {
						$qq_message.html(message).show();
					} else {
						$qq_message.hide();
					}
				});
				
				getProvince("province","city","country",null,"countryCode");
				
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
				
				$("#contactForm").validateForm({
					submitHandler: function(form) {
						var message;
						if ($("#qq_type").val() === 'true') {
							message = validateQQGroup($(".bigqq_id"), $(".bigqq_name"), true);
						} else {
							message = validateQQGroup($(".qq_id"), $(".qq_name"));
						}
						if (message !== true) {
							$qq_message.html(message).show();
							return false;
						} else {
							form.submit();
						}
					},
					rules: {
						contact: {required:true, maxlength:20},
						province: {required:true},
						city: {required:true},
						street: {required:true, maxlength:200},
						zip: {number:true, minlength:6, maxlength:6},
						department: {maxlength:50},
						job: {maxlength:50},
						email: {required:true, email:true, maxlength:80,remote:"/user/check_email.htm?userId=" + $("#userId").val()},
						tel1: {required:true, telAreaCode:"#tel2"},
						tel2: {required:true, telNumber:"#tel1"},
						fax1: {faxAreaCode:"#fax2"},
						fax2: {faxNumber:"#fax1"},
						mobile: {digits :true, minlength:11, maxlength:11},
						title: {maxlength: 5, required: function() { return $("input[name='sex']:checked").val() === '4' ? true : false; }}
					},
					messages: {
						contact: '<s:text name="user.contact.required" />',
						province: '<s:text name="user.province.required" />',
						city: '<s:text name="user.city.required" />',
						street: '<s:text name="user.street.required" />',
						zip: '<s:text name="user.zip.maxlength" />',
						department: '<s:text name="user.department.maxlength" />',
						job: '<s:text name="user.job.maxlength" />',
						email: {email:'<s:text name="email.required" />'},
						tel1: '<s:text name="tel.required" />',
						tel2: '<s:text name="tel.required" />',
						fax1: '<s:text name="fax.required" />',
						fax2: '<s:text name="fax.required" />',
						mobile: '<s:text name="user.mobile.maxlength" />'
					}
				});
				
				$("[name='tel2']").blur(function(){
					$("#telTip .error").css("margin-left", "0");
				});
				
				if($("#userState").val() == "10"){
					$("#redText").text("（审核不通过）");
				}
			});
			
			function openWindow(url) {
				var param = "height=700px,width=800px,left=250px,top=150px,resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=no,location=no";
				window.open(url, "", param);
			}
			
			function delectCheck(moreId){
				deleteConfirm("/member/contact_more_delete.htm?userId="+moreId);
			}
			
		</script>
	</head>
	<body>
		<h2><s:text name="input.notice" /></h2> 
		<s:if test="result.user.state == 10">
			<div style="color:red;padding:10px 20px;">
				<ul>
					<li>
						<span>公司联系人信息审核未通过，请根据<a id='adminLog' href="javascript:void(0);">未通过原因</a>修改</span>
					</li>
				</ul>
			</div>
		</s:if>
		<input type="hidden" value="${result.user.state }" id="userState" />
		<form id="contactForm" action="/member/contact_edit_submit.htm" method="post">
			<s:token />
			<input type="hidden" id="userId" name="userId" value="<s:property value='result.user.userId'/>">
			<input type="hidden" id="countryCode" name="countryCode" value="<s:property value='result.user.countryCode'/>"/>
			<input type="hidden" name="createTime" value="<s:property value='result.user.createTime'/>"/>
			<input type="hidden" name="modifyTime" value="<s:property value='result.user.modifyTime'/>"/>
			<input type="hidden" name="googleLocal" id="googleLocal" value="<s:property value='result.user.googleLocal' />"/>
			<input type="hidden" name="preLoginTime" value="<s:property value='result.user.preLoginTime'/>"/>
			<input type="hidden" name="preLoginIP" value="<s:property value='result.user.preLoginIP'/>"/>
			<input type="hidden" name="loginTimes" value="<s:property value='result.user.loginTimes'/>"/>
			<input type="hidden" name="admin" value="<s:property value='result.user.admin'/>"/>
			<table class="formTable">
				<tr>
					<th><s:text name="modifyTime" />：</th>
					<td><s:property value="result.user.modifyTime" /></td>
				</tr>
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
						
						<input type="radio" name="sex" value="1" <s:if test="result.user.sex==1">checked="checked"</s:if> />&nbsp;先生&nbsp;&nbsp;
						<input type="radio" name="sex" value="2" <s:if test="result.user.sex==2">checked="checked"</s:if> />&nbsp;女士&nbsp;&nbsp;
						<input type="radio" name="sex" value="3" <s:if test="result.user.sex==3">checked="checked"</s:if> />&nbsp;小姐&nbsp;&nbsp;
						<input type="radio" name="sex" value="4" <s:if test="result.user.sex==4">checked="checked"</s:if> />&nbsp;其他
						<input type="text" name="title" size="15" maxlength="5" value="<s:property value='result.user.title' />" />
						<div id="contactTip"></div>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;联系地址：</th>
					<td>
						<select id="province" name="province" style="width: 130px; margin-right: 5px;"></select>
						<select id="city" name="city" style="width: 130px; margin-right: 5px;"></select>
						<select id="country" name="town" style="width: 130px; margin-right: 5px;"></select>
						<span id="cityTip"></span>
						<textarea type="text" name="street" placeholder="街道地址，不需要重复填写省/市/区" style="width: 402px; margin-top:4px; font-size: 12px;"><s:property value='result.user.street' /></textarea>
						<div id="streetTip"></div>
					</td>
				</tr>
				<tr>
					<th>
						&nbsp;电子地图坐标：
					</th>
					<td>
						<span id="googleLocalSpan"><s:property value='result.user.googleLocal' /></span>
						<a id="a_map" style="text-decoration: none;" href="#">
							<img border="0" align="absmiddle" src="/img/map.gif"/>重新定位
						</a>
						<script>
						$("#a_map").click(function() {
						  openWindow('/page/member/inc/select_map.jsp?' + $("#googleLocalSpan").html());
						  return false;
						});
						</script>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.zip" />：</th>
					<td><input type="text" name="zip" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.zip' />" /></td>
				</tr>
				<tr>
					<th><s:text name="user.department" />：</th>
					<td><input type="text" name="department" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.department' />" /></td>
				</tr>
				<tr>
					<th><s:text name="user.job" />：</th>
					<td>
						<input type="text" name="job" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.job' />" /></td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="email" />：</th>
					<td>
						<input type="text" id="email" name="email"  style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.email' />" maxlength="80"/>
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span><s:text name="user.tel" />：</th>
					<td>
						<span style="margin-right:70px;"><s:text name="user.cityCode" />：</span>
						<span><s:text name="user.number" />：</span><br />
						<input type="text" id="tel1" name="tel1" style="width: 50px;" value="<s:property value='result.user.tel1' />" />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<input type="text" id="tel2" name="tel2" style="width: 100px;" value="<s:property value='result.user.tel2' />" />
						<div id="telTip" style="width:580px;"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.fax" />：</th>
					<td>
						<input type="text" name="fax1" id="fax1" style="width: 50px;" value="<s:property value='result.user.fax1' />" />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<input type="text" name="fax2" id="fax2" style="width: 100px;" value="<s:property value='result.user.fax2' />" />
						<div id="faxTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name="user.mobile" />：</th>
					<td>
						<input type="text" name="mobile" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.mobile' />" />
						<%--<input id="bindingMobile" type="button" value="绑定号码"/>--%>
					</td>
				</tr>
				<s:if test="loginUser.admin && loginUser.memberType == 2">
					<tr>
						<th><s:text name="user.sms" />：</th>
						<td>
							<s:if test="result.user.sms == true">
								<input type="radio" name="sms" value="true" checked="checked" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
								<input type="radio" name="sms" value="false" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
							</s:if>
							<s:else>
								<input type="radio" name="sms" value="true" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
								<input type="radio" name="sms" value="false" checked="checked" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
							</s:else>
							<div class="fieldTips">
								有询盘时发短信提醒我(仅限中国移动、联通号码，格式：+86-13888888888、13888888888) 
								<div style="color:red;">特别提醒： 由于通道原因，联通155、186暂时不能发送，敬请谅解！</div>
							</div>
						</td>
					</tr>
				</s:if>
				<s:else>
					<input type="hidden" id="sms" name="sms" value="<s:property value="user.sms"/>">
				</s:else>
				<tr>
					<th><s:text name="user.showMobile" />：</th>
					<td>
						<s:if test="result.user.showMobile == true">
							<input type="radio" name="showMobile" value="true" checked="checked" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="showMobile" value="false" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:if>
						<s:else>
							<input type="radio" name="showMobile" value="true" />&nbsp;<s:text name="yes" />&nbsp;&nbsp;
							<input type="radio" name="showMobile" value="false" checked="checked" />&nbsp;<s:text name="no" />&nbsp;&nbsp;
						</s:else>
					</td>
				</tr>
				<tr>
					<th>是否企业QQ：</th>
					<td>
						<s:radio id="qq_type" theme="simple" name="qq_type" list="#{true: '是', false: '否'}" value="result.user.qq_type" />
					</td>
				</tr>
				<tr>
					<th>
						<s:text name="user.qq" />：
					</th>
					<td>
						<ul id="bigqq_group" class="qq_group" <s:if test="!result.user.qq_type">style="display: none;"</s:if>>
						<s:iterator value="result.user.talks" id="talk" status="st">
						<s:if test="#st.index == 0">
						<li>
							<input value="${talk.name }" class="bigqq_name" name="bigqq_name" type="text" autocomplete="false" placeholder="请输入网站显示名"/>
							<input value="${talk.code }" class="bigqq_id" name="bigqq_id" type="text" placeholder="请输入QQ账号"/>
						</li>
						</s:if>
						</s:iterator>
						</ul>
						<ul id="qq_group" class="qq_group" <s:if test="result.user.qq_type">style="display: none;"</s:if>>
							<s:iterator value="result.user.talks" id="talk" status="st">
							<li>
							<input value="${talk.name }" class="qq_name" name="qq_name" type="text" autocomplete="false" placeholder="请输入网站显示名"/>
							<input value="${talk.code }" class="qq_id" name="qq_id" type="text" placeholder="请输入QQ账号"/>
							<s:if test="#st.index == 0">
							<input id="group_add" type="button" value="添加"/>
							</s:if>
							<s:else>
							<input class="group_del" type="button" onclick="$(this).parent().remove()" value="删除">
							</s:else>
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
						<input type="text" name="msn" style="width: 160px; margin-right: 5px;" value="<s:property value='result.user.msn' />" maxlength="80"/>
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
						<input type="text" name="skype" style="width: 160px;margin-right: 5px;" value="<s:property value='result.user.skype' />" />
					</td>
				</tr>
				--%>
				<tr>
					<th><s:text name="user.preLoginTime" />：</th>
					<td>
						<s:property value="result.user.preLoginTime" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.preLoginIP" />：</th>
					<td>
						<s:property value="result.user.preLoginIP" />
					</td>
				</tr>
				<tr>
					<th><s:text name="user.loginTimes" />：</th>
					<td>
						<s:property value="result.user.loginTimes" />
					</td>
				</tr>
			</table>
			<s:if test="result.user.admin == true">
				<s:if test="result.moreId == 0">
					<div class="buttonLeft">
					<input type="button" onclick="location.href='/member/contact_more.htm?userId=<s:property value='result.user.userId' />'" value="<s:text name="user.moreContactAdd" />" />
					</div>	
				</s:if>
				<s:else>
					<div class="buttonLeft">
					<input type="button" onclick="location.href='/member/contact_more.htm?userId=<s:property value='result.user.userId' />'" value="<s:text name="user.moreContactModify" />" />
					<input type="button" onclick="delectCheck(<s:property value='result.moreId' />)" value="<s:text name="user.moreContactRemove" />" />
					</div>
				</s:else>
			</s:if>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name="button.submit"/>" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="reset" value="<s:text name="button.reset"/>" />
			</div>
	
		</form>
		<div id="selectImgDialog"></div>
		<div id="adminLogDialog"></div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>