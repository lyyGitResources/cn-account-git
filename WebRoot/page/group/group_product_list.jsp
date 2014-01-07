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
				（<s:text name="group.noGroupProductTitle"/>）
			</s:else>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript">
			$(function () {
				//绑定表单验证
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryText: $("#queryText").val(),
							userId:$("#userId").val(),
							state: $("#state").val(),
							pageNo:1
						}, true);
					}
				});
				
				$("#normalGroupList").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 500,
					width: 520,
					modal: true
				});
			});
			
			function showGroupList () {
				$("#normalGroupList").dialog('open');
			}
			
			function selectGroup(groupId,groupName,groupNamePath) {
				if (!Util.isChecked("proId")) {
					window.alert('<s:text name="group.selectProduct"/>');
					
				} else if(groupId == <s:property value="result.group.groupId"/>){
					alert("产品已属于该分组，请重新选择分组");
					
				}else if(groupId == 0){
					document.productForm.action = "/group/remove_from_product_group.htm";
					document.productForm.groupId.value = <s:property value="result.group.groupId"/>;
					document.productForm.groupName.value = '<s:property value="result.group.groupName"/>';
					document.productForm.submit();
				}else{
					<s:if test="result.group.groupId > 0">
						document.productForm.action = '/group/change_product_group.htm';
						document.productForm.groupId.value = <s:property value="result.group.groupId"/>;
						document.productForm.newGroupId.value = groupId;
					</s:if>
					<s:else>
						document.productForm.action = '/group/select_product_group.htm';
						document.productForm.groupId.value = groupId;
					</s:else>
					if(ListTable.params["pageNo"]){
						document.productForm.pageNo.value = ListTable.params["pageNo"];
					}
					document.productForm.groupName.value = groupName;
					document.productForm.submit();
				}
			}
			
			function deleteProduct(){
				if(!Util.isChecked("proId")){
					alert('<s:text name="group.selectProduct"/>');
				}else{
					if(confirm("<s:text name='deleteConfirm' />")){
						if(ListTable.params["queryText"]){
							document.productForm.queryText.value=ListTable.params["queryText"];
						}
						if(ListTable.params["state"]){
							document.productForm.state.value=ListTable.params["state"];
						}
						if(ListTable.params["userId"]){
							document.productForm.userIdTmp.value=ListTable.params["userId"];
						}
						
						document.productForm.groupId.value = <s:property value="result.group.groupId"/>;
						document.productForm.action = '/group/product_delete.do';
						document.productForm.submit();
					}
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
					<s:if test="result.userList != null && result.userList.size > 0">
						<td>
							<select id="userId">
								<option value="0" <s:if test="userId == 0">selected</s:if>><s:text name="search.userAll" /></option>
								<s:iterator value="result.userList" id="user">
									<option value="<s:property value='#user.userId' />" <s:if test="#user.userId == loginUser.userId">selected</s:if>>
										<s:if test="#user.admin"><s:text name="search.userAdmin"/></s:if><s:else><s:property value='#user.email' /></s:else>
									</option>
								</s:iterator>
							</select>
						</td>
					</s:if>
					<td>
						<select id="state">
							<option value="-1" <s:if test="state == -1">selected</s:if>><s:i18n name="com.hisupplier.cn.account.product.package"><s:text name="product.searchState" /></s:i18n></option>
							<option value="10" <s:if test="state == 10">selected</s:if>><s:text name="auditState.reject" /></option>
							<option value="14" <s:if test="state == 14">selected</s:if>><s:text name="auditState.auditing" /></option>
							<option value="15" <s:if test="state == 15">selected</s:if>><s:text name="auditState.wait" /></option>
							<option value="20" <s:if test="state == 20">selected</s:if>><s:text name="auditState.pass" /></option>
						</select>
					</td>
					<td><input type="submit" class="searchButton" value="<s:text name='button.search'/>"/></td>
				</tr>
			</table>
		</form>
		
		<div class="buttonLeft">
			<input type="button" onclick="javascript:deleteProduct();" value='<s:text name="button.removeProductFromGroup"/>' />
			
			<s:if test="result.group.groupId > 0">
				<input type="button" onclick="javascript:showGroupList();" value='<s:text name="button.removeToOtherProGroup"/>' />
			</s:if>
			<s:else>
				<input type="button" onclick="javascript:showGroupList();" value='<s:text name="button.removeToOtherProGroup"/>' />
			</s:else>
		</div>
		
		<form action="" name="productForm" method="post">
			<div id="listTable">
				<s:include value="/page/product/product_list_inc.jsp"/>
			</div>
			<input type="hidden" name="pageNo"/>
			<input type="hidden" name="groupId"/>
			<input type="hidden" name="newGroupId"/>
			<input type="hidden" name="groupName"/>
			<input type="hidden" name="queryText"/>
			<input type="hidden" name="state"/>
			<input type="hidden" name="userIdTmp"/>


		</form>
		
		<div id="normalGroupList" title='<s:text name="group.listTitle"/>'>
			<iframe src='/group/select_group_list.do' frameborder="no" scrolling="yes" style="width:100%; height:420px;*height:450px !important;*height:450px;"></iframe>
		</div>
	</body>
</html>
