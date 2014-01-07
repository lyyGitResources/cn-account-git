<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.hisupplier.commons.Config"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>
			<s:text name="group.title"/>
			<span class="gray">已创建<s:property value="result.groupList.size - 1"/>个组（数量不限）</span>
		</title>
		<script type="text/javascript">
			var groupTree = null;
			var addGroupUrl="";
			$(function (){
				$("#formDialog").dialog({
					bgiframe: true,
					autoOpen: false,
					height: 170,
					width: 580,
					modal: true,
					close: function(){
						$("#formDialog").empty();
					}
				});
				$('#groupNotice').dialog({
					autoOpen: false,
					height: 200,
					width: 530,
					buttons: {
						"<s:text name='button.cancel'/>": function() { 
							$(this).dialog("close"); 
						},
						"<s:text name='button.confirm'/>": function() { 
							$(this).dialog("close"); 
							showFormDialog (addGroupUrl,"<s:text name='group.createChildGroupTitle'/>");
						}
					}
				});
			});
			
			function showFormDialog (url,title) {
				$("#formDialog").html(AJAX_LOADING_CODE);
				$("#formDialog").load(url);
				$("#formDialog").dialog('option', 'title', title);
				$("#formDialog").dialog('open');
			}
			
			
			function showThirdChoose(url,title,depth){
				if(depth==2){	
					addGroupUrl=url;
					$('#groupNotice').dialog('open');
					return false;
				}else{
					showFormDialog (url,title);
				}
			}
			
			function showOptions(groupId){
				for(i=0; i < groupIDArray.length; i++){		
					$("#" + groupIDArray[i]).hide();
				}
				$("#"+groupId).show();
			}
			
			function deleteGroup(groupId){
				deleteConfirm("/group/group_delete.htm?groupId="+groupId);
			}
			
			function showNewIco(groupId,showNewIco) {
				$.ajax({
				  type: "POST",
				  url: "/group/update_group_ico.do?groupId="+groupId+"&showNewIco="+showNewIco,
				  cache: false,
				  dataType:"json",
				  success: function(data){
				  	 if(data.tip == "addSuccess"){
				  	 	if(showNewIco){
				  	 		$("#groupListNewIco"+groupId).html("<img src='<%=Config.getString("img.base") %>/img/ico/group_new.gif'/>");
				  	 		$("#groupNewIco"+groupId).html('<a href="javaScript:showNewIco('+groupId+',<%=false %>)"><s:text name="group.new"/></a>');
				  	 	}else{
				  	 		$("#groupListNewIco"+groupId).html("");
				  	 		$("#groupNewIco"+groupId).html('<a href="javaScript:showNewIco('+groupId+',<%=true %>)"><s:text name="group.new"/></a>');
				  	 	}
				  	 }
				  }
				}); 
			}
		</script>
	</head>
	<body>
		<div id="groupNotice" title="<s:text name='systemTips'/>">
			<span style="line-height:28px;font-weight:bold;font-size:14px;"><s:text name="group.addNotice"/></span>
		</div>
		<div class="pageTips">
			<s:text name="group.notice.tip"/>
		</div>
		<div class="buttonLeft">
			<input type="button" onclick="javascript:showFormDialog('/group/group_add.do?parentId=0','<s:text name="group.createFirstGroupTitle"/>');" value="<s:text name='button.addFirstGroup'/>" />
			<input type="button" onclick="window.location.href = '/group/group_order.htm?parentId=0'" value='<s:text name="button.orderFirstGroup"/>'/>
			<%-- 
			<input type="button" onclick="window.location.href = '/group/no_group_product_list.htm?groupId=0'" value='<s:text name="group.noGroupProduct"><s:param><s:property value="result.noGroupProductCount"/></s:param></s:text>' />
			<input type="button" onclick="window.location.href = '/group/no_group_trade_list.htm?groupId=0'" value='<s:text name="group.noGroupTrade"><s:param><s:property value="result.noGroupTradeCount"/></s:param></s:text>' />
			--%>
		</div>
		<div class="groupList">
			<div class="groupListLeft">
				<h2><s:text name="group.listTitle"/>（<s:text name="group.productCount"/>/<s:text name="group.tradeCount"/>）</h2>
				<div id="listTable">
					<s:include value="/page/group/group_list_inc.jsp"/>
				</div>
			</div>
			
			<div class="groupListRight">
				<h2><s:text name="group.optionTitle"/></h2>
				<s:iterator value="result.groupList" id="group" status="st">
					<s:property value="#group.option" escape="false"/>
				</s:iterator>
			</div>
		</div>
	
		<div id="formDialog"></div>
	</body>
</html>