<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="voteBox">
	<div class="voteList">
		<s:if test="result.listResult.list.size > 0">
			<s:include value="/page/inc/pagination.jsp"/>
		</s:if>
		<ul>	
			<s:iterator value="result.listResult.list" id="vote" status="st">	
				<li>
					<a href="/vote/vote_view.htm?voteId=<s:property value="#vote.voteId"/>"><s:property value="#vote.title"/></a><br />
					<s:if test="#vote.voteType == 1">
						<s:if test="#vote.optionList.size > 2">
							<s:subset source="#vote.optionList" start="0" count="2" >
								<s:iterator>
									<input type="radio" disabled/><s:property value="content"/><br />
								</s:iterator>
							</s:subset>
						</s:if>
						<s:else>
							<s:iterator value="#vote.optionList" id="voteOption">
								<input type="radio" disabled/><s:property value="#voteOption.content"/><br />
							</s:iterator>
						</s:else>	
					</s:if>	
					<s:elseif test="#vote.voteType == 2">
						<s:if test="#vote.optionList.size > 2">
							<s:subset source="#vote.optionList" start="0" count="2" >
								<s:iterator>
									<input type="checkbox" disabled/><s:property value="content"/><br />
								</s:iterator>
							</s:subset>
						</s:if>
						<s:else>
							<s:iterator value="#vote.optionList" id="voteOption">
								<input type="checkbox" disabled/><s:property value="#voteOption.content"/><br />
							</s:iterator>
						</s:else>
					</s:elseif>
					&nbsp;	...
					<span class="span"><s:text name="vote.count"/>：<s:property value="#vote.voteCount"/> &nbsp;&nbsp;&nbsp;&nbsp;<s:text name="vote.createTime"/>：<s:property value="#vote.createTime"/>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/vote/vote_view.htm?voteId=<s:property value="#vote.voteId"/>"><s:text name="vote.view"/>&gt;&gt;</a></span>
				</li>
			</s:iterator>	 
		</ul>
		<s:if test="result.listResult.list.size > 0">
			<s:include value="/page/inc/pagination.jsp"/>
		</s:if>
	</div>
</div>