<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="product.recycleListTitle" />
				<s:if test="loginUser.admin == true">
					（<s:property value="result.productDelCount"/>）
				</s:if>
			<span class="gray">
				<s:text name="product.recycleNotice">
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
						alert('<s:text name="product.selectOne"/>');
					}else{
						if(confirm('<s:text name='product.isDelete' />')){
							if(ListTable.params["queryText"]){
								$("#queryTextTmp").val(ListTable.params["queryText"]);
							}
							$("#form").attr("action", "/product/product_recycle_delete.do");
							$("#form").submit();
						}
					}
				});
				
				$("#emptyRecycle").click(function(){
					if(confirm('<s:text name="trade.emptyRecycle"/>')){
						$("#form").attr("action", "/product/product_recycle_empty.do");
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
						<td><input value="<s:if test="queryText == null || queryText == ''">请输入产品名&型号</s:if><s:else><s:property value='queryText' /></s:else>" id="queryText" type="text" style="width: 200px;" oriValue="请输入产品名&型号"></td>
						<td><input class="searchButton" value="<s:text name="button.search" />" type="submit"></td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<form id="form" method="post">
			<input type="hidden" name="queryText" id="queryTextTmp" />
		
			<div class="buttonLeft">
				<input type="button" id="deleteRecycle" value='<s:text name="button.deleteProduct"/>' />
				<s:if test="result.listResult.page.resultTotal > 0">
					<input type="button" id="emptyRecycle" value="<s:text name="button.emptyRecycle"/>" />
				</s:if>
			</div>
				
			<div id="listTable">
				<s:include value="/page/product/product_list_inc.jsp"/>
			</div>
		</form>
	</body>
</html>
