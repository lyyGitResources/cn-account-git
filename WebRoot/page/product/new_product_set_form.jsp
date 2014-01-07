<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="newproduct.newproductSetTitle" /></title>
		<script type="text/javascript" src="/js/datePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			$(function (){
				$("#newProPassExpiry").click(function(){WdatePicker({lang:'zh-cn'});});
				$("#newproductsetform").validateForm({
					rules: {
						newProPass: {passwd:true},
						newProPassExpiry: {required:true},
						newProMenuName: {maxlength:120}
					},
					messages: {
						newProPass: '<s:text name="newproduct.newProPass.required" />',
						newProPassExpiry: '<s:text name="newproduct.newProPassExpiry.required" />',
						newProMenuName: '<s:text name="newproduct.newProMenuName.maxlength" />'
					}
				});
			});
		</script>
	</head>
	<body>
		<h2><s:text name="input.notice" /></h2>
		<div class="pageTips">
			<s:text name="newproduct.newProPass.notice" />
		</div>
		<form id="newproductsetform" name="newproductPasswdform" method="post" action="/newproduct/new_product_set_submit.htm">
			<table class="formTable">
				<tr>
					<th><span class="red">*</span>&nbsp;<s:text name="newproduct.newProPass" />：</th>
					<td>
						<input type="text" name="newProPass" style="width:120px;height:18px; float:left; margin-right:5px;" value="<s:property value='result.company.newProPass' />"/>
					</td>
				</tr>
				<tr>
					<th><s:text name="newproduct.newProMenuName" />：</th>
					<td>
						<input type="text" name=newProMenuName style="width:250px;height:18px; float:left; margin-right:5px;" value="<s:property value='result.company.newProMenuName' />" />
						<div style="clear: both;">
							<s:text name="newproduct.newProMenuName.notice" />
						</div>
					</td>
				</tr>
			</table>
			<div class="buttonCenter">
				<input type="submit" value="<s:text name='button.submit' />"/>
				<input type="reset" value="<s:text name='button.reset' />"/>
			</div>
		</form>
	</body>
</html>