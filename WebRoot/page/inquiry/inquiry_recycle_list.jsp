<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='inquiry.replyListTitle'/></title>
		<script type="text/javascript" src="<%=Config.getString("img.base")%>/js/locale_cn.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				getProvince("province","city","country","code","countryCode");

				//绑定表单验证
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.params["queryBy"] = $("#queryBy").val();
						ListTable.params["queryText"] = $("#queryText").val();
						ListTable.params["userId"] = $("#userId").val();
						ListTable.params["countryCode"] = $("#countryCode").val();
						ListTable.params["pageNo"] = 1;
						ListTable.reload();
					}
				});
			});
			
			function reuseInquiry(){
				if(!Util.isChecked('inqId')){
					alert('<s:text name="inquiry.selectLimit" />');
					return false;
				}else{
					document.inquiryRecycleForm.action = "/inquiry/inquiry_recycle_reuse.htm";
					document.inquiryRecycleForm.submit();
				}
			}
			
			function deleteInquiry(){
				if(!Util.isChecked('inqId')){
					alert('<s:text name="inquiry.selectLimit" />');
					return false;
				}else{
					document.inquiryRecycleForm.action = "/inquiry/inquiry_recycle_delete.htm";
					document.inquiryRecycleForm.submit();
				}
			}
			
			function emptyInquiry(){
				if(!Util.isChecked('inqId')){
					alert('<s:text name="inquiry.selectLimit" />');
					return false;
				}else{
					document.inquiryRecycleForm.action = "/inquiry/inquiry_recycle_empty.htm";
					document.inquiryRecycleForm.submit();
				}
			}
		
		</script>
	</head>
	<body>
		<form class="searchForm" id="searchForm">
			<table>
				<tr>
					<td>
						<s:if test="result.userList != null">
							<select name="userId" id="userId">
								<option value="-1" selected><s:text name='inquiry.byAllUserId'/></option>
								<s:iterator value="result.userList" id="list" status="st">
									<option value="<s:property value='#list.userId' />"><s:if test="#list.admin">管理员</s:if><s:else><s:property value='#list.email' /></s:else></option>
								</s:iterator>
							</select>
						</s:if>
					</td>
					<td>
						<select name="queryBy" id="queryBy">
							<option value="subject">
								<s:text name='inquiry.bySubject' />
							</option>
							<option value="fromName">
								<s:text name='inquiry.byFromName' />
							</option>
						</select>
					</td>
					<td>
						<input name="queryText" id="queryText" style="width:200px;" />
					</td>
					<td>
						<select id="province"></select>
						<input type="hidden" name="countryCode" id="countryCode" value="">	
					</td>		
								
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		
		<form method="post" name="inquiryRecycleForm">
			<div class="buttonLeft">
				<input type="button" onclick="javascript:reuseInquiry();" value="<s:text name='button.reuse'/>"/>
				<input type="button" onclick="javascript:deleteInquiry();" value="<s:text name='button.delete'/>"/>
				<input type="button" onclick="javascript:emptyInquiry();" value="<s:text name='button.empty'/>"/>
			</div>
			<div id="listTable">
				<%@ include file="/page/inquiry/inquiry_recycle_list_inc.jsp" %>
			</div>
		</form>
	</body>
</html>