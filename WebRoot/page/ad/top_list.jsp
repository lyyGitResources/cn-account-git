<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='top.listTitle'/></title>
	</head>
	<body>
		<div id="listTable">
			<s:if test="result.listResult.list.size == 0">
				<div><strong>温馨提示:</strong><label style="color: red">尊敬的用户，您目前的TopSite服务列表为空。</label><a href="/ad/top_order.htm">马上订购</a></div>
				
			</s:if>
			<s:else>
					<s:include value="/page/inc/pagination.jsp"/>
				<table cellspacing="1" class="listTable">
					<tr>
						<th width="20%"><s:text name='topOrder.keyword'/></th>
						<th width="20%"><s:text name='topOrder.proImg'/></th>
						<th width="20%"><s:text name='topOrder.proName'/></th>
						<th width="10%"><s:text name='startDate'/></th>
						<th width="10%"><s:text name='endDate'/></th>
						<th width="10%"><s:text name='topOrder.topTypeName'/></th>
						<th width="10%"><s:text name='operate'/></th>
					</tr>
					<s:iterator value="result.listResult.list" id="top" status="st">
						<tr style="height:40px;">
							<td><s:property value="#top.keyword"/></td>
							<td><img id="logoImg" src="<s:property value="#top.imgPath75"/>" class="imgLoadError" width="60px" height="60px" /></td>
							<td><s:property value="#top.shortProName"/></td>
							<td><s:property value="#top.beginDate"/></td>
							<td><s:property value="#top.endDate"/></td>
							<td><s:property value="#top.topTypeName"/></td>
							<td>
								<a href="/ad/top_edit.htm?topId=<s:property value="#top.topId"/>">
									<s:text name='button.edit'/>
								</a>
							</td>
						</tr>						
					</s:iterator>
				</table>
					<s:include value="/page/inc/pagination.jsp"/>
			</s:else>
		</div>
		<%@ include file="/page/inc/image_error.jsp" %>
	</body>
</html>