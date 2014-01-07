<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.listTitle"></s:text></s:i18n>
			<s:if test="result.group.groupId > 0">
				（<s:property value="result.group.groupName"/>）
			</s:if>
			<s:else>
				（<s:text name="specialGroup.noGroupProductTitle"/>）
			</s:else>
		</title>
		<script type="text/javascript">
			$(document).ready(function () {
				//绑定表单验证
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryText: $("#queryText").val(),
							pageNo:1
						}, true);
					}
				});
				
				$("#specialGroupList").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 220,
					width: 520,
					modal: true,
					close: function(){
						$("#specialGroupList").empty();
					}
				});
			});
			
			function showSpecialGroupList () {
				<s:if test="result.group.groupId > 0">
					$("#specialGroupList").load('/specialGroup/select_group_list.do?groupId=<s:property value="result.group.groupId"/>');
				</s:if>
				<s:else>
					$("#specialGroupList").load("/specialGroup/select_group_list.do?groupId=0");
				</s:else>
				$("#specialGroupList").dialog('open');
			}
			
			
			function selectSpecialGroup(groupId,groupName) {
				if (!Util.isChecked("proId")) {
					window.alert('<s:text name="specialGroup.selectProduct"/>');
				} else if($("#oldGroupId").val() == groupId){
					window.alert('<s:text name="specialGroup.selectRepeatProduct"/>');
				}else{
					<s:if test="result.group.groupId > 0">
						document.productForm.action = "/specialGroup/select_other_product_group.htm";
					</s:if>
					<s:else>
						document.productForm.action = "/specialGroup/select_product_group.htm";
					</s:else>
					if(ListTable.params["pageNo"]){
						document.productForm.pageNo.value = ListTable.params["pageNo"];
					}
					document.productForm.groupId.value = groupId;
					document.productForm.groupName.value = groupName;
					document.productForm.submit();
				}
			}
			
			function removeFromProductGroup(groupId,groupName) {
				if (!Util.isChecked("proId")) {
					window.alert('<s:text name="specialGroup.selectProduct"/>');
				} else {
					document.productForm.action = "/specialGroup/remove_from_product_group.htm";
					document.productForm.groupId.value = groupId;
					document.productForm.groupName.value = groupName;
					document.productForm.submit();
				}
			}
			// 存在对应商情时
			function isModifyTrade(proId){
				if(confirm("<s:i18n name='com.hisupplier.cn.account.product.package'><s:text name="product.tradeExist"/></s:i18n>")){
					location.href = "/trade/trade_edit.htm?proId=" + proId;
				}
			}
		</script>
	</head>

	<body>
	
	
		<form class="searchForm" id="searchForm">
			<table>
				<tr>
					<td>
						<input name="queryText" id="queryText" value="<s:i18n name="com.hisupplier.cn.account.product.package"><s:text name='product.queryText'/></s:i18n>" style="width:200px;" oriValue="<s:i18n name="com.hisupplier.cn.account.product.package"><s:text name='product.queryText'/></s:i18n>"/>
					</td>
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		
		<div class="buttonLeft">
			<s:if test="result.group.groupId > 0">
				<input type="button" onclick="javascript:removeFromProductGroup('<s:property value="result.group.groupId"/>','<s:property value="result.group.groupName"/>');" value='移除产品' />
				<input type="button" onclick="javascript:showSpecialGroupList();" value='<s:text name="button.selectOtherGroup"/>' />
			</s:if>
			<s:else>
				<input type="button" onclick="javascript:showSpecialGroupList();" value='<s:text name="button.selectGroup"/>' />
			</s:else>
		</div>

		<form action="" name="productForm" method="post">
			<div id="listTable">
				<s:include value="/page/product/product_list_inc.jsp"/>
			</div>
			<input type="hidden" name="pageNo"/>
			<input type="hidden" name="groupId"/>
			<input type="hidden" name="groupName"/>
			<input type="hidden" id="oldGroupId" value="<s:property value="result.group.groupId"/>"/>
		</form>
		
		<div id="specialGroupList" title='<s:text name="specialGroup.selectListTitle"/>'></div>
	</body>
</html>
