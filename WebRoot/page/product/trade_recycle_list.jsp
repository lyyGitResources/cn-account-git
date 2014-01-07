<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
		<s:if test="loginUser.admin == true">
			<s:text name="trade.recycleListTitle">
					<s:param><s:property value="result.tradeDelCount"/></s:param>
			</s:text>
		</s:if>	
		<s:else>
			商情回收站
		</s:else>
			<span class="gray">
				<s:text name="trade.recycle.max">
					<s:param><s:property value="result.recycleMax"/></s:param>
				</s:text>
			</span>
		</title>
		<script type="text/javascript">
			$(document).ready(function () {
				// 搜索
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryText: $("#queryText").val(),
							state: 5,
							pageNo:1
						}, true);
					}
				});

				$("#deleteRecycle").click(function(){
					if($("[name='proId']:checked").size() <= 0){
						alert('<s:text name="trade.selectTrade"/>');
					}else{
						if(confirm('<s:text name="trade.deleteRecycle"/>')){
							$("#form").attr("action", "/trade/trade_recycle_delete.do");
							$("#form").submit();
						}
					}
				});
				
				$("#emptyRecycle").click(function(){
					if(confirm('<s:text name="trade.emptyRecycle"/>')){
						$("#form").attr("action", "/trade/trade_recycle_empty.do");
						$("#form").submit();
					}
				});

			});

		</script>
	</head>
	<body>
		<form id="searchForm" class="searchForm">
			<table>
				<tbody>
					<tr>
						<td><input value="<s:text name='trade.queryText' />" id="queryText" type="text" style="width: 200px;" oriValue="<s:text name='trade.queryText' />"></td>
						<td><input class="searchButton" value="<s:text name="button.search" />" type="submit"></td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<form id="form" method="post" action="">
			
			<div class="buttonLeft">
				<input type="button" id="deleteRecycle" value='<s:text name="button.deleteTrade"/>' />
				<s:if test="result.listResult.page.resultTotal > 0">
					<input type="button" id="emptyRecycle" value="<s:text name="button.emptyRecycle"/>" />
				</s:if>
			</div>
				
			<div id="listTable">
				<s:include value="/page/product/trade_list_inc.jsp"/>
			</div>
		</form>
	</body>
</html>
