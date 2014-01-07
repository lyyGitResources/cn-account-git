<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$(document).ready(function () {
		$("#adminLogDialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 200,
			width: 360,
			modal: true,
			close: function(){
				$("#adminLogDialog").empty();
			}
		});
	
		$("[name='adminLog']").each(function(){
			$(this).click(function(){
				$("#adminLogDialog").load("/basic/admin_log.do?tableName=Menu&tableId=" + $(this).attr("menuId"),{random: Math.random()});
				$("#adminLogDialog").dialog('option', 'title', '<s:text name="auditState.rejectRemark"/>');
				$("#adminLogDialog").dialog('open');
			});
		});
	});
</script>
<s:include value="/page/inc/image_error.jsp"></s:include>
<s:include value="/page/inc/pagination.jsp"/>
<table cellspacing="1" class="listTable">
	<tr>
		<th width="5%"><s:text name="serialNumber" /></th>
		<th width="10%"><s:text name='menu.imgPath' /></th>
		<th width="45%"><s:text name='menu.title' /></th>
		<th width="10%"><s:text name='auditState' /></th>
		<th width="10%"  onclick="ListTable.sort('viewCount')" class="sort" id="sort_viewCount"><s:text name='menu.viewCount' /></th>
		<th width="10%"  onclick="ListTable.sort('modifyTime')" class="sort" id="sort_modifyTime"><s:text name='modifyTime' /></th>
		<th width="10%"><s:text name='operate' /></th>
	</tr>
	<s:iterator value="result.listResult.list" id="menu" status="st">
	<tr>
		<td><s:property value="#st.count + result.listResult.page.startIndex" /></td>
		<td class="img75">
			<a href="/menu/menu_edit.htm?menuId=<s:property value='#menu.menuId' />">
				<img src="<s:property value='#menu.imgPath75' />" class="imgLoadError" onload="Util.setImgWH(this, 75, 75)"/>
			</a>
		</td>
		<td title="<s:property value='#menu.title' />"><a href="/menu/menu_edit.htm?menuId=<s:property value='#menu.menuId' />"><s:property value="#menu.shortTitle" /></a></td>
		<td>
			<s:if test="#menu.state == 10">
				<a href="#position" menuId="<s:property value='#menu.menuId' />" name="adminLog">
					<s:property value='#menu.stateName' />
				</a>
			</s:if>
			<s:else>
				<s:property value='#menu.stateName' />
			</s:else>		
		</td>	
		<td><s:property value="#menu.viewCount" /></td>
		<td><s:property value="#menu.modifyTime" /></td>
		<td>
		<a href="javascript:deleteMenu(<s:property value='#menu.menuId' />,<s:property value='#menu.groupId' />)"><s:text name="button.delete" /></a>
		</td>
	</tr>
	</s:iterator>
</table>
<s:include value="/page/inc/pagination.jsp"/>

<div id="adminLogDialog"></div>
