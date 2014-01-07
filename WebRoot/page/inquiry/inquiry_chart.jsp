<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/page/inquiry/functions.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.charTitle'/></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript" src="/js/datePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/js/fusionCharts.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				getProvince("province","city","country","code","countryCode");
				
				//绑定表单验证
				$("#inquiry_seach").validate({
					submitHandler: function(form){
						$(form).ajaxSubmit($.extend(ajaxSubmitOptions,{
							beforeSubmit: function(){
								$('#dialog').dialog('open');
							},
						    success: function (response, status){
						    	$('#dialog').dialog('close');
						    	$("#chartCode").html(response.result.chartCode);
						    }
						}));
					}
				});
				
			});

			
		</script>
	</head>
	<body>
		<form class="searchForm" id="inquiry_seach" action="/inquiry/inquiry_chart_stat.htm">
			<table>
				<tr>					
					<td>
						<s:text name='inquiry.byFromName' />
					</td>
					<td>
						<input name="queryText" id="queryText" style="width:200px;" />
					</td>
					<td>
						<s:if test="result.userList != null && result.userList.size > 0">
							<select name="userId" id="userId">
								<option value="-1" selected><s:text name='inquiry.byAllUserId'/></option>
								<s:iterator value="result.userList" id="user" status="st">
									<option value="<s:property value='#user.userId' />"> 
										<s:if test="#user.admin"><s:text name="search.userAdmin"/></s:if><s:else><s:property value='#user.email' /></s:else>
									</option>
								</s:iterator>
							</select>
						</s:if>
					</td>
					<td>
						<select id="province"></select>
						<input type="hidden" name="countryCode" id="countryCode" value="">	
					</td>		
								
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		<div id="chartCode"><s:property value='result.chartCode' escape="false"/></div>
	</body>
</html>
