<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ page import="com.hisupplier.commons.Config" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name='topOrder.listTitle'/></title>
	</head>
	<body>
		<div id="listTable">
			<s:if test="result.listResult.list.size == 0">
				<div><strong>温馨提示:</strong><label style="color: red">尊敬的用户，您目前的TopSite订购列表为空。</label><a href="/ad/top_order.htm">马上订购</a></div>
				
			</s:if>
			<s:else>
					<s:include value="/page/inc/pagination.jsp"/>
				<table cellspacing="1" class="listTable">
					<tr>
						<th width="10%"><s:text name='adOrder.id'/></th>
						<th width="10%"><s:text name='topOrder.topType'/></th>
						<th width="22%"><s:text name='topOrder.keyword'/></th>
						<th width="40%"><s:text name='upgrade.remark'/></th>
						<th width="18%"><s:text name='adOrder.createTime'/></th>
					</tr>
					<s:iterator value="result.listResult.list" id="topOrder" status="st">
						<tr style="height:40px;">
							<td><s:property value="#topOrder.id"/></td>
							<td><s:property value="#topOrder.topTypeName"/></td>
							<td title="<s:property value='#topOrder.keyword'/>"><s:property value="#topOrder.shortKeyword"/></td>
							<td title="<s:property value='#topOrder.remark'/>"><s:property value="#topOrder.shortRemark"/></td>
							<td><s:property value="#topOrder.createTime"/></td>
						</tr>
					</s:iterator>
				</table>
					<s:include value="/page/inc/pagination.jsp"/>
			</s:else>
		</div>
	</body>
</html>