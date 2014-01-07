<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="user.moreContactTitle"/></title>	
		<script type="text/javascript" src="/js/user.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
					$("#contactForm").validateForm({
					rules: {
						contact: {required:true, maxlength:20},
						department: {maxlength:50},
						job: {maxlength:50},
						tel1: {required:true, telAreaCode:"#tel2"},
						tel2: {required:true, telNumber:"#tel1"},
						fax1: {faxAreaCode:"#fax2"},
						fax2: {faxNumber:"#fax1"},
						mobile: {digits :true, minlength:11, maxlength:11},
						email: {required:true, email:true, maxlength:80,remote:"/user/check_email.htm?userId=" + $("#userId").val()},
						street: {required:true, maxlength:200},
						zip: {number:true, minlength:6, maxlength:6},
						title: {maxlength: 5, required: function() { return $("input[name='sex']:checked").val() === '4' ? true : false; }}
					},
					messages: {
						contact: '<s:text name="user.contact.required" />',
						department: '<s:text name="user.department.maxlength" />',
						job: '<s:text name="user.job.maxlength" />',						
						tel1: '<s:text name="tel.required" />',
						tel2: '<s:text name="tel.required" />',
						fax1: '<s:text name="fax.required" />',
						fax2: '<s:text name="fax.required" />',
						mobile: '<s:text name="user.mobile.maxlength" />',
						email: {email:'<s:text name="email.required" />'},
						street: '<s:text name="user.street.required" />',
						zip: '<s:text name="user.zip.maxlength" />'					
					}
				});	
			});
		</script>
		<style type="text/css">
			.contactForm{color:red;}
			.postSuggestdiv{float:left;}
		</style> 
	</head>
	<body>
		<div id="page">
			<div class="pageNotice"><s:actionerror/></div>
			<form action="/member/contact_more_submit.htm" method="post" id="contactForm" name="contactForm">
			<s:token/>
				<table class="formTable">
					<!-- contact name -->
					<tr>
						<th align="right"><span class="red">*</span><s:text name="user.contact" />：</th>
						<td width="81%">
							<div class="postSuggestdiv">
								<input type="text" id="contact" name="contact" value="<s:property value="result.user.contact"/>" style="width:160px;" />
								<input type="radio" name="sex" value="1" <s:if test="result.user.sex==1">checked="checked"</s:if> />&nbsp;先生&nbsp;&nbsp;
								<input type="radio" name="sex" value="2" <s:if test="result.user.sex==2">checked="checked"</s:if> />&nbsp;女士&nbsp;&nbsp;	
								<input type="radio" name="sex" value="3" <s:if test="result.user.sex==3">checked="checked"</s:if> />&nbsp;小姐&nbsp;&nbsp;	
								<input type="radio" name="sex" value="4" <s:if test="result.user.sex==4">checked="checked"</s:if> />&nbsp;其他
								<input type="text" name="title" size="10" maxlength="5" value="<s:property value='result.user.title' />" />
								<div id="contactTip"></div>
							</div>
						</td>
					</tr>
					<!-- department -->
					<tr>
						<th align="right"><s:text name="user.department" />：</th>
						<td>
							<div style="float:left "><input type="text" id="department" name="department" value="<s:property value="result.user.department"/>" style="width:160px;"></div>
						</td>
					</tr>
					<!-- job -->
					<tr>
						<th align="right"><s:text name="user.job"/>：</th>
						<td>
							<div style="float:left "><input type="text" id="job" name="job" value="<s:property value="result.user.job"/>" style="width:160px;"></div>
						</td>
					</tr>
					<!-- tel -->
					<tr>
						<th align="right"><span class="red">*</span><s:text name="user.tel" />：</th>
						<td>
							<span style="margin-right:70px;"><s:text name="user.cityCode" />：</span>
							<span><s:text name="user.number" />：</span><br />
							<input type="text" id="tel1" name="tel1" style="width: 50px;" value="<s:property value='result.user.tel1' />" />
							&nbsp;&nbsp;-&nbsp;&nbsp;
							<input type="text" id="tel2" name="tel2" style="width: 100px;" value="<s:property value='result.user.tel2' />" />
							<div id="telTip"></div>
						</td>
					</tr>
					<!-- fax -->
					<tr>
						<th align="right"><s:text name="user.fax" />：</th>
						<td>
							<input type="text" name="fax1" id="fax1" style="width: 50px;" value="<s:property value='result.user.fax1' />" />
							&nbsp;&nbsp;-&nbsp;&nbsp;
							<input type="text" name="fax2" id="fax2" style="width: 100px;" value="<s:property value='result.user.fax2' />" />
							<div id="faxTip"></div>

						</td>
					</tr>
					<!-- mobile -->
					<tr>
						<th align="right"><s:text name="user.mobile" />：</th>
						<td>
							<div style="float:left "><input type="text" id="mobile" name="mobile" value="<s:property value="result.user.mobile"/>" style="width:160px;"></div>
						</td>
					</tr>
					<!-- email -->
					<tr>
						<th align="right"><span class="red">*</span><s:text name="email" />：</th>
						<td>
							<div class="postSuggestdiv"><input type="text" id="email" name="email" maxlength="80" value="<s:property value="result.user.email"/>" style="width:160px;" /></div>
						</td>
					</tr>
		            <!-- street -->
		            <tr>
		                <th align="right"><span class="red">*</span>地址：</th>
		                <td>
			                <div><input type="text" id="street" name="street" value="<s:property value="result.user.street"/>" style="width:410px" /></div>
			                <div id="streetTip" style="width:500px;"></div>
		                </td>
		            </tr>
		            
					<!-- zip -->
					<tr>
						<th align="right"><s:text name="user.zip"/>：</th>
						<td >
							<div style="float:left ">
								<input type="text" name="zip" id="zip" value="<s:property value="result.user.zip"/>"/>
							</div>							
						</td>
					</tr>
					
				</table>
				<div class="buttonCenter">
					<input type="hidden" id="userId" name="userId" value="<s:property value='result.user.userId'/>" />
					<input type="hidden" name="parentId" value="<s:property value='userId'/>" />
					<input type="hidden" name="comId" value="<s:property value='loginUser.comId'/>" />
					<input type="submit" id="submit" value="<s:text name="button.submit" />" class="button" /> 
					<input type="reset" id="reset" value="<s:text name="button.reset"/>" class="button"/>
				</div>
			</form>
		</div>
	</body>
</html>
