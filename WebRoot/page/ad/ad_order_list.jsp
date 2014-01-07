<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='adOrder.listTitle'/></title>
	</head>
	<body>
		<div id="listTable">
			<s:if test="result.listResult.list.size == 0">
				<div><strong>温馨提示:</strong><label style="color: red">尊敬的用户，您目前的广告申请列表为空。</label><a href="/ad/ad_order.htm">马上订购</a></div>
				
			</s:if>
			<s:else>
					<s:include value="/page/inc/pagination.jsp"/>
				<table cellspacing="1" class="listTable">
					<tr>
						<th width="10%"><s:text name='adOrder.id'/></th>
						<th width="20%"><s:text name='adOrder.adType'/></th>
						<th width="50%"><s:text name='upgrade.remark'/></th>
						<th width="20%"><s:text name='adOrder.createTime'/></th>
					</tr>
					<s:iterator value="result.listResult.list" id="adOrder" status="st">
						<tr style="height:40px;">
							<td><s:property value="#adOrder.id"/></td>
							<td><s:property value="#adOrder.adTypeName"/></td>
							<td title="<s:property value='#adOrder.remark'/>"><s:property value="#adOrder.shortRemark"/></td>
							<td><s:property value="#adOrder.createTime"/></td>
						</tr>						
					</s:iterator>
				</table>
					<s:include value="/page/inc/pagination.jsp"/>
			</s:else>
		</div>
	</body>
</html>