<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="specialGroup.listTitle"/>
			<span class="gray">
				<s:text name="specialGroup.exist">
					<s:param><s:property value="result.groupList.size"/></s:param>
				</s:text>
			</span>
		</title>
		<script type="text/javascript">
			$(document).ready(function (){
				$("#formDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 200,
					width: 600,
					modal: true,
					close: function(){
						$("#formDialog").empty();
					}
				});
			});
			
			function showFormDialog_special_group (url,title) {
				$("#formDialog").load(url,{random: Math.random()});
				$("#formDialog").dialog('option', 'title', title);
				$("#formDialog").dialog('open');
			}
			
			function deleteGroup(groupId){
				deleteConfirm("/specialGroup/group_delete.htm?groupId="+groupId);
			}
		</script>
	</head>
	<body>
	
		<div class="pageTips">
			<span><s:text name="specialGroup.notice0"/>:</span>
			<ul>
				<li><s:text name="specialGroup.notice1"/></li>
				<li><s:text name="specialGroup.notice2"/></li>
				<li><s:text name="specialGroup.notice3" /></li>
			</ul>
		</div>
		
		<div class="buttonLeft">
			<input type="button" onclick="javascript:showFormDialog_special_group('/specialGroup/group_add.do','<s:text name="specialGroup.createGroupTitle"/>')" value="<s:text name='button.addSpecialGroup'/>" />
			<input type="button" onclick="window.location.href='/specialGroup/no_group_product_list.htm'" value='<s:text name="specialGroup.noGroupProduct"><s:param><s:property value="result.noGroupProductCount"/></s:param></s:text>'/>
		</div>
		
		<div id="listTable">
			<s:include value="/page/group/special_group_list_inc.jsp"/>
		</div>
		<div id="formDialog"></div>
	</body>
</html>