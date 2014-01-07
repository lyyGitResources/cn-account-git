<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="user.listTitle" />
			<span class="gray">
				已创建<s:property value="result.userList.size"/>个子帐号（最多<s:property value="result.company.userMax" />个）
			</span>
		</title>
		<script type="text/javascript">
			function deleteGroup(userId){
				deleteConfirm("/member/user_delete.htm?userId="+userId);
			}
		</script>
	</head>
	<body>
		<div class="buttonLeft">
			<input type="button" onclick="window.location.href = '/member/user_add.htm'" value='添加子帐号'/>
		</div>
		<table cellspacing="1" class="listTable">
			<tr>
				<th width="5%"><s:text name='serialNumber'/></th>
				<th width="25%"><s:text name='user.contact'/></th>
				<th width="25%"><s:text name='email'/></th>
				<th width="15%"><s:text name='user.job'/></th>
				<th width="15%"><s:text name='user.show'/></th>
				<th width="20%"><s:text name='operate'/></th>
			</tr>
			<s:iterator value="result.userList" id="user" status="st">
			<tr style="height:40px">
				<td><s:property value="#st.index + 1"/></td>
				<td><s:property value="#user.contact"/> <s:property value="#user.titleShow" /></td>
				<td><s:property value="#user.email" /></td>
				<td><s:property value="#user.job" /></td>
				<td><s:property value="#user.showString" /></td>
				<td>
					<a href="/member/user_detail.htm?userId=<s:property value='#user.userId' />">查看</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="/member/user_edit.htm?userId=<s:property value='#user.userId' />" ><s:text name='button.edit'/></a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:deleteGroup(<s:property value='#user.userId' />);"><s:text name='button.delete'/></a>
				</td>
			</tr>
			</s:iterator>
		</table>
	</body>
</html>