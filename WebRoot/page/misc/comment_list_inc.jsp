<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="commentList">
	<s:if test="result.listResult.list.size > 0">
		<s:include value="/page/inc/pagination.jsp"/>
	</s:if>
	<div class="content">
		<s:iterator value="result.listResult.list" id="comment" status="st">
		<li>
			<div class="span1">
				<s:property value="#comment.title"/>
				<s:if test="result.commentType == 'product'">
					<span>(
					<a href="<%=Config.getString("sys.base") %>/product/detail-<s:property value="#comment.proId"/>.html" target="_blank">
						<s:property value="#comment.proName"/>
					</a>
					<s:if test="#comment.model != null && #comment.model != ''">
						&nbsp;&&nbsp;<s:property value="#comment.model"/>
					</s:if>
					)</span>
				</s:if>
				<a href="javaScript:showReplyFormDialog('<s:property value="#comment.commentId"/>')">
					<img src="/img/reply.jpg" width="53" height="25" border="0" />
				</a>
			</div>
			<div><s:property value="#comment.content"/></div>
			<div class="span2">
				<s:text name="comment.from"/>：
				${comment.contact == null ? comment.authorIP : comment.contact }
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				<s:text name="comment.score"/>：
				<s:iterator value="new int[#comment.score]">
					<img src="/img/ico/xin.gif"/>
				</s:iterator>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<s:text name="comment.createTime"/>：<s:property value="#comment.createTime"/>
			</div>
			<s:if test="#comment.replyList.size >0">
				<s:iterator value="#comment.replyList" id="reply" status="sta">
				<div class="reply">
					<div class="left"><span class="span3"><s:text name="button.reply"/>：</span>&nbsp;&nbsp;<s:property value="#reply.content"/></div>
					<div class="right"><s:property value="#reply.createTime"/></div>
				</div>
				</s:iterator>
			</s:if>
		</li>
		</s:iterator>
	</div>
	<s:if test="result.listResult.list.size > 0">
		<s:include value="/page/inc/pagination.jsp"/>
	</s:if>
</div>