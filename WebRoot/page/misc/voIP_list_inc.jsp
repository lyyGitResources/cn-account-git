<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<table class="listTable" cellspacing="1">
	<tbody>
		<tr>
			<th width="15%">主叫号码</th>
			<th width="15%">被叫号码</th>
			<th width="15%">通话开始时间</th>
			<th width="15%">通话结束时间</th>
			<th width="10%">接通状态</th>
			<th width="15%">通话时间（秒）</th>
			<th width="15%">通话费用（元）</th>
		</tr>
		<s:iterator value="result.listResult.list" id="voIP">
			<tr>
				<td><s:property value='#voIP.teled' /></td>
				<td><s:property value='#voIP.teling' /></td>
				<td><s:property value='#voIP.beginTime' /></td>
				<td><s:property value='#voIP.endTime' /></td>
				<td><s:property value='#voIP.state' /></td>
				<td><s:property value='#voIP.seconds' /></td>
				<td><s:property value='#voIP.aiqb' /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<p>&nbsp;</p>
<%@ include file="/page/inc/pagination.jsp"%>