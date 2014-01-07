<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
		<s:if test="loginUser.admin">
			<s:text name="alert.listTitle" >
					<s:param><s:property value="result.alertList.size" /></s:param>			
			</s:text>
		</s:if>
		<s:else>
			订阅管理  <span class="gray">您已订阅<s:property value="result.alertList.size" />条商情（最多10条）</span>
		</s:else>
		</title>
		<script type="text/javascript">
			function deleteAlert(id){
				deleteConfirm("/alert/trade_alert_delete.htm?id="+id);
			}
		</script>
	</head>
	<body>
		<s:if test="loginUser.admin == false">
		<div class="pageTips">
				<span>提示：</span>
				<ul>
					<li>主账号和子账号共能订阅10条商情。</li>
				</ul>
		</div>
		</s:if>
		
		<div class="buttonLeft">
			<input name="button2" type="button" value="<s:text name='alert.addTitle' />" onclick="document.location.href='/alert/trade_alert_add.htm'" />
		</div>
			<table cellspacing="1" class="listTable">
				<tr>
					<th width="10%"><s:text name="serialNumber" /></th>
					<th width="35%"><s:text name='alert.mode' /></th>
					<th width="10%"><s:text name='alert.content' /></th>
					<th width="20%"><s:text name='alert.createTime' />/<s:text name='alert.enable' /></th>
					<th width="15%"><s:text name='alert.MemberId' /></th>
					<th width="10%"><s:text name='operate' /></th>
				</tr>
				<s:iterator value="result.alertList" id="alert" status="st">
				<tr>
					<td ><s:property value="#st.count"/></td>
					<td class="tradeAlertContent">
						<s:if test="#alert.keyword != null && #alert.keyword != ''">
							<s:property value="#alert.keyword"/><br><br>
						</s:if>
						<s:else>
							<s:iterator value="#alert.catNamePaths" id="path" status="st">
								<s:property value="path"/><br>
							</s:iterator>
						</s:else>	
					</td>
					<td class="tradeAlertContent">
					<div class="subscribeList">
						<ul>
 							<s:property value="#alert.infoTypeImg" escape="false"/>
						</ul>
					</div>
					</td>
					<td >
						<s:property value="#alert.createTime"/><br />
						<s:property value="#alert.enableName"/><br />
					</td>
					<td >
						<s:if test="loginUser.userId == #alert.userId && loginUser.admin">管理员</s:if>
						<s:else><s:property value="#alert.email" /></s:else>
					</td>					
					<td>
						<s:if test="#alert.enable == true">
							<a href="/alert/trade_alert_enable.htm?id=<s:property value="#alert.id" />&enable=false"><s:text name="button.cancel" /></a>
						</s:if>
						<s:else>
							<a href="/alert/trade_alert_enable.htm?id=<s:property value="#alert.id" />&enable=true"><s:text name="button.reuse" /></a>
						</s:else>
						<br />
						<a href="/alert/trade_alert_edit.htm?id=<s:property value="#alert.id" />"><s:text name="button.edit" /></a><br />
						<a href="javascript:deleteAlert('<s:property value="#alert.id" />')"><s:text name="button.delete" /></a>
					</td>
				</tr>
				</s:iterator>
			</table>
	</body>
</html>
