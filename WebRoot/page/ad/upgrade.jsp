<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='upgrade.formTitle'/></title>
		<script type="text/javascript">
			$(function(){
 				$("#upgradeForm").validateForm({
					rules: {
						remark:{maxlength:500}
					},
					messages: {
						remark: '<s:text name="upgrade.remark.required"/>'
					}
				});
 			});
		</script>
	</head>
	<body>
		<form id="upgradeForm" name="upgradeForm" method="post" action="/ad/upgrade_submit.htm" enctype="multipart/form-data" >
			<input type="hidden" name="comId" value="<s:property value="result.company.comId"/>"/>
			<input type="hidden" name="userId" value="<s:property value="result.user.userId"/>"/>
			<input type="hidden" name="memberId" value="<s:property value="result.company.memberId"/>"/>
			<input type="hidden" name="comName" value="<s:property value="result.company.comName"/>"/>
			<input type="hidden" name="contact" value="<s:property value="result.user.contact"/>"/>
			<input type="hidden" name="email" value="<s:property value="result.user.email"/>"/>
			<input type="hidden" name="tel" value="<s:property value="result.user.tel"/>"/>
			<input type="hidden" name="fax" value="<s:property value="result.user.fax"/>"/>
			<input type="hidden" name="mobile" value="<s:property value="result.user.mobile"/>"/>
			<input type="hidden" name="upType" value="1" />
			<s:token />
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='upgrade.upType'/>：</th>
					<td>
						<s:text name='upgrade.upType1'/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.remark'/>：</th>
					<td>
						<textarea id="remark" name="remark" style="width: 450px; height: 70px;"></textarea>
						<div id="remarkTip"></div>
					</td>
				</tr>
				<tr>
					<th><s:text name='memberId'/>：</th>
					<td>
						<s:property value="result.company.memberId"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='comName'/>：</th>
					<td>
						<s:property value="result.company.comName"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.contact'/>：</th>
					<td>
						<s:property value="result.user.contact"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='email'/>：</th>
					<td>
						<s:property value="result.user.email"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.tel'/>：</th>
					<td>
						<s:property value="result.user.tel"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.fax'/>：</th>
					<td>
						<s:property value="result.user.fax"/>
					</td>
				</tr>
				<tr>
					<th><s:text name='upgrade.mobile'/>：</th>
					<td>
						<s:property value="result.user.mobile"/>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit"  value="<s:text name='button.submit' />"/> &nbsp;&nbsp;
				<input type="reset"  value="<s:text name='button.reset' />"/> 
			</div>
		</form>
	</body>
</html>
