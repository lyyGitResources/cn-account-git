<%@ page language="java" contentType="text/html; charset=UTF-8" errorPage="/page/error/500.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="trade.listTitle" />
			<s:if test="state > 0">
				<span class="red">（<s:text name="auditState.reject" />）</span>
			</s:if>
			<s:else>
				（
				<s:if test="loginUser.admin">
					<s:text name="trade.allProduct" />
				</s:if>
				<s:else>
					<s:text name="trade.myProduct" />
				</s:else>
				）
			</s:else>
		</title>
		<meta name="memoinfo" content="<s:property value='memoInfo'/>"/>
		<script type="text/javascript">
			$(document).ready(function () {
				// 搜索
				$("#searchForm").validate({
					submitHandler: function(){ 
						ListTable.reload({
							queryText: $("#queryText").val(),
							userId: $("#userId").val(),
							state: $("#state").val(),
							pageNo:1
						}, true);
					}
				});
				
				$("#delete").click(function(){
					if(!Util.isChecked("proId")){
						alert('<s:text name="trade.selectTrade"/>');
					}else{
						if(confirm('<s:text name="trade.deleteToRecycle"/>')){
							if(ListTable.params["state"]){
								$("#stateTmp").val(ListTable.params["state"]);
							}
							if(ListTable.params["userId"]){
								$("#userIdTmp").val(ListTable.params["userId"]);
							}
							if(ListTable.params["proType"]){
								$("#proTypeTmp").val(ListTable.params["proType"]);
							}
							if(ListTable.params["queryText"]){
								$("#queryTextTmp").val(ListTable.params["queryText"]);
							}
							$("#form").attr("action", "/trade/trade_delete.do");
							$("#form").submit();
						}
					}
				});
				$("#formDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 200,
					width: 380,
					modal: true
				});
				$("#allot").click(function(){
					$("#formDialog").dialog('option', 'title', '<s:text name="button.allotTrade"/>');
					$("#formDialog").dialog("open");
				});
				$("[name='userListName']").each(function(){
					$(this).click(function(){
						if(!Util.isChecked("proId")){
							alert('<s:text name="trade.selectTrade"/>');
						}else{
							$("#userListId").val($(this).attr("id"));
							if(ListTable.params["state"]){
								$("#stateTmp").val(ListTable.params["state"]);
							}
							if(ListTable.params["userId"]){
								$("#userIdTmp").val(ListTable.params["userId"]);
							}
							if(ListTable.params["proType"]){
								$("#proTypeTmp").val(ListTable.params["proType"]);
							}
							if(ListTable.params["queryText"]){
								$("#queryTextTmp").val(ListTable.params["queryText"]);
							}
							if(ListTable.params["pageNo"]){
								$("#pageNoTmp").val(ListTable.params["pageNo"]);
							}
							if(ListTable.params["pageSize"]){
								$("#pageSizeTmp").val(ListTable.params["pageSize"]);
							}
							$("#form").attr("action", "/trade/trade_change_user.do");
							$("#form").submit();
							
							// 解决IE6下，无法重定向问题，原因未知
							if($.browser.msie && $.browser.version == 6.0){							
								setTimeout("location.reload()", 1000);
								$("#formDialog").dialog("close");
							}
						}
					});
				});
				
			});

			function setModelValue(proType, tmpStateId, title){
				$('#proType').val(proType);
				$('#state').val(tmpStateId);

				if(tmpStateId == 10){
					$('#stateText').hide()
					
					$("#titleText").text("<s:text name='trade.listTitle' />");
					$("#redText").show();
					$("#redText").html("（" + title +"）");
				}else{
					$('#stateText').show()
					
					$("#redText").hide();
					$("#titleText").text("<s:text name='trade.listTitle' />（" + title + "）");
				}
			}
		</script>
	</head>
	<body>
		<div class="tabMenu" id="tabMenu">
			<ul>
			<s:if test="loginUser.admin">
				<li <s:if test="proType == -1 && state == -1">class="current"</s:if>><span onclick="tabMenu(1,{empty:true});setModelValue(-1, -1, '<s:text name="trade.allProduct" />');"><s:text name="trade.allProduct" />[<s:property value='result.company.tradeCount' />]</span></li>
				<li <s:if test="state == 10">class="current"</s:if>><span onclick="tabMenu(2,{state:10});setModelValue(-1, 10, '<s:text name="auditState.reject" />');"><s:text name="auditState.reject" />[<s:property value='result.company.tradeRejectCount' />]</span></li>
				<li <s:if test="proType == 1">class="current"</s:if>><span onclick="tabMenu(3,{proType:1});setModelValue(1, -1, '<s:text name="trade.sell" />');"><s:text name="trade.sell" />[<s:property value='result.tradeSellCount' />]</span></li>
				<li <s:if test="proType == 2">class="current"</s:if>><span onclick="tabMenu(4,{proType:2});setModelValue(2, -1, '<s:text name="trade.buy" />')"><s:text name="trade.buy" />[<s:property value='result.tradeBuyCount' />]</span></li>
				<li <s:if test="proType == -2">class="current"</s:if>><span onclick="tabMenu(5,{proType:-2});setModelValue(-2, -2, '过期商情')">过期商情[<s:property value='result.tradeDatedCount' />]</span></li>
			</s:if>
			<s:else>
				<li <s:if test="proType == -1 && state == -1">class="current"</s:if>><span onclick="tabMenu(1,{empty:true});setModelValue(-1, -1, '<s:text name="trade.myProduct" />');"><s:text name="trade.myProduct" /></span></li>
				<li <s:if test="state == 10">class="current"</s:if>><span onclick="tabMenu(2,{state:10});setModelValue(-1, 10, '<s:text name="auditState.reject" />');"><s:text name="auditState.reject" /></span></li>
				<li <s:if test="proType == 1">class="current"</s:if>><span onclick="tabMenu(3,{proType:1});setModelValue(1, -1, '<s:text name="trade.sell" />');"><s:text name="trade.sell" /></span></li>
				<li <s:if test="proType == 2">class="current"</s:if>><span onclick="tabMenu(4,{proType:2});setModelValue(2, -1, '<s:text name="trade.buy" />')"><s:text name="trade.buy" /></span></li>
				<li <s:if test="proType == -2">class="current"</s:if>><span onclick="tabMenu(5,{proType:-2});setModelValue(-2, -2, '过期商情')">过期商情</span></li>
			</s:else>
			</ul>
		</div>
		<script>$("#tabMenu li").click(function() {$("#userId").val(0);$("#state").val(-1);$("#queryText").val($("#queryText").attr("oriValue"))});</script>
		<form id="searchForm" class="searchForm">
			<table>
				<tbody>
					<tr>
						<td><input value="<s:text name='trade.queryText' />" id="queryText" type="text" style="width: 200px;" oriValue="<s:text name='trade.queryText' />"></td>
						<s:if test="result.userList != null && result.userList.size > 0">
							<td <s:if test="loginUser.admin == false">style="display:none"</s:if>>
								<select id="userId">
									<option value="0"><s:text name="search.userAll" /></option>
									<s:iterator value="result.userList" id="user">
										<option value="${user.userId }">${user.admin?"管理员":user.email }</option>
									</s:iterator>
								</select>
							</td>
						</s:if>
						<td id="stateText" <s:if test="state>0">style="display:none"</s:if>>
							<select id="state">
								<option value="-1" <s:if test="state == -1">selected</s:if> ><s:text name="product.searchState" /></option>
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
				</tbody>
			</table>
		</form>
		<form id="form" method="post">
			<input type="hidden" name="userId" id="userListId" />
			<input type="hidden" name="state" id="stateTmp" />
			<input type="hidden" name="userIdTmp" id="userIdTmp" />
			<input type="hidden" name="queryText" id="queryTextTmp" />
			<input type="hidden" name="pageNo" id="pageNoTmp" />
			<input type="hidden" name="pageSize" id="pageSizeTmp" />
			<input type="hidden" name="proType" id="proTypeTmp" />
			
			<div class="buttonLeft">
				<input type="button" id="delete" value="<s:text name="button.deleteTrade"/>" />
				<s:if test="loginUser.admin && result.userList != null && result.userList.size > 0">
					<input type="button" id="allot" value="<s:text name="button.allotTrade"/>"/>
				</s:if>
			</div>
				
			<div id="listTable">
				<s:include value="/page/product/trade_list_inc.jsp"/>
			</div>
		
			<div id="formDialog">
				<s:if test="result.userList != null && result.userList.size > 0">
					<table cellspacing="1" class="listTable" style="width:330px;">
						<tbody>
							<s:iterator value="result.userList" id="user">
								<s:if test="#user.userId != loginUser.userId">
									<tr>
										<td>
											<a href="#position;" id="<s:property value='#user.userId' />" name="userListName"><s:property value='#user.email' /></a>
										</td>
									</tr>
								</s:if>
							</s:iterator>
						</tbody>
					</table>
				</s:if>
			</div>
			
		</form>
	</body>
</html>
