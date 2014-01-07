<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="userLog.title" /></title>
		<script type="text/javascript" src="/js/datePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			$(function () {
				$("#startTime").click(function(){
					WdatePicker({lang:'zh-cn',maxDate:'#F{$dp.$D(\'endTime\')}'});
				});
				$("#endTime").click(function(){
					WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'startTime\')}'});
				});
				$("#searchForm").validate({
					submitHandler: function(){
						if($("#userId").size() > 0){
							ListTable.params["userId"] = $("#userId").val();
						}
						ListTable.params["startTime"] = $("#startTime").val();
						ListTable.params["endTime"] = $("#endTime").val();
						ListTable.params["logType"] = $("#logType").val();
						ListTable.params["operate"] = $("#operate").val();
						ListTable.params["pageNo"] = 1;
						ListTable.reload();
					}
				});
			});
		</script>
	</head>
	<body>
		<form class="searchForm" id="searchForm">
			<table>
				<tr>
					<s:if test="result.userList != null && result.userList.size > 0">
						<td>
							<select id="userId" style="width:100px;">
								<option value="-1" <s:if test="userId == -1">selected</s:if>><s:text name="search.userAll" /></option>
								<s:iterator value="result.userList" id="user">
									<option value="<s:property value='#user.userId' />" <s:if test="#user.userId == loginUser.userId">selected</s:if>>
										<s:if test="#user.admin"><s:text name="search.userAdmin"/></s:if><s:else><s:property value='#user.email' /></s:else>
									</option>
								</s:iterator>
							</select>
						</td>
					</s:if>
					<td>
					    <select style="width:100px;" id="logType"> 
					    	<option value=""><s:text name="userLog.operateTable" /></option>
					    	<%--<option value="home">后台首页</option>
					    	--%><option value="member">会员信息</option>
					    	<option value="group">分组</option>
					    	<option value="product">产品</option>
					    	<option value="trade">商情</option>
					    	<option value="menu">自定义菜单</option>
					    	<option value="website">网站设计</option>
					    	<option value="video">视频</option>
					    	<%--<option value="inquiry">询盘</option>
					    	<option value="alert">订阅</option>
					    --%></select>
					</td>
					<td>
	 					<select style="width:100px;" id="operate">
	 						<option value="-1"><s:text name="userLog.operateOption" /></option>
	 						<option value="1"><s:text name='button.add'/></option>
	 						<option value="2"><s:text name='button.edit'/></option>
	 						<option value="3"><s:text name='userLog.recovery'/></option>
	 						<option value="4"><s:text name='userLog.revert'/></option>
	 						<option value="5"><s:text name='button.delete'/></option>
	 					</select>
					</td>
					<td>
					    <s:text name="startDate"/>
					    <input id="startTime"  readonly="true" style="width:80px"/>
					   	<s:text name="endDate"/>
					   	<input id="endTime"  readonly="true" style="width:80px"/>
					</td>
					<td><input type="submit" class="searchButton" value="<s:text name="button.search"/>" /></td>
				</tr>
			</table>
		</form>	
		<div id="listTable">
			<%@ include file="/page/basic/user_log_list_inc.jsp" %>
		</div> 			
	</body>
	</html>