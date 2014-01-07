<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>	
			<s:text name="group.listTitle" />
			<span class="gray">
				<s:text name="group.listTitleNotice" >
				 	<s:param><s:property value="result.menuGroupCount" /></s:param>
				 	<s:param><s:property value="result.menuGroupMax" /></s:param>
				</s:text>
			</span>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript">
			/**
			$(function (){
				$("#formDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 250,
					width: 560,
					modal: true,
					close: function(){
						$("#formDialog").empty();
					}
				});
			});
			
			function showFormDialog (url,title) {
				$("#formDialog").html(AJAX_LOADING_CODE);
				$("#formDialog").load(url,{random:Math.random()});
				$("#formDialog").dialog('option', 'title', title);
				$("#formDialog").dialog('open');
			}*/
		</script>		
	</head>
	<body>
		<div class="pageTips">
			<s:text name="group.tip1" /><br />
			&diams;&nbsp;&nbsp;<s:text name="group.tip2" /><br/>
			&diams;&nbsp;&nbsp;<s:text name="group.tip3" /><br/>
			&diams;&nbsp;&nbsp;<s:text name="group.tip4" /><br/>
			
		</div>
		<div class="buttonLeft">
			<label>
				<input type="button" value="<s:text name='button.addGroup' />" onclick="javascript:showFormDialog('/menu/menu_group_add.do','<s:text name="button.addGroup" />')" />
			</label>
			<input type="button" value="<s:text name='button.menuGroupOrder' />" onclick="location.href='/menu/menu_group_order.htm'"/>
		</div>
		
		<div id="listTable">
			<s:include value="/page/menu/menu_group_list_inc.jsp"/>
		</div>
		<%-- 
		<div id="formDialog"></div>--%>
	</body>
</html>
