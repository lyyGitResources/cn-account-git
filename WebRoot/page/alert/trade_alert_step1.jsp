<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@page import="com.hisupplier.commons.Config"%>
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
		<meta name="description" content="<s:if test="result.pageDescription != null"><s:property value="result.pageDescription"/></s:if>" />
		<script type="text/javascript">
			jQuery.extend(jQuery.validator.methods, {
				// 复选框至少一个选中
				oneChecked: function(value, element, param) {
					var result = false;

					if($(element).attr("checked")){
						result = true;
					}else{
						$(param).each(function(){
							if($(this).attr("checked")){
								result = true;
							}
						});
					}
					return result;
				}
			});
			
			$(function(){
				$(".operateTips").css("width", "934px");
			
				$("#alertForm").validateForm({
					rules: {
						product: {oneChecked: "#buy,#company"},
						<s:if test="result.tradeAlert.mode != 'category'">
						keyword: {required: true, maxlength: 60},
						</s:if>
						email: {email: true, maxlength:80}	//, remote:"/user/check_email.htm"
					},
					messages: {
						product: "请选择订阅内容",
						<s:if test="result.tradeAlert.mode != 'category'">
						keyword: "请输入订阅关键词，长度60个字符内",
						</s:if>
				 		email: "请输入电子邮箱"
				 	}
				}); 
				
				$("#forget").click(function(){
					var email = $.trim($("#email").val());
					$(this).attr("href", "/user/forget_passwd.htm?email=" + email);
					$(this).attr("target","_blank");
				});
				
				$("#member").click(function(){
					$('#passwdField,#notMember').show();
					$(this).hide();
					$("[name='passwd']").rules("add", {passwd: true,  messages: {passwd:'请输入登录密码'}});
				});
				
				$("#notMember").click(function(){
					$('#member').show();
					$('#passwdField,#notMember').hide();
					$("[name='passwd']").rules("remove");
				});
				
				<s:if test='result.tradeAlert.member == true'>
					$("#member").click();
				</s:if>
			});		
		</script>
	</head>
	<body>
		<!--产品列表部分-->
		<div class="area">
			<div class="mainNav"><span class="T">您正在浏览：</span><a href="<%=sysBase %>">首页</a>&nbsp;&gt;&nbsp;<span>商情订阅</span></div>
			<div class="blank6"></div>
			<%@ include file="/page/inc/messages.jsp"%>
			<div class="freesubbus">
				<div class="h1"><div class="tips"><span class="s1">必填项</span><span class="s2"><a href="http://help.cn.hisupplier.com/supplier/accidence/info.php?bid=181&id=187" target="_blank">帮助中心</a></span></div>注册成为会员，您就可以免费订阅商机</div>
				<form class="submitForm" id="alertForm" action="/user/trade_alert_step2.htm" method="post">
					<input type="hidden" name="mode" value="<s:property value='result.tradeAlert.mode' />"/>
					<input type="hidden" name="catIds" value="<s:property value='result.tradeAlert.catIds' />" />
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<th colspan="2">&nbsp;</th>
						</tr>
						<tr>
							<th><label><strong>订阅内容：</strong></label></th>
							<td>
								<input type="checkbox" name="product" value="true" <s:if test="result.tradeAlert.product == true || result.tradeAlert.sell == true ">checked</s:if>/>  供应信息<br />
								<input type="checkbox" name="buy" id="buy" value="true" <s:if test="result.tradeAlert.buy == true">checked</s:if>  /> 求购信息<br />
								<input type="checkbox" name="company" id="company" value="true" <s:if test="result.tradeAlert.company == true">checked</s:if>/> 公司信息
								<span id="productTip" style="margin-left:5px;"></span>
							</td>
						</tr>
						<s:if test="result.tradeAlert.mode == 'keyword'">
	                        <tr>
								<th><label><strong>关键词：</strong></label></th>
								<td><input name="keyword" value="<s:property value='result.tradeAlert.keyword' />" maxlength="60"/><span id="keywordTip" style="margin-left:5px;"></span></td>
	                        </tr>
						</s:if>
						<s:elseif test="result.tradeAlert.mode == 'category'">
							<tr>
								<th><label><strong>行业目录：</strong></label></th>
								<td class="c"><s:property value='result.tradeAlert.catNamePaths' /><span id="keywordTip" style="margin-left:5px;"></span></td>
							</tr>
	                    </s:elseif>
	                    <s:else>
	                        <tr>
								<th><label><strong>关键词：</strong></label></th>
								<td><input name="keyword" value="<s:property value='result.tradeAlert.keyword' />" maxlength="60" style=" width:200px; height:17px;"/><span id="keywordTip" style="margin-left:5px;"></span></td>
	                        </tr>
	                    </s:else>
						<tr>
							<th><label><strong>电子邮箱：</strong></label></th>
							<td>
								<input name="email" id="email" style="width: 200px;margin-bottom: 5px;" value="<s:property value='result.tradeAlert.email' />" />
								<span id="emailTip" style="margin-left:5px;"></span>
								<div class="t">
									<a href="#position" id="member">已经是会员！</a>
									<a href="#position" id="notMember" style="display:none">还不是会员！</a>
								</div>
							</td>
						</tr>
						<tr style="display:none;" id="passwdField">
							<th><label><strong>密码：</strong></label></th>
							<td>
								<input type="password" name="passwd" style="width:200px; height:17px;"/>
								<a href="#position" id="forget">忘记密码？</a>
								<span id="passwdTip"></span>
							</td>
						</tr>
						<tr>
							<th>&nbsp;</th>
							<td style="padding-top:15px;"><input type="submit" name="Submit" value="" class="button8"/></td>
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
