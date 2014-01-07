<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<link href="/css/css.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript">
			$(function () {
				ListTable.url = "/product/no_feature_product_list.do";
				var queryText = $("#queryText").attr("oriValue");
				$("#queryText").blur(function(){
					if($(this).val() == ""){
						$(this).val(queryText);
					}
				}).focus(function(){
					if($(this).val() == queryText){
						$(this).val("");
					}
				});
				
				$("#searchForm").submit(function(){
					if($("#queryText").val() == queryText){
						$("#queryText").val("");
					}
				});
		
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.params["queryText"] = $("#queryText").val();
						ListTable.params["state"] = -2;
						ListTable.params["feature"] = 0;
						ListTable.params["pageNo"] = 1;
						ListTable.reload({}, true);
					}
				});
			});
		</script>
	</head>
	<body>
		<form id="searchForm" class="searchForm" style="width:672px;">
			<table>
				<tbody>
					<tr>
						<td><input value="<s:text name='product.queryText' />" id="queryText" type="text" style="width: 200px;" oriValue="<s:text name='product.queryText' />"></td>
						<td><input class="searchButton" value="<s:text name="button.search" />" type="submit"></td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<div id="listTable">
			<s:include value="/page/product/no_feature_product_list_inc.jsp"/>
		</div>
	</body>
</html>
