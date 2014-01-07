<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="pagination">
	<s:text name='pagination.info'>
		<s:param><strong><s:property value="result.listResult.page.resultTotal"/></strong></s:param>
		<s:param><strong><s:property value="result.listResult.page.startIndex+1"/></strong></s:param>
		<s:param><strong><s:property value="result.listResult.page.endIndex"/></strong></s:param>
		<s:param><strong><s:property value="result.listResult.page.pageNo"/></strong></s:param>
		<s:param><strong><s:property value="result.listResult.page.pageTotal"/></strong></s:param>
	</s:text>
	<s:if test="result.listResult.page.previousPage">
		<a href="javascript:ListTable.gotoPage(<s:property value="result.listResult.page.pageNo -1"/>);" class="prePage"><s:text name='pagination.pre'/></a>
		<a href="javascript:ListTable.gotoPage(1);">1</a>
	</s:if>
	<s:if test="result.listResult.page.indexArray[0] > 2">
		...
	</s:if>
	<s:iterator value="result.listResult.page.indexArray" id="pageNo" status="st">
		<s:if test="#pageNo == result.listResult.page.pageNo">
			<a class="curpage"><s:property value='#pageNo'/></a>
		</s:if>
		<s:elseif test="#pageNo != 0 && #pageNo < result.listResult.page.pageTotal && #pageNo >1">
			<a href="javascript:ListTable.gotoPage(<s:property value='#pageNo'/>)"><s:property value='#pageNo'/></a>
		</s:elseif>
	</s:iterator>
	<s:if test="result.listResult.page.indexArray[result.listResult.page.indexArray.length-1] < result.listResult.page.pageTotal-1">
		...
	</s:if>
	<s:if test="result.listResult.page.nextPage">
		<a href="javascript:ListTable.gotoPage(<s:property value='result.listResult.page.pageTotal'/>);"><s:property value='result.listResult.page.pageTotal'/></a>
		<a href="javascript:ListTable.gotoPage(<s:property value="result.listResult.page.pageNo + 1"/>);" class="nextPage"><s:text name='pagination.next'/></a>
	</s:if>
	<span>
		<s:text name='pagination.goto'/>
		<select onchange="javascript:ListTable.gotoPage(this.value);">
			<s:iterator value="result.listResult.page.indexArray" id="pageNo" status="st">
				<s:if test="result.listResult.page.pageNo == #pageNo">
					<option value="<s:property value='#pageNo'/>" selected><s:property value='#pageNo'/></option>
				</s:if>
				<s:else>
					<option value="<s:property value='#pageNo'/>"><s:property value='#pageNo'/></option>
				</s:else>
			</s:iterator>
		</select> 
	</span>

	<script type="text/javascript">
		ListTable.resultTotal = "<s:property value='result.listResult.page.resultTotal'/>";
		ListTable.pageTotal = "<s:property value='result.listResult.page.pageTotal'/>";
	</script>
</div>