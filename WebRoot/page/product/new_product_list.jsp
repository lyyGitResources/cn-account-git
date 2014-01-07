<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="newproduct.listTitle" /></title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript">
			$(function (){
				// 搜索
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryText: $("#queryText").val(),
							state: $("#state").val(),
							pageNo:1
						}, true);
					}
				});
				$("#deleteNewProduct").click(function() {
					if($("[name='proId']:checked").size() <= 0){
						alert("<s:text name='product.selectOne' />");
					}else{
						if(confirm(("<s:text name='deleteConfirm' />"))){
							if(ListTable.params["state"]){
								$("#stateTmp").val(ListTable.params["state"]);
							}
							if(ListTable.params["queryText"]){
								$("#queryTextTmp").val(ListTable.params["queryText"]);
							}
							$("#newproductform").attr("action", "/newproduct/new_product_delete.do");
							$("#newproductform").submit();
						}
					}
				});
			});
		</script>
	</head>
	<body>
		<form id="searchForm" class="searchForm">
			<table>
				<tr>
					<td><input value="<s:if test="queryText == null || queryText == ''"><s:text name='newproduct.searchText' /></s:if><s:else><s:property value='queryText' /></s:else>" id="queryText" type="text" style="width: 200px;" oriValue="<s:text name='newproduct.searchText' />"></td>
					<td id="stateText">
						<select id="state">
							<option value="-1" <s:if test="state == -1">selected</s:if>><s:text name="product.searchState" /></option>
							<option value="10" <s:if test="state == 10">selected</s:if>><s:text name="auditState.reject" /></option>
							<s:if test="loginUser.memberType == 2">
								<option value="14" <s:if test="state == 14">selected</s:if>><s:text name="auditState.auditing" /></option>
							</s:if>
							<option value="15" <s:if test="state == 15">selected</s:if>><s:text name="auditState.wait" /></option>
							<option value="20" <s:if test="state == 20">selected</s:if>><s:text name="auditState.pass" /></option>
						</select>
					</td>
					<td><input class="searchButton" value="<s:text name="button.search" />" type="submit"></td>
				</tr>
			</table>
		</form>
		
		<form id="newproductform">
			<input type="hidden" name="state" id="stateTmp" />
			<input type="hidden" name="queryText" id="queryTextTmp" />

			<div class="buttonLeft">
				<input type="button" id="addNewProduct" onclick="document.location.href='/newproduct/new_product_add.htm'" value="<s:text name='button.addNewProduct' />" />
				<input type="button" id="deleteNewProduct" value="<s:text name='button.deleteNewProduct' />" />
			</div>

			<div id="listTable">
				<s:include value="/page/product/new_product_list_inc.jsp"/>
			</div>
		</form>
		
	</body>
</html>
