<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ page import="com.hisupplier.cn.account.entity.Contact"%>
<%@ page import="com.hisupplier.cn.account.entity.ContactGroup"%>
<%@ page import="com.hisupplier.commons.util.Coder"%>
<%@ page import="com.hisupplier.commons.page.Page"%>
<%@ page import="com.opensymphony.xwork2.util.*"%>
<%@page import="java.util.Map"%>
<%@page import="com.hisupplier.commons.page.ListResult"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	ValueStack vs = (ValueStack) request.getAttribute("struts.valueStack");
	Map<String,Object> result = (Map<String,Object>)vs.findValue("result");
	ListResult listResult=(ListResult)result.get("listResult");
	Page p = listResult.getPage();
 %>
<table width="100%" border="0" cellpadding="2" cellspacing="1" class="tableRank">
		<tr>
			<th width="10%">
				短信编号
			</th>
			<th width="12%">
				接收号码
			</th>
			<th>
				短信内容
			</th>
			<th width="10%">
				状态
			</th>
			<th width="20%">
				发送时间
			</th>
			<th width="10%">
				选项
			</th>
		</tr>
		<% int i=1; %>
		<s:iterator value="result.listResult.list" id="msg">
			<tr>
				<td class="td1">
					<%=i+p.getStartIndex() %>
				</td>
				<td>
					<s:property value="#msg.mobile" />
				</td>
				<td class="tdText">
					<s:property value="#msg.content" />
				</td>
				<td>
					<s:if test="#msg.result=='a' || #msg.result=='b'">失败</s:if>
					<s:elseif test="#msg.result=='0'">等待发送</s:elseif>
					<s:else>成功</s:else>
				</td>
				<td>
					<s:property value="#msg.submit_time" />
				</td>
				<td>
					<a href="javascript:setDeleteUrl(<s:property value="#msg.id" />);">删除</a>
					<s:if test="#msg.result=='a' || #msg.result=='b'">
					     &nbsp;&nbsp;<a href="javascript:setRepeatUrl(<s:property value="#msg.id" />);">重发</a>
					</s:if>
				</td>
			</tr>
			<%i++; %>
		</s:iterator>
	</table>
<%@include file="/page/inc/pagination.jsp" %>
