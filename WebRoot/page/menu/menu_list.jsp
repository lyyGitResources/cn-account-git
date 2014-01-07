<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="menu.listTitle">
				<s:param>(<s:property value="result.group.groupName" />)</s:param>
			</s:text>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript">
			function deleteMenu(menuId,groupId){
				deleteConfirm("/menu/menu_delete.htm?menuId="+menuId+"&groupId="+groupId);
			}
		</script>
	</head>
	<body>
		<input type="hidden" value="<s:property value='result.group.groupId'/>" id="groupId"/>
		<div class="tabMenu">
			<ul>
				<li <s:if test="state == -1">class="current"</s:if>><span onclick="tabMenu(1,{state:-1,groupId:$('#groupId').val()});"><s:text name="menu.all" />[<s:property value='result.group.menuCount' />]</span></li>
				<li <s:if test="state == 10">class="current"</s:if>><span onclick="tabMenu(2,{state:10,groupId:$('#groupId').val()});"><s:text name="auditState.reject" />[<s:property value='result.group.menuRejectCount' />]</span></li>
			</ul>
		</div>
		<div class="buttonLeft">
			<label>
				<input type="button"  value="<s:text name='menu.addTitle' />"  onclick="document.location.href='/menu/menu_add.htm?groupId=<s:property value='result.group.groupId'/>'" />
			</label>
			<input type="button"  value="<s:text name='button.menuOrder' />" onclick="document.location.href='/menu/menu_order.htm?groupId=<s:property value='result.group.groupId'/>'"/>
		</div>
		<div id="listTable">
			<s:include value="/page/menu/menu_list_inc.jsp"/>
		</div>
	</body>
</html>
