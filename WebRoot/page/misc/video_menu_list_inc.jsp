<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript">
	$("img.imgLoadError").error(function(){
		$(this).attr("src","http://img.hisupplier.com/img/no_photo.gif");
	});
</script>
<s:include value="/page/inc/pagination.jsp"/>
<%@ include file="/page/inc/image_error.jsp" %>
<table cellspacing="1" class="listTable">
	<tr>
		<th><s:text name="serialNumber"/></th>
		<th><s:i18n name="com.hisupplier.cn.account.menu.package"><s:text name="menu.imgPath"/></s:i18n></th>
		<th><s:i18n name="com.hisupplier.cn.account.menu.package"><s:text name="menu.title"/></s:i18n></th>
		<th><s:text name="auditState"/></th>
		<th><s:i18n name="com.hisupplier.cn.account.menu.package"><s:text name="menu.viewCount"/></s:i18n></th>
		<th><s:text name="modifyTime"/></th>
		<th><s:text name="operate"/></th>
	</tr>
	<s:iterator value="result.listResult.list" id="menu" status="st">
	<tr>
		<td><s:property value="#st.count"/></td>
		<td class="img75">
			<a href="<s:property value='#menu.imgPathS'/>" target="_blank">
				<img src="<s:property value='#menu.imgPath75'/>" width="75" height="75" class="imgLoadError"/>	
			</a>				
		</td>
		<td ><s:property value="#menu.title"/></td>
		<td >
			<s:property value="#menu.stateName"/>
		</td>
		<td ><s:property value="#menu.viewCount"/></td>
		<td ><s:property value="#menu.modifyTime"/></td>
		<td >
			<a href="/menu/menu_edit.htm?menuId=<s:property value="#menu.menuId"/>"><s:text name="button.edit"/></a>
		</td>
	</tr>
	</s:iterator>
</table>
<s:include value="/page/inc/pagination.jsp"/>