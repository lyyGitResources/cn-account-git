<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='topOrder.formTitle'/></title>
		<meta http-equiv="X-UA-Compatible" content="IE=7" />
		<script type="text/javascript">
			$(function(){
				$("#topOrderForm").validateForm({
					rules: {
						keyword:{required:true,maxlength:60},
						remark:{maxlength:500}
					},
					messages: {
						keyword: '<s:text name="adOrder.keyword.required"/>',
						remark: '<s:text name="upgrade.remark.required"/>'
					}
				});
			});
		</script>
	</head>
	<body>
		<form id="topOrderForm" name="topOrderForm" method="post" action="/ad/top_order_submit.htm" enctype="multipart/form-data" >
			<input type="hidden" name="comId" value="<s:property value="result.company.comId"/>"/>
			<input type="hidden" name="userId" value="<s:property value="result.user.userId"/>"/>
			<input type="hidden" name="memberId" value="<s:property value="result.company.memberId"/>"/>
			<input type="hidden" name="comName" value="<s:property value="result.company.comName"/>"/>
			<input type="hidden" name="contact" value="<s:property value="result.user.contact"/>"/>
			<input type="hidden" name="email" value="<s:property value="result.user.email"/>"/>
			<input type="hidden" name="tel" value="<s:property value="result.user.tel"/>"/>
			<input type="hidden" name="fax" value="<s:property value="result.user.fax"/>"/>
			<input type="hidden" name="mobile" value="<s:property value="result.user.mobile"/>"/>
			<s:token />
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='topOrder.topType'/>：</th>
					<td>
						<input type="radio" name="topType" value="1" checked/> &nbsp;<s:text name='topOrder.topType1'/><span class="red"><s:text name='topOrder.money1'/></span><br />
						<input type="radio" name="topType" value="2" /> &nbsp;<s:text name='topOrder.topType2'/><span class="red"><s:text name='topOrder.money2'/></span><br />
					</td>
				</tr>
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name='topOrder.keyword'/>：</th>
					<td>
						<input type="text" name="keyword" value="" style="width: 300px;"/><br />
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
