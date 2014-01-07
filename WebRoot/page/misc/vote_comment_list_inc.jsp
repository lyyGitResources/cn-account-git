<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>
<ul>
	<s:iterator value="result.listResult.list" id="voteComment" status="st">
		<li>
			<span class="name"><s:property value='#voteComment.contact'/>说道:</span>
			<span class="time"><s:property value='#voteComment.createTime'/></span><br />
			<span class="text"><s:property value='#voteComment.content'/></span>
		</li>
	</s:iterator>
</ul>
<s:if test="result.listResult.list.size > 0">
	<s:include value="/page/inc/pagination.jsp"/>
</s:if>

